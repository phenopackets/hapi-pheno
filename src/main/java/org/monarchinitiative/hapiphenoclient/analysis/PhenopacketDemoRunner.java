package org.monarchinitiative.hapiphenoclient.analysis;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.monarchinitiative.hapiphenoclient.examples.BethlemMyopathyExample;
import org.monarchinitiative.hapiphenoclient.examples.PhenoExample;
import org.monarchinitiative.hapiphenoclient.except.PhenoClientRuntimeException;
import org.monarchinitiative.hapiphenoclient.fhir.util.MyPractitioner;
import org.monarchinitiative.hapiphenoclient.phenopacket.Measurement;
import org.monarchinitiative.hapiphenoclient.phenopacket.Phenopacket;
import org.monarchinitiative.hapiphenoclient.phenopacket.PhenotypicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is intended to just do two simple operations with a FHIR server
 * as a sanity check
 */
@Component
public class PhenopacketDemoRunner {
    private final static Logger LOG = LoggerFactory.getLogger(PhenopacketDemoRunner.class);
    @Autowired
    private String hapiUrl;

    private final FhirContext ctx;

    private final LoggingInterceptor loggingInterceptor;

    public PhenopacketDemoRunner() {
        ctx = FhirContext.forR4();
        String propFile = "classpath:narrative.properties";
        CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator(propFile);

        ctx.setNarrativeGenerator(gen);
        loggingInterceptor = new LoggingInterceptor(true);
    }


    public Parameters searchForPhenopacketEverything(IIdType id) {
        org.hl7.fhir.r4.model.DateType dtBeg = new DateType("2019-11-01");
        org.hl7.fhir.r4.model.DateType dtEnd = new DateType("2023-02-02");
        Parameters inParams = new Parameters();
        {
           inParams.addParameter().setName("start").setValue(dtBeg);
           inParams.addParameter().setName("end").setValue(dtEnd);
        }
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        Parameters outParams =  client.operation()
                .onInstance(new IdDt("Composition", id.getIdPart()))
                .named("$everything")
                //.withNoParameters(Parameters.class) // No input parameters
                .withParameters(inParams)
                .useHttpGet() // Use HTTP GET instead of POST
                .execute();
        return outParams;
    }

    public Bundle searchForPhenopacketById(IIdType id) {
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        Bundle response = client.search()
                .forResource(Phenopacket.class)
                .where(Resource.RES_ID.exactly().code(id.getIdPart()))
                .include(new Include("Phenopacket/$everything"))
                .include(new Include("Observation/$everything"))
                .returnBundle(Bundle.class)
                .execute();
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        System.out.println(parser.encodeResourceToString(response));
        return response;
    }


    public List<PhenotypicFeature> retrievePhenotypicFeaturesFromBundle(Bundle patientBundle) {
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        List<Bundle.BundleEntryComponent> entries = patientBundle.getEntry();
        List<PhenotypicFeature> pfeatures = new ArrayList<>();
        for (var entry : entries) {
            if (entry.getResource() instanceof Composition) {
                Composition composition = (Composition) entry.getResource();
                for (Composition.SectionComponent c : composition.getSection()) {
                    if (c.getCode().getCodingFirstRep().getCode().equals("phenotypic_features")) {
                        // expect observations
                        List<Reference> reflist = c.getEntry();
                        for (Reference ref : reflist) {
                            String id = ref.getReference();
                            System.out.println("PF id = " + id);
                            PhenotypicFeature pf = client.read().resource(PhenotypicFeature.class).withId(id).execute();
                            pfeatures.add(pf);
                        }
                    }
                }
            }
        }
        return pfeatures;
    }

    public List<Measurement> retrieveMeasurementsFromBundle(Bundle patientBundle) {
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        List<Bundle.BundleEntryComponent> entries = patientBundle.getEntry();
        List<Measurement> measurementList = new ArrayList<>();
        for (var entry : entries) {
            if (entry.getResource() instanceof Composition) {
                Composition composition = (Composition) entry.getResource();
                for (Composition.SectionComponent c : composition.getSection()) {
                    if (c.getCode().getCodingFirstRep().getCode().equals("measurements")) {
                        // expect observations
                        List<Reference> reflist = c.getEntry();
                        for (Reference ref : reflist) {
                            String id = ref.getReference();
                            System.out.println("Measurement id = " + id);
                            Measurement pf = client.read().resource(Measurement.class).withId(id).execute();
                            measurementList.add(pf);
                        }
                    }
                }
            }
        }
        return measurementList;
    }


    public void searchByPatientId(IIdType id) {
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        Bundle response = client.search()
                .forResource(PhenotypicFeature.class)
                .where(Patient.FAMILY.matches().value("Smith"))
                .returnBundle(Bundle.class)
                .execute();

        System.out.println("Responses: " + response.getTotal());
        if (response.getTotal() == 0) {
            System.out.println("No reponses");
        } else {
            System.out.println("First response ID: " + response.getEntry().get(0).getResource().getId());
        }
    }







    public IIdType postResource(Resource pfeature) {
        LOG.info("Posting resource={}", pfeature);
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
       // System.out.println(parser.encodeResourceToString(pfeature));
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        try {
            MethodOutcome outcome = client
                    .create()
                    .resource(pfeature)
                    .execute();
            System.out.println(outcome.getId());
            return outcome.getId();
        } catch (ResourceNotFoundException e) {
            //404 means we can contact the server but the server does not have
            // the resource we want or does not want to disclose the information
            //int code = e.getStatusCode();
            String msg = String.format("Could not create individal: %s\n", e.getMessage());
            throw new PhenoClientRuntimeException(msg);
        }
    }


    private void prelims() {
        MyPractitioner williamHarvey = MyPractitioner.harvey();
        Practitioner practitioner = new Practitioner();
        practitioner.setId(williamHarvey.getId());
        HumanName humanName = new HumanName();
        humanName.setFamily(williamHarvey.getFamilyName());
        humanName.addGiven(williamHarvey.getGivenName());
        humanName.addSuffix(williamHarvey.suffix());
        List<HumanName> names = new ArrayList<>();
        names.add(humanName);
        practitioner.setName(names);

        MyPractitioner apgar = MyPractitioner.apgar();
        Practitioner practitioner2 = new Practitioner();
        practitioner2.setId(apgar.getId());
        HumanName humanName2 = new HumanName();
        humanName2.setFamily(apgar.getFamilyName());
        humanName2.addGiven(apgar.getGivenName());
        names = new ArrayList<>();
        names.add(humanName2);
        practitioner2.setName(names);
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        //client.registerInterceptor(loggingInterceptor);
        try {
            MethodOutcome outcome = client
                    .update()
                    .resource(practitioner)
                    .execute();
            System.out.println(outcome.getId());
            outcome = client
                    .update()
                    .resource(practitioner2)
                    .execute();
            System.out.println(outcome.getId());
        } catch (ResourceNotFoundException e) {
            //404 means we can contact the server but the server does not have
            // the resource we want or does not want to disclose the information
            //int code = e.getStatusCode();
            String msg = String.format("Could not create Practiioner: %s\n", e.getMessage());
            throw new PhenoClientRuntimeException(msg);
        }
    }


    public PhenoExample postBethlemClinicalExample() {
        prelims();
        BethlemMyopathyExample bethlem = new BethlemMyopathyExample();
        IIdType individualId = postResource(bethlem.individual());
        bethlem.setIndividualId(individualId);


        Phenopacket phenopacket = bethlem.phenopacket();
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        MethodOutcome methodOutcome = client.update().resource(phenopacket).execute();
        if (methodOutcome.getId() == null) {
            throw new PhenoClientRuntimeException("Could not retrieve Phenopacket ID from server");
        }
        IIdType phnenopacketId = methodOutcome.getId();
        bethlem.setPhenopacketId(phnenopacketId);
        List<PhenotypicFeature> phenotypicFeatureList = bethlem.phenotypicFeatureList();
        Composition.SectionComponent phenotypicFeaturesSection =
                new Composition.SectionComponent()
                        .setTitle("phenotypic_features")
                        .setCode(new CodeableConcept()
                                .addCoding(new Coding()
                                                .setCode("phenotypic_features")
                                                .setSystem("http://ga4gh.org/fhir/phenopackets/CodeSystem/SectionType")));
        phenopacket.addSection(phenotypicFeaturesSection);
        for (PhenotypicFeature pfeature : phenotypicFeatureList) {
            IIdType pfeatureId = postResource(pfeature);
            pfeature.setId(pfeatureId.getIdPart());
            phenotypicFeaturesSection.addEntry(new Reference(pfeature));
            client.update().resource(phenopacket).execute();
        }
        Composition.SectionComponent measurementSection =
                new Composition.SectionComponent()
                        .setTitle("measurements")
                        .setCode(new CodeableConcept()
                                .addCoding(new Coding()
                                        .setCode("measurements")
                                        .setSystem("http://ga4gh.org/fhir/phenopackets/CodeSystem/SectionType")));
        phenopacket.addSection(measurementSection);
        List<Measurement> measurementList = bethlem.measurementList();
        for (Measurement measurement : measurementList) {
            IIdType measurementId = postResource(measurement);
            measurement.setId(measurementId.getIdPart());
            measurementSection.addEntry(new Reference(measurement));
            client.update().resource(phenopacket).execute();
        }
        return bethlem;
    }



}

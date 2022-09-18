package org.monarchinitiative.hapiphenoclient.analysis;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.monarchinitiative.hapiphenoclient.examples.PhenopacketPoster;
import org.monarchinitiative.hapiphenoclient.fhir.util.MyPractitioner;
import org.monarchinitiative.hapiphenocore.except.PhenoClientRuntimeException;
import org.monarchinitiative.hapiphenocore.fhir_to_ga4gh.Ga4GhPhenopacket;
import org.monarchinitiative.hapiphenocore.phenopacket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PhenopacketRunner {

    private final static Logger LOG = LoggerFactory.getLogger(PhenopacketDemoRunner.class);
    @Autowired
    private String hapiUrl;

    private final FhirContext ctx;

    private PhenopacketPoster poster;

    private final LoggingInterceptor loggingInterceptor;

    public void setPoster(PhenopacketPoster p) {
        this.poster = p;
    }

    public PhenopacketRunner() {
        ctx = FhirContext.forR4();
        String propFile = "classpath:narrative.properties";
        CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator(propFile);

        ctx.setNarrativeGenerator(gen);
        loggingInterceptor = new LoggingInterceptor(true);
    }

    public Bundle searchForPhenopacketById(IIdType id) {
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        Bundle response = client.search()
                .forResource(org.monarchinitiative.hapiphenocore.phenopacket.Phenopacket.class)
                .where(Resource.RES_ID.exactly().code(id.getIdPart()))
                .returnBundle(Bundle.class)
                .execute();
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        //System.out.println(parser.encodeResourceToString(response));
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


    public IIdType putResource(Resource resourceArg) {
        LOG.info("Putting resource={}", resourceArg);
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        // System.out.println(parser.encodeResourceToString(resourceArg));
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);

        try {
            MethodOutcome methodOutcome = client
                    .update()
                    .resource(resourceArg)
                    .execute();
            //System.out.println("putResource() returned Id:" + methodOutcome.getId());
            System.out.println(methodOutcome.getOperationOutcome());
            System.out.println("============ OO");
            return methodOutcome.getId();
        } catch (Exception e) {
            String msg = String.format("Could not update resource: %s\n", e.getMessage());
            throw new PhenoClientRuntimeException(msg);
        }
    }


    public IIdType postResource(Resource resource) {
        LOG.info("Posting resource={}", resource);
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);

        System.out.println(parser.encodeResourceToString(resource));
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        try {
            MethodOutcome outcome = client
                    .create()
                    .resource(resource)
                    .execute();
            Thread.sleep(1000);
            System.out.println("postResource() returned Id: " + outcome.getId());
            return outcome.getId();
        } catch (ResourceNotFoundException e) {
            //404 means we can contact the server but the server does not have
            // the resource we want or does not want to disclose the information
            //int code = e.getStatusCode();
            String msg = String.format("Could not create resource (%s): %s\n", resource.toString(), e.getMessage());
            throw new PhenoClientRuntimeException(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new PhenoClientRuntimeException("Bad sleep");
        }
    }


    private void postPractitioners() {
        if (hapiUrl == null) {
            hapiUrl = "http://20.119.216.32:9999/fhir";
        }
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

    public PhenopacketPoster postPhenopacket() {
        System.out.println("\nPUT practitioners");
        postPractitioners();
        // Patient
        System.out.println("\nPOST patient");

        IIdType individualId = postResource(poster.individual());
        poster.setIndividualId(individualId);


        // Phenopacket
        System.out.println("\nPUT phenopacket/composition");
        Phenopacket fhirPhenopacket = poster.phenopacket();
        IIdType phenopacketId = postResource(fhirPhenopacket);
        if (phenopacketId == null) {
            throw new PhenoClientRuntimeException("Could not retrieve Phenopacket ID from server");
        }
        poster.setPhenopacketId(phenopacketId);

        // Features
        List<PhenotypicFeature> phenotypicFeatureList = poster.phenotypicFeatureList();
        Composition.SectionComponent phenotypicFeaturesSection =
                new Composition.SectionComponent()
                        .setTitle("phenotypic_features")
                        .setCode(new CodeableConcept()
                                .addCoding(new Coding()
                                        .setCode("phenotypic_features")
                                        .setSystem("http://ga4gh.org/fhir/phenopackets/CodeSystem/SectionType")));
        fhirPhenopacket.addSection(phenotypicFeaturesSection);
        int i=1;
        for (PhenotypicFeature pfeature : phenotypicFeatureList) {
            System.out.println("\nPOST observation PUT phenopacket/composition features " + i++);
            IIdType pfeatureId = postResource(pfeature);
            pfeature.setId(pfeatureId.getIdPart());
            phenotypicFeaturesSection.addEntry(new Reference(pfeature));
            putResource(fhirPhenopacket);
        }


        // Measurements
        List<Measurement> measurementList = poster.measurementList();
        Composition.SectionComponent measurementSection =
                new Composition.SectionComponent()
                        .setTitle("measurements")
                        .setCode(new CodeableConcept()
                                .addCoding(new Coding()
                                        .setCode("measurements")
                                        .setSystem("http://ga4gh.org/fhir/phenopackets/CodeSystem/SectionType")));

        fhirPhenopacket.addSection(measurementSection);
        int j=1;
        for (Measurement measurement : measurementList) {
            System.out.println("\nPOST measurement PUT phenopacket " + j++);
            IIdType measurementId = postResource(measurement);
            measurement.setId(measurementId.getIdPart());
            measurementSection.addEntry(new Reference(measurement));
            putResource(fhirPhenopacket);
        }


// TODO: cleanup
       /* System.out.println("\n(TODO) POST variant");
        PhenopacketsVariant variant = poster.createPhenopacketsVariant();
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        System.out.println("\nShow local version of report on variant");
          System.out.println("\nPhenopacketsVariant: " + parser.encodeResourceToString(variant));
        IIdType variantId = postResource(variant);

        PhenopacketsGenomicInterpretation genomicInterpretation = poster.addGenomicInterpretation(variant);
        System.out.println(parser.encodeResourceToString(genomicInterpretation));
        */





        return poster;
    }



// TODO: fetch patients from FHIR and show? ...using extractIndividual function below?
    /*
    List<Bundle.BundleEntryComponent> entries = patientBundle.getEntry();
        for (var entry : entries) {
            if (entry.getResource() instanceof Patient) {
                Patient patient = (Patient) entry.getResource();
     */

    public Individual extractIndividual(String individualId) {
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        Individual indi = client.read().resource(Individual.class).withId(individualId).execute();
        if (indi == null) {
            throw new PhenoClientRuntimeException("Could not retrieve individual with id "+ individualId);
        }
        return indi;
    }

    public static class FhirParts {
        // does java14 have some kind of struct/record for this? -- we are still at Java 11, rcords were Java 16
        private final Bundle bundle;
        private final Individual individual;
        private final List<PhenotypicFeature> features;
        private final List<Measurement> measurements;

        public FhirParts(Bundle bundle,
                         Individual individual,
                         List<PhenotypicFeature> features,
                         List<Measurement> measurements) {
            this.bundle = bundle;
            this.individual = individual;
            this.features = features;
            this.measurements = measurements;
        }

        public Bundle getBundle() { return bundle; }
        public Individual getIndividual() { return individual; }
        public List<PhenotypicFeature> getFeatures() { return features; }
        public List<Measurement> getMeasurements() {return measurements; }
    }


    public PhenopacketDemoRunner.FhirParts retrieveFhirParts() {
        // Retrieve packet (bundle, this is FHIR)from FHIR server
        System.out.println("\nApp:Retrieving phenopacket " + poster.getPhenopacketId().getIdPart());
        Bundle patientBundle = searchForPhenopacketById(poster.getPhenopacketId());
        System.out.println(patientBundle);

        System.out.println("*************************");
        System.out.println("\nApp:Fetch Individual from FHIR server"); //, xtract parts and create Phenopacket ");
        Individual individual = extractIndividual(poster.getUnqualifiedIndividualId());
        System.out.println("\nApp:Fetch Features from FHIR server"); //, xtract parts and create Phenopacket ");
        List<PhenotypicFeature> features = retrievePhenotypicFeaturesFromBundle(patientBundle);
        System.out.println("\nApp:Fetch Measurements from FHIR server"); //, xtract parts and create Phenopacket ");
        List<Measurement> measurements = retrieveMeasurementsFromBundle(patientBundle);

        return(new PhenopacketDemoRunner.FhirParts(patientBundle, individual, features, measurements));
    }

    public org.phenopackets.schema.v2.Phenopacket assemblePhenopacket(PhenopacketDemoRunner.FhirParts fhirParts) {
        System.out.println("\nCreate phenopacket protobuf");
        org.phenopackets.schema.v2.Phenopacket ga4ghPhenopacket = Ga4GhPhenopacket.fromFhir(fhirParts.getIndividual(),
                fhirParts.getFeatures(), fhirParts.getMeasurements() );
        return ga4ghPhenopacket;

    }
}

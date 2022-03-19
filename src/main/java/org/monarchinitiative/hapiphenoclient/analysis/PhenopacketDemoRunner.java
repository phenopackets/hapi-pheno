package org.monarchinitiative.hapiphenoclient.analysis;

import ca.uhn.fhir.context.FhirContext;
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
                ;
        return outParams;






    }

        public Bundle searchForPatient(IIdType id) {
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        Bundle response = client.search()
                .forResource(Patient.class)
                .where(Resource.RES_ID.exactly().code(id.getIdPart()))
                .returnBundle(Bundle.class)
                .execute();
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        System.out.println(parser.encodeResourceToString(response));
        return response;
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
        System.out.println(parser.encodeResourceToString(pfeature));
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
            System.out.println(outcome.getId());
            outcome = client
                    .update()
                    .resource(practitioner2)
                    .execute();
            System.out.println(outcome.getId());
            System.out.println(outcome.getId());
            //outcome.getResource().ge

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
        List<PhenotypicFeature> phenotypicFeatureList = bethlem.phenotypicFeatureList();
        for (PhenotypicFeature pfeature : phenotypicFeatureList) {
            IIdType pfeatureId = postResource(pfeature);
            pfeature.setId(pfeatureId);
        }

        List<Measurement> measurementList = bethlem.measurementList();
        for (Measurement measurement : measurementList) {
            IIdType measurementId = postResource(measurement);
//            measurement.setId(measurementId);
        }

       Phenopacket phenopacket = bethlem.phenopacket();
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        MethodOutcome methodOutcome = client.update().resource(phenopacket).execute();
        if (methodOutcome.getId() == null) {
            throw new PhenoClientRuntimeException("Could not retrieve Phenopacket ID from server");
        }
        IIdType phnenopacketId = methodOutcome.getId();
        bethlem.setPhenopacketId(phnenopacketId);

        return bethlem;
    }



}

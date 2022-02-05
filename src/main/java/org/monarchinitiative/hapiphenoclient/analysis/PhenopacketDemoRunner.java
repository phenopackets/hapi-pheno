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
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.monarchinitiative.hapiphenoclient.examples.BethlemMyopathyExample;
import org.monarchinitiative.hapiphenoclient.except.PhenoClientRuntimeException;
import org.monarchinitiative.hapiphenoclient.phenopacket.Individual;
import org.monarchinitiative.hapiphenoclient.phenopacket.KaryotypicSexExtension;
import org.monarchinitiative.hapiphenoclient.phenopacket.Measurement;
import org.monarchinitiative.hapiphenoclient.phenopacket.PhenotypicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public IIdType createPatient() {
        LOG.info("ABout to create patieht, hapiURL={}", hapiUrl);
        Patient pat = new Patient();
        pat.addName().setFamily(" Simpson"). addGiven(" Homer"). addGiven("J");
        pat.addIdentifier().setSystem("http://acme.org/MRNs"). setValue("42");
        ContactPoint contact = pat.addTelecom();
        contact.setUse(ContactPoint.ContactPointUse.HOME);
        contact.setSystem(ContactPoint.ContactPointSystem.PHONE);
        contact.setValue("1 (416) 340 4800");
        pat.setGender(Enumerations.AdministrativeGender.MALE);
        // first dump tp shell
        // Create a JSON parser
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        System.out.println(parser.encodeResourceToString(pat));
        // Now post resource to server
        String myUrl = "http://localhost:8888/fhir/";
        IGenericClient client = ctx.newRestfulGenericClient(myUrl);
        try {
            MethodOutcome outcome = client
                    .create()
                    .resource(pat)
                    .execute();
            System.out.println(outcome .getId());
            return outcome.getId();
        } catch (ResourceNotFoundException e) {
            //404 means we can contact the server but the server does not have
            // the resource we want or does not want to disclose the information
            int code = e.getStatusCode();
            System.out.printf("Could not create patient. HTTP Status code: %d: %s\n",
                    code, e.getMessage());
        }
// Print the ID of the newly created resource

        return null;
    }


    public void searchForPatient(IIdType id) {
        IGenericClient  client = ctx .newRestfulGenericClient(this.hapiUrl);
        Bundle response = client.search()
                .forResource(Patient.class)
                .where(Patient.NAME.matches().value("Simpson"))
                .returnBundle(Bundle.class)
                .execute();

        System.out.println("Responses: " + response.getTotal());
        System.out.println("First response ID: " + response .getEntry().get(0).getResource().getId());
    }


    public void searchForAnything() {
        IGenericClient  client = ctx .newRestfulGenericClient(this.hapiUrl);
        Bundle response = client.search()
                .forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        System.out.println("Responses: " + response.getTotal());
        System.out.println("First response ID: " + response .getEntry().get(0).getResource().getId());
    }


    public Measurement createMeasurement() {
        Measurement obs = new Measurement();

            obs.setId("obs-example-age-weight-"+ 2022 +"-"+3);
            obs.setSubject(new Reference().setReference("Patient/123"));
            obs.setStatus(ObservationStatus.FINAL);
            Calendar when = Calendar.getInstance();
            when.add(Calendar.YEAR, 2022);
            when.add(Calendar.MONTH, 3);
            obs.setEffective(new DateTimeType(when));
            obs.getCode().addCoding().setCode("29463-7").setSystem("http://loinc.org");
            obs.setValue(new Quantity());
            obs.getValueQuantity().setCode("kg");
            obs.getValueQuantity().setSystem("http://unitsofmeasure.org");
            obs.getValueQuantity().setUnit("kg");
            obs.getValueQuantity().setValue(new BigDecimal(23));
            return obs;

    }

    public IIdType postMeasurementToServer(Measurement m) {
        IGenericClient  client = ctx .newRestfulGenericClient(this.hapiUrl);
        client.registerInterceptor(loggingInterceptor);
        MethodOutcome
                outcome = client
                .create()
                .resource( m)
                .execute();
        return outcome.getId();
    }


    public IIdType postPf() {
        PhenotypicFeature pf = new PhenotypicFeature();
        pf.setId("id");
        IGenericClient  client = ctx .newRestfulGenericClient(this.hapiUrl);
        client.registerInterceptor(loggingInterceptor);
        MethodOutcome
                outcome = client
                .create()
                .resource(pf)
                .execute();
        return outcome.getId();
    }


    public IIdType postIndividual(Individual individual) {
        LOG.info("Posting individual={}", individual);
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        System.out.println(parser.encodeResourceToString(individual));

        Patient individual2 = new Patient();
        individual2.setId("id.1");
        individual2.setGender(Enumerations.AdministrativeGender.MALE);
        Date birthdate = new GregorianCalendar(2007, Calendar.FEBRUARY, 11).getTime();
        individual2.setBirthDate(birthdate);
        IGenericClient  client = ctx .newRestfulGenericClient(this.hapiUrl);
        client.registerInterceptor(loggingInterceptor);
        try {
            MethodOutcome outcome = client
                    .create()
                    .resource(individual2)
                    .execute();
            System.out.println(outcome .getId());
            return outcome.getId();
        } catch (ResourceNotFoundException e) {
            //404 means we can contact the server but the server does not have
            // the resource we want or does not want to disclose the information
            //int code = e.getStatusCode();
            String msg = String.format("Could not create individal: %s\n", e.getMessage());
            throw new PhenoClientRuntimeException(msg);
        }
    }


    public IIdType postBethlemClinicalExample() {
        BethlemMyopathyExample bethlem = new BethlemMyopathyExample();
        IIdType individualId = postIndividual(bethlem.individual());
        System.out.println("Bethlem individual id: " + individualId);
        return individualId;
    }


}

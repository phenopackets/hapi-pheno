package org.monarchinitiative.hapiphenoclient.analysis;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is intended to just do two simple operations with a FHIR server
 * as a sanity check
 */
@Component
public class SimpleHandShake {
    private final static Logger LOG = LoggerFactory.getLogger(SimpleHandShake.class);
    @Autowired
    private String hapiUrl;

    private final FhirContext ctx;

    public SimpleHandShake() {
        ctx = FhirContext.forR4();
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

        IGenericClient client = ctx.newRestfulGenericClient(hapiUrl);
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
        IGenericClient  client = ctx . newRestfulGenericClient(this.hapiUrl);
        Bundle response = client.search()
                .forResource(Patient.class)
                .where(Patient.NAME.matches().value("Simpson"))
                .returnBundle(Bundle.class)
                .execute();

        System.out.println("Responses: " + response.getTotal());
        System.out.println("First response ID: " + response .getEntry().get(0).getResource().getId());
    }
}

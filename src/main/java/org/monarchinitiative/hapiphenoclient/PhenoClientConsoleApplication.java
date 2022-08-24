package org.monarchinitiative.hapiphenoclient;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.hl7.fhir.r4.model.Bundle;
import org.monarchinitiative.hapiphenoclient.analysis.PhenopacketDemoRunner;
import org.monarchinitiative.hapiphenoclient.examples.PhenoExample;
import org.monarchinitiative.hapiphenoclient.ga4gh.Ga4GhPhenopacket;
import org.monarchinitiative.hapiphenoclient.phenopacket.Individual;
import org.monarchinitiative.hapiphenoclient.phenopacket.Measurement;
import org.monarchinitiative.hapiphenoclient.phenopacket.PhenotypicFeature;
import org.phenopackets.schema.v2.Phenopacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class PhenoClientConsoleApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory
            .getLogger(PhenoClientConsoleApplication.class);


    @Autowired
    PhenopacketDemoRunner demoRunner;

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(PhenoClientConsoleApplication.class, args);
        LOG.info("APPLICATION FINISHED");
        System.exit(0);
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");

        // Create and post Bethlem data to FHIR server
        PhenoExample bethlem = demoRunner.postBethlemClinicalExample();


        // Retrieve packet from FHIR server
        System.out.println("\nApp:Retrieving phenopacket " + bethlem.getPhenopacketId().getIdPart());
        Bundle patientBundle = demoRunner.searchForPhenopacketById(bethlem.getPhenopacketId());
        System.out.println(patientBundle);


        // extract parts from FHIR Bundle & create Phenopacket
        System.out.println("*************************");
        System.out.println("\nApp:Fetch Individual from FHIR server"); //, xtract parts and create Phenopacket ");
        Individual individual = demoRunner.extractIndividual(bethlem.getUnqualifiedIndividualId());
        System.out.println("\nApp:Fetch Features from FHIR server"); //, xtract parts and create Phenopacket ");
        List<PhenotypicFeature> features = demoRunner.retrievePhenotypicFeaturesFromBundle(patientBundle);
        System.out.println("\nApp:Fetch Measurements from FHIR server"); //, xtract parts and create Phenopacket ");
        List<Measurement> measurements = demoRunner.retrieveMeasurementsFromBundle(patientBundle);

        // TODO: could you have retreived a bundle, or the whole (?) composition from the FHIR server with a single call?
        // I don't know the structures well enough yet to ask.

        System.out.println("\nCreate phenopacket protobuf");
        Phenopacket ga4ghPhenopacket = Ga4GhPhenopacket.fromFhir(individual, features, measurements);

        System.out.println("\nApp:Show phenopacket contents");
        try {
            String json = JsonFormat.printer().print(ga4ghPhenopacket);
            System.out.println(json);
        } catch (InvalidProtocolBufferException ipe) {
            ipe.printStackTrace();
        }

    }


}

package org.monarchinitiative.hapiphenoclient;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.monarchinitiative.hapiphenoclient.analysis.PhenopacketDemoRunner;
import org.monarchinitiative.hapiphenoclient.examples.PhenoExample;
import org.monarchinitiative.hapiphenoclient.ga4gh.IndividualFactory;
import org.monarchinitiative.hapiphenoclient.phenopacket.Measurement;
import org.monarchinitiative.hapiphenoclient.phenopacket.PhenotypicFeature;
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
        PhenoExample bethlem = demoRunner.postBethlemClinicalExample();
        System.out.println("Retrieving phenopacket " + bethlem.getPhenopacketId().getIdPart());
        Bundle patientBundle = demoRunner.searchForPhenopacketById(bethlem.getPhenopacketId());
        System.out.println(patientBundle);
        List<Bundle.BundleEntryComponent> entries = patientBundle.getEntry();
        for (var entry : entries) {
            System.out.println("Entry" + entry);
            if (entry.getResource() instanceof Patient) {
                Patient patient = (Patient) entry.getResource();
                org.phenopackets.schema.v2.core.Individual ga4ghIndividual = IndividualFactory.toGa4gh(patient);
                System.out.println("FHIR Patient: " + patient.getId());
                System.out.println(ga4ghIndividual);
            }
        }
        System.out.println("*************************");
        // get the PhenotypicFeature elements
        List<PhenotypicFeature> features = demoRunner.retrievePhenotypicFeaturesFromBundle(patientBundle);
        for (PhenotypicFeature phenotypicFeature : features) {
            System.out.println("PF" + phenotypicFeature);
        }
        List<Measurement> measurements = demoRunner.retrieveMeasurementsFromBundle(patientBundle);
        for (Measurement measurement : measurements) {
            System.out.println("Measurment" + measurement);
        }
    }


}

package org.monarchinitiative.hapiphenoclient;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.monarchinitiative.hapiphenoclient.analysis.PhenopacketDemoRunner;

import org.monarchinitiative.hapiphenoclient.analysis.PhenopacketDemoRunner.FhirParts;

import org.monarchinitiative.hapiphenoclient.examples.BethlemMyopathyExample;
import org.monarchinitiative.hapiphenoclient.examples.PhenoExample;

import org.monarchinitiative.hapiphenoclient.ga4gh.Ga4GhPhenopacket;

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


        demoRunner.postToFhir();
        FhirParts fhirParts = demoRunner.retrieveFhirParts();
        org.phenopackets.schema.v2.Phenopacket ga4ghPhenopacket = demoRunner.assemblePhenopacket(fhirParts);

        System.out.println("\nApp:Show phenopacket contents");
        // TODO: cleanup
/*
        PhenoExample bethlem = new BethlemMyopathyExample();
        demoRunner.setPhenoExample(bethlem);
        demoRunner.postClinicalExample();
        System.out.println("Retrieving phenopacket " + bethlem.getPhenopacketId().getIdPart());
        Bundle patientBundle = demoRunner.searchForPhenopacketById(bethlem.getPhenopacketId());
        System.out.println(patientBundle);
        System.out.println("*************************");
        Individual individual = demoRunner.extractIndividual(bethlem.getUnqualifiedIndidualId());
        List<PhenotypicFeature> features = demoRunner.retrievePhenotypicFeaturesFromBundle(patientBundle);
        List<Measurement> measurements = demoRunner.retrieveMeasurementsFromBundle(patientBundle);
        Phenopacket ga4ghPhenopacket = Ga4GhPhenopacket.fromFhir(individual, features, measurements);
*/
        try {
            String json = JsonFormat.printer().print(ga4ghPhenopacket);
            System.out.println(json);
        } catch (InvalidProtocolBufferException ipe) {
            ipe.printStackTrace();
        }

    }


}

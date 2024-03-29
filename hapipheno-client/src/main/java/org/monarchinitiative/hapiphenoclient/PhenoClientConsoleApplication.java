package org.monarchinitiative.hapiphenoclient;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.monarchinitiative.hapiphenoclient.analysis.FhirParts;
import org.monarchinitiative.hapiphenoclient.analysis.PhenopacketDemoRunner;


import org.monarchinitiative.hapiphenoclient.analysis.PhenopacketJsonFileRunner;
import org.monarchinitiative.hapiphenoclient.analysis.PhenopacketRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;


@SpringBootApplication
public class PhenoClientConsoleApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory
            .getLogger(PhenoClientConsoleApplication.class);


    @Autowired
    PhenopacketDemoRunner demoRunner;

    @Autowired
    PhenopacketJsonFileRunner jsonFileRunner;

    @Autowired
    PhenopacketRunner runner;



    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(PhenoClientConsoleApplication.class, args);
        LOG.info("APPLICATION FINISHED");
        System.exit(0);
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");
        if (args.length == 1) {
            Path path = Paths.get(args[0]);
            jsonFileRunner.postJsonFile(path);

            runner.setPoster(jsonFileRunner);
           // runner.postPhenopacket();
            FhirParts fhirParts = runner.retrieveFhirParts();
            org.phenopackets.schema.v2.Phenopacket ga4ghPhenopacket = runner.assemblePhenopacket(fhirParts);

            System.out.println("\nApp:Show phenopacket contents");

            try {
                String json = JsonFormat.printer().print(ga4ghPhenopacket);
                System.out.println(json);
            } catch (InvalidProtocolBufferException ipe) {
                ipe.printStackTrace();
            }
        } else {
          /*
           TODO REFACOTR DEMO RUNNER

           demoRunner.postToFhir();
            FhirParts fhirParts = demoRunner.retrieveFhirParts();
            org.phenopackets.schema.v2.Phenopacket ga4ghPhenopacket = demoRunner.assemblePhenopacket(fhirParts);

            System.out.println("\nApp:Show phenopacket contents");

            try {
                String json = JsonFormat.printer().print(ga4ghPhenopacket);
                System.out.println(json);
            } catch (InvalidProtocolBufferException ipe) {
                ipe.printStackTrace();
            } */
        }



    }


}

package org.monarchinitiative.hapiphenoclient;

import org.hl7.fhir.instance.model.api.IIdType;
import org.monarchinitiative.hapiphenoclient.analysis.PhenoClient;
import org.monarchinitiative.hapiphenoclient.analysis.PhenopacketDemoRunner;
import org.monarchinitiative.hapiphenoclient.phenopacket.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PhenoClientConsoleApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(PhenoClientConsoleApplication.class);

    @Autowired
    PhenoClient client;

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

        for (int i = 0; i < args.length; ++i) {
            LOG.info("args[{}]: {}", i, args[i]);
        }
        System.out.println(client.getUrl());
        //client.connect();
        //client.searchForPhenopackets();
//        IIdType id = demoRunner.createPatient();
//        //simpleHandShake.searchForPatient(id);
//        //simpleHandShake.searchForAnything();
//        Measurement m = demoRunner.createMeasurement();
        //IIdType m_id = demoRunner.postMeasurementToServer(m);
        //System.out.println(m_id);
        //demoRunner.postPf();

        demoRunner.postBethlemClinicalExample();

    }
}

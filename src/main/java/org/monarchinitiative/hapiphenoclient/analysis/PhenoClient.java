package org.monarchinitiative.hapiphenoclient.analysis;


import org.monarchinitiative.hapiphenoclient.fhir.FhirIgConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;

@Component
public class PhenoClient {
    private final static Logger LOG = LoggerFactory.getLogger(PhenoClient.class);
    @Autowired
    private String hapiUrl;

    @Autowired Path igPath;


    public PhenoClient() {
    }

    public String getUrl() {
        return hapiUrl;
    }

    public void connect() {
        LOG.info("FHIR IG Path: {}", igPath);
        LOG.info("HAPI URL: {}", hapiUrl);
        FhirIgConnector connector = new FhirIgConnector(hapiUrl, igPath);
        try {
            BufferedWriter stdout = new BufferedWriter(new OutputStreamWriter(System.out));
            connector.printStatus(stdout);
            stdout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.monarchinitiative.hapiphenoclient.analysis;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.monarchinitiative.hapiphenoclient.fhir.FhirIgConnector;
import org.monarchinitiative.hapiphenoclient.phenopacket.PhenotypicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;

import static org.hl7.fhir.r4.model.Patient.BIRTHDATE;

@Component
public class PhenoClient {
    private final static Logger LOG = LoggerFactory.getLogger(PhenoClient.class);
    @Autowired
    private String hapiUrl;

    @Autowired
    Path igPath;

    private final FhirContext ctx;


    public PhenoClient() {
        ctx = FhirContext.forR4();
    }

    public void searchForPhenopackets() {
        IGenericClient client = ctx.newRestfulGenericClient(hapiUrl);
        Bundle response = client.search()
                .forResource(PhenotypicFeature.class)
                .count(100)
                .returnBundle(Bundle.class)
                .execute();
        System.out.println("Responses: " + response.getTotal());
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

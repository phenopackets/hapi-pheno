package org.monarchinitiative.hapiphenoclient.analysis;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.protobuf.util.JsonFormat;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.monarchinitiative.hapiphenoclient.examples.PhenopacketPoster;
import org.monarchinitiative.hapiphenocore.except.PhenoClientRuntimeException;
import org.monarchinitiative.hapiphenocore.ga4gh_to_fhir.IndividualToFhirPatient;
import org.monarchinitiative.hapiphenocore.ga4gh_to_fhir.MeasurementToFhir;
import org.monarchinitiative.hapiphenocore.ga4gh_to_fhir.PhenotypicFeatureToObservation;
import org.monarchinitiative.hapiphenocore.phenopacket.*;
import org.phenopackets.schema.v2.Phenopacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class PhenopacketJsonFileRunner implements PhenopacketPoster  {
    private final static Logger LOG = LoggerFactory.getLogger(PhenopacketDemoRunner.class);
    @Value("${hapi.server}")
    private String hapiUrl;

    private final FhirContext ctx;


    private Individual patient = null;

    private IIdType individualId = null;

    private IIdType phenopacketId = null;

    private List<org.phenopackets.schema.v2.core.PhenotypicFeature> phenotypicFeatureList;

    private List<org.phenopackets.schema.v2.core.Measurement> measurementList;

    private org.monarchinitiative.hapiphenocore.phenopacket.Phenopacket fhirPhenopacket = null;


    public PhenopacketJsonFileRunner() {
        ctx = FhirContext.forR4();
        String propFile = "classpath:narrative.properties";
        CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator(propFile);

        ctx.setNarrativeGenerator(gen);
    }

    public void postJsonFile(Path path) {
        var builder = Phenopacket.newBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            JsonFormat.parser().ignoringUnknownFields().merge(reader, builder);
        } catch (IOException e) {
            System.err.println("Error! Unable to read input file, " + e.getMessage() + "\nPlease check the format of file " + path);
            return;
        }
        var v2Phenopacket = builder.build();
        patient = IndividualToFhirPatient.convert(v2Phenopacket.getSubject());
        IIdType indId = postResource(patient);
        setIndividualId(indId);

        this.fhirPhenopacket
                = new org.monarchinitiative.hapiphenocore.phenopacket.Phenopacket();
        this.fhirPhenopacket.setIdentifier(v2Phenopacket.getId());
        fhirPhenopacket.setIndividual(patient);
        phenotypicFeatureList = v2Phenopacket.getPhenotypicFeaturesList();
        for (var pf : v2Phenopacket.getPhenotypicFeaturesList()) {
            PhenotypicFeature fhirPhenoFeature = PhenotypicFeatureToObservation.convert(pf, individualId.getIdPart());
            IIdType phId = postResource(fhirPhenoFeature);
            fhirPhenopacket.addPhenotypicFeature(new Reference("Observation/" + phId.getIdPart()));
        }

        measurementList = v2Phenopacket.getMeasurementsList();
        for (var measurement : v2Phenopacket.getMeasurementsList()) {
            var fhirMeasurement = MeasurementToFhir.convert(measurement);
            IIdType measurementId = postResource(fhirMeasurement);
            fhirPhenopacket.addMeasurement(new Reference("Observation/" + measurementId.getIdPart()));
        }
        IIdType phenopacketId = postResource(fhirPhenopacket);
        fhirPhenopacket.setId(phenopacketId);
        setPhenopacketId(phenopacketId);
    }


    public IIdType postResource(Resource resource) {
        LOG.info("Posting resource={}", resource);
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);

        System.out.println(parser.encodeResourceToString(resource));
        IGenericClient client = ctx.newRestfulGenericClient(this.hapiUrl);
        try {
            MethodOutcome outcome = client
                    .create()
                    .resource(resource)
                    .execute();
            //Thread.sleep(100);
            System.out.println("postResource() returned Id: " + outcome.getId());
            return outcome.getId();
        } catch (ResourceNotFoundException e) {
            //404 means we can contact the server but the server does not have
            // the resource we want or does not want to disclose the information
            //int code = e.getStatusCode();
            String msg = String.format("Could not create resource (%s): %s\n", resource.toString(), e.getMessage());
            throw new PhenoClientRuntimeException(msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PhenoClientRuntimeException("Bad sleep");
        }
    }


    @Override
    public org.monarchinitiative.hapiphenocore.phenopacket.Phenopacket phenopacket() {
        return this.fhirPhenopacket;
    }

    @Override
    public Individual individual() {
        return patient;
    }

    @Override
    public org.monarchinitiative.hapiphenocore.phenopacket.Phenopacket modifyPhenopacket(org.monarchinitiative.hapiphenocore.phenopacket.Phenopacket p) {
        return null;
    }

    @Override
    public void setIndividualId(IIdType individualId) {
        this.individualId = individualId;
    }

    @Override
    public void setPhenopacketId(IIdType phenopacketId) {
        this.phenopacketId = phenopacketId;
    }

    @Override
    public IIdType getPhenopacketId() {
        return this.phenopacketId;
    }

    @Override
    public String getUnqualifiedIndividualId() {
        return this.individualId.getIdPart();
    }

    @Override
    public PhenopacketsVariant createPhenopacketsVariant() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PhenopacketsGenomicInterpretation addGenomicInterpretation(PhenopacketsVariant variant) {
        return null;
    }

    @Override
    public List<PhenotypicFeature> phenotypicFeatureList() {
        return List.of(); //this.fhirPhenopacket.getPhenopacketList();
    }

    @Override
    public List<Measurement> measurementList() {
        return List.of();
    }
}

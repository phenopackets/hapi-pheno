package org.monarchinitiative.hapiphenoclient.examples;

import org.hl7.fhir.instance.model.api.IIdType;
import org.monarchinitiative.hapiphenocore.phenopacket.*;

import java.util.List;

/**
 * Create an examples to demonstrate how to
 * create Phenopacket PhenotypicFeature elements with this FHIR IG. The
 * demo command will store and retrieve these and other elements from our
 * FHIR server.
 * @author Peter N Robinson
 */
public interface PhenoExample {

    Phenopacket phenopacket();

    Individual individual();

    Phenopacket modifyPhenopacket(Phenopacket p);

    void setIndividualId(IIdType individualId);
    void setPhenopacketId(IIdType phenopacketId);

    IIdType getPhenopacketId();
    String getUnqualifiedIndividualId();
    PhenopacketsVariant createPhenopacketsVariant();
    PhenopacketsGenomicInterpretation addGenomicInterpretation(PhenopacketsVariant variant);

    List<PhenotypicFeature> phenotypicFeatureList();
    List<Measurement> measurementList();







}

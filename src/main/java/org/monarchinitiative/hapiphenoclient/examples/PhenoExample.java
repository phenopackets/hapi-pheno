package org.monarchinitiative.hapiphenoclient.examples;

import org.hl7.fhir.instance.model.api.IIdType;
import org.monarchinitiative.hapiphenoclient.phenopacket.Phenopacket;
import org.monarchinitiative.hapiphenoclient.phenopacket.PhenopacketsVariant;

/**
 * Create an examples to demonstrate how to
 * create Phenopacket PhenotypicFeature elements with this FHIR IG. The
 * demo command will store and retrieve these and other elements from our
 * FHIR server.
 * @author Peter N Robinson
 */
public interface PhenoExample {

    Phenopacket phenopacket();

    Phenopacket modifyPhenopacket(Phenopacket p);

    void setIndividualId(IIdType individualId);
    void setPhenopacketId(IIdType phenopacketId);

    IIdType getPhenopacketId();
    String getUnqualifiedIndidualId();
    PhenopacketsVariant createPhenopacketsVariant();







}

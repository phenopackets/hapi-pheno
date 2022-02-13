package org.monarchinitiative.hapiphenoclient.examples;

import org.monarchinitiative.hapiphenoclient.phenopacket.Phenopacket;

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







}

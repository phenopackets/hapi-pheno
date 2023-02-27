package org.monarchinitiative.hapiphenoclient.analysis;


import org.hl7.fhir.r4.model.Bundle;
import org.monarchinitiative.hapiphenocore.fhir_to_ga4gh.Ga4GhPhenopacket;
import org.monarchinitiative.hapiphenocore.phenopacket.Individual;
import org.monarchinitiative.hapiphenocore.phenopacket.Measurement;
import org.monarchinitiative.hapiphenocore.phenopacket.PhenotypicFeature;

import java.util.List;

public class FhirParts {
    // does java14 have some kind of struct/record for this? -- we are still at Java 11, rcords were Java 16
    private final Bundle bundle;
    private final Individual individual;
    private final List<PhenotypicFeature> features;
    private final List<Measurement> measurements;

    public FhirParts(Bundle bundle,
                     Individual individual,
                     List<PhenotypicFeature> features,
                     List<Measurement> measurements) {
        this.bundle = bundle;
        this.individual = individual;
        this.features = features;
        this.measurements = measurements;
    }

    public Bundle getBundle() { return bundle; }
    public Individual getIndividual() { return individual; }
    public List<PhenotypicFeature> getFeatures() { return features; }
    public List<Measurement> getMeasurements() {return measurements; }
}
//    public void postToFhir() {
//        // Create and post Bethlem data to FHIR server
//        bethlem = postBethlemClinicalExample();
//    }



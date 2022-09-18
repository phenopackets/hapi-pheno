package org.monarchinitiative.hapiphenocore.ga4gh_to_fhir;

import org.monarchinitiative.hapiphenocore.phenopacket.Disease;

public class DiseaseToFhirDisease {

    public static Disease convert(org.phenopackets.schema.v2.core.Disease disease, String patId) {
        //disease.

        Disease fhirDisease = new Disease();

        return fhirDisease;
    }

}

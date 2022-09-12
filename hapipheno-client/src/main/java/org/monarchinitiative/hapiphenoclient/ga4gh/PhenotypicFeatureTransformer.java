package org.monarchinitiative.hapiphenoclient.ga4gh;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

public class PhenotypicFeatureTransformer {


    public static PhenotypicFeature toGa4gh(org.monarchinitiative.hapiphenoclient.phenopacket.PhenotypicFeature fhirPhenotypicFeature) {
        PhenotypicFeature.Builder builder = PhenotypicFeature.newBuilder();
        CodeableConcept codeableConcept = fhirPhenotypicFeature.getCode();
        String hpoConceptId = codeableConcept.getCodingFirstRep().getCode();
        String hpoLabel = codeableConcept.getCodingFirstRep().getDisplay();
        builder.setType(OntologyClass.newBuilder().setId(hpoConceptId).setLabel(hpoLabel).build());
        return builder.build();
    }
}

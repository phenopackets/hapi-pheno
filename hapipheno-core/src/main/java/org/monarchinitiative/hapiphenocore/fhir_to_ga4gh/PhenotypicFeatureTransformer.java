package org.monarchinitiative.hapiphenocore.fhir_to_ga4gh;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.List;

public class PhenotypicFeatureTransformer {
    private final static String HPO_CODESYSTEM_URI = "http://github.com/phenopackets/core-ig/CodeSystem/hpo";


    public static PhenotypicFeature toGa4gh(org.monarchinitiative.hapiphenocore.phenopacket.PhenotypicFeature fhirPhenotypicFeature) {
        PhenotypicFeature.Builder builder = PhenotypicFeature.newBuilder();
        CodeableConcept codeableConcept = fhirPhenotypicFeature.getCode();
        String hpoConceptId = codeableConcept.getCodingFirstRep().getCode();
        String hpoLabel = codeableConcept.getCodingFirstRep().getDisplay();
        builder.setType(OntologyClass.newBuilder().setId(hpoConceptId).setLabel(hpoLabel).build());
        List<Observation.ObservationComponentComponent> occList = fhirPhenotypicFeature.getComponent();
        for (var occ: occList) {
            CodeableConcept cc = occ.getCode();
            List<Coding> codings = cc.getCoding();
            for (var coding : codings) {
                if (coding.getSystem().equals(HPO_CODESYSTEM_URI)) {
                    String code = coding.getCode();
                    String display = coding.getDisplay();
                    // in general, could be Sevcerity, HPO Clinical modifier, Onset
                    // check!
                }
            }
        }
        return builder.build();
    }
}

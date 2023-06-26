package org.monarchinitiative.hapiphenocore.ga4gh_to_fhir;

import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;
import org.monarchinitiative.hapiphenocore.except.PhenoClientRuntimeException;
import org.monarchinitiative.hapiphenocore.phenopacket.PhenotypicFeature;
import org.phenopackets.schema.v2.core.OntologyClass;

public class PhenotypicFeatureToObservation {

    private final static String HPO_SYSTEM = "http://www.human-phenotype-ontology.org";
    private final static String valueSystemLoinc = "http://loinc.org";
    private final static String valueCodeLoincPresent = "LA9633-4";
    private final static String valueDisplayPresent = "Present";
    private final static String valueCodeLoincAbsent = "LA9634-2";
    private final static String valueDisplayAbsent = "Absent";
    private final static CodeableConcept ABSENT_CODING =  new CodeableConcept().addCoding(
            new Coding().setSystem(valueSystemLoinc)
            .setCode(valueCodeLoincAbsent)
            .setDisplay(valueDisplayAbsent));
    private final static CodeableConcept PRESENT_CODING =   new CodeableConcept().addCoding(
            new Coding().setSystem(valueSystemLoinc)
            .setCode(valueCodeLoincPresent)
            .setDisplay(valueDisplayPresent));

    public static PhenotypicFeature convert(org.phenopackets.schema.v2.core.PhenotypicFeature pf, String patId) {
        if (! pf.hasType()) {
            throw new PhenoClientRuntimeException("Attempt to convert PhenotypicFeature with empty Type field");
        }
        String hpoCode = pf.getType().getId();
        String hpoLabel = pf.getType().getLabel();
        CodeableConcept cc = new CodeableConcept().addCoding(new Coding()
                        .setSystem(HPO_SYSTEM)
                        .setCode(hpoCode)
                        .setDisplay(hpoLabel)
                );
        PhenotypicFeature fhirPhenotypicFeature =  (PhenotypicFeature) new PhenotypicFeature()
                .setStatus(ObservationStatus.FINAL)
                .setCode(cc)
                .setSubject(new Reference( "Patient/"+patId ));
        if (pf.getExcluded()) {
            fhirPhenotypicFeature.setValue(ABSENT_CODING);
        } else {
            fhirPhenotypicFeature.setValue(PRESENT_CODING);
        }
        // check for severity
        if (pf.hasSeverity()) {
            OntologyClass severity = pf.getSeverity();
            String severityId = severity.getId();
            String severityLabel = severity.getLabel();
            fhirPhenotypicFeature.setSeverity(severityId, severityLabel);
        }
        // check for clinical modifiers
        // check for age of onset


        return fhirPhenotypicFeature;
    }
}

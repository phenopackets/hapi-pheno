package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.*;


/**
 * The PhenotypicFeature constrains Observation as follows
 * * code 1..1 MS
 * * value[x] only CodeableConcept
 * * value[x] 1..1 MS
 * * value[x] ^short = "true: observed; false: excluded"
 * * specimen ..0
 * * device ..0
 * * referenceRange ..0
 * * hasMember ..0
 * * component MS
 * * component ^short = "GA4GH severity and modifiers should be coded as CodeableConcepts in the code field of the component"
 * * component.value[x] only boolean
 */
@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/PhenotypicFeature")
public class PhenotypicFeature extends Observation {
    private static final long serialVersionUID = 1L;

    private final static String system = "http://www.human-phenotype-ontology.org";
    private final static String valueSystemLoinc = "http://loinc.org";
    private final static String valueCodeLoincPresent = "LA9633-4";
    private final static String valueDisplayPresent = "Present";
    private final static String valueCodeLoincAbsent = "LA9634-2";
    private final static String valueDisplayAbsent = "Absent";


    public static PhenotypicFeature createObservation(String hpoCode, String hpoLabel, String patId) {
        return (PhenotypicFeature) new PhenotypicFeature()
                .setStatus(ObservationStatus.FINAL)
                .setCode(new CodeableConcept().addCoding(
                        new Coding().setSystem(system)
                                .setCode(hpoCode)
                                .setDisplay(hpoLabel)))
                .setSubject(new Reference("Patient/" + patId))
                .setValue(new CodeableConcept().addCoding(
                        new Coding().setSystem(valueSystemLoinc)
                                .setCode(valueCodeLoincPresent)
                                .setDisplay(valueDisplayPresent)));
    }

    public PhenotypicFeature createExcludedObservation(String hpoCode, String hpoLabel, String patId) {
        return (PhenotypicFeature) new PhenotypicFeature()
                .setStatus(ObservationStatus.FINAL)
                .setCode(new CodeableConcept().addCoding(
                        new Coding().setSystem(system)
                                .setCode(hpoCode)
                                .setDisplay(hpoLabel)))
                .setSubject(new Reference("Patient/" + patId))
                .setValue(new CodeableConcept().addCoding(
                        new Coding().setSystem(valueSystemLoinc)
                                .setCode(valueCodeLoincAbsent)
                                .setDisplay(valueDisplayAbsent)));
    }


}

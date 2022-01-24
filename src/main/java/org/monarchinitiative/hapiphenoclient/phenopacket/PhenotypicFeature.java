package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.*;
import org.springframework.context.annotation.Profile;


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
@ResourceDef(name="Observation",
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/PhenotypicFeature")
public class PhenotypicFeature extends Observation {
    private static final long serialVersionUID = 1L;

    private final static String system = "http://www.human-phenotype-ontology.org";
    private final static String valueSystemLoinc = "http://loinc.org";
    private final static String valueCodeLoincPresent = "LA9633-4";
    private final static String valueDisplayPresent = "Present";
    private final static String valueCodeLoincAbsent = "LA9634-2";
    private final static String valueDisplayAbsent = "Abset";


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


//    /**
//     * Each extension is defined in a field. Any valid HAPI Data Type
//     * can be used for the field type. Note that the [name=""] attribute
//     * in the @Child annotation needs to match the name for the bean accessor
//     * and mutator methods.
//     */
//    @Child(name="petName")
//    @Extension(url="http://example.com/dontuse#petname", definedLocally=false, isModifier=false)
//    @Description(shortDefinition="The name of the patient's favourite pet")
//    private StringType myPetName;

}

package org.monarchinitiative.hapiphenocore.phenopacket;
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

    private final static String HPO_CODESYSTEM_URI = "http://github.com/phenopackets/core-ig/CodeSystem/hpo";


    public static PhenotypicFeature createObservation(String hpoCode, String hpoLabel, String patId) {
        return (PhenotypicFeature) new PhenotypicFeature()
                .setStatus(ObservationStatus.FINAL)
                .setCode(new CodeableConcept().addCoding(
                        new Coding().setSystem(system)
                                .setCode(hpoCode)
                                .setDisplay(hpoLabel)))
                .setSubject(new Reference( "Patient/"+patId ))
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

    /**
     * <component>
     *     <code>
     *       <coding>
     *         <system value="http://github.com/phenopackets/core-ig/CodeSystem/hpo"/>
     *         <code value="HP:0012828"/>
     *         <display value="Severe"/>
     *       </coding>
     *     </code>
     *     <valueBoolean value="true"/>
     *   </component>
     * @param severityId Ontology id for severity code
     * @param severityLabel Ontology label for severity code
     */
    public void setSeverity(String severityId, String severityLabel) {
        Coding severityCoding = new Coding().setSystem(HPO_CODESYSTEM_URI)
                .setCode(severityId).setDisplay(severityLabel);
        CodeableConcept severityCC = new CodeableConcept().addCoding(severityCoding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setValue(new BooleanType().setValue(true));
        occ.setCode(severityCC);
        this.addComponent(occ);
    }

    public void setHpoSevere() {
        setSeverity("HP:0012828", "Severe");
    }

    public void setHpoModerate() {
        setSeverity("HP:0012826", "Moderate");
    }
    public void setHpoMild() {
        setSeverity("HP:0012825", "Mild");
    }

    public void setHpoBorderline() {
        setSeverity( "HP:0012827", "Borderline");
    }

    public void setHpoProfound() {
        setSeverity("HP:0012829", "Profound");
    }

    public void setOnset(int year, int month, int day){
        setEffective(new DateType().setYear(year).setMonth(month).setDay(day));
    }


}

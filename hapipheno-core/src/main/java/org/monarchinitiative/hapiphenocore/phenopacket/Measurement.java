package org.monarchinitiative.hapiphenocore.phenopacket;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Observation;

@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/Measurement")
public class Measurement extends Observation {
    private static final long serialVersionUID = 1L;

    private final static String system = "http://www.human-phenotype-ontology.org";
    private final static String valueSystemLoinc = "http://loinc.org";
    private final static String valueCodeLoincPresent = "LA9633-4";
    private final static String valueDisplayPresent = "Present";
    private final static String valueCodeLoincAbsent = "LA9634-2";
    private final static String valueDisplayAbsent = "Absent";




}

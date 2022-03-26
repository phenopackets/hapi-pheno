package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.*;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.*;

//  value="https://github.com/phenopackets/core-ig/StructureDefinition/phenopackets-genomic-interpretation"/>
//  </meta>
@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/PhenopacketsGenomicInterpretation")
public class PhenopacketsGenomicInterpretation extends DiagnosticReport {
    private static final long serialVersionUID = 1L;

    private static final String LOINC_SYSTEM = "http://loinc.org/";
    private static final String LOINC_GENETIC_VARIANT_REPORTING_PANEL_ID = "81247-9";
    private static final String LOINC_GENETIC_VARIANT_REPORTING_PANEL_DISPLAY = "Master HL7 genetic variant reporting panel";

    private static final String GA4GH_GENOMIC_INTERPRETATION_SYSTEM = "http://phenopacket-schema.readthedocs.io/en/v2/genomic-interpretation.html#interpretationstatus";
   // private static final String GA4GH_INTERPRETATION_STATUS_URL = "\"http://phenopacket-schema.readthedocs.io/en/v2/genomic-interpretation.html#interpretationstatus";

    public PhenopacketsGenomicInterpretation() {
        Coding coding = new Coding();
        coding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_GENETIC_VARIANT_REPORTING_PANEL_ID)
                .setDisplay(LOINC_GENETIC_VARIANT_REPORTING_PANEL_DISPLAY);
        setCode( new CodeableConcept().addCoding(coding));
        Coding categoryCoding = new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0074")
                .setCode("GE")
                .setDisplay("Genetics");
        addCategory(new CodeableConcept().addCoding(categoryCoding));
    }


    /*
      <modifierExtension
                     url="https://github.com/phenopackets/core-ig/StructureDefinition/interpretation-status">
    <valueCodeableConcept>
      <coding>
        <system
                value="http://phenopacket-schema.readthedocs.io/en/v2/genomic-interpretation.html#interpretationstatus"/>
        <code value="0"/>
        <display value="UNKNOWN_STATUS"/>
      </coding>
    </valueCodeableConcept>
  </modifierExtension>
     */





    public void setPatientId(String unqualifiedIndidualId) {
        setSubject(new Reference( "Patient/"+unqualifiedIndidualId ));
    }

    /**
     * We are adding a reference to a {@link PhenopacketsVariant} object, such as
     *   <reference value="Observation/PhenopacketsVariantExample01"/>
     * @param variant
     */
    public void addResult(PhenopacketsVariant variant) {
        String refValue = "Observation/" + variant.getId();
        addResult(new Reference(refValue));
    }

    /**
     * 0 	No information is available about the status
     */
    public void unknownStatus() {
        // Create an extension
        Extension ext = new Extension();
        ext.setUrl(GA4GH_GENOMIC_INTERPRETATION_SYSTEM);
        ext.setValue(new StringType("UNKNOWN"));
        addExtension(ext);
    }

    /**
     * 1 	The variant or gene reported here is interpreted not to be related to the diagnosis
     */
    public void rejectedStatus() {
        // Create an extension
        Extension ext = new Extension();
        ext.setUrl(GA4GH_GENOMIC_INTERPRETATION_SYSTEM);
        ext.setValue(new StringType("REJECTED"));
        addExtension(ext);
    }

    /**
     * 2 	The variant or gene reported here is interpreted to possibly be related to the diagnosis
     */
    public void candidateStatus() {
        // Create an extension
        Extension ext = new Extension();
        ext.setUrl(GA4GH_GENOMIC_INTERPRETATION_SYSTEM);
        ext.setValue(new StringType("CANDIDATE"));
        addExtension(ext);
    }

    /**
     * 3 	The variant or gene reported here is interpreted to be related to the diagnosis
     */
    public void contributoryStatus() {
        // Create an extension
        Extension ext = new Extension();
        ext.setUrl(GA4GH_GENOMIC_INTERPRETATION_SYSTEM);
        ext.setValue(new StringType("CONTRIBUTORY"));
        addExtension(ext);
    }

    /**
     * 4. The variant or gene reported here is interpreted to be causative of the diagnosis
     */
    public void causativeStatus() {
        // Create an extension
        Extension ext = new Extension();
        ext.setUrl(GA4GH_GENOMIC_INTERPRETATION_SYSTEM);
        ext.setValue(new StringType("CAUSATIVE"));
        addExtension(ext);
    }


}

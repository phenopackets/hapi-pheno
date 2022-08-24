package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.r4.model.*;

@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/PhenopacketsVariant")
public class PhenopacketsVariant extends Observation {
    private static final long serialVersionUID = 1L;
    private static final String HGNC_SYSTEM = "http://www.genenames.org/";
    private static final String LOINC_SYSTEM = "http://loinc.org";
    private static final String LOINC_CYTOGENETIC_CHROMOSOME_LOCATION_ID = "LOINC:48001-2";
    private static final String LOINC_CYTOGENETIC_CHROMOSOME_LOCATION_DISPLAY = "Cytogenetic (chromosome) location";
    /** LOINC code for Human reference sequence assembly version */
    private static final String LOINC_HUMAN_GENOME_ASSEMBLY_ID = "62374-4";
    private static final String LOINC_HUMAN_GENOME_ASSEMBLY_DISPLAY = "Human reference sequence assembly version";
    private static final String LOINC_GENOMIC_REF_ALLELE_ID = "69547-8";
    private static final String LOINC_GENOMIC_REF_ALLELE_DISPLAY = "Genomic ref allele [ID]";
    private static final String LOINC_GENOMIC_ALT_ALLELE_ID = "69551-0";
    private static final String LOINC_GENOMIC_ALT_ALLELE_DISPLAY = "Genomic alt allele [ID]";
    private static final String LOINC_GENOMIC_COORDINATE_SYSTEM_TYPE_ID = "92822-6";
    private static final String LOINC_GENOMIC_COORDINATE_SYSTEM_TYPE_DISPLAY = "Genomic coordinate system [Type]";
    private static final String LOINC_dbSNP_ID = "81255-2";
    private static final String LOINC_dbSNP_DISPLAY = "dbSNP [ID]";
    private static final String NCBI_DBSNP_SYSTEM = "http://www.ncbi.nlm.nih.gov/snp/";
    private static final String HGVS_SYSTEM = "http://www.hgvs.org/";
    private static final String LOINC_DISCRETE_GENETIC_VARIANT_ID = "81252-9";
    private static final String LOINC_DISCRETE_GENETIC_VARIANT_DISPLAY = "Discrete genetic variant";
    private static final String FHIR_GENOMICS_REPORTING_SYSTEM = "http://hl7.org/fhir/uv/genomics-reporting/CodeSystem/tbd-codes/";
    private static final String SEQUENCE_ONTOLOGY_SYSTEM = "http://www.sequenceontology.org/";
    private static final String LOINC_ALLELIC_STATE_ID = "53034-5";
    private static final String LOINC_ALLELIC_STATE_DISPLAY = "Allelic state";
    private static final String GENO_ONTOLOGY_SYSTEM = "http://www.ebi.ac.uk/ols/ontologies/geno";
    private static final String LOINC_GENETIC_VARIANT_ASSESSMENT_ID = "69548-6";
    private static final String LOINC_GENETIC_VARIANT_ASSESSMENT_DISPLAY = "Genetic variant assessment";

    /**
     * The constructor sets of the code of this profiled Observation to be a LOINC genetic variant assessment
     * and sets the category as laboratory, and the value as present.
     */
    public PhenopacketsVariant() {
        CodeableConcept cc = new CodeableConcept();
        cc.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("laboratory");
        addCategory(cc);
        Coding coding = new Coding();
        coding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_GENETIC_VARIANT_ASSESSMENT_ID)
                .setDisplay(LOINC_GENETIC_VARIANT_ASSESSMENT_DISPLAY);
        setCode( new CodeableConcept().addCoding(coding));
        //valueCodeableConcept = LNC#LA9633-4 "Present"
        CodeableConcept presentCC = new CodeableConcept();
        presentCC.addCoding(new Coding().setCode("LA9633-4").setDisplay("Present").setSystem(LOINC_SYSTEM));
        setValue(presentCC);

    }



    /**
     * e.g., HGNC#HGNC:3477 "ETF1"
     * @param hgncId The integer part of an HGNC id
     * @param symbol a gene symbol
     */
    public void setGeneStudied(int hgncId, String symbol) {
        CodeableConcept cc = new CodeableConcept();
        Coding coding = new Coding();
        String geneid = "HGNC:" + hgncId;
        coding.setSystem(HGNC_SYSTEM).setCode(geneid).setDisplay(symbol);
        cc.addCoding(coding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(cc);
        this.addComponent(occ);
    }

    /**
     * Something like this
     * <component>
     *     <code>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="62374-4"/>
     *       </coding>
     *     </code>
     *     <valueCodeableConcept>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="LA26806-2"/>
     *         <display value="GRCh38"/>
     *       </coding>
     *     </valueCodeableConcept>
     *   </component>
     * 62374-4Human reference sequence assembly version
     * @param loincCode LOINC code for a chromosome
     * @param display Display for a chromosome
     */
    public void setReferenceSequenceAssembly(String loincCode, String display) {
        Coding loincGenomeAssemblyCoding = new Coding();
        loincGenomeAssemblyCoding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_HUMAN_GENOME_ASSEMBLY_ID)
                .setDisplay(LOINC_HUMAN_GENOME_ASSEMBLY_DISPLAY);
        CodeableConcept genomeAssemblyCodeableConcept = new CodeableConcept().addCoding(loincGenomeAssemblyCoding);
        CodeableConcept genomeAssembly = new CodeableConcept();
        genomeAssembly.addCoding(new Coding().setSystem(LOINC_SYSTEM).setCode(loincCode).setDisplay(display));
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(genomeAssemblyCodeableConcept);
        occ.setValue(genomeAssembly);
        this.addComponent(occ);
    }

    /**
     * Set the genomic assembly reference for this variant to hg38
     */
    public void setHg38ReferenceAssembly() {
        setReferenceSequenceAssembly("LA26806-2", "GRCh38");
    }

    /**
     * Set the genomic assembly reference for this variant to hg19
     */
    public void setHg19ReferenceAssembly() {
        setReferenceSequenceAssembly("LA14025-3", "GRCh38");
    }


    /**
     * We want have something like this:
     * Note that 48001-2  Cytogenetic (chromosome) location
     *  <component>
     *     <code>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="48001-2"/>
     *       </coding>
     *     </code>
     *     <valueCodeableConcept>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="LA21263-1"/>
     *         <display value="10"/>
     *       </coding>
     *     </valueCodeableConcept>
     *   </component>
     * @param loincCode the LOINC code to represent a chromosome, e.g., "LA21263-1" for chromosome 10
     * @param display The display value, e.g., "10" for chromosome 10
     */
    public void setCytogeneticLocation(String loincCode, String display) {
        Coding loincCytogeneticCoding = new Coding();
        loincCytogeneticCoding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_CYTOGENETIC_CHROMOSOME_LOCATION_ID)
                .setDisplay(LOINC_CYTOGENETIC_CHROMOSOME_LOCATION_DISPLAY);
        CodeableConcept cytogeneticCodeableConcept = new CodeableConcept().addCoding(loincCytogeneticCoding);
        Coding chromosomeCoding = new Coding();
        chromosomeCoding.setSystem(LOINC_SYSTEM)
                .setCode(loincCode)
                .setDisplay(display);
        CodeableConcept cc = new CodeableConcept();
        cc.addCoding(chromosomeCoding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(cytogeneticCodeableConcept);
        occ.setValue(cc);
        this.addComponent(occ);
    }

    public void chromosome1() {
        setCytogeneticLocation("LA21254-0", "1");
    }
    public void chromosome2() {
        setCytogeneticLocation("LA21255-7", "2");
    }
    public void chromosome3() {
        setCytogeneticLocation("LA21256-5", "3");
    }
    public void chromosome4() {
        setCytogeneticLocation("LA21257-3", "4");
    }
    public void chromosome5() {
        setCytogeneticLocation("LA21258-1", "5");
    }
    public void chromosome6() {
        setCytogeneticLocation("LA21259-9", "6");
    }
    public void chromosome7() {
        setCytogeneticLocation("LA21260-7", "7");
    }
    public void chromosome8() {
        setCytogeneticLocation("LA21261-5", "8");
    }
    public void chromosome9() {
        setCytogeneticLocation("LA21262-3", "9");
    }
    public void chromosome10() {
        setCytogeneticLocation("LA21263-1", "10");
    }
    public void chromosome11() {
        setCytogeneticLocation("LA21264-9", "11");
    }
    public void chromosome12() {
        setCytogeneticLocation("LA21265-6", "12");
    }
    public void chromosome13() {
        setCytogeneticLocation("LA21266-4", "13");
    }
    public void chromosome14() {
        setCytogeneticLocation("LA21267-2", "14");
    }
    public void chromosome15() {
        setCytogeneticLocation("LA21268-0", "15");
    }
    public void chromosome16() {
        setCytogeneticLocation("LA21269-8", "16");
    }
    public void chromosome17() {
        setCytogeneticLocation("LA21270-6", "17");
    }
    public void chromosome18() {
        setCytogeneticLocation("LA21271-4", "18");
    }
    public void chromosome19() {
        setCytogeneticLocation("LA21272-2", "19");
    }
    public void chromosome20() {
        setCytogeneticLocation("LA21273-0", "20");
    }
    public void chromosome21() {
        setCytogeneticLocation("LA21274-8", "21");
    }
    public void chromosome22() {
        setCytogeneticLocation("LA21275-5", "22");
    }
    public void chromosomeX() {
        setCytogeneticLocation("LA21276-3", "X");
    }
    public void chromosomeY() {
        setCytogeneticLocation("LA21277-1", "Y");
    }

    /**
     * <component>
     *     <code>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="69547-8"/>
     *       </coding>
     *     </code>
     *     <valueString value="T"/>
     *   </component>
     * @param refAllele a nucleotide or nucleotide sequence representing the wildtype sequence
     */
    public void setGenomicRefAllele(String refAllele) {
        Coding loincGenomicRefAlleleCoding = new Coding();
        loincGenomicRefAlleleCoding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_GENOMIC_REF_ALLELE_ID)
                .setDisplay(LOINC_GENOMIC_REF_ALLELE_DISPLAY);
        CodeableConcept cytogeneticCodeableConcept = new CodeableConcept().addCoding(loincGenomicRefAlleleCoding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(cytogeneticCodeableConcept);
        occ.setValue(new StringType().setValue(refAllele));
        this.addComponent(occ);
    }

    /**
     * Analogous to {@link #setGenomicRefAllele(String)}
     * @param refAllele a nucleotide or nucleotide sequence representing the variant sequence
     */
    public void setGenomicAltAllele(String refAllele) {
        Coding loincGenomicRefAlleleCoding = new Coding();
        loincGenomicRefAlleleCoding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_GENOMIC_ALT_ALLELE_ID)
                .setDisplay(LOINC_GENOMIC_ALT_ALLELE_DISPLAY);
        CodeableConcept cytogeneticCodeableConcept = new CodeableConcept().addCoding(loincGenomicRefAlleleCoding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(cytogeneticCodeableConcept);
        occ.setValue(new StringType().setValue(refAllele));
        this.addComponent(occ);
    }

    /**
     * Like this
     * <component>
     *     <code>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="92822-6"/>
     *       </coding>
     *     </code>
     *     <valueCodeableConcept>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="LA30102-0"/>
     *         <display value="1-based character counting"/>
     *       </coding>
     *     </valueCodeableConcept>
     *   </component>
     */
    public void setCoordinateSystem(String loincId, String display) {
        Coding loincGenomicCoordinateSystemCoding = new Coding();
        loincGenomicCoordinateSystemCoding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_GENOMIC_COORDINATE_SYSTEM_TYPE_ID)
                .setDisplay(LOINC_GENOMIC_COORDINATE_SYSTEM_TYPE_DISPLAY);

        CodeableConcept loincGenomicCoordinateSystemCC = new CodeableConcept().addCoding(loincGenomicCoordinateSystemCoding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(loincGenomicCoordinateSystemCC);
        Coding coordinateCoding = new Coding();
        coordinateCoding.setSystem(LOINC_SYSTEM)
                .setCode(loincId)
                .setDisplay(display);
        CodeableConcept cc = new CodeableConcept();
        cc.addCoding(coordinateCoding);
        occ.setValue(cc);
        this.addComponent(occ);
    }


    public void oneBasedCoordinateSystem() {
        setCoordinateSystem("LA30102-0", "1-based character counting");
    }

    /**
     * <component>
     *     <extension
     *                url="https://github.com/phenopackets/core-ig/StructureDefinition/filter-status">
     *       <valueString value="PASS"/>
     *     </extension>
     *     <code>
     *       <coding>
     *         <system
     *                 value="http://hl7.org/fhir/uv/genomics-reporting/CodeSystem/tbd-codes"/>
     *         <code value="exact-start-end"/>
     *       </coding>
     *     </code>
     *     <valueRange>
     *       <low>
     *         <value value="121496701"/>
     *       </low>
     *     </valueRange>
     *   </component>
     */
    public void setFilterStatus() {
        // TODO
        throw new NotImplementedException("Need to implement extension for VCF filter status");
    }

    /**
     * 81255-2dbSNP [ID]
     *  <component>
     *     <code>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="81255-2"/>
     *       </coding>
     *     </code>
     *     <valueCodeableConcept>
     *       <coding>
     *         <system value="http://www.ncbi.nlm.nih.gov/snp/"/>
     *         <code value="rs121918506"/>
     *         <display value="rs121918506, FGFR2 : Missense Variant"/>
     *       </coding>
     *     </valueCodeableConcept>
     *   </component>
     */
    public void setDbsnpId(String dbSNPid, String display) {
        Coding loincDbsnpCoding = new Coding();
        loincDbsnpCoding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_dbSNP_ID)
                .setDisplay(LOINC_dbSNP_DISPLAY);

        CodeableConcept loincGenomicCoordinateSystemCC = new CodeableConcept().addCoding(loincDbsnpCoding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(loincGenomicCoordinateSystemCC);
        Coding dbSnpIdCoding = new Coding();
        dbSnpIdCoding.setSystem(NCBI_DBSNP_SYSTEM)
                .setCode(dbSNPid)
                .setDisplay(display);
        CodeableConcept cc = new CodeableConcept();
        cc.addCoding(dbSnpIdCoding);
        occ.setValue(cc);
        this.addComponent(occ);
    }

    /**
     * <component>
     *     <code>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="81252-9"/>
     *       </coding>
     *     </code>
     *     <valueCodeableConcept>
     *       <coding>
     *         <system value="http://www.hgvs.org/"/>
     *         <code value="NM_001144915.2"/>
     *         <display value="NM_001144915.2:c.1427A&gt;C"/>
     *       </coding>
     *     </valueCodeableConcept>
     *   </component>
     */
    public void setVariationCode(String codeValue, String display) {
        Coding loincVariationCoding = new Coding();
        loincVariationCoding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_DISCRETE_GENETIC_VARIANT_ID)
                .setDisplay(LOINC_DISCRETE_GENETIC_VARIANT_DISPLAY);

        CodeableConcept loincGenomicCoordinateSystemCC = new CodeableConcept().addCoding(loincVariationCoding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(loincGenomicCoordinateSystemCC);
        Coding hgvsCoding = new Coding();
        hgvsCoding.setSystem(HGVS_SYSTEM)
                .setCode(codeValue)
                .setDisplay(display);
        CodeableConcept cc = new CodeableConcept();
        cc.addCoding(hgvsCoding);
        occ.setValue(cc);
        this.addComponent(occ);
    }

    /**
     *  <component>
     *     <code>
     *       <coding>
     *         <system
     *                 value="http://hl7.org/fhir/uv/genomics-reporting/CodeSystem/tbd-codes"/>
     *         <code value="functional-annotation"/>
     *       </coding>
     *     </code>
     *     <valueCodeableConcept>
     *       <coding>
     *         <system value="http://www.sequenceontology.org/"/>
     *         <code value="0001878"/>
     *         <display value="feature_variant (SO:0001878)"/>
     *       </coding>
     *     </valueCodeableConcept>
     *   </component>
     */
    public void setFunctionalAnnotation(String codeValue, String display) {
        Coding functionalAnnotationCoding = new Coding();
        functionalAnnotationCoding.setSystem(FHIR_GENOMICS_REPORTING_SYSTEM).setCode("functional-annotation");

        CodeableConcept loincGenomicCoordinateSystemCC = new CodeableConcept().addCoding(functionalAnnotationCoding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(loincGenomicCoordinateSystemCC);
        Coding sequenceOntologyCoding = new Coding();
        sequenceOntologyCoding.setSystem(SEQUENCE_ONTOLOGY_SYSTEM)
                .setCode(codeValue)
                .setDisplay(display);
        CodeableConcept cc = new CodeableConcept();
        cc.addCoding(sequenceOntologyCoding);
        occ.setValue(cc);
        this.addComponent(occ);
    }

    /**
     * <component>
     *     <code>
     *       <coding>
     *         <system value="http://loinc.org"/>
     *         <code value="53034-5"/>
     *       </coding>
     *     </code>
     *     <valueCodeableConcept>
     *       <coding>
     *         <system value="http://www.ebi.ac.uk/ols/ontologies/geno"/>
     *         <code value="0000135"/>
     *         <display value="Heterozygous"/>
     *       </coding>
     *     </valueCodeableConcept>
     *   </component>
     */
    public void setAllelicState(String codeValue, String display) {
        Coding loincAllelicStateCoding = new Coding();
        loincAllelicStateCoding.setSystem(LOINC_SYSTEM)
                .setCode(LOINC_ALLELIC_STATE_ID)
                .setDisplay(LOINC_ALLELIC_STATE_DISPLAY);

        CodeableConcept loincAllelicStateCC = new CodeableConcept().addCoding(loincAllelicStateCoding);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setCode(loincAllelicStateCC);
        Coding sequenceOntologyCoding = new Coding();
        sequenceOntologyCoding.setSystem(GENO_ONTOLOGY_SYSTEM)
                .setCode(codeValue)
                .setDisplay(display);
        CodeableConcept cc = new CodeableConcept();
        cc.addCoding(sequenceOntologyCoding);
        occ.setValue(cc);
        this.addComponent(occ);
    }

    public void setHeterozygous() {
        setAllelicState("GENO:0000135", "heterozygous");
    }

    public void setHomozygous() {
        setAllelicState("GENO:0000136", "homozygous");
    }

    public void setHemizygous() {
        setAllelicState("GENO:0000134", "hemizygous");
    }



    public void setExactStartEnd(int start, int end) {
        //exact-start-end
        Range valueRange = new Range();
        Quantity low = new Quantity().setValue(start);
        Quantity high = new Quantity().setValue(end);
        valueRange.setLow(low).setHigh(high);
        ObservationComponentComponent occ = new ObservationComponentComponent();
        occ.setValue(valueRange);
        this.addComponent(occ);
    }


    public void setPatientId(String unqualifiedIndidualId) {
         setSubject(new Reference( "Patient/"+unqualifiedIndidualId ));
    }
}

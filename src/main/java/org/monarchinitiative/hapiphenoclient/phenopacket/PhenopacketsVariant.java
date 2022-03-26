package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.r4.model.*;
import org.monarchinitiative.hapiphenoclient.except.PhenoClientRuntimeException;

@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/PhenopacketsVariant")
public class PhenopacketsVariant extends Observation {
    private static final long serialVersionUID = 1L;
    private static final String HGNC_SYSTEM = "http://www.genenames.org/";
    private static final String LOINC_SYSTEM = "http://loinc.org/";
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
        occ.setValue(chromosomeCoding);
        this.addComponent(occ);
    }

    public void setChromosome(String chr) {
        switch (chr) {
            case "10":
            case "chr10" : setCytogeneticLocation("LNC#LA21263-1", "10");
            break;
            default:
                throw new PhenoClientRuntimeException("Could not find chromosome " + chr);
        }
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


    public void setOneBasedCoordinateSystem() {
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





}

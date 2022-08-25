package org.monarchinitiative.hapiphenoclient.examples;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.monarchinitiative.hapiphenoclient.fhir.util.MyPractitioner;
import org.monarchinitiative.hapiphenoclient.phenopacket.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BethlemMyopathyExample implements PhenoExample {

    private final String phenopacketIdentifier = "phenopacket.1";

    private final String GA4GH_SYSTEM = "https://www.ga4gh.org/";
    private final String GA4GH_TYPE = "phenopacketv2";

    private final MyPractitioner williamHarvey = MyPractitioner.harvey();

    private final Individual individual;

    private Phenopacket phenopacket = null;

    private IIdType phenopacketId = null;



    public BethlemMyopathyExample() {
        individual = individual();
    }



    /**
     * The FHIR server assigns the patient an ID such as http://localhost:8888/fhir/Patient/208/_history/1
     * This method would then return "208"
     * @return the individual id (assigned by FHIR server) as string
     */
    public String getUnqualifiedIndividualId() {
        return individual.getIdElement().toUnqualified().getIdPart();
    }


    /**
     * @return A simulated patient -- 14 year old boy with XY karyotype.
     */
    public Individual individual() {
        Individual individual = new Individual();
        HumanName name = new HumanName();
        name.setFamily("Smith");
        individual.setGender(Enumerations.AdministrativeGender.MALE);
        individual.setKaryotypicSex("XY");
        Date birthdate = new GregorianCalendar(2007, Calendar.FEBRUARY, 11).getTime();
        individual.setBirthDate(birthdate);
        return individual;
    }



    @Override
    public Phenopacket phenopacket() {
        this.phenopacket = new Phenopacket();
        this.phenopacket.setIdentifier(new Identifier().setValue(phenopacketIdentifier));
        this.phenopacket.setIndividual(individual);
        this.phenopacket.setStatus(Composition.CompositionStatus.FINAL);
        this.phenopacket.setSubject(new Reference("Patient/" + getUnqualifiedIndividualId()));
        CodeableConcept ga4ghType = new CodeableConcept();
        ga4ghType.addCoding(
                new Coding().setSystem(GA4GH_SYSTEM)
                        .setCode(GA4GH_TYPE));
        this.phenopacket.setType(ga4ghType);
        this.phenopacket.setDate(new Date()); // current date/time
        this.phenopacket.addAuthor().setReference(williamHarvey.getReference()).setDisplay(williamHarvey.getDisplayName());
        this.phenopacket.setTitle("Phenopacket: Bethlem Myopathy");
        this.phenopacket.setId("example.id");
        return phenopacket;
    }


    public List<PhenotypicFeature> phenotypicFeatureList() {
        List<PhenotypicFeature> features = new ArrayList<>();
        PhenotypicFeature pf = PhenotypicFeature.createObservation("HP:0001558", "Decreased fetal movement", getUnqualifiedIndividualId());
        features.add(pf);
        PhenotypicFeature pf2 = PhenotypicFeature.createObservation("HP:0011463", "Macroscopic hematuria", getUnqualifiedIndividualId());
        features.add(pf2);
        PhenotypicFeature pf3 = PhenotypicFeature.createObservation("HP:0001270", "Motor delay", getUnqualifiedIndividualId());
        features.add(pf3);
        return features;
    }

    public List<Measurement> measurementList() {
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(proteinInUrine());
        return measurements;
    }


    /**
     * Proteinuria was 187.60 mg/day
     * @return A measurement reflecting protein in urine
     */
    private Measurement proteinInUrine() {
        Measurement measurement = new Measurement();
        measurement.setStatus(Observation.ObservationStatus.FINAL);
        Coding coding = measurement.getCode().addCoding();
        coding.setCode("2889-4").setSystem("http://loinc.org").setDisplay("Protein (24H U) [Mass/Time]");

        Quantity value = new Quantity();
        value.setValue(187.60).setSystem("http://unitsofmeasure.org").setCode("mg");
        measurement.setValue(value);

        SimpleQuantity low = new SimpleQuantity();
        low.setValue(0).setSystem("http://unitsofmeasure.org").setCode("mg");
        measurement.getReferenceRangeFirstRep().setLow(low);
        SimpleQuantity high = new SimpleQuantity();
        low.setValue(100).setSystem("http://unitsofmeasure.org").setCode("mg");
        measurement.getReferenceRangeFirstRep().setHigh(high);
        measurement.setSubject(new Reference( "Patient/"+getUnqualifiedIndividualId()  ));

        Reference perf = measurement.addPerformer();
        perf.setDisplay(williamHarvey.getDisplayName()).setReference(williamHarvey.getReference());
        String dateTime = "2014-03-08";
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = ft.parse(dateTime);
            measurement.setEffective(new DateTimeType().setValue(date));
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }
        return measurement;
    }


    /**
     * NM_001848.3:c.877G>A 	NM_001848.3
     * NP_001839.2:p.(Gly293Arg) 	NP_001839.2
     * GRCh38:21:45989626:G:A
     * COL6A1, HGNC:2211
     * heterozygous
     * 21q22.3
     * @return A {@link PhenopacketsVariant} object representing NM_001848.3:c.877G>A
     */
    @Override
    public PhenopacketsVariant createPhenopacketsVariant() {
        PhenopacketsVariant phenopacketsVariant = new PhenopacketsVariant();
        phenopacketsVariant.setGeneStudied(2211, "COL6A1");
        phenopacketsVariant.setHeterozygous();
        phenopacketsVariant.setHg38ReferenceAssembly();
        phenopacketsVariant.chromosome21();
        phenopacketsVariant.setGenomicRefAllele("G");
        phenopacketsVariant.setGenomicAltAllele("A");
        phenopacketsVariant.setExactStartEnd(45989626,45989626);
        phenopacketsVariant.oneBasedCoordinateSystem();
        phenopacketsVariant.setVariationCode("NM_001848.3:c.877G>A", "NP_001839.2:p.(Gly293Arg)");
        phenopacketsVariant.setStatus(Observation.ObservationStatus.FINAL);
        phenopacketsVariant.setPatientId(getUnqualifiedIndividualId());
        return phenopacketsVariant;
    }





    @Override
    public Phenopacket modifyPhenopacket(Phenopacket p) {
        return null;
    }


    @Override
    public void setIndividualId(IIdType individualId) {
        this.individual.setId(individualId);
    }

    @Override
    public void setPhenopacketId(IIdType phenopacketId) {
        this.phenopacketId = phenopacketId;
    }

    @Override
    public IIdType getPhenopacketId() {
        return this.phenopacketId;
    }

    @Override
    public PhenopacketsGenomicInterpretation addGenomicInterpretation(PhenopacketsVariant variant) {
        PhenopacketsGenomicInterpretation genomicInterpretation = new PhenopacketsGenomicInterpretation();
        genomicInterpretation.setPatientId(getUnqualifiedIndividualId());
        genomicInterpretation.causativeStatus();
        genomicInterpretation.addResult(variant);
        return genomicInterpretation;
    }
}

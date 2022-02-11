package org.monarchinitiative.hapiphenoclient.examples;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.monarchinitiative.hapiphenoclient.phenopacket.Individual;
import org.monarchinitiative.hapiphenoclient.phenopacket.Measurement;
import org.monarchinitiative.hapiphenoclient.phenopacket.Phenopacket;
import org.monarchinitiative.hapiphenoclient.phenopacket.PhenotypicFeature;

import java.util.*;

public class BethlemMyopathyExample implements PhenoExample {

    private final String phenopacketId = "phenopacket.1";

    private final Individual individual;

    public BethlemMyopathyExample() {
        individual = individual();
    }

    /**
     * We get the individual ID from posting the Indiviudal to the server, and need to set it here.
     * @param id
     */
    public void setIndividualId(IIdType id) {
        this.individual.setId(id);
    }

    /**
     * The FHIR server assigns the patient an ID such as http://localhost:8888/fhir/Patient/208/_history/1
     * This method would then return "208"
     * @return
     */
    public String getUnqualifiedIndidualId() {
        return individual.getIdElement().toUnqualified().getIdPart();
    }


    /**
     * @return A simulated patient -- 14 year old boy with XY karyotype.
     */
    public Individual individual() {
        Individual individual = new Individual();

        individual.addIdentifier(new Identifier().setValue("id.1").setSystem("http://phenopackets.org"));
        individual.setGender(Enumerations.AdministrativeGender.MALE);
        individual.setKaryotypicSex("XY");
        Date birthdate = new GregorianCalendar(2007, Calendar.FEBRUARY, 11).getTime();
        individual.setBirthDate(birthdate);
        return individual;
    }


    public List<PhenotypicFeature> phenotypicFeatureList() {
        List<PhenotypicFeature> features = new ArrayList<>();
       // individual.getIdElement().
        PhenotypicFeature pf = PhenotypicFeature.createObservation("HP:0001558", "Decreased fetal movement", getUnqualifiedIndidualId());
        features.add(pf);
        PhenotypicFeature pf2 = PhenotypicFeature.createObservation("HP:0011463", "Macroscopic hematuria", getUnqualifiedIndidualId());
        features.add(pf2);
        PhenotypicFeature pf3 = PhenotypicFeature.createObservation("HP:0001270", "Motor delay", getUnqualifiedIndidualId());
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
     * @return
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
        measurement.setSubject(new Reference( "Patient/"+getUnqualifiedIndidualId()  ));

        Reference perf = measurement.addPerformer();
        perf.setDisplay("A. Langeveld").setReference("Practitioner/f005/");
        return measurement;
    }



    @Override
    public Phenopacket createPhenopacket() {
        Phenopacket phenopacket = new Phenopacket(phenopacketId);
        phenopacket.setIndividual(individual());
        return phenopacket;
    }

    @Override
    public Phenopacket modifyPhenopacket(Phenopacket p) {
        return null;
    }
}

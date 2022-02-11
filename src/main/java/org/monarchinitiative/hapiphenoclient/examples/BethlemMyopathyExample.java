package org.monarchinitiative.hapiphenoclient.examples;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.monarchinitiative.hapiphenoclient.phenopacket.Individual;
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

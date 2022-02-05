package org.monarchinitiative.hapiphenoclient.examples;

import org.hl7.fhir.r4.model.Enumerations;
import org.monarchinitiative.hapiphenoclient.phenopacket.Individual;
import org.monarchinitiative.hapiphenoclient.phenopacket.KaryotypicSexExtension;
import org.monarchinitiative.hapiphenoclient.phenopacket.Phenopacket;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BethlemMyopathyExample implements PhenoExample {

    private final String phenopacketId = "phenopacket.1";

    public BethlemMyopathyExample() {

    }


    /**
     * @return A simulated patient -- 14 year old boy with XY karyotype.
     */
    public Individual individual() {
        Individual individual = new Individual();
        individual.setId("id.1");
        individual.setGender(Enumerations.AdministrativeGender.MALE);
        individual.setKaryotypicSex(KaryotypicSexExtension.fromString("XY"));
        Date birthdate = new GregorianCalendar(2007, Calendar.FEBRUARY, 11).getTime();
        individual.setBirthDate(birthdate);
        return individual;
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

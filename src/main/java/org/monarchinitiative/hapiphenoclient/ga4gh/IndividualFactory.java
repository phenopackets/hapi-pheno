package org.monarchinitiative.hapiphenoclient.ga4gh;

import org.hl7.fhir.r4.model.Patient;
import org.phenopackets.schema.v2.core.Age;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.Sex;
import org.phenopackets.schema.v2.core.TimeElement;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class IndividualFactory {

/*
  Individual individual = new Individual();
        HumanName name = new HumanName();
        name.setFamily("Smith");
        individual.addIdentifier(new Identifier().setValue("id.1").setSystem("http://phenopackets.org"));
        individual.setGender(Enumerations.AdministrativeGender.MALE);
        individual.setKaryotypicSex("XY");
        Date birthdate = new GregorianCalendar(2007, Calendar.FEBRUARY, 11).getTime();
        individual.setBirthDate(birthdate);
        return individual;
 */

    public static Individual toGa4gh(Patient patient) {
        Individual.Builder builder = Individual.newBuilder();
        if (patient.getId() != null) {
            builder.setId(patient.getId());
        }
        switch (patient.getGender()) {
            case MALE:
                builder.setSex(Sex.MALE);
                break;
            case FEMALE:
                builder.setSex(Sex.FEMALE);
                break;
            case OTHER:
                builder.setSex(Sex.OTHER_SEX);
                break;
        }
        Date birthdate = patient.getBirthDate();
        Date now = new Date();
        LocalDate start = convertToLocalDate( birthdate) ;
        LocalDate stop = convertToLocalDate(now);
        long years = java.time.temporal.ChronoUnit.YEARS.between( start , stop );
        String isoAge = String.format("P%dY", years);
        builder.setTimeAtLastEncounter(TimeElement.newBuilder()
                .setAge(Age.newBuilder().setIso8601Duration(isoAge)).build());
        return builder.build();

    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}

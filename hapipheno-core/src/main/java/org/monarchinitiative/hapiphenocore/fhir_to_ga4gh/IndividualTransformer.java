package org.monarchinitiative.hapiphenocore.fhir_to_ga4gh;

import org.phenopackets.schema.v2.core.*;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class IndividualTransformer {

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

    public static Individual toGa4gh(org.monarchinitiative.hapiphenocore.phenopacket.Individual patient) {
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
        org.monarchinitiative.hapiphenocore.phenopacket.Individual.KaryotypicSex ksex = patient.getKaryotypicSex();
        builder.setTimeAtLastEncounter(TimeElement.newBuilder()
                .setAge(Age.newBuilder().setIso8601Duration(isoAge)).build());
        if (!ksex.isEmpty()) {
            builder.setKaryotypicSex(KaryotypicSex.valueOf(ksex.getKaryotype().getCode()));
        }
        return builder.build();

    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        // TODO what should we do if local date is null????
        if (dateToConvert == null) {
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date date = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
            return date.toInstant().atZone(defaultZoneId).toLocalDate();
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}

package org.monarchinitiative.hapiphenocore.ga4gh_to_fhir;

import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.phenopackets.schema.v2.core.Individual;
import org.phenopackets.schema.v2.core.Sex;

public class IndividualToFhirPatient {

    /**
     * * extension contains
     *  //   AgeOrAgeRange named AgeOrAgeRange ..1 MS and
     *     KaryotypicSex named KaryotypicSex 0..1 MS //and
     * //    Taxonomy named Taxonomy ..1 MS
     * * identifier MS
     * * gender MS
     * * birthDate MS
     * @param ga4ghIndividual
     * @return
     */
    public static org.monarchinitiative.hapiphenocore.phenopacket.Individual
        convert(Individual ga4ghIndividual) {
        org.monarchinitiative.hapiphenocore.phenopacket.Individual fhirPatient =
                new org.monarchinitiative.hapiphenocore.phenopacket.Individual();
        fhirPatient.addIdentifier().setValue(ga4ghIndividual.getId());
        Sex biologicalSex = ga4ghIndividual.getSex();
        if (biologicalSex == Sex.FEMALE)  {
            fhirPatient.setGender(Enumerations.AdministrativeGender.FEMALE);
        } else if (biologicalSex == Sex.MALE) {
            fhirPatient.setGender(Enumerations.AdministrativeGender.MALE);
        }
        return fhirPatient;
    }
}

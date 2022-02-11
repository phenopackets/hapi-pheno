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
        PhenotypicFeature pf = PhenotypicFeature.createObservation("HP:0001558", "Decreased fetal movement", individual.getIdElement().toUnqualified().getIdPart());
        features.add(pf);

        /*
        id: "HP:0001558"
    label: "Decreased fetal movement"
  onset:
    ontologyClass:
      id: "HP:0011461"
      label: "Fetal onset"
  evidence:
  - evidenceCode:
      id: "ECO:0000033"
      label: "author statement supported by traceable reference"
    reference:
      id: "PMID:30808312"
      description: "COL6A1 mutation leading to Bethlem myopathy with recurrent hematuria:\
        \ a case report."
         */

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

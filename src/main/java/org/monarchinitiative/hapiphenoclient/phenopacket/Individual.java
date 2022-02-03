package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Patient;

public class Individual extends Patient {


    // Frage dieses Element ist optional 0..1
    // wie kodiert man das?

    // karyotypic sex, gender, identifier, birthdate are defined as MS
    // wie kodiert man das?

    @Child(name="karyotypicSex")
    @Extension(url="https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex",
            definedLocally=false, isModifier=false)
    @Description(shortDefinition = "the chromosomal sex of an individual")
    private Enumeration<Karyotype> karyotype;
}

package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Patient;

public class Individual extends Patient {




    @Child(name="karyotypicSex")
    @Extension(url="https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex",
            definedLocally=false, isModifier=false)
    @Description(shortDefinition = "the chromosomal sex of an individual")
    private KaryotypicSexExtension karyotype;
}

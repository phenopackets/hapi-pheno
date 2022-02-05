package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;


@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/Individual")
public class Individual extends Patient {


    @Child(name="karyotypicSex", type = KaryotypicSexExtension.class)
    @Extension(url="https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex")
    @Description(shortDefinition = "the chromosomal sex of an individual")
    private KaryotypicSexExtension karyotype;


    public void setKaryotypicSex(KaryotypicSexExtension k) {
        this.karyotype = k;
    }

    public KaryotypicSexExtension getKaryotypicSex() {
        return this.karyotype;
    }



}

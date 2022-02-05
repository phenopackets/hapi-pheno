package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Patient;

import javax.annotation.processing.Generated;


@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/fhir/StructureDefinition/Individual")
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

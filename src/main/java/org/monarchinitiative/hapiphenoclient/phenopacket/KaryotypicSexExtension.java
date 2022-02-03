package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Extension;

@ResourceDef(name="Extension",
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex")
public class KaryotypicSexExtension extends Extension {

    private Karyotype karyotype;

    //"https://github.com/phenopackets/core-ig/CodeSystem/KaryotypicSex",

    private KaryotypicSexExtension(Karyotype k) {
        this.karyotype = k;
        setUrl("https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex");
        setValue(new CodeableConcept().addCoding(new Coding()
                .setSystem("https://github.com/phenopackets/core-ig/CodeSystem/KaryotypicSex")
                .setCode(k.name())));
    }


    //public void setKaryotype();

   public static KaryotypicSexExtension fromString(String karyotpe) {
       switch (karyotpe) {
           case "XX": return new KaryotypicSexExtension(Karyotype.XX);

           default: return new KaryotypicSexExtension(Karyotype.UNKNOWN_KARYOTYPE);
       }
   }


}

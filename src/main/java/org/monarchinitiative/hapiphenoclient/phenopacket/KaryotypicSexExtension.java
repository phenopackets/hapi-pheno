package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;

@ResourceDef(name="Extension",
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex")
public class KaryotypicSexExtension extends Extension {

    private final Karyotype karyotype;

    //"https://github.com/phenopackets/core-ig/CodeSystem/KaryotypicSex",

    private KaryotypicSexExtension(Karyotype k) {
        this.karyotype = k;
        setUrl("https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex");
        setValue(new CodeableConcept().addCoding(new Coding()
                .setSystem("https://github.com/phenopackets/core-ig/CodeSystem/KaryotypicSex")
                .setCode(k.name())));
    }

    /**
     * It is important to override the isEmpty() method, adding a check for any
     * newly added fields.
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(karyotype);
    }

    public IResource KaryotypicSexExtension(){
        return (IResource) new KaryotypicSexExtension(karyotype);
    }



    //public void setKaryotype();

   public static KaryotypicSexExtension fromString(String karyotpe) {
       switch (karyotpe) {
           case "XX": return new KaryotypicSexExtension(Karyotype.XX);
           case "XY": return new KaryotypicSexExtension(Karyotype.XY);
           default: return new KaryotypicSexExtension(Karyotype.UNKNOWN_KARYOTYPE);
       }
   }


}

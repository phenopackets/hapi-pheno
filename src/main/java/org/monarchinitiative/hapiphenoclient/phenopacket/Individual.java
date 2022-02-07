package org.monarchinitiative.hapiphenoclient.phenopacket;

import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.*;


@ResourceDef(
        profile="https://github.com/phenopackets/core-ig/StructureDefinition/Individual")
public class Individual extends Patient {


//    @Child(name="karyotypicSex", type = KaryotypicSexExtension.class)
//    @Extension(url="https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex")
//    @Description(shortDefinition = "the chromosomal sex of an individual")
//    private KaryotypicSexExtension karyotype;


    @Child(name = "karyotypicSex", type = KaryotypicSex.class, order = 2, min = 0, max = 1)
    @Extension(url = "https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex", definedLocally = true, isModifier = false)
    private KaryotypicSex karyotypicSex;

    public KaryotypicSex getKaryotypicSex() {
        return karyotypicSex;
    }

    public void setKaryotypicSex(String karyotype) {
        //this.karyotypicSex = Karyotype.fromCode(karyotype);
    }

    @Block()
    public static class KaryotypicSex extends BackboneElement  {

        @Child(name = "karyotypicSex", type = Enumeration.class, order = 1, min = 0, max = Child.MAX_UNLIMITED)
        @Extension(url="https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex")
        private Enumeration<Karyotype>  karyotype;
        private IdType myId;

        public Enumeration<Karyotype> getKaryotype() {
            return karyotype;
        }


        @Override
        public BackboneElement copy() {
            KaryotypicSex copy = new KaryotypicSex();
            copy.setKaryotype(karyotype);
            return copy;
        }


        @Override
        public boolean isEmpty() {
            return ElementUtil.isEmpty(karyotype);
        }

        public void setKaryotype(Enumeration<Karyotype> k) {
            karyotype = k;
        }

    }




}

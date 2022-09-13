package org.monarchinitiative.hapiphenocore.phenopacket;

import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.util.ElementUtil;
import org.hl7.fhir.r4.model.*;


@ResourceDef(profile="https://github.com/phenopackets/core-ig/StructureDefinition/Individual")
public class Individual extends Patient {

    @Child(name = "karyotypicSex", type = KaryotypicSex.class, order = 2, min = 0, max = 1)
    @Extension(url = "https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex", definedLocally = true, isModifier = false)
    @Description(shortDefinition = "the chromosomal sex of an individual")
    private KaryotypicSex karyotypicSex = null;

    public KaryotypicSex getKaryotypicSex() {
        if (karyotypicSex == null) {
            karyotypicSex = new KaryotypicSex();
        }
        return karyotypicSex;
    }

    public void setKaryotypicSex(String karyotype) {
        if (karyotypicSex == null) {
            karyotypicSex = new KaryotypicSex();
        }
        this.karyotypicSex.setKaryotype(karyotype);
    }

    @Block()
    public static class KaryotypicSex extends BackboneElement  {
        /**
         * The default value is UNKNOWN_KARYOTYPE
         */
        @Child(name = "karyotypicSex", type = Enumeration.class, order = 1, min = 0, max = Child.MAX_UNLIMITED)
        @Extension(url="https://github.com/phenopackets/core-ig/StructureDefinition/KaryotypicSex")
        private Enumeration<Karyotype>  karyotype = new Enumeration<>(Karyotype.UNKNOWN_KARYOTYPE);

        public Enumeration<Karyotype> getKaryotype() {
            return karyotype;
        }

        public void setKaryotype(String code) {
            this.karyotype.getEnumFactory().fromCode(code);
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

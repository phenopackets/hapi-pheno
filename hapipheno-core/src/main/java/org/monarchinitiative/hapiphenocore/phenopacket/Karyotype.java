package org.monarchinitiative.hapiphenocore.phenopacket;

import org.hl7.fhir.r4.model.EnumFactory;

public enum Karyotype implements EnumFactory<Karyotype> {
    UNKNOWN_KARYOTYPE,
    XX,
    XY,
    XO,
    XXY,
    XXX,
    XXYY,
    XXXY,
    XXXX,
    XYY,
    OTHER_KARYOTYPE;

    // TODO Complete me!
    @Override
    public Karyotype fromCode(String s) throws IllegalArgumentException {
        switch (s) {
            case "XX": return XX;
            case "XY": return XY;
            default: return UNKNOWN_KARYOTYPE;
        }
    }

    @Override
    public String toCode(Karyotype karyotype) {
        switch (karyotype) {
            case XX: return "XX";
            case XY: return "XY";
            // TODO
            default: return "UNKNOWN_KARYOTYPE";
        }
    }

    @Override
    public String toSystem(Karyotype karyotype) {
        return null;
    }
}

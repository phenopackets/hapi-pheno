package org.monarchinitiative.hapiphenoclient.ga4gh;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Type;
import org.phenopackets.schema.v2.core.*;

import java.math.BigDecimal;

public class MeasurementTransformer {

    public static Measurement toGa4gh(org.monarchinitiative.hapiphenoclient.phenopacket.Measurement fhirMeasurement) {
        Measurement.Builder builder = Measurement.newBuilder();
        CodeableConcept codeableConcept = fhirMeasurement.getCode();
        String hpoConceptId = codeableConcept.getCodingFirstRep().getCode();
        String hpoLabel = codeableConcept.getCodingFirstRep().getDisplay();
        builder.setAssay(OntologyClass.newBuilder().setId(hpoConceptId).setLabel(hpoLabel).build());
        Type value = fhirMeasurement.getValue();
      //  builder.setType();
       // double val = Double.parseDouble(value.primitiveValue());
        //mValue ga4ghValue = Value.newBuilder().setQuantity(Quantity.newBuilder().setValue(val).setUnit().build()).build()
        return builder.build();
    }
}

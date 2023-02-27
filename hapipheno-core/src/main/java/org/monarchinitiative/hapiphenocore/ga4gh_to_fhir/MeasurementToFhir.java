package org.monarchinitiative.hapiphenocore.ga4gh_to_fhir;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.SimpleQuantity;
import org.monarchinitiative.hapiphenocore.phenopacket.Measurement;
import org.phenopackets.schema.v2.core.OntologyClass;

public class MeasurementToFhir {

    private static final String LOINC_SYSTEM = "http://loinc.org";
    private static final String UCUM_SYSTEM = "http://unitsofmeasure.org";

    public static Measurement convert(org.phenopackets.schema.v2.core.Measurement ga4ghMeasurement) {

        Measurement measurement = new Measurement();
        measurement.setStatus(Observation.ObservationStatus.FINAL);

        OntologyClass assay = ga4ghMeasurement.getAssay();
        if (! assay.getId().startsWith("LOINC")) {
            return measurement;
        }
        Coding coding = measurement.getCode().addCoding();
        coding.setCode(assay.getId())
                .setSystem(LOINC_SYSTEM)
                .setDisplay(assay.getLabel());
        Quantity value = new Quantity();
        if (ga4ghMeasurement.hasValue()) {
            var ga4ghQuantity = ga4ghMeasurement.getValue().getQuantity();
            value.setValue(ga4ghQuantity.getValue());
            String unitCode = ga4ghQuantity.getUnit().getId();
            value.setCode(unitCode);
            value.setSystem(UCUM_SYSTEM);
            measurement.setValue(value);
            if (ga4ghQuantity.hasReferenceRange()) {
                var refRange = ga4ghQuantity.getReferenceRange();
                SimpleQuantity low = new SimpleQuantity();
                low.setValue(refRange.getLow()).setSystem(UCUM_SYSTEM).setCode(unitCode);
                measurement.getReferenceRangeFirstRep().setLow(low);
                SimpleQuantity high = new SimpleQuantity();
                low.setValue(refRange.getHigh()).setSystem(UCUM_SYSTEM).setCode(unitCode);
                measurement.getReferenceRangeFirstRep().setHigh(high);
            }
        }
        return measurement;
    }


}


/*
 Measurement measurement = new Measurement();


        SimpleQuantity low = new SimpleQuantity();
        low.setValue(0).setSystem("http://unitsofmeasure.org").setCode("mg");
        measurement.getReferenceRangeFirstRep().setLow(low);
        SimpleQuantity high = new SimpleQuantity();
        low.setValue(100).setSystem("http://unitsofmeasure.org").setCode("mg");
        measurement.getReferenceRangeFirstRep().setHigh(high);
        measurement.setSubject(new Reference( "Patient/"+getUnqualifiedIndividualId()  ));

        Reference perf = measurement.addPerformer();
        perf.setDisplay(williamHarvey.getDisplayName()).setReference(williamHarvey.getReference());
        String dateTime = "2014-03-08";
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = ft.parse(dateTime);
            measurement.setEffective(new DateTimeType().setValue(date));
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }
        return measurement;
 */
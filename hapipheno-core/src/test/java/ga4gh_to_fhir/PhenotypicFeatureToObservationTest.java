package ga4gh_to_fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.hapiphenocore.ga4gh_to_fhir.PhenotypicFeatureToObservation;
import org.phenopackets.schema.v2.core.OntologyClass;
import org.phenopackets.schema.v2.core.PhenotypicFeature;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PhenotypicFeatureToObservationTest {
    private final static String HPO_SYSTEM = "http://www.human-phenotype-ontology.org";
    private final static String valueSystemLoinc = "http://loinc.org";
    private final static String valueCodeLoincPresent = "LA9633-4";
    private final static String valueDisplayPresent = "Present";
    private final static String valueCodeLoincAbsent = "LA9634-2";
    private final static String valueDisplayAbsent = "Absent";
    private final static OntologyClass arachnodactyly = OntologyClass.newBuilder()
            .setId("HP:0001166")
            .setLabel("Arachnodactyly")
            .build();
    private final static OntologyClass severe = OntologyClass.newBuilder()
            .setId("HP:0012828")
            .setLabel("Severe")
            .build();

    @Test
    public void testSimplePhenotypicFeature() {

        String patientId = "patient.1";

        PhenotypicFeature pf = PhenotypicFeature.newBuilder().setType(arachnodactyly).build();
        org.monarchinitiative.hapiphenocore.phenopacket.PhenotypicFeature observation = PhenotypicFeatureToObservation.convert(pf, patientId);
        assertNotNull(observation);
        assertEquals(1, observation.getCode().getCoding().size());
        Coding coding = observation.getCode().getCoding().get(0);
        assertEquals("Arachnodactyly", coding.getDisplay());
        assertEquals("HP:0001166", coding.getCode());
        CodeableConcept val = (CodeableConcept) observation.getValue();
        assertEquals(1, val.getCoding().size());
        Coding valueCoding = val.getCoding().get(0);
        assertEquals(valueCodeLoincPresent, valueCoding.getCode());
        assertEquals(valueDisplayPresent, valueCoding.getDisplay());
    }

    @Test
    public void testExcludedPhenotypicFeature() {
        String patientId = "patient.1";

        PhenotypicFeature pf = PhenotypicFeature.newBuilder().setType(arachnodactyly).setExcluded(true).build();
        org.monarchinitiative.hapiphenocore.phenopacket.PhenotypicFeature observation = PhenotypicFeatureToObservation.convert(pf, patientId);
        assertNotNull(observation);
        assertEquals(1, observation.getCode().getCoding().size());
        Coding coding = observation.getCode().getCoding().get(0);
        assertEquals("Arachnodactyly", coding.getDisplay());
        assertEquals("HP:0001166", coding.getCode());
        CodeableConcept val = (CodeableConcept) observation.getValue();
        assertEquals(1, val.getCoding().size());
        Coding valueCoding = val.getCoding().get(0);
        assertEquals(valueCodeLoincAbsent, valueCoding.getCode());
        assertEquals(valueDisplayAbsent, valueCoding.getDisplay());
    }


    @Test
    public void testSeverePhenotypicFeature() {
        String patientId = "patient.1";

        PhenotypicFeature pf = PhenotypicFeature.newBuilder().setType(arachnodactyly).setSeverity(severe).build();
        org.monarchinitiative.hapiphenocore.phenopacket.PhenotypicFeature observation = PhenotypicFeatureToObservation.convert(pf, patientId);
        observation.setHpoSevere();
        assertNotNull(observation);
        assertEquals(1, observation.getCode().getCoding().size());
        Coding coding = observation.getCode().getCoding().get(0);
        assertEquals("Arachnodactyly", coding.getDisplay());
        assertEquals("HP:0001166", coding.getCode());
        CodeableConcept val = (CodeableConcept) observation.getValue();
        assertEquals(1, val.getCoding().size());
        Coding valueCoding = val.getCoding().get(0);
        assertEquals(valueCodeLoincPresent, valueCoding.getCode());
        assertEquals(valueDisplayPresent, valueCoding.getDisplay());
        List<Observation.ObservationComponentComponent> componentList = observation.getComponent();
        assertEquals(1, componentList.size());
        Observation.ObservationComponentComponent occ = componentList.get(0);
        assertEquals(1, occ.getCode().getCoding().size());
        Coding severity = occ.getCode().getCoding().get(0);
        assertEquals("Severe", severity.getDisplay());
        assertEquals("HP:0012828", severity.getCode());
        FhirContext ctx = FhirContext.forR4();
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        System.out.println(parser.encodeResourceToString(observation));
    }



}

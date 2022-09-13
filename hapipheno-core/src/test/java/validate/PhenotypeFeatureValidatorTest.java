package validate;


import static org.junit.jupiter.api.Assertions.assertFalse;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.hapiphenocore.phenopacket.PhenotypicFeature;


public class PhenotypeFeatureValidatorTest {


    PhenotypicFeature createPhenotypicFeature() {
        PhenotypicFeature pfeature = new PhenotypicFeature();
        Observation obs = new Observation();
        pfeature.setStatus(Observation.ObservationStatus.FINAL);
        return pfeature;
    }

    @DisplayName("Reject incomplete phenopacket")
    @Test
    public void submitIncompletePhenopacket1_ifRejected_thenOk() {
        PhenotypicFeature pfeature = new PhenotypicFeature();
        pfeature.setStatus(Observation.ObservationStatus.FINAL);
        FhirValidator validator = FhirContext.forR4().newValidator();
        ValidationResult validationResult = validator.validateWithResult(pfeature);
        System.out.println(validationResult);
        assertFalse(validationResult.isSuccessful());
    }


}

package org.monarchinitiative.hapiphenoclient.fhir.validate;

import ca.uhn.fhir.rest.gclient.IValidate;
import ca.uhn.fhir.rest.gclient.IValidateUntyped;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class PhenotypeFeatureValidator implements IValidate  {
    @Override
    public IValidateUntyped resource(IBaseResource iBaseResource) {
        return null;
    }

    @Override
    public IValidateUntyped resource(String s) {
        return null;
    }
}

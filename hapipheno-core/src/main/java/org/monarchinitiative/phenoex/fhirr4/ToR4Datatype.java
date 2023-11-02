package org.monarchinitiative.phenoex.fhirr4;

import org.monarchinitiative.phenoex.model.Coding;

public interface ToR4Datatype {

  default org.hl7.fhir.r4.model.Coding visit(Coding coding){
    org.hl7.fhir.r4.model.Coding r4coding = new org.hl7.fhir.r4.model.Coding();
    r4coding.setDisplay(coding.getDisplay());
    r4coding.setCode(coding.getCode());
    r4coding.setSystem(coding.getSystem());
    return r4coding;
  }

}

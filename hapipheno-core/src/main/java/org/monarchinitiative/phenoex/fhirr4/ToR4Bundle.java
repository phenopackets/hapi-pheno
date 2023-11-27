package org.monarchinitiative.phenoex.fhirr4;

import java.util.List;
import lombok.Builder;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.Observation;
import org.monarchinitiative.phenoex.model.Coding;
import org.monarchinitiative.phenoex.model.PhenomicsExchangePacket;
import org.monarchinitiative.phenoex.model.Phenotype;


public class ToR4Bundle implements ToR4Resource  {
  private final HTTPVerb method;


  @Builder
  public ToR4Bundle(HTTPVerb method, BundleType type) {
    this.method = method;
  }




}

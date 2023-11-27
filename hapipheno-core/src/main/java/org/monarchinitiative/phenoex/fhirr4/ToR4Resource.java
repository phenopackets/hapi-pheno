package org.monarchinitiative.phenoex.fhirr4;

import java.util.List;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Observation;
import org.monarchinitiative.phenoex.model.Coding;
import org.monarchinitiative.phenoex.model.PhenomicsExchangePacket;
import org.monarchinitiative.phenoex.model.Phenotype;

public interface ToR4Resource extends  ToR4Datatype {

  default Observation visit(Phenotype phenotype){
    Observation o = new Observation();
    List<Coding> codings = phenotype.getCodings();
    if(codings != null){
      for (Coding coding : codings){
        o.getCode().addCoding(visit(coding));
      }
    }
    return o;
  }

  default Bundle visit(PhenomicsExchangePacket packet){
    final Bundle bundle = new Bundle();
    bundle.setType(BundleType.TRANSACTION);
    for(Phenotype phenotype: packet.cgetPhenotypes()){
      Observation o = visit(phenotype);
      bundle.addEntry().setResource(o);
    }
    return bundle;
  }

}

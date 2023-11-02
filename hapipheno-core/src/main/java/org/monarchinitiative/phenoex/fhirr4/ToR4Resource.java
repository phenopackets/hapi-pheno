package org.monarchinitiative.phenoex.fhirr4;

import java.util.List;
import org.hl7.fhir.r4.model.Observation;
import org.monarchinitiative.phenoex.model.Coding;
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

}

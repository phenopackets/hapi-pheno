package org.monarchinitiative.phenoex.model;

import java.util.List;

public interface Phenotype {

  List<Coding> getCodings();
  void setCodings(List<Coding> list);
  List<Coding> cgetCodings();
  Phenotype csetCodings(List<Coding> list);
  Coding addCodings();
  Phenotype addCodings(Coding object);



}

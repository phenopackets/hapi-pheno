package org.monarchinitiative.phenoex.model;

import java.util.List;

public interface PhenomicsExchangePacket {

  List<Phenotype> getPhenotypes();
  void setPhenotypes(List<Phenotype> list);
  List<Phenotype> cgetPhenotypes();
  PhenomicsExchangePacket csetPhenotypes(List<Phenotype> list);
  Phenotype addPhenotypes();
  PhenomicsExchangePacket addPhenotypes(Phenotype object);
}

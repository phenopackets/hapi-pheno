package org.monarchinitiative.phenoex.model;

import java.util.List;

/**
 * PhenomicsExchangePacket
 */
@SuppressWarnings("all")
public interface PhenomicsExchangePacket {
  /**
   * The Phenotypes
   */
  List<Phenotype> getPhenotypes();

  /**
   * The Phenotypes
   */
  void setPhenotypes(final List<Phenotype> phenotypes);

  /**
   * The Phenotypes
   */
  List<Phenotype> cgetPhenotypes();

  /**
   * The Phenotypes
   */
  PhenomicsExchangePacket csetPhenotypes(final List<Phenotype> phenotypes);

  /**
   * The Phenotypes
   */
  PhenomicsExchangePacket addPhenotypes(final Phenotype phenotypes);

  /**
   * The Phenotypes
   */
  Phenotype addPhenotypes();
}

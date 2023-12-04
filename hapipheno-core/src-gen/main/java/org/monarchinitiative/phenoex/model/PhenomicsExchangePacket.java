package org.monarchinitiative.phenoex.model;

import java.util.List;
import org.monarchinitiative.phenoex.model.base.Element;

/**
 * PhenomicsExchangePacket
 */
@SuppressWarnings("all")
public interface PhenomicsExchangePacket extends Element {
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
  List<Phenotype> getOrDefaultPhenotypes(final List<Phenotype> defaultValue);

  /**
   * The Phenotypes
   */
  List<Phenotype> getOrSetPhenotypes(final List<Phenotype> setValue);

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

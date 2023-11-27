package org.monarchinitiative.phenoex.model;

import java.util.List;

/**
 * Phenotype
 */
@SuppressWarnings("all")
public interface Phenotype {
  /**
   * JavaDoc for: codings
   */
  List<Coding> getCodings();

  /**
   * JavaDoc for: codings
   */
  void setCodings(final List<Coding> codings);

  /**
   * JavaDoc for: codings
   */
  List<Coding> cgetCodings();

  /**
   * JavaDoc for: codings
   */
  Phenotype csetCodings(final List<Coding> codings);

  /**
   * JavaDoc for: codings
   */
  Phenotype addCodings(final Coding codings);

  /**
   * JavaDoc for: codings
   */
  Coding addCodings();
}

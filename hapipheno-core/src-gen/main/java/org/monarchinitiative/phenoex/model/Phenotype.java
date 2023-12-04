package org.monarchinitiative.phenoex.model;

import java.util.List;
import org.monarchinitiative.phenoex.model.base.Element;

/**
 * Phenotype
 */
@SuppressWarnings("all")
public interface Phenotype extends Element {
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
  List<Coding> getOrDefaultCodings(final List<Coding> defaultValue);

  /**
   * JavaDoc for: codings
   */
  List<Coding> getOrSetCodings(final List<Coding> setValue);

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

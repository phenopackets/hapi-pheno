package org.monarchinitiative.phenoex.model;

import org.monarchinitiative.phenoex.model.base.Element;

/**
 * Coding
 */
@SuppressWarnings("all")
public interface Coding extends Element {
  /**
   * The code.
   */
  String getCode();

  /**
   * The code.
   */
  void setCode(final String code);

  /**
   * The code.
   */
  String getOrDefaultCode(final String defaultValue);

  /**
   * The code.
   */
  String getOrSetCode(final String setValue);

  /**
   * The code.
   */
  String cgetCode();

  /**
   * The code.
   */
  Coding csetCode(final String code);

  /**
   * The display
   */
  String getDisplay();

  /**
   * The display
   */
  void setDisplay(final String display);

  /**
   * The display
   */
  String getOrDefaultDisplay(final String defaultValue);

  /**
   * The display
   */
  String getOrSetDisplay(final String setValue);

  /**
   * The display
   */
  String cgetDisplay();

  /**
   * The display
   */
  Coding csetDisplay(final String display);

  /**
   * The system
   */
  String getSystem();

  /**
   * The system
   */
  void setSystem(final String system);

  /**
   * The system
   */
  String getOrDefaultSystem(final String defaultValue);

  /**
   * The system
   */
  String getOrSetSystem(final String setValue);

  /**
   * The system
   */
  String cgetSystem();

  /**
   * The system
   */
  Coding csetSystem(final String system);
}

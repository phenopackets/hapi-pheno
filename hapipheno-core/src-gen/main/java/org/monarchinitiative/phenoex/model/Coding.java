package org.monarchinitiative.phenoex.model;

/**
 * Coding
 */
@SuppressWarnings("all")
public interface Coding {
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
  String cgetSystem();

  /**
   * The system
   */
  Coding csetSystem(final String system);
}

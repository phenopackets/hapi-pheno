package org.monarchinitiative.phenoex.model.base;

/**
 * Entity Extension
 */
@SuppressWarnings("all")
public interface Extension extends Element {
  /**
   * JavaDoc for: key
   */
  String getKey();

  /**
   * JavaDoc for: key
   */
  void setKey(final String key);

  /**
   * JavaDoc for: key
   */
  String getOrDefaultKey(final String defaultValue);

  /**
   * JavaDoc for: key
   */
  String getOrSetKey(final String setValue);

  /**
   * JavaDoc for: key
   */
  String cgetKey();

  /**
   * JavaDoc for: key
   */
  Extension csetKey(final String key);

  /**
   * JavaDoc for: valueString
   */
  String getValueString();

  /**
   * JavaDoc for: valueString
   */
  void setValueString(final String valueString);

  /**
   * JavaDoc for: valueString
   */
  String getOrDefaultValueString(final String defaultValue);

  /**
   * JavaDoc for: valueString
   */
  String getOrSetValueString(final String setValue);

  /**
   * JavaDoc for: valueString
   */
  String cgetValueString();

  /**
   * JavaDoc for: valueString
   */
  Extension csetValueString(final String valueString);
}

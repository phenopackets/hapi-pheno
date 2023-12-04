package org.monarchinitiative.phenoex.model.base;

/**
 * Entity StringElement
 */
@SuppressWarnings("all")
public interface StringElement extends Element {
  /**
   * JavaDoc for: value
   */
  String getValue();

  /**
   * JavaDoc for: value
   */
  void setValue(final String value);

  /**
   * JavaDoc for: value
   */
  String getOrDefaultValue(final String defaultValue);

  /**
   * JavaDoc for: value
   */
  String getOrSetValue(final String setValue);

  /**
   * JavaDoc for: value
   */
  String cgetValue();

  /**
   * JavaDoc for: value
   */
  StringElement csetValue(final String value);
}

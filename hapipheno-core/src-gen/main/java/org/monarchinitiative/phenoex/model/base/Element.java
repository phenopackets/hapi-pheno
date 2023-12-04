package org.monarchinitiative.phenoex.model.base;

import java.util.List;

/**
 * Entity Element
 */
@SuppressWarnings("all")
public interface Element {
  /**
   * JavaDoc for: extensions
   */
  List<Extension> getExtensions();

  /**
   * JavaDoc for: extensions
   */
  void setExtensions(final List<Extension> extensions);

  /**
   * JavaDoc for: extensions
   */
  List<Extension> getOrDefaultExtensions(final List<Extension> defaultValue);

  /**
   * JavaDoc for: extensions
   */
  List<Extension> getOrSetExtensions(final List<Extension> setValue);

  /**
   * JavaDoc for: extensions
   */
  List<Extension> cgetExtensions();

  /**
   * JavaDoc for: extensions
   */
  Element csetExtensions(final List<Extension> extensions);

  /**
   * JavaDoc for: extensions
   */
  Element addExtensions(final Extension extensions);

  /**
   * JavaDoc for: extensions
   */
  Extension addExtensions();
}

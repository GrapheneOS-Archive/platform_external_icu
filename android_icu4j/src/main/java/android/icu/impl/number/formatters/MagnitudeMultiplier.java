/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number.formatters;

import android.icu.impl.number.Format;
import android.icu.impl.number.Format.BeforeFormat;
import android.icu.impl.number.FormatQuantity;
import android.icu.impl.number.ModifierHolder;
import android.icu.impl.number.Properties;

/**
 * @hide Only a subset of ICU is exposed in Android
 */
public class MagnitudeMultiplier extends Format.BeforeFormat {
  private static final MagnitudeMultiplier DEFAULT = new MagnitudeMultiplier(0);

  public static interface IProperties {

    static int DEFAULT_MAGNITUDE_MULTIPLIER = 0;

    /** @see #setMagnitudeMultiplier */
    public int getMagnitudeMultiplier();

    /**
     * Multiply all numbers by this power of ten before formatting. Negative multipliers reduce the
     * magnitude and make numbers smaller (closer to zero).
     *
     * @param magnitudeMultiplier The number of powers of ten to scale.
     * @return The property bag, for chaining.
     * @see BigDecimalMultiplier
     */
    public IProperties setMagnitudeMultiplier(int magnitudeMultiplier);
  }

  public static boolean useMagnitudeMultiplier(IProperties properties) {
    return properties.getMagnitudeMultiplier() != IProperties.DEFAULT_MAGNITUDE_MULTIPLIER;
  }

  // Properties
  final int delta;

  public static BeforeFormat getInstance(Properties properties) {
    if (properties.getMagnitudeMultiplier() == 0) {
      return DEFAULT;
    }
    return new MagnitudeMultiplier(properties.getMagnitudeMultiplier());
  }

  private MagnitudeMultiplier(int delta) {
    this.delta = delta;
  }

  @Override
  public void before(FormatQuantity input, ModifierHolder mods) {
    input.adjustMagnitude(delta);
  }

  @Override
  public void export(Properties properties) {
    properties.setMagnitudeMultiplier(delta);
  }
}

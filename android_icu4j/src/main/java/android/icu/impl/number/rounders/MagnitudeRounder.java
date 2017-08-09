/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number.rounders;

import android.icu.impl.number.FormatQuantity;
import android.icu.impl.number.Rounder;

/**
 * @hide Only a subset of ICU is exposed in Android
 */
public class MagnitudeRounder extends Rounder {

  public static interface IProperties extends IBasicRoundingProperties {}

  public static boolean useFractionFormat(IProperties properties) {
    return properties.getMinimumFractionDigits() != IProperties.DEFAULT_MINIMUM_FRACTION_DIGITS
        || properties.getMaximumFractionDigits() != IProperties.DEFAULT_MAXIMUM_FRACTION_DIGITS;
  }

  public static MagnitudeRounder getInstance(IBasicRoundingProperties properties) {
    return new MagnitudeRounder(properties);
  }

  private MagnitudeRounder(IBasicRoundingProperties properties) {
    super(properties);
  }

  @Override
  public void apply(FormatQuantity input) {
    input.roundToMagnitude(-maxFrac, mathContext);
    applyDefaults(input);
  }
}

/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number.formatters;

import android.icu.impl.number.Rounder;
import android.icu.impl.number.Rounder.IBasicRoundingProperties;
import android.icu.impl.number.rounders.IncrementRounder;
import android.icu.impl.number.rounders.MagnitudeRounder;
import android.icu.impl.number.rounders.NoRounder;
import android.icu.impl.number.rounders.SignificantDigitsRounder;

// TODO: Figure out a better place to put these methods.

/**
 * @hide Only a subset of ICU is exposed in Android
 */
public class RoundingFormat {

  public static interface IProperties
      extends IBasicRoundingProperties,
          IncrementRounder.IProperties,
          MagnitudeRounder.IProperties,
          SignificantDigitsRounder.IProperties {}

  public static Rounder getDefaultOrNoRounder(IProperties properties) {
    Rounder candidate = getDefaultOrNull(properties);
    if (candidate == null) {
      candidate = NoRounder.getInstance(properties);
    }
    return candidate;
  }

  public static Rounder getDefaultOrNull(IProperties properties) {
    if (SignificantDigitsRounder.useSignificantDigits(properties)) {
      return SignificantDigitsRounder.getInstance(properties);
    } else if (IncrementRounder.useRoundingIncrement(properties)) {
      return IncrementRounder.getInstance(properties);
    } else if (MagnitudeRounder.useFractionFormat(properties)) {
      return MagnitudeRounder.getInstance(properties);
    } else {
      return null;
    }
  }
}

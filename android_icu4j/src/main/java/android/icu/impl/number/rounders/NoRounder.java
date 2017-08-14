/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number.rounders;

import android.icu.impl.number.FormatQuantity;
import android.icu.impl.number.Rounder;

/** Sets the integer and fraction length based on the properties, but does not perform rounding. 
 * @hide Only a subset of ICU is exposed in Android*/
public final class NoRounder extends Rounder {

  public static NoRounder getInstance(IBasicRoundingProperties properties) {
    return new NoRounder(properties);
  }

  private NoRounder(IBasicRoundingProperties properties) {
    super(properties);
  }

  @Override
  public void apply(FormatQuantity input) {
    applyDefaults(input);
    input.roundToInfinity();
  }
}

/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number.modifiers;

import android.icu.impl.number.Format;
import android.icu.impl.number.FormatQuantity;
import android.icu.impl.number.Modifier;
import android.icu.impl.number.Modifier.AffixModifier;
import android.icu.impl.number.ModifierHolder;
import android.icu.impl.number.Properties;

/** A class containing a positive form and a negative form of {@link ConstantAffixModifier}. 
 * @hide Only a subset of ICU is exposed in Android*/
public class PositiveNegativeAffixModifier extends Format.BeforeFormat
    implements Modifier.PositiveNegativeModifier {
  private final AffixModifier positive;
  private final AffixModifier negative;

  /**
   * Constructs an instance using the two {@link ConstantMultiFieldModifier} classes for positive
   * and negative.
   *
   * @param positive The positive-form Modifier.
   * @param negative The negative-form Modifier.
   */
  public PositiveNegativeAffixModifier(AffixModifier positive, AffixModifier negative) {
    this.positive = positive;
    this.negative = negative;
  }

  @Override
  public Modifier getModifier(boolean isNegative) {
    return isNegative ? negative : positive;
  }

  @Override
  public void before(FormatQuantity input, ModifierHolder mods) {
    Modifier mod = getModifier(input.isNegative());
    mods.add(mod);
  }

  @Override
  public void export(Properties properties) {
    exportPositiveNegative(properties, positive, negative);
  }

  /** Internal method used to export a positive and negative modifier to a property bag. */
  static void exportPositiveNegative(Properties properties, Modifier positive, Modifier negative) {
    properties.setPositivePrefix(positive.getPrefix().isEmpty() ? null : positive.getPrefix());
    properties.setPositiveSuffix(positive.getSuffix().isEmpty() ? null : positive.getSuffix());
    properties.setNegativePrefix(negative.getPrefix().isEmpty() ? null : negative.getPrefix());
    properties.setNegativeSuffix(negative.getSuffix().isEmpty() ? null : negative.getSuffix());
  }
}

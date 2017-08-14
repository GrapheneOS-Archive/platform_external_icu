/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number.modifiers;

import android.icu.impl.number.Modifier;
import android.icu.impl.number.Modifier.AffixModifier;
import android.icu.impl.number.NumberStringBuilder;
import android.icu.impl.number.Properties;
import android.icu.text.NumberFormat.Field;

/**
 * An implementation of {@link Modifier} that allows for multiple types of fields in the same
 * modifier. Constructed based on the contents of two {@link NumberStringBuilder} instances (one for
 * the prefix, one for the suffix).
 * @hide Only a subset of ICU is exposed in Android
 */
public class ConstantMultiFieldModifier extends Modifier.BaseModifier implements AffixModifier {

  // TODO: Avoid making a new instance by default if prefix and suffix are empty
  public static final ConstantMultiFieldModifier EMPTY = new ConstantMultiFieldModifier();

  private final char[] prefixChars;
  private final char[] suffixChars;
  private final Field[] prefixFields;
  private final Field[] suffixFields;
  private final String prefix;
  private final String suffix;
  private final boolean strong;

  public ConstantMultiFieldModifier(
      NumberStringBuilder prefix, NumberStringBuilder suffix, boolean strong) {
    prefixChars = prefix.toCharArray();
    suffixChars = suffix.toCharArray();
    prefixFields = prefix.toFieldArray();
    suffixFields = suffix.toFieldArray();
    this.prefix = new String(prefixChars);
    this.suffix = new String(suffixChars);
    this.strong = strong;
  }

  private ConstantMultiFieldModifier() {
    prefixChars = new char[0];
    suffixChars = new char[0];
    prefixFields = new Field[0];
    suffixFields = new Field[0];
    prefix = "";
    suffix = "";
    strong = false;
  }

  @Override
  public int apply(NumberStringBuilder output, int leftIndex, int rightIndex) {
    // Insert the suffix first since inserting the prefix will change the rightIndex
    int length = output.insert(rightIndex, suffixChars, suffixFields);
    length += output.insert(leftIndex, prefixChars, prefixFields);
    return length;
  }

  @Override
  public int length() {
    return prefixChars.length + suffixChars.length;
  }

  @Override
  public boolean isStrong() {
    return strong;
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @Override
  public String getSuffix() {
    return suffix;
  }

  public boolean contentEquals(NumberStringBuilder prefix, NumberStringBuilder suffix) {
    return prefix.contentEquals(prefixChars, prefixFields)
        && suffix.contentEquals(suffixChars, suffixFields);
  }

  @Override
  public String toString() {
    return String.format(
        "<ConstantMultiFieldModifier(%d) prefix:'%s' suffix:'%s'>", length(), prefix, suffix);
  }

  @Override
  public void export(Properties properties) {
    throw new UnsupportedOperationException();
  }
}

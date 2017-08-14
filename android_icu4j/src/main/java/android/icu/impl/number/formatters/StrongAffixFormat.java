/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number.formatters;

import java.util.Deque;

import android.icu.impl.number.Format;
import android.icu.impl.number.FormatQuantity;
import android.icu.impl.number.ModifierHolder;
import android.icu.impl.number.NumberStringBuilder;
import android.icu.impl.number.Properties;

// TODO: This class isn't currently being used anywhere.  Consider removing it.

/** Attaches all prefixes and suffixes at this point in the render tree without bubbling up. 
 * @hide Only a subset of ICU is exposed in Android*/
public class StrongAffixFormat extends Format implements Format.AfterFormat {
  private final Format child;

  public StrongAffixFormat(Format child) {
    this.child = child;

    if (child == null) {
      throw new IllegalArgumentException("A child formatter is required for StrongAffixFormat");
    }
  }

  @Override
  public int process(
      Deque<FormatQuantity> inputs,
      ModifierHolder mods,
      NumberStringBuilder string,
      int startIndex) {
    int length = child.process(inputs, mods, string, startIndex);
    length += mods.applyAll(string, startIndex, startIndex + length);
    return length;
  }

  @Override
  public int after(
      ModifierHolder mods, NumberStringBuilder string, int leftIndex, int rightIndex) {
    return mods.applyAll(string, leftIndex, rightIndex);
  }

  @Override
  public void export(Properties properties) {
    // Nothing to do.
  }
}

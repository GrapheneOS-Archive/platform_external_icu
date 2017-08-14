/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number;

import java.math.BigDecimal;
import java.math.BigInteger;

/** @author sffc 
 * @hide Only a subset of ICU is exposed in Android*/
public class FormatQuantitySelector {
  public static FormatQuantityBCD from(int input) {
    return new FormatQuantity4(input);
  }

  public static FormatQuantityBCD from(long input) {
    return new FormatQuantity4(input);
  }

  public static FormatQuantityBCD from(double input) {
    return new FormatQuantity4(input);
  }

  public static FormatQuantityBCD from(BigInteger input) {
    return new FormatQuantity4(input);
  }

  public static FormatQuantityBCD from(BigDecimal input) {
    return new FormatQuantity4(input);
  }

  public static FormatQuantityBCD from(android.icu.math.BigDecimal input) {
    return from(input.toBigDecimal());
  }

  public static FormatQuantityBCD from(Number number) {
    if (number instanceof Long) {
      return from(number.longValue());
    } else if (number instanceof Integer) {
      return from(number.intValue());
    } else if (number instanceof Double) {
      return from(number.doubleValue());
    } else if (number instanceof BigInteger) {
      return from((BigInteger) number);
    } else if (number instanceof BigDecimal) {
      return from((BigDecimal) number);
    } else if (number instanceof android.icu.math.BigDecimal) {
      return from((android.icu.math.BigDecimal) number);
    } else {
      throw new IllegalArgumentException(
          "Number is of an unsupported type: " + number.getClass().getName());
    }
  }
}

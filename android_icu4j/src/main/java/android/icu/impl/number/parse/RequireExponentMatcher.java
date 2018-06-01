/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number.parse;

/**
 * @author sffc
 * @hide Only a subset of ICU is exposed in Android
 *
 */
public class RequireExponentMatcher extends ValidationMatcher {

    @Override
    public void postProcess(ParsedNumber result) {
        if (0 == (result.flags & ParsedNumber.FLAG_HAS_EXPONENT)) {
            result.flags |= ParsedNumber.FLAG_FAIL;
        }
    }

    @Override
    public String toString() {
        return "<RequireExponent>";
    }

}

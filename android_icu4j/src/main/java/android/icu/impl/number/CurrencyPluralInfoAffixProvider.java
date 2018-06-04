/* GENERATED SOURCE. DO NOT MODIFY. */
// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package android.icu.impl.number;

import android.icu.impl.StandardPlural;
import android.icu.impl.number.PatternStringParser.ParsedPatternInfo;
import android.icu.text.CurrencyPluralInfo;

/**
 * @hide Only a subset of ICU is exposed in Android
 */
public class CurrencyPluralInfoAffixProvider implements AffixPatternProvider {
    private final AffixPatternProvider[] affixesByPlural;

    public CurrencyPluralInfoAffixProvider(CurrencyPluralInfo cpi) {
        affixesByPlural = new ParsedPatternInfo[StandardPlural.COUNT];
        for (StandardPlural plural : StandardPlural.VALUES) {
            affixesByPlural[plural.ordinal()] = PatternStringParser
                    .parseToPatternInfo(cpi.getCurrencyPluralPattern(plural.getKeyword()));
        }
    }

    @Override
    public char charAt(int flags, int i) {
        int pluralOrdinal = (flags & Flags.PLURAL_MASK);
        return affixesByPlural[pluralOrdinal].charAt(flags, i);
    }

    @Override
    public int length(int flags) {
        int pluralOrdinal = (flags & Flags.PLURAL_MASK);
        return affixesByPlural[pluralOrdinal].length(flags);
    }

    @Override
    public String getString(int flags) {
        int pluralOrdinal = (flags & Flags.PLURAL_MASK);
        return affixesByPlural[pluralOrdinal].getString(flags);
    }

    @Override
    public boolean positiveHasPlusSign() {
        return affixesByPlural[StandardPlural.OTHER.ordinal()].positiveHasPlusSign();
    }

    @Override
    public boolean hasNegativeSubpattern() {
        return affixesByPlural[StandardPlural.OTHER.ordinal()].hasNegativeSubpattern();
    }

    @Override
    public boolean negativeHasMinusSign() {
        return affixesByPlural[StandardPlural.OTHER.ordinal()].negativeHasMinusSign();
    }

    @Override
    public boolean hasCurrencySign() {
        return affixesByPlural[StandardPlural.OTHER.ordinal()].hasCurrencySign();
    }

    @Override
    public boolean containsSymbolType(int type) {
        return affixesByPlural[StandardPlural.OTHER.ordinal()].containsSymbolType(type);
    }

    @Override
    public boolean hasBody() {
        return affixesByPlural[StandardPlural.OTHER.ordinal()].hasBody();
    }
}
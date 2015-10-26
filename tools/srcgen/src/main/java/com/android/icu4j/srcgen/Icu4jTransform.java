/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.icu4j.srcgen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.currysrc.Main;
import com.google.currysrc.api.Rules;
import com.google.currysrc.api.input.CompoundDirectoryInputFileGenerator;
import com.google.currysrc.api.input.DirectoryInputFileGenerator;
import com.google.currysrc.api.input.InputFileGenerator;
import com.google.currysrc.api.match.SourceMatchers;
import com.google.currysrc.api.output.BasicOutputSourceFileGenerator;
import com.google.currysrc.api.output.OutputSourceFileGenerator;
import com.google.currysrc.api.transform.AstTransformRule;
import com.google.currysrc.api.transform.AstTransformer;
import com.google.currysrc.api.transform.DocumentTransformRule;
import com.google.currysrc.api.transform.DocumentTransformer;
import com.google.currysrc.api.transform.ast.BodyDeclarationLocater;
import com.google.currysrc.api.transform.ast.BodyDeclarationLocaters;
import com.google.currysrc.api.transform.ast.TypeLocater;
import com.google.currysrc.transformers.HidePublicClasses;
import com.google.currysrc.transformers.InsertHeader;
import com.google.currysrc.transformers.ModifyQualifiedNames;
import com.google.currysrc.transformers.ModifyStringLiterals;
import com.google.currysrc.transformers.RenamePackage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Applies Android's ICU4J source code transformation rules. If you make any changes to this class
 * then you should re-run the generate_android_icu4j.sh script.
 */
public class Icu4jTransform {

  // The list of public ICU API classes exposed on Android. If you change this, you should change
  // the INITIAL_DEPRECATED_SET below to include entries from the new classes.
  static final String[] PUBLIC_API_CLASSES = new String[] {
      /* ASCII order please. */
      "android.icu.lang.UCharacter",
      "android.icu.lang.UCharacterCategory",
      "android.icu.lang.UCharacterDirection",
      "android.icu.lang.UCharacterEnums",
      "android.icu.lang.UProperty",
      "android.icu.lang.UScript",
      "android.icu.math.BigDecimal",
      "android.icu.math.MathContext",
      "android.icu.text.AlphabeticIndex",
      "android.icu.text.BreakIterator",
      "android.icu.text.CollationKey",
      "android.icu.text.Collator",
      "android.icu.text.CollationElementIterator",
      "android.icu.text.CompactDecimalFormat",
      "android.icu.text.CurrencyPluralInfo",
      "android.icu.text.DateFormat",
      "android.icu.text.DateFormatSymbols",
      "android.icu.text.DateIntervalInfo",
      "android.icu.text.DateIntervalFormat",
      "android.icu.text.DateTimePatternGenerator",
      "android.icu.text.DecimalFormat",
      "android.icu.text.DecimalFormatSymbols",
      "android.icu.text.DisplayContext",
      "android.icu.text.IDNA",
      "android.icu.text.LocaleDisplayNames",
      "android.icu.text.MeasureFormat",
      "android.icu.text.MessageFormat",
      "android.icu.text.MessagePattern",
      "android.icu.text.Normalizer",
      "android.icu.text.Normalizer2",
      "android.icu.text.NumberFormat",
      "android.icu.text.NumberingSystem",
      "android.icu.text.PluralFormat",
      "android.icu.text.PluralRules",
      "android.icu.text.RawCollationKey",
      "android.icu.text.RelativeDateTimeFormatter",
      "android.icu.text.Replaceable",
      "android.icu.text.RuleBasedCollator",
      "android.icu.text.ScientificNumberFormatter",
      "android.icu.text.SearchIterator",
      "android.icu.text.SelectFormat",
      "android.icu.text.SimpleDateFormat",
      "android.icu.text.StringPrepParseException",
      "android.icu.text.StringSearch",
      "android.icu.text.SymbolTable",
      "android.icu.text.TimeZoneFormat",
      "android.icu.text.TimeZoneNames",
      "android.icu.text.UCharacterIterator",
      "android.icu.text.UFormat",
      "android.icu.text.UnicodeFilter",
      "android.icu.text.UnicodeMatcher",
      "android.icu.text.UnicodeSet",
      "android.icu.text.UnicodeSetIterator",
      "android.icu.text.UnicodeSetSpanner",
      "android.icu.util.BuddhistCalendar",
      "android.icu.util.ByteArrayWrapper",
      "android.icu.util.Calendar",
      "android.icu.util.CECalendar",
      "android.icu.util.ChineseCalendar",
      "android.icu.util.CopticCalendar",
      "android.icu.util.Currency",
      "android.icu.util.CurrencyAmount",
      "android.icu.util.DateInterval",
      "android.icu.util.EthiopticCalendar",
      "android.icu.util.Freezable",
      "android.icu.util.GregorianCalendar",
      "android.icu.util.HebrewCalendar",
      "android.icu.util.ICUUncheckedIOException",
      "android.icu.util.IndianCalendar",
      "android.icu.util.IslamicCalendar",
      "android.icu.util.JapaneseCalendar",
      "android.icu.util.Measure",
      "android.icu.util.MeasureUnit",
      "android.icu.util.RangeValueIterator",
      "android.icu.util.TaiwanCalendar",
      "android.icu.util.TimeUnit",
      "android.icu.util.TimeZone",
      "android.icu.util.ULocale",
      "android.icu.util.ValueIterator",
      "android.icu.util.VersionInfo",
  };

  /**
   * The set of deprecated ICU types/methods/fields that must not be part of the public API as they
   * were deprecated when Android first exposed ICU as a public API. Methods deprecated by ICU after
   * this will be visible and deprecated in Android.
   */
  // This list was originally generated by the CaptureDeprecatedElements tool when run against
  // ICU56 with the original PUBLIC_API_CLASSES.
  private static final String[] INITIAL_DEPRECATED_SET = new String[] {
      /* ASCII order please. */
      "class:android.icu.text.DateTimePatternGenerator$FormatParser",
      "class:android.icu.text.DateTimePatternGenerator$VariableField",
      "class:android.icu.text.Normalizer$Mode",
      "class:android.icu.text.PluralRules$Factory",
      "class:android.icu.text.PluralRules$FixedDecimal",
      "class:android.icu.text.PluralRules$FixedDecimalRange",
      "class:android.icu.text.PluralRules$FixedDecimalSamples",
      "class:android.icu.text.TimeZoneNames$Factory",
      "class:android.icu.util.Calendar$FormatConfiguration",
      "class:android.icu.util.MeasureUnit$Factory",
      "enum:android.icu.text.PluralRules$SampleType",
      "enum:android.icu.text.PluralRules$StandardPluralCategories",
      "enum:android.icu.util.ULocale$Minimize",
      "field:android.icu.lang.UProperty#ISO_COMMENT",
      "field:android.icu.lang.UProperty#UNDEFINED",
      "field:android.icu.lang.UProperty#UNICODE_1_NAME",
      "field:android.icu.lang.UScript#DUPLOYAN_SHORTAND",
      "field:android.icu.text.DateFormat#ABBR_STANDALONE_MONTH",
      "field:android.icu.text.DateFormat#DATE_SKELETONS",
      "field:android.icu.text.DateFormat#HOUR_GENERIC_TZ",
      "field:android.icu.text.DateFormat#HOUR_MINUTE_GENERIC_TZ",
      "field:android.icu.text.DateFormat#HOUR_MINUTE_TZ",
      "field:android.icu.text.DateFormat#HOUR_TZ",
      "field:android.icu.text.DateFormat#STANDALONE_MONTH",
      "field:android.icu.text.DateFormat#TIME_SKELETONS",
      "field:android.icu.text.DateFormat#ZONE_SKELETONS",
      "field:android.icu.text.DateFormatSymbols#DT_CONTEXT_COUNT",
      "field:android.icu.text.DateFormatSymbols#DT_WIDTH_COUNT",
      "field:android.icu.text.DateFormatSymbols#NUMERIC",
      "field:android.icu.text.DateTimePatternGenerator#MATCH_MINUTE_FIELD_LENGTH",
      "field:android.icu.text.DateTimePatternGenerator#MATCH_SECOND_FIELD_LENGTH",
      "field:android.icu.text.IDNA#ALLOW_UNASSIGNED",
      "field:android.icu.text.Normalizer#COMPARE_NORM_OPTIONS_SHIFT",
      "field:android.icu.text.Normalizer#COMPOSE",
      "field:android.icu.text.Normalizer#COMPOSE_COMPAT",
      "field:android.icu.text.Normalizer#DECOMP",
      "field:android.icu.text.Normalizer#DECOMP_COMPAT",
      "field:android.icu.text.Normalizer#DEFAULT",
      "field:android.icu.text.Normalizer#DONE",
      "field:android.icu.text.Normalizer#FCD",
      "field:android.icu.text.Normalizer#IGNORE_HANGUL",
      "field:android.icu.text.Normalizer#NFC",
      "field:android.icu.text.Normalizer#NFD",
      "field:android.icu.text.Normalizer#NFKC",
      "field:android.icu.text.Normalizer#NFKD",
      "field:android.icu.text.Normalizer#NONE",
      "field:android.icu.text.Normalizer#NO_OP",
      "field:android.icu.text.Normalizer#UNICODE_3_2",
      "field:android.icu.text.PluralRules#CATEGORY_SEPARATOR",
      "field:android.icu.text.PluralRules#KEYWORD_RULE_SEPARATOR",
      "field:android.icu.text.PluralRules$FixedDecimal#decimalDigits",
      "field:android.icu.text.PluralRules$FixedDecimal#decimalDigitsWithoutTrailingZeros",
      "field:android.icu.text.PluralRules$FixedDecimal#hasIntegerValue",
      "field:android.icu.text.PluralRules$FixedDecimal#integerValue",
      "field:android.icu.text.PluralRules$FixedDecimal#isNegative",
      "field:android.icu.text.PluralRules$FixedDecimal#source",
      "field:android.icu.text.PluralRules$FixedDecimal#visibleDecimalDigitCount",
      "field:android.icu.text.PluralRules$FixedDecimal#visibleDecimalDigitCountWithoutTrailingZeros",
      "field:android.icu.text.PluralRules$FixedDecimalRange#end",
      "field:android.icu.text.PluralRules$FixedDecimalRange#start",
      "field:android.icu.text.PluralRules$FixedDecimalSamples#bounded",
      "field:android.icu.text.PluralRules$FixedDecimalSamples#sampleType",
      "field:android.icu.text.PluralRules$FixedDecimalSamples#samples",
      "field:android.icu.text.PluralRules$StandardPluralCategories#COUNT",
      "field:android.icu.text.PluralRules$StandardPluralCategories#VALUES",
      "field:android.icu.text.UnicodeSetIterator#endElement",
      "field:android.icu.text.UnicodeSetIterator#nextElement",
      "field:android.icu.util.Calendar#WEEKDAY",
      "field:android.icu.util.Calendar#WEEKEND",
      "field:android.icu.util.Calendar#WEEKEND_CEASE",
      "field:android.icu.util.Calendar#WEEKEND_ONSET",
      "field:android.icu.util.MeasureUnit#subType",
      "field:android.icu.util.MeasureUnit#type",
      "field:android.icu.util.VersionInfo#ICU_DATA_VERSION",
      "field:android.icu.util.VersionInfo#ICU_DATA_VERSION_PATH",
      "field:android.icu.util.VersionInfo#UCOL_TAILORINGS_VERSION",
      "method:android.icu.lang.UCharacter#getCharFromName1_0(String)",
      "method:android.icu.lang.UCharacter#getISOComment(int)",
      "method:android.icu.lang.UCharacter#getName1_0(int)",
      "method:android.icu.lang.UCharacter#getName1_0Iterator()",
      "method:android.icu.lang.UCharacter#getPropertyValueEnumNoThrow(int,CharSequence)",
      "method:android.icu.lang.UCharacter#getStringPropertyValue(int,int,int)",
      "method:android.icu.lang.UCharacter#isJavaLetter(int)",
      "method:android.icu.lang.UCharacter#isJavaLetterOrDigit(int)",
      "method:android.icu.lang.UCharacter#isSpace(int)",
      "method:android.icu.lang.UCharacter#toTitleFirst(ULocale,String)",
      "method:android.icu.text.AlphabeticIndex#getFirstCharactersInScripts()",
      "method:android.icu.text.BreakIterator#getBreakInstance(ULocale,int)",
      "method:android.icu.text.CollationElementIterator#getRuleBasedCollator()",
      "method:android.icu.text.CollationElementIterator#hashCode()",
      "method:android.icu.text.Collator#doCompare(CharSequence,CharSequence)",
      "method:android.icu.text.Collator#setStrength2(int)",
      "method:android.icu.text.Collator#setVariableTop(String)",
      "method:android.icu.text.Collator#setVariableTop(int)",
      "method:android.icu.text.CompactDecimalFormat#CompactDecimalFormat(String,DecimalFormatSymbols,CompactStyle,PluralRules,long[],Map<String,String[][]>,Map<String,String[]>,Collection<String>)",
      "method:android.icu.text.CurrencyPluralInfo#hashCode()",
      "method:android.icu.text.DateFormatSymbols#getDateFormatBundle(Calendar,Locale)",
      "method:android.icu.text.DateFormatSymbols#getDateFormatBundle(Calendar,ULocale)",
      "method:android.icu.text.DateFormatSymbols#getDateFormatBundle(Class<? extends Calendar>,Locale)",
      "method:android.icu.text.DateFormatSymbols#getDateFormatBundle(Class<? extends Calendar>,ULocale)",
      "method:android.icu.text.DateFormatSymbols#getLeapMonthPattern(int,int)",
      "method:android.icu.text.DateFormatSymbols#initializeData(ULocale,CalendarData)",
      "method:android.icu.text.DateFormatSymbols#setLeapMonthPattern(String,int,int)",
      "method:android.icu.text.DateIntervalFormat#DateIntervalFormat(String,DateIntervalInfo,SimpleDateFormat)",
      "method:android.icu.text.DateIntervalFormat#getPatterns(Calendar,Calendar,Output<String>)",
      "method:android.icu.text.DateIntervalFormat#getRawPatterns()",
      "method:android.icu.text.DateIntervalFormat#parseObject(String,ParsePosition)",
      "method:android.icu.text.DateIntervalInfo#DateIntervalInfo()",
      "method:android.icu.text.DateIntervalInfo#genPatternInfo(String,boolean)",
      "method:android.icu.text.DateIntervalInfo#getPatterns()",
      "method:android.icu.text.DateIntervalInfo#getRawPatterns()",
      "method:android.icu.text.DateTimePatternGenerator#addPatternWithSkeleton(String,String,boolean,PatternInfo)",
      "method:android.icu.text.DateTimePatternGenerator#getAppendFormatNumber(String)",
      "method:android.icu.text.DateTimePatternGenerator#getCanonicalSkeletonAllowingDuplicates(String)",
      "method:android.icu.text.DateTimePatternGenerator#getDefaultHourFormatChar()",
      "method:android.icu.text.DateTimePatternGenerator#getFields(String)",
      "method:android.icu.text.DateTimePatternGenerator#getFrozenInstance(ULocale)",
      "method:android.icu.text.DateTimePatternGenerator#getRedundants(Collection<String>)",
      "method:android.icu.text.DateTimePatternGenerator#getSkeletonAllowingDuplicates(String)",
      "method:android.icu.text.DateTimePatternGenerator#isSingleField(String)",
      "method:android.icu.text.DateTimePatternGenerator#setDefaultHourFormatChar(char)",
      "method:android.icu.text.DateTimePatternGenerator#skeletonsAreSimilar(String,String)",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#FormatParser()",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#getItems()",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#hasDateAndTimeFields()",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#quoteLiteral(String)",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#set(String)",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#set(String,boolean)",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#toString()",
      "method:android.icu.text.DateTimePatternGenerator$FormatParser#toString(int,int)",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#VariableField(String)",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#VariableField(String,boolean)",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#getCanonicalCode(int)",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#getType()",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#isNumeric()",
      "method:android.icu.text.DateTimePatternGenerator$VariableField#toString()",
      "method:android.icu.text.DecimalFormat#getEffectiveCurrency()",
      "method:android.icu.text.DecimalFormatSymbols#getMinusString()",
      "method:android.icu.text.DecimalFormatSymbols#getPlusString()",
      "method:android.icu.text.IDNA#IDNA()",
      "method:android.icu.text.IDNA#addError(Info,Error)",
      "method:android.icu.text.IDNA#addLabelError(Info,Error)",
      "method:android.icu.text.IDNA#compare(String,String,int)",
      "method:android.icu.text.IDNA#compare(StringBuffer,StringBuffer,int)",
      "method:android.icu.text.IDNA#compare(UCharacterIterator,UCharacterIterator,int)",
      "method:android.icu.text.IDNA#convertIDNToASCII(String,int)",
      "method:android.icu.text.IDNA#convertIDNToASCII(StringBuffer,int)",
      "method:android.icu.text.IDNA#convertIDNToASCII(UCharacterIterator,int)",
      "method:android.icu.text.IDNA#convertIDNToUnicode(String,int)",
      "method:android.icu.text.IDNA#convertIDNToUnicode(StringBuffer,int)",
      "method:android.icu.text.IDNA#convertIDNToUnicode(UCharacterIterator,int)",
      "method:android.icu.text.IDNA#convertToASCII(String,int)",
      "method:android.icu.text.IDNA#convertToASCII(StringBuffer,int)",
      "method:android.icu.text.IDNA#convertToASCII(UCharacterIterator,int)",
      "method:android.icu.text.IDNA#convertToUnicode(String,int)",
      "method:android.icu.text.IDNA#convertToUnicode(StringBuffer,int)",
      "method:android.icu.text.IDNA#convertToUnicode(UCharacterIterator,int)",
      "method:android.icu.text.IDNA#hasCertainErrors(Info,EnumSet<Error>)",
      "method:android.icu.text.IDNA#hasCertainLabelErrors(Info,EnumSet<Error>)",
      "method:android.icu.text.IDNA#isBiDi(Info)",
      "method:android.icu.text.IDNA#isOkBiDi(Info)",
      "method:android.icu.text.IDNA#promoteAndResetLabelErrors(Info)",
      "method:android.icu.text.IDNA#resetInfo(Info)",
      "method:android.icu.text.IDNA#setBiDi(Info)",
      "method:android.icu.text.IDNA#setNotOkBiDi(Info)",
      "method:android.icu.text.IDNA#setTransitionalDifferent(Info)",
      "method:android.icu.text.LocaleDisplayNames#LocaleDisplayNames()",
      "method:android.icu.text.LocaleDisplayNames#scriptDisplayNameInContext(String)",
      "method:android.icu.text.MeasureFormat#formatMeasureRange(Measure,Measure)",
      "method:android.icu.text.MeasureFormat#getRangeFormat(ULocale,FormatWidth)",
      "method:android.icu.text.MeasureFormat#getRangePattern(ULocale,FormatWidth)",
      "method:android.icu.text.Normalizer#Normalizer(CharacterIterator,Mode,int)",
      "method:android.icu.text.Normalizer#Normalizer(String,Mode,int)",
      "method:android.icu.text.Normalizer#Normalizer(UCharacterIterator,Mode,int)",
      "method:android.icu.text.Normalizer#clone()",
      "method:android.icu.text.Normalizer#compose(String,boolean)",
      "method:android.icu.text.Normalizer#compose(String,boolean,int)",
      "method:android.icu.text.Normalizer#compose(char[],char[],boolean,int)",
      "method:android.icu.text.Normalizer#compose(char[],int,int,char[],int,int,boolean,int)",
      "method:android.icu.text.Normalizer#concatenate(String,String,Mode,int)",
      "method:android.icu.text.Normalizer#concatenate(char[],char[],Mode,int)",
      "method:android.icu.text.Normalizer#concatenate(char[],int,int,char[],int,int,char[],int,int,Normalizer.Mode,int)",
      "method:android.icu.text.Normalizer#current()",
      "method:android.icu.text.Normalizer#decompose(String,boolean)",
      "method:android.icu.text.Normalizer#decompose(String,boolean,int)",
      "method:android.icu.text.Normalizer#decompose(char[],char[],boolean,int)",
      "method:android.icu.text.Normalizer#decompose(char[],int,int,char[],int,int,boolean,int)",
      "method:android.icu.text.Normalizer#endIndex()",
      "method:android.icu.text.Normalizer#first()",
      "method:android.icu.text.Normalizer#getBeginIndex()",
      "method:android.icu.text.Normalizer#getEndIndex()",
      "method:android.icu.text.Normalizer#getFC_NFKC_Closure(int)",
      "method:android.icu.text.Normalizer#getFC_NFKC_Closure(int,char[])",
      "method:android.icu.text.Normalizer#getIndex()",
      "method:android.icu.text.Normalizer#getLength()",
      "method:android.icu.text.Normalizer#getMode()",
      "method:android.icu.text.Normalizer#getOption(int)",
      "method:android.icu.text.Normalizer#getText()",
      "method:android.icu.text.Normalizer#getText(char[])",
      "method:android.icu.text.Normalizer#isNormalized(String,Mode,int)",
      "method:android.icu.text.Normalizer#isNormalized(char[],int,int,Mode,int)",
      "method:android.icu.text.Normalizer#isNormalized(int,Mode,int)",
      "method:android.icu.text.Normalizer#last()",
      "method:android.icu.text.Normalizer#next()",
      "method:android.icu.text.Normalizer#normalize(String,Mode)",
      "method:android.icu.text.Normalizer#normalize(String,Mode,int)",
      "method:android.icu.text.Normalizer#normalize(char[],char[],Mode,int)",
      "method:android.icu.text.Normalizer#normalize(char[],int,int,char[],int,int,Mode,int)",
      "method:android.icu.text.Normalizer#normalize(int,Mode)",
      "method:android.icu.text.Normalizer#normalize(int,Mode,int)",
      "method:android.icu.text.Normalizer#previous()",
      "method:android.icu.text.Normalizer#quickCheck(String,Mode)",
      "method:android.icu.text.Normalizer#quickCheck(String,Mode,int)",
      "method:android.icu.text.Normalizer#quickCheck(char[],Mode,int)",
      "method:android.icu.text.Normalizer#quickCheck(char[],int,int,Mode,int)",
      "method:android.icu.text.Normalizer#reset()",
      "method:android.icu.text.Normalizer#setIndex(int)",
      "method:android.icu.text.Normalizer#setIndexOnly(int)",
      "method:android.icu.text.Normalizer#setMode(Mode)",
      "method:android.icu.text.Normalizer#setOption(int,boolean)",
      "method:android.icu.text.Normalizer#setText(CharacterIterator)",
      "method:android.icu.text.Normalizer#setText(String)",
      "method:android.icu.text.Normalizer#setText(StringBuffer)",
      "method:android.icu.text.Normalizer#setText(UCharacterIterator)",
      "method:android.icu.text.Normalizer#setText(char[])",
      "method:android.icu.text.Normalizer#startIndex()",
      "method:android.icu.text.Normalizer$Mode#Mode()",
      "method:android.icu.text.Normalizer$Mode#getNormalizer2(int)",
      "method:android.icu.text.Normalizer2#Normalizer2()",
      "method:android.icu.text.NumberFormat#getEffectiveCurrency()",
      "method:android.icu.text.NumberFormat#getPattern(Locale,int)",
      "method:android.icu.text.PluralFormat#setLocale(ULocale)",
      "method:android.icu.text.PluralRules#addSample(String,Number,int,Set<Double>)",
      "method:android.icu.text.PluralRules#compareTo(PluralRules)",
      "method:android.icu.text.PluralRules#computeLimited(String,SampleType)",
      "method:android.icu.text.PluralRules#getAllKeywordValues(String,SampleType)",
      "method:android.icu.text.PluralRules#getDecimalSamples(String,SampleType)",
      "method:android.icu.text.PluralRules#getKeywordStatus(String,int,Set<Double>,Output<Double>,SampleType)",
      "method:android.icu.text.PluralRules#getRules(String)",
      "method:android.icu.text.PluralRules#getSamples(String,SampleType)",
      "method:android.icu.text.PluralRules#hashCode()",
      "method:android.icu.text.PluralRules#isLimited(String)",
      "method:android.icu.text.PluralRules#isLimited(String,SampleType)",
      "method:android.icu.text.PluralRules#matches(FixedDecimal,String)",
      "method:android.icu.text.PluralRules#select(FixedDecimal)",
      "method:android.icu.text.PluralRules#select(double,int,long)",
      "method:android.icu.text.PluralRules$Factory#Factory()",
      "method:android.icu.text.PluralRules$Factory#forLocale(ULocale)",
      "method:android.icu.text.PluralRules$Factory#forLocale(ULocale,PluralType)",
      "method:android.icu.text.PluralRules$Factory#getAvailableULocales()",
      "method:android.icu.text.PluralRules$Factory#getDefaultFactory()",
      "method:android.icu.text.PluralRules$Factory#getFunctionalEquivalent(ULocale,boolean[])",
      "method:android.icu.text.PluralRules$Factory#hasOverride(ULocale)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(String)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(double)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(double,int)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(double,int,long)",
      "method:android.icu.text.PluralRules$FixedDecimal#FixedDecimal(long)",
      "method:android.icu.text.PluralRules$FixedDecimal#compareTo(FixedDecimal)",
      "method:android.icu.text.PluralRules$FixedDecimal#decimals(double)",
      "method:android.icu.text.PluralRules$FixedDecimal#doubleValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#equals(Object)",
      "method:android.icu.text.PluralRules$FixedDecimal#floatValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#get(Operand)",
      "method:android.icu.text.PluralRules$FixedDecimal#getBaseFactor()",
      "method:android.icu.text.PluralRules$FixedDecimal#getDecimalDigits()",
      "method:android.icu.text.PluralRules$FixedDecimal#getDecimalDigitsWithoutTrailingZeros()",
      "method:android.icu.text.PluralRules$FixedDecimal#getIntegerValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#getOperand(String)",
      "method:android.icu.text.PluralRules$FixedDecimal#getShiftedValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#getSource()",
      "method:android.icu.text.PluralRules$FixedDecimal#getVisibleDecimalDigitCount()",
      "method:android.icu.text.PluralRules$FixedDecimal#getVisibleDecimalDigitCountWithoutTrailingZeros()",
      "method:android.icu.text.PluralRules$FixedDecimal#hasIntegerValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#hashCode()",
      "method:android.icu.text.PluralRules$FixedDecimal#intValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#isHasIntegerValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#isNegative()",
      "method:android.icu.text.PluralRules$FixedDecimal#longValue()",
      "method:android.icu.text.PluralRules$FixedDecimal#toString()",
      "method:android.icu.text.PluralRules$FixedDecimalRange#FixedDecimalRange(FixedDecimal,FixedDecimal)",
      "method:android.icu.text.PluralRules$FixedDecimalRange#toString()",
      "method:android.icu.text.PluralRules$FixedDecimalSamples#addSamples(Set<Double>)",
      "method:android.icu.text.PluralRules$FixedDecimalSamples#getSamples()",
      "method:android.icu.text.PluralRules$FixedDecimalSamples#getStartEndSamples(Set<FixedDecimal>)",
      "method:android.icu.text.PluralRules$FixedDecimalSamples#toString()",
      "method:android.icu.text.RuleBasedCollator#doCompare(CharSequence,CharSequence)",
      "method:android.icu.text.RuleBasedCollator#internalGetCEs(CharSequence)",
      "method:android.icu.text.RuleBasedCollator#isHiraganaQuaternary()",
      "method:android.icu.text.RuleBasedCollator#setHiraganaQuaternary(boolean)",
      "method:android.icu.text.RuleBasedCollator#setHiraganaQuaternaryDefault()",
      "method:android.icu.text.RuleBasedCollator#setVariableTop(String)",
      "method:android.icu.text.RuleBasedCollator#setVariableTop(int)",
      "method:android.icu.text.SearchIterator#setMatchNotFound()",
      "method:android.icu.text.SimpleDateFormat#SimpleDateFormat(String,DateFormatSymbols,ULocale)",
      "method:android.icu.text.SimpleDateFormat#getInstance(Calendar.FormatConfiguration)",
      "method:android.icu.text.SimpleDateFormat#intervalFormatByAlgorithm(Calendar,Calendar,StringBuffer,FieldPosition)",
      "method:android.icu.text.SimpleDateFormat#subFormat(StringBuffer,char,int,int,int,DisplayContext,FieldPosition,Calendar)",
      "method:android.icu.text.SimpleDateFormat#subFormat(char,int,int,int,DisplayContext,FieldPosition,Calendar)",
      "method:android.icu.text.SimpleDateFormat#zeroPaddingNumber(NumberFormat,StringBuffer,int,int,int)",
      "method:android.icu.text.StringPrepParseException#hashCode()",
      "method:android.icu.text.StringSearch#setMatchNotFound()",
      "method:android.icu.text.TimeZoneNames#getDisplayNames(String,NameType[],long,String[],int)",
      "method:android.icu.text.TimeZoneNames#loadAllDisplayNames()",
      "method:android.icu.text.TimeZoneNames$Factory#Factory()",
      "method:android.icu.text.TimeZoneNames$Factory#getTimeZoneNames(ULocale)",
      "method:android.icu.text.UnicodeFilter#UnicodeFilter()",
      "method:android.icu.text.UnicodeSet#addBridges(UnicodeSet)",
      "method:android.icu.text.UnicodeSet#applyPattern(String,ParsePosition,SymbolTable,int)",
      "method:android.icu.text.UnicodeSet#compare(Iterator<T>,Iterator<T>)",
      "method:android.icu.text.UnicodeSet#findIn(CharSequence,int,boolean)",
      "method:android.icu.text.UnicodeSet#findLastIn(CharSequence,int,boolean)",
      "method:android.icu.text.UnicodeSet#getDefaultXSymbolTable()",
      "method:android.icu.text.UnicodeSet#getRegexEquivalent()",
      "method:android.icu.text.UnicodeSet#getSingleCodePoint(CharSequence)",
      "method:android.icu.text.UnicodeSet#matchesAt(CharSequence,int)",
      "method:android.icu.text.UnicodeSet#setDefaultXSymbolTable(XSymbolTable)",
      "method:android.icu.text.UnicodeSet#spanAndCount(CharSequence,int,SpanCondition,OutputInt)",
      "method:android.icu.text.UnicodeSet#stripFrom(CharSequence,boolean)",
      "method:android.icu.text.UnicodeSetIterator#getSet()",
      "method:android.icu.text.UnicodeSetIterator#loadRange(int)",
      "method:android.icu.util.Calendar#getDateTimePattern(Calendar,ULocale,int)",
      "method:android.icu.util.Calendar#getDayOfWeekType(int)",
      "method:android.icu.util.Calendar#getRelatedYear()",
      "method:android.icu.util.Calendar#getWeekendTransition(int)",
      "method:android.icu.util.Calendar#haveDefaultCentury()",
      "method:android.icu.util.Calendar#setRelatedYear(int)",
      "method:android.icu.util.Calendar$FormatConfiguration#getCalendar()",
      "method:android.icu.util.Calendar$FormatConfiguration#getDateFormatSymbols()",
      "method:android.icu.util.Calendar$FormatConfiguration#getLocale()",
      "method:android.icu.util.Calendar$FormatConfiguration#getOverrideString()",
      "method:android.icu.util.Calendar$FormatConfiguration#getPatternString()",
      "method:android.icu.util.ChineseCalendar#ChineseCalendar(TimeZone,ULocale,int,TimeZone)",
      "method:android.icu.util.ChineseCalendar#haveDefaultCentury()",
      "method:android.icu.util.CopticCalendar#getJDEpochOffset()",
      "method:android.icu.util.CopticCalendar#handleComputeFields(int)",
      "method:android.icu.util.CopticCalendar#handleGetExtendedYear()",
      "method:android.icu.util.Currency#parse(ULocale,String,int,ParsePosition)",
      "method:android.icu.util.HebrewCalendar#isLeapYear(int)",
      "method:android.icu.util.HebrewCalendar#validateField(int)",
      "method:android.icu.util.JapaneseCalendar#haveDefaultCentury()",
      "method:android.icu.util.MeasureUnit#MeasureUnit(String,String)",
      "method:android.icu.util.MeasureUnit#addUnit(String,String,Factory)",
      "method:android.icu.util.MeasureUnit#internalGetInstance(String,String)",
      "method:android.icu.util.MeasureUnit#resolveUnitPerUnit(MeasureUnit,MeasureUnit)",
      "method:android.icu.util.TimeZone#TimeZone(String)",
      "method:android.icu.util.ULocale#getDisplayScriptInContext()",
      "method:android.icu.util.ULocale#getDisplayScriptInContext(String,String)",
      "method:android.icu.util.ULocale#getDisplayScriptInContext(String,ULocale)",
      "method:android.icu.util.ULocale#getDisplayScriptInContext(ULocale)",
      "method:android.icu.util.ULocale#minimizeSubtags(ULocale,Minimize)",
      "method:android.icu.util.VersionInfo#getVersionString(int,int)",
      "method:android.icu.util.VersionInfo#javaVersion()",
  };

  private static final boolean DEBUG = true;

  private Icu4jTransform() {
  }

  /**
   * Usage:
   * java com.android.icu4j.srcgen.Icu4JTransform {one or more source directories} {target dir}
   */
  public static void main(String[] args) throws Exception {
    new Main(DEBUG).execute(new Icu4jRules(args));
  }

  private static class Icu4jRules implements Rules {

    private static final String SOURCE_CODE_HEADER = "/* GENERATED SOURCE. DO NOT MODIFY. */\n";

    private final InputFileGenerator inputFileGenerator;

    private final BasicOutputSourceFileGenerator outputSourceFileGenerator;

    public Icu4jRules(String[] args) {
      if (args.length < 2) {
        throw new IllegalArgumentException("At least 2 arguments required.");
      }

      inputFileGenerator = createInputFileGenerator(args);
      outputSourceFileGenerator = createOutputFileGenerator(args[args.length - 1]);
    }

    public List<AstTransformRule> getAstTransformRules(File file) {
      return Lists.newArrayList(
          createMandatoryRule(new RenamePackage("com.ibm.icu", "android.icu")),
          createOptionalRule(new ModifyQualifiedNames("com.ibm.icu", "android.icu")),
          createOptionalRule(new ModifyStringLiterals("com.ibm.icu", "android.icu")),
          createOptionalRule(new ModifyStringLiterals("com/ibm/icu", "android/icu"))
      );
    }

    private static AstTransformRule createMandatoryRule(AstTransformer transformer) {
      return new AstTransformRule(transformer, SourceMatchers.all(), true /* mustModify */);
    }

    private static AstTransformRule createOptionalRule(AstTransformer transformer) {
      return new AstTransformRule(transformer, SourceMatchers.all(), false /* mustModify */);
    }

    @Override
    public List<DocumentTransformRule> getDocumentTransformRules(File file) {
      ImmutableList.Builder<TypeLocater> apiClassesWhitelistBuilder = ImmutableList.builder();
      for (String publicClassName : PUBLIC_API_CLASSES) {
        apiClassesWhitelistBuilder.add(new TypeLocater(publicClassName));
      }
      ImmutableList.Builder<BodyDeclarationLocater> deprecationBlacklistBuilder =
          ImmutableList.builder();
      for (String deprecatedElementLocaterString : INITIAL_DEPRECATED_SET) {
          BodyDeclarationLocater locater =
              BodyDeclarationLocaters.fromStringForm(deprecatedElementLocaterString);
          deprecationBlacklistBuilder.add(locater);
      }

      DocumentTransformRule[] rules = new DocumentTransformRule[] {
          createMandatoryRule(new InsertHeader(SOURCE_CODE_HEADER)),
          createOptionalRule(new HidePublicClasses(apiClassesWhitelistBuilder.build(),
              "Only a subset of ICU is exposed in Android")),
          createOptionalRule(new HideOriginalDeprecatedSet(deprecationBlacklistBuilder.build())),
          createOptionalRule(new HideDraftProvisionalInternal()),
          createOptionalRule(new MungeIcuJavaDocTags()),
          // Comment the line below to see what Android looks like with ICU.
          createOptionalRule(new HidePublicClasses(Collections.<TypeLocater>emptyList(),
               "All android.icu classes are currently hidden")),
      };
      return Lists.newArrayList(rules);
    }

    private static DocumentTransformRule createMandatoryRule(DocumentTransformer transformer) {
      return new DocumentTransformRule(transformer, SourceMatchers.all(), true /* mustModify */);
    }

    private static DocumentTransformRule createOptionalRule(DocumentTransformer transformer) {
      return new DocumentTransformRule(transformer, SourceMatchers.all(), false /* mustModify*/);
    }

    @Override
    public InputFileGenerator getInputFileGenerator() {
      return inputFileGenerator;
    }

    @Override
    public OutputSourceFileGenerator getOutputSourceFileGenerator() {
      return outputSourceFileGenerator;
    }

    private static CompoundDirectoryInputFileGenerator createInputFileGenerator(String[] args) {
      List<InputFileGenerator> dirs = new ArrayList<>(args.length - 1);
      for (int i = 0; i < args.length - 1; i++) {
        File inputDir = new File(args[i]);
        if (!isValidDir(inputDir)) {
          throw new IllegalArgumentException("Input dir [" + inputDir + "] does not exist.");
        }
        dirs.add(new DirectoryInputFileGenerator(inputDir));
      }
      return new CompoundDirectoryInputFileGenerator(dirs);
    }

    private static BasicOutputSourceFileGenerator createOutputFileGenerator(String outputDirName) {
      File outputDir = new File(outputDirName);
      if (!isValidDir(outputDir)) {
        throw new IllegalArgumentException("Output dir [" + outputDir + "] does not exist.");
      }
      return new BasicOutputSourceFileGenerator(outputDir);
    }

    private static boolean isValidDir(File outputDir) {
      return outputDir.exists() && outputDir.isDirectory();
    }
  }
}

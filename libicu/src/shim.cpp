/* GENERATED SOURCE. DO NOT MODIFY. */
/* Run external/icu/tools/icu4c_srcgen/generate_ndk.py to regenerate */
// © 2018 and later: Unicode, Inc. and others.'
// License & terms of use: http://www.unicode.org/copyright.html

#include <unicode/uchar.h>
#include <unicode/uloc.h>
#include <unicode/ustring.h>
#include <unicode/utypes.h>
#include <unicode/uversion.h>

/* Disable the macros in urename.h renaming the below C function definitions. */
#undef u_charAge
#undef u_charDigitValue
#undef u_charDirection
#undef u_charFromName
#undef u_charMirror
#undef u_charName
#undef u_charType
#undef u_countChar32
#undef u_digit
#undef u_enumCharNames
#undef u_enumCharTypes
#undef u_errorName
#undef u_foldCase
#undef u_forDigit
#undef u_getBidiPairedBracket
#undef u_getCombiningClass
#undef u_getIntPropertyMaxValue
#undef u_getIntPropertyMinValue
#undef u_getIntPropertyValue
#undef u_getNumericValue
#undef u_getPropertyEnum
#undef u_getPropertyName
#undef u_getPropertyValueEnum
#undef u_getPropertyValueName
#undef u_getUnicodeVersion
#undef u_getVersion
#undef u_hasBinaryProperty
#undef u_isIDIgnorable
#undef u_isIDPart
#undef u_isIDStart
#undef u_isISOControl
#undef u_isJavaIDPart
#undef u_isJavaIDStart
#undef u_isJavaSpaceChar
#undef u_isMirrored
#undef u_isUAlphabetic
#undef u_isULowercase
#undef u_isUUppercase
#undef u_isUWhiteSpace
#undef u_isWhitespace
#undef u_isalnum
#undef u_isalpha
#undef u_isbase
#undef u_isblank
#undef u_iscntrl
#undef u_isdefined
#undef u_isdigit
#undef u_isgraph
#undef u_islower
#undef u_isprint
#undef u_ispunct
#undef u_isspace
#undef u_istitle
#undef u_isupper
#undef u_isxdigit
#undef u_memcasecmp
#undef u_memchr
#undef u_memchr32
#undef u_memcmp
#undef u_memcmpCodePointOrder
#undef u_memcpy
#undef u_memmove
#undef u_memrchr
#undef u_memrchr32
#undef u_memset
#undef u_strCaseCompare
#undef u_strCompare
#undef u_strFindFirst
#undef u_strFindLast
#undef u_strFoldCase
#undef u_strFromUTF32
#undef u_strFromUTF32WithSub
#undef u_strFromUTF8
#undef u_strFromUTF8Lenient
#undef u_strFromUTF8WithSub
#undef u_strHasMoreChar32Than
#undef u_strToLower
#undef u_strToTitle
#undef u_strToUTF32
#undef u_strToUTF32WithSub
#undef u_strToUTF8
#undef u_strToUTF8WithSub
#undef u_strToUpper
#undef u_strcasecmp
#undef u_strcat
#undef u_strchr
#undef u_strchr32
#undef u_strcmp
#undef u_strcmpCodePointOrder
#undef u_strcpy
#undef u_strcspn
#undef u_strlen
#undef u_strncasecmp
#undef u_strncat
#undef u_strncmp
#undef u_strncmpCodePointOrder
#undef u_strncpy
#undef u_strpbrk
#undef u_strrchr
#undef u_strrchr32
#undef u_strrstr
#undef u_strspn
#undef u_strstr
#undef u_strtok_r
#undef u_tolower
#undef u_totitle
#undef u_toupper
#undef u_versionToString
#undef uloc_acceptLanguage
#undef uloc_addLikelySubtags
#undef uloc_canonicalize
#undef uloc_countAvailable
#undef uloc_forLanguageTag
#undef uloc_getAvailable
#undef uloc_getBaseName
#undef uloc_getCharacterOrientation
#undef uloc_getCountry
#undef uloc_getDisplayCountry
#undef uloc_getDisplayKeyword
#undef uloc_getDisplayKeywordValue
#undef uloc_getDisplayLanguage
#undef uloc_getDisplayName
#undef uloc_getDisplayScript
#undef uloc_getDisplayVariant
#undef uloc_getISO3Country
#undef uloc_getISO3Language
#undef uloc_getISOCountries
#undef uloc_getISOLanguages
#undef uloc_getKeywordValue
#undef uloc_getLanguage
#undef uloc_getLineOrientation
#undef uloc_getName
#undef uloc_getScript
#undef uloc_getVariant
#undef uloc_isRightToLeft
#undef uloc_minimizeSubtags
#undef uloc_openKeywords
#undef uloc_setKeywordValue
#undef uloc_toLanguageTag
#undef uloc_toLegacyKey
#undef uloc_toLegacyType
#undef uloc_toUnicodeLocaleKey
#undef uloc_toUnicodeLocaleType

extern "C" {
void u_charAge(UChar32 c, UVersionInfo versionArray) {
  U_ICU_ENTRY_POINT_RENAME(u_charAge)(c, versionArray);
}
int32_t u_charDigitValue(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_charDigitValue)(c);
}
UCharDirection u_charDirection(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_charDirection)(c);
}
UChar32 u_charFromName(UCharNameChoice nameChoice, const char * name, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_charFromName)(nameChoice, name, pErrorCode);
}
UChar32 u_charMirror(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_charMirror)(c);
}
int32_t u_charName(UChar32 code, UCharNameChoice nameChoice, char * buffer, int32_t bufferLength, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_charName)(code, nameChoice, buffer, bufferLength, pErrorCode);
}
int8_t u_charType(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_charType)(c);
}
int32_t u_countChar32(const UChar * s, int32_t length) {
  return U_ICU_ENTRY_POINT_RENAME(u_countChar32)(s, length);
}
int32_t u_digit(UChar32 ch, int8_t radix) {
  return U_ICU_ENTRY_POINT_RENAME(u_digit)(ch, radix);
}
void u_enumCharNames(UChar32 start, UChar32 limit, UEnumCharNamesFn * fn, void * context, UCharNameChoice nameChoice, UErrorCode * pErrorCode) {
  U_ICU_ENTRY_POINT_RENAME(u_enumCharNames)(start, limit, fn, context, nameChoice, pErrorCode);
}
void u_enumCharTypes(UCharEnumTypeRange * enumRange, const void * context) {
  U_ICU_ENTRY_POINT_RENAME(u_enumCharTypes)(enumRange, context);
}
const char * u_errorName(UErrorCode code) {
  return U_ICU_ENTRY_POINT_RENAME(u_errorName)(code);
}
UChar32 u_foldCase(UChar32 c, uint32_t options) {
  return U_ICU_ENTRY_POINT_RENAME(u_foldCase)(c, options);
}
UChar32 u_forDigit(int32_t digit, int8_t radix) {
  return U_ICU_ENTRY_POINT_RENAME(u_forDigit)(digit, radix);
}
UChar32 u_getBidiPairedBracket(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_getBidiPairedBracket)(c);
}
uint8_t u_getCombiningClass(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_getCombiningClass)(c);
}
int32_t u_getIntPropertyMaxValue(UProperty which) {
  return U_ICU_ENTRY_POINT_RENAME(u_getIntPropertyMaxValue)(which);
}
int32_t u_getIntPropertyMinValue(UProperty which) {
  return U_ICU_ENTRY_POINT_RENAME(u_getIntPropertyMinValue)(which);
}
int32_t u_getIntPropertyValue(UChar32 c, UProperty which) {
  return U_ICU_ENTRY_POINT_RENAME(u_getIntPropertyValue)(c, which);
}
double u_getNumericValue(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_getNumericValue)(c);
}
UProperty u_getPropertyEnum(const char * alias) {
  return U_ICU_ENTRY_POINT_RENAME(u_getPropertyEnum)(alias);
}
const char * u_getPropertyName(UProperty property, UPropertyNameChoice nameChoice) {
  return U_ICU_ENTRY_POINT_RENAME(u_getPropertyName)(property, nameChoice);
}
int32_t u_getPropertyValueEnum(UProperty property, const char * alias) {
  return U_ICU_ENTRY_POINT_RENAME(u_getPropertyValueEnum)(property, alias);
}
const char * u_getPropertyValueName(UProperty property, int32_t value, UPropertyNameChoice nameChoice) {
  return U_ICU_ENTRY_POINT_RENAME(u_getPropertyValueName)(property, value, nameChoice);
}
void u_getUnicodeVersion(UVersionInfo versionArray) {
  U_ICU_ENTRY_POINT_RENAME(u_getUnicodeVersion)(versionArray);
}
void u_getVersion(UVersionInfo versionArray) {
  U_ICU_ENTRY_POINT_RENAME(u_getVersion)(versionArray);
}
UBool u_hasBinaryProperty(UChar32 c, UProperty which) {
  return U_ICU_ENTRY_POINT_RENAME(u_hasBinaryProperty)(c, which);
}
UBool u_isIDIgnorable(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isIDIgnorable)(c);
}
UBool u_isIDPart(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isIDPart)(c);
}
UBool u_isIDStart(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isIDStart)(c);
}
UBool u_isISOControl(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isISOControl)(c);
}
UBool u_isJavaIDPart(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isJavaIDPart)(c);
}
UBool u_isJavaIDStart(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isJavaIDStart)(c);
}
UBool u_isJavaSpaceChar(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isJavaSpaceChar)(c);
}
UBool u_isMirrored(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isMirrored)(c);
}
UBool u_isUAlphabetic(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isUAlphabetic)(c);
}
UBool u_isULowercase(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isULowercase)(c);
}
UBool u_isUUppercase(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isUUppercase)(c);
}
UBool u_isUWhiteSpace(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isUWhiteSpace)(c);
}
UBool u_isWhitespace(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isWhitespace)(c);
}
UBool u_isalnum(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isalnum)(c);
}
UBool u_isalpha(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isalpha)(c);
}
UBool u_isbase(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isbase)(c);
}
UBool u_isblank(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isblank)(c);
}
UBool u_iscntrl(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_iscntrl)(c);
}
UBool u_isdefined(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isdefined)(c);
}
UBool u_isdigit(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isdigit)(c);
}
UBool u_isgraph(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isgraph)(c);
}
UBool u_islower(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_islower)(c);
}
UBool u_isprint(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isprint)(c);
}
UBool u_ispunct(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_ispunct)(c);
}
UBool u_isspace(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isspace)(c);
}
UBool u_istitle(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_istitle)(c);
}
UBool u_isupper(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isupper)(c);
}
UBool u_isxdigit(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_isxdigit)(c);
}
int32_t u_memcasecmp(const UChar * s1, const UChar * s2, int32_t length, uint32_t options) {
  return U_ICU_ENTRY_POINT_RENAME(u_memcasecmp)(s1, s2, length, options);
}
UChar * u_memchr(const UChar * s, UChar c, int32_t count) {
  return U_ICU_ENTRY_POINT_RENAME(u_memchr)(s, c, count);
}
UChar * u_memchr32(const UChar * s, UChar32 c, int32_t count) {
  return U_ICU_ENTRY_POINT_RENAME(u_memchr32)(s, c, count);
}
int32_t u_memcmp(const UChar * buf1, const UChar * buf2, int32_t count) {
  return U_ICU_ENTRY_POINT_RENAME(u_memcmp)(buf1, buf2, count);
}
int32_t u_memcmpCodePointOrder(const UChar * s1, const UChar * s2, int32_t count) {
  return U_ICU_ENTRY_POINT_RENAME(u_memcmpCodePointOrder)(s1, s2, count);
}
UChar * u_memcpy(UChar * dest, const UChar * src, int32_t count) {
  return U_ICU_ENTRY_POINT_RENAME(u_memcpy)(dest, src, count);
}
UChar * u_memmove(UChar * dest, const UChar * src, int32_t count) {
  return U_ICU_ENTRY_POINT_RENAME(u_memmove)(dest, src, count);
}
UChar * u_memrchr(const UChar * s, UChar c, int32_t count) {
  return U_ICU_ENTRY_POINT_RENAME(u_memrchr)(s, c, count);
}
UChar * u_memrchr32(const UChar * s, UChar32 c, int32_t count) {
  return U_ICU_ENTRY_POINT_RENAME(u_memrchr32)(s, c, count);
}
UChar * u_memset(UChar * dest, UChar c, int32_t count) {
  return U_ICU_ENTRY_POINT_RENAME(u_memset)(dest, c, count);
}
int32_t u_strCaseCompare(const UChar * s1, int32_t length1, const UChar * s2, int32_t length2, uint32_t options, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strCaseCompare)(s1, length1, s2, length2, options, pErrorCode);
}
int32_t u_strCompare(const UChar * s1, int32_t length1, const UChar * s2, int32_t length2, UBool codePointOrder) {
  return U_ICU_ENTRY_POINT_RENAME(u_strCompare)(s1, length1, s2, length2, codePointOrder);
}
UChar * u_strFindFirst(const UChar * s, int32_t length, const UChar * substring, int32_t subLength) {
  return U_ICU_ENTRY_POINT_RENAME(u_strFindFirst)(s, length, substring, subLength);
}
UChar * u_strFindLast(const UChar * s, int32_t length, const UChar * substring, int32_t subLength) {
  return U_ICU_ENTRY_POINT_RENAME(u_strFindLast)(s, length, substring, subLength);
}
int32_t u_strFoldCase(UChar * dest, int32_t destCapacity, const UChar * src, int32_t srcLength, uint32_t options, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strFoldCase)(dest, destCapacity, src, srcLength, options, pErrorCode);
}
UChar * u_strFromUTF32(UChar * dest, int32_t destCapacity, int32_t * pDestLength, const UChar32 * src, int32_t srcLength, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strFromUTF32)(dest, destCapacity, pDestLength, src, srcLength, pErrorCode);
}
UChar * u_strFromUTF32WithSub(UChar * dest, int32_t destCapacity, int32_t * pDestLength, const UChar32 * src, int32_t srcLength, UChar32 subchar, int32_t * pNumSubstitutions, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strFromUTF32WithSub)(dest, destCapacity, pDestLength, src, srcLength, subchar, pNumSubstitutions, pErrorCode);
}
UChar * u_strFromUTF8(UChar * dest, int32_t destCapacity, int32_t * pDestLength, const char * src, int32_t srcLength, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strFromUTF8)(dest, destCapacity, pDestLength, src, srcLength, pErrorCode);
}
UChar * u_strFromUTF8Lenient(UChar * dest, int32_t destCapacity, int32_t * pDestLength, const char * src, int32_t srcLength, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strFromUTF8Lenient)(dest, destCapacity, pDestLength, src, srcLength, pErrorCode);
}
UChar * u_strFromUTF8WithSub(UChar * dest, int32_t destCapacity, int32_t * pDestLength, const char * src, int32_t srcLength, UChar32 subchar, int32_t * pNumSubstitutions, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strFromUTF8WithSub)(dest, destCapacity, pDestLength, src, srcLength, subchar, pNumSubstitutions, pErrorCode);
}
UBool u_strHasMoreChar32Than(const UChar * s, int32_t length, int32_t number) {
  return U_ICU_ENTRY_POINT_RENAME(u_strHasMoreChar32Than)(s, length, number);
}
int32_t u_strToLower(UChar * dest, int32_t destCapacity, const UChar * src, int32_t srcLength, const char * locale, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strToLower)(dest, destCapacity, src, srcLength, locale, pErrorCode);
}
int32_t u_strToTitle(UChar * dest, int32_t destCapacity, const UChar * src, int32_t srcLength, UBreakIterator * titleIter, const char * locale, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strToTitle)(dest, destCapacity, src, srcLength, titleIter, locale, pErrorCode);
}
UChar32 * u_strToUTF32(UChar32 * dest, int32_t destCapacity, int32_t * pDestLength, const UChar * src, int32_t srcLength, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strToUTF32)(dest, destCapacity, pDestLength, src, srcLength, pErrorCode);
}
UChar32 * u_strToUTF32WithSub(UChar32 * dest, int32_t destCapacity, int32_t * pDestLength, const UChar * src, int32_t srcLength, UChar32 subchar, int32_t * pNumSubstitutions, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strToUTF32WithSub)(dest, destCapacity, pDestLength, src, srcLength, subchar, pNumSubstitutions, pErrorCode);
}
char * u_strToUTF8(char * dest, int32_t destCapacity, int32_t * pDestLength, const UChar * src, int32_t srcLength, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strToUTF8)(dest, destCapacity, pDestLength, src, srcLength, pErrorCode);
}
char * u_strToUTF8WithSub(char * dest, int32_t destCapacity, int32_t * pDestLength, const UChar * src, int32_t srcLength, UChar32 subchar, int32_t * pNumSubstitutions, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strToUTF8WithSub)(dest, destCapacity, pDestLength, src, srcLength, subchar, pNumSubstitutions, pErrorCode);
}
int32_t u_strToUpper(UChar * dest, int32_t destCapacity, const UChar * src, int32_t srcLength, const char * locale, UErrorCode * pErrorCode) {
  return U_ICU_ENTRY_POINT_RENAME(u_strToUpper)(dest, destCapacity, src, srcLength, locale, pErrorCode);
}
int32_t u_strcasecmp(const UChar * s1, const UChar * s2, uint32_t options) {
  return U_ICU_ENTRY_POINT_RENAME(u_strcasecmp)(s1, s2, options);
}
UChar * u_strcat(UChar * dst, const UChar * src) {
  return U_ICU_ENTRY_POINT_RENAME(u_strcat)(dst, src);
}
UChar * u_strchr(const UChar * s, UChar c) {
  return U_ICU_ENTRY_POINT_RENAME(u_strchr)(s, c);
}
UChar * u_strchr32(const UChar * s, UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_strchr32)(s, c);
}
int32_t u_strcmp(const UChar * s1, const UChar * s2) {
  return U_ICU_ENTRY_POINT_RENAME(u_strcmp)(s1, s2);
}
int32_t u_strcmpCodePointOrder(const UChar * s1, const UChar * s2) {
  return U_ICU_ENTRY_POINT_RENAME(u_strcmpCodePointOrder)(s1, s2);
}
UChar * u_strcpy(UChar * dst, const UChar * src) {
  return U_ICU_ENTRY_POINT_RENAME(u_strcpy)(dst, src);
}
int32_t u_strcspn(const UChar * string, const UChar * matchSet) {
  return U_ICU_ENTRY_POINT_RENAME(u_strcspn)(string, matchSet);
}
int32_t u_strlen(const UChar * s) {
  return U_ICU_ENTRY_POINT_RENAME(u_strlen)(s);
}
int32_t u_strncasecmp(const UChar * s1, const UChar * s2, int32_t n, uint32_t options) {
  return U_ICU_ENTRY_POINT_RENAME(u_strncasecmp)(s1, s2, n, options);
}
UChar * u_strncat(UChar * dst, const UChar * src, int32_t n) {
  return U_ICU_ENTRY_POINT_RENAME(u_strncat)(dst, src, n);
}
int32_t u_strncmp(const UChar * ucs1, const UChar * ucs2, int32_t n) {
  return U_ICU_ENTRY_POINT_RENAME(u_strncmp)(ucs1, ucs2, n);
}
int32_t u_strncmpCodePointOrder(const UChar * s1, const UChar * s2, int32_t n) {
  return U_ICU_ENTRY_POINT_RENAME(u_strncmpCodePointOrder)(s1, s2, n);
}
UChar * u_strncpy(UChar * dst, const UChar * src, int32_t n) {
  return U_ICU_ENTRY_POINT_RENAME(u_strncpy)(dst, src, n);
}
UChar * u_strpbrk(const UChar * string, const UChar * matchSet) {
  return U_ICU_ENTRY_POINT_RENAME(u_strpbrk)(string, matchSet);
}
UChar * u_strrchr(const UChar * s, UChar c) {
  return U_ICU_ENTRY_POINT_RENAME(u_strrchr)(s, c);
}
UChar * u_strrchr32(const UChar * s, UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_strrchr32)(s, c);
}
UChar * u_strrstr(const UChar * s, const UChar * substring) {
  return U_ICU_ENTRY_POINT_RENAME(u_strrstr)(s, substring);
}
int32_t u_strspn(const UChar * string, const UChar * matchSet) {
  return U_ICU_ENTRY_POINT_RENAME(u_strspn)(string, matchSet);
}
UChar * u_strstr(const UChar * s, const UChar * substring) {
  return U_ICU_ENTRY_POINT_RENAME(u_strstr)(s, substring);
}
UChar * u_strtok_r(UChar * src, const UChar * delim, UChar ** saveState) {
  return U_ICU_ENTRY_POINT_RENAME(u_strtok_r)(src, delim, saveState);
}
UChar32 u_tolower(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_tolower)(c);
}
UChar32 u_totitle(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_totitle)(c);
}
UChar32 u_toupper(UChar32 c) {
  return U_ICU_ENTRY_POINT_RENAME(u_toupper)(c);
}
void u_versionToString(const UVersionInfo versionArray, char * versionString) {
  U_ICU_ENTRY_POINT_RENAME(u_versionToString)(versionArray, versionString);
}
int32_t uloc_acceptLanguage(char * result, int32_t resultAvailable, UAcceptResult * outResult, const char ** acceptList, int32_t acceptListCount, UEnumeration * availableLocales, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_acceptLanguage)(result, resultAvailable, outResult, acceptList, acceptListCount, availableLocales, status);
}
int32_t uloc_addLikelySubtags(const char * localeID, char * maximizedLocaleID, int32_t maximizedLocaleIDCapacity, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_addLikelySubtags)(localeID, maximizedLocaleID, maximizedLocaleIDCapacity, err);
}
int32_t uloc_canonicalize(const char * localeID, char * name, int32_t nameCapacity, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_canonicalize)(localeID, name, nameCapacity, err);
}
int32_t uloc_countAvailable() {
  return U_ICU_ENTRY_POINT_RENAME(uloc_countAvailable)();
}
int32_t uloc_forLanguageTag(const char * langtag, char * localeID, int32_t localeIDCapacity, int32_t * parsedLength, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_forLanguageTag)(langtag, localeID, localeIDCapacity, parsedLength, err);
}
const char * uloc_getAvailable(int32_t n) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getAvailable)(n);
}
int32_t uloc_getBaseName(const char * localeID, char * name, int32_t nameCapacity, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getBaseName)(localeID, name, nameCapacity, err);
}
ULayoutType uloc_getCharacterOrientation(const char * localeId, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getCharacterOrientation)(localeId, status);
}
int32_t uloc_getCountry(const char * localeID, char * country, int32_t countryCapacity, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getCountry)(localeID, country, countryCapacity, err);
}
int32_t uloc_getDisplayCountry(const char * locale, const char * displayLocale, UChar * country, int32_t countryCapacity, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getDisplayCountry)(locale, displayLocale, country, countryCapacity, status);
}
int32_t uloc_getDisplayKeyword(const char * keyword, const char * displayLocale, UChar * dest, int32_t destCapacity, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getDisplayKeyword)(keyword, displayLocale, dest, destCapacity, status);
}
int32_t uloc_getDisplayKeywordValue(const char * locale, const char * keyword, const char * displayLocale, UChar * dest, int32_t destCapacity, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getDisplayKeywordValue)(locale, keyword, displayLocale, dest, destCapacity, status);
}
int32_t uloc_getDisplayLanguage(const char * locale, const char * displayLocale, UChar * language, int32_t languageCapacity, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getDisplayLanguage)(locale, displayLocale, language, languageCapacity, status);
}
int32_t uloc_getDisplayName(const char * localeID, const char * inLocaleID, UChar * result, int32_t maxResultSize, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getDisplayName)(localeID, inLocaleID, result, maxResultSize, err);
}
int32_t uloc_getDisplayScript(const char * locale, const char * displayLocale, UChar * script, int32_t scriptCapacity, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getDisplayScript)(locale, displayLocale, script, scriptCapacity, status);
}
int32_t uloc_getDisplayVariant(const char * locale, const char * displayLocale, UChar * variant, int32_t variantCapacity, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getDisplayVariant)(locale, displayLocale, variant, variantCapacity, status);
}
const char * uloc_getISO3Country(const char * localeID) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getISO3Country)(localeID);
}
const char * uloc_getISO3Language(const char * localeID) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getISO3Language)(localeID);
}
const char *const * uloc_getISOCountries() {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getISOCountries)();
}
const char *const * uloc_getISOLanguages() {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getISOLanguages)();
}
int32_t uloc_getKeywordValue(const char * localeID, const char * keywordName, char * buffer, int32_t bufferCapacity, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getKeywordValue)(localeID, keywordName, buffer, bufferCapacity, status);
}
int32_t uloc_getLanguage(const char * localeID, char * language, int32_t languageCapacity, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getLanguage)(localeID, language, languageCapacity, err);
}
ULayoutType uloc_getLineOrientation(const char * localeId, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getLineOrientation)(localeId, status);
}
int32_t uloc_getName(const char * localeID, char * name, int32_t nameCapacity, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getName)(localeID, name, nameCapacity, err);
}
int32_t uloc_getScript(const char * localeID, char * script, int32_t scriptCapacity, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getScript)(localeID, script, scriptCapacity, err);
}
int32_t uloc_getVariant(const char * localeID, char * variant, int32_t variantCapacity, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_getVariant)(localeID, variant, variantCapacity, err);
}
UBool uloc_isRightToLeft(const char * locale) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_isRightToLeft)(locale);
}
int32_t uloc_minimizeSubtags(const char * localeID, char * minimizedLocaleID, int32_t minimizedLocaleIDCapacity, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_minimizeSubtags)(localeID, minimizedLocaleID, minimizedLocaleIDCapacity, err);
}
UEnumeration * uloc_openKeywords(const char * localeID, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_openKeywords)(localeID, status);
}
int32_t uloc_setKeywordValue(const char * keywordName, const char * keywordValue, char * buffer, int32_t bufferCapacity, UErrorCode * status) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_setKeywordValue)(keywordName, keywordValue, buffer, bufferCapacity, status);
}
int32_t uloc_toLanguageTag(const char * localeID, char * langtag, int32_t langtagCapacity, UBool strict, UErrorCode * err) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_toLanguageTag)(localeID, langtag, langtagCapacity, strict, err);
}
const char * uloc_toLegacyKey(const char * keyword) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_toLegacyKey)(keyword);
}
const char * uloc_toLegacyType(const char * keyword, const char * value) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_toLegacyType)(keyword, value);
}
const char * uloc_toUnicodeLocaleKey(const char * keyword) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_toUnicodeLocaleKey)(keyword);
}
const char * uloc_toUnicodeLocaleType(const char * keyword, const char * value) {
  return U_ICU_ENTRY_POINT_RENAME(uloc_toUnicodeLocaleType)(keyword, value);
}
}
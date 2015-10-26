/* GENERATED SOURCE. DO NOT MODIFY. */
/*
 *******************************************************************************
 * Copyright (C) 1996-2010, International Business Machines Corporation and    *
 * others. All Rights Reserved.                                                *
 *******************************************************************************
 */

package android.icu.util;

/**
 * <b>Note:</b> The Holiday framework is a technology preview.
 * Despite its age, is still draft API, and clients should treat it as such.
 * 
 * {@literal @}draft ICU 2.8 (retainAll)
 * {@literal @}provisional This API might change or be removed in a future release.
 * @hide All android.icu classes are currently hidden
 */
public class HebrewHoliday extends Holiday
{
    private static final HebrewCalendar gCalendar = new HebrewCalendar();

    /**
     * Construct a holiday defined in reference to the Hebrew calendar.
     *
     * @param name The name of the holiday
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public HebrewHoliday(int month, int date, String name)
    {
        this(month, date, 1, name);
    }

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public HebrewHoliday(int month, int date, int length, String name)
    {
        super(name, new SimpleDateRule(month, date, gCalendar));
    }

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday ROSH_HASHANAH   = new HebrewHoliday(HebrewCalendar.TISHRI,  1,  2,  "Rosh Hashanah");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday GEDALIAH        = new HebrewHoliday(HebrewCalendar.TISHRI,  3,      "Fast of Gedaliah");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday YOM_KIPPUR      = new HebrewHoliday(HebrewCalendar.TISHRI, 10,      "Yom Kippur");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday SUKKOT          = new HebrewHoliday(HebrewCalendar.TISHRI, 15,  6,  "Sukkot");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday HOSHANAH_RABBAH = new HebrewHoliday(HebrewCalendar.TISHRI, 21,      "Hoshanah Rabbah");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday SHEMINI_ATZERET = new HebrewHoliday(HebrewCalendar.TISHRI, 22,      "Shemini Atzeret");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday SIMCHAT_TORAH   = new HebrewHoliday(HebrewCalendar.TISHRI, 23,      "Simchat Torah");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday HANUKKAH        = new HebrewHoliday(HebrewCalendar.KISLEV, 25,      "Hanukkah");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday TEVET_10        = new HebrewHoliday(HebrewCalendar.TEVET,  10,      "Fast of Tevet 10");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday TU_BSHEVAT      = new HebrewHoliday(HebrewCalendar.SHEVAT, 15,      "Tu B'Shevat");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday ESTHER          = new HebrewHoliday(HebrewCalendar.ADAR,   13,      "Fast of Esther");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday PURIM           = new HebrewHoliday(HebrewCalendar.ADAR,   14,      "Purim");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday SHUSHAN_PURIM   = new HebrewHoliday(HebrewCalendar.ADAR,   15,      "Shushan Purim");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday PASSOVER        = new HebrewHoliday(HebrewCalendar.NISAN,  15,  8,  "Passover");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday YOM_HASHOAH     = new HebrewHoliday(HebrewCalendar.NISAN,  27,      "Yom Hashoah");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday YOM_HAZIKARON   = new HebrewHoliday(HebrewCalendar.IYAR,    4,      "Yom Hazikaron");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday YOM_HAATZMAUT   = new HebrewHoliday(HebrewCalendar.IYAR,    5,      "Yom Ha'Atzmaut");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday PESACH_SHEINI   = new HebrewHoliday(HebrewCalendar.IYAR,   14,      "Pesach Sheini");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday LAG_BOMER       = new HebrewHoliday(HebrewCalendar.IYAR,   18,      "Lab B'Omer");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday YOM_YERUSHALAYIM = new HebrewHoliday(HebrewCalendar.IYAR,   28,      "Yom Yerushalayim");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday SHAVUOT         = new HebrewHoliday(HebrewCalendar.SIVAN,   6,  2,  "Shavuot");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday TAMMUZ_17       = new HebrewHoliday(HebrewCalendar.TAMUZ,  17,      "Fast of Tammuz 17");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday TISHA_BAV       = new HebrewHoliday(HebrewCalendar.AV,      9,      "Fast of Tisha B'Av");

    /**
     * {@literal @}draft ICU 2.8
     * {@literal @}provisional This API might change or be removed in a future release.
     */
    public static HebrewHoliday SELIHOT         = new HebrewHoliday(HebrewCalendar.ELUL,   21,      "Selihot");
}

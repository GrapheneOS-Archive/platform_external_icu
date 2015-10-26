/* GENERATED SOURCE. DO NOT MODIFY. */
/*
 *******************************************************************************
 * Copyright (C) 2011, International Business Machines Corporation and         *
 * others. All Rights Reserved.                                                *
 *******************************************************************************
 */
package android.icu.impl;

import android.icu.text.TimeZoneNames;
import android.icu.text.TimeZoneNames.Factory;
import android.icu.util.ULocale;

/**
 * The implementation class of <code>TimeZoneNames.Factory</code>
 * @hide All android.icu classes are currently hidden
 */
public class TimeZoneNamesFactoryImpl extends Factory {

    /* (non-Javadoc)
     * @see com.ibm.icu.text.TimeZoneNames.Factory#getTimeZoneNames(com.ibm.icu.util.ULocale)
     */
    @Override
    public TimeZoneNames getTimeZoneNames(ULocale locale) {
        return new TimeZoneNamesImpl(locale);
    }

}

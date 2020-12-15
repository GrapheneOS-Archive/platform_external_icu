/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.icu.util;

import android.icu.util.TimeZone;

import libcore.api.IntraCoreApi;

import java.time.zone.ZoneRules;
import java.time.zone.ZoneRulesException;


/**
 * Provide extra functionalities on top of {@link TimeZone} public APIs.
 *
 * @hide
 */
@IntraCoreApi
public class ExtendedTimeZone {

    private final TimeZone timezone;

    private ExtendedTimeZone(String id) {
        timezone = TimeZone.getTimeZone(id);
    }

    // The API which calls an implementation in android.icu does not use nullability annotation
    // because the upstream can't guarantee the stability. See http://b/140196694.
    /**
     * Returns an instance from the time zone ID. Note that the returned instance could be shared.
     *
     * @see TimeZone#getTimeZone(String) for the more information.
     * @hide
     */
    @IntraCoreApi
    public static ExtendedTimeZone getInstance(String id) {
        return new ExtendedTimeZone(id);
    }

    /**
     * Clears the default time zone in ICU4J. When next {@link TimeZone#getDefault()} is called,
     * ICU4J will re-initialize the default time zone from the value obtained from the libcore's
     * {@link java.util.TimeZone#getDefault()}.
     *
     * This API is useful for libcore's {@link java.util.TimeZone#setDefault(java.util.TimeZone)} to
     * break the cycle of synchronizing the default time zone between libcore and ICU4J.
     *
     * @hide
     */
    @IntraCoreApi
    public static void clearDefaultTimeZone() {
        TimeZone.setICUDefault(null);
    }

    /**
     * Returns the underlying {@link TimeZone} instance.
     *
     * @hide
     */
    @IntraCoreApi
    public TimeZone getTimeZone() {
        return timezone;
    }

}

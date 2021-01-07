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

package com.android.icu.text;

import android.icu.text.TimeZoneNames;
import android.icu.util.ULocale;

import libcore.api.IntraCoreApi;
import libcore.util.NonNull;
import libcore.util.Nullable;

/**
 * Provide extra functionalities on top of {@link TimeZoneNames} public APIs.
 *
 * @hide
 */
@IntraCoreApi
public class ExtendedTimeZoneNames {

    private final TimeZoneNames timeZoneNames;

    /**
     * A class representing the return result of {@link #matchName(CharSequence, int, String)}
     *
     * @hide
     */
    @IntraCoreApi
    public static class MatchedTimeZone {

        private final int matchLength;
        private final @NonNull String tzId;
        private final boolean isDst;

        private MatchedTimeZone(int matchLength, @NonNull String tzId, boolean isDst) {
            this.matchLength = matchLength;
            this.tzId = tzId;
            this.isDst = isDst;
        }

        /**
         * Returns the number of chars in the matched name.
         *
         * @hide
         */
        @IntraCoreApi
        public int getMatchLength() {
            return matchLength;
        }

        /**
         * Returns the time zone id associated with the matched name.
         *
         * @hide
         */
        @IntraCoreApi
        public @NonNull String getTzId() {
            return tzId;
        }

        /**
         * Returns true if the matched name is a display name for daylight saving time.
         *
         * @hide
         */
        @IntraCoreApi
        public boolean isDst() {
            return isDst;
        }
    }

    private ExtendedTimeZoneNames(ULocale locale) {
        this.timeZoneNames = TimeZoneNames.getInstance(locale);
    }

    /**
     * Returns an instance of {@link ExtendedTimeZoneNames}.
     *
     * @hide
     */
    @IntraCoreApi
    public static @NonNull ExtendedTimeZoneNames getInstance(@NonNull ULocale locale) {
        return new ExtendedTimeZoneNames(locale);
    }

    /**
     * Returns the underlying {@link TimeZoneNames} instance.
     *
     * @hide
     */
    @IntraCoreApi
    public @NonNull TimeZoneNames getTimeZoneNames() {
        return timeZoneNames;
    }

}

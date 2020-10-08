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

package com.android.i18n.system;

import android.icu.impl.CacheValue;
import android.icu.text.DateFormatSymbols;
import android.icu.text.DecimalFormatSymbols;
import android.icu.util.TimeZone;
import android.icu.util.ULocale;

/**
 * Provides hooks for {@link dalvik.system.ZygoteHooks} to call into this class to initialize
 * in the Zygote.
 *
 * @hide
 */
@libcore.api.IntraCoreApi
public final class ZygoteHooks {

    /** All methods are static, no need to instantiate. */
    private ZygoteHooks() {
    }

    /**
     * Called when the Zygote begins preloading classes and data.
     * It caches some ICU objects before the Zygote forks, and tune some behaviors during preload,
     * e.g. enabling strong cache for preloaded classes.
     */
    @libcore.api.IntraCoreApi
    public static void onBeginPreload() {
        // Pin ICU data in memory from this point that would normally be held by soft references.
        // Without this, any references created immediately below or during class preloading
        // would be collected when the Zygote GC runs in gcAndFinalize().
        CacheValue.setStrength(CacheValue.Strength.STRONG);

        // Explicitly exercise code to cache data apps/framework are likely to need.
        ULocale[] localesToPin = { ULocale.ROOT, ULocale.US, ULocale.getDefault() };
        for (ULocale uLocale : localesToPin) {
            new DecimalFormatSymbols(uLocale);
            new DateFormatSymbols(uLocale);
        }

        // Framework's LocalLog is used during app start-up. It indirectly uses the current ICU time
        // zone. Pre-loading the current time zone in ICU improves app startup time. b/150605074
        TimeZone.getDefault();
    }

    /**
     * Called when the Zygote has completed preloading classes and data.
     */
    @libcore.api.IntraCoreApi
    public static void onEndPreload() {
        // All cache references created by ICU from this point will be soft.
        CacheValue.setStrength(CacheValue.Strength.SOFT);
    }
}

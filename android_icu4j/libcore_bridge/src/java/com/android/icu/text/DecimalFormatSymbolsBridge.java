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

import android.icu.text.DecimalFormatSymbols;
import android.icu.text.NumberingSystem;
import android.icu.util.ULocale;

import libcore.api.IntraCoreApi;

/**
 * Used by {@link java.text.DecimalFormatSymbols}.
 * @hide
 */
@IntraCoreApi
public class DecimalFormatSymbolsBridge {

    /**
     * Public API {@link DecimalFormatSymbols#getPatternSeparator()} does not localize pattern
     * separator. This API is consumed by libcore's {@link java.text.DecimalFormatSymbols}.
     */
    @IntraCoreApi
    public static String getLocalizedPatternSeparator(ULocale locale, NumberingSystem ns) {
        return DecimalFormatSymbols.getLocalizedPatternSeparator(locale, ns);
    }
}

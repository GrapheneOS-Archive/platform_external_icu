/*
 * Copyright (C) 2016 The Android Open Source Project
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
package android.icu.cts.coverage.text;

import android.icu.text.TimeZoneNames;
import android.icu.util.ULocale;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * Extra tests to improve CTS Test Coverage.
 */
@RunWith(JUnit4.class)
public class TimeZoneNamesTest {

    @Test
    public void testGetInstance_Locale() {
        TimeZoneNames uLocaleInstance = TimeZoneNames.getInstance(ULocale.CANADA);
        TimeZoneNames localeInstance = TimeZoneNames.getInstance(Locale.CANADA);

        Set<String> uLocaleAvailableIds = uLocaleInstance.getAvailableMetaZoneIDs();
        Set<String> localeAvailableIds = localeInstance.getAvailableMetaZoneIDs();
        assertEquals("Available ids", uLocaleAvailableIds, localeAvailableIds);

        for (String availableId : uLocaleAvailableIds) {
            long date = 1458385200000L;
            TimeZoneNames.NameType nameType = TimeZoneNames.NameType.SHORT_GENERIC;
            String uLocaleName = uLocaleInstance.getDisplayName(availableId, nameType, date);
            String localeName = localeInstance.getDisplayName(availableId, nameType, date);
            assertEquals("Id: " + availableId, uLocaleName, localeName);
        }
    }
}

/*
 * Copyright (C) 2021 The Android Open Source Project
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

package android.icu.extratest.platform;

import static org.junit.Assert.assertTrue;

import android.icu.platform.AndroidDataFiles;
import android.icu.testsharding.MainTestShard;
import android.icu.util.VersionInfo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@MainTestShard
@RunWith(JUnit4.class)
public class AndroidDataFilesTest {

    /**
     * If this test fails and needs to be fixed, please also fix
     * {@link com.android.compatibility.common.deviceinfo.LocaleDeviceInfo} which has the same
     * assumption on the data file paths.
     */
    @Test
    public void testGenerateIcuDataPath() {
        String path = AndroidDataFiles.generateIcuDataPath();

        String[] dataDirs = path.split(":");
        // List all readable".dat" files in the directories.
        Set<String> datFiles = Arrays.stream(dataDirs)
                .filter((dir) -> dir != null && !dir.isEmpty())
                .map((dir) -> new File(dir))
                .filter((f) -> f.canRead() && f.isDirectory())
                .map((f) -> f.listFiles())
                .filter((files) -> files != null)
                .flatMap(files -> Stream.of(files))
                .filter((f) -> f != null && f.canRead() && f.getName().endsWith(".dat"))
                .map(f -> f.getPath())
                .collect(Collectors.toSet());

        int icuVersion = VersionInfo.ICU_VERSION.getMajor();
        assertContains(datFiles, "/apex/com.android.tzdata/etc/icu/icu_tzdata.dat");
        assertContains(datFiles, "/apex/com.android.i18n/etc/icu/icudt" + icuVersion + "l.dat");
    }

    private static void assertContains(Set<String> set, String member) {
        assertTrue("Expect to contain \"" + member + "\" but not", set.contains(member));
    }
}

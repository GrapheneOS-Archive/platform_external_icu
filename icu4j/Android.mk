#
# Copyright (C) 2014 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

LOCAL_PATH := $(call my-dir)

# User-supplied locale service providers (using the java.text.spi or
# java.util.spi mechanisms) are not supported in Android:
#
# http://developer.android.com/reference/java/util/Locale.html

icu4j_src_files := \
    $(filter-out main/classes/localespi/%, \
    $(call all-java-files-under,main/classes))

icu4j_test_src_files := \
    $(filter-out main/tests/localespi/%, \
    $(call all-java-files-under,main/tests))

# Not all src dirs contain resources, some instead contain other random files
# that should not be included as resources. The ones that should be included
# can be identifed by the fact that they contain particular subdir trees.

define all-subdir-with-subdir
$(patsubst $(LOCAL_PATH)/%/$(2),%,$(wildcard $(LOCAL_PATH)/$(1)/$(2)))
endef

icu4j_resource_dirs := \
    $(filter-out main/classes/localespi/%, \
    $(call all-subdir-with-subdir,main/classes/*/src,com/ibm/icu))

icu4j_test_resource_dirs := \
    $(filter-out main/tests/localespi/%, \
    $(call all-subdir-with-subdir,main/tests/*/src,com/ibm/icu/dev/data))

# ICU4J depends on being able to use deprecated APIs and doing unchecked
# conversions, so these otherwise noisy lint warnings must be turned off.

icu4j_javac_flags := -encoding UTF-8 -Xlint:-deprecation,-unchecked
icu4j_test_javac_flags := $(icu4j_javac_flags)

# TODO: Replace use of ICU4J data JAR files with accessing ICU4C data (ie. by
# setting the com.ibm.icu.impl.ICUBinary.dataPath property), after everything
# else works and ICU4C in Android has been updated to ICU 54.

# For each data *.jar file, define a corresponding icu4j-* target.
icu4j_data_jars := \
    $(shell find $(LOCAL_PATH)/main/shared/data -name "*.jar" \
    | sed "s,^$(LOCAL_PATH)/\(.*/\(.*\)\.jar\)$$,icu4j-\2:\1,")

include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := $(icu4j_data_jars)
include $(BUILD_MULTI_PREBUILT)

include $(CLEAR_VARS)
LOCAL_IS_HOST_MODULE := true
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := $(icu4j_data_jars)
include $(BUILD_MULTI_PREBUILT)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-icudata icu4j-icutzdata
LOCAL_JAVACFLAGS := $(icu4j_javac_flags)
LOCAL_MODULE := icu4j
include $(BUILD_STATIC_JAVA_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-icudata icu4j-icutzdata
LOCAL_JAVACFLAGS := $(icu4j_javac_flags)
LOCAL_MODULE := icu4j-hostdex
include $(BUILD_HOST_DALVIK_JAVA_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-icudata icu4j-icutzdata
LOCAL_JAVACFLAGS := $(icu4j_javac_flags)
LOCAL_MODULE := icu4j-host
include $(BUILD_HOST_JAVA_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_test_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_test_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-testdata
LOCAL_JAVA_LIBRARIES := icu4j
LOCAL_JAVACFLAGS := $(icu4j_test_javac_flags)
LOCAL_MODULE := icu4j-tests
include $(BUILD_STATIC_JAVA_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_test_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_test_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-testdata
LOCAL_JAVA_LIBRARIES := icu4j-host
LOCAL_JAVACFLAGS := $(icu4j_test_javac_flags)
LOCAL_MODULE := icu4j-tests-host
include $(BUILD_HOST_JAVA_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_test_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_test_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-testdata
LOCAL_JAVA_LIBRARIES := icu4j-hostdex
LOCAL_JAVACFLAGS := $(icu4j_test_javac_flags)
LOCAL_MODULE := icu4j-tests-hostdex
include $(BUILD_HOST_DALVIK_JAVA_LIBRARY)

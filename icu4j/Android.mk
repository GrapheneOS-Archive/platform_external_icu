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
    $(call all-subdir-with-subdir,main/tests/*/src,com/ibm/icu/dev))

# For each data *.jar file, define a corresponding icu4j-* target.
icu4j_data_jars := \
    $(shell find $(LOCAL_PATH)/main/shared/data -name "*.jar" \
    | sed "s,^$(LOCAL_PATH)/\(.*/\(.*\)\.jar\)$$,icu4j-\2:\1,")

# Prebuilt data resource jars for device. e.g. icu4j-icudata.jar, icu4j-icutzdata.jar,
# icu4j-testdata.jar.
include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := $(icu4j_data_jars)
include $(BUILD_MULTI_PREBUILT)

# Host resource jars. e.g. icu4j-icudata-host.jar, icu4j-icutzdata-host.jar,
# icu4j-testdata-host.jar.
include $(CLEAR_VARS)
LOCAL_IS_HOST_MODULE := true
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := $(subst :,-host:,$(icu4j_data_jars))
include $(BUILD_MULTI_PREBUILT)

#
# ICU4J / device runtime.
#

# See libcore/JavaLibrary.mk.
# libcore builds icu4j source directly in core-libart and repackages it
# from com.ibm.icu -> android.icu. In that case data files are read from
# the ICU4C data files and configured via the java.util.System class.

# ICU4J with ICU4J data included in the .jar. For running ICU4J tests on device.
include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-icudata icu4j-icutzdata
LOCAL_DONT_DELETE_JAR_DIRS := true
LOCAL_MODULE := icu4j-plus-data
include $(BUILD_STATIC_JAVA_LIBRARY)

# ICU4J tests + test data. For running ICU4J tests on device (with icu4j-plus-data).
include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_test_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_test_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-testdata
LOCAL_DONT_DELETE_JAR_DIRS := true
LOCAL_JAVA_LIBRARIES := icu4j-plus-data
LOCAL_MODULE := icu4j-plus-data-tests
include $(BUILD_STATIC_JAVA_LIBRARY)

$(LOCAL_INTERMEDIATE_TARGETS): PRIVATE_EXTRA_JAR_ARGS += \
    -C "$(LOCAL_PATH)/main/tests/core/src" \
    "com/ibm/icu/dev/test/serializable/data"

#
# ICU4J / host Android runtime.
#

ifeq ($(HOST_OS),linux)

# Host runtime equivalent of icu4j-plus-data. For running ICU4J tests on host.
include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-icudata-host icu4j-icutzdata-host
LOCAL_DONT_DELETE_JAR_DIRS := true
LOCAL_MODULE := icu4j-plus-data-hostdex
include $(BUILD_HOST_DALVIK_JAVA_LIBRARY)

# Host runtime equivalent of icu4j-tests. For running ICU4J tests on host with
# icu4j-plus-data-hostdex.
include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_test_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_test_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-testdata-host
LOCAL_DONT_DELETE_JAR_DIRS := true
LOCAL_JAVA_LIBRARIES := icu4j-plus-data-hostdex
LOCAL_MODULE := icu4j-plus-data-tests-hostdex
include $(BUILD_HOST_DALVIK_JAVA_LIBRARY)

$(LOCAL_INTERMEDIATE_TARGETS): PRIVATE_EXTRA_JAR_ARGS += \
    -C "$(LOCAL_PATH)/main/tests/core/src" \
    "com/ibm/icu/dev/test/serializable/data"

endif  # HOST_OS == linux

#
# ICU4J / host JRE.
#

# ICU4J with ICU4J data included in the .jar. Built against the host JRE. For transconsole.
include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(icu4j_src_files)
LOCAL_JAVA_RESOURCE_DIRS := $(icu4j_resource_dirs)
LOCAL_STATIC_JAVA_LIBRARIES := icu4j-icudata-host icu4j-icutzdata-host
LOCAL_DONT_DELETE_JAR_DIRS := true
LOCAL_MODULE := icu4j-host
include $(BUILD_HOST_JAVA_LIBRARY)

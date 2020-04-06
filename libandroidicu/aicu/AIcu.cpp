/*
 * Copyright (C) 2015 The Android Open Source Project
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

#include <aicu/AIcu.h>

#include <stdlib.h>

#ifdef __ANDROID__
#include <androidicuinit/android_icu_reg.h>
#endif

void AIcu_initializeIcuOrDie() {
#ifdef __ANDROID__
    android_icu_register();
#else
    // This function is only supported when there is access to
    // androidicuinit.
    abort();
#endif
}

#ifdef __ANDROID__
void AIcu_register() {
    android_icu_register();
}

void AIcu_deregister() {
    android_icu_deregister();
}
#endif

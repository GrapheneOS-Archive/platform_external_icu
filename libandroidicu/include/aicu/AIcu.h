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

#ifndef AICU_H
#define AICU_H

#include <sys/cdefs.h>

// Disable declaration of register functions in Windows because
// sys/mman.h is not available in MinGW.
#if !defined (WINVER) && !defined (_WIN32_WINNT)
  #define ENABLE_ICU_REGISTER 1
#endif

__BEGIN_DECLS

#ifdef ENABLE_ICU_REGISTER

#ifdef __ANDROID__
// Initializes ICU using the best available data files or dies trying.
// This must be called when the process is single threaded.
void AIcu_initializeIcuOrDie();
#endif

// The AIcu_register and AIcu_deregister should only be called by libcore.
// For other clients, please call AIcu_initializeIcuOrDie instead.
void AIcu_register();

void AIcu_deregister();

#endif // ENABLE_ICU_REGISTER

__END_DECLS

#endif  // AICU_H


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
package com.google.currysrc.api.transform.ast;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import java.util.List;

/**
 * Matches the parameters associated with a method.
 */
public final class ParameterMatcher {

  private final List<String> parameterTypes;

  public ParameterMatcher(List<String> parameterTypes) {
    this.parameterTypes = parameterTypes;
  }

  public boolean matches(MethodDeclaration methodDeclaration) {
    List<SingleVariableDeclaration> actualParameters =
        (List<SingleVariableDeclaration>) methodDeclaration.parameters();
    if (actualParameters.size() != parameterTypes.size()) {
      return false;
    }

    for (int i = 0; i < parameterTypes.size(); i++) {
      String actualTypeName = actualParameters.get(i).getType().toString();
      String expectedTypeName = parameterTypes.get(i);
      if (!actualTypeName.equals(expectedTypeName)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return "ParameterMatcher{" +
        "parameterTypes=" + parameterTypes +
        '}';
  }
}

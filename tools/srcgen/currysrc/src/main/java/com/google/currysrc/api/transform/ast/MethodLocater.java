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

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.List;

/**
 * Locates the {@link org.eclipse.jdt.core.dom.BodyDeclaration} associated with a method
 * declaration.
 */
public final class MethodLocater implements BodyDeclarationLocater {

  private final TypeLocater typeLocater;

  private final String methodName;

  private final ParameterMatcher parameterMatcher;

  public MethodLocater(TypeLocater typeLocater, String methodName, List<String> parameterTypes) {
    this.typeLocater = typeLocater;
    this.methodName = methodName;
    this.parameterMatcher = new ParameterMatcher(parameterTypes);
  }

  @Override
  public boolean matches(BodyDeclaration node) {
    if (node instanceof MethodDeclaration) {
      MethodDeclaration methodDeclaration = (MethodDeclaration) node;
      if (methodDeclaration.getName().getFullyQualifiedName().equals(methodName)) {
        return parameterMatcher.matches(methodDeclaration);
      }
    }
    return false;
  }

  @Override
  public MethodDeclaration find(CompilationUnit cu) {
    AbstractTypeDeclaration typeDeclaration = typeLocater.find(cu);
    if (typeDeclaration == null) {
      return null;
    }
    for (BodyDeclaration bodyDeclaration
        : (List<BodyDeclaration>) typeDeclaration.bodyDeclarations()) {
      if (bodyDeclaration instanceof MethodDeclaration) {
        MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
        if (methodDeclaration.getName().getFullyQualifiedName().equals(methodName)) {
          if (parameterMatcher.matches(methodDeclaration)) {
            return methodDeclaration;
          }
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "MethodLocater{" +
        "typeLocater=" + typeLocater +
        ", methodName='" + methodName + '\'' +
        ", parameterMatcher=" + parameterMatcher +
        '}';
  }
}

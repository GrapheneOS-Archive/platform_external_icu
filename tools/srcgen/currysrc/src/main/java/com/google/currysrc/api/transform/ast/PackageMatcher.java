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

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;

/**
 * Matches the package name associated with ASTNodes.
 */
public class PackageMatcher {

  protected final String packageName;

  public PackageMatcher(String packageName) {
    this.packageName = packageName;
  }

  public boolean matches(CompilationUnit cu) {
    PackageDeclaration aPackage = cu.getPackage();
    return matches(aPackage);
  }

  public boolean matches(PackageDeclaration packageDeclaration) {
    String cuPackageName = packageDeclaration.getName().getFullyQualifiedName();
    return cuPackageName.equals(packageName);
  }

  @Override
  public String toString() {
    return "PackageMatcher{" +
        "packageName='" + packageName + '\'' +
        '}';
  }
}

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

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Iterator;
import java.util.List;

/**
 * Locates the {@link org.eclipse.jdt.core.dom.BodyDeclaration} associated with an class
 * declaration.
 */
public final class TypeLocater implements BodyDeclarationLocater {

  private final PackageMatcher packageMatcher;

  private final List<String> classNameElements;

  public TypeLocater(String packageName, String className) {
    this.packageMatcher = new PackageMatcher(packageName);
    this.classNameElements = classNameElements(className);
    if (classNameElements.isEmpty()) {
      throw new IllegalArgumentException("Empty className");
    }
  }

  @Override
  public boolean matches(BodyDeclaration node) {
    if (!(node instanceof AbstractTypeDeclaration)) {
      return false;
    }
    if (!packageMatcher.matches((CompilationUnit) node.getRoot())) {
      return false;
    }

    Iterable<String> reverseClassNames = Lists.reverse(classNameElements);
    Iterator<String> reverseClassNamesIterator = reverseClassNames.iterator();
    return matchNested(reverseClassNamesIterator, (AbstractTypeDeclaration) node);
  }

  private boolean matchNested(Iterator<String> reverseClassNamesIterator,
      AbstractTypeDeclaration node) {
    String subClassName = reverseClassNamesIterator.next();
    if (!node.getName().getFullyQualifiedName().equals(subClassName)) {
      return false;
    }
    if (!reverseClassNamesIterator.hasNext()) {
      return true;
    }

    ASTNode parentNode = node.getParent();
    // This won't work with method-declared types. But they're not documented so...?
    if (!(parentNode instanceof AbstractTypeDeclaration)) {
      return false;
    }
    return matchNested(reverseClassNamesIterator, (AbstractTypeDeclaration) parentNode);
  }

  @Override
  public AbstractTypeDeclaration find(CompilationUnit cu) {
    if (!packageMatcher.matches(cu)) {
      return null;
    }

    Iterator<String> classNameIterator = classNameElements.iterator();
    String topLevelClassName = classNameIterator.next();
    for (AbstractTypeDeclaration abstractTypeDeclaration : (List<AbstractTypeDeclaration>) cu
        .types()) {
      if (abstractTypeDeclaration.getName().getFullyQualifiedName().equals(topLevelClassName)) {
        // Top-level interface / class / enum match.
        return findNested(classNameIterator, abstractTypeDeclaration);
      }
    }
    return null;
  }

  private AbstractTypeDeclaration findNested(Iterator<String> classNameIterator,
      AbstractTypeDeclaration typeDeclaration) {
    if (!classNameIterator.hasNext()) {
      return typeDeclaration;
    }

    String subClassName = classNameIterator.next();
    for (BodyDeclaration bodyDeclaration : (List<BodyDeclaration>) typeDeclaration
        .bodyDeclarations()) {
      if (bodyDeclaration instanceof AbstractTypeDeclaration) {
        AbstractTypeDeclaration subTypeDeclaration = (AbstractTypeDeclaration) bodyDeclaration;
        if (subTypeDeclaration.getName().getFullyQualifiedName().equals(subClassName)) {
          return findNested(classNameIterator, subTypeDeclaration);
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "TypeLocater{" +
        "packageMatcher=" + packageMatcher +
        ", classNameElements=" + classNameElements +
        '}';
  }

  private static List<String> classNameElements(String className) {
    return Splitter.on("$").splitToList(className);
  }
}

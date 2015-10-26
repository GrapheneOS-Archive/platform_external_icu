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
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.List;

/**
 * Locates the {@link org.eclipse.jdt.core.dom.BodyDeclaration} associated with an field
 * declaration.
 */
public final class FieldLocater implements BodyDeclarationLocater {

  private final TypeLocater typeLocater;

  private final String fieldName;

  public FieldLocater(String packageName, String typeName, String fieldName) {
    this(new TypeLocater(packageName, typeName), fieldName);
  }

  public FieldLocater(TypeLocater typeLocater, String fieldName) {
    this.typeLocater = typeLocater;
    this.fieldName = fieldName;
  }

  @Override
  public boolean matches(BodyDeclaration node) {
    if (node instanceof FieldDeclaration) {
      FieldDeclaration fieldDeclaration = (FieldDeclaration) node;
      for (VariableDeclarationFragment variableDeclarationFragment
          : (List<VariableDeclarationFragment>) fieldDeclaration.fragments()) {
        String nodeFieldName = variableDeclarationFragment.getName().getFullyQualifiedName();
        if (nodeFieldName.equals(fieldName)) {
          BodyDeclaration parentNode = (BodyDeclaration) node.getParent();
          return typeLocater.matches(parentNode);
        }
      }
    }
    return false;
  }

  @Override
  public FieldDeclaration find(CompilationUnit cu) {
    AbstractTypeDeclaration typeDeclaration = typeLocater.find(cu);
    if (typeDeclaration == null) {
      return null;
    }
    for (BodyDeclaration bodyDeclaration
        : (List<BodyDeclaration>) typeDeclaration.bodyDeclarations()) {
      if (bodyDeclaration instanceof FieldDeclaration) {
        FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
        for (VariableDeclarationFragment variableDeclarationFragment
            : (List<VariableDeclarationFragment>) fieldDeclaration.fragments()) {
          String nodeFieldName = variableDeclarationFragment.getName().getFullyQualifiedName();
          if (nodeFieldName.equals(fieldName)) {
            return fieldDeclaration;
          }
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "FieldLocator{" +
        "typeLocater=" + typeLocater +
        ", fieldName='" + fieldName + '\'' +
        '}';
  }
}

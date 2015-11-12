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

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Interface for objects that match and find {@link org.eclipse.jdt.core.dom.BodyDeclaration}
 * instances within an AST.
 */
public interface BodyDeclarationLocater {

  boolean matches(BodyDeclaration node);

  BodyDeclaration find(CompilationUnit cu);

  /**
   * The prefix used to identify the {@link BodyDeclarationLocater} in string form. e.g. "method",
   * "field".
   */
  String getStringFormType();

  /**
   * Generates a string form of the locater that identifies the declaration being targeted. e.g.
   * {@code com.foo.Bar#baz}, {@code com.foo.Bar$Baz}, {@code com.foo.Bar#baz()}. Note, it differs
   * from Javadoc locaters used in {@literal @}see and {@literal @}link by using '$' to delineate
   * classes from package separators. Used by {@link BodyDeclarationLocaters}.
   */
  String getStringFormTarget();

  /**
   * Returns the {@link TypeLocater} for the type declaration associated with this
   * locater. e.g. for a {@link MethodLocater} it returns a type locater that can find the
   * declaring type of the method. When called on a {@link TypeLocater} it returns {@code this}.
   */
  TypeLocater getTypeLocater();
}

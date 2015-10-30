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
package com.google.currysrc.transformers;

import com.google.currysrc.api.transform.AstTransformer;
import com.google.currysrc.api.transform.ast.BodyDeclarationLocater;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * A base-class for {@link AstTransformer}s that make modifications to a single
 * {@link BodyDeclaration} or surrounding nodes.
 */
public abstract class BaseModifyBodyDeclaration implements AstTransformer {
  private final BodyDeclarationLocater locater;

  protected BaseModifyBodyDeclaration(BodyDeclarationLocater locater) {
    this.locater = locater;
  }

  @Override public void transform(CompilationUnit cu, ASTRewrite rewrite) {
    BodyDeclaration node = locater.find(cu);
    if (node == null) {
      return;
    }

    modifyNode(node, rewrite);
  }

  protected abstract void modifyNode(BodyDeclaration node, ASTRewrite rewrite);
}

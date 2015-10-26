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

import com.google.common.collect.Lists;
import com.google.currysrc.api.transform.DocumentTransformer;
import com.google.currysrc.api.transform.JavadocUtils;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;

import java.util.List;

/**
 * Inserts the tag text to the Javadoc for any type declaration that matches
 * {@link #mustTag(AbstractTypeDeclaration)}.
 */
public abstract class JavadocTagClasses implements DocumentTransformer {

  private final String tagText;

  protected JavadocTagClasses(String tagText) {
    this.tagText = tagText;
  }

  @Override
  public void transform(CompilationUnit cu, final Document document) {
    final List<AbstractTypeDeclaration> toHide = Lists.newArrayList();
    cu.accept(new ASTVisitor() {
      @Override
      public boolean visit(TypeDeclaration node) {
        return visitAbstract(node);
      }

      @Override
      public boolean visit(EnumDeclaration node) {
        return visitAbstract(node);
      }

      private boolean visitAbstract(AbstractTypeDeclaration node) {
        if (mustTag(node)) {
          toHide.add(node);
        }
        return false;
      }
    });
    // To avoid screwing up the offsets, attack in reverse order.
    // TODO Is there a better way?
    for (AbstractTypeDeclaration node : Lists.reverse(toHide)) {
      JavadocUtils.insertCommentText(document, node, tagText);
    }
  }

  protected abstract boolean mustTag(AbstractTypeDeclaration node);
}

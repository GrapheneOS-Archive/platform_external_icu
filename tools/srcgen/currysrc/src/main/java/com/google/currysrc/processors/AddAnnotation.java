/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.google.currysrc.processors;

import static com.google.currysrc.api.process.ast.BodyDeclarationLocators.matchesAny;

import com.google.currysrc.api.process.Context;
import com.google.currysrc.api.process.Processor;
import com.google.currysrc.api.process.ast.BodyDeclarationLocator;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

/**
 * Add a fully qualified marker annotation to a white list of classes and class members.
 */
public class AddAnnotation implements Processor {

  private final String annotationString;
  private final List<BodyDeclarationLocator> annotationTargetLocators;

  public AddAnnotation(String annotationString,
      List<BodyDeclarationLocator> annotationTargetLocators) {
    this.annotationString = annotationString;
    this.annotationTargetLocators = annotationTargetLocators;
  }

  @Override
  public void process(Context context, CompilationUnit cu) {
    final ASTRewrite rewrite = context.rewrite();
    ASTVisitor visitor = new ASTVisitor(false /* visitDocTags */) {
      @Override
      public boolean visit(AnnotationTypeDeclaration node) {
        return handleBodyDeclaration(rewrite, node);
      }

      @Override
      public boolean visit(AnnotationTypeMemberDeclaration node) {
        return handleBodyDeclaration(rewrite, node);
      }

      @Override
      public boolean visit(EnumConstantDeclaration node) {
        return handleBodyDeclaration(rewrite, node);
      }

      @Override
      public boolean visit(EnumDeclaration node) {
        return handleBodyDeclaration(rewrite, node);
      }

      @Override
      public boolean visit(FieldDeclaration node) {
        return handleBodyDeclaration(rewrite, node);
      }

      @Override
      public boolean visit(MethodDeclaration node) {
        return handleBodyDeclaration(rewrite, node);
      }

      @Override
      public boolean visit(TypeDeclaration node) {
        return handleBodyDeclaration(rewrite, node);
      }

    };
    cu.accept(visitor);
  }

  private boolean handleBodyDeclaration(ASTRewrite rewrite, BodyDeclaration node) {
    if (matchesAny(annotationTargetLocators, node)) {
      final TextEditGroup editGroup = null;
      AST ast = node.getAST();
      Annotation annotation = ast.newMarkerAnnotation();
      annotation.setTypeName(ast.newName(annotationString));
      ListRewrite listRewrite = rewrite.getListRewrite(node, node.getModifiersProperty());
      listRewrite.insertFirst(annotation, editGroup);
    }
    return true;
  }
}

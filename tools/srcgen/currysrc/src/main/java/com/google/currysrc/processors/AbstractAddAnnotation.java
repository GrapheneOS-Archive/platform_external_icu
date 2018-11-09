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

import com.google.currysrc.api.process.Context;
import com.google.currysrc.api.process.Processor;
import com.google.currysrc.processors.AnnotationInfo.Placeholder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

/**
 * Base class of {@link Processor} implementations that add annotations.
 */
public abstract class AbstractAddAnnotation implements Processor {

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

  protected abstract boolean handleBodyDeclaration(ASTRewrite rewrite, BodyDeclaration node);

  /**
   * Add an annotation to a {@link BodyDeclaration} node.
   */
  protected static void insertAnnotationBefore(ASTRewrite rewrite, BodyDeclaration node,
      AnnotationInfo annotationInfo) {
    final TextEditGroup editGroup = null;
    AST ast = node.getAST();
    Map<String, Object> elements = annotationInfo.getProperties();
    Annotation annotation;
    if (elements.isEmpty()) {
      annotation = ast.newMarkerAnnotation();
    } else if (elements.size() == 1 && elements.containsKey("value")) {
      SingleMemberAnnotation singleMemberAnnotation = ast.newSingleMemberAnnotation();
      singleMemberAnnotation.setValue(createAnnotationValue(rewrite, elements.get("value")));
      annotation = singleMemberAnnotation;
    } else {
      NormalAnnotation normalAnnotation = ast.newNormalAnnotation();
      @SuppressWarnings("unchecked")
      List<MemberValuePair> values = normalAnnotation.values();
      for (Entry<String, Object> entry : elements.entrySet()) {
        MemberValuePair pair = ast.newMemberValuePair();
        pair.setName(ast.newSimpleName(entry.getKey()));
        pair.setValue(createAnnotationValue(rewrite, entry.getValue()));
        values.add(pair);
      }
      annotation = normalAnnotation;
    }

    annotation.setTypeName(ast.newName(annotationInfo.getQualifiedName()));
    ListRewrite listRewrite = rewrite.getListRewrite(node, node.getModifiersProperty());
    listRewrite.insertFirst(annotation, editGroup);
  }

  private static Expression createAnnotationValue(ASTRewrite rewrite, Object value) {
    if (value instanceof String) {
      StringLiteral stringLiteral = rewrite.getAST().newStringLiteral();
      stringLiteral.setLiteralValue((String) value);
      return stringLiteral;
    }
    if (value instanceof Integer) {
      NumberLiteral numberLiteral = rewrite.getAST().newNumberLiteral();
      numberLiteral.setToken(value.toString());
      return numberLiteral;
    }
    if (value instanceof Placeholder) {
      Placeholder placeholder = (Placeholder) value;
      // The cast is safe because createStringPlaceholder returns an instance of type NumberLiteral
      // which is an Expression.
      return (Expression)
          rewrite.createStringPlaceholder(placeholder.getText(), ASTNode.NUMBER_LITERAL);
    }
    throw new IllegalStateException("Unknown value '" + value + "' of class " +
        (value == null ? "NULL" : value.getClass()));
  }
}

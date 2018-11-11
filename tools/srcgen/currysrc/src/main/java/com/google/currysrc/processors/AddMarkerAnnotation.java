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

import com.google.currysrc.api.process.ast.BodyDeclarationLocator;
import com.google.currysrc.processors.AnnotationInfo.AnnotationClass;
import java.util.Collections;
import java.util.List;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * Add a fully qualified marker annotation to a white list of classes and class members.
 */
public class AddMarkerAnnotation extends AbstractAddAnnotation {

  private final AnnotationInfo markerAnnotation;
  private final List<BodyDeclarationLocator> annotationTargetLocators;

  public AddMarkerAnnotation(String annotationString,
      List<BodyDeclarationLocator> annotationTargetLocators) {
    AnnotationClass annotationClass = new AnnotationClass(annotationString);
    markerAnnotation = new AnnotationInfo(annotationClass, Collections.emptyMap());
    this.annotationTargetLocators = annotationTargetLocators;
  }

  @Override
  protected boolean handleBodyDeclaration(ASTRewrite rewrite, BodyDeclaration node) {
    if (matchesAny(annotationTargetLocators, node)) {
      insertAnnotationBefore(rewrite, node, markerAnnotation);
    }
    return true;
  }
}

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
package com.android.icu4j.srcgen;

import com.google.common.collect.Lists;
import com.google.currysrc.api.transform.DocumentTransformer;
import com.google.currysrc.api.transform.JavadocUtils;
import com.google.currysrc.api.transform.ast.BodyDeclarationLocater;
import com.google.currysrc.api.transform.ast.StartPositionComparator;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

import java.util.Collections;
import java.util.List;

/**
 * Adds {@literal @}hide to the javadoc for all the {@link BodyDeclaration}s in the specified
 * blacklist. Used to hide all the deprecated methods that existed when Android made ICU public,
 * but does not touch those that were deprecated afterwards.
 */
public class HideOriginalDeprecatedSet implements DocumentTransformer {
  private static final String HIDDEN_TAG_COMMENT = "@hide original deprecated method";
  private final List<BodyDeclarationLocater> blacklist;

  public HideOriginalDeprecatedSet(List<BodyDeclarationLocater> blacklist) {
    this.blacklist = blacklist;
  }

  @Override
  public void transform(CompilationUnit cu, Document document) {
    List<BodyDeclaration> matchingNodes = Lists.newArrayList();
    // This is inefficient but it is very simple.
    for (BodyDeclarationLocater locater : blacklist) {
      BodyDeclaration bodyDeclaration = locater.find(cu);
      if (bodyDeclaration != null) {
        matchingNodes.add(bodyDeclaration);
      }
    }
    // Tackle nodes in reverse order to avoid messing up the ASTNode offsets.
    Collections.sort(matchingNodes, new StartPositionComparator());
    for (BodyDeclaration bodyDeclaration : Lists.reverse(matchingNodes)) {
      JavadocUtils.insertCommentText(document, bodyDeclaration, HIDDEN_TAG_COMMENT);
    }
  }
}

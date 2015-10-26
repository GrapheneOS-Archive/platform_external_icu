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

import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jface.text.Document;

import java.util.List;

/**
 * Inserts the tag text to the Javadoc for any Javadoc comment that matches
 * {@link #mustTag(org.eclipse.jdt.core.dom.Javadoc)}.
 */
public abstract class JavadocTagJavadoc implements DocumentTransformer {

  private final String tagText;

  protected JavadocTagJavadoc(String tagText) {
    this.tagText = tagText;
  }

  @Override
  public void transform(CompilationUnit cu, Document document) {
    for (Comment comment : Lists.reverse((List<Comment>) cu.getCommentList())) {
      if (comment.isDocComment()) {
        Javadoc javadoc = (Javadoc) comment;
        if (mustTag(javadoc)) {
          JavadocUtils.insertCommentText(document, javadoc, tagText);
        }
      }
    }
  }

  protected abstract boolean mustTag(Javadoc node);
}

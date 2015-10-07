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

import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A hacky {@link DocumentTransformer} that escapes ICU tags that doclava doesn't understand.
 * This will evolve over time as we either transform the tags into something else, add support to
 * doclava or remove them more correctly.
 */
public class MungeIcuJavaDocTags implements DocumentTransformer {

  /** The set of problem tags. The pattern contains a capturing group for the "@". */
  private final static Pattern PATTERN =
      Pattern.compile(
          "(@)(?:icu|internal|draft|provisional|icuenhanced|icunote|\\.jcite|obsolete)");

  @Override public void transform(CompilationUnit cu, Document document) {
    for (Comment comment : Lists.reverse((List<Comment>) cu.getCommentList())) {
      String commentString;
      try {
        commentString = document.get(comment.getStartPosition(), comment.getLength());
      } catch (BadLocationException e) {
        throw new AssertionError(e);
      }

      // Build a list of the offsets of the "@" for problematic tags.
      List<Integer> offsets = new ArrayList<>();
      Matcher matcher = PATTERN.matcher(commentString);
      while (matcher.find()) {
        offsets.add(matcher.start(0 /* group */));
      }

      if (!offsets.isEmpty()) {
        // For problematic tags, replace the "@" with "{@literal @}". This happens in reverse order
        // so the offsets remain valid after replacement.
        StringBuilder adjustedCommentString = new StringBuilder(commentString);
        for (Integer offset : Lists.reverse(offsets)) {
          adjustedCommentString.replace(offset, offset + 1, "{@literal @}");
        }

        // Write back the adjusted comment.
        try {
          document.replace(comment.getStartPosition(), comment.getLength(),
              adjustedCommentString.toString());
        } catch (BadLocationException e) {
          throw new AssertionError(e);
        }
      }
    }
  }
}

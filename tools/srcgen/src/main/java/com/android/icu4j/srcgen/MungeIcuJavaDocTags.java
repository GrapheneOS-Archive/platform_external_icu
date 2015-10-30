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
import com.google.currysrc.transformers.BaseModifyCommentScanner;

import org.eclipse.jdt.core.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A hacky {@link DocumentTransformer} that escapes ICU tags that doclava doesn't understand.
 * This will evolve over time as we either transform the tags into something else, add support to
 * doclava or remove them more correctly.
 */
public class MungeIcuJavaDocTags extends BaseModifyCommentScanner {

  /** The set of problem tags. The pattern contains a capturing group for the "@". */
  private final static Pattern PATTERN =
      Pattern.compile(
          "(@)(?:icu|internal|draft|provisional|icuenhanced|icunote|\\.jcite|obsolete)");

  @Override protected String generateReplacementText(Comment commentNode, String commentText) {
    if (!commentNode.isDocComment()) {
      return null;
    }

    // Build a list of the offsets of the "@" for problematic tags.
    List<Integer> offsets = new ArrayList<>();
    Matcher matcher = PATTERN.matcher(commentText);
    while (matcher.find()) {
      offsets.add(matcher.start(0 /* group */));
    }

    if (offsets.isEmpty()) {
      return null;
    }

    // For problematic tags, replace the "@" with "{@literal @}". This happens in reverse order
    // so the offsets remain valid after replacement.
    StringBuilder adjustedCommentString = new StringBuilder(commentText);
    for (Integer offset : Lists.reverse(offsets)) {
      adjustedCommentString.replace(offset, offset + 1, "{@literal @}");
    }
    return adjustedCommentString.toString();
  }
}

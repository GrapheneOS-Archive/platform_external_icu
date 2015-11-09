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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.currysrc.api.transform.ast.BodyDeclarationLocater;
import com.google.currysrc.api.transform.ast.BodyDeclarationLocaters;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.Javadoc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Replaces selected Javadoc comments with canned text.
 */
public class ReplaceSelectedJavadoc extends BaseModifyCommentScanner {

  private static final String MARKER = "--";

  private List<Replacement> replacements;

  public ReplaceSelectedJavadoc(List<Replacement> replacements) {
    this.replacements = ImmutableList.copyOf(replacements);
  }

  @Override protected String generateReplacementText(Comment commentNode, String commentText) {
    if (!(commentNode instanceof Javadoc)) {
      return null;
    }
    Javadoc javadoc = (Javadoc) commentNode;
    ASTNode declarationNode = javadoc.getParent();
    if (declarationNode == null || !(declarationNode instanceof BodyDeclaration)) {
      return null;
    }

    BodyDeclaration bodyDeclaration = (BodyDeclaration) declarationNode;
    for (Replacement replacement : replacements) {
      if (replacement.locater.matches(bodyDeclaration)) {
        return replacement.replacementText;
      }
    }
    return null;
  }

  /**
   * Reads a file containing replacement javadoc.
   *
   * <p>The format is:
   * <pre>
   *   --method:foo.Bar#Bar(String)
   *     Replacement text.
   *   --
   *
   *   # This is a comment.
   *   --field:foo.Bar#baz
   *     Replacement text.
   *   --
   * </pre>
   *
   * <p>Empty lines are ignored. Lines beginning with # are ignored. The first line of a block must
   * start with "--" and be immediately followed by a {@link BodyDeclarationLocater} string. The
   * following lines are read verbatim until a line containing closing marker, also "--".
   */
  public static ReplaceSelectedJavadoc createFromResource(String resourceName) throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream is = classLoader.getResourceAsStream(resourceName);
    if (is == null) {
      throw new FileNotFoundException("Unknown resource: " + resourceName);
    }
    Reader fileReader = new InputStreamReader(is, StandardCharsets.UTF_8);
    try (LineNumberReader reader = new LineNumberReader(fileReader)) {
      List<Replacement> replacements = Lists.newArrayList();
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.trim().isEmpty() || line.startsWith("#")) {
          // Forgive empty lines and comments.
          continue;
        }
        if (!line.startsWith(MARKER)) {
          throw new IOException(
              "Unexpected locater line: " + line + " at line " + reader.getLineNumber());
        }
        String locaterString = line.substring(MARKER.length());
        BodyDeclarationLocater locater = BodyDeclarationLocaters.fromStringForm(locaterString);

        // Read the replacement comment.
        String replacementText = readUntilMarker(reader);
        replacements.add(new Replacement(locater, replacementText));
      }
      return new ReplaceSelectedJavadoc(replacements);
    }
  }

  private static String readUntilMarker(LineNumberReader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      if (line.startsWith(MARKER)) {
        break;
      }
      builder.append(line);
      builder.append("\n");
    }
    if (line == null) {
      throw new IOException("Did not find closing marker at end of file " + reader.getLineNumber());
    }
    // Remove the trailing \n
    builder.setLength(builder.length() - 1);
    return builder.toString();
  }

  public static class Replacement {
    public final BodyDeclarationLocater locater;
    public final String replacementText;

    public Replacement(BodyDeclarationLocater locater, String replacementText) {
      this.locater = locater;
      this.replacementText = replacementText;
    }
  }
}

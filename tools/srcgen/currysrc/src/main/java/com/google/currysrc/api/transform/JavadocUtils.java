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
package com.google.currysrc.api.transform;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IRegion;

/**
 * Basic utility methods for modifying Javadoc.
 */
public final class JavadocUtils {

  private JavadocUtils() {
  }

  public static void insertCommentText(Document document, BodyDeclaration bodyDeclaration,
      String text) {
    Javadoc comment = bodyDeclaration.getJavadoc();
    if (comment == null) {
      try {
        document.replace(bodyDeclaration.getStartPosition(), 0, "/** " + text + " */\n");
      } catch (BadLocationException e) {
        throw new AssertionError(e);
      }
    } else {
      insertCommentText(document, comment, text);
    }
  }

  private static String determineIndent(String line) {
    StringBuilder indentBuilder = new StringBuilder();
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (!Character.isWhitespace(c)) {
        break;
      }
      indentBuilder.append(c);
    }
    return indentBuilder.toString();
  }

  public static void insertCommentText(Document document, Javadoc javadoc, String text) {
    try {
      int insertPosition;
      String insertText;
      int firstLineIndex = document.getLineOfOffset(javadoc.getStartPosition());
      int lastLineIndex = document
          .getLineOfOffset(javadoc.getStartPosition() + javadoc.getLength() - 1);
      if (firstLineIndex == lastLineIndex) {
        // Single line comment: /** foo */
        IRegion lineInfo = document
            .getLineInformationOfOffset(javadoc.getStartPosition() + javadoc.getLength() - 1);
        String line = document.get(lineInfo.getOffset(), lineInfo.getLength());
        String indent = determineIndent(line);
        insertPosition = lineInfo.getOffset() + lineInfo.getLength() - 1 - "*/".length();
        insertText = document.getDefaultLineDelimiter() + indent + "* " + text + document
            .getDefaultLineDelimiter() + indent;
      } else {
        // TODO - write a decent Javadoc comment parser / generator instead.
        IRegion lastLineInfo = document
            .getLineInformationOfOffset(javadoc.getStartPosition() + javadoc.getLength() - 1);
        String lastLine = document.get(lastLineInfo.getOffset(), lastLineInfo.getLength());
        String indent = determineIndent(lastLine);
        insertPosition = lastLineInfo.getOffset();
        insertText = indent + "* " + text + document.getDefaultLineDelimiter();
      }

      document.replace(insertPosition, 0, insertText);
    } catch (BadLocationException e) {
      throw new AssertionError(e);
    }
  }
}

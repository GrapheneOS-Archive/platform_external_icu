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
package com.google.currysrc;

import com.google.currysrc.api.transform.AstTransformRule;
import com.google.currysrc.api.transform.DocumentTransformRule;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import com.google.currysrc.api.Rules;
import com.google.currysrc.api.input.InputFileGenerator;
import com.google.currysrc.api.output.OutputSourceFileGenerator;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The main execution API for users of currysrc.
 */
public class Main {

  private static final Charset JAVA_SOURCE_CHARSET = StandardCharsets.UTF_8;

  private final boolean debug;

  public Main(boolean debug) {
    this.debug = debug;
  }

  public void execute(Rules rules) throws Exception {
    ASTParser parser = ASTParser.newParser(AST.JLS8);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);

    InputFileGenerator inputFileGenerator = rules.getInputFileGenerator();
    OutputSourceFileGenerator outputSourceFileGenerator = rules.getOutputSourceFileGenerator();
    for (File file : inputFileGenerator.generate()) {
      System.out.println("Processing: " + file);
      String source = readSource(file);
      Document document = new Document(source);

      CompilationUnit cu = parseDocument(file, parser, document);
      List<AstTransformRule> astTransformerRules = rules.getAstTransformRules(file);
      if (!astTransformerRules.isEmpty()) {
        TrackingASTRewrite rewrite = createTrackingASTRewrite(cu);
        for (AstTransformRule astTransformRule : astTransformerRules) {
          if (astTransformRule.matches(cu)) {
            astTransformRule.transform(cu, rewrite);
            if (debug) {
              System.out.println(
                  "AST Transformer: " + astTransformRule + ", rewrite: " + (rewrite.isEmpty()
                      ? "None" : rewrite.toString()));
            }
            if (astTransformRule.mustModify() && rewrite.isEmpty()) {
              throw new RuntimeException("AST Transformer Rule: " + astTransformRule
                  + " did not modify compilation unit as it should");
            }
            cu = applyRewrite(file + " after " + astTransformRule, parser, document, rewrite);
            rewrite = createTrackingASTRewrite(cu);
          }
        }
      }
      List<DocumentTransformRule> documentTransformRules = rules.getDocumentTransformRules(file);
      if (!documentTransformRules.isEmpty()) {
        for (DocumentTransformRule documentTransformRule : documentTransformRules) {
          if (documentTransformRule.matches(cu)) {
            String before = document.get();
            documentTransformRule.transform(cu, document);
            String after = document.get();
            if (documentTransformRule.mustModify() && before.equals(after)) {
              throw new RuntimeException("Document Transformer Rule: " + documentTransformRule
                  + " did not modify document as it should");
            }
            if (debug) {
              System.out.println(
                  "Document Transformer: " + documentTransformRule + ", diff: " + generateDiff(
                      before, after));
            }
            // Regenerate the AST
            cu = parseDocument(file + " after document transformer " + documentTransformRule,
                parser, document);
          }
        }
      }

      File outputFile = outputSourceFileGenerator.generate(cu);
      if (outputFile != null) {
        writeSource(document, outputFile);
      }
    }
  }

  private static TrackingASTRewrite createTrackingASTRewrite(CompilationUnit cu) {
    return new TrackingASTRewrite(cu.getAST());
  }

  private static String generateDiff(String before, String after) {
    if (before.equals(after)) {
      return "No diff";
    }
    // TODO Implement this
    return "Diff. DIFF NOT IMPLEMENTED";
  }

  private static CompilationUnit applyRewrite(Object documentId, ASTParser parser,
      Document document, ASTRewrite rewrite) throws BadLocationException {
    TextEdit textEdit = rewrite.rewriteAST(document, null);
    textEdit.apply(document, TextEdit.UPDATE_REGIONS);
    // Reparse the document.
    return parseDocument(documentId, parser, document);
  }

  private static CompilationUnit parseDocument(Object documentId, ASTParser parser,
      Document document) {
    parser.setSource(document.get().toCharArray());
    configureParser(parser);

    CompilationUnit cu = (CompilationUnit) parser.createAST(null /* progressMonitor */);
    if (cu.getProblems().length > 0) {
      System.err.println("Error parsing:" + documentId + ": " + Arrays.toString(cu.getProblems()));
      throw new RuntimeException("Unable to parse document. Stopping.");
    }
    return cu;
  }

  private static void configureParser(ASTParser parser) {
    Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
    options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
    parser.setCompilerOptions(options);
  }

  private static void writeSource(Document document, File outputFile) throws IOException {
    File outputDir = outputFile.getParentFile();
    if (outputDir.exists()) {
      if (!outputDir.isDirectory()) {
        throw new IOException(outputDir + " is not a directory");
      }
    }
    if (!outputDir.exists()) {
      if (!outputDir.mkdirs()) {
        throw new IOException("Unable to create " + outputDir);
      }
    }
    String source = document.get();

    // TODO Look at guava for this
    FileOutputStream fos = new FileOutputStream(outputFile);
    Writer writer = new OutputStreamWriter(fos, JAVA_SOURCE_CHARSET);
    try {
      writer.write(source.toCharArray());
    } finally {
      writer.close();
    }
  }

  private static String readSource(File file) throws IOException {
    StringBuilder sb = new StringBuilder(2048);
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(file), JAVA_SOURCE_CHARSET))) {
      char[] buffer = new char[1024];
      int count;
      while ((count = reader.read(buffer)) != -1) {
        sb.append(buffer, 0, count);
      }
    }
    return sb.toString();
  }

  private static class TrackingASTRewrite extends ASTRewrite {

    public TrackingASTRewrite(AST ast) {
      super(ast);
    }

    public boolean isEmpty() {
      return !getRewriteEventStore().getChangeRootIterator().hasNext();
    }
  }
}

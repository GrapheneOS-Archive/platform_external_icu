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
import com.google.currysrc.Main;
import com.google.currysrc.api.Rules;
import com.google.currysrc.api.input.CompoundDirectoryInputFileGenerator;
import com.google.currysrc.api.input.DirectoryInputFileGenerator;
import com.google.currysrc.api.input.InputFileGenerator;
import com.google.currysrc.api.match.SourceMatchers;
import com.google.currysrc.api.output.OutputSourceFileGenerator;
import com.google.currysrc.api.output.BasicOutputSourceFileGenerator;
import com.google.currysrc.api.transform.AstTransformRule;
import com.google.currysrc.api.transform.AstTransformer;
import com.google.currysrc.api.transform.DocumentTransformRule;
import com.google.currysrc.api.transform.DocumentTransformer;
import com.google.currysrc.api.transform.ast.TypeLocater;
import com.google.currysrc.transformers.HidePublicClasses;
import com.google.currysrc.transformers.InsertHeader;
import com.google.currysrc.transformers.ModifyQualifiedNames;
import com.google.currysrc.transformers.ModifyStringLiterals;
import com.google.currysrc.transformers.RenamePackage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Applies Android's ICU4J source code transformation rules.
 */
public class Icu4jTransform {

  private static final boolean DEBUG = true;

  private Icu4jTransform() {
  }

  /**
   * Usage:
   * java com.android.icu4j.srcgen.Icu4JTransform {one or more source directories} {target dir}
   */
  public static void main(String[] args) throws Exception {
    new Main(DEBUG).execute(new Icu4jRules(args));
  }

  private static class Icu4jRules implements Rules {

    private static final String SOURCE_CODE_HEADER = "/* GENERATED SOURCE. DO NOT MODIFY. */\n";

    private final InputFileGenerator inputFileGenerator;

    private final BasicOutputSourceFileGenerator outputSourceFileGenerator;

    public Icu4jRules(String[] args) {
      if (args.length < 2) {
        throw new IllegalArgumentException("At least 2 arguments required.");
      }

      inputFileGenerator = createInputFileGenerator(args);
      outputSourceFileGenerator = createOutputFileGenerator(args[args.length - 1]);
    }

    public List<AstTransformRule> getAstTransformRules(File file) {
      return Lists.newArrayList(
          createMandatoryRule(new RenamePackage("com.ibm.icu", "android.icu")),
          createOptionalRule(new ModifyQualifiedNames("com.ibm.icu", "android.icu")),
          createOptionalRule(new ModifyStringLiterals("com.ibm.icu", "android.icu")),
          createOptionalRule(new ModifyStringLiterals("com/ibm/icu", "android/icu"))
      );
    }

    private static AstTransformRule createMandatoryRule(AstTransformer transformer) {
      return new AstTransformRule(transformer, SourceMatchers.all(), true /* mustModify */);
    }

    private static AstTransformRule createOptionalRule(AstTransformer transformer) {
      return new AstTransformRule(transformer, SourceMatchers.all(), false /* mustModify */);
    }

    @Override
    public List<DocumentTransformRule> getDocumentTransformRules(File file) {
      return Lists.newArrayList(
          createMandatoryRule(new InsertHeader(SOURCE_CODE_HEADER)),
          createOptionalRule(new HidePublicClasses(Collections.<TypeLocater>emptyList(),
              "All android.icu classes are currently hidden")),
          createOptionalRule(new MungeIcuJavaDocTags()));
    }

    private static DocumentTransformRule createMandatoryRule(DocumentTransformer transformer) {
      return new DocumentTransformRule(transformer, SourceMatchers.all(), true /* mustModify */);
    }

    private static DocumentTransformRule createOptionalRule(DocumentTransformer transformer) {
      return new DocumentTransformRule(transformer, SourceMatchers.all(), false /* mustModify*/);
    }

    @Override
    public InputFileGenerator getInputFileGenerator() {
      return inputFileGenerator;
    }

    @Override
    public OutputSourceFileGenerator getOutputSourceFileGenerator() {
      return outputSourceFileGenerator;
    }

    private static CompoundDirectoryInputFileGenerator createInputFileGenerator(String[] args) {
      List<InputFileGenerator> dirs = new ArrayList<>(args.length - 1);
      for (int i = 0; i < args.length - 1; i++) {
        File inputDir = new File(args[i]);
        if (!isValidDir(inputDir)) {
          throw new IllegalArgumentException("Input dir [" + inputDir + "] does not exist.");
        }
        dirs.add(new DirectoryInputFileGenerator(inputDir));
      }
      return new CompoundDirectoryInputFileGenerator(dirs);
    }

    private static BasicOutputSourceFileGenerator createOutputFileGenerator(String outputDirName) {
      File outputDir = new File(outputDirName);
      if (!isValidDir(outputDir)) {
        throw new IllegalArgumentException("Output dir [" + outputDir + "] does not exist.");
      }
      return new BasicOutputSourceFileGenerator(outputDir);
    }

    private static boolean isValidDir(File outputDir) {
      return outputDir.exists() && outputDir.isDirectory();
    }
  }
}

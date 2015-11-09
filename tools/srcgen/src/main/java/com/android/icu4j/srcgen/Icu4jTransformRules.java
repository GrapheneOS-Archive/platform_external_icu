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

import com.google.currysrc.api.input.CompoundDirectoryInputFileGenerator;
import com.google.currysrc.api.input.DirectoryInputFileGenerator;
import com.google.currysrc.api.input.InputFileGenerator;
import com.google.currysrc.api.input.FilesInputFileGenerator;
import com.google.currysrc.api.match.SourceMatchers;
import com.google.currysrc.api.output.BasicOutputSourceFileGenerator;
import com.google.currysrc.api.transform.AstTransformRule;
import com.google.currysrc.api.transform.AstTransformer;
import com.google.currysrc.api.transform.DocumentTransformRule;
import com.google.currysrc.api.transform.DocumentTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Useful chunks of {@link com.google.currysrc.api.Rules} code shared between various tools.
 */
class Icu4jTransformRules {
  private Icu4jTransformRules() {}

  static CompoundDirectoryInputFileGenerator createInputFileGenerator(String[] args) {
    List<InputFileGenerator> dirs = new ArrayList<>(args.length - 1);
    for (int i = 0; i < args.length - 1; i++) {
      File inputFile = new File(args[i]);
      InputFileGenerator inputFileGenerator;
      if (isValidDir(inputFile)) {
        inputFileGenerator = new DirectoryInputFileGenerator(inputFile);
      } else if (isValidFile(inputFile)) {
        inputFileGenerator = new FilesInputFileGenerator(inputFile);
      } else {
        throw new IllegalArgumentException("Input arg [" + inputFile + "] does not exist.");
      }
      dirs.add(inputFileGenerator);
    }
    return new CompoundDirectoryInputFileGenerator(dirs);
  }

  static BasicOutputSourceFileGenerator createOutputFileGenerator(String outputDirName) {
    File outputDir = new File(outputDirName);
    if (!isValidDir(outputDir)) {
      throw new IllegalArgumentException("Output dir [" + outputDir + "] does not exist.");
    }
    return new BasicOutputSourceFileGenerator(outputDir);
  }

  static AstTransformRule createMandatoryRule(AstTransformer transformer) {
    return new AstTransformRule(transformer, SourceMatchers.all(), true /* mustModify */);
  }

  static AstTransformRule createOptionalRule(AstTransformer transformer) {
    return new AstTransformRule(transformer, SourceMatchers.all(), false /* mustModify */);
  }

  static DocumentTransformRule createMandatoryRule(DocumentTransformer transformer) {
    return new DocumentTransformRule(transformer, SourceMatchers.all(), true /* mustModify */);
  }

  static DocumentTransformRule createOptionalRule(DocumentTransformer transformer) {
    return new DocumentTransformRule(transformer, SourceMatchers.all(), false /* mustModify*/);
  }

  private static boolean isValidDir(File dir) {
    return dir.exists() && dir.isDirectory();
  }

  private static boolean isValidFile(File file) {
    return file.exists() && file.isFile();
  }

}

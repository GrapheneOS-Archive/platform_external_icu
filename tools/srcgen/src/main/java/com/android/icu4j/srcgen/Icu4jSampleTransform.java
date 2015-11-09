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
import com.google.currysrc.api.input.InputFileGenerator;
import com.google.currysrc.api.output.BasicOutputSourceFileGenerator;
import com.google.currysrc.api.output.OutputSourceFileGenerator;
import com.google.currysrc.api.transform.TransformRule;

import java.io.File;
import java.util.List;

import static com.android.icu4j.srcgen.Icu4jTransformRules.createOptionalRule;

/**
 * Applies Android's ICU4J source code transformation rules to sample code and fixes up the
 * jcite start/end tags so they can be used with doclava.
 */
public class Icu4jSampleTransform {

  private static final boolean DEBUG = false;

  private Icu4jSampleTransform() {
  }

  /**
   * Usage:
   * java com.android.icu4j.srcgen.Icu4jSampleTransform {source files/directories} {target dir}
   */
  public static void main(String[] args) throws Exception {
    new Main(DEBUG).execute(new Icu4jSampleRules(args));
  }

  private static class Icu4jSampleRules implements Rules {

    private final InputFileGenerator inputFileGenerator;

    private final BasicOutputSourceFileGenerator outputSourceFileGenerator;

    public Icu4jSampleRules(String[] args) {
      if (args.length < 2) {
        throw new IllegalArgumentException("At least 2 arguments required.");
      }

      inputFileGenerator = Icu4jTransformRules.createInputFileGenerator(args);
      outputSourceFileGenerator = Icu4jTransformRules.createOutputFileGenerator(
          args[args.length - 1]);
    }

    @Override
    public List<TransformRule> getTransformRules(File file) {
      List<TransformRule> transformRules =
          Lists.newArrayList(Icu4jTransform.Icu4jRules.getRepackagingRules());

      // Change sample jcite begin / end tags ---XYZ to Androids 'BEGIN(XYZ)' / 'END(XYZ)'
      transformRules.add(createOptionalRule(new TranslateJcite.BeginEndTagsHandler()));

      return transformRules;
    }

    @Override
    public InputFileGenerator getInputFileGenerator() {
      return inputFileGenerator;
    }

    @Override
    public OutputSourceFileGenerator getOutputSourceFileGenerator() {
      return outputSourceFileGenerator;
    }
  }
}

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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.currysrc.Main;
import com.google.currysrc.api.Rules;
import com.google.currysrc.api.input.CompoundDirectoryInputFileGenerator;
import com.google.currysrc.api.input.DirectoryInputFileGenerator;
import com.google.currysrc.api.input.InputFileGenerator;
import com.google.currysrc.api.match.SourceMatchers;
import com.google.currysrc.api.output.NullOutputSourceFileGenerator;
import com.google.currysrc.api.output.OutputSourceFileGenerator;
import com.google.currysrc.api.transform.DocumentTransformRule;
import com.google.currysrc.api.transform.DocumentTransformer;
import com.google.currysrc.api.transform.TransformRule;
import com.google.currysrc.api.transform.ast.BodyDeclarationLocaters;
import com.google.currysrc.api.transform.ast.TypeLocater;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jface.text.Document;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates text that can be injected into Icu4JTransform for describing source elements that
 * should be hidden because they are deprecated. Only intended for use in capturing the
 * <em>initial set</em> of ICU elements to be hidden. Typically, anything that ICU deprecates in
 * future should remain public until they can safely be removed from Android's public APIs.
 */
public class CaptureDeprecatedElements {

  private static final boolean DEBUG = true;

  private static final String ANDROID_ICU_PREFIX = "android.icu.";
  private static final String ORIGINAL_ICU_PREFIX = "com.ibm.icu.";

  private CaptureDeprecatedElements() {
  }

  /**
   * Usage:
   * java com.android.icu4j.srcgen.CaptureDeprecatedMethods {one or more source directories}
   */
  public static void main(String[] args) throws Exception {
    CaptureDeprecatedMethodsRules rules = new CaptureDeprecatedMethodsRules(args);
    new Main(DEBUG).execute(rules);
    List<String> deprecatedElements = rules.getCaptureRule().getDeprecatedElements();

    // ASCII order for easier maintenance of the source this goes into.
    List<String> sortedDeprecatedElements = Lists.newArrayList(deprecatedElements);
    Collections.sort(sortedDeprecatedElements);
    for (String entry : sortedDeprecatedElements) {
      String entryInAndroid = entry.replace(ORIGINAL_ICU_PREFIX, ANDROID_ICU_PREFIX);
      System.out.println("      \"" + entryInAndroid + "\",");
    }
  }

  private static class CaptureDeprecatedMethodsRules implements Rules {

    private final InputFileGenerator inputFileGenerator;

    private final CaptureDeprecatedTransformer captureTransformer;

    public CaptureDeprecatedMethodsRules(String[] args) {
      if (args.length < 1) {
        throw new IllegalArgumentException("At least 1 argument required.");
      }
      inputFileGenerator = createInputFileGenerator(args);

      ImmutableList.Builder<TypeLocater> apiClassesWhitelistBuilder = ImmutableList.builder();
      for (String publicClassName : Icu4jTransform.PUBLIC_API_CLASSES) {
        String originalIcuClassName = publicClassName.replace(ANDROID_ICU_PREFIX,
            ORIGINAL_ICU_PREFIX);
        apiClassesWhitelistBuilder.add(new TypeLocater(originalIcuClassName));
      }
      captureTransformer = new CaptureDeprecatedTransformer(apiClassesWhitelistBuilder.build());
    }

    @Override
    public List<TransformRule> getTransformRules(File file) {
      return Lists.<TransformRule>newArrayList(
          new DocumentTransformRule(captureTransformer, SourceMatchers.all(), false /* mustModify*/));
    }

    @Override
    public InputFileGenerator getInputFileGenerator() {
      return inputFileGenerator;
    }

    @Override
    public OutputSourceFileGenerator getOutputSourceFileGenerator() {
      return NullOutputSourceFileGenerator.INSTANCE;
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

    private static boolean isValidDir(File outputDir) {
      return outputDir.exists() && outputDir.isDirectory();
    }

    public CaptureDeprecatedTransformer getCaptureRule() {
      return captureTransformer;
    }
  }

  private static class CaptureDeprecatedTransformer implements DocumentTransformer {

    private final List<TypeLocater> publicClassLocaters;
    private final List<String> deprecatedElements = Lists.newArrayList();

    public CaptureDeprecatedTransformer(List<TypeLocater> publicClassLocaters) {
      this.publicClassLocaters = publicClassLocaters;
    }

    @Override public void transform(CompilationUnit cu, Document document) {
      for (TypeLocater publicClassLocater : publicClassLocaters) {
        AbstractTypeDeclaration matchedType = publicClassLocater.find(cu);
        if (matchedType != null) {
          if (isDeprecated(matchedType)) {
            String typeName = BodyDeclarationLocaters.toLocaterStringForm(matchedType);
            deprecatedElements.add(typeName);
          }
          trackDeprecationsRecursively(matchedType);
        }
      }
    }

    private void trackDeprecationsRecursively(AbstractTypeDeclaration matchedType) {
      for (BodyDeclaration bodyDeclaration : (List<BodyDeclaration>) matchedType.bodyDeclarations()) {
        if (isApiVisible(bodyDeclaration) && isDeprecated(bodyDeclaration)) {
          deprecatedElements.add(BodyDeclarationLocaters.toLocaterStringForm(bodyDeclaration));
          if (bodyDeclaration instanceof AbstractTypeDeclaration) {
            trackDeprecationsRecursively((AbstractTypeDeclaration) bodyDeclaration);
          }
        }
      }
    }

    private boolean isApiVisible(BodyDeclaration bodyDeclaration) {
      // public elements and those that might be inherited are ones that might show up in the APIs.
      return (bodyDeclaration.getModifiers() & (Modifier.PROTECTED | Modifier.PUBLIC)) > 0;
    }

    private static boolean isDeprecated(BodyDeclaration bodyDeclaration) {
      Javadoc doc = bodyDeclaration.getJavadoc();
      // This only checks for the @deprecated javadoc tag, not the java.lang.Deprecated annotation.
      if (doc != null) {
        for (TagElement tag : (List<TagElement>) doc.tags()) {
          if (tag.getTagName() != null && tag.getTagName().equalsIgnoreCase("@deprecated")) {
            return true;
          }
        }
      }
      return false;
    }

    public List<String> getDeprecatedElements() {
      return deprecatedElements;
    }
  }
}

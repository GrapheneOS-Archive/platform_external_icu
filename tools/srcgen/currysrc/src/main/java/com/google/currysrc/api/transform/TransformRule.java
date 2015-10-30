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

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;

/**
 * The interface for all transform rules. Transform rules can be matched against a
 * {@link CompilationUnit} and, if applied, can indicate whether they must have made a
 * modification to be considered successful.
 */
public interface TransformRule {

  /**
   * Enables the TransformRule to request what it needs to support transformation. Only one method
   * must be called.
   */
  interface Context {
    /** Returns an ASTRewrite for AST modifications. */
    ASTRewrite rewrite();
    /** Returns a Document for direct text manipulation. */
    Document document();
  }

  /**
   * Returns {@code true} if this rule should be applied to the supplied {@link CompilationUnit}.
   */
  boolean matches(CompilationUnit cu);

  /**
   * Generate a transform for the supplied {@link CompilationUnit}. TransformRules must not modify
   * the {@code compilationUnit} directly: they must request an appropriate tool from the
   * {@code context}. Only one tool can be requested from {@code context}.
   */
  void transform(Context context, CompilationUnit cu);

  /** Returns {@code true} if the rule must make a modification to be considered successful. */
  boolean mustModify();
}

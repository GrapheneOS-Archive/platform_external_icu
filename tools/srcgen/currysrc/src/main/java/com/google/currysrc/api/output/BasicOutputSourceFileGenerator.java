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
package com.google.currysrc.api.output;

import org.eclipse.jdt.core.dom.*;

import com.google.currysrc.api.output.OutputSourceFileGenerator;

import java.io.File;
import java.util.List;

/**
 * Generate the output source file name from a CompilationUnit's type information.
 */
public final class BasicOutputSourceFileGenerator implements OutputSourceFileGenerator {

  private final File baseDir;

  public BasicOutputSourceFileGenerator(File baseDir) {
    this.baseDir = baseDir;
  }

  @Override
  public File generate(CompilationUnit cu) {
    PackageDeclaration outputPackage = cu.getPackage();
    List<AbstractTypeDeclaration> types = (List<AbstractTypeDeclaration>) cu.types();
    String sourceFileName;
    if (types.isEmpty()) {
      System.out.println("No types found in CompilationUnit. Assuming package-info.java");
      sourceFileName = "package-info.java";
    } else {
      AbstractTypeDeclaration firstClass = types.get(0);
      AbstractTypeDeclaration publicClass = null;
      for (AbstractTypeDeclaration type : types) {
        if ((type.getModifiers() & Modifier.PUBLIC) > 0) {
          publicClass = type;
          break;
        }
      }
      AbstractTypeDeclaration sourceNameType = publicClass;
      if (sourceNameType == null) {
        sourceNameType = firstClass;
      }
      sourceFileName = sourceNameType.getName().getIdentifier() + ".java";
    }

    String fqn = outputPackage.getName().getFullyQualifiedName();
    String packageSubDir = fqn.replace(".", File.separator);
    return new File(new File(baseDir, packageSubDir), sourceFileName);
  }
}

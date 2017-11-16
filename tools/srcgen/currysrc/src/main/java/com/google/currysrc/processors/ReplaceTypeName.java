/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.google.currysrc.processors;

import com.google.currysrc.api.process.Context;
import com.google.currysrc.api.process.Processor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.Map;
import java.util.Objects;

/**
 * Replace occurrences of the given type names in
 * {@link ImportDeclaration}, {@link SimpleType}, and {@link QualifiedType}
 */
public class ReplaceTypeName implements Processor {

    private final Map<String, String> replacements;

    /**
     * @param replacements maps fully qualified names of imports to the names
     *        that they should be replaced with.
     */
    public ReplaceTypeName(Map<String, String> replacements) {
        this.replacements = Objects.requireNonNull(replacements);
    }

    @Override
    public void process(Context context, CompilationUnit cu) {
        final ASTRewrite rewrite = context.rewrite();
        cu.accept(new ASTVisitor() {
            @Override
            public boolean visit(SimpleType node) {
                replace(node.getName());
                return super.visit(node);
            }

            @Override
            public boolean visit(QualifiedType node) {
                replace(node.getName());
                return super.visit(node);
            }

            @Override
            public boolean visit(ImportDeclaration node) {
                replace(node.getName());
                return super.visit(node);
            }

            private void replace(Name oldName) {
                String replacement = replacements.get(oldName.getFullyQualifiedName());
                if (replacement != null) {
                    Name newName = cu.getAST().newName(replacement);
                    rewrite.replace(oldName, newName, null);
                }
            }
        });
    }
}

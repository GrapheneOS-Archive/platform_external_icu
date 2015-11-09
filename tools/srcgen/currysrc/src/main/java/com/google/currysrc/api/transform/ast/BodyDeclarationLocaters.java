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
package com.google.currysrc.api.transform.ast;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.Collections;
import java.util.List;

/**
 * Utility methods associated with {@link BodyDeclarationLocater} and its standard implementations.
 */
public final class BodyDeclarationLocaters {

  private final static BiMap<Integer, String> NODE_TYPE_TO_STRING_NAME =
      ImmutableBiMap.<Integer, String>builder()
          .put(ASTNode.FIELD_DECLARATION, "field")
          .put(ASTNode.METHOD_DECLARATION, "method")
          .put(ASTNode.TYPE_DECLARATION, "class")
          .put(ASTNode.ENUM_DECLARATION, "enum")
          .put(ASTNode.ENUM_CONSTANT_DECLARATION, "enumConstant")
          .build();

  private BodyDeclarationLocaters() {
  }

  public static List<BodyDeclarationLocater> createLocatersFromStrings(String[] locaterStrings) {
    ImmutableList.Builder<BodyDeclarationLocater> locaterListBuilder = ImmutableList.builder();
    for (String locaterString : locaterStrings) {
      BodyDeclarationLocater locater = BodyDeclarationLocaters.fromStringForm(locaterString);
      locaterListBuilder.add(locater);
    }
    return locaterListBuilder.build();
  }

  /**
   * Generates a string that can be used with {@link #fromStringForm(String)} to generate a
   * {@link BodyDeclarationLocater} capable of locating the supplied node.
   */
  public static String toLocaterStringForm(BodyDeclaration bodyDeclaration) {
    return getDeclarationTypeString(bodyDeclaration) + ":" + createName(bodyDeclaration);
  }

  /**
   * Creates a {@link BodyDeclarationLocater} from a string form of a body declaration.
   * See {@link #toLocaterStringForm(BodyDeclaration)}.
   */
  public static BodyDeclarationLocater fromStringForm(String stringForm) {
    List<String> components = splitInTwo(stringForm, ":");
    int nodeType = getDeclarationNodeType(components.get(0));
    String locaterString = components.get(1);
    switch (nodeType) {
      case ASTNode.FIELD_DECLARATION:
        List<String> typeAndField = splitInTwo(locaterString, "#");
        return new FieldLocater(new TypeLocater(typeAndField.get(0)), typeAndField.get(1));
      case ASTNode.METHOD_DECLARATION:
        List<String> typeAndMethod = splitInTwo(locaterString, "#");
        String methodNameAndParameters = typeAndMethod.get(1);
        int parameterStartIndex = methodNameAndParameters.indexOf('(');
        if (parameterStartIndex == -1) {
          throw new IllegalArgumentException("No '(' found in " + methodNameAndParameters);
        }
        String methodName = methodNameAndParameters.substring(0, parameterStartIndex);
        String parametersString = methodNameAndParameters.substring(parameterStartIndex);
        List<String> parameterTypes = extractParameterTypes(parametersString);
        return new MethodLocater(new TypeLocater(typeAndMethod.get(0)), methodName, parameterTypes);
      case ASTNode.TYPE_DECLARATION:
      case ASTNode.ENUM_DECLARATION:
        return new TypeLocater(locaterString);
      case ASTNode.ENUM_CONSTANT_DECLARATION:
        List<String> typeAndConstant = splitInTwo(locaterString, "#");
        return new EnumConstantLocater(
            new TypeLocater(typeAndConstant.get(0)), typeAndConstant.get(1));
      default:
        throw new IllegalArgumentException("Unsupported node type: " + nodeType);
    }
  }

  public static boolean matchesAny(List<BodyDeclarationLocater> locaters, BodyDeclaration node) {
    for (BodyDeclarationLocater locater : locaters) {
      if (locater.matches(node)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Finds the declaration associated with a given node. If the node is not a child of a declaration
   * {@code null} is returned.
   */
  public static BodyDeclaration findDeclarationNode(ASTNode node) {
    ASTNode ancestor = node;
    while (ancestor != null && !(ancestor instanceof BodyDeclaration)) {
      ancestor = ancestor.getParent();
    }

    return ancestor instanceof BodyDeclaration ? (BodyDeclaration) ancestor : null;
  }

  private static String getDeclarationTypeString(BodyDeclaration bodyDeclaration) {
    String typeString = NODE_TYPE_TO_STRING_NAME.get(bodyDeclaration.getNodeType());
    if (typeString == null) {
      throw new IllegalArgumentException("Unsupported node type: " + bodyDeclaration);
    }
    return typeString;
  }

  private static int getDeclarationNodeType(String bodyDeclarationTypeString) {
    Integer nodeType = NODE_TYPE_TO_STRING_NAME.inverse().get(bodyDeclarationTypeString);
    if (nodeType == null) {
      throw new IllegalArgumentException(
          "Unsupported node type string: " + bodyDeclarationTypeString);
    }
    return nodeType;
  }

  private static String createName(BodyDeclaration bodyDeclaration) {
    if (bodyDeclaration instanceof AbstractTypeDeclaration) {
      AbstractTypeDeclaration abstractTypeDeclaration = (AbstractTypeDeclaration) bodyDeclaration;
      if (abstractTypeDeclaration.isPackageMemberTypeDeclaration()) {
        // Top-level class
        CompilationUnit cu = (CompilationUnit) abstractTypeDeclaration.getParent();
        return cu.getPackage().getName().getFullyQualifiedName() + "." +
            abstractTypeDeclaration.getName().getFullyQualifiedName();
      }
    }

    switch (bodyDeclaration.getNodeType()) {
      case ASTNode.FIELD_DECLARATION: {
        // Ignores the possibility of multiple fields covered by a single javadoc.
        FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
        List<VariableDeclarationFragment> fieldDeclarations =
            (List<VariableDeclarationFragment>) fieldDeclaration.fragments();
        if (fieldDeclarations.size() > 1) {
          throw new AssertionError("No field name found: multiple found");
        }
        AbstractTypeDeclaration parentType =
            (AbstractTypeDeclaration) fieldDeclaration.getParent();
        return createName(parentType) + "#" + fieldDeclarations.get(0).getName();
      }
      case ASTNode.METHOD_DECLARATION: {
        MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
        AbstractTypeDeclaration parentType =
            (AbstractTypeDeclaration) methodDeclaration.getParent();
        return createName(parentType) + "#" + methodDeclaration.getName()
            + createParameterTypeListString(methodDeclaration.parameters());
      }
      case ASTNode.TYPE_DECLARATION:
      case ASTNode.ENUM_DECLARATION: {
        AbstractTypeDeclaration nestedTypeDeclaration = (AbstractTypeDeclaration) bodyDeclaration;
        AbstractTypeDeclaration parentType =
            (AbstractTypeDeclaration) nestedTypeDeclaration.getParent();
        return createName(parentType) + "$" + nestedTypeDeclaration.getName();
      }
      case ASTNode.ENUM_CONSTANT_DECLARATION: {
        EnumConstantDeclaration enumConstantDeclaration =
            (EnumConstantDeclaration) bodyDeclaration;
        EnumDeclaration parentType = (EnumDeclaration) enumConstantDeclaration.getParent();
        return createName(parentType) + "#" + enumConstantDeclaration.getName();
      }
      default:
        throw new AssertionError("Unsupported node type: " + bodyDeclaration);
    }
  }

  private static String createParameterTypeListString(List<SingleVariableDeclaration> parameters) {
    return "(" + Joiner.on(",").join(createParameterTypeList(parameters)) + ")";
  }

  private static List<String> createParameterTypeList(List<SingleVariableDeclaration> parameters) {
    List<String> types = Lists.newArrayList();
    for (SingleVariableDeclaration singleVariableDeclaration : parameters) {
      Type type = singleVariableDeclaration.getType();
      // toString() does the right thing in all cases.
      types.add(type.toString());
    }
    return types;
  }

  private static List<String> extractParameterTypes(String parametersString) {
    if (!(parametersString.startsWith("(") && parametersString.endsWith(")"))) {
      throw new IllegalArgumentException("Expected \"(<types>)\" but was " + parametersString);
    }
    parametersString = parametersString.substring(1, parametersString.length() - 1);
    if (parametersString.isEmpty()) {
      return Collections.emptyList();
    }
    return Splitter.on(',').splitToList(parametersString);
  }

  private static List<String> splitInTwo(String string, String separator) {
    List<String> components = Splitter.on(separator).splitToList(string);
    if (components.size() != 2) {
      throw new IllegalArgumentException("Cannot split " + string + " on " + separator);
    }
    return components;
  }

}

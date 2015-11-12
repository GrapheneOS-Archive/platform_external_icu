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

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods associated with {@link BodyDeclarationLocater} and its standard implementations.
 */
public final class BodyDeclarationLocaters {

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
   * Generates strings that can be used with {@link #fromStringForm(String)} to generate
   * {@link BodyDeclarationLocater} instances capable of locating the supplied node. Usually returns
   * a single element, except for fields declarations.
   */
  public static List<String> toLocaterStringForms(BodyDeclaration bodyDeclaration) {
    List<BodyDeclarationLocater> locaters = createLocaters(bodyDeclaration);
    List<String> stringForms = new ArrayList<>(locaters.size());
    for (BodyDeclarationLocater locater : locaters) {
      stringForms.add(locater.getStringFormType() + ":" + locater.getStringFormTarget());
    }
    return stringForms;
  }

  /**
   * Creates {@link BodyDeclarationLocater} objects that can find the supplied
   * {@link BodyDeclaration}. Usually returns a single element, except for fields declarations.
   */
  public static List<BodyDeclarationLocater> createLocaters(BodyDeclaration bodyDeclaration) {
    AbstractTypeDeclaration typeDeclaration = TypeLocater.findTypeDeclarationNode(bodyDeclaration);
    if (typeDeclaration == null) {
      throw new AssertionError("Unable to find type declaration for " + typeDeclaration);
    }
    TypeLocater typeLocater = new TypeLocater(typeDeclaration);

    int nodeType = bodyDeclaration.getNodeType();
    switch (nodeType) {
      case ASTNode.FIELD_DECLARATION:
        List<String> fieldNames = FieldLocater.getFieldNames((FieldDeclaration) bodyDeclaration);
        List<BodyDeclarationLocater> fieldLocaters = new ArrayList<>(fieldNames.size());
        for (String fieldName : fieldNames) {
          fieldLocaters.add(new FieldLocater(typeLocater, fieldName));
        }
        return fieldLocaters;
      case ASTNode.METHOD_DECLARATION:
        MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
        List<String> parameterTypeNames = ParameterMatcher.getParameterTypeNames(methodDeclaration);
        return ImmutableList.<BodyDeclarationLocater>of(
            new MethodLocater(typeLocater, methodDeclaration.getName().getIdentifier(),
            parameterTypeNames));
      case ASTNode.TYPE_DECLARATION:
      case ASTNode.ENUM_DECLARATION:
        return ImmutableList.<BodyDeclarationLocater>of(typeLocater);
      case ASTNode.ENUM_CONSTANT_DECLARATION:
        EnumConstantDeclaration enumConstantDeclaration = (EnumConstantDeclaration) bodyDeclaration;
        String constantName = enumConstantDeclaration.getName().getIdentifier();
        return ImmutableList.<BodyDeclarationLocater>of(
            new EnumConstantLocater(typeLocater, constantName));
      default:
        throw new IllegalArgumentException("Unsupported node type: " + nodeType);
    }
  }

  /**
   * Creates a {@link BodyDeclarationLocater} from a string form of a body declaration.
   * See {@link #toLocaterStringForms(BodyDeclaration)}.
   */
  public static BodyDeclarationLocater fromStringForm(String stringForm) {
    List<String> components = splitInTwo(stringForm, ":");
    String locaterTypeName = components.get(0);
    String locaterString = components.get(1);
    switch (locaterTypeName) {
      case FieldLocater.LOCATER_TYPE_NAME:
        List<String> typeAndField = splitInTwo(locaterString, "#");
        return new FieldLocater(new TypeLocater(typeAndField.get(0)), typeAndField.get(1));
      case MethodLocater.LOCATER_TYPE_NAME:
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
      case TypeLocater.LOCATER_TYPE_NAME:
        return new TypeLocater(locaterString);
      case EnumConstantLocater.LOCATER_TYPE_NAME:
        List<String> typeAndConstant = splitInTwo(locaterString, "#");
        return new EnumConstantLocater(
            new TypeLocater(typeAndConstant.get(0)), typeAndConstant.get(1));
      default:
        throw new IllegalArgumentException("Unsupported locater type: " + locaterTypeName);
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

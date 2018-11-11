/*
 * Copyright (C) 2018 The Android Open Source Project
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

import com.google.currysrc.api.process.Processor;
import com.google.currysrc.api.process.ast.BodyDeclarationLocator;
import com.google.currysrc.api.process.ast.BodyDeclarationLocatorStore;
import com.google.currysrc.api.process.ast.BodyDeclarationLocators;
import com.google.currysrc.processors.AnnotationInfo.AnnotationClass;
import com.google.currysrc.processors.AnnotationInfo.Placeholder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * Add annotations to a white list of classes and class members.
 */
public class AddAnnotation extends AbstractAddAnnotation {

  private final BodyDeclarationLocatorStore<AnnotationInfo> locator2AnnotationInfo;

  /**
   * Create a {@link Processor} that will add annotations of the supplied class to classes and class
   * members specified in the supplied file.
   *
   * <p>The supplied JSON file must consist of an outermost array containing objects with the
   * following structure:
   *
   * <pre>{@code
   * {
   *  "@location": "<body declaration location>",
   *  [<property>[, <property>]*]?
   * }
   * }</pre>
   *
   * <p>Where:
   * <ul>
   * <li>{@code <body declaration location>} is in the format expected by
   * {@link BodyDeclarationLocators#fromStringForm(String)}. This is the only required field.</li>
   * <li>{@code <property>} is a property of the annotation and is of the format
   * {@code "<name>": <value>} where {@code <name>} is the name of the annotations property which
   * must correspond to the name of a property in the supplied {@link AnnotationClass} and
   * {@code <value>} is the value that will be supplied for the property. A {@code <value>} must
   * match the type of the property in the supplied {@link AnnotationClass}.
   * </ul>
   *
   * <p>A {@code <value>} can be one of the following types:
   * <ul>
   * <li>{@code <int>} and {@code <long>} which are literal JSON numbers that are inserted into the
   * source as literal primitive values. The corresponding property type in the supplied
   * {@link AnnotationClass} must be {@code int.class} or {@code long.class} respectively.</li>
   * <li>{@code <string>} is a quoted JSON string that is inserted into the source as a literal
   * string.The corresponding property type in the supplied {@link AnnotationClass} must be
   * {@code String.class}.</li>
   * <li>{@code <placeholder>} is a quoted JSON string that is inserted into the source as if it
   * was a constant expression. It is used to reference constants in annotation values, e.g. {@code
   * dalvik.annotation.compat.UnsupportedAppUsage.VERSION_CODES.P}. It can be used for any property
   * type and will be type checked when the generated code is compiled.</li>
   * </ul>
   *
   * <p>See external/icu/tools/srcgen/unsupported-app-usage.json for an example.
   *
   * @param annotationClass the type of the annotation to add, includes class name, property names
   * and types.
   * @param file the JSON file.
   */
  public static AddAnnotation fromJsonFile(AnnotationClass annotationClass, Path file)
      throws IOException {
    Gson gson = new GsonBuilder().setLenient().create();
    BodyDeclarationLocatorStore<AnnotationInfo> annotationStore = new BodyDeclarationLocatorStore<>();
    try (JsonReader reader = gson.newJsonReader(Files.newBufferedReader(file, Charset.forName("UTF-8")))) {
      try {
        reader.beginArray();

        while (reader.hasNext()) {
          reader.beginObject();

          BodyDeclarationLocator locator = null;

          String annotationClassName = annotationClass.getName();
          Map<String, Object> annotationProperties = new LinkedHashMap<>();

          while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
              case "@location":
                locator = BodyDeclarationLocators.fromStringForm(reader.nextString());
                break;
              default:
                Class<?> propertyType = annotationClass.getPropertyType(name);
                Object value;
                JsonToken token = reader.peek();
                if (token == JsonToken.STRING) {
                  String text = reader.nextString();
                  if (propertyType != String.class) {
                    value = new Placeholder(text);
                  } else {
                    // Literal string.
                    value = text;
                  }
                } else {
                  if (propertyType == boolean.class) {
                    value = reader.nextBoolean();
                  } else if (propertyType == int.class) {
                    value = reader.nextInt();
                  } else if (propertyType == double.class) {
                    value = reader.nextDouble();
                  } else {
                    throw new IllegalStateException(
                        "Unknown property type: " + propertyType + " for " + annotationClassName);
                  }
                }

                annotationProperties.put(name, value);
            }
          }

          if (locator == null) {
            throw new IllegalStateException("Missing location");
          }
          AnnotationInfo annotationInfo = new AnnotationInfo(annotationClass, annotationProperties);
          annotationStore.add(locator, annotationInfo);

          reader.endObject();
        }

        reader.endArray();
      } catch (RuntimeException e) {
        throw new MalformedJsonException("Error parsing JSON at " + reader.getPath(), e);
      }
    }

    return new AddAnnotation(annotationStore);
  }

  public AddAnnotation(BodyDeclarationLocatorStore<AnnotationInfo> locator2AnnotationInfo) {
    this.locator2AnnotationInfo = locator2AnnotationInfo;
  }

  @Override
  protected boolean handleBodyDeclaration(ASTRewrite rewrite, BodyDeclaration node) {
    AnnotationInfo annotationInfo = locator2AnnotationInfo.find(node);
    if (annotationInfo != null) {
      insertAnnotationBefore(rewrite, node, annotationInfo);
    }
    return true;
  }
}

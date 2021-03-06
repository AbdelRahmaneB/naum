/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kordamp.naum.diff;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kordamp.naum.model.AnnotationInfo;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.kordamp.naum.diff.AnnotationDiffer.KEY_ANNOTATION_ANNOTATION_ADDED;
import static org.kordamp.naum.diff.AnnotationDiffer.KEY_ANNOTATION_ANNOTATION_REMOVED;
import static org.kordamp.naum.diff.AnnotationDiffer.KEY_ANNOTATION_ENUM_VALUE_ADDED;
import static org.kordamp.naum.diff.AnnotationDiffer.KEY_ANNOTATION_ENUM_VALUE_MODIFIED;
import static org.kordamp.naum.diff.AnnotationDiffer.KEY_ANNOTATION_ENUM_VALUE_REMOVED;
import static org.kordamp.naum.diff.AnnotationDiffer.KEY_ANNOTATION_VALUE_ADDED;
import static org.kordamp.naum.diff.AnnotationDiffer.KEY_ANNOTATION_VALUE_MODIFIED;
import static org.kordamp.naum.diff.AnnotationDiffer.KEY_ANNOTATION_VALUE_REMOVED;
import static org.kordamp.naum.diff.Diff.Severity.ERROR;
import static org.kordamp.naum.diff.Diff.Type.ADDED;
import static org.kordamp.naum.diff.Diff.Type.MODIFIED;
import static org.kordamp.naum.diff.Diff.Type.REMOVED;
import static org.kordamp.naum.diff.Diff.diff;
import static org.kordamp.naum.model.AnnotationInfo.annotationInfo;
import static org.kordamp.naum.model.AnnotationInfo.newEnumEntry;

/**
 * @author Andres Almiray
 * @author Stephan Classen
 */
@RunWith(JUnitParamsRunner.class)
public class AnnotationDifferTest extends AbstractDifferTestCase {
    private static final String ANNOTATIONNAME = "annotationName";
    private static final String VALUENAME = "valueName";
    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";
    private static final String ENUM_NAME = "amount";
    private static final String ENUM_TYPE = "Amount";
    private static final String ENUM_VALUE1 = "ONE";
    private static final String ENUM_VALUE2 = "TWO";

    @Test
    @Parameters(method = "parameters")
    @TestCaseName("{method}[{index}] - {0}")
    public void annotationDiffer(String testName, AnnotationInfo previous, AnnotationInfo next, List<Diff> expected) throws Exception {
        AnnotationDiffer differ = AnnotationDiffer.annotationDiffer(previous, next);
        Collection<Diff> actual = differ.diff();
        assertThat(actual, equalTo(expected));
    }


    private Object[] parameters() {
        return new Object[]{
            new Object[]{
                "value-added",
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build(),
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .value(VALUENAME, "")
                    .build(),
                asList(
                    diff()
                        .severity(ERROR)
                        .type(ADDED)
                        .messageKey(KEY_ANNOTATION_VALUE_ADDED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg(VALUENAME)
                        .build()
                )
            },

            new Object[]{
                "value-removed",
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .value(VALUENAME, "")
                    .build(),
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build(),
                asList(
                    diff()
                        .severity(ERROR)
                        .type(REMOVED)
                        .messageKey(KEY_ANNOTATION_VALUE_REMOVED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg(VALUENAME)
                        .build()
                )
            },

            new Object[]{
                "value-modified",
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .value(VALUENAME, VALUE1)
                    .build(),
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .value(VALUENAME, VALUE2)
                    .build(),
                asList(
                    diff()
                        .severity(ERROR)
                        .type(MODIFIED)
                        .messageKey(KEY_ANNOTATION_VALUE_MODIFIED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg(VALUENAME)
                        .messageArg(String.class.getName())
                        .messageArg(VALUE1)
                        .messageArg(String.class.getName())
                        .messageArg(VALUE2)
                        .build()
                )
            },

            new Object[]{
                "enum-value-added",
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build(),
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .enumValue(ENUM_NAME, newEnumEntry(ENUM_TYPE, ENUM_VALUE1))
                    .build(),
                asList(
                    diff()
                        .severity(ERROR)
                        .type(ADDED)
                        .messageKey(KEY_ANNOTATION_ENUM_VALUE_ADDED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg(ENUM_NAME)
                        .build()
                )
            },

            new Object[]{
                "enum-value-removed",
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .enumValue(ENUM_NAME, newEnumEntry(ENUM_TYPE, ENUM_VALUE1))
                    .build(),
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build(),
                asList(
                    diff()
                        .severity(ERROR)
                        .type(REMOVED)
                        .messageKey(KEY_ANNOTATION_ENUM_VALUE_REMOVED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg(ENUM_NAME)
                        .build()
                )
            },

            new Object[]{
                "enum-value-modified",
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .enumValue(ENUM_NAME, newEnumEntry(ENUM_TYPE, ENUM_VALUE1))
                    .build(),
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .enumValue(ENUM_NAME, newEnumEntry(ENUM_TYPE, ENUM_VALUE2))
                    .build(),
                asList(
                    diff()
                        .severity(ERROR)
                        .type(MODIFIED)
                        .messageKey(KEY_ANNOTATION_ENUM_VALUE_MODIFIED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg(ENUM_NAME)
                        .messageArg(ENUM_TYPE + "." + ENUM_VALUE1)
                        .messageArg(ENUM_TYPE + "." + ENUM_VALUE2)
                        .build()
                )
            },

            new Object[]{
                "annotations-added",
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build(),
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build()
                    .addToAnnotations(annotationInfo().name(ANNOTATION_A).build()),
                asList(
                    diff()
                        .severity(ERROR)
                        .type(ADDED)
                        .messageKey(KEY_ANNOTATION_ANNOTATION_ADDED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg("@" + ANNOTATION_A)
                        .build()
                )
            },

            new Object[]{
                "annotations-removed",
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build()
                    .addToAnnotations(annotationInfo().name(ANNOTATION_A).build()),
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build(),
                asList(
                    diff()
                        .severity(ERROR)
                        .type(REMOVED)
                        .messageKey(KEY_ANNOTATION_ANNOTATION_REMOVED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg("@" + ANNOTATION_A)
                        .build()
                )
            },

            new Object[]{
                "annotations-modified",
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build()
                    .addToAnnotations(annotationInfo().name(ANNOTATION_A).build()),
                annotationInfo()
                    .name(ANNOTATIONNAME)
                    .build()
                    .addToAnnotations(annotationInfo().name(ANNOTATION_B).build()),
                asList(
                    diff()
                        .severity(ERROR)
                        .type(REMOVED)
                        .messageKey(KEY_ANNOTATION_ANNOTATION_REMOVED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg("@" + ANNOTATION_A)
                        .build(),
                    diff()
                        .severity(ERROR)
                        .type(ADDED)
                        .messageKey(KEY_ANNOTATION_ANNOTATION_ADDED)
                        .messageArg(ANNOTATIONNAME)
                        .messageArg("@" + ANNOTATION_B)
                        .build()
                )
            }
        };
    }
}
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
package org.kordamp.naum.processor.field;

import org.junit.Test;
import org.kordamp.naum.model.AnnotationInfo;
import org.kordamp.naum.model.ClassInfo;
import org.kordamp.naum.model.ConstructorInfo;
import org.kordamp.naum.model.FieldInfo;
import org.kordamp.naum.model.Opcodes;
import org.kordamp.naum.processor.AbstractProcessorTest;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;

public class FieldsTest extends AbstractProcessorTest {
    @Test
    public void loadAndCheckPublicPrimitiveFieldsClass() throws Exception {
        loadAndCheck("org/kordamp/naum/processor/field/PublicPrimitiveFieldsClass.class", (klass) -> {
            assertThat(klass, equalTo(classInfoFor("org.kordamp.naum.processor.field.PublicPrimitiveFieldsClass", fields(Integer.TYPE, ACC_PUBLIC))));
        });
    }

    @Test
    public void loadAndCheckPublicReferenceFieldsClass() throws Exception {
        loadAndCheck("org/kordamp/naum/processor/field/PublicReferenceFieldsClass.class", (klass) -> {
            assertThat(klass, equalTo(classInfoFor("org.kordamp.naum.processor.field.PublicReferenceFieldsClass", fields(Integer.class, ACC_PUBLIC))));
        });
    }

    @Test
    public void loadAndCheckProtectedPrimitiveFieldsClass() throws Exception {
        loadAndCheck("org/kordamp/naum/processor/field/ProtectedPrimitiveFieldsClass.class", (klass) -> {
            assertThat(klass, equalTo(classInfoFor("org.kordamp.naum.processor.field.ProtectedPrimitiveFieldsClass", fields(Integer.TYPE, ACC_PROTECTED))));
        });
    }

    @Test
    public void loadAndCheckProtectedReferenceFieldsClass() throws Exception {
        loadAndCheck("org/kordamp/naum/processor/field/ProtectedReferenceFieldsClass.class", (klass) -> {
            assertThat(klass, equalTo(classInfoFor("org.kordamp.naum.processor.field.ProtectedReferenceFieldsClass", fields(Integer.class, ACC_PROTECTED))));
        });
    }

    @Test
    public void loadAndCheckPrivatePrimitiveFieldsClass() throws Exception {
        loadAndCheck("org/kordamp/naum/processor/field/PrivatePrimitiveFieldsClass.class", (klass) -> {
            assertThat(klass, equalTo(classInfoFor("org.kordamp.naum.processor.field.PrivatePrimitiveFieldsClass", fields(Integer.TYPE, ACC_PRIVATE))));
        });
    }

    @Test
    public void loadAndCheckPrivateReferenceFieldsClass() throws Exception {
        loadAndCheck("org/kordamp/naum/processor/field/PrivateReferenceFieldsClass.class", (klass) -> {
            assertThat(klass, equalTo(classInfoFor("org.kordamp.naum.processor.field.PrivateReferenceFieldsClass", fields(Integer.class, ACC_PRIVATE))));
        });
    }

    @Test
    public void loadAndCheckPackagePrimitiveFieldsClass() throws Exception {
        loadAndCheck("org/kordamp/naum/processor/field/PackagePrimitiveFieldsClass.class", (klass) -> {
            assertThat(klass, equalTo(classInfoFor("org.kordamp.naum.processor.field.PackagePrimitiveFieldsClass", fields(Integer.TYPE, 0))));
        });
    }

    @Test
    public void loadAndCheckPackageReferenceFieldsClass() throws Exception {
        loadAndCheck("org/kordamp/naum/processor/field/PackageReferenceFieldsClass.class", (klass) -> {
            assertThat(klass, equalTo(classInfoFor("org.kordamp.naum.processor.field.PackageReferenceFieldsClass", fields(Integer.class, 0))));
        });
    }

    @Test
    public void loadAndCheckFieldsWithGenerics() throws Exception {
        List<FieldInfo> fields = new ArrayList<>();
        fields.add(FieldInfo.builder()
            .name("fieldWithGenericType")
            .type("A")
            .modifiers(ACC_PUBLIC)
            .build());
        fields.add(FieldInfo.builder()
            .name("fieldWithWildcards")
            .type("java.util.Map<?, ?>")
            .modifiers(ACC_PUBLIC)
            .build());
        fields.add(FieldInfo.builder()
            .name("fieldWithGenericTypes")
            .type("java.util.Map<java.lang.String, java.lang.Number>")
            .modifiers(ACC_PUBLIC)
            .build());
        fields.add(FieldInfo.builder()
            .name("fieldWithBoundTypes")
            .type("java.util.Map<? extends java.lang.String, ? extends java.lang.Number>")
            .modifiers(ACC_PUBLIC)
            .build());

        ClassInfo classInfo = classInfoBuilderFor("org.kordamp.naum.processor.field.FieldsWithGenerics")
            .typeParameters("<A extends java.lang.Number>")
            .build();
        classInfo.addToConstructors(ConstructorInfo.builder()
            .modifiers(ACC_PUBLIC)
            .build());
        for (FieldInfo field : fields) {
            classInfo.addToFields(field);
        }

        loadAndCheck("org/kordamp/naum/processor/field/FieldsWithGenerics.class", (klass) -> {
            assertThat(klass, equalTo(classInfo));
        });
    }

    @Test
    public void loadAndCheckFieldsWithAnnotations() throws Exception {
        List<FieldInfo> fields = new ArrayList<>();
        FieldInfo field = FieldInfo.builder()
            .name("field")
            .type("int")
            .modifiers(ACC_PUBLIC)
            .build();
        field.addToAnnotations(AnnotationInfo.builder()
            .name("javax.inject.Named")
            .build());
        fields.add(field);
        field = FieldInfo.builder()
            .name("field_value")
            .type("int")
            .modifiers(ACC_PUBLIC)
            .build();
        field.addToAnnotations(AnnotationInfo.builder()
            .name("javax.inject.Named")
            .value("value", "value")
            .build());
        fields.add(field);

        ClassInfo classInfo = classInfoBuilderFor("org.kordamp.naum.processor.field.FieldsWithAnnotations").build();
        classInfo.addToConstructors(ConstructorInfo.builder()
            .modifiers(ACC_PUBLIC)
            .build());
        for (FieldInfo f : fields) {
            classInfo.addToFields(f);
        }

        loadAndCheck("org/kordamp/naum/processor/field/FieldsWithAnnotations.class", (klass) -> {
            assertThat(klass, equalTo(classInfo));
        });
    }

    private static ClassInfo.ClassInfoBuilder classInfoBuilderFor(String className) {
        return ClassInfo.builder()
            .name(className)
            .superclass(Object.class.getName())
            .version(Opcodes.V1_8)
            .modifiers(ACC_PUBLIC | ACC_SUPER);
    }

    private static ClassInfo classInfoFor(String className, List<FieldInfo> fields) {
        ClassInfo classInfo = classInfoBuilderFor(className).build();
        classInfo.addToConstructors(ConstructorInfo.builder()
            .modifiers(ACC_PUBLIC)
            .build());
        for (FieldInfo field : fields) {
            classInfo.addToFields(field);
        }
        return classInfo;
    }

    private static List<FieldInfo> fields(Class<?> type, int modifiers) {
        List<FieldInfo> fields = new ArrayList<>();
        fields.add(FieldInfo.builder()
            .name("field")
            .type(type.getName().toString())
            .modifiers(modifiers)
            .value(type.isPrimitive() ? 42 : null)
            .build());
        fields.add(FieldInfo.builder()
            .name("final_field")
            .type(type.getName().toString())
            .modifiers(modifiers | ACC_FINAL)
            .value(type.isPrimitive() ? 42 : null)
            .build());
        fields.add(FieldInfo.builder()
            .name("static_field")
            .type(type.getName().toString())
            .modifiers(modifiers | ACC_STATIC)
            .value(type.isPrimitive() ? 42 : null)
            .build());
        fields.add(FieldInfo.builder()
            .name("static_final_field")
            .type(type.getName().toString())
            .modifiers(modifiers | ACC_STATIC | ACC_FINAL)
            .value(type.isPrimitive() ? 42 : null)
            .build());
        return fields;
    }
}
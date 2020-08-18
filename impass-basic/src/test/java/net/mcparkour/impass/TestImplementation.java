/*
 * MIT License
 *
 * Copyright (c) 2019 MCParkour
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.mcparkour.impass;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public class TestImplementation {

    private void unannotatedMethod() {
        throw new MethodInvokedException();
    }

    private int primitiveField = 1;

    public int getPrimitiveField() {
        return this.primitiveField;
    }

    private String objectField = "string";

    public String getObjectField() {
        return this.objectField;
    }

    private TestImplementationTwo accessorField = new TestImplementationTwo(1);

    public TestImplementationTwo getAccessorField() {
        return this.accessorField;
    }

    @Nullable
    private TestImplementationTwo nullField;

    @Nullable
    public TestImplementationTwo getNullField() {
        return this.nullField;
    }

    public void setNullField(@Nullable final TestImplementationTwo nullField) {
        this.nullField = nullField;
    }

    private void voidVoidMethod() {
        throw new MethodInvokedException();
    }

    private int primitiveVoidMethod() {
        return 1;
    }

    private void voidPrimitiveMethod(final int integer) {
        throw new MethodInvokedException();
    }

    private int primitivePrimitiveMethod(final int integer) {
        return integer;
    }

    private String objectVoidMethod() {
        return "string";
    }

    private void voidObjectMethod(final String string) {
        throw new MethodInvokedException();
    }

    private String objectObjectMethod(final String string) {
        return string;
    }

    private TestImplementationTwo accessorVoidMethod() {
        return new TestImplementationTwo(1);
    }

    private void voidAccessorMethod(final TestImplementationTwo accessor) {
        throw new MethodInvokedException();
    }

    private TestImplementationTwo accessorAccessorMethod(final TestImplementationTwo accessor) {
        return accessor;
    }

    private void voidPrimitiveObjectMethod(final int integer, final String string) {
        throw new MethodInvokedException();
    }

    private int primitivePrimitiveObjectMethod(final int integer, final String string) {
        return integer + Integer.parseInt(string);
    }

    private String objectPrimitiveObjectMethod(final int integer, final String string) {
        return integer + " " + string;
    }

    private TestImplementationTwo accessorPrimitiveObjectAccessorMethod(final int integer, final String string, final TestImplementationTwo accessor) {
        return new TestImplementationTwo(integer + Integer.parseInt(string) + accessor.getConstructorValue());
    }

    @Nullable
    private TestImplementationTwo nullVoidMethod() {
        return null;
    }

    private void voidNullMethod(@Nullable final TestImplementationTwo accessor) {
        throw new MethodInvokedException();
    }

    @Nullable
    private TestImplementationTwo nullNullMethod(@Nullable final TestImplementationTwo accessor) {
        return accessor;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        TestImplementation that = (TestImplementation) object;
        return this.primitiveField == that.primitiveField && Objects.equals(this.objectField, that.objectField) && Objects.equals(this.accessorField, that.accessorField) && Objects.equals(this.nullField, that.nullField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.primitiveField, this.objectField, this.accessorField, this.nullField);
    }

    @Override
    public String toString() {
        return "TestImplementation{" + "primitiveField=" + this.primitiveField + ", objectField='" + this.objectField + "'" + ", accessorField=" + this.accessorField + ", nullField=" + this.nullField + "}";
    }

    private static void staticUnannotatedMethod() {
        throw new MethodInvokedException();
    }

    private static int staticPrimitiveField = 1;

    public static int getStaticPrimitiveField() {
        return staticPrimitiveField;
    }

    public static void setStaticPrimitiveField(final int staticPrimitiveField) {
        TestImplementation.staticPrimitiveField = staticPrimitiveField;
    }

    private static String staticObjectField = "string";

    public static String getStaticObjectField() {
        return staticObjectField;
    }

    public static void setStaticObjectField(final String staticObjectField) {
        TestImplementation.staticObjectField = staticObjectField;
    }

    private static TestImplementationTwo staticAccessorField = new TestImplementationTwo(1);

    public static TestImplementationTwo getStaticAccessorField() {
        return staticAccessorField;
    }

    public static void setStaticAccessorField(final TestImplementationTwo staticAccessorField) {
        TestImplementation.staticAccessorField = staticAccessorField;
    }

    @Nullable
    private static TestImplementationTwo staticNullField;

    @Nullable
    public static TestImplementationTwo getStaticNullField() {
        return staticNullField;
    }

    public static void setStaticNullField(@Nullable final TestImplementationTwo nullField) {
        staticNullField = nullField;
    }

    private static void staticVoidVoidMethod() {
        throw new MethodInvokedException();
    }

    private static int staticPrimitiveVoidMethod() {
        return 1;
    }

    private static void staticVoidPrimitiveMethod(final int integer) {
        throw new MethodInvokedException();
    }

    private static int staticPrimitivePrimitiveMethod(final int integer) {
        return integer;
    }

    private static String staticObjectVoidMethod() {
        return "string";
    }

    private static void staticVoidObjectMethod(final String string) {
        throw new MethodInvokedException();
    }

    private static String staticObjectObjectMethod(final String string) {
        return string;
    }

    private static TestImplementationTwo staticAccessorVoidMethod() {
        return new TestImplementationTwo(1);
    }

    private static void staticVoidAccessorMethod(final TestImplementationTwo accessor) {
        throw new MethodInvokedException();
    }

    private static TestImplementationTwo staticAccessorAccessorMethod(final TestImplementationTwo accessor) {
        return accessor;
    }

    private static void staticVoidPrimitiveObjectMethod(final int integer, final String string) {
        throw new MethodInvokedException();
    }

    private static int staticPrimitivePrimitiveObjectMethod(final int integer, final String string) {
        return integer + Integer.parseInt(string);
    }

    private static String staticObjectPrimitiveObjectMethod(final int integer, final String string) {
        return integer + " " + string;
    }

    private static TestImplementationTwo staticAccessorPrimitiveObjectAccessorMethod(final int integer, final String string, final TestImplementationTwo accessor) {
        return new TestImplementationTwo(integer + Integer.parseInt(string) + accessor.getConstructorValue());
    }

    @Nullable
    private static TestImplementationTwo staticNullVoidMethod() {
        return null;
    }

    private static void staticVoidNullMethod(@Nullable final TestImplementationTwo accessor) {
        throw new MethodInvokedException();
    }

    @Nullable
    private static TestImplementationTwo staticNullNullMethod(@Nullable final TestImplementationTwo accessor) {
        return accessor;
    }

    public TestImplementation() {}

    public TestImplementation(final int primitiveField) {
        this.primitiveField = primitiveField;
    }

    public TestImplementation(final String objectField) {
        this.objectField = objectField;
    }

    public TestImplementation(final TestImplementationTwo accessorField) {
        this.accessorField = accessorField;
    }

    public TestImplementation(final int primitiveField, final String objectField, final TestImplementationTwo accessorField) {
        this.primitiveField = primitiveField;
        this.objectField = objectField;
        this.accessorField = accessorField;
    }
}

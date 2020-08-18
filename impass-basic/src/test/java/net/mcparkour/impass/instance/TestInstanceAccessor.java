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

package net.mcparkour.impass.instance;

import net.mcparkour.impass.annotation.method.Getter;
import net.mcparkour.impass.annotation.method.Method;
import net.mcparkour.impass.annotation.method.Setter;
import net.mcparkour.impass.annotation.type.Type;
import org.jetbrains.annotations.Nullable;

@Type("net.mcparkour.impass.TestImplementation")
public interface TestInstanceAccessor extends InstanceAccessor {

    void unannotatedMethod();

    @Getter("primitiveField")
    int getPrimitiveField();

    @Setter("primitiveField")
    void setPrimitiveField(int integer);

    @Getter("objectField")
    String getObjectField();

    @Setter("objectField")
    void setObjectField(String string);

    @Getter("accessorField")
    TestInstanceAccessorTwo getAccessorField();

    @Setter("accessorField")
    void setAccessorField(TestInstanceAccessorTwo accessor);

    @Nullable
    @Getter("nullField")
    TestInstanceAccessorTwo getNullField();

    @Setter("nullField")
    void setNullField(@Nullable TestInstanceAccessorTwo accessor);

    @Method("voidVoidMethod")
    void voidVoidMethod();

    @Method("primitiveVoidMethod")
    int primitiveVoidMethod();

    @Method("voidPrimitiveMethod")
    void voidPrimitiveMethod(int integer);

    @Method("primitivePrimitiveMethod")
    int primitivePrimitiveMethod(int integer);

    @Method("objectVoidMethod")
    String objectVoidMethod();

    @Method("voidObjectMethod")
    void voidObjectMethod(String string);

    @Method("objectObjectMethod")
    String objectObjectMethod(String string);

    @Method("accessorVoidMethod")
    TestInstanceAccessorTwo accessorVoidMethod();

    @Method("voidAccessorMethod")
    void voidAccessorMethod(TestInstanceAccessorTwo accessor);

    @Method("accessorAccessorMethod")
    TestInstanceAccessorTwo accessorAccessorMethod(TestInstanceAccessorTwo accessor);

    @Method("voidPrimitiveObjectMethod")
    void voidPrimitiveObjectMethod(int integer, String string);

    @Method("primitivePrimitiveObjectMethod")
    int primitivePrimitiveObjectMethod(int integer, String string);

    @Method("objectPrimitiveObjectMethod")
    String objectPrimitiveObjectMethod(int integer, String string);

    @Method("accessorPrimitiveObjectAccessorMethod")
    TestInstanceAccessorTwo accessorPrimitiveObjectAccessorMethod(int integer, String string, TestInstanceAccessorTwo accessor);

    @Nullable
    @Method("nullVoidMethod")
    TestInstanceAccessorTwo nullVoidMethod();

    @Method("voidNullMethod")
    void voidNullMethod(@Nullable TestInstanceAccessorTwo accessor);

    @Nullable
    @Method("nullNullMethod")
    TestInstanceAccessorTwo nullNullMethod(@Nullable TestInstanceAccessorTwo accessor);

    default String defaultObjectObjectMethod(final String string) {
        String upperCase = toUpperCase(string);
        return objectObjectMethod(upperCase);
    }

    private String toUpperCase(final String string) {
        return string.toUpperCase();
    }
}

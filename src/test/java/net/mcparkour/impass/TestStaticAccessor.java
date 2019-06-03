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

import net.mcparkour.impass.annotation.ImpassClass;
import net.mcparkour.impass.annotation.ImpassConstructor;
import net.mcparkour.impass.annotation.ImpassGetter;
import net.mcparkour.impass.annotation.ImpassMethod;
import net.mcparkour.impass.annotation.ImpassSetter;
import org.jetbrains.annotations.Nullable;

@ImpassClass("net.mcparkour.impass.TestImplementation")
public interface TestStaticAccessor extends StaticAccessor {

	void staticUnannotatedMethod();

	@ImpassGetter("staticPrimitiveField")
	int getStaticPrimitiveField();

	@ImpassSetter("staticPrimitiveField")
	void setStaticPrimitiveField(int integer);

	@ImpassGetter("staticObjectField")
	String getStaticObjectField();

	@ImpassSetter("staticObjectField")
	void setStaticObjectField(String string);

	@ImpassGetter("staticAccessorField")
	TestAccessorTwo getStaticAccessorField();

	@ImpassSetter("staticAccessorField")
	void setStaticAccessorField(TestAccessorTwo accessor);

	@Nullable
	@ImpassGetter("staticNullField")
	TestAccessorTwo getStaticNullField();

	@ImpassSetter("staticNullField")
	void setStaticNullField(@Nullable TestAccessorTwo accessor);

	@ImpassMethod("staticVoidVoidMethod")
	void staticVoidVoidMethod();

	@ImpassMethod("staticPrimitiveVoidMethod")
	int staticPrimitiveVoidMethod();

	@ImpassMethod("staticVoidPrimitiveMethod")
	void staticVoidPrimitiveMethod(int integer);

	@ImpassMethod("staticPrimitivePrimitiveMethod")
	int staticPrimitivePrimitiveMethod(int integer);

	@ImpassMethod("staticObjectVoidMethod")
	String staticObjectVoidMethod();

	@ImpassMethod("staticVoidObjectMethod")
	void staticVoidObjectMethod(String string);

	@ImpassMethod("staticObjectObjectMethod")
	String staticObjectObjectMethod(String string);

	@ImpassMethod("staticAccessorVoidMethod")
	TestAccessorTwo staticAccessorVoidMethod();

	@ImpassMethod("staticVoidAccessorMethod")
	void staticVoidAccessorMethod(TestAccessorTwo accessor);

	@ImpassMethod("staticAccessorAccessorMethod")
	TestAccessorTwo staticAccessorAccessorMethod(TestAccessorTwo accessor);

	@ImpassMethod("staticVoidPrimitiveObjectMethod")
	void staticVoidPrimitiveObjectMethod(int integer, String string);

	@ImpassMethod("staticPrimitivePrimitiveObjectMethod")
	int staticPrimitivePrimitiveObjectMethod(int integer, String string);

	@ImpassMethod("staticObjectPrimitiveObjectMethod")
	String staticObjectPrimitiveObjectMethod(int integer, String string);

	@ImpassMethod("staticAccessorPrimitiveObjectAccessorMethod")
	TestAccessorTwo staticAccessorPrimitiveObjectAccessorMethod(int integer, String string, TestAccessorTwo accessor);

	@Nullable
	@ImpassMethod("staticNullVoidMethod")
	TestAccessorTwo staticNullVoidMethod();

	@ImpassMethod("staticVoidNullMethod")
	void staticVoidNullMethod(@Nullable TestAccessorTwo accessor);

	@Nullable
	@ImpassMethod("staticNullNullMethod")
	TestAccessorTwo staticNullNullMethod(@Nullable TestAccessorTwo accessor);

	@ImpassConstructor
	TestAccessor construct();

	@ImpassConstructor
	TestAccessor construct(int primitive);

	@ImpassConstructor
	TestAccessor construct(String object);

	@ImpassConstructor
	TestAccessor construct(TestAccessorTwo accessor);

	@ImpassConstructor
	TestAccessor construct(int primitive, String object, TestAccessorTwo accessor);
}

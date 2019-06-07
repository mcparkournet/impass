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

package net.mcparkour.impass.type;

import net.mcparkour.impass.annotation.method.Constructor;
import net.mcparkour.impass.annotation.method.Getter;
import net.mcparkour.impass.annotation.method.Method;
import net.mcparkour.impass.annotation.method.Setter;
import net.mcparkour.impass.annotation.type.Type;
import net.mcparkour.impass.instance.TestInstanceAccessor;
import net.mcparkour.impass.instance.TestInstanceAccessorTwo;
import org.jetbrains.annotations.Nullable;

@Type("net.mcparkour.impass.TestImplementation")
public interface TestTypeAccessor extends TypeAccessor {

	void staticUnannotatedMethod();

	@Getter("staticPrimitiveField")
	int getStaticPrimitiveField();

	@Setter("staticPrimitiveField")
	void setStaticPrimitiveField(int integer);

	@Getter("staticObjectField")
	String getStaticObjectField();

	@Setter("staticObjectField")
	void setStaticObjectField(String string);

	@Getter("staticAccessorField")
	TestInstanceAccessorTwo getStaticAccessorField();

	@Setter("staticAccessorField")
	void setStaticAccessorField(TestInstanceAccessorTwo accessor);

	@Nullable
	@Getter("staticNullField")
	TestInstanceAccessorTwo getStaticNullField();

	@Setter("staticNullField")
	void setStaticNullField(@Nullable TestInstanceAccessorTwo accessor);

	@Method("staticVoidVoidMethod")
	void staticVoidVoidMethod();

	@Method("staticPrimitiveVoidMethod")
	int staticPrimitiveVoidMethod();

	@Method("staticVoidPrimitiveMethod")
	void staticVoidPrimitiveMethod(int integer);

	@Method("staticPrimitivePrimitiveMethod")
	int staticPrimitivePrimitiveMethod(int integer);

	@Method("staticObjectVoidMethod")
	String staticObjectVoidMethod();

	@Method("staticVoidObjectMethod")
	void staticVoidObjectMethod(String string);

	@Method("staticObjectObjectMethod")
	String staticObjectObjectMethod(String string);

	@Method("staticAccessorVoidMethod")
	TestInstanceAccessorTwo staticAccessorVoidMethod();

	@Method("staticVoidAccessorMethod")
	void staticVoidAccessorMethod(TestInstanceAccessorTwo accessor);

	@Method("staticAccessorAccessorMethod")
	TestInstanceAccessorTwo staticAccessorAccessorMethod(TestInstanceAccessorTwo accessor);

	@Method("staticVoidPrimitiveObjectMethod")
	void staticVoidPrimitiveObjectMethod(int integer, String string);

	@Method("staticPrimitivePrimitiveObjectMethod")
	int staticPrimitivePrimitiveObjectMethod(int integer, String string);

	@Method("staticObjectPrimitiveObjectMethod")
	String staticObjectPrimitiveObjectMethod(int integer, String string);

	@Method("staticAccessorPrimitiveObjectAccessorMethod")
	TestInstanceAccessorTwo staticAccessorPrimitiveObjectAccessorMethod(int integer, String string, TestInstanceAccessorTwo accessor);

	@Nullable
	@Method("staticNullVoidMethod")
	TestInstanceAccessorTwo staticNullVoidMethod();

	@Method("staticVoidNullMethod")
	void staticVoidNullMethod(@Nullable TestInstanceAccessorTwo accessor);

	@Nullable
	@Method("staticNullNullMethod")
	TestInstanceAccessorTwo staticNullNullMethod(@Nullable TestInstanceAccessorTwo accessor);

	@Constructor
	TestInstanceAccessor construct();

	@Constructor
	TestInstanceAccessor construct(int primitive);

	@Constructor
	TestInstanceAccessor construct(String object);

	@Constructor
	TestInstanceAccessor construct(TestInstanceAccessorTwo accessor);

	@Constructor
	TestInstanceAccessor construct(int primitive, String object, TestInstanceAccessorTwo accessor);
}

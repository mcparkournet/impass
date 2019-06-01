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

import net.mcparkour.impass.annotation.ImpassGetter;
import net.mcparkour.impass.annotation.ImpassMethod;
import net.mcparkour.impass.annotation.ImpassSetter;
import org.jetbrains.annotations.Nullable;

public interface TestAccessor extends Accessor {

	void unannotatedMethod();

	@ImpassGetter("primitiveField")
	int getPrimitiveField();

	@ImpassSetter("primitiveField")
	void setPrimitiveField(int integer);

	@ImpassGetter("objectField")
	String getObjectField();

	@ImpassSetter("objectField")
	void setObjectField(String string);

	@ImpassGetter("accessorField")
	TestAccessorTwo getAccessorField();

	@ImpassSetter("accessorField")
	void setAccessorField(TestAccessorTwo accessor);

	@Nullable
	@ImpassGetter("nullField")
	TestAccessorTwo getNullField();

	@ImpassSetter("nullField")
	void setNullField(@Nullable TestAccessorTwo accessor);

	@ImpassMethod("voidVoidMethod")
	void voidVoidMethod();

	@ImpassMethod("primitiveVoidMethod")
	int primitiveVoidMethod();

	@ImpassMethod("voidPrimitiveMethod")
	void voidPrimitiveMethod(int integer);

	@ImpassMethod("primitivePrimitiveMethod")
	int primitivePrimitiveMethod(int integer);

	@ImpassMethod("objectVoidMethod")
	String objectVoidMethod();

	@ImpassMethod("voidObjectMethod")
	void voidObjectMethod(String string);

	@ImpassMethod("objectObjectMethod")
	String objectObjectMethod(String string);

	@ImpassMethod("accessorVoidMethod")
	TestAccessorTwo accessorVoidMethod();

	@ImpassMethod("voidAccessorMethod")
	void voidAccessorMethod(TestAccessorTwo accessor);

	@ImpassMethod("accessorAccessorMethod")
	TestAccessorTwo accessorAccessorMethod(TestAccessorTwo accessor);

	@ImpassMethod("voidPrimitiveObjectMethod")
	void voidPrimitiveObjectMethod(int integer, String string);

	@ImpassMethod("primitivePrimitiveObjectMethod")
	int primitivePrimitiveObjectMethod(int integer, String string);

	@ImpassMethod("objectPrimitiveObjectMethod")
	String objectPrimitiveObjectMethod(int integer, String string);

	@ImpassMethod("accessorPrimitiveObjectAccessorMethod")
	TestAccessorTwo accessorPrimitiveObjectAccessorMethod(int integer, String string, TestAccessorTwo accessor);

	@Nullable
	@ImpassMethod("nullVoidMethod")
	TestAccessorTwo nullVoidMethod();

	@ImpassMethod("voidNullMethod")
	void voidNullMethod(@Nullable TestAccessorTwo accessor);

	@Nullable
	@ImpassMethod("nullNullMethod")
	TestAccessorTwo nullNullMethod(@Nullable TestAccessorTwo accessor);
}

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

	@ImpassMethod("nothing")
	void nothing();

	@ImpassMethod("returnFoo")
	String returnFoo();

	@ImpassMethod("acceptFoo")
	void acceptFoo(String foo);

	@ImpassMethod("acceptAndReturn")
	String acceptAndReturn(String foo);

	@ImpassGetter("intField")
	int getIntField();

	@ImpassSetter("intField")
	void setIntField(int intFieldValue);

	@ImpassGetter("stringField")
	String getStringField();

	@ImpassSetter("stringField")
	void setStringField(String stringFieldValue);

	@ImpassMethod("annotatedMethodI")
	String annotatedMethod(String s, int i);

	@ImpassMethod("annotatedMethod2I")
	int annotatedMethod2(int i, String s);

	@ImpassMethod("testImplementation2")
	TestAccessor2 testAccessor2();

	@ImpassMethod("testImplementation22")
	String testAccessor22(TestAccessor2 testAccessor21, TestAccessor2 testAccessor22, String string);

	@ImpassGetter("implField")
	TestAccessor2 getAccessorField();

	@ImpassSetter("implField")
	void setAccessorField(TestAccessor2 testAccessor2);

	@Nullable
	@ImpassMethod("nullMethod")
	TestAccessor2 nullMethod();

	@Nullable
	@ImpassGetter("nullField")
	TestAccessor2 getNullField();

	void unannotatedMethod();
}

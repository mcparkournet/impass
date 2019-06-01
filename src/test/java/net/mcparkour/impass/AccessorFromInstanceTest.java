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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccessorFromInstanceTest {

	private Impass impass;
	private TestImplementation implementation;
	private TestAccessor accessor;

	@BeforeEach
	public void setUp() {
		this.impass = new Impass();
		this.implementation = new TestImplementation();
		this.accessor = this.impass.createAccessor(TestAccessor.class, this.implementation);
	}

	@Test
	public void testVoidVoidMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, this.accessor::nothing);
	}

	@Test
	public void testObjectVoidMethodAccess() {
		Assertions.assertEquals("foo", this.accessor.returnFoo());
	}

	@Test
	public void testVoidObjectMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.acceptFoo("foo"));
	}

	@Test
	public void testObjectObjectMethodAccess() {
		Assertions.assertEquals("foo", this.accessor.acceptAndReturn("foo"));
	}

	@Test
	public void testPrimitiveFieldAccess() {
		Assertions.assertEquals(1, this.accessor.getIntField());
		this.accessor.setIntField(2);
		Assertions.assertEquals(2, this.implementation.intField());
	}

	@Test
	public void testObjectFieldAccess() {
		Assertions.assertEquals("string", this.accessor.getStringField());
		this.accessor.setStringField("string2");
		Assertions.assertEquals("string2", this.implementation.stringField());
	}

	@Test
	public void testAnnotatedMethodAccess() {
		Assertions.assertEquals("string 1", this.accessor.annotatedMethod("string", 1));
		Assertions.assertEquals(2, this.accessor.annotatedMethod2(1, "1"));
	}

	@Test
	public void testGetInstance() {
		Object implementation = this.accessor.getImplementation();
		Assertions.assertSame(this.implementation, implementation);
	}

	@Test
	public void testAccessorReturnTypeInMethod() {
		TestAccessor2 testAccessor2 = this.accessor.testAccessor2();
		String string2 = testAccessor2.returnString2();
		Assertions.assertEquals("string2", string2);
	}

	@Test
	public void testAccessorParameterTypesInMethod() {
		TestAccessor2 accessor1 = createTestAccessor2(1);
		TestAccessor2 accessor2 = createTestAccessor2(2);
		String string = this.accessor.testAccessor22(accessor1, accessor2, "string");
		Assertions.assertEquals("string1 string2 string 1 2", string);
	}

	@Test
	public void testAccessorFieldAccess() {
		TestAccessor2 accessorField = this.accessor.getAccessorField();
		Assertions.assertEquals(1, accessorField.returnI());
		TestAccessor2 accessor = createTestAccessor2(2);
		this.accessor.setAccessorField(accessor);
		Assertions.assertEquals(2, this.implementation.implField()
			.iField());
	}

	@Test
	public void testNullMethodAccess() {
		Assertions.assertNull(this.accessor.nullMethod());
	}

	@Test
	public void testNullFieldAccess() {
		Assertions.assertNull(this.accessor.getNullField());
	}

	@Test
	public void testUnannotatedMethod() {
		Assertions.assertThrows(AccessorHandlerException.class, () -> this.accessor.unannotatedMethod());
	}

	private TestAccessor2 createTestAccessor2(int i) {
		TestImplementation2 implementation = new TestImplementation2(i);
		return this.impass.createAccessor(TestAccessor2.class, implementation);
	}
}

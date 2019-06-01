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
	public void testGetAccessorImplementation() {
		Object implementation = this.accessor.getImplementation();
		Assertions.assertSame(this.implementation, implementation);
	}

	@Test
	public void testAccessorEquals() {
		Assertions.assertEquals(this.accessor, this.accessor);
		Assertions.assertEquals(this.accessor, this.implementation);
	}

	@Test
	public void testAccessorHashCode() {
		Assertions.assertEquals(this.implementation.hashCode(), this.accessor.hashCode());
	}

	@Test
	public void testAccessorToString() {
		Assertions.assertEquals(this.implementation.toString(), this.accessor.toString());
	}

	@Test
	public void testUnannotatedMethodAccess() {
		Assertions.assertThrows(AccessorHandlerException.class, () -> this.accessor.unannotatedMethod());
	}

	@Test
	public void testPrimitiveFieldGetterAccess() {
		Assertions.assertEquals(1, this.accessor.getPrimitiveField());
	}

	@Test
	public void testPrimitiveFieldSetterAccess() {
		this.accessor.setPrimitiveField(2);
		Assertions.assertEquals(2, this.implementation.getPrimitiveField());
	}

	@Test
	public void testObjectFieldGetterAccess() {
		Assertions.assertEquals("string", this.accessor.getObjectField());
	}

	@Test
	public void testObjectFieldSetterAccess() {
		this.accessor.setObjectField("");
		Assertions.assertEquals("", this.implementation.getObjectField());
	}

	@Test
	public void testAccessorFieldGetterAccess() {
		TestAccessorTwo accessor = this.accessor.getAccessorField();
		Assertions.assertEquals(1, accessor.getConstructorValue());
	}

	@Test
	public void testAccessorFieldSetterAccess() {
		TestAccessorTwo accessor = createTestAccessorTwo(2);
		this.accessor.setAccessorField(accessor);
		TestImplementationTwo accessorField = this.implementation.getAccessorField();
		Assertions.assertEquals(2, accessorField.getConstructorValue());
	}

	@Test
	public void testNullFieldGetterAccess() {
		Assertions.assertNull(this.accessor.getNullField());
	}

	@Test
	public void testNullFieldSetterAccess() {
		this.implementation.setNullField(new TestImplementationTwo(1));
		this.accessor.setNullField(null);
		Assertions.assertNull(this.implementation.getNullField());
	}

	@Test
	public void testVoidVoidMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, this.accessor::voidVoidMethod);
	}

	@Test
	public void testPrimitiveVoidMethodAccess() {
		Assertions.assertEquals(1, this.accessor.primitiveVoidMethod());
	}

	@Test
	public void testVoidPrimitiveMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.voidPrimitiveMethod(1));
	}

	@Test
	public void testPrimitivePrimitiveMethodAccess() {
		Assertions.assertEquals(1, this.accessor.primitivePrimitiveMethod(1));
	}

	@Test
	public void testObjectVoidMethodAccess() {
		Assertions.assertEquals("string", this.accessor.objectVoidMethod());
	}

	@Test
	public void testVoidObjectMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.voidObjectMethod("string"));
	}

	@Test
	public void testObjectObjectMethodAccess() {
		Assertions.assertEquals("string", this.accessor.objectObjectMethod("string"));
	}

	@Test
	public void testAccessorVoidMethodAccess() {
		TestAccessorTwo testAccessorTwo = this.accessor.accessorVoidMethod();
		Assertions.assertEquals(1, testAccessorTwo.getConstructorValue());
	}

	@Test
	public void testVoidAccessorMethodAccess() {
		TestAccessorTwo testAccessorTwo = createTestAccessorTwo(1);
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.voidAccessorMethod(testAccessorTwo));
	}

	@Test
	public void testAccessorAccessorMethodAccess() {
		TestAccessorTwo testAccessorTwoExpected = createTestAccessorTwo(1);
		TestAccessorTwo testAccessorTwo = this.accessor.accessorAccessorMethod(testAccessorTwoExpected);
		Assertions.assertSame(1, testAccessorTwo.getConstructorValue());
	}

	@Test
	public void testVoidPrimitiveObjectMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.voidPrimitiveObjectMethod(1, "string"));
	}

	@Test
	public void testPrimitivePrimitiveObjectMethodAccess() {
		Assertions.assertEquals(2, this.accessor.primitivePrimitiveObjectMethod(1, "1"));
	}

	@Test
	public void testObjectPrimitiveObjectMethodAccess() {
		Assertions.assertEquals("1 string", this.accessor.objectPrimitiveObjectMethod(1, "string"));
	}

	@Test
	public void testAccessorPrimitiveObjectAccessorMethodAccess() {
		TestAccessorTwo accessor = createTestAccessorTwo(1);
		TestAccessorTwo testAccessorTwo = this.accessor.accessorPrimitiveObjectAccessorMethod(1, "1", accessor);
		Assertions.assertEquals(3, testAccessorTwo.getConstructorValue());
	}

	@Test
	public void testNullVoidMethodAccess() {
		TestAccessorTwo testAccessorTwo = this.accessor.nullVoidMethod();
		Assertions.assertNull(testAccessorTwo);
	}

	@Test
	public void testVoidNullMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.voidNullMethod(null));
	}

	@Test
	public void testNullNullMethodAccess() {
		TestAccessorTwo testAccessorTwo = this.accessor.nullNullMethod(null);
		Assertions.assertNull(testAccessorTwo);
	}

	private TestAccessorTwo createTestAccessorTwo(int constructorValue) {
		TestImplementationTwo implementation = new TestImplementationTwo(constructorValue);
		return this.impass.createAccessor(TestAccessorTwo.class, implementation);
	}
}

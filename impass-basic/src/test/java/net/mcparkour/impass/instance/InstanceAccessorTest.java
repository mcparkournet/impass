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

import net.mcparkour.impass.BasicAccessorFactory;
import net.mcparkour.impass.MethodInvokedException;
import net.mcparkour.impass.TestImplementation;
import net.mcparkour.impass.TestImplementationTwo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InstanceAccessorTest {

	private BasicAccessorFactory accessorFactory;
	private TestImplementation implementation;
	private TestInstanceAccessor accessor;

	@BeforeEach
	public void setUp() {
		this.accessorFactory = new BasicAccessorFactory();
		this.implementation = new TestImplementation();
		this.accessor = this.accessorFactory.createInstanceAccessor(TestInstanceAccessor.class, this.implementation);
	}

	@Test
	public void testGetAccessorImplementation() {
		var instance = this.accessor.getInstance();
		Assertions.assertSame(this.implementation, instance);
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
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.unannotatedMethod());
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
		var accessor = this.accessor.getAccessorField();
		Assertions.assertEquals(1, accessor.getConstructorValue());
	}

	@Test
	public void testAccessorFieldSetterAccess() {
		var accessor = createAccessorTwo(2);
		this.accessor.setAccessorField(accessor);
		var implementation = this.implementation.getAccessorField();
		Assertions.assertEquals(2, implementation.getConstructorValue());
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
		var accessor = this.accessor.accessorVoidMethod();
		Assertions.assertEquals(1, accessor.getConstructorValue());
	}

	@Test
	public void testVoidAccessorMethodAccess() {
		var accessor = createAccessorTwo(1);
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.voidAccessorMethod(accessor));
	}

	@Test
	public void testAccessorAccessorMethodAccess() {
		var accessorExpected = createAccessorTwo(1);
		var accessor = this.accessor.accessorAccessorMethod(accessorExpected);
		Assertions.assertSame(1, accessor.getConstructorValue());
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
		var accessor = createAccessorTwo(1);
		var outputAccessor = this.accessor.accessorPrimitiveObjectAccessorMethod(1, "1", accessor);
		Assertions.assertEquals(3, outputAccessor.getConstructorValue());
	}

	@Test
	public void testNullVoidMethodAccess() {
		var accessor = this.accessor.nullVoidMethod();
		Assertions.assertNull(accessor);
	}

	@Test
	public void testVoidNullMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.voidNullMethod(null));
	}

	@Test
	public void testNullNullMethodAccess() {
		var accessor = this.accessor.nullNullMethod(null);
		Assertions.assertNull(accessor);
	}

	private TestInstanceAccessorTwo createAccessorTwo(int constructorValue) {
		var implementation = new TestImplementationTwo(constructorValue);
		return this.accessorFactory.createInstanceAccessor(TestInstanceAccessorTwo.class, implementation);
	}

	@Test
	public void testDefaultObjectObjectMethodCall() {
		Assertions.assertEquals("TEST", this.accessor.defaultObjectObjectMethod("test"));
	}
}

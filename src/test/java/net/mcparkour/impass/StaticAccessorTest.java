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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StaticAccessorTest {

	private Impass impass;
	private TestStaticAccessor accessor;

	@BeforeEach
	public void setUp() {
		this.impass = new Impass();
		this.accessor = this.impass.createStaticAccessor(TestStaticAccessor.class);
	}

	@AfterEach
	public void tearDown() {
		TestImplementation.setStaticPrimitiveField(1);
		TestImplementation.setStaticObjectField("string");
		TestImplementation.setStaticAccessorField(new TestImplementationTwo(1));
		TestImplementation.setStaticNullField(null);
	}

	@Test
	public void testStaticUnannotatedMethodAccess() {
		Assertions.assertThrows(AccessorHandlerException.class, () -> this.accessor.staticUnannotatedMethod());
	}

	@Test
	public void testStaticPrimitiveFieldGetterAccess() {
		Assertions.assertEquals(1, this.accessor.getStaticPrimitiveField());
	}

	@Test
	public void testStaticPrimitiveFieldSetterAccess() {
		this.accessor.setStaticPrimitiveField(2);
		Assertions.assertEquals(2, TestImplementation.getStaticPrimitiveField());
	}

	@Test
	public void testStaticObjectFieldGetterAccess() {
		Assertions.assertEquals("string", this.accessor.getStaticObjectField());
	}

	@Test
	public void testStaticObjectFieldSetterAccess() {
		this.accessor.setStaticObjectField("");
		Assertions.assertEquals("", TestImplementation.getStaticObjectField());
	}

	@Test
	public void testStaticAccessorFieldGetterAccess() {
		TestAccessorTwo accessor = this.accessor.getStaticAccessorField();
		Assertions.assertEquals(1, accessor.getConstructorValue());
	}

	@Test
	public void testStaticAccessorFieldSetterAccess() {
		TestAccessorTwo accessor = createTestAccessorTwo(2);
		this.accessor.setStaticAccessorField(accessor);
		TestImplementationTwo accessorField = TestImplementation.getStaticAccessorField();
		Assertions.assertEquals(2, accessorField.getConstructorValue());
	}

	@Test
	public void testStaticNullFieldGetterAccess() {
		Assertions.assertNull(this.accessor.getStaticNullField());
	}

	@Test
	public void testStaticNullFieldSetterAccess() {
		TestImplementation.setStaticNullField(new TestImplementationTwo(1));
		this.accessor.setStaticNullField(null);
		Assertions.assertNull(TestImplementation.getStaticNullField());
	}

	@Test
	public void testStaticVoidVoidMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, this.accessor::staticVoidVoidMethod);
	}

	@Test
	public void testStaticPrimitiveVoidMethodAccess() {
		Assertions.assertEquals(1, this.accessor.staticPrimitiveVoidMethod());
	}

	@Test
	public void testStaticVoidPrimitiveMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.staticVoidPrimitiveMethod(1));
	}

	@Test
	public void testStaticPrimitivePrimitiveMethodAccess() {
		Assertions.assertEquals(1, this.accessor.staticPrimitivePrimitiveMethod(1));
	}

	@Test
	public void testStaticObjectVoidMethodAccess() {
		Assertions.assertEquals("string", this.accessor.staticObjectVoidMethod());
	}

	@Test
	public void testStaticVoidObjectMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.staticVoidObjectMethod("string"));
	}

	@Test
	public void testStaticObjectObjectMethodAccess() {
		Assertions.assertEquals("string", this.accessor.staticObjectObjectMethod("string"));
	}

	@Test
	public void testStaticAccessorVoidMethodAccess() {
		TestAccessorTwo testAccessorTwo = this.accessor.staticAccessorVoidMethod();
		Assertions.assertEquals(1, testAccessorTwo.getConstructorValue());
	}

	@Test
	public void testStaticVoidAccessorMethodAccess() {
		TestAccessorTwo testAccessorTwo = createTestAccessorTwo(1);
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.staticVoidAccessorMethod(testAccessorTwo));
	}

	@Test
	public void testStaticAccessorAccessorMethodAccess() {
		TestAccessorTwo testAccessorTwoExpected = createTestAccessorTwo(1);
		TestAccessorTwo testAccessorTwo = this.accessor.staticAccessorAccessorMethod(testAccessorTwoExpected);
		Assertions.assertSame(1, testAccessorTwo.getConstructorValue());
	}

	@Test
	public void testStaticVoidPrimitiveObjectMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.staticVoidPrimitiveObjectMethod(1, "string"));
	}

	@Test
	public void testStaticPrimitivePrimitiveObjectMethodAccess() {
		Assertions.assertEquals(2, this.accessor.staticPrimitivePrimitiveObjectMethod(1, "1"));
	}

	@Test
	public void testStaticObjectPrimitiveObjectMethodAccess() {
		Assertions.assertEquals("1 string", this.accessor.staticObjectPrimitiveObjectMethod(1, "string"));
	}

	@Test
	public void testStaticAccessorPrimitiveObjectAccessorMethodAccess() {
		TestAccessorTwo accessor = createTestAccessorTwo(1);
		TestAccessorTwo testAccessorTwo = this.accessor.staticAccessorPrimitiveObjectAccessorMethod(1, "1", accessor);
		Assertions.assertEquals(3, testAccessorTwo.getConstructorValue());
	}

	@Test
	public void testStaticNullVoidMethodAccess() {
		TestAccessorTwo testAccessorTwo = this.accessor.staticNullVoidMethod();
		Assertions.assertNull(testAccessorTwo);
	}

	@Test
	public void testStaticVoidNullMethodAccess() {
		Assertions.assertThrows(MethodInvokedException.class, () -> this.accessor.staticVoidNullMethod(null));
	}

	@Test
	public void testStaticNullNullMethodAccess() {
		TestAccessorTwo testAccessorTwo = this.accessor.staticNullNullMethod(null);
		Assertions.assertNull(testAccessorTwo);
	}

	@Test
	public void testStaticVoidConstructorAccess() {
		TestAccessor accessor = this.accessor.construct();
		Assertions.assertTrue(areEqual(accessor, new TestImplementation()));
	}

	@Test
	public void testStaticPrimitiveConstructorAccess() {
		TestAccessor accessor = this.accessor.construct(2);
		Assertions.assertTrue(areEqual(accessor, new TestImplementation(2)));
	}

	@Test
	public void testStaticObjectConstructorAccess() {
		TestAccessor accessor = this.accessor.construct("");
		Assertions.assertTrue(areEqual(accessor, new TestImplementation("")));
	}

	@Test
	public void testStaticAccessorConstructorAccess() {
		TestAccessorTwo testAccessorTwo = createTestAccessorTwo(2);
		TestAccessor accessor = this.accessor.construct(testAccessorTwo);
		Assertions.assertTrue(areEqual(accessor, new TestImplementation(new TestImplementationTwo(2))));
	}

	@Test
	public void testStaticPrimitiveObjectAccessorConstructorAccess() {
		TestAccessorTwo testAccessorTwo = createTestAccessorTwo(2);
		TestAccessor accessor = this.accessor.construct(2, "", testAccessorTwo);
		Assertions.assertTrue(areEqual(accessor, new TestImplementation(2, "", new TestImplementationTwo(2))));
	}

	private boolean areEqual(TestAccessor accessor, TestImplementation implementation) {
		Object accessorImplementation = accessor.getImplementation();
		TestImplementation testImplementation = (TestImplementation) accessorImplementation;
		return implementation.equals(testImplementation);
	}

	private TestAccessorTwo createTestAccessorTwo(int constructorValue) {
		TestImplementationTwo implementation = new TestImplementationTwo(constructorValue);
		return this.impass.createAccessor(TestAccessorTwo.class, implementation);
	}
}

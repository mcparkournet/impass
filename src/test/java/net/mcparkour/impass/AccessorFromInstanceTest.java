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

	private TestImplementation implementation;
	private TestAccessor accessor;

	@BeforeEach
	public void setUp() {
		Assertions.assertDoesNotThrow(() -> {
			Impass impass = new Impass();
			this.implementation = new TestImplementation();
			this.accessor = impass.createImplementationAccessor(TestAccessor.class, this.implementation);
		});
	}

	@Test
	public void testMethodAccess() {
		Assertions.assertDoesNotThrow(this.accessor::nothing);
		Assertions.assertEquals("foo", this.accessor.returnFoo());
		Assertions.assertDoesNotThrow(() -> this.accessor.acceptFoo("foo"));
		Assertions.assertEquals("foo", this.accessor.acceptAndReturn("foo"));
		Assertions.assertEquals("foo 1", this.accessor.acceptMultiParamAndReturn("foo", 1));
	}

	@Test
	public void testPrimitiveFieldAccess() {
		Assertions.assertEquals(1, this.accessor.getIntField());
		Assertions.assertDoesNotThrow(() -> this.accessor.setIntField(2));
		Assertions.assertEquals(2, this.implementation.intField());
	}

	@Test
	public void testObjectFieldAccess() {
		Assertions.assertEquals("string", this.accessor.getStringField());
		Assertions.assertDoesNotThrow(() -> this.accessor.setStringField("string2"));
		Assertions.assertEquals("string2", this.implementation.stringField());
	}

	@Test
	public void testAnnotatedMethodAccess() {
		Assertions.assertEquals("string 1", this.accessor.annotatedMethod("string", 1));
		Assertions.assertEquals(2, this.accessor.annotatedMethod2(1, "1"));
	}
}

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

package net.mcparkour.impass.instance.inheritance;

import net.mcparkour.impass.AccessorFactory;
import net.mcparkour.impass.BasicAccessorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InheritanceTest {

	private AccessorFactory accessorFactory;

	@BeforeEach
	public void setUp() {
		this.accessorFactory = new BasicAccessorFactory();
	}

	@Test
	public void testSuperclassMethodAccess() {
		SubclassAccessor subclassAccessor = this.accessorFactory.createInstanceAccessor(SubclassAccessor.class, new Subclass());
		Assertions.assertEquals(1, subclassAccessor.superclassMethod());
	}

	@Test
	public void testSuperclassParameterMethodAccess() {
		SubclassAccessor subclassRoot = this.accessorFactory.createInstanceAccessor(SubclassAccessor.class, new Subclass());
		SuperclassAccessor superclass = this.accessorFactory.createInstanceAccessor(SuperclassAccessor.class, new Superclass());
		SubclassAccessor subclass = this.accessorFactory.createInstanceAccessor(SubclassAccessor.class, new Subclass());
		Assertions.assertEquals(1, subclassRoot.accept(superclass));
		Assertions.assertEquals(2, subclassRoot.accept(subclass));
	}
}

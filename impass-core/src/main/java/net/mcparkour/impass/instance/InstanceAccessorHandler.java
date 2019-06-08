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

import java.lang.reflect.Method;
import net.mcparkour.impass.AccessorFactory;
import net.mcparkour.impass.AccessorHandler;
import net.mcparkour.impass.handler.method.MethodHandler;
import net.mcparkour.impass.handler.registry.method.MethodAnnotationHandlerRegistry;
import net.mcparkour.impass.handler.registry.type.TypeAnnotationHandlerRegistry;
import net.mcparkour.impass.util.reflection.Reflections;
import org.jetbrains.annotations.Nullable;

public class InstanceAccessorHandler extends AccessorHandler {

	private static final Method GET_INSTANCE_METHOD = Reflections.getMethod(InstanceAccessor.class, "getInstance");
	private static final Method EQUALS_METHOD = Reflections.getMethod(Object.class, "equals", Object.class);
	private static final Method HASH_CODE_METHOD = Reflections.getMethod(Object.class, "hashCode");
	private static final Method TO_STRING_METHOD = Reflections.getMethod(Object.class, "toString");

	private Object instance;

	public InstanceAccessorHandler(AccessorFactory accessorFactory, TypeAnnotationHandlerRegistry typeHandlerRegistry, MethodAnnotationHandlerRegistry methodHandlerRegistry, Object instance) {
		super(accessorFactory, typeHandlerRegistry, methodHandlerRegistry, new InstanceReflectionOperations(instance));
		this.instance = instance;
	}

	@Override
	@Nullable
	public Object handle(MethodHandler handler) throws Throwable {
		if (handler.isInvoked(GET_INSTANCE_METHOD)) {
			return this.instance;
		}
		if (handler.isInvoked(EQUALS_METHOD)) {
			handler.remapParameters();
			var firstParameter = handler.getFirstParameter();
			return firstParameter.equals(this.instance);
		}
		if (handler.isInvoked(HASH_CODE_METHOD)) {
			return this.instance.hashCode();
		}
		if (handler.isInvoked(TO_STRING_METHOD)) {
			return this.instance.toString();
		}
		return super.handle(handler);
	}

	public Object getInstance() {
		return this.instance;
	}
}

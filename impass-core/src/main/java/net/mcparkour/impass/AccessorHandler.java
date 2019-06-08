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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import net.mcparkour.impass.handler.method.MethodHandler;
import net.mcparkour.impass.handler.method.ReflectionOperations;
import net.mcparkour.impass.handler.registry.method.MethodAnnotationHandlerRegistry;
import net.mcparkour.impass.handler.registry.type.TypeAnnotationHandlerRegistry;
import org.jetbrains.annotations.Nullable;

public class AccessorHandler implements InvocationHandler {

	private AccessorFactory accessorFactory;
	private TypeAnnotationHandlerRegistry typeHandlerRegistry;
	private MethodAnnotationHandlerRegistry methodHandlerRegistry;
	private ReflectionOperations reflectionOperations;

	public AccessorHandler(AccessorFactory accessorFactory, TypeAnnotationHandlerRegistry typeHandlerRegistry, MethodAnnotationHandlerRegistry methodHandlerRegistry, ReflectionOperations reflectionOperations) {
		this.accessorFactory = accessorFactory;
		this.typeHandlerRegistry = typeHandlerRegistry;
		this.methodHandlerRegistry = methodHandlerRegistry;
		this.reflectionOperations = reflectionOperations;
	}

	@Override
	@Nullable
	public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
		var accessorType = method.getDeclaringClass();
		var type = this.typeHandlerRegistry.handleType(accessorType);
		var parameters = args == null ? new Object[0] : args;
		var handler = new MethodHandler(type, method, parameters, this.accessorFactory, this.typeHandlerRegistry, this.reflectionOperations);
		return handle(handler);
	}

	@Nullable
	public Object handle(MethodHandler handler) throws Throwable {
		return this.methodHandlerRegistry.handleMethod(handler);
	}

	public AccessorFactory getAccessorFactory() {
		return this.accessorFactory;
	}

	public TypeAnnotationHandlerRegistry getTypeHandlerRegistry() {
		return this.typeHandlerRegistry;
	}

	public MethodAnnotationHandlerRegistry getMethodHandlerRegistry() {
		return this.methodHandlerRegistry;
	}
}

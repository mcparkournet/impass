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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import net.mcparkour.impass.AccessorFactory;
import net.mcparkour.impass.AccessorHandler;
import net.mcparkour.impass.annotation.type.Type;
import net.mcparkour.impass.AccessorHandlerException;
import net.mcparkour.impass.handler.method.MethodAnnotationHandler;
import net.mcparkour.impass.handler.method.MethodHandler;
import net.mcparkour.impass.handler.type.TypeAnnotationHandler;
import net.mcparkour.impass.handler.registry.AnnotationHandlerRegistry;
import net.mcparkour.impass.util.reflection.Reflections;
import org.jetbrains.annotations.Nullable;

public class InstanceAccessorHandler extends AccessorHandler {

	private static final Method GET_INSTANCE_METHOD = Reflections.getMethod(InstanceAccessor.class, "getInstance");
	private static final Method EQUALS_METHOD = Reflections.getMethod(Object.class, "equals", Object.class);
	private static final Method HASH_CODE_METHOD = Reflections.getMethod(Object.class, "hashCode");
	private static final Method TO_STRING_METHOD = Reflections.getMethod(Object.class, "toString");

	private Class<?> instanceType;
	private Object instance;

	public InstanceAccessorHandler(AccessorFactory accessorFactory, AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry, AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry, Class<? extends InstanceAccessor> accessorClass, Object instance) {
		super(accessorFactory, typeHandlerRegistry, methodHandlerRegistry);
		this.instanceType = typeHandlerRegistry.get(Type.class)
			.getTypeFromAnnotation(accessorClass);
		this.instance = instance;
	}

	@Override
	@Nullable
	public Object handle(Method method, Object[] parameters) throws Throwable {
		var accessorFactory = getAccessorFactory();
		var typeHandlerRegistry = getTypeHandlerRegistry();
		var methodHandlerRegistry = getMethodHandlerRegistry();
		var handler = new MethodHandler(this.instanceType, method, parameters, accessorFactory, typeHandlerRegistry, new InstanceReflectionOperations(this.instance));
		if (method.equals(GET_INSTANCE_METHOD)) {
			return this.instance;
		}
		if (method.equals(EQUALS_METHOD)) {
			handler.remapParameters();
			return handler.getParameters()[0].equals(this.instance);
		}
		if (method.equals(HASH_CODE_METHOD)) {
			return this.instance.hashCode();
		}
		if (method.equals(TO_STRING_METHOD)) {
			return this.instance.toString();
		}
		var annotations = method.getDeclaredAnnotations();
		for (var annotation : annotations) {
			var annotationType = annotation.annotationType();
			var methodAnnotationHandler = methodHandlerRegistry.get(annotationType);
			if (methodAnnotationHandler != null) {
				return methodAnnotationHandler.handleRaw(annotation, handler);
			}
		}
		throw new AccessorHandlerException("Method does not have any annotation");
	}
}

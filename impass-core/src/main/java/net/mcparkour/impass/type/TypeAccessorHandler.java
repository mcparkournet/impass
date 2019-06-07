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

package net.mcparkour.impass.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import net.mcparkour.impass.AccessorFactory;
import net.mcparkour.impass.AccessorHandler;
import net.mcparkour.impass.AccessorHandlerException;
import net.mcparkour.impass.handler.method.MethodAnnotationHandler;
import net.mcparkour.impass.handler.method.MethodHandler;
import net.mcparkour.impass.handler.registry.AnnotationHandlerRegistry;
import net.mcparkour.impass.handler.type.TypeAnnotationHandler;
import org.jetbrains.annotations.Nullable;

public class TypeAccessorHandler extends AccessorHandler {

	private Class<?> type;

	public TypeAccessorHandler(AccessorFactory accessorFactory, AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry, AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry, Class<?> accessorClass) {
		super(accessorFactory, typeHandlerRegistry, methodHandlerRegistry);
		var annotations = accessorClass.getAnnotations();
		for (var annotation : annotations) {
			var annotationType = annotation.annotationType();
			var methodAnnotationHandler = typeHandlerRegistry.get(annotationType);
			if (methodAnnotationHandler != null) {
				this.type = methodAnnotationHandler.getTypeFromAnnotation(accessorClass);
				break;
			}
		}
	}

	@Override
	@Nullable
	public Object handle(Method method, Object[] parameters) throws Throwable {
		var accessorFactory = getAccessorFactory();
		var typeHandlerRegistry = getTypeHandlerRegistry();
		var methodHandlerRegistry = getMethodHandlerRegistry();
		var context = new MethodHandler(this.type, method, parameters, accessorFactory, typeHandlerRegistry, methodHandlerRegistry, new TypeReflectionOperations());
		var annotations = method.getDeclaredAnnotations();
		for (var annotation : annotations) {
			var annotationType = annotation.annotationType();
			var handler = methodHandlerRegistry.get(annotationType);
			if (handler != null) {
				return handler.handleRaw(annotation, context);
			}
		}
		throw new AccessorHandlerException("Method does not have any annotation");
	}
}

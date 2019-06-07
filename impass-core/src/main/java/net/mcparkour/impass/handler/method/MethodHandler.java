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

package net.mcparkour.impass.handler.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import net.mcparkour.impass.AccessorFactory;
import net.mcparkour.impass.handler.registry.AnnotationHandlerRegistry;
import net.mcparkour.impass.handler.type.TypeAnnotationHandler;
import net.mcparkour.impass.instance.InstanceAccessor;
import net.mcparkour.impass.util.reflection.Reflections;
import org.jetbrains.annotations.Nullable;

public class MethodHandler {

	private Class<?> type;
	private Method method;
	private Object[] parameters;
	private Class<?>[] parameterTypes;
	private AccessorFactory accessorFactory;
	private AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry;
	private AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry;
	private ReflectionOperations reflectionOperations;

	public MethodHandler(Class<?> type, Method method, Object[] parameters, AccessorFactory accessorFactory, AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry, AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry, ReflectionOperations reflectionOperations) {
		this.type = type;
		this.method = method;
		this.parameters = parameters;
		this.parameterTypes = method.getParameterTypes();
		this.accessorFactory = accessorFactory;
		this.typeHandlerRegistry = typeHandlerRegistry;
		this.methodHandlerRegistry = methodHandlerRegistry;
		this.reflectionOperations = reflectionOperations;
	}

	@Nullable
	public Object getFieldValue(String fieldName) {
		var field = Reflections.getField(this.type, fieldName);
		var value = this.reflectionOperations.getFieldValue(field);
		if (value == null) {
			return null;
		}
		return remapReturnType(value);
	}

	public void setFieldValue(String fieldName) {
		remapParameters();
		var field = Reflections.getField(this.type, fieldName);
		this.reflectionOperations.setFieldValue(field, this.parameters[0]);
	}

	@Nullable
	public Object invokeMethod(String methodName) throws Throwable {
		remapParameters();
		var method = Reflections.getMethod(this.type, methodName, this.parameterTypes);
		var value = this.reflectionOperations.invokeMethod(method, this.parameters);
		if (value == null) {
			return null;
		}
		return remapReturnType(value);
	}

	public Object newInstance() {
		remapParameters();
		var constructor = Reflections.getConstructor(this.type, this.parameterTypes);
		var implementation = Reflections.newInstance(constructor, this.parameters);
		return remapReturnType(implementation);
	}

	public Object remapReturnType(Object value) {
		var returnType = this.method.getReturnType();
		if (InstanceAccessor.class.isAssignableFrom(returnType)) {
			var returnAccessorType = returnType.asSubclass(InstanceAccessor.class);
			return this.accessorFactory.createInstanceAccessor(this.typeHandlerRegistry, this.methodHandlerRegistry, returnAccessorType, value);
		}
		return value;
	}

	public void remapParameters() {
		var parametersLength = this.parameters.length;
		for (int index = 0; index < parametersLength; index++) {
			var parameterType = this.parameterTypes[index];
			if (InstanceAccessor.class.isAssignableFrom(parameterType)) {
				var parameter = this.parameters[index];
				if (parameter != null) {
					var accessor = (InstanceAccessor) parameter;
					var instance = accessor.getInstance();
					this.parameters[index] = instance;
					this.parameterTypes[index] = instance.getClass();
				} else {
					var annotations = parameterType.getAnnotations();
					for (var annotation : annotations) {
						var annotationType = annotation.annotationType();
						var methodAnnotationHandler = this.typeHandlerRegistry.get(annotationType);
						if (methodAnnotationHandler != null) {
							this.parameterTypes[index] = methodAnnotationHandler.getTypeFromAnnotation(parameterType);
							break;
						}
					}
				}
			}
		}
	}

	public Class<?> getType() {
		return this.type;
	}

	public Method getMethod() {
		return this.method;
	}

	public Object[] getParameters() {
		return this.parameters;
	}

	public Class<?>[] getParameterTypes() {
		return this.parameterTypes;
	}

	public AccessorFactory getAccessorFactory() {
		return this.accessorFactory;
	}

	public AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> getTypeHandlerRegistry() {
		return this.typeHandlerRegistry;
	}

	public ReflectionOperations getReflectionOperations() {
		return this.reflectionOperations;
	}
}

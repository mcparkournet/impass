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
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import net.mcparkour.common.reflection.Reflections;
import net.mcparkour.impass.AccessorFactory;
import net.mcparkour.impass.handler.registry.AnnotationHandlerRegistry;
import net.mcparkour.impass.handler.registry.type.TypeAnnotationHandlerRegistry;
import net.mcparkour.impass.handler.type.TypeAnnotationHandler;
import net.mcparkour.impass.instance.InstanceAccessor;
import org.jetbrains.annotations.Nullable;

public class MethodHandler {

	private Object proxyInstance;
	private Method method;
	private Class<?> accessorType;
	private Object[] parameters;
	private Class<?>[] parameterTypes;
	private Class<?> implementationType;
	private AccessorFactory accessorFactory;
	private TypeAnnotationHandlerRegistry typeHandlerRegistry;
	private ReflectionOperations reflectionOperations;

	public MethodHandler(Object proxyInstance, Method method, Class<?> accessorType, Object[] parameters, Class<?>[] parameterTypes, Class<?> implementationType, AccessorFactory accessorFactory, TypeAnnotationHandlerRegistry typeHandlerRegistry, ReflectionOperations reflectionOperations) {
		this.proxyInstance = proxyInstance;
		this.method = method;
		this.accessorType = accessorType;
		this.parameters = parameters;
		this.parameterTypes = parameterTypes;
		this.implementationType = implementationType;
		this.accessorFactory = accessorFactory;
		this.typeHandlerRegistry = typeHandlerRegistry;
		this.reflectionOperations = reflectionOperations;
	}

	@Nullable
	public Object getFieldValue(String fieldName) {
		var field = Reflections.getField(this.implementationType, fieldName);
		var value = this.reflectionOperations.getFieldValue(field);
		if (value == null) {
			return null;
		}
		return remapReturnType(value);
	}

	public void setFieldValue(String fieldName) {
		remapParameters();
		var field = Reflections.getField(this.implementationType, fieldName);
		var firstParameter = getFirstParameter();
		this.reflectionOperations.setFieldValue(field, firstParameter);
	}

	@Nullable
	public Object invokeMethod() throws Throwable {
		var name = this.method.getName();
		return invokeMethod(name);
	}

	@Nullable
	public Object invokeMethod(String methodName) throws Throwable {
		remapParameters();
		var method = Reflections.getMethod(this.implementationType, methodName, this.parameterTypes);
		var value = this.reflectionOperations.invokeMethod(method, this.parameters);
		if (value == null) {
			return null;
		}
		return remapReturnType(value);
	}

	public Object newInstance() {
		remapParameters();
		var constructor = Reflections.getConstructor(this.implementationType, this.parameterTypes);
		var implementation = Reflections.newInstance(constructor, this.parameters);
		return remapReturnType(implementation);
	}

	public Object remapReturnType(Object value) {
		var returnType = this.method.getReturnType();
		if (InstanceAccessor.class.isAssignableFrom(returnType)) {
			var returnAccessorType = returnType.asSubclass(InstanceAccessor.class);
			return this.accessorFactory.createInstanceAccessor(returnAccessorType, value);
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
				}
				this.parameterTypes[index] = this.typeHandlerRegistry.handleType(parameterType);
			}
		}
	}

	public boolean isInvoked(Method method) {
		return this.method.equals(method);
	}

	public Object invokeDefaultMethod() throws Throwable {
		var methodName = this.method.getName();
		var methodType = createMethodType();
		return MethodHandles.lookup()
			.findSpecial(this.accessorType, methodName, methodType, this.accessorType)
			.bindTo(this.proxyInstance)
			.invokeWithArguments(this.parameters);
	}

	private MethodType createMethodType() {
		var returnType = this.method.getReturnType();
		return MethodType.methodType(returnType, this.parameterTypes);
	}

	public boolean isMethodDefault() {
		return this.method.isDefault();
	}

	public Object getFirstParameter() {
		return this.parameters[0];
	}

	public Object getProxyInstance() {
		return this.proxyInstance;
	}

	public Method getMethod() {
		return this.method;
	}

	public Class<?> getAccessorType() {
		return this.accessorType;
	}

	public Object[] getParameters() {
		return this.parameters;
	}

	public Class<?>[] getParameterTypes() {
		return this.parameterTypes;
	}

	public Class<?> getImplementationType() {
		return this.implementationType;
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

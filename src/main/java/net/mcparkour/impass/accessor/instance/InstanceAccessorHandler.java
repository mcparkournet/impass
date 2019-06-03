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

package net.mcparkour.impass.accessor.instance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import net.mcparkour.impass.AccessorFactory;
import net.mcparkour.impass.accessor.AccessorHandlerException;
import net.mcparkour.impass.annotation.Getter;
import net.mcparkour.impass.annotation.Setter;
import net.mcparkour.impass.annotation.Type;
import net.mcparkour.impass.reflection.Reflections;
import org.jetbrains.annotations.Nullable;

public class InstanceAccessorHandler implements InvocationHandler {

	private static final Method GET_INSTANCE_METHOD = Reflections.getMethod(InstanceAccessor.class, "getInstance");
	private static final Method EQUALS_METHOD = Reflections.getMethod(Object.class, "equals", Object.class);
	private static final Method HASH_CODE_METHOD = Reflections.getMethod(Object.class, "hashCode");
	private static final Method TO_STRING_METHOD = Reflections.getMethod(Object.class, "toString");

	private AccessorFactory accessorFactory;
	private Class<?> instanceType;
	private Object instance;

	public InstanceAccessorHandler(AccessorFactory accessorFactory, Class<? extends InstanceAccessor> accessorClass, Object instance) {
		this.accessorFactory = accessorFactory;
		this.instanceType = getAnnotationClass(accessorClass);
		this.instance = instance;
	}

	private Class<?> getAnnotationClass(Class<?> accessorClass) {
		var typeAnnotation = accessorClass.getAnnotation(Type.class);
		if (typeAnnotation == null) {
			throw new AccessorHandlerException("Type annotation not found");
		}
		var implementationClassName = typeAnnotation.value();
		return Reflections.getClass(implementationClassName);
	}

	@Nullable
	@Override
	public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
		var parameters = args == null ? new Object[0] : args;
		var handler = new Handler(method, parameters);
		return handler.handle();
	}

	private final class Handler {

		private Method accessorMethod;
		private Object[] parameters;

		private Handler(Method accessorMethod, Object[] parameters) {
			this.accessorMethod = accessorMethod;
			this.parameters = parameters;
		}

		@Nullable
		private Object handle() throws Throwable {
			if (this.accessorMethod.equals(GET_INSTANCE_METHOD)) {
				return InstanceAccessorHandler.this.instance;
			}
			if (this.accessorMethod.equals(EQUALS_METHOD)) {
				return handleEquals();
			}
			if (this.accessorMethod.equals(HASH_CODE_METHOD)) {
				return InstanceAccessorHandler.this.instance.hashCode();
			}
			if (this.accessorMethod.equals(TO_STRING_METHOD)) {
				return InstanceAccessorHandler.this.instance.toString();
			}
			var getterAnnotation = this.accessorMethod.getAnnotation(Getter.class);
			if (getterAnnotation != null) {
				return handleGetter(getterAnnotation);
			}
			var setterAnnotation = this.accessorMethod.getAnnotation(Setter.class);
			if (setterAnnotation != null) {
				return handleSetter(setterAnnotation);
			}
			var methodAnnotation = this.accessorMethod.getAnnotation(net.mcparkour.impass.annotation.Method.class);
			if (methodAnnotation != null) {
				return handleMethod(methodAnnotation);
			}
			throw new AccessorHandlerException("Method does not have any Impass annotation");
		}

		private boolean handleEquals() {
			var parameterTypes = this.accessorMethod.getParameterTypes();
			remapParameters(this.parameters, parameterTypes);
			return this.parameters[0].equals(InstanceAccessorHandler.this.instance);
		}

		@Nullable
		private Object handleGetter(Getter getterAnnotation) {
			var fieldName = getterAnnotation.value();
			var field = Reflections.getField(InstanceAccessorHandler.this.instanceType, fieldName);
			var value = Reflections.getFieldValue(field, InstanceAccessorHandler.this.instance);
			if (value == null) {
				return null;
			}
			var returnType = this.accessorMethod.getReturnType();
			return remapReturnType(returnType, value);
		}

		@Nullable
		private Object handleSetter(Setter setterAnnotation) {
			var fieldName = setterAnnotation.value();
			var parameterTypes = this.accessorMethod.getParameterTypes();
			remapParameters(this.parameters, parameterTypes);
			var field = Reflections.getField(InstanceAccessorHandler.this.instanceType, fieldName);
			Reflections.setFieldValue(field, InstanceAccessorHandler.this.instance, this.parameters[0]);
			return null;
		}

		@Nullable
		private Object handleMethod(net.mcparkour.impass.annotation.Method methodAnnotation) throws Throwable {
			var value = invokeMethod(methodAnnotation);
			if (value == null) {
				return null;
			}
			var returnType = this.accessorMethod.getReturnType();
			return remapReturnType(returnType, value);
		}

		@Nullable
		private Object invokeMethod(net.mcparkour.impass.annotation.Method methodAnnotation) throws Throwable {
			var methodName = methodAnnotation.value();
			var parameterTypes = this.accessorMethod.getParameterTypes();
			remapParameters(this.parameters, parameterTypes);
			var method = Reflections.getMethod(InstanceAccessorHandler.this.instanceType, methodName, parameterTypes);
			return Reflections.invokeMethod(method, InstanceAccessorHandler.this.instance, this.parameters);
		}

		private Object remapReturnType(Class<?> returnType, Object value) {
			if (InstanceAccessor.class.isAssignableFrom(returnType)) {
				var returnAccessorType = returnType.asSubclass(InstanceAccessor.class);
				return InstanceAccessorHandler.this.accessorFactory.createInstanceAccessor(returnAccessorType, value);
			}
			return value;
		}

		private void remapParameters(Object[] parameters, Class<?>[] parameterTypes) {
			for (int index = 0; index < parameters.length; index++) {
				var parameterType = parameterTypes[index];
				if (InstanceAccessor.class.isAssignableFrom(parameterType)) {
					var parameter = parameters[index];
					if (parameter != null) {
						var accessor = (InstanceAccessor) parameter;
						var instance = accessor.getInstance();
						parameters[index] = instance;
						parameterTypes[index] = instance.getClass();
					} else {
						parameterTypes[index] = getAnnotationClass(parameterType);
					}
				}
			}
		}
	}
}

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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import net.mcparkour.impass.annotation.ImpassGetter;
import net.mcparkour.impass.annotation.ImpassMethod;
import net.mcparkour.impass.annotation.ImpassSetter;
import net.mcparkour.impass.util.reflection.Reflections;
import org.jetbrains.annotations.Nullable;

class AccessorHandler implements InvocationHandler {

	private static final Method GET_IMPLEMENTATION_METHOD = Reflections.getMethod(Accessor.class, "getImplementation");
	private static final Method EQUALS_METHOD = Reflections.getMethod(Object.class, "equals", Object.class);
	private static final Method HASH_CODE_METHOD = Reflections.getMethod(Object.class, "hashCode");
	private static final Method TO_STRING_METHOD = Reflections.getMethod(Object.class, "toString");

	private Impass impass;
	private Object implementation;
	private Class<?> implementationClass;

	AccessorHandler(Impass impass, Object implementation) {
		this.impass = impass;
		this.implementation = implementation;
		this.implementationClass = implementation.getClass();
	}

	@Nullable
	@Override
	public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
		Object[] parameters = args == null ? new Object[0] : args;
		Handler handler = new Handler(method, parameters);
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
			if (this.accessorMethod.equals(GET_IMPLEMENTATION_METHOD)) {
				return AccessorHandler.this.implementation;
			}
			if (this.accessorMethod.equals(EQUALS_METHOD)) {
				Class<?>[] parameterTypes = this.accessorMethod.getParameterTypes();
				remapParameters(this.parameters, parameterTypes);
				return this.parameters[0].equals(AccessorHandler.this.implementation);
			}
			if (this.accessorMethod.equals(HASH_CODE_METHOD)) {
				return AccessorHandler.this.implementation.hashCode();
			}
			if (this.accessorMethod.equals(TO_STRING_METHOD)) {
				return AccessorHandler.this.implementation.toString();
			}
			ImpassGetter getterAnnotation = this.accessorMethod.getAnnotation(ImpassGetter.class);
			if (getterAnnotation != null) {
				return handleGetter(getterAnnotation);
			}
			ImpassSetter setterAnnotation = this.accessorMethod.getAnnotation(ImpassSetter.class);
			if (setterAnnotation != null) {
				return handleSetter(setterAnnotation);
			}
			ImpassMethod methodAnnotation = this.accessorMethod.getAnnotation(ImpassMethod.class);
			if (methodAnnotation != null) {
				return handleMethod(methodAnnotation);
			}
			throw new AccessorHandlerException("Method does not have any Impass annotation");
		}

		@Nullable
		private Object handleGetter(ImpassGetter getterAnnotation) {
			String fieldName = getterAnnotation.value();
			Field field = Reflections.getField(AccessorHandler.this.implementationClass, fieldName);
			Object value = Reflections.getFieldValue(field, AccessorHandler.this.implementation);
			if (value == null) {
				return null;
			}
			Class<?> returnType = this.accessorMethod.getReturnType();
			return remapReturnType(returnType, value);
		}

		@Nullable
		private Object handleSetter(ImpassSetter setterAnnotation) {
			String fieldName = setterAnnotation.value();
			Class<?>[] parameterTypes = this.accessorMethod.getParameterTypes();
			remapParameters(this.parameters, parameterTypes);
			Field field = Reflections.getField(AccessorHandler.this.implementationClass, fieldName);
			Reflections.setFieldValue(field, AccessorHandler.this.implementation, this.parameters[0]);
			return null;
		}

		@Nullable
		private Object handleMethod(ImpassMethod methodAnnotation) throws Throwable {
			Object value = invokeMethod(methodAnnotation);
			if (value == null) {
				return null;
			}
			Class<?> returnType = this.accessorMethod.getReturnType();
			return remapReturnType(returnType, value);
		}

		@Nullable
		private Object invokeMethod(ImpassMethod methodAnnotation) throws Throwable {
			String methodName = methodAnnotation.value();
			Class<?>[] parameterTypes = this.accessorMethod.getParameterTypes();
			remapParameters(this.parameters, parameterTypes);
			Method method = Reflections.getMethod(AccessorHandler.this.implementationClass, methodName, parameterTypes);
			return Reflections.invokeMethod(method, AccessorHandler.this.implementation, this.parameters);
		}

		private Object remapReturnType(Class<?> returnType, Object value) {
			if (Accessor.class.isAssignableFrom(returnType)) {
				Class<? extends Accessor> returnAccessorType = returnType.asSubclass(Accessor.class);
				return AccessorHandler.this.impass.createAccessor(returnAccessorType, value);
			}
			return value;
		}

		private void remapParameters(Object[] parameters, Class<?>[] parameterTypes) {
			for (int index = 0; index < parameters.length; index++) {
				Class<?> parameterType = parameterTypes[index];
				if (Accessor.class.isAssignableFrom(parameterType)) {
					Object parameter = parameters[index];
					Accessor accessor = (Accessor) parameter;
					Object implementation = accessor.getImplementation();
					parameters[index] = implementation;
					parameterTypes[index] = implementation.getClass();
				}
			}
		}
	}
}

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

package net.mcparkour.impass.accessor.type;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import net.mcparkour.impass.AccessorFactory;
import net.mcparkour.impass.accessor.AccessorHandlerException;
import net.mcparkour.impass.accessor.instance.InstanceAccessor;
import net.mcparkour.impass.annotation.Getter;
import net.mcparkour.impass.annotation.Setter;
import net.mcparkour.impass.annotation.Type;
import net.mcparkour.impass.reflection.Reflections;
import org.jetbrains.annotations.Nullable;

public class TypeAccessorHandler implements InvocationHandler {

	private AccessorFactory accessorFactory;
	private Class<?> type;

	public TypeAccessorHandler(AccessorFactory accessorFactory, Class<?> accessorClass) {
		this.accessorFactory = accessorFactory;
		this.type = getAnnotationClass(accessorClass);
	}

	private Class<?> getAnnotationClass(Class<?> accessorClass) {
		var typeAnnotation = accessorClass.getAnnotation(Type.class);
		if (typeAnnotation == null) {
			throw new AccessorHandlerException("Type annotation not found");
		}
		var className = typeAnnotation.value();
		return Reflections.getClass(className);
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
			var constructorAnnotation = this.accessorMethod.getAnnotation(net.mcparkour.impass.annotation.Constructor.class);
			if (constructorAnnotation != null) {
				return handleConstructor();
			}
			throw new AccessorHandlerException("Method does not have any Impass annotation");
		}

		@Nullable
		private Object handleGetter(Getter getterAnnotation) {
			var fieldName = getterAnnotation.value();
			var field = Reflections.getField(TypeAccessorHandler.this.type, fieldName);
			var value = Reflections.getStaticFieldValue(field);
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
			var field = Reflections.getField(TypeAccessorHandler.this.type, fieldName);
			Reflections.setStaticFieldValue(field, this.parameters[0]);
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
			var method = Reflections.getMethod(TypeAccessorHandler.this.type, methodName, parameterTypes);
			return Reflections.invokeStaticMethod(method, this.parameters);
		}

		private Object handleConstructor() {
			var parameterTypes = this.accessorMethod.getParameterTypes();
			remapParameters(this.parameters, parameterTypes);
			var constructor = Reflections.getConstructor(TypeAccessorHandler.this.type, parameterTypes);
			var implementation = Reflections.newInstance(constructor, this.parameters);
			var returnType = this.accessorMethod.getReturnType();
			return remapReturnType(returnType, implementation);
		}

		private Object remapReturnType(Class<?> returnType, Object value) {
			if (InstanceAccessor.class.isAssignableFrom(returnType)) {
				var returnAccessorType = returnType.asSubclass(InstanceAccessor.class);
				return TypeAccessorHandler.this.accessorFactory.createInstanceAccessor(returnAccessorType, value);
			}
			return value;
		}

		private void remapParameters(Object[] parameters, Class<?>[] parameterTypes) {
			for (int index = 0; index < parameters.length; index++) {
				var parameterType = parameterTypes[index];
				if (InstanceAccessor.class.isAssignableFrom(parameterType)) {
					Object parameter = parameters[index];
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

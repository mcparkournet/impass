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

	private Object implementation;
	private Class<?> implementationClass;

	AccessorHandler(Object implementation) {
		this.implementation = implementation;
		this.implementationClass = implementation.getClass();
	}

	@Nullable
	@Override
	public Object invoke(Object proxy, Method accessorMethod, Object[] parameters) {
		ImpassGetter getterAnnotation = accessorMethod.getAnnotation(ImpassGetter.class);
		if (getterAnnotation != null) {
			return handleGetter(getterAnnotation);
		}
		ImpassSetter setterAnnotation = accessorMethod.getAnnotation(ImpassSetter.class);
		if (setterAnnotation != null) {
			return handleSetter(setterAnnotation, parameters[0]);
		}
		return handleMethod(accessorMethod, parameters);
	}

	@Nullable
	private Object handleGetter(ImpassGetter getterAnnotation) {
		String fieldName = getterAnnotation.value();
		Field targetField = Reflections.getField(this.implementationClass, fieldName);
		return Reflections.getFieldValue(targetField, this.implementation);
	}

	@Nullable
	private Object handleSetter(ImpassSetter setterAnnotation, @Nullable Object value) {
		String fieldName = setterAnnotation.value();
		Field targetField = Reflections.getField(this.implementationClass, fieldName);
		Reflections.setFieldValue(targetField, this.implementation, value);
		return null;
	}

	@Nullable
	private Object handleMethod(Method accessorMethod, Object[] parameters) {
		String methodName = getMethodName(accessorMethod);
		Class<?>[] parameterTypes = accessorMethod.getParameterTypes();
		Method method = Reflections.getMethod(this.implementationClass, methodName, parameterTypes);
		return Reflections.invokeMethod(method, this.implementation, parameters);
	}

	private String getMethodName(Method method) {
		ImpassMethod methodAnnotation = method.getAnnotation(ImpassMethod.class);
		if (methodAnnotation != null) {
			return methodAnnotation.value();
		}
		return method.getName();
	}
}

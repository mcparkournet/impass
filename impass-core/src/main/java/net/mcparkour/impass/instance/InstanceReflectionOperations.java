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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.mcparkour.impass.handler.method.ReflectionOperations;
import net.mcparkour.impass.util.reflection.Reflections;
import org.jetbrains.annotations.Nullable;

public class InstanceReflectionOperations implements ReflectionOperations {

	private Object instance;

	public InstanceReflectionOperations(Object instance) {
		this.instance = instance;
	}

	@Override
	@Nullable
	public Object getFieldValue(Field field) {
		return Reflections.getFieldValue(field, this.instance);
	}

	@Override
	public void setFieldValue(Field field, @Nullable Object value) {
		Reflections.setFieldValue(field, this.instance, value);
	}

	@Override
	@Nullable
	public Object invokeMethod(Method method, Object... parameters) throws Throwable {
		return Reflections.invokeMethod(method, this.instance, parameters);
	}

	public Object getInstance() {
		return this.instance;
	}
}
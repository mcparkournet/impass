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

import java.lang.annotation.Annotation;
import net.mcparkour.impass.handler.method.MethodAnnotationHandler;
import net.mcparkour.impass.handler.registry.AnnotationHandlerRegistry;
import net.mcparkour.impass.handler.type.TypeAnnotationHandler;
import net.mcparkour.impass.instance.InstanceAccessor;
import net.mcparkour.impass.instance.InstanceAccessorHandler;
import net.mcparkour.impass.type.TypeAccessor;
import net.mcparkour.impass.type.TypeAccessorHandler;
import net.mcparkour.impass.util.reflection.Reflections;

public class AccessorFactory {

	private AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry;
	private AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry;

	public AccessorFactory(AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry, AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry) {
		this.typeHandlerRegistry = typeHandlerRegistry;
		this.methodHandlerRegistry = methodHandlerRegistry;
	}

	public <T extends TypeAccessor> T createTypeAccessor(Class<T> accessorClass) {
		return createTypeAccessor(this.typeHandlerRegistry, this.methodHandlerRegistry, accessorClass);
	}

	public <T extends TypeAccessor> T createTypeAccessor(AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry, AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry, Class<T> accessorClass) {
		var handler = new TypeAccessorHandler(this, typeHandlerRegistry, methodHandlerRegistry, accessorClass);
		return createAccessor(accessorClass, handler);
	}

	public <T extends InstanceAccessor> T createInstanceAccessor(Class<T> accessorClass, Object instance) {
		return createInstanceAccessor(this.typeHandlerRegistry, this.methodHandlerRegistry, accessorClass, instance);
	}

	public <T extends InstanceAccessor> T createInstanceAccessor(AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry, AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry, Class<T> accessorClass, Object instance) {
		var handler = new InstanceAccessorHandler(this, typeHandlerRegistry, methodHandlerRegistry, accessorClass, instance);
		return createAccessor(accessorClass, handler);
	}

	public <T extends Accessor> T createAccessor(Class<T> accessorClass, AccessorHandler handler) {
		return Reflections.newProxyInstance(accessorClass, handler);
	}

	public AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> getTypeHandlerRegistry() {
		return this.typeHandlerRegistry;
	}

	public AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> getMethodHandlerRegistry() {
		return this.methodHandlerRegistry;
	}
}

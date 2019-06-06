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
import net.mcparkour.impass.annotation.handler.method.ConstructorAnnotationHandler;
import net.mcparkour.impass.annotation.handler.method.GetterAnnotationHandler;
import net.mcparkour.impass.annotation.handler.method.MethodAnnotationHandler;
import net.mcparkour.impass.annotation.handler.method.SetterAnnotationHandler;
import net.mcparkour.impass.annotation.handler.type.TypeAnnotationHandler;
import net.mcparkour.impass.handler.registry.AnnotationHandlerRegistry;
import net.mcparkour.impass.instance.InstanceAccessor;
import net.mcparkour.impass.instance.InstanceAccessorHandler;
import net.mcparkour.impass.type.TypeAccessor;
import net.mcparkour.impass.type.TypeAccessorHandler;
import net.mcparkour.impass.util.reflection.Reflections;

public class AccessorFactory {

	public static final AnnotationHandlerRegistry<net.mcparkour.impass.handler.type.TypeAnnotationHandler<? extends Annotation>> TYPE_HANDLER_REGISTRY = AnnotationHandlerRegistry.<net.mcparkour.impass.handler.type.TypeAnnotationHandler<? extends Annotation>>builder().
		add(new TypeAnnotationHandler())
		.build();
	public static final AnnotationHandlerRegistry<net.mcparkour.impass.handler.method.MethodAnnotationHandler<? extends Annotation>> TYPE_METHOD_HANDLER_REGISTRY = AnnotationHandlerRegistry.<net.mcparkour.impass.handler.method.MethodAnnotationHandler<?>>builder().
		add(new GetterAnnotationHandler())
		.add(new SetterAnnotationHandler())
		.add(new MethodAnnotationHandler())
		.add(new ConstructorAnnotationHandler())
		.build();
	public static final AnnotationHandlerRegistry<net.mcparkour.impass.handler.method.MethodAnnotationHandler<? extends Annotation>> INSTANCE_METHOD_HANDLER_REGISTRY = AnnotationHandlerRegistry.<net.mcparkour.impass.handler.method.MethodAnnotationHandler<?>>builder().
		add(new GetterAnnotationHandler())
		.add(new SetterAnnotationHandler())
		.add(new MethodAnnotationHandler())
		.build();

	public <T extends TypeAccessor> T createTypeAccessor(Class<T> accessorClass) {
		return createTypeAccessor(TYPE_HANDLER_REGISTRY, TYPE_METHOD_HANDLER_REGISTRY, accessorClass);
	}

	public <T extends TypeAccessor> T createTypeAccessor(AnnotationHandlerRegistry<net.mcparkour.impass.handler.type.TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry, AnnotationHandlerRegistry<net.mcparkour.impass.handler.method.MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry, Class<T> accessorClass) {
		var handler = new TypeAccessorHandler(this, typeHandlerRegistry, methodHandlerRegistry, accessorClass);
		return createAccessor(accessorClass, handler);
	}

	public <T extends InstanceAccessor> T createInstanceAccessor(Class<T> accessorClass, Object instance) {
		return createInstanceAccessor(TYPE_HANDLER_REGISTRY, INSTANCE_METHOD_HANDLER_REGISTRY, accessorClass, instance);
	}

	public <T extends InstanceAccessor> T createInstanceAccessor(AnnotationHandlerRegistry<net.mcparkour.impass.handler.type.TypeAnnotationHandler<? extends Annotation>> typeHandlerRegistry, AnnotationHandlerRegistry<net.mcparkour.impass.handler.method.MethodAnnotationHandler<? extends Annotation>> methodHandlerRegistry, Class<T> accessorClass, Object instance) {
		var handler = new InstanceAccessorHandler(this, TYPE_HANDLER_REGISTRY, INSTANCE_METHOD_HANDLER_REGISTRY, accessorClass, instance);
		return createAccessor(accessorClass, handler);
	}

	public <T extends Accessor> T createAccessor(Class<T> accessorClass, AccessorHandler handler) {
		return Reflections.newProxyInstance(accessorClass, handler);
	}
}

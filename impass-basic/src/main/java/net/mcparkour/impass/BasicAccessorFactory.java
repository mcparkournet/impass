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

import net.mcparkour.common.reflection.Reflections;
import net.mcparkour.impass.annotation.handler.method.ConstructorAnnotationHandler;
import net.mcparkour.impass.annotation.handler.method.GetterAnnotationHandler;
import net.mcparkour.impass.annotation.handler.method.MethodAnnotationHandler;
import net.mcparkour.impass.annotation.handler.method.SetterAnnotationHandler;
import net.mcparkour.impass.annotation.handler.type.TypeAnnotationHandler;
import net.mcparkour.impass.handler.registry.method.MethodAnnotationHandlerRegistry;
import net.mcparkour.impass.handler.registry.type.TypeAnnotationHandlerRegistry;
import net.mcparkour.impass.instance.InstanceAccessor;
import net.mcparkour.impass.instance.InstanceAccessorHandler;
import net.mcparkour.impass.type.TypeAccessor;
import net.mcparkour.impass.type.TypeAccessorHandler;

public class BasicAccessorFactory implements AccessorFactory {

    private TypeAnnotationHandlerRegistry typeHandlerRegistry;
    private MethodAnnotationHandlerRegistry methodHandlerRegistry;

    public BasicAccessorFactory() {
        this(createTypeHandlerRegistry(), createMethodHandlerRegistry());
    }

    public BasicAccessorFactory(final TypeAnnotationHandlerRegistry typeHandlerRegistry, final MethodAnnotationHandlerRegistry methodHandlerRegistry) {
        this.typeHandlerRegistry = typeHandlerRegistry;
        this.methodHandlerRegistry = methodHandlerRegistry;
    }

    public static TypeAnnotationHandlerRegistry createTypeHandlerRegistry() {
        return TypeAnnotationHandlerRegistry.builder()
            .add(new TypeAnnotationHandler())
            .build();
    }

    public static MethodAnnotationHandlerRegistry createMethodHandlerRegistry() {
        return MethodAnnotationHandlerRegistry.builder()
            .add(new GetterAnnotationHandler())
            .add(new SetterAnnotationHandler())
            .add(new MethodAnnotationHandler())
            .add(new ConstructorAnnotationHandler())
            .build();
    }

    @Override
    public <T extends TypeAccessor> T createTypeAccessor(final Class<T> accessorClass) {
        return createTypeAccessor(accessorClass, this.typeHandlerRegistry, this.methodHandlerRegistry);
    }

    public <T extends TypeAccessor> T createTypeAccessor(final Class<T> accessorClass, final TypeAnnotationHandlerRegistry typeHandlerRegistry, final MethodAnnotationHandlerRegistry methodHandlerRegistry) {
        var handler = new TypeAccessorHandler(this, typeHandlerRegistry, methodHandlerRegistry);
        return createAccessor(accessorClass, handler);
    }

    @Override
    public <T extends InstanceAccessor> T createInstanceAccessor(final Class<T> accessorClass, final Object instance) {
        return createInstanceAccessor(accessorClass, instance, this.typeHandlerRegistry, this.methodHandlerRegistry);
    }

    public <T extends InstanceAccessor> T createInstanceAccessor(final Class<T> accessorClass, final Object instance, final TypeAnnotationHandlerRegistry typeHandlerRegistry, final MethodAnnotationHandlerRegistry methodHandlerRegistry) {
        var handler = new InstanceAccessorHandler(this, typeHandlerRegistry, methodHandlerRegistry, instance);
        return createAccessor(accessorClass, handler);
    }

    public <T extends Accessor> T createAccessor(final Class<T> accessorClass, final AccessorHandler handler) {
        return Reflections.newProxyInstance(accessorClass, handler);
    }

    public TypeAnnotationHandlerRegistry getTypeHandlerRegistry() {
        return this.typeHandlerRegistry;
    }

    public MethodAnnotationHandlerRegistry getMethodHandlerRegistry() {
        return this.methodHandlerRegistry;
    }
}

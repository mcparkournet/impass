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

package net.mcparkour.impass.handler.registry.type;

import java.lang.annotation.Annotation;
import java.util.Map;
import net.mcparkour.impass.handler.registry.AnnotationHandlerRegistry;
import net.mcparkour.impass.handler.type.TypeAnnotationHandler;

public class TypeAnnotationHandlerRegistry extends AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> {

    public static TypeAnnotationHandlerRegistryBuilder builder() {
        return new TypeAnnotationHandlerRegistryBuilder();
    }

    public TypeAnnotationHandlerRegistry(final Map<Class<? extends Annotation>, TypeAnnotationHandler<? extends Annotation>> handlers) {
        super(handlers);
    }

    public Class<?> handleType(final Class<?> type) {
        var annotations = type.getAnnotations();
        for (final var annotation : annotations) {
            var annotationType = annotation.annotationType();
            var handler = get(annotationType);
            if (handler != null) {
                return handler.handleRaw(annotation);
            }
        }
        return type;
    }
}

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

package net.mcparkour.impass.handler.registry;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import net.mcparkour.impass.handler.AnnotationHandler;
import net.mcparkour.impass.util.Builder;

public class AnnotationHandlerRegistryBuilder<H extends AnnotationHandler<? extends Annotation>> implements Builder<AnnotationHandlerRegistry<H>> {

	private Map<Class<? extends Annotation>, H> handlers = new HashMap<>(4);

	public AnnotationHandlerRegistryBuilder<H> with(AnnotationHandlerRegistry<H> handlerRegistry) {
		Map<Class<? extends Annotation>, H> handlers = handlerRegistry.getHandlers();
		this.handlers.putAll(handlers);
		return this;
	}

	public AnnotationHandlerRegistryBuilder<H> add(H handler) {
		var annotationType = handler.getAnnotationType();
		this.handlers.put(annotationType, handler);
		return this;
	}

	@Override
	public AnnotationHandlerRegistry<H> build() {
		return new AnnotationHandlerRegistry<>(this.handlers);
	}
}

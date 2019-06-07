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
import java.util.Map;
import net.mcparkour.impass.handler.AnnotationHandler;

public class AnnotationHandlerRegistry<H extends AnnotationHandler<? extends Annotation>> {

	private Map<Class<? extends Annotation>, H> handlers;

	public static <H extends AnnotationHandler<? extends Annotation>> AnnotationHandlerRegistryBuilder<H> builder() {
		return new AnnotationHandlerRegistryBuilder<>();
	}

	public AnnotationHandlerRegistry(Map<Class<? extends Annotation>, H> handlers) {
		this.handlers = handlers;
	}

	public H get(Class<? extends Annotation> annotation) {
		return this.handlers.get(annotation);
	}

	public Map<Class<? extends Annotation>, H> getHandlers() {
		return Map.copyOf(this.handlers);
	}
}

package net.mcparkour.impass.handler.registry.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import net.mcparkour.impass.handler.AnnotationHandlerException;
import net.mcparkour.impass.handler.method.MethodAnnotationHandler;
import net.mcparkour.impass.handler.method.MethodHandler;
import net.mcparkour.impass.handler.registry.AnnotationHandlerRegistry;
import org.jetbrains.annotations.Nullable;

public class MethodAnnotationHandlerRegistry extends AnnotationHandlerRegistry<MethodAnnotationHandler<? extends Annotation>> {

	public static MethodAnnotationHandlerRegistryBuilder builder() {
		return new MethodAnnotationHandlerRegistryBuilder();
	}

	public MethodAnnotationHandlerRegistry(Map<Class<? extends Annotation>, MethodAnnotationHandler<? extends Annotation>> handlers) {
		super(handlers);
	}

	@Nullable
	public Object handleMethod(MethodHandler methodHandler) throws Throwable {
		Method method = methodHandler.getMethod();
		var annotations = method.getAnnotations();
		for (var annotation : annotations) {
			var annotationType = annotation.annotationType();
			var handler = get(annotationType);
			if (handler != null) {
				return handler.handleRaw(annotation, methodHandler);
			}
		}
		throw new AnnotationHandlerException("Member does not have any annotation");
	}
}

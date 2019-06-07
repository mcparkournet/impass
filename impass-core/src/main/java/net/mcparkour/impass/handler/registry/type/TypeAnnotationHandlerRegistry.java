package net.mcparkour.impass.handler.registry.type;

import java.lang.annotation.Annotation;
import java.util.Map;
import net.mcparkour.impass.handler.AnnotationHandlerException;
import net.mcparkour.impass.handler.registry.AnnotationHandlerRegistry;
import net.mcparkour.impass.handler.type.TypeAnnotationHandler;

public class TypeAnnotationHandlerRegistry extends AnnotationHandlerRegistry<TypeAnnotationHandler<? extends Annotation>> {

	public static TypeAnnotationHandlerRegistryBuilder builder() {
		return new TypeAnnotationHandlerRegistryBuilder();
	}

	public TypeAnnotationHandlerRegistry(Map<Class<? extends Annotation>, TypeAnnotationHandler<? extends Annotation>> handlers) {
		super(handlers);
	}

	public Class<?> handleType(Class<?> type) {
		var annotations = type.getAnnotations();
		for (var annotation : annotations) {
			var annotationType = annotation.annotationType();
			var handler = get(annotationType);
			if (handler != null) {
				return handler.handleRaw(annotation);
			}
		}
		throw new AnnotationHandlerException("Member does not have any annotation");
	}
}

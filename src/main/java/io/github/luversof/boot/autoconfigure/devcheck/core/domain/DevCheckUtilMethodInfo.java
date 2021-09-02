package io.github.luversof.boot.autoconfigure.devcheck.core.domain;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotatedElementUtils;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;
import io.github.luversof.boot.autoconfigure.devcheck.core.util.DevCheckUtil;
import lombok.Data;

@Data
public class DevCheckUtilMethodInfo {

	public DevCheckUtilMethodInfo(Method method) {
		this.method = method.getName();
		this.parameterNames = DevCheckUtil.getParameterNames(method);
		this.returnType = method.getReturnType().getSimpleName();

		if (AnnotatedElementUtils.hasAnnotation(method, DevCheckDescription.class)) {
			var mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, DevCheckDescription.class);
			if (mergedAnnotation != null) {
				this.description = mergedAnnotation.value();
			}
		}
	}

	private String description;
	private String returnType;
	private String method;
	private String[] parameterNames;

}

package io.github.luversof.boot.autoconfigure.devcheck.core.domain;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.annotation.AnnotatedElementUtils;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;
import lombok.Data;

@Data
public class DevCheckUtilInfo {

	public DevCheckUtilInfo(Class<?> util) {
		this.methodName = util.getSimpleName();
		this.methodInfoList = new ArrayList<>();
		Arrays.asList(util.getDeclaredMethods()).stream()
			.filter(method -> Modifier.isPublic(method.getModifiers()) && (!AnnotatedElementUtils.hasAnnotation(method, DevCheckDescription.class) || (AnnotatedElementUtils.hasAnnotation(method, DevCheckDescription.class) && AnnotatedElementUtils.findMergedAnnotation(method, DevCheckDescription.class).displayable())))
			.forEach(method -> this.methodInfoList.add(new DevCheckUtilMethodInfo(method)));
	}
	
	private String methodName;
	private List<DevCheckUtilMethodInfo> methodInfoList;

}

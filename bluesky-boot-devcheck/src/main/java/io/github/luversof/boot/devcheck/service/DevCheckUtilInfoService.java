package io.github.luversof.boot.devcheck.service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;

import io.github.luversof.boot.devcheck.annotation.DevCheckDescription;
import io.github.luversof.boot.devcheck.annotation.DevCheckUtil;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilMethodInfo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DevCheckUtilInfoService {

	private final Reflections reflections;
	
	private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	
	public List<DevCheckUtilInfo> getDevCheckUtilInfo() {
		
		Set<Class<?>> targetClassSet = getTargetClassSet();
		
		List<DevCheckUtilInfo> devCheckUtilInfoList = new ArrayList<>();
		targetClassSet.stream().forEach(targetClass -> devCheckUtilInfoList.add(createDevCheckUtilInfo(targetClass)));
		for (Class<?> util : targetClassSet) {
			Method[] declaredMethods = util.getDeclaredMethods();
			for (Method method : declaredMethods) {
				method.getName();
			}
		}
		return devCheckUtilInfoList;
	}
	
	private Set<Class<?>> getTargetClassSet() {
		return reflections.getTypesAnnotatedWith(DevCheckUtil.class);
	}
	
	private DevCheckUtilInfo createDevCheckUtilInfo(Class<?> targetClass) {
		var devCheckUtilMethodInfoList = new ArrayList<DevCheckUtilMethodInfo>();
		Arrays.asList(targetClass.getDeclaredMethods()).stream()
			.filter(method -> 
				Modifier.isPublic(method.getModifiers()) 
				&& (
					!AnnotatedElementUtils.hasAnnotation(method, DevCheckDescription.class)
					|| (AnnotatedElementUtils.hasAnnotation(method, DevCheckDescription.class) && AnnotatedElementUtils.findMergedAnnotation(method, DevCheckDescription.class).displayable())
				)
			)
			.forEach(method -> devCheckUtilMethodInfoList.add(createDevCheckUtilMethodInfo(method)));
		
		return new DevCheckUtilInfo(targetClass.getSimpleName(), devCheckUtilMethodInfoList);
	}
	
	private DevCheckUtilMethodInfo createDevCheckUtilMethodInfo(Method method) {
		String description = null;
		if (AnnotatedElementUtils.hasAnnotation(method, DevCheckDescription.class)) {
			var mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, DevCheckDescription.class);
			if (mergedAnnotation != null) {
				description = mergedAnnotation.value();
			}
		}
		return new DevCheckUtilMethodInfo(
			method.getName(),
			method.getReturnType().getSimpleName(),
			parameterNameDiscoverer.getParameterNames(method),
			description
		);
	}
}

package io.github.luversof.boot.autoconfigure.devcheck.core.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckController;
import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;
import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckUtil;
import io.github.luversof.boot.autoconfigure.devcheck.core.domain.DevCheckInfo;
import io.github.luversof.boot.autoconfigure.devcheck.core.domain.DevCheckUtilInfo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractDevCheckViewController {

	private final ApplicationContext applicationContext;

	private final Reflections reflections;

	private final String pathPrefix;

	protected List<DevCheckInfo> getDevCheckInfoList() {
		Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().entrySet().stream()
				.filter(handlerMapping -> handlerMapping.getValue().getBeanType().isAnnotationPresent(DevCheckController.class)
						&& handlerMapping.getKey().getPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.startsWith("/_check"))
						&& handlerMapping.getKey().getProducesCondition().getExpressions().stream().anyMatch(mediaTypeExpression -> mediaTypeExpression.getMediaType().equals(MediaType.APPLICATION_JSON)))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		List<DevCheckInfo> devCheckInfoList = new ArrayList<>();
		handlerMethodMap.entrySet().forEach(map -> {
			if ((!map.getValue().hasMethodAnnotation(DevCheckDescription.class) || (map.getValue().hasMethodAnnotation(DevCheckDescription.class) && map.getValue().getMethodAnnotation(DevCheckDescription.class).displayable())))
				devCheckInfoList.add(new DevCheckInfo(pathPrefix, map));
		});
		return devCheckInfoList.stream().sorted(Comparator.comparing(DevCheckInfo::getBeanName).thenComparing(devCheckInfo -> devCheckInfo.getUrlList().get(0))).collect(Collectors.toList());
	}

	protected List<DevCheckUtilInfo> getDevCheckUtilInfoList() {
		Set<Class<?>> utilSet = reflections.getTypesAnnotatedWith(DevCheckUtil.class);
		List<DevCheckUtilInfo> devCheckUtilInfoList = new ArrayList<>();
		utilSet.stream().forEach(util -> devCheckUtilInfoList.add(new DevCheckUtilInfo(util)));
		for (Class<?> util : utilSet) {
			Method[] declaredMethods = util.getDeclaredMethods();
			for (Method method : declaredMethods) {
				method.getName();
			}
		}
		return devCheckUtilInfoList;
	}

}

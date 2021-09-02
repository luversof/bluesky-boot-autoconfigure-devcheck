package io.github.luversof.boot.autoconfigure.devcheck.core.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckController;
import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;
import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.ReactiveDevCheckUtil;
import io.github.luversof.boot.autoconfigure.devcheck.core.domain.DevCheckUtilInfo;
import io.github.luversof.boot.autoconfigure.devcheck.core.domain.ReactiveDevCheckInfo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractReactiveDevCheckViewController {

	private final Reflections reflections;

	private final String pathPrefix;

	protected List<ReactiveDevCheckInfo> getDevCheckInfoList(ServerWebExchange exchange) {
		Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = exchange.getApplicationContext().getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class).getHandlerMethods().entrySet().stream()
				.filter(handlerMapping -> handlerMapping.getValue().getBeanType().isAnnotationPresent(DevCheckController.class)
						&& handlerMapping.getKey().getPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.getPatternString().startsWith("/_check"))
						&& handlerMapping.getKey().getProducesCondition().getExpressions().stream().anyMatch(mediaTypeExpression -> mediaTypeExpression.getMediaType().equals(MediaType.APPLICATION_JSON)))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		List<ReactiveDevCheckInfo> devCheckInfoList = new ArrayList<>();
		handlerMethodMap.entrySet().forEach(map -> {
			if (!map.getValue().hasMethodAnnotation(DevCheckDescription.class) || (map.getValue().hasMethodAnnotation(DevCheckDescription.class) && map.getValue().getMethodAnnotation(DevCheckDescription.class).displayable()))
				devCheckInfoList.add(new ReactiveDevCheckInfo(pathPrefix, map));
		});
		return devCheckInfoList.stream().sorted(Comparator.comparing(ReactiveDevCheckInfo::getBeanName).thenComparing(reactiveDevCheckInfo -> reactiveDevCheckInfo.getUrlList().get(0))).collect(Collectors.toList());
	}

	protected List<DevCheckUtilInfo> getDevCheckUtilInfoList() {
		Set<Class<?>> utilSet = reflections.getTypesAnnotatedWith(ReactiveDevCheckUtil.class);
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

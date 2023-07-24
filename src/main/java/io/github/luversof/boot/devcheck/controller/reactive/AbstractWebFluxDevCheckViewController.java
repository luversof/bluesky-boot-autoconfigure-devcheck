package io.github.luversof.boot.devcheck.controller.reactive;

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

import io.github.luversof.boot.autoconfigure.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.annotation.DevCheckController;
import io.github.luversof.boot.devcheck.annotation.DevCheckDescription;
import io.github.luversof.boot.devcheck.annotation.ReactiveDevCheckUtil;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.domain.reactive.WebFluxDevCheckInfo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractWebFluxDevCheckViewController {

	private final Reflections reflections;

	protected String getPathPrefix(ServerWebExchange exchange) {
		return exchange.getRequest().getURI().toString().replace(exchange.getRequest().getPath().contextPath().value(), "").replace("/index", "").replace("/util", "");
	}
	
	protected String getContextPath(ServerWebExchange exchange) {
		return exchange.getRequest().getPath().contextPath().value();
	}

	protected List<WebFluxDevCheckInfo> getDevCheckInfoList(ServerWebExchange exchange) {
		var devCheckCoreProperties = exchange.getApplicationContext().getBean(DevCheckProperties.class);
		
		Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = exchange.getApplicationContext().getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class).getHandlerMethods().entrySet().stream()
				.filter(handlerMapping -> handlerMapping.getValue().getBeanType().isAnnotationPresent(DevCheckController.class)
						&& handlerMapping.getKey().getPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.getPatternString().startsWith(devCheckCoreProperties.getPathPrefix()))
						&& handlerMapping.getKey().getProducesCondition().getExpressions().stream().anyMatch(mediaTypeExpression -> mediaTypeExpression.getMediaType().equals(MediaType.APPLICATION_JSON)))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		List<WebFluxDevCheckInfo> devCheckInfoList = new ArrayList<>();
		handlerMethodMap.entrySet().forEach(map -> {
			if (!map.getValue().hasMethodAnnotation(DevCheckDescription.class) || (map.getValue().hasMethodAnnotation(DevCheckDescription.class) && map.getValue().getMethodAnnotation(DevCheckDescription.class).displayable()))
				devCheckInfoList.add(new WebFluxDevCheckInfo(getContextPath(exchange), getPathPrefix(exchange), map));
		});
		return devCheckInfoList.stream().sorted(Comparator.comparing(WebFluxDevCheckInfo::getBeanName).thenComparing(reactiveDevCheckInfo -> reactiveDevCheckInfo.getUrlList().get(0))).toList();
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

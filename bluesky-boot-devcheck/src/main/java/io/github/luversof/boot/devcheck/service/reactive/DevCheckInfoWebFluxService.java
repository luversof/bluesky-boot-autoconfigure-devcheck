package io.github.luversof.boot.devcheck.service.reactive;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;

import io.github.luversof.boot.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.annotation.DevCheckController;
import io.github.luversof.boot.devcheck.annotation.DevCheckDescription;
import io.github.luversof.boot.devcheck.domain.DevCheckInfo;
import io.github.luversof.boot.devcheck.util.DevCheckUtil;

public class DevCheckInfoWebFluxService {

	private Map<RequestMappingInfo, HandlerMethod> getTargetHandlerMethodMap(ServerWebExchange exchange) {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = exchange.getApplicationContext().getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class).getHandlerMethods();
		
		var devCheckCoreProperties = exchange.getApplicationContext().getBean(DevCheckProperties.class);
		
		return handlerMethods.entrySet().stream()
		.filter(handlerMapping -> handlerMapping.getValue().getBeanType().isAnnotationPresent(DevCheckController.class)
				&& handlerMapping.getKey().getPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.getPatternString().startsWith(devCheckCoreProperties.getPathPrefix()))
				&& handlerMapping.getKey().getProducesCondition().getExpressions().stream().anyMatch(mediaTypeExpression -> mediaTypeExpression.getMediaType().equals(MediaType.APPLICATION_JSON)))
		.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	private DevCheckInfo createDevCheckInfo(ServerWebExchange exchange, Entry<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
		var requestMappingInfo = handlerMethodMap.getKey();
		var handlerMethod = handlerMethodMap.getValue();
		
		String beanName = handlerMethod.getBean().toString();
		List<String> urlList = new ArrayList<>();
		var patternsCondition = requestMappingInfo.getPatternsCondition();
		var contextPath = exchange.getRequest().getPath().contextPath().value();
		var pathPrefix = exchange.getRequest().getURI().toString().replace(exchange.getRequest().getPath().contextPath().value(), "").replace("/index", "").replace("/util", "");
		for (PathPattern pathPattern : patternsCondition.getPatterns()) {
			urlList.add(DevCheckUtil.getUrlWithParameter(contextPath, pathPrefix, pathPattern.getPatternString(), handlerMethod.getMethod()));
		}
		String description = null;
		var methodAnnotation = handlerMethod.getMethodAnnotation(DevCheckDescription.class);
		if (methodAnnotation != null) {
			description = methodAnnotation.value();
		}
		
		return new DevCheckInfo(beanName, urlList, description);
	}
	
	public List<DevCheckInfo> getDevCheckInfoList(ServerWebExchange exchange) {
		var handlerMethodMap = getTargetHandlerMethodMap(exchange);
		
		List<DevCheckInfo> devCheckInfoList = new ArrayList<>();
		
		handlerMethodMap.entrySet().stream()
			.filter(map -> (!map.getValue().hasMethodAnnotation(DevCheckDescription.class) || (map.getValue().hasMethodAnnotation(DevCheckDescription.class) && map.getValue().getMethodAnnotation(DevCheckDescription.class).displayable())))
			.forEach(map -> devCheckInfoList.add(createDevCheckInfo(exchange, map)));
	
		return devCheckInfoList.stream().sorted(Comparator.comparing(DevCheckInfo::beanName).thenComparing(devCheckInfo -> devCheckInfo.urlList().get(0))).toList();
	}
}

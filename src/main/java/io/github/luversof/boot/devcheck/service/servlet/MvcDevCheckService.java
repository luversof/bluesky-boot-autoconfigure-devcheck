package io.github.luversof.boot.devcheck.service.servlet;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import io.github.luversof.boot.autoconfigure.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.annotation.DevCheckController;

public class MvcDevCheckService {
	
	private final ApplicationContext applicationContext;
	
	public MvcDevCheckService(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * get handlerMethod map from RequestMappingHAndlerMapping bean
	 * @return
	 */
	private Map<RequestMappingInfo, HandlerMethod> getHandlerMethodMap() {
		var requestMappingHandlerMappingMap = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
		
		return requestMappingHandlerMappingMap.values().stream()
				.map(RequestMappingHandlerMapping::getHandlerMethods)
				.map(Map::entrySet)
				.flatMap(Collection::stream)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	/**
	 * get target handlerMethod map 
	 * @return
	 */
	private Map<RequestMappingInfo, HandlerMethod> getTargetHandlerMethodMap() {
		var handlerMethodMap = getHandlerMethodMap();
		
		var devCheckCoreProperties = applicationContext.getBean(DevCheckProperties.class);
		
		return handlerMethodMap.entrySet().stream()
		.filter(handlerMapping -> handlerMapping.getValue().getBeanType().isAnnotationPresent(DevCheckController.class)
				&& (
						(handlerMapping.getKey().getPatternsCondition() != null && handlerMapping.getKey().getPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.startsWith(devCheckCoreProperties.getPathPrefix())))
						|| handlerMapping.getKey().getPathPatternsCondition() != null && handlerMapping.getKey().getPathPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.getPatternString().startsWith(devCheckCoreProperties.getPathPrefix()))
						)
				&& handlerMapping.getKey().getProducesCondition().getExpressions().stream().anyMatch(mediaTypeExpression -> mediaTypeExpression.getMediaType().equals(MediaType.APPLICATION_JSON)))
		.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
}

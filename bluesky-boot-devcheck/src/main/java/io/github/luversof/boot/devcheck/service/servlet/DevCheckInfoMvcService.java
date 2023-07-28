package io.github.luversof.boot.devcheck.service.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import io.github.luversof.boot.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.annotation.DevCheckController;
import io.github.luversof.boot.devcheck.annotation.DevCheckDescription;
import io.github.luversof.boot.devcheck.domain.DevCheckInfo;
import io.github.luversof.boot.devcheck.util.DevCheckUtil;

public class DevCheckInfoMvcService implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public List<DevCheckInfo> getDevCheckInfoList() {
		var handlerMethodMap = getTargetHandlerMethodMap();
		
		List<DevCheckInfo> devCheckInfoList = new ArrayList<>();
		
		handlerMethodMap.entrySet().stream()
			.filter(map -> (!map.getValue().hasMethodAnnotation(DevCheckDescription.class) || (map.getValue().hasMethodAnnotation(DevCheckDescription.class) && map.getValue().getMethodAnnotation(DevCheckDescription.class).displayable())))
			.forEach(map -> devCheckInfoList.add(createDevCheckInfo(map)));
		
		return devCheckInfoList.stream().sorted(Comparator.comparing(DevCheckInfo::beanName).thenComparing(devCheckInfo -> devCheckInfo.urlList().get(0))).toList();
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
		var devCheckCoreProperties = applicationContext.getBean(DevCheckProperties.class);
		return getHandlerMethodMap().entrySet().stream()
		.filter(handlerMapping -> handlerMapping.getValue().getBeanType().isAnnotationPresent(DevCheckController.class)
			&& (
				(
					handlerMapping.getKey().getPatternsCondition() != null
					&& handlerMapping.getKey().getPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.startsWith(devCheckCoreProperties.getPathPrefix()))
				)
				|| (
					handlerMapping.getKey().getPathPatternsCondition() != null
					&& handlerMapping.getKey().getPathPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.getPatternString().startsWith(devCheckCoreProperties.getPathPrefix()))
				)
			)
			&& handlerMapping.getKey().getProducesCondition().getExpressions().stream().anyMatch(mediaTypeExpression -> mediaTypeExpression.getMediaType().equals(MediaType.APPLICATION_JSON)))
		.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	private DevCheckInfo createDevCheckInfo(Entry<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
		var requestMappingInfo = handlerMethodMap.getKey();
		var handlerMethod = handlerMethodMap.getValue();
		
		// 구하려고 하는 값 : beanName, urlList, description
		String beanName = handlerMethod.getBean().toString();
		var urlList = new ArrayList<String>();
		String description = null;
		
		var patternsCondition = requestMappingInfo.getPatternsCondition();
		var pathPatternsCondition = requestMappingInfo.getPathPatternsCondition();
		if (patternsCondition == null && pathPatternsCondition == null) {
			Assert.notNull(patternsCondition, "patternsCondition or pathPatternsCondition must not null");
		}
		
		if (patternsCondition != null) {
			for (String url : patternsCondition.getPatterns()) {
				urlList.add(DevCheckUtil.getUrlWithParameter(url, handlerMethodMap.getValue().getMethod()));
			}
		}
		
		if (pathPatternsCondition != null) {
			for (PathPattern pattern : pathPatternsCondition.getPatterns()) {
				urlList.add(DevCheckUtil.getUrlWithParameter(pattern.getPatternString(), handlerMethodMap.getValue().getMethod()));
			}
		}
	
		var methodAnnotation = handlerMethod.getMethodAnnotation(DevCheckDescription.class);
		if (methodAnnotation != null) {
			description = methodAnnotation.value();
		}
		return new DevCheckInfo(beanName, urlList, description);
	}
}

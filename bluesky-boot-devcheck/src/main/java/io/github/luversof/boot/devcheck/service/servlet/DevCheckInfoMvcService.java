package io.github.luversof.boot.devcheck.service.servlet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import io.github.luversof.boot.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.annotation.DevCheckDescription;
import io.github.luversof.boot.devcheck.domain.DevCheckInfo;

public class DevCheckInfoMvcService implements ApplicationContextAware {
	
	List<DevCheckInfo> devCheckInfoList = new ArrayList<>();
	
	private ApplicationContext applicationContext;
	
	private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public List<DevCheckInfo> getDevCheckInfoList() {
		if (!devCheckInfoList.isEmpty()) {
			return devCheckInfoList;
		}
		
		var handlerMethodMap = getTargetHandlerMethodMap();
		handlerMethodMap.entrySet().stream()
			.filter(map -> (!map.getValue().hasMethodAnnotation(DevCheckDescription.class) || (map.getValue().hasMethodAnnotation(DevCheckDescription.class) && map.getValue().getMethodAnnotation(DevCheckDescription.class).displayable())))
			.forEach(map -> devCheckInfoList.add(createDevCheckInfo(map)));
		devCheckInfoList = devCheckInfoList.stream().sorted(Comparator.comparing(DevCheckInfo::beanName).thenComparing(devCheckInfo -> devCheckInfo.urlList().get(0))).toList();
		return devCheckInfoList;
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
		var devCheckProperties = applicationContext.getBean(DevCheckProperties.class);
		if (devCheckProperties.getDevCheckControllerAnnotationList() == null) {
			return Collections.emptyMap();
		}
		return getHandlerMethodMap().entrySet().stream()
		.filter(handlerMapping -> 
			devCheckProperties.getDevCheckControllerAnnotationList().stream().anyMatch(annotation -> handlerMapping.getValue().getBeanType().isAnnotationPresent(annotation))
			&& (
				(
					handlerMapping.getKey().getPatternsCondition() != null
					&& handlerMapping.getKey().getPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.startsWith(devCheckProperties.getPathPrefix()))
				)
				|| (
					handlerMapping.getKey().getPathPatternsCondition() != null
					&& handlerMapping.getKey().getPathPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.getPatternString().startsWith(devCheckProperties.getPathPrefix()))
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
				urlList.add(getUrlWithParameter(url, handlerMethodMap.getValue().getMethod()));
			}
		}
		
		if (pathPatternsCondition != null) {
			for (PathPattern pattern : pathPatternsCondition.getPatterns()) {
				urlList.add(getUrlWithParameter(pattern.getPatternString(), handlerMethodMap.getValue().getMethod()));
			}
		}
	
		var methodAnnotation = handlerMethod.getMethodAnnotation(DevCheckDescription.class);
		if (methodAnnotation != null) {
			description = methodAnnotation.value();
		}
		return new DevCheckInfo(beanName, urlList, description);
	}
	
	public String getUrlWithParameter(String pattern, Method method) {
		var stringBuilder = new StringBuilder();
		stringBuilder.append(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getContextPath());
		stringBuilder.append(pattern);
		
		var parameterNames = parameterNameDiscoverer.getParameterNames(method);
		if (parameterNames != null && parameterNames.length > 0) {
			for (int i = 0; i < parameterNames.length; i++) {
				if (method.getParameters()[i].isAnnotationPresent(PathVariable.class)) {
					continue;
				}
				stringBuilder.append(i == 0 ? "?" : "&").append(parameterNames[i]).append("={").append(parameterNames[i]).append("}");
			}
		}
		
		return stringBuilder.toString().replace("//", "/");
	}
}

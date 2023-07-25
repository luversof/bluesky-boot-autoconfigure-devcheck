package io.github.luversof.boot.devcheck.controller.servlet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import io.github.luversof.boot.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.annotation.DevCheckController;
import io.github.luversof.boot.devcheck.annotation.DevCheckDescription;
import io.github.luversof.boot.devcheck.annotation.DevCheckUtil;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.domain.servlet.MvcDevCheckInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractMvcDevCheckViewController {

	private final ApplicationContext applicationContext;

	private final Reflections reflections;
	
	protected String getPathPrefix() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getRequestURI().replace(request.getContextPath(), "").replace("/index", "").replace("/util", "");
	}
	
	protected String getContextPath() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getContextPath();
	}

	protected List<MvcDevCheckInfo> getDevCheckInfoList() {
		var requestMappingHandlerMappingMap = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
		
		Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = new HashMap<>();
		
		requestMappingHandlerMappingMap.forEach((key, requestMappingHandlerMapping) ->
			handlerMethodMap.putAll(getHandlerMethodMap(requestMappingHandlerMapping))	
		);

		List<MvcDevCheckInfo> devCheckInfoList = new ArrayList<>();
		handlerMethodMap.entrySet().forEach(map -> {
			if ((!map.getValue().hasMethodAnnotation(DevCheckDescription.class) || (map.getValue().hasMethodAnnotation(DevCheckDescription.class) && map.getValue().getMethodAnnotation(DevCheckDescription.class).displayable())))
				devCheckInfoList.add(new MvcDevCheckInfo(getContextPath(), getPathPrefix(), map));
		});
		return devCheckInfoList.stream().sorted(Comparator.comparing(MvcDevCheckInfo::getBeanName).thenComparing(devCheckInfo -> devCheckInfo.getUrlList().get(0))).toList();
	}
	
	private Map<RequestMappingInfo, HandlerMethod> getHandlerMethodMap(RequestMappingHandlerMapping requestMappingHandlerMapping) {
		var devCheckCoreProperties = applicationContext.getBean(DevCheckProperties.class);
		
		return requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
				.filter(handlerMapping -> handlerMapping.getValue().getBeanType().isAnnotationPresent(DevCheckController.class)
						&& ((handlerMapping.getKey().getPatternsCondition() != null && handlerMapping.getKey().getPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.startsWith(devCheckCoreProperties.getPathPrefix())))
								|| handlerMapping.getKey().getPathPatternsCondition() != null && handlerMapping.getKey().getPathPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.getPatternString().startsWith(devCheckCoreProperties.getPathPrefix())))
						&& handlerMapping.getKey().getProducesCondition().getExpressions().stream().anyMatch(mediaTypeExpression -> mediaTypeExpression.getMediaType().equals(MediaType.APPLICATION_JSON)))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
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

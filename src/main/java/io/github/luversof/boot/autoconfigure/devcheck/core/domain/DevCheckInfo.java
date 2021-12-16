package io.github.luversof.boot.autoconfigure.devcheck.core.domain;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;
import io.github.luversof.boot.autoconfigure.devcheck.core.util.DevCheckUtil;

public class DevCheckInfo extends AbstractDevCheckInfo<RequestMappingInfo> {

	public DevCheckInfo(String requestPath, Entry<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
		setBeanName(handlerMethodMap.getValue().getBean().toString());
		setUrlList(new ArrayList<>());
		var patternsCondition = handlerMethodMap.getKey().getPatternsCondition();
		var pathPatternsCondition = handlerMethodMap.getKey().getPathPatternsCondition();
		
		if (patternsCondition == null && pathPatternsCondition == null) {
			Assert.notNull(patternsCondition, "patternsCondition or pathPatternsCondition must not null");
		}
		
		if (patternsCondition != null) {
			for (String url : patternsCondition.getPatterns()) {
				getUrlList().add(DevCheckUtil.getUrlWithParameter(requestPath, url, handlerMethodMap.getValue().getMethod()));
			}
		} else if (pathPatternsCondition != null) {
			for (PathPattern pattern : pathPatternsCondition.getPatterns()) {
				getUrlList().add(DevCheckUtil.getUrlWithParameter(requestPath, pattern.getPatternString(), handlerMethodMap.getValue().getMethod()));
			}
		}
		setHandlerMethodMap(handlerMethodMap);
		var methodAnnotation = handlerMethodMap.getValue().getMethodAnnotation(DevCheckDescription.class);
		if (methodAnnotation != null) {
			setDescription(methodAnnotation.value());
		}
	}

}

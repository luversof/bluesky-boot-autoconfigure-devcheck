package io.github.luversof.boot.autoconfigure.devcheck.core.domain;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPattern;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;
import io.github.luversof.boot.autoconfigure.devcheck.core.util.DevCheckUtil;

public class ReactiveDevCheckInfo extends AbstractDevCheckInfo<RequestMappingInfo> {

	public ReactiveDevCheckInfo(String pathPrefix, Entry<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
		setBeanName(handlerMethodMap.getValue().getBean().toString());
		setUrlList(new ArrayList<>());
		var patternsCondition = handlerMethodMap.getKey().getPatternsCondition();
		if (patternsCondition != null) {
			for (PathPattern pathPattern : patternsCondition.getPatterns()) {
				getUrlList().add(DevCheckUtil.getUrlWithParameter(pathPrefix, pathPattern.getPatternString(), handlerMethodMap.getValue().getMethod()));
			}
		}
		setHandlerMethodMap(handlerMethodMap);
		var methodAnnotation = handlerMethodMap.getValue().getMethodAnnotation(DevCheckDescription.class);
		if (methodAnnotation != null) {
			setDescription(methodAnnotation.value());
		}
	}

}
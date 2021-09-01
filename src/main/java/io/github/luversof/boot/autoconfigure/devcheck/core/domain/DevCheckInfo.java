package io.github.luversof.boot.autoconfigure.devcheck.core.domain;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;
import io.github.luversof.boot.autoconfigure.devcheck.core.util.DevCheckUtil;

public class DevCheckInfo extends AbstractDevCheckInfo<RequestMappingInfo> {

	public DevCheckInfo(String pathPrefix, Entry<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
		setBeanName(handlerMethodMap.getValue().getBean().toString());
		setUrlList(new ArrayList<>());
		var patternsCondition = handlerMethodMap.getKey().getPatternsCondition();
		if (patternsCondition != null) {
			for (String url : patternsCondition.getPatterns()) {
				getUrlList().add(DevCheckUtil.getUrlWithParameter(pathPrefix, url, handlerMethodMap.getValue().getMethod()));
			}
		}
		setHandlerMethodMap(handlerMethodMap);
		var methodAnnotation = handlerMethodMap.getValue().getMethodAnnotation(DevCheckDescription.class);
		if (methodAnnotation != null) {
			setDescription(methodAnnotation.value());
		}
	}

}

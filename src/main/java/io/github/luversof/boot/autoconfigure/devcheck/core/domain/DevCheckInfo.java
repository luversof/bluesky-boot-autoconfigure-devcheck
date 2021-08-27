package io.github.luversof.boot.autoconfigure.devcheck.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;
import io.github.luversof.boot.autoconfigure.devcheck.core.util.DevCheckUtil;
import lombok.Data;

@Data
public class DevCheckInfo {
	
	public DevCheckInfo(String pathPrefix, Entry<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
		this.beanName = handlerMethodMap.getValue().getBean().toString()/* .replace("DevCheckController", "") */;
		this.urlList = new ArrayList<>();
		var patternsCondition = handlerMethodMap.getKey().getPatternsCondition();
		if (patternsCondition != null) {
			for (String url : patternsCondition.getPatterns()) {
				urlList.add(DevCheckUtil.getUrlWithParameter(pathPrefix, url, handlerMethodMap.getValue().getMethod()));
			}
		}
		this.handlerMethodMap = handlerMethodMap;
		var methodAnnotation = handlerMethodMap.getValue().getMethodAnnotation(DevCheckDescription.class);
		if (methodAnnotation != null)	{
			this.description = methodAnnotation.value();
		}
	}
	
	private String beanName;
	private List<String> urlList;
	private String description;
	@JsonIgnore
	private Entry<RequestMappingInfo, HandlerMethod> handlerMethodMap;

}

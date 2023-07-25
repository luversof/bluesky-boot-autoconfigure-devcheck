package io.github.luversof.boot.devcheck.domain;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.web.method.HandlerMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public abstract class AbstractDevCheckInfo<T> {

	private String beanName;
	private List<String> urlList;
	private String description;
	@JsonIgnore
	private Entry<T, HandlerMethod> handlerMethodMap;

}

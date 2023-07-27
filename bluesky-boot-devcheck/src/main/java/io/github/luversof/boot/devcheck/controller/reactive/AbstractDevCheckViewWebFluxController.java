package io.github.luversof.boot.devcheck.controller.reactive;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.web.server.ServerWebExchange;

import io.github.luversof.boot.devcheck.annotation.ReactiveDevCheckUtil;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.service.reactive.DevCheckInfoWebFluxService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractDevCheckViewWebFluxController {
	
	protected final DevCheckInfoWebFluxService devCheckInfoWebFluxService;

	private final Reflections reflections;

	protected String getPathPrefix(ServerWebExchange exchange) {
		return exchange.getRequest().getURI().toString().replace(exchange.getRequest().getPath().contextPath().value(), "").replace("/index", "").replace("/util", "");
	}
	
	protected String getContextPath(ServerWebExchange exchange) {
		return exchange.getRequest().getPath().contextPath().value();
	}

	protected List<DevCheckUtilInfo> getDevCheckUtilInfoList() {
		Set<Class<?>> utilSet = reflections.getTypesAnnotatedWith(ReactiveDevCheckUtil.class);
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

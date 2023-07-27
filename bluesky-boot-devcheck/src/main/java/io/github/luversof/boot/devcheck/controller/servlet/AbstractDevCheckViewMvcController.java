package io.github.luversof.boot.devcheck.controller.servlet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.github.luversof.boot.devcheck.annotation.DevCheckUtil;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.service.servlet.DevCheckInfoMvcService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractDevCheckViewMvcController {
	
	protected final DevCheckInfoMvcService devCheckInfoMvcService;
	
	private final Reflections reflections;
	
	protected String getPathPrefix() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getRequestURI().replace(request.getContextPath(), "").replace("/index", "").replace("/util", "");
	}
	
	protected String getContextPath() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getContextPath();
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

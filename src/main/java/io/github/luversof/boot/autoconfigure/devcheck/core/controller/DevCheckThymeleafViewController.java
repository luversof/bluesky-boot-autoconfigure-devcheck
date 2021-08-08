package io.github.luversof.boot.autoconfigure.devcheck.core.controller;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;
import io.github.luversof.boot.autoconfigure.devcheck.core.util.DevCheckUtil;
import lombok.Data;

@Controller
@RequestMapping(value = "/_check")
public class DevCheckThymeleafViewController {

	private final ApplicationContext applicationContext;
	
	private final Reflections reflections;
	
	private final String pathPrefix;
	
	public DevCheckThymeleafViewController(ApplicationContext applicationContext, Reflections reflections, String pathPrefix) {
		this.applicationContext = applicationContext;
		this.reflections = reflections;
		this.pathPrefix = pathPrefix;
	}
	
	@GetMapping({"", "/index"})
	public String index(ModelMap modelMap) {
		
		Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().entrySet().stream()
				.filter(handlerMapping -> 
					handlerMapping.getValue().getBean().toString().toLowerCase().contains("devcheckcontroller")
						&& handlerMapping.getKey().getPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.startsWith("/_check")) 
						&& handlerMapping.getKey().getProducesCondition().getExpressions().stream().anyMatch(mediaTypeExpression -> mediaTypeExpression.getMediaType().equals(MediaType.APPLICATION_JSON)))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		
		List<DevCheckInfo> devCheckInfoList = new ArrayList<>();
		handlerMethodMap.entrySet().forEach(map -> {
			if ((!map.getValue().hasMethodAnnotation(DevCheckDescription.class) || (map.getValue().hasMethodAnnotation(DevCheckDescription.class) && map.getValue().getMethodAnnotation(DevCheckDescription.class).displayable())))
				devCheckInfoList.add(new DevCheckInfo(pathPrefix, map));
			});
		modelMap.addAttribute("devCheckInfoList", devCheckInfoList.stream().sorted(Comparator.comparing(DevCheckInfo::getBeanName).thenComparing(devCheckInfo-> devCheckInfo.getUrlList().get(0))).collect(Collectors.toList()));
		return "_check/thymeleaf/index";
	}
	
	
	@Data
	public static class DevCheckInfo {
		
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
		Entry<RequestMappingInfo, HandlerMethod> handlerMethodMap;
	}
	
	@GetMapping("/util")
	public String util(ModelMap modelMap) {
		Set<Class<?>> utilSet = reflections.getTypesAnnotatedWith(io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckUtil.class);
		modelMap.addAttribute("utilSet", utilSet);
		
		List<DevCheckUtilInfo> devCheckUtilInfoList = new ArrayList<>();
		utilSet.stream().forEach(util -> devCheckUtilInfoList.add(new DevCheckUtilInfo(util)));
		modelMap.addAttribute("devCheckUtilInfoList", devCheckUtilInfoList);
		for (Class<?> util : utilSet) {
			Method[] declaredMethods = util.getDeclaredMethods();
			
			for (Method method : declaredMethods) {
				method.getName();
			}
			
		}
		return "_check/thymeleaf/util";
	}
	
	@Data
	public static class DevCheckUtilInfo {
		
		public DevCheckUtilInfo(Class<?> util) {
			this.methodName = util.getSimpleName();
			this.methodInfoList = new ArrayList<>();
			Arrays.asList(util.getDeclaredMethods()).stream()
				.filter(method -> Modifier.isPublic(method.getModifiers()) && (!AnnotatedElementUtils.hasAnnotation(method, DevCheckDescription.class) || (AnnotatedElementUtils.hasAnnotation(method, DevCheckDescription.class) && AnnotatedElementUtils.findMergedAnnotation(method, DevCheckDescription.class).displayable())))
				.forEach(method -> this.methodInfoList.add(new DevCheckUtilMethodInfo(method)));
		}
		
		private String methodName;
		private List<DevCheckUtilMethodInfo> methodInfoList;
	}
	
	@Data
	public static class DevCheckUtilMethodInfo {
		
		public DevCheckUtilMethodInfo(Method method) {
			this.method = method.getName();
			this.parameterNames = DevCheckUtil.getParameterNames(method);
			this.returnType =  method.getReturnType().getSimpleName(); 
			
			if (AnnotatedElementUtils.hasAnnotation(method, DevCheckDescription.class)) {
				var mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, DevCheckDescription.class);
				if (mergedAnnotation != null) {
					this.description = mergedAnnotation.value();
				}
			}
		}
		private String description;
		private String returnType;
		private String method;
		private String[] parameterNames;
		
	}

}

package io.github.luversof.boot.autoconfigure.devcheck.core.util;

import java.lang.reflect.Method;

import org.reflections.Reflections;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.luversof.boot.autoconfigure.devcheck.core.config.DevCheckCoreProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * _check 페이지 구성을 위헤 제공하는 유틸
 * 
 * @author bluesky
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DevCheckUtil {

	private static final DefaultParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();

	public static String[] getParameterNames(Method method) {
		return DISCOVERER.getParameterNames(method);
	}

	/**
	 * In the case of the test url address, if requestPath is set, the corresponding
	 * path is added.
	 * 
	 * @param pathPrefix prefix before path
	 * @param url        target url
	 * @param method     target method
	 * @return url value including parameters
	 */
	public static String getUrlWithParameter(String pathPrefix, String url, Method method) {
		var stringBuilder = new StringBuilder();
		if (StringUtils.hasText(pathPrefix)) {
			stringBuilder.append(pathPrefix);
		}
		stringBuilder.append(url);
		appendParameter(stringBuilder, method);
		return stringBuilder.toString().replace("//", "/");
	}

	private static void appendParameter(StringBuilder stringBuilder, Method method) {

		var parameterNames = getParameterNames(method);
		if (parameterNames == null || parameterNames.length == 0) {
			return;
		}

		for (int i = 0; i < parameterNames.length; i++) {
			if (method.getParameters()[i].isAnnotationPresent(PathVariable.class)) {
				continue;
			}
			stringBuilder.append(i == 0 ? "?" : "&").append(parameterNames[i]).append("={").append(parameterNames[i]).append("}");
		}
	}
	
	public static Reflections getReflections(DevCheckCoreProperties devCheckCoreProperties) {
		if (devCheckCoreProperties.getBasePackages() == null) {
			return new Reflections("io.github.luversof");
		}
		return new Reflections("io.github.luversof", devCheckCoreProperties.getBasePackages());
	}

}

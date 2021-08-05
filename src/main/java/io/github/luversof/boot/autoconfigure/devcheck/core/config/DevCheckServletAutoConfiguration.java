package io.github.luversof.boot.autoconfigure.devcheck.core.config;

import javax.servlet.Servlet;

import org.reflections.Reflections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import io.github.luversof.boot.autoconfigure.devcheck.core.controller.DevCheckViewController;

@Configuration(value = "_blueskyDevCheckServletAutoConfiguration", proxyBeanMethods = false)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(prefix = "bluesky-modules.dev-check", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DevCheckServletAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(name = "org.thymeleaf.spring5.view.ThymeleafViewResolver")
	public DevCheckViewController blueskyBootDevCheckViewController(ApplicationContext applicationContext) {
		Reflections reflections = new Reflections();
		String pathPrefix = "/";
		return new DevCheckViewController(reflections, pathPrefix);
	}
}

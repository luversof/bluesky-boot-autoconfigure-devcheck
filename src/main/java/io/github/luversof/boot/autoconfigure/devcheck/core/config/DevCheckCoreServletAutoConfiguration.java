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

import io.github.luversof.boot.autoconfigure.devcheck.core.controller.CoreDevCheckController;
import io.github.luversof.boot.autoconfigure.devcheck.core.controller.JsonDevCheckViewController;
import io.github.luversof.boot.autoconfigure.devcheck.core.controller.ThymeleafDevCheckViewController;

@Configuration(value = "_blueskyBootDevCheckCoreServletAutoConfiguration", proxyBeanMethods = false)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true")
public class DevCheckCoreServletAutoConfiguration {
	
	@Bean
	@ConditionalOnClass(name = "org.thymeleaf.spring5.view.ThymeleafViewResolver")
	public ThymeleafDevCheckViewController blueskyBootThymeleafDevCheckViewController(ApplicationContext applicationContext, DevCheckCoreProperties devCheckProperties) {
		Reflections reflections = new Reflections("io.github.luversof", devCheckProperties.getBasePackages());
		String pathPrefix = "/";
		return new ThymeleafDevCheckViewController(applicationContext, reflections, pathPrefix);
	}
	
	@Bean
	@ConditionalOnMissingBean(name = "blueskyBootThymeleafDevCheckViewController")
	public JsonDevCheckViewController blueskyBootJsonDevCheckViewController(ApplicationContext applicationContext, DevCheckCoreProperties devCheckProperties) {
		Reflections reflections = new Reflections("io.github.luversof", devCheckProperties.getBasePackages());
		String pathPrefix = "/";
		return new JsonDevCheckViewController(applicationContext, reflections, pathPrefix);
	}
	
	@Bean
	public CoreDevCheckController blueskyBootCoreDevCheckController(ApplicationContext applicationContext) {
		return new CoreDevCheckController(applicationContext);
	}
	
}

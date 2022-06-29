package io.github.luversof.boot.autoconfigure.devcheck.core.config;

import org.reflections.Reflections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import io.github.luversof.boot.autoconfigure.devcheck.core.controller.DevCheckCoreController;
import io.github.luversof.boot.autoconfigure.devcheck.core.controller.JsonReactiveDevCheckViewController;
import io.github.luversof.boot.autoconfigure.devcheck.core.controller.ThymeleafReactiveDevCheckViewController;
import io.github.luversof.boot.autoconfigure.devcheck.core.util.DevCheckUtil;

@Configuration(value = "_blueskyBootDevCheckCoreReactiveAutoConfiguration", proxyBeanMethods = false)
@ConditionalOnClass({ WebFluxConfigurer.class })
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true")
public class DevCheckCoreReactiveAutoConfiguration {

	@Bean
	@ConditionalOnClass(name = "org.thymeleaf.spring5.view.ThymeleafViewResolver")
	public ThymeleafReactiveDevCheckViewController blueskyBootThymeleafReactiveDevCheckViewController(DevCheckCoreProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new ThymeleafReactiveDevCheckViewController(reflections);
	}

	@Bean
	@ConditionalOnMissingBean(name = "blueskyBootThymeleafReactiveDevCheckViewController")
	public JsonReactiveDevCheckViewController blueskyBootJsonReactiveDevCheckViewController(DevCheckCoreProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new JsonReactiveDevCheckViewController(reflections);
	}

	@Bean
	public DevCheckCoreController blueskyBootDevCheckCoreController(ApplicationContext applicationContext) {
		return new DevCheckCoreController(applicationContext);
	}

}

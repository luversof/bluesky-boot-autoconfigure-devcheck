package io.github.luversof.boot.autoconfigure.devcheck.reactive;

import org.reflections.Reflections;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import io.github.luversof.boot.autoconfigure.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.controller.DevCheckCoreController;
import io.github.luversof.boot.devcheck.controller.reactive.JsonWebFluxDevCheckViewController;
import io.github.luversof.boot.devcheck.controller.reactive.ThymeleafWebFluxDevCheckViewController;
import io.github.luversof.boot.devcheck.util.DevCheckUtil;

@AutoConfiguration("_blueskyBootDevCheckCoreReactiveAutoConfiguration")
@ConditionalOnClass({ WebFluxConfigurer.class })
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DevCheckWebFluxAutoConfiguration {

    @Bean
    @ConditionalOnClass(name = "org.thymeleaf.spring5.view.ThymeleafViewResolver")
    ThymeleafWebFluxDevCheckViewController blueskyBootThymeleafReactiveDevCheckViewController(DevCheckProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new ThymeleafWebFluxDevCheckViewController(reflections);
	}

    @Bean
    @ConditionalOnMissingBean(name = "blueskyBootThymeleafReactiveDevCheckViewController")
    JsonWebFluxDevCheckViewController blueskyBootJsonReactiveDevCheckViewController(DevCheckProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new JsonWebFluxDevCheckViewController(reflections);
	}

    @Bean
    DevCheckCoreController blueskyBootDevCheckCoreController(ApplicationContext applicationContext) {
		return new DevCheckCoreController(applicationContext);
	}

}

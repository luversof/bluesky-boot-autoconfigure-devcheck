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

import io.github.luversof.boot.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.controller.DevCheckCoreController;
import io.github.luversof.boot.devcheck.controller.reactive.JsonDevCheckViewWebFluxController;
import io.github.luversof.boot.devcheck.controller.reactive.ThymeleafDevCheckViewWebFluxController;
import io.github.luversof.boot.devcheck.service.reactive.DevCheckInfoWebFluxService;
import io.github.luversof.boot.devcheck.util.DevCheckUtil;

@AutoConfiguration("_blueskyBootDevCheckCoreReactiveAutoConfiguration")
@ConditionalOnClass({ WebFluxConfigurer.class })
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DevCheckWebFluxAutoConfiguration {
	
	@Bean
	DevCheckInfoWebFluxService devCheckInfoWebFluxService() {
		return new DevCheckInfoWebFluxService();
	}

    @Bean
    @ConditionalOnClass(name = "org.thymeleaf.spring6.view.ThymeleafViewResolver")
    ThymeleafDevCheckViewWebFluxController blueskyBootThymeleafDevCheckViewWebFluxController(DevCheckInfoWebFluxService devCheckInfoWebFluxService, DevCheckProperties devCheckProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckProperties);
		return new ThymeleafDevCheckViewWebFluxController(devCheckInfoWebFluxService, reflections);
	}

    @Bean
    @ConditionalOnMissingBean(name = "blueskyBootThymeleafDevCheckViewWebFluxController")
    JsonDevCheckViewWebFluxController blueskyBootJsonDevCheckViewWebFluxController(DevCheckInfoWebFluxService devCheckInfoWebFluxService, DevCheckProperties devCheckProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckProperties);
		return new JsonDevCheckViewWebFluxController(devCheckInfoWebFluxService, reflections);
	}

    @Bean
    DevCheckCoreController blueskyBootDevCheckCoreController(ApplicationContext applicationContext) {
		return new DevCheckCoreController(applicationContext);
	}

}

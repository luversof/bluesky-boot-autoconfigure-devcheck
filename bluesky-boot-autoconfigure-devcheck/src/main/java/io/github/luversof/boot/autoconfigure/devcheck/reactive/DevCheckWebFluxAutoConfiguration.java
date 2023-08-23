package io.github.luversof.boot.autoconfigure.devcheck.reactive;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import io.github.luversof.boot.devcheck.controller.DevCheckCoreController;
import io.github.luversof.boot.devcheck.controller.DevCheckViewController;
import io.github.luversof.boot.devcheck.controller.reactive.DevCheckApiWebFluxController;
import io.github.luversof.boot.devcheck.service.DevCheckUtilInfoService;
import io.github.luversof.boot.devcheck.service.reactive.DevCheckInfoWebFluxService;

@AutoConfiguration
@ConditionalOnClass({ WebFluxConfigurer.class })
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DevCheckWebFluxAutoConfiguration {
	
	@Bean
	DevCheckInfoWebFluxService blueskyBootDevCheckInfoWebFluxService() {
		return new DevCheckInfoWebFluxService();
	}

    @Bean
    DevCheckApiWebFluxController blueskyBootDevCheckApiWebFluxController(DevCheckInfoWebFluxService devCheckInfoWebFluxService, DevCheckUtilInfoService devCheckUtilInfoService) {
		return new DevCheckApiWebFluxController(devCheckInfoWebFluxService, devCheckUtilInfoService);
	}
    
	@Bean
	DevCheckViewController blueskyBootDevCheckViewController() {
		return new DevCheckViewController();
	}

    @Bean
    DevCheckCoreController blueskyBootDevCheckCoreController(ApplicationContext applicationContext) {
		return new DevCheckCoreController(applicationContext);
	}

}

package io.github.luversof.boot.autoconfigure.devcheck.servlet;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.luversof.boot.devcheck.controller.DevCheckCoreController;
import io.github.luversof.boot.devcheck.controller.DevCheckViewController;
import io.github.luversof.boot.devcheck.controller.servlet.DevCheckApiMvcController;
import io.github.luversof.boot.devcheck.service.DevCheckUtilInfoService;
import io.github.luversof.boot.devcheck.service.servlet.DevCheckInfoMvcService;
import jakarta.servlet.Servlet;

@AutoConfiguration
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DevCheckMvcAutoConfiguration implements WebMvcConfigurer {
	
	@Bean
	DevCheckInfoMvcService blueskyBootDevCheckInfoMvcService() {
		return new DevCheckInfoMvcService();
	}

    @Bean
    DevCheckApiMvcController blueskyBootDevCheckApiMvcController(DevCheckInfoMvcService devCheckInfoMvcService, DevCheckUtilInfoService devCheckUtilInfoService) {
		return new DevCheckApiMvcController(devCheckInfoMvcService, devCheckUtilInfoService);
	}
    
	@Bean
	DevCheckViewController blueskyBootDevCheckViewController() {
		return new DevCheckViewController();
	}

    @Bean
    DevCheckCoreController blueskyBootDevCheckCoreController(ApplicationContext applicationContext) {
		return new DevCheckCoreController(applicationContext);
	}
    
    @Value("${bluesky-boot.dev-check.path-prefix}")
    private String pathPrefix;
    
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController(pathPrefix, pathPrefix + "/index");
	}
    
}

package io.github.luversof.boot.autoconfigure.devcheck.servlet;


import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.luversof.boot.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.controller.DevCheckCoreController;
import io.github.luversof.boot.devcheck.controller.servlet.JsonDevCheckViewMvcController;
import io.github.luversof.boot.devcheck.controller.servlet.ThymeleafDevCheckViewMvcController;
import io.github.luversof.boot.devcheck.service.servlet.DevCheckInfoMvcService;
import io.github.luversof.boot.devcheck.util.DevCheckUtil;
import jakarta.servlet.Servlet;

@AutoConfiguration("_blueskyBootDevCheckCoreServletAutoConfiguration")
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DevCheckMvcAutoConfiguration implements WebMvcConfigurer {
	
	@Bean
	DevCheckInfoMvcService devCheckInfoMvcService() {
		return new DevCheckInfoMvcService();
	}

    @Bean
    @ConditionalOnClass(name = "org.thymeleaf.spring6.view.ThymeleafViewResolver")
    ThymeleafDevCheckViewMvcController blueskyBootThymeleafDevCheckViewMvcController(DevCheckInfoMvcService devCheckInfoMvcService, DevCheckProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new ThymeleafDevCheckViewMvcController(devCheckInfoMvcService, reflections);
	}

    @Bean
    @ConditionalOnMissingBean(name = "blueskyBootThymeleafDevCheckViewMvcController")
    JsonDevCheckViewMvcController blueskyBootJsonDevCheckViewMvcController(DevCheckInfoMvcService devCheckInfoMvcService, DevCheckProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new JsonDevCheckViewMvcController(devCheckInfoMvcService, reflections);
	}

    @Bean
    DevCheckCoreController blueskyBootDevCheckCoreController(ApplicationContext applicationContext) {
		return new DevCheckCoreController(applicationContext);
	}
    
    @Value("${bluesky-boot.dev-check.path-prefix}")
    private String pathPrefix;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController(pathPrefix + "/devCheck").setViewName("/_check/devCheck.html");
	}
    

    
}

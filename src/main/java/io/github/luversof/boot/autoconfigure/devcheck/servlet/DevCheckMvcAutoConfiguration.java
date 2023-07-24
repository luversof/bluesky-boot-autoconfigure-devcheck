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

import io.github.luversof.boot.autoconfigure.devcheck.DevCheckProperties;
import io.github.luversof.boot.devcheck.controller.DevCheckCoreController;
import io.github.luversof.boot.devcheck.controller.servlet.JsonMvcDevCheckViewController;
import io.github.luversof.boot.devcheck.controller.servlet.ThymeleafMvcDevCheckViewController;
import io.github.luversof.boot.devcheck.util.DevCheckUtil;
import jakarta.servlet.Servlet;

@AutoConfiguration("_blueskyBootDevCheckCoreServletAutoConfiguration")
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DevCheckMvcAutoConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnClass(name = "org.thymeleaf.spring5.view.ThymeleafViewResolver")
    ThymeleafMvcDevCheckViewController blueskyBootThymeleafDevCheckViewController(ApplicationContext applicationContext, DevCheckProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new ThymeleafMvcDevCheckViewController(applicationContext, reflections);
	}

    @Bean
    @ConditionalOnMissingBean(name = "blueskyBootThymeleafDevCheckViewController")
    JsonMvcDevCheckViewController blueskyBootJsonDevCheckViewController(ApplicationContext applicationContext, DevCheckProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new JsonMvcDevCheckViewController(applicationContext, reflections);
	}

    @Bean
    DevCheckCoreController blueskyBootDevCheckCoreController(ApplicationContext applicationContext) {
		return new DevCheckCoreController(applicationContext);
	}
    
    @Value("${bluesky-boot.dev-check.path-prefix}")
    private String pathPrefix;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController(pathPrefix + "/devCheck").setViewName("/devCheck.html");
	}
    

    
}

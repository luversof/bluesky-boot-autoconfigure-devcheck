package io.github.luversof.boot.autoconfigure.devcheck.servlet;


import org.reflections.Reflections;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

import io.github.luversof.boot.autoconfigure.devcheck.DevCheckCoreProperties;
import io.github.luversof.boot.devcheck.controller.DevCheckCoreController;
import io.github.luversof.boot.devcheck.controller.servlet.JsonMvcDevCheckViewController;
import io.github.luversof.boot.devcheck.controller.servlet.ThymeleafMvcDevCheckViewController;
import io.github.luversof.boot.devcheck.util.DevCheckUtil;
import jakarta.servlet.Servlet;

@AutoConfiguration("_blueskyBootDevCheckCoreServletAutoConfiguration")
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DevCheckCoreMvcAutoConfiguration {

    @Bean
    @ConditionalOnClass(name = "org.thymeleaf.spring5.view.ThymeleafViewResolver")
    ThymeleafMvcDevCheckViewController blueskyBootThymeleafDevCheckViewController(ApplicationContext applicationContext, DevCheckCoreProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new ThymeleafMvcDevCheckViewController(applicationContext, reflections);
	}

    @Bean
    @ConditionalOnMissingBean(name = "blueskyBootThymeleafDevCheckViewController")
    JsonMvcDevCheckViewController blueskyBootJsonDevCheckViewController(ApplicationContext applicationContext, DevCheckCoreProperties devCheckCoreProperties) {
		Reflections reflections = DevCheckUtil.getReflections(devCheckCoreProperties);
		return new JsonMvcDevCheckViewController(applicationContext, reflections);
	}

    @Bean
    DevCheckCoreController blueskyBootDevCheckCoreController(ApplicationContext applicationContext) {
		return new DevCheckCoreController(applicationContext);
	}

}

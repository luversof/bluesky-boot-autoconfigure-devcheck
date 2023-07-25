package io.github.luversof.boot.autoconfigure.devcheck.core;

import static io.github.luversof.boot.autoconfigure.devcheck.AutoConfigurationTestInfo.BASE_PROPERTY;
import static io.github.luversof.boot.autoconfigure.devcheck.AutoConfigurationTestInfo.DEVCHECK_CORE_SERVLET_USER_CONFIGURATION;
import static io.github.luversof.boot.autoconfigure.devcheck.AutoConfigurationTestInfo.DEVCHECK_SERVLET_CONFIGURATION;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import io.github.luversof.boot.devcheck.controller.DevCheckCoreController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class DevCheckCoreAutoServletConfigurationTests {
	
	private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
			.withInitializer(ConditionEvaluationReportLoggingListener.forLogLevel(LogLevel.INFO))
			.withPropertyValues(BASE_PROPERTY)
			.withConfiguration(AutoConfigurations.of(DEVCHECK_SERVLET_CONFIGURATION))
			.withUserConfiguration(DEVCHECK_CORE_SERVLET_USER_CONFIGURATION)
			;
	
	@Test
	void hasCoreDevCheckController() {
		this.contextRunner.run(context -> {
			assertThat(context).hasSingleBean(DevCheckCoreController.class);
		});
	}
	
	@Test
	void handlerMethods() {
		this.contextRunner.run(context -> {
			var handlerMethods = context.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
			log.debug("handlerMethods : {}", handlerMethods);
		});
	}
}

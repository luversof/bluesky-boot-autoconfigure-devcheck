package io.github.luversof.boot.autoconfigure.devcheck.core;

import static io.github.luversof.boot.autoconfigure.devcheck.AutoConfigurationTestInfo.BASE_PROPERTY;
import static io.github.luversof.boot.autoconfigure.devcheck.AutoConfigurationTestInfo.DEVCHECK_CORE_USER_CONFIGURATION;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.github.luversof.boot.autoconfigure.devcheck.core.config.DevCheckCoreProperties;

class DevCheckCoreAutoConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withPropertyValues(BASE_PROPERTY)
			.withUserConfiguration(DEVCHECK_CORE_USER_CONFIGURATION)
			;
	
	@Test
	void hasDevCheckCoreProperties() {
		this.contextRunner.run(context -> {
			assertThat(context).hasSingleBean(DevCheckCoreProperties.class);
		});
	}
}

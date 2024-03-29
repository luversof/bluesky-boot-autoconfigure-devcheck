package io.github.luversof.boot.autoconfigure.devcheck.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import io.github.luversof.boot.autoconfigure.devcheck.core.aspect.DevCheckControllerAspect;

@AutoConfiguration("_blueskyBootDevCheckCoreAutoConfiguration")
@EnableConfigurationProperties(DevCheckCoreProperties.class)
@PropertySource("classpath:dev-check.properties")
// @ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true")
public class DevCheckCoreAutoConfiguration {
	
	@Bean
	public DevCheckControllerAspect devCheckControllerAspect(DevCheckCoreProperties devCheckCoreProperties) {
		return new DevCheckControllerAspect(devCheckCoreProperties);
	}

}

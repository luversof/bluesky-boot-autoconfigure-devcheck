package io.github.luversof.boot.autoconfigure.devcheck.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(value = "_blueskyBootDevCheckCoreAutoConfiguration", proxyBeanMethods = false)
@EnableConfigurationProperties(DevCheckCoreProperties.class)
@PropertySource("classpath:dev-check.properties")
// @ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true")
public class DevCheckCoreAutoConfiguration {

}

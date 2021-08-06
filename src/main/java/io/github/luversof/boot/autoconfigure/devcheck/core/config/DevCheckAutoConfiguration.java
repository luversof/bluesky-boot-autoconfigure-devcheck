package io.github.luversof.boot.autoconfigure.devcheck.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration("_blueskyDevCheckAutoConfiguration")
@EnableConfigurationProperties(DevCheckProperties.class)
@PropertySource("classpath:dev-check.properties")
public class DevCheckAutoConfiguration {

}

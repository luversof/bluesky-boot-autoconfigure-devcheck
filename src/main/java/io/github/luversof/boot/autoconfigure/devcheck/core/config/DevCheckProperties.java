package io.github.luversof.boot.autoconfigure.devcheck.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "bluesky-boot.dev-check")
public class DevCheckProperties {

	private boolean enabled;
	
	private String[] basePackages;
}

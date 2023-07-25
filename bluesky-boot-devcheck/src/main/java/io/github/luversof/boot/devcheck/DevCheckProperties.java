package io.github.luversof.boot.devcheck;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "bluesky-boot.dev-check")
public class DevCheckProperties {

	private boolean enabled;

	/**
	 * 검색할 package 를 지정합니다.<br>static util class 검색 시 사용합니다.<br>여러 개를 지정할 수 있습니다.<br>
	 * Specifies the package to search for.<br>Used when searching for static util class.<br>You can specify multiple.<br>
	 */
	private String[] basePackages;
	
	private String pathPrefix;
}

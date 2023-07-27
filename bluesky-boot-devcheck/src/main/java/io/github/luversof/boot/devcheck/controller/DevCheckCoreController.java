package io.github.luversof.boot.devcheck.controller;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

import io.github.luversof.boot.devcheck.annotation.DevCheckController;
import io.github.luversof.boot.devcheck.annotation.DevCheckDescription;

@DevCheckController
public class DevCheckCoreController {
	
	private final String pathPrefix = "/blueskyDevCheck/core";

	private final ApplicationContext applicationContext;

	public DevCheckCoreController(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@DevCheckDescription("spring activeProfiles")
	@GetMapping(pathPrefix + "/activeProfiles")
	public String[] activeProfiles() {
		return applicationContext.getEnvironment().getActiveProfiles();
	}

	@DevCheckDescription("locale")
	@GetMapping(pathPrefix + "/locale")
	public Locale locale() {
		return LocaleContextHolder.getLocale();
	}

	@DevCheckDescription("LocaleDateTime.now()")
	@GetMapping(pathPrefix + "/localDateTimeNow")
	public LocalDateTime localeDateTimeNow() {
		return LocalDateTime.now();
	}

	@DevCheckDescription("ZonedDateTime.now()")
	@GetMapping(pathPrefix + "/zonedDateTimeNow")
	public ZonedDateTime zonedDateTimeNow() {
		return ZonedDateTime.now();
	}
}

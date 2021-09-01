package io.github.luversof.boot.autoconfigure.devcheck.core.controller;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckDescription;

@RestController
@RequestMapping(value = "/_check/core", produces = MediaType.APPLICATION_JSON_VALUE)
public class CoreDevCheckController {

	private ApplicationContext applicationContext;

	public CoreDevCheckController(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@DevCheckDescription("spring activeProfiles")
	@GetMapping("/activeProfiles")
	public String[] activeProfiles() {
		return applicationContext.getEnvironment().getActiveProfiles();
	}

	@DevCheckDescription("locale")
	@GetMapping("/locale")
	public Locale locale() {
		return LocaleContextHolder.getLocale();
	}

	@DevCheckDescription("LocaleDateTime.now()")
	@GetMapping("/localDateTimeNow")
	public LocalDateTime localeDateTimeNow() {
		return LocalDateTime.now();
	}

	@DevCheckDescription("ZonedDateTime.now()")
	@GetMapping("/zonedDateTimeNow")
	public ZonedDateTime zonedDateTimeNow() {
		return ZonedDateTime.now();
	}
}

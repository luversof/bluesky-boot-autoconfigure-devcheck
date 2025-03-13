package io.github.luversof.boot.devcheck.logging.logback.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import io.github.luversof.boot.devcheck.annotation.DevCheckController;
import io.github.luversof.boot.devcheck.annotation.DevCheckDescription;
import io.github.luversof.boot.devcheck.logging.logback.service.LogbackAppenderService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@DevCheckController
public class DevCheckLogbackController {
	
	private static final String PATH_PREFIX = "/devCheck/logging/logback";

	private final LogbackAppenderService<ILoggingEvent> playncLogbackAppenderService;

	@DevCheckDescription("Check last 500 line log")
	@GetMapping(PATH_PREFIX + "/logView")
	public List<String> logView() {
		return playncLogbackAppenderService.getLogQueue().stream().map(queue -> queue.getLogMessage().replaceAll(CoreConstants.LINE_SEPARATOR, "").replace("\t", "")).toList();
	}
}

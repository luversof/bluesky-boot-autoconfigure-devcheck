package io.github.luversof.boot.devcheck.logging.logback.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import io.github.luversof.boot.devcheck.annotation.DevCheckController;
import io.github.luversof.boot.devcheck.annotation.DevCheckDescription;
import io.github.luversof.boot.devcheck.logging.logback.service.LogbackAppenderService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@DevCheckController
@RestController
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}/blueskyDevCheck/logging/logback", produces = MediaType.APPLICATION_JSON_VALUE)
public class DevCheckLogbackController {

	private final LogbackAppenderService<ILoggingEvent> blueskyLogbackAppenderService;

	@DevCheckDescription("Check last 500 line log")
	@GetMapping("/logView")
	public List<String> logView() {
		return blueskyLogbackAppenderService.getLogQueue().stream().map(queue -> queue.getLogMessage().replaceAll(CoreConstants.LINE_SEPARATOR, "").replace("\t", "")).collect(Collectors.toList());
	}
}

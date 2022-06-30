package io.github.luversof.boot.autoconfigure.devcheck.logging.logback.config;

import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import io.github.luversof.boot.autoconfigure.devcheck.logging.logback.controller.DevCheckLogbackController;
import io.github.luversof.boot.autoconfigure.devcheck.logging.logback.service.LogbackAppender;
import io.github.luversof.boot.autoconfigure.devcheck.logging.logback.service.LogbackAppenderService;

@Configuration(value = "_blueskyBootDevCheckLoggingLogbackAutoConfiguration", proxyBeanMethods = false)
@ConditionalOnClass(Appender.class)
@ConditionalOnProperty(prefix = "bluesky-boot.dev-check", name = "enabled", havingValue = "true")
public class LogbackAutoConfiguration {

	@Bean
	public LogbackAppenderService<ILoggingEvent> blueskyBootLogbackAppenderService() {
		var logbackAppenderService = new LogbackAppenderService<ILoggingEvent>();
		addLogbackAppender(logbackAppenderService);
		return logbackAppenderService;
	}

	private void addLogbackAppender(LogbackAppenderService<ILoggingEvent> logbackAppenderService) {
		var loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		var appender = (RollingFileAppender<ILoggingEvent>) loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).getAppender("FILE");
		var logbackAppender = new LogbackAppender<>(logbackAppenderService);
		logbackAppender.setContext(loggerContext);
		logbackAppender.setName("blueskyBootLogbackAppender");
		logbackAppender.setEncoder(appender == null ? new PatternLayoutEncoder() : appender.getEncoder());

		logbackAppender.start();
		loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(logbackAppender);
	}

	@Bean
	public DevCheckLogbackController blueskyBootDevCheckLogbackController(LogbackAppenderService<ILoggingEvent> logbackAppenderService) {
		return new DevCheckLogbackController(logbackAppenderService);
	}
}

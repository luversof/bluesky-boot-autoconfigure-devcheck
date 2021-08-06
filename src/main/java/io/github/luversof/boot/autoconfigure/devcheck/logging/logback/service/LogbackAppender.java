package io.github.luversof.boot.autoconfigure.devcheck.logging.logback.service;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import lombok.Setter;

public class LogbackAppender<E> extends UnsynchronizedAppenderBase<E> {
	
	private LogbackAppenderService<E> blueskyLogbackAppenderService;
	
	public LogbackAppender(LogbackAppenderService<E> blueskyLogbackAppenderService) {
		this.blueskyLogbackAppenderService = blueskyLogbackAppenderService;
	}
	
	@Setter
	private Encoder<E> encoder;

	@Override
	protected void append(E eventObject) {
		if (!isStarted()) {
			return;
		}
		blueskyLogbackAppenderService.addLog(eventObject, new String(encoder.encode(eventObject)));
	}

}

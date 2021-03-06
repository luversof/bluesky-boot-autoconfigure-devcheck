package io.github.luversof.boot.autoconfigure.devcheck.core;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import io.github.luversof.boot.autoconfigure.devcheck.core.annotation.DevCheckController;
import io.github.luversof.boot.autoconfigure.devcheck.core.controller.DevCheckCoreController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class SimpleTests {

	@Test
	void patternTest() {
	
		String pattern = ".*(DevCheckController)";
		String pattern2 = ".*(DevCheck).*(Controller)";
		
		String str = "coreDevCheckController";
		String str2 = "coreDevCheckController2";
		String str3 = "coreDevCheckTestController";
		String str4 = "coreDevCheckTestController2";
		
		log.debug("test : {}. {}, {}, {}", Pattern.matches(pattern, str), Pattern.matches(pattern, str2), Pattern.matches(pattern, str3), Pattern.matches(pattern, str4));
		log.debug("test : {}, {}, {}, {}", Pattern.matches(pattern2, str), Pattern.matches(pattern2, str2), Pattern.matches(pattern2, str3), Pattern.matches(pattern2, str4));
	}
	
	@Test
	void annotationTest() {
		DevCheckCoreController devCheckCoreController = new DevCheckCoreController(null);
		log.debug("test : {}", devCheckCoreController.getClass().isAnnotationPresent(DevCheckController.class));
	}

}

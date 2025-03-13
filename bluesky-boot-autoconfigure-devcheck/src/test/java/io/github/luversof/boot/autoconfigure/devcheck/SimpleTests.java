package io.github.luversof.boot.autoconfigure.devcheck;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import io.github.luversof.boot.devcheck.annotation.DevCheckController;
import io.github.luversof.boot.devcheck.controller.DevCheckCoreController;
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
		log.debug("test : {}", DevCheckCoreController.class.isAnnotationPresent(DevCheckController.class));
		log.debug("test : {}", TestDevCheckController.class.isAnnotationPresent(InheritedDevCheckController.class));
	}

}

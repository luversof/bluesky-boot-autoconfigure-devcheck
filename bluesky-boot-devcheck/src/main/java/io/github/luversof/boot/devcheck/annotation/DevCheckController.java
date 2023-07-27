package io.github.luversof.boot.devcheck.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 대상 DevCheckController를 지정합니다.<br>
 * Specifies the target DevCheckController.<br>
 * @author bluesky
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@RestController
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
public @interface DevCheckController {

	@AliasFor(annotation = RestController.class)
	String value() default "";

}

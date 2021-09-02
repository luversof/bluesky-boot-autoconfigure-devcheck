package io.github.luversof.boot.autoconfigure.devcheck.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 대상 DevCheckController를 지정합니다.<br />
 * Specifies the target DevCheckController.<br />
 * @author bluesky
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DevCheckController {

}

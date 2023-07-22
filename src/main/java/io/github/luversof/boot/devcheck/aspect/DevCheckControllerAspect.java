package io.github.luversof.boot.devcheck.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import io.github.luversof.boot.autoconfigure.devcheck.DevCheckCoreProperties;
import io.github.luversof.boot.devcheck.exception.DevCheckException;

@Aspect
public class DevCheckControllerAspect {
	
	private DevCheckCoreProperties devCheckCoreProperties;
	
	public DevCheckControllerAspect(DevCheckCoreProperties devCheckCoreProperties) {
		this.devCheckCoreProperties = devCheckCoreProperties;
	}
	
	@Around("@within(io.github.luversof.boot.devcheck.annotation.DevCheckController)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		if (!devCheckCoreProperties.isEnabled()) {
			throw new DevCheckException();
		}
		return proceedingJoinPoint.proceed();
	}
}

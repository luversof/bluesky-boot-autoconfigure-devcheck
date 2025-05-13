package io.github.luversof.boot.devcheck.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DevCheckUtilTestsSource1 {

	CASE_A(new String[]{"/_check", "/test/_check", "/user/_check"}, "/_check/asdf/asdf/sadf", "/_check"),
	CASE_B(new String[]{"/_check", "/test/_check", "/user/_check"}, "/test/_check/asdf/asdf/sadf", "/test/_check"),
	CASE_C(new String[]{"/_check", "/test/_check", "/test/test2/_check"}, "/test/_check/asdf/asdf/sadf", "/test/_check"),
	CASE_D(new String[]{"/_check", "/test/_check", "/test/test2/_check"}, "/test/test2/_check/asdf/asdf/sadf", "/test/test2/_check")
	
	;
	
	private String[] pathPrefixes;
	
	private String currentRequestURL;
	
	private String expectedPathPrefix;
}

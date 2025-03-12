package io.github.luversof.boot.devcheck.util;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DevCheckUtilTestsSource2 {

	CASE_A("/_check", new ArrayList<>(List.of("/test2/_check/test", "/_check/test", "/test/_check/test")), "/_check/test")
	
	;
	
	private String pathPrefix;
	
	private List<String> urlList;
	
	private String expectedUrlListFirst;
}

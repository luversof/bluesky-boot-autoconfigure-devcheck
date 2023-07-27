package io.github.luversof.boot.devcheck.domain;

import java.util.List;

public record DevCheckInfo(String beanName, List<String> urlList, String description) {
	
}
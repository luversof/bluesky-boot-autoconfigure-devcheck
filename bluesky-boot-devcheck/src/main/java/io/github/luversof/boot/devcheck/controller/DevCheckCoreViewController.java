package io.github.luversof.boot.devcheck.controller;

import org.springframework.web.bind.annotation.GetMapping;

import io.github.luversof.boot.devcheck.annotation.DevCheckViewController;

@DevCheckViewController
public class DevCheckCoreViewController {
	
	@GetMapping("/index")
	public String index() {
		return "forward:/_check/devCheckInfo.html";
	}
	
	@GetMapping("/util")
	public String util() {
		return "forward:/_check/devCheckUtilInfo.html";
	}

}
package io.github.luversof.boot.devcheck.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "${brick-modules.dev-check.path-prefix}", produces = MediaType.TEXT_HTML_VALUE)
public class DevCheckViewController {
	
	@GetMapping("/index")
	public String index() {
		return "forward:/_check/devCheckInfo.html";
	}
	
	@GetMapping("/util")
	public String util() {
		return "forward:/_check/devCheckUtilInfo.html";
	}

}
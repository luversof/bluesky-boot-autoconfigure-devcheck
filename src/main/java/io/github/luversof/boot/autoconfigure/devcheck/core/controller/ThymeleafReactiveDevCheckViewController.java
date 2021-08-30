package io.github.luversof.boot.autoconfigure.devcheck.core.controller;

import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value = "/_check")
public class ThymeleafReactiveDevCheckViewController extends AbstractReactiveDevCheckViewController {
	
	public ThymeleafReactiveDevCheckViewController(Reflections reflections, String pathPrefix) {
		super(reflections, pathPrefix);
	}
	
	@GetMapping({ "", "/index" })
	public Mono<String> index(ServerWebExchange exchange, Model model) {
		model.addAttribute("devCheckInfoList", getDevCheckInfoList(exchange));
		return Mono.just("_check/index");
	}
	
	@GetMapping("/util")
	public Mono<String> util(Model model) {
		model.addAttribute("devCheckUtilInfoList", getDevCheckUtilInfoList());
		return Mono.just("_check/util");
	}
	
}

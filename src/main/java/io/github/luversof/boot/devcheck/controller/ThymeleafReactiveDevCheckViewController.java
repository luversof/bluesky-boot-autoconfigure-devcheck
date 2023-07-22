package io.github.luversof.boot.devcheck.controller;

import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}")
public class ThymeleafReactiveDevCheckViewController extends AbstractReactiveDevCheckViewController {

	public ThymeleafReactiveDevCheckViewController(Reflections reflections) {
		super(reflections);
	}

	@GetMapping({ "", "/index" })
	public Mono<String> index(ServerWebExchange exchange, Model model) {
		model.addAttribute("devCheckInfoList", getDevCheckInfoList(exchange));
		model.addAttribute("pathPrefix", getPathPrefix(exchange));
		return Mono.just("_check/index");
	}

	@GetMapping("/util")
	public Mono<String> util(ServerWebExchange exchange, Model model) {
		model.addAttribute("devCheckUtilInfoList", getDevCheckUtilInfoList());
		model.addAttribute("pathPrefix", getPathPrefix(exchange));
		return Mono.just("_check/util");
	}

}

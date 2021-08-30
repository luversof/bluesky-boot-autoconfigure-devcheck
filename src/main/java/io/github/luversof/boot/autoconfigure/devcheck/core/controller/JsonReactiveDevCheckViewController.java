package io.github.luversof.boot.autoconfigure.devcheck.core.controller;

import java.util.List;

import org.reflections.Reflections;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import io.github.luversof.boot.autoconfigure.devcheck.core.domain.DevCheckUtilInfo;
import io.github.luversof.boot.autoconfigure.devcheck.core.domain.ReactiveDevCheckInfo;

@RestController
@RequestMapping(value = "/_check", produces = MediaType.APPLICATION_JSON_VALUE)
public class JsonReactiveDevCheckViewController extends AbstractReactiveDevCheckViewController {
	
	public JsonReactiveDevCheckViewController(Reflections reflections, String pathPrefix) {
		super(reflections, pathPrefix);
	}
	
	@GetMapping({ "", "/index" })
	public List<ReactiveDevCheckInfo> index(ServerWebExchange exchange, Model model) {
		return getDevCheckInfoList(exchange);
	}
	
	@GetMapping("/util")
	public List<DevCheckUtilInfo> util(Model model) {
		return getDevCheckUtilInfoList();
	}
	
}

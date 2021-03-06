package io.github.luversof.boot.autoconfigure.devcheck.core.controller;

import java.util.List;

import org.reflections.Reflections;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import io.github.luversof.boot.autoconfigure.devcheck.core.domain.DevCheckUtilInfo;
import io.github.luversof.boot.autoconfigure.devcheck.core.domain.ReactiveDevCheckInfo;

@RestController
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
public class JsonReactiveDevCheckViewController extends AbstractReactiveDevCheckViewController {

	public JsonReactiveDevCheckViewController(Reflections reflections) {
		super(reflections);
	}

	@GetMapping({ "", "/index" })
	public List<ReactiveDevCheckInfo> index(ServerWebExchange exchange) {
		var devCheckInfoList = getDevCheckInfoList(exchange);
		var uri = exchange.getRequest().getURI();
		devCheckInfoList.forEach(devCheckInfo -> {
			for (int i = 0; i < devCheckInfo.getUrlList().size(); i++) {
				devCheckInfo.getUrlList().set(i, uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort() + devCheckInfo.getUrlList().get(i));
			}
		});
		return devCheckInfoList;
	}

	@GetMapping("/util")
	public List<DevCheckUtilInfo> util() {
		return getDevCheckUtilInfoList();
	}

}

package io.github.luversof.boot.devcheck.controller.reactive;

import java.util.List;

import org.reflections.Reflections;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import io.github.luversof.boot.devcheck.domain.DevCheckInfo;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.service.reactive.DevCheckInfoWebFluxService;

@RestController
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
public class JsonDevCheckViewWebFluxController extends AbstractDevCheckViewWebFluxController {

	public JsonDevCheckViewWebFluxController(DevCheckInfoWebFluxService devCheckInfoWebFluxService, Reflections reflections) {
		super(devCheckInfoWebFluxService, reflections);
	}

	@GetMapping({ "", "/index" })
	public List<DevCheckInfo> index(ServerWebExchange exchange) {
		var devCheckInfoList = devCheckInfoWebFluxService.getDevCheckInfoList(exchange);
		var uri = exchange.getRequest().getURI();
		devCheckInfoList.forEach(devCheckInfo -> {
			for (int i = 0; i < devCheckInfo.urlList().size(); i++) {
				devCheckInfo.urlList().set(i, uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort() + devCheckInfo.urlList().get(i));
			}
		});
		return devCheckInfoList;
	}

	@GetMapping("/util")
	public List<DevCheckUtilInfo> util() {
		return getDevCheckUtilInfoList();
	}

}

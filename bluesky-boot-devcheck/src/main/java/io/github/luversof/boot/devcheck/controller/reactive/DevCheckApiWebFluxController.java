package io.github.luversof.boot.devcheck.controller.reactive;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import io.github.luversof.boot.devcheck.domain.DevCheckInfo;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.service.DevCheckUtilInfoService;
import io.github.luversof.boot.devcheck.service.reactive.DevCheckInfoWebFluxService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
public class DevCheckApiWebFluxController {
	
	private final DevCheckInfoWebFluxService devCheckInfoWebFluxService;

	private final DevCheckUtilInfoService devCheckUtilInfoService;

	@GetMapping("/devCheckInfoList")
	public List<DevCheckInfo> devCheckInfoList(ServerWebExchange exchange) {
		return devCheckInfoWebFluxService.getDevCheckInfoList(exchange);
	}

	@GetMapping("/devCheckUtilInfoList")
	public List<DevCheckUtilInfo> devCheckUtilInfoList() {
		return devCheckUtilInfoService.getDevCheckUtilInfo();
	}

}

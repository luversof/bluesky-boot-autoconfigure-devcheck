package io.github.luversof.boot.devcheck.controller.servlet;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.luversof.boot.devcheck.domain.DevCheckInfo;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.service.DevCheckUtilInfoService;
import io.github.luversof.boot.devcheck.service.servlet.DevCheckInfoMvcService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
public class DevCheckApiMvcController {
	
	private final DevCheckInfoMvcService devCheckInfoMvcService;
	
	private final DevCheckUtilInfoService devCheckUtilInfoService;
	
	@GetMapping("/devCheckInfoList")
	public List<DevCheckInfo> devCheckInfoList(HttpServletRequest request) {
		return devCheckInfoMvcService.getDevCheckInfoList();
	}
	
	@GetMapping("/devCheckUtilInfoList")
	public List<DevCheckUtilInfo> devCheckUtilInfoList() {
		return devCheckUtilInfoService.getDevCheckUtilInfo();
	}

}

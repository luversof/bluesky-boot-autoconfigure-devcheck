package io.github.luversof.boot.devcheck.controller.servlet;

import java.util.List;

import org.reflections.Reflections;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.luversof.boot.devcheck.domain.DevCheckInfo;
import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.service.servlet.DevCheckInfoMvcService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
public class JsonDevCheckViewMvcController extends AbstractDevCheckViewMvcController {

	public JsonDevCheckViewMvcController(DevCheckInfoMvcService devCheckInfoMvcService, Reflections reflections) {
		super(devCheckInfoMvcService, reflections);
	}

	@GetMapping({ "/devCheckInfoList" })
	public List<DevCheckInfo> index(HttpServletRequest request) {
		var devCheckInfoList = devCheckInfoMvcService.getDevCheckInfoList();
		devCheckInfoList.forEach(devCheckInfo -> {
			for (int i = 0; i < devCheckInfo.urlList().size(); i++) {
				devCheckInfo.urlList().set(i, request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + devCheckInfo.urlList().get(i));
			}
		});
		return devCheckInfoList;
	}
	
	@GetMapping({"", "/index"})
	public String index() {
		return "index.html";
	}

	@GetMapping("/util")
	public List<DevCheckUtilInfo> util() {
		return getDevCheckUtilInfoList();
	}

}

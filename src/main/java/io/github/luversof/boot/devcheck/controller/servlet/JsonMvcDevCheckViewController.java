package io.github.luversof.boot.devcheck.controller.servlet;

import java.util.List;


import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.luversof.boot.devcheck.domain.DevCheckUtilInfo;
import io.github.luversof.boot.devcheck.domain.servlet.MvcDevCheckInfo;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
public class JsonMvcDevCheckViewController extends AbstractMvcDevCheckViewController {

	public JsonMvcDevCheckViewController(ApplicationContext applicationContext, Reflections reflections) {
		super(applicationContext, reflections);
	}

	@GetMapping({ "", "/index" })
	public List<MvcDevCheckInfo> index(HttpServletRequest request) {
		var devCheckInfoList = getDevCheckInfoList();
		devCheckInfoList.forEach(devCheckInfo -> {
			for (int i = 0; i < devCheckInfo.getUrlList().size(); i++) {
				devCheckInfo.getUrlList().set(i, request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + devCheckInfo.getUrlList().get(i));
			}
		});
		return devCheckInfoList;
	}

	@GetMapping("/util")
	public List<DevCheckUtilInfo> util() {
		return getDevCheckUtilInfoList();
	}

}

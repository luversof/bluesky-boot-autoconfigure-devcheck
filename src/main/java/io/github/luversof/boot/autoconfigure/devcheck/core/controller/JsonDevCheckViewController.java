package io.github.luversof.boot.autoconfigure.devcheck.core.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.luversof.boot.autoconfigure.devcheck.core.domain.DevCheckInfo;
import io.github.luversof.boot.autoconfigure.devcheck.core.domain.DevCheckUtilInfo;

@RestController
@RequestMapping(value = "/_check", produces = MediaType.APPLICATION_JSON_VALUE)
public class JsonDevCheckViewController extends AbstractDevCheckViewController {

	public JsonDevCheckViewController(ApplicationContext applicationContext, Reflections reflections, String pathPrefix) {
		super(applicationContext, reflections, pathPrefix);
	}
	
	@GetMapping({"", "/index"})
	public List<DevCheckInfo> index(HttpServletRequest request) {
		var devCheckInfoList = getDevCheckInfoList();
		devCheckInfoList.forEach(devCheckInfo -> {
			for (int i = 0 ; i < devCheckInfo.getUrlList().size() ; i++) {
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

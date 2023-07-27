package io.github.luversof.boot.devcheck.controller.servlet;

import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.luversof.boot.devcheck.service.servlet.DevCheckInfoMvcService;

@Controller
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}")
public class ThymeleafDevCheckViewMvcController extends AbstractDevCheckViewMvcController {

	public ThymeleafDevCheckViewMvcController(DevCheckInfoMvcService devCheckInfoMvcService, Reflections reflections) {
		super(devCheckInfoMvcService, reflections);
	}

	@GetMapping({ "", "/index" })
	public String index(ModelMap modelMap) {
		modelMap.addAttribute("devCheckInfoList", devCheckInfoMvcService.getDevCheckInfoList());
		modelMap.addAttribute("pathPrefix", getPathPrefix());
		return "_check/thymeleaf/index";
	}

	@GetMapping("/util")
	public String util(ModelMap modelMap) {
		modelMap.addAttribute("devCheckUtilInfoList", getDevCheckUtilInfoList());
		modelMap.addAttribute("pathPrefix", getPathPrefix());
		return "_check/thymeleaf/util";
	}

}

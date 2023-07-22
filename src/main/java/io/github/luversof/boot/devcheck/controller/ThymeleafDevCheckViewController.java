package io.github.luversof.boot.devcheck.controller;

import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "${bluesky-boot.dev-check.path-prefix}")
public class ThymeleafDevCheckViewController extends AbstractDevCheckViewController {

	public ThymeleafDevCheckViewController(ApplicationContext applicationContext, Reflections reflections) {
		super(applicationContext, reflections);
	}

	@GetMapping({ "", "/index" })
	public String index(ModelMap modelMap) {
		modelMap.addAttribute("devCheckInfoList", getDevCheckInfoList());
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

package io.github.luversof.boot.autoconfigure.devcheck.core.controller;

import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/_check")
public class ThymeleafDevCheckViewController extends AbstractDevCheckViewController {

	public ThymeleafDevCheckViewController(ApplicationContext applicationContext, Reflections reflections, String pathPrefix) {
		super(applicationContext, reflections, pathPrefix);
	}

	@GetMapping({ "", "/index" })
	public String index(ModelMap modelMap) {
		modelMap.addAttribute("devCheckInfoList", getDevCheckInfoList());
		return "_check/thymeleaf/index";
	}

	@GetMapping("/util")
	public String util(ModelMap modelMap) {
		modelMap.addAttribute("devCheckUtilInfoList", getDevCheckUtilInfoList());
		return "_check/thymeleaf/util";
	}

}
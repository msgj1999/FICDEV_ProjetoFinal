package com.example.demo.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GestaoControllerView {
	@GetMapping("/inicio")
	public ModelAndView paginaInicial() {
		var view = new ModelAndView("inicio");
		return view;
	}
	
	
	
}
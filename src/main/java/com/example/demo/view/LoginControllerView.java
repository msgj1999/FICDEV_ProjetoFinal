package com.example.demo.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class LoginControllerView {
	@GetMapping("/login")
	String login() {
		return "login";
	}
}
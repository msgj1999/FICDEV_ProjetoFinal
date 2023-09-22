package com.example.demo.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginControllerView {
    @GetMapping("/login")
    public String showLoginForm() {
        System.out.println("Acessando a página de login");
        return "login";
    }
}
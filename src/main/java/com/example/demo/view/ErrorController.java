package com.example.demo.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error404")
    public String error404() {
        return "error404";
    }
    
    @GetMapping("/error403")
    public String error403() {
        return "error403";
    }
}

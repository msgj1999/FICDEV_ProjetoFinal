package com.example.demo.entities;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErrorConfig {

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new MyErrorPageRegistrar();
    }
    
    @Bean
    public ErrorPageRegistrar errorPageRegistrar2() {
        return new MyErrorPageRegistrar2();
    }

    private static class MyErrorPageRegistrar implements ErrorPageRegistrar {
        @Override
        public void registerErrorPages(ErrorPageRegistry registry) {
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error404");
            registry.addErrorPages(error404Page);
        }
    }
    
    private static class MyErrorPageRegistrar2 implements ErrorPageRegistrar {
        @Override
        public void registerErrorPages(ErrorPageRegistry registry) {
            ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/error403");
            registry.addErrorPages(error403Page);
        }
    }
}

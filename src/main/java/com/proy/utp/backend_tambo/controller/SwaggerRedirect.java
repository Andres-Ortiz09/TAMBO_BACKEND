package com.proy.utp.backend_tambo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirect {

    @GetMapping("/docs")
    public String redirectToSwaggerUi() {
        return "forward:/swagger-ui/index.html";
    }
}

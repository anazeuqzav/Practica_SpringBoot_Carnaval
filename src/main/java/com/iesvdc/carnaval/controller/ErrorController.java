package com.iesvdc.carnaval.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "mipaginError"; // Debe coincidir con el nombre del archivo en src/main/resources/templates/
    }

    public String getErrorPath() {
        return "/error";
    }
}
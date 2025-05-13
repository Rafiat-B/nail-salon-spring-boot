package com.example.nailsalon.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Operation(summary = "Loads the main page of the application.", description = "Displays the application's main page.")
    @GetMapping("/")
    public String home() {
        return "home/index";
    }
}

package com.example.nailsalon.controllers;

import com.example.nailsalon.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Loads the login page of the application.", description = "Displays the application's login page.")
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    @Operation(summary = "Loads the user registration form of the application.", description = "Displays the application's user registration form.")
    @GetMapping("/register")
    public String showRegisterPage() {
        return "auth/register";
    }

    @Operation(summary = "This endpoint is used to send the filled data to the backend server.", description = "This endpoint is will be used when the user has filled the registration form and clicks the submit button.")
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {

        if (authService.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists");
            return "auth/register";
        }
        authService.registerUser(username, password, email);

        return "redirect:/auth/login?registered";
    }
}

package com.example.nailsalon.controllers;

import com.example.nailsalon.models.Users;
import com.example.nailsalon.repository.UserRepository;
import com.example.nailsalon.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Loads the User Dashboard after successful Login.", description = "Displays the users dashboard and the features available on it.")
    @GetMapping("/dashboard")
    public String userDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Users user = userService.getCurrentUser(userDetails);

        if (user == null) {
            return "redirect:/"; // fallback
        }

        model.addAttribute("user", user);
        return "user/user-dashboard";
    }
}

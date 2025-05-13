package com.example.nailsalon.controllers;

import com.example.nailsalon.models.Admin;
import com.example.nailsalon.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Endpoints for admin dashboard")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    @Operation(
            summary = "Get Admin Dashboard",
            description = "Loads the dashboard page for the currently authenticated admin"
    )
    public String adminDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Admin admin = adminService.getCurrentAdmin(userDetails);
        if (admin == null) {
            return "redirect:/"; // fallback
        }
        model.addAttribute("admin", admin);
        return "admin/admin-dashboard";
    }
}

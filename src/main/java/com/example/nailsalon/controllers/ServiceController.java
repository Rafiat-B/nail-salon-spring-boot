package com.example.nailsalon.controllers;

import com.example.nailsalon.models.Admin;
import com.example.nailsalon.models.OfferedServices;
import com.example.nailsalon.services.AdminService;
import com.example.nailsalon.services.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private AdminService adminService;

    // Show list of services (For users/admins)
    @Operation(summary = "Get all services", description = "Fetches a list of all available services")
    @GetMapping
    public String getAllServices(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Admin admin = adminService.getCurrentAdmin(userDetails);
        if (admin == null) {
            return "redirect:/"; // fallback
        }
        model.addAttribute("services", serviceService.getAllServices());
        return "services/services"; // ✅ Correct path to "services.html"
    }

    // Show Add Service Form (For Admins)
    @Operation(summary = "Show Add Service Form", description = "Displays a form to add a new service")
    @GetMapping("/add")
    public String showAddServiceForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Admin admin = adminService.getCurrentAdmin(userDetails);
        if (admin == null) {
            return "redirect:/"; // fallback
        }
        model.addAttribute("service", new OfferedServices());
        return "services/service-form"; // ✅ Correct path to "service-form.html"
    }

    // Process Add Service (For Admins)
    @Operation(summary = "Save a new service", description = "Saves a new service to the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Service successfully saved"), @ApiResponse(responseCode = "400", description = "Invalid service data")})
    @PostMapping("/save")
    public ResponseEntity<Void> saveService(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute OfferedServices service) {
        Admin admin = adminService.getCurrentAdmin(userDetails);
        if (admin == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).build();

        }
        serviceService.saveService(service);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/services")); // 👈 redirect target

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }


    // Show Edit Service Form (For Admins)
    @Operation(summary = "Show Edit Service Form", description = "Displays the edit form for a specific service")
    @GetMapping("/edit/{id}")
    public String showEditServiceForm(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Model model) {
        Admin admin = adminService.getCurrentAdmin(userDetails);
        if (admin == null) {
            return "redirect:/"; // fallback
        }
        model.addAttribute("service", serviceService.getServiceById(id).orElse(new OfferedServices()));
        return "services/service-form"; // ✅ Correct path to "service-form.html"
    }

    // Delete a service (For Admins)
    @Operation(summary = "Delete a service", description = "Deletes a specific service from the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Service successfully deleted"), @ApiResponse(responseCode = "404", description = "Service not found")})
    @GetMapping("/delete/{id}")
    public String deleteService(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Admin admin = adminService.getCurrentAdmin(userDetails);
        if (admin == null) {
            return "redirect:/"; // fallback
        }
        serviceService.deleteService(id);
        return "redirect:/services"; // ✅ Redirects to service list
    }
}

package com.example.nailsalon.controllers;


import com.example.nailsalon.enums.PaymentStatus;
import com.example.nailsalon.models.Admin;
import com.example.nailsalon.models.Payment;
import com.example.nailsalon.services.AdminService;
import com.example.nailsalon.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/payments")
public class AdminPaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AdminService adminService;

    @GetMapping
    public String listPayments(Model model) {
        List<Payment> payments = paymentService.getAllPayments(); // ✅ Fetch all payments
        model.addAttribute("payments", payments);
        return "payments/payment-status"; // ✅ Renders payment-status.html
    }

    // ✅ Show all payments for admin

    @Operation(summary = "API to get the payment status.", description = "This endpoint will be used to get the status of the payments.")
    @GetMapping("/status/{paymentId}")
    public String viewPaymentStatus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long paymentId, Model model) {
        Admin admin = adminService.getCurrentAdmin(userDetails);
        if (admin == null) {
            return "redirect:/"; // fallback
        }
        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment == null) {
            return "redirect:/admin/dashboard"; // ✅ Redirects to admin dashboard
        }
        model.addAttribute("payment", payment);
        return "payments/payment-status"; // ✅ Renders payment-status.html
    }

    @PostMapping("/update-payment-status")
    @Operation(summary = " API to update the payment status", description = "This endpoint will be used to update the status of payment.")

    public String updatePaymentStatus(@AuthenticationPrincipal UserDetails userDetails, @RequestParam int paymentId, @RequestParam String status, Model model) {
        try {
            Admin admin = adminService.getCurrentAdmin(userDetails);
            if (admin == null) {
                return "redirect:/"; // fallback
            }
            PaymentStatus newStatus = PaymentStatus.valueOf(status.toUpperCase()); // ✅ Convert to ENUM

            paymentService.updatePaymentStatus(paymentId, newStatus); // ✅ Use existing method
            List<Payment> payments = paymentService.getAllPayments(); // ✅ Fetch all payments
            model.addAttribute("payments", payments);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status value: " + status); // ✅ Log invalid status errors
        }

        return "payments/payment-status"; // ✅ Renders payment-status.html
    }
}

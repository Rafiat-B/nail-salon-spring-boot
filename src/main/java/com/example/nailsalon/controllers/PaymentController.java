package com.example.nailsalon.controllers;

import com.example.nailsalon.models.Appointment;
import com.example.nailsalon.models.Payment;
import com.example.nailsalon.enums.PaymentStatus;
import com.example.nailsalon.models.Users;
import com.example.nailsalon.services.AppointmentService;
import com.example.nailsalon.services.PaymentService;
import com.example.nailsalon.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AppointmentService appointmentService; // ✅ Injected
    @Autowired
    private UserService userService;


    @Operation(summary = "Shows the payment form for a specific appointment", description = "This endpoint shows the form with payment details of a specific appointment.")
    @GetMapping("/{appointmentId}")
    public String showPaymentPage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long appointmentId, Model model) {
        Users loggedInUser = userService.getCurrentUser(userDetails);
        if (loggedInUser == null) {
            return "redirect:/";
        }
        Optional<Appointment> appointmentOptional = appointmentService.getAppointmentById(appointmentId);

        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            model.addAttribute("appointment", appointment); // ✅ Pass appointment details

            List<Payment> payments = paymentService.getPaymentsByAppointmentId(appointmentId);
            model.addAttribute("payments", payments);

            return "payments/payment";
        } else {
            return "redirect:/appointments";
        }
    }

    @Operation(summary = "Rest API to process the payment", description = "This endpoint will be used to process the payment.")
    @PostMapping("/pay")
    @ResponseBody
    public ResponseEntity<String> processPayment(@AuthenticationPrincipal UserDetails userDetails,@RequestParam long appointmentId, @RequestParam double amount) {
        Users loggedInUser = userService.getCurrentUser(userDetails);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/" ).build();
        }
        try {
            paymentService.processPayment(appointmentId, amount); // ✅ Ensures status is updated, not duplicated
            // Instead of redirecting, you return a response with the URL for redirection
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/payments/" + appointmentId).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed: " + e.getMessage());
        }
    }

//    @Operation(summary = "Rest API to update the payment status", description = "This endpoint will be used to update the status of payment.")
//    @PostMapping("/update-status")
//    @ResponseBody
//    public ResponseEntity<String> updatePaymentStatus(@AuthenticationPrincipal UserDetails userDetails,@RequestParam int paymentId, @RequestParam String status) {
//        Users loggedInUser = userService.getCurrentUser(userDetails);
//        if (loggedInUser == null) {
//            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/" ).build();
//        }
//        try {
//            PaymentStatus newStatus = PaymentStatus.valueOf(status.toUpperCase());
//            paymentService.updatePaymentStatus(paymentId, newStatus);
//            // Instead of redirecting, return a success message
//            return ResponseEntity.ok("Payment status updated successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating payment status: " + e.getMessage());
//        }
//    }



}

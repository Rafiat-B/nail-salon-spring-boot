package com.example.nailsalon.controllers;

import com.example.nailsalon.models.Appointment;
import com.example.nailsalon.models.OfferedServices;
import com.example.nailsalon.models.Users;
import com.example.nailsalon.services.AppointmentService;
import com.example.nailsalon.services.ServiceService;
import com.example.nailsalon.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ServiceService serviceService; // ✅ Inject ServiceService to fetch price
    private final UserService userService;

    // Show list of appointments

    @GetMapping
    @Operation(summary = "Loads the list of appointments for the user.", description = "Displays the appointments list.")
    public String listAppointments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Users loggedInUser = userService.getCurrentUser(userDetails);
        if (loggedInUser == null) {
            return "redirect:/";
        }
        List<Appointment> appointments = appointmentService.getAllAppointmentsForUser(loggedInUser);
        model.addAttribute("appointments", appointments);

        return "appointments/appointments";

    }

    // Show the appointment booking form
    @GetMapping("/book")
    @Operation(summary = "Loads the appointment booking form.", description = "Displays the new appointment booking form.")
    public String showBookingForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Users loggedInUser = userService.getCurrentUser(userDetails);
        if (loggedInUser == null) {
            return "redirect:/";
        }

        List<OfferedServices> availableServices = serviceService.getAllServices();
        if (availableServices.isEmpty()) {
            model.addAttribute("error", "No services available at the moment.");
        }

        model.addAttribute("appointment", new Appointment());

        model.addAttribute("services", availableServices); // ✅ Ensure services list is available

        return "appointments/appointment-form";

    }

    // Process the appointment booking
    @Operation(summary = "Submit an appointment booking form.", description = "Handles the submission of a new appointment booking.")
    @PostMapping("/book")
    public String bookAppointment(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute Appointment appointment) {
        Users loggedInUser = userService.getCurrentUser(userDetails);
        if (loggedInUser == null) {
            return "redirect:/";
        }
        Optional<OfferedServices> selectedService = serviceService.getServiceByName(appointment.getService());
        if (selectedService.isPresent()) {
            appointment.setPrice(selectedService.get().getPrice());
        } else {
            appointment.setPrice(0.0); // Default if no service found (should not happen)

        }


        Appointment savedAppointment = appointmentService.saveAppointment(appointment);
        //return "redirect:/appointments";
        return "redirect:/payments/" + savedAppointment.getId();

    }

    // Delete an appointment
    @Operation(summary = "Deletes an appointment booking", description = "Handles the deletion of an existing appointment booking.")
    @GetMapping("/delete/{id}")
    public String deleteAppointment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Users loggedInUser = userService.getCurrentUser(userDetails);
        if (loggedInUser == null) {
            return "redirect:/";
        }
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }
}
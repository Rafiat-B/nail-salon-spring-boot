package com.example.nailsalon.services;

import com.example.nailsalon.models.Appointment;
import com.example.nailsalon.models.Users;
import com.example.nailsalon.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get appointment by ID
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    // Save a new appointment
//    public void saveAppointment(Appointment appointment) {
//        appointmentRepository.save(appointment);
//    }
    //ahmed
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);  // ✅ Returns the saved object
    }


    // Delete appointment by ID
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> getAllAppointmentsForUser(Users loggedInUser) {
        String email = loggedInUser.getEmail();
        return appointmentRepository.findByEmail(email);
    }
}
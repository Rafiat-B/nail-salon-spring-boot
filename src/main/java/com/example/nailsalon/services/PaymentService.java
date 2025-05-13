package com.example.nailsalon.services;

import com.example.nailsalon.enums.PaymentStatus;
import com.example.nailsalon.models.Appointment;
import com.example.nailsalon.models.Payment;
import com.example.nailsalon.repository.AppointmentRepository;
import com.example.nailsalon.repository.PaymentRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    public void processPayment(Long appointmentId, double amount) throws BadRequestException {
        // ✅ Check if a payment already exists for this appointment
        Optional<Payment> existingPayment = paymentRepository.findByAppointmentId(appointmentId).stream().findFirst();
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            throw new BadRequestException("Appointment not found.");
        }

        if (existingPayment.isPresent()) {
        } else {
            // ✅ Create a new payment only if none exists
            Payment payment = new Payment();
            payment.setAppointment(appointment);
            payment.setAmount(amount);
            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setPaymentStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);
            appointment.setStatus(PaymentStatus.PAID.toString());
            appointmentRepository.save(appointment);
        }
    }

    // Create a new payment
    public Payment createPayment(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.PENDING); // Default status
        return paymentRepository.save(payment);
    }

    // Retrieve all payments for an appointment
    public List<Payment> getPaymentsByAppointmentId(long appointmentId) {
        return paymentRepository.findByAppointmentId(appointmentId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElse(null);
    }

    // Update payment status
    public Payment updatePaymentStatus(long paymentId, PaymentStatus newStatus) {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();
            payment.setPaymentStatus(newStatus);
            return paymentRepository.save(payment);
        }
        return null; // Handle error scenario properly in the controller
    }
}

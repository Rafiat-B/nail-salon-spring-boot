package com.example.nailsalon.repository;
import com.example.nailsalon.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

    // Fetch all payments for a specific appointment
    List<Payment> findByAppointmentId(long appointmentId);
}

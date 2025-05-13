package com.example.nailsalon.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String service;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String status = "Pending";  // Default status

    @Column(nullable = false)
    private double price;  // ✅ Added price field

    @OneToMany(mappedBy = "appointment", fetch = FetchType.LAZY)
    private List<Payment> paymentList;

    // ✅ Added constructor for setting only ID
    public Appointment(Long id) {
        this.id = id;
    }
}
package com.example.nailsalon.repository;
import com.example.nailsalon.models.OfferedServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<OfferedServices, Long> {
    Optional<OfferedServices> findByName(String name); // ✅ Query service by name
}

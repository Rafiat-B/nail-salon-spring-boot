package com.example.nailsalon.services;

import com.example.nailsalon.models.OfferedServices;
import com.example.nailsalon.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<OfferedServices> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<OfferedServices> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public OfferedServices saveService(OfferedServices offeredServices) {
        return serviceRepository.save(offeredServices);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    // ✅ Fetch service by name (to get price)
    public Optional<OfferedServices> getServiceByName(String serviceName) {
        return serviceRepository.findByName(serviceName);
    }
}

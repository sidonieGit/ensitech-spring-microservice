package com.project.registration_service.dao.repository;

import com.project.registration_service.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration , Long> {
    boolean existsByRegistrationNumber(Long number);
    List<Registration> findByMatricule(String matricule);
}

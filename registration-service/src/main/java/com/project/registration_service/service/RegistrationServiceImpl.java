package com.project.registration_service.service;

import com.project.registration_service.dao.repository.RegistrationRepository;
import com.project.registration_service.domain.Registration;
import com.project.registration_service.dto.CreateRegistrationDTO;
import com.project.registration_service.dto.RegistrationStudentDTO;
import com.project.registration_service.dto.RegistrationDTO;
import com.project.registration_service.feign.StudentRestClient;
import com.project.registration_service.mapper.RegistrationMapper;
import com.project.registration_service.mapper.StudentRegistrationMapper;
import com.project.registration_service.model.Student;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationServiceImpl implements RegistrationService{
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final StudentRestClient studentRestClient;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper, StudentRestClient studentRestClient) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.studentRestClient = studentRestClient;
    }


    @Override
    public RegistrationDTO getRegistration(Long id) {
        Optional<Registration> registration = registrationRepository.findById(id);
        if(registration.isPresent()){
            return this.registrationMapper.toDto(registration.get());
        }
         return null;
    }

    public RegistrationStudentDTO create(CreateRegistrationDTO dto) {
        try {
            // 1. Vérifier l'étudiant via Feign
            Student student = studentRestClient.getStudentByMatricule(dto.matricule());
            if (student == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Etudiant introuvable avec la matricule : " + dto.matricule());
            }

            // 2. Mapper DTO -> Entité et sauvegarder
            Registration entity = StudentRegistrationMapper.toEntity(dto);
            entity = registrationRepository.save(entity);

            // 3. Mapper Entité -> DTO et l'enrichir
            return StudentRegistrationMapper.toDto(entity, student);
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Etudiant introuvable avec la matricule : " + dto.matricule(), e);
        }
    }

    /** Récupérer une inscription par id, enrichie optionnellement avec Student */
    public RegistrationStudentDTO getById(Long id, boolean expandStudent) {
        Registration entity = registrationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscription introuvable"));

        if (expandStudent) {
            try {
                Student student = studentRestClient.getStudentByMatricule(entity.getMatricule());
                return StudentRegistrationMapper.toDto(entity, student);
            } catch (FeignException.NotFound e) {
                // Gérer le cas où l'étudiant n'est pas trouvé
                return StudentRegistrationMapper.toDto(entity, null);
            }
        } else {
            return StudentRegistrationMapper.toDto(entity);
        }
    }


    /** Toutes les inscriptions d’un étudiant (1 Student -> n Registrations) */
    public List<RegistrationStudentDTO> listByStudent(String matricule, boolean expandStudent) {
//        List<Registration> registrations = registrationRepository.findByMatricule(matricule);
//
//        // Si l'enrichissement n'est pas demandé, on retourne les DTOs simples
//        if (!expandStudent) {
//            return registrations.stream()
//                    .map(StudentRegistrationMapper::toDto)
//                    .collect(Collectors.toList());
//        }
//
//        // Sinon, on enrichit chaque DTO avec les informations de l'étudiant
//        try {
//            Student student = studentRestClient.getStudentByMatricule(matricule);
//            return registrations.stream()
//                    .map(r -> StudentRegistrationMapper.toDto(r, student))
//                    .collect(Collectors.toList());
//        } catch (FeignException.NotFound e) {
//            // Si l'étudiant n'est pas trouvé, on retourne quand même la liste non-enrichie
//            return registrations.stream()
//                    .map(r -> StudentRegistrationMapper.toDto(r, null))
//                    .collect(Collectors.toList());
//        }
        // 1. Récupérer toutes les registrations
        List<Registration> registrations = registrationRepository.findByMatricule(matricule);

        // 2. Si demandé, récupérer les infos de l'étudiant
        Student student = null;
        if (expandStudent && !registrations.isEmpty()) {
            student = studentRestClient.getStudentByMatricule(matricule);
        }

        // 3. Mapper chaque entity en DTO
//        return registrations.stream()
//                .map(reg -> StudentRegistrationMapper.toDto(reg, student))
//                .toList();

        return List.of();
    }

    @Override
    public List<RegistrationDTO> getAllRegistrations() {
        List<Registration> registrations = this.registrationRepository.findAll();
        return this.registrationMapper.toDtoList(registrations);
    }

    @Override
    public RegistrationDTO createRegistration(RegistrationDTO registrationDTO) {
           if(this.registrationRepository.existsByRegistrationNumber(registrationDTO.registrationNumber())){
               throw new IllegalArgumentException("Registration number already exists");
           }
           Registration registration = this.registrationMapper.toEntity(registrationDTO);
           this.registrationRepository.save(registration);
        return registrationDTO;

    }

    @Override
    public RegistrationDTO updateRegistration(Long id, RegistrationDTO registrationDTO) {

        Registration existingYear = this.registrationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Already exists!"));

        existingYear.setRegistrationNumber(registrationDTO.registrationNumber());
        existingYear.setLevel(registrationDTO.level());

        Registration registration = this.registrationRepository.save(existingYear);

        return registrationMapper.toDto(registration);


    }

    @Override
    public void deleteRegistration(Long id) {
        if(this.registrationRepository.findById(id).isPresent()){
            this.registrationRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("Not deleted!");
        }

    }


}

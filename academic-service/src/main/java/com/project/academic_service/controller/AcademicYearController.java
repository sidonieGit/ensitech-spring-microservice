package com.project.academic_service.controller;

import com.project.academic_service.domain.AcademicYear;
import com.project.academic_service.dto.AcademicDTO;
import com.project.academic_service.service.AcademicYearServiceImpl;
import com.project.academic_service.service.IAcademicYearService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/academic-year")
public class AcademicYearController {

    private final IAcademicYearService academicYearService;


    public AcademicYearController(IAcademicYearService academicYearService) {
        this.academicYearService = academicYearService;
    }

    @GetMapping("/test")
    public String test(){
        return "YES";
    }

    @GetMapping
    public List<AcademicDTO> getAll(){
        return this.academicYearService.getYears();
    }

    @GetMapping("/{id}")
    public AcademicDTO get(@PathVariable("id") int id){
        return this.academicYearService.get(id);
    }

    @PostMapping
    public ResponseEntity<AcademicYear> create(@RequestBody AcademicDTO academicDTO){
        AcademicYear post = academicYearService.addAcademicYear(academicDTO);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicDTO> update(@PathVariable int id, @RequestBody AcademicDTO academicDTO){
        AcademicDTO put = academicYearService.updateAcademicYear(id, academicDTO);
        return ResponseEntity.ok(put);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        this.academicYearService.delete(id);

        return ResponseEntity.noContent().build();
    }
}

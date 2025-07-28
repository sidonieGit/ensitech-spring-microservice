package com.project.academic_service.controller;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/academics")
public class AcademicYearController {
    @GetMapping("/")
    String get(){
        return "Academic-service";
    }

}

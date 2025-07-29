package com.project.academic_service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AcademicYearAlreadyExistsException extends RuntimeException{
    private String message;

    public AcademicYearAlreadyExistsException(String msg){
        super(msg);
        this.message = msg;
    }

}

package com.project.academic_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class AcademicServiceApplication {

	public static void main(String[] args) {
//		Dotenv dotenv = Dotenv.load();
//		System.out.println(dotenv.get("MYSQL_USER"));
		SpringApplication.run(AcademicServiceApplication.class, args);
	}

}

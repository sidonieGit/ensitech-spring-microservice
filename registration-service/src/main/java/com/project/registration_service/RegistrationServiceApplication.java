package com.project.registration_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Objects;

@SpringBootApplication
@EnableFeignClients
public class RegistrationServiceApplication {


	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.out.println(dotenv.get("MYSQL_DATABASE"));


		SpringApplication.run(RegistrationServiceApplication.class, args);

//		System.setProperty("MYSQL_DATABASE", Objects.requireNonNull(dotenv.get("MYSQL_DATABASE")));
//		System.setProperty("MYSQL_USERNAME", Objects.requireNonNull(dotenv.get("MYSQL_USERNAME")));
//		System.setProperty("MYSQL_PASSWORD", Objects.requireNonNull(dotenv.get("MYSQL_PASSWORD")));
	}

}

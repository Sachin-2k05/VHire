package com.example.VHire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class VHireApplication {

	public static void main(String[] args) {
		SpringApplication.run(VHireApplication.class, args);
	}

}

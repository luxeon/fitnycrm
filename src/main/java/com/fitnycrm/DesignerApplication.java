package com.fitnycrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class DesignerApplication {

	public static void main(String[] args) {
		// Set the default timezone to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(DesignerApplication.class, args);
	}

}

package com.borismilenski.museumis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class MuseumIsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuseumIsApplication.class, args);
	}

}

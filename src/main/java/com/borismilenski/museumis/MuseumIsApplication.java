package com.borismilenski.museumis;

import com.google.ortools.Loader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MuseumIsApplication {
	public static void main(String[] args) {
		Loader.loadNativeLibraries();
		SpringApplication.run(MuseumIsApplication.class, args);
	}

}

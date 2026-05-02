package com.sartiniomar.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
    System.out.println("🔥 VERSION version123 🔥");
    System.out.println("BUILD TIME: " + java.time.Instant.now());
    SpringApplication.run(LibraryApplication.class, args);
	}

}

package com.utopia.springboothn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.utopia.springboothn")
public class SpringbootHnApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootHnApplication.class, args);
	}
}

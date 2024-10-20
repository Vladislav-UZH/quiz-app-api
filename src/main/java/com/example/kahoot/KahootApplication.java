package com.example.kahoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/config/.env")
@SpringBootApplication
public class KahootApplication {


	public static void main(String[] args) {
		SpringApplication.run(KahootApplication.class, args);
	}

}

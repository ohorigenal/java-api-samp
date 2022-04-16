package com.compass.javaapisamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class JavaApiSampApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaApiSampApplication.class, args);
	}

}

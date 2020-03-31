package com.reactive.fluxdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class FluxdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FluxdemoApplication.class, args);
	}

}

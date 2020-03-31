package com.reactive.fluxdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClientOptions;

@Configuration
public class MongoDbSettings {

	// https://stackoverflow.com/questions/36784017/set-mongo-timeout-in-spring-boot/39851184#39851184
	// https://blog.marcosbarbero.com/pt_BR/multiple-mongodb-connectors-in-spring-boot/
	@Bean
	public MongoClientOptions mongoOptions() {
		return MongoClientOptions.builder().socketTimeout(2000).build();
	}

}
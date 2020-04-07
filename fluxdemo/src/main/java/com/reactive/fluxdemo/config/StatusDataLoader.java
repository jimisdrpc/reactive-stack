package com.reactive.fluxdemo.config;

import com.reactive.fluxdemo.domain.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactive.fluxdemo.repository.StatusRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

@Component
public class StatusDataLoader implements CommandLineRunner {

	private StatusRepository statusRepository;

	private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

	StatusDataLoader(final StatusRepository statusMongoReactiveRepository) {
		this.statusRepository = statusMongoReactiveRepository;
	}

	@Override
	public void run(final String... args) throws Exception {

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream("load-status-test-data.json")));

		ObjectMapper mapper = new ObjectMapper();

		Flux<Status> flux = Flux.generate(() -> parser(bufferedReader, mapper), this::pullOrComplete, jsonParser -> {
			try {
				jsonParser.close();
			} catch (IOException e) {
			}
		});

		flux.map(l -> statusRepository.save(l)).subscribe(m -> log.info("Carga Status Teste: {}", m.block()));
	}

	private JsonParser parser(Reader reader, ObjectMapper mapper) {
		JsonParser parser = null;
		try {
			parser = mapper.getFactory().createParser(reader);
			parser.nextToken();
		} catch (IOException e) {
		}
		return parser;
	}

	private JsonParser pullOrComplete(JsonParser parser, SynchronousSink<Status> emitter) {
		try {
			if (parser.nextToken() != JsonToken.END_ARRAY) {
				Status t = parser.readValueAs(Status.class);
				emitter.next(t);
			} else {
				emitter.complete();
			}
		} catch (IOException e) {
			emitter.error(e);
		}
		return parser;
	}
}

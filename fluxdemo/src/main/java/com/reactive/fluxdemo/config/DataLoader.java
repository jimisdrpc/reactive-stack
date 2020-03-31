package com.reactive.fluxdemo.config;

import com.reactive.fluxdemo.domain.*;
import com.reactive.fluxdemo.repository.*;
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
import com.reactive.fluxdemo.repository.TransferRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

@Component
public class DataLoader implements CommandLineRunner {

	private TransferRepository transferRepository;
	
	private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

	DataLoader(final TransferRepository transferMongoReactiveRepository) {
		this.transferRepository = transferMongoReactiveRepository;
	}

	@Override
	public void run(final String... args) throws Exception {

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream("load-test-data.json")));

		ObjectMapper mapper = new ObjectMapper();

		Flux<Transfer> flux = Flux.generate(() -> parser(bufferedReader, mapper), this::pullOrComplete, jsonParser -> {
			try {
				jsonParser.close();
			} catch (IOException e) {
			}
		});

		flux.map(l -> transferRepository.save(l)).subscribe(m -> log.info("Carga Teste: {}", m.block()));
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

	private JsonParser pullOrComplete(JsonParser parser, SynchronousSink<Transfer> emitter) {
		try {
			if (parser.nextToken() != JsonToken.END_ARRAY) {
				Transfer t = parser.readValueAs(Transfer.class);
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

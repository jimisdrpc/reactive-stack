package com.reactive.fluxdemo.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.reactive.fluxdemo.domain.Status;
import com.reactive.fluxdemo.domain.Transfer;
import com.reactive.fluxdemo.repository.StatusRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class StatusController {

//https://docs.spring.io/spring-framework/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html	
	/*
	 * When using stream types like Flux or Observable, the media type specified in
	 * the request/response or at mapping/routing level is used to determine how the
	 * data should be serialized and flushed. For example a REST endpoint that
	 * returns a Flux<User> will be serialized by default as following:
	 * 
	 * application/json: a Flux<User> is handled as an asynchronous collection and
	 * serialized as a JSON array with an explicit flush when the complete event is
	 * emitted.
	 * 
	 * application/stream+json: a Flux<User> will be handled as a stream of User
	 * elements serialized as individual JSON object separated by new lines and
	 * explicitly flushed after each element. The WebClient supports JSON stream
	 * decoding so this is a good use case for server to server use case.an
	 * application/stream+json media isn't a simple JSON. That is useful for telling
	 * our client that we are returning a stream instead of a regular response
	 * entity.
	 * 
	 * text/event-stream: a Flux<User> or Flux<ServerSentEvent<User>> will be
	 * handled as a stream of User or ServerSentEvent elements serialized as
	 * individual SSE elements using by default JSON for data encoding and explicit
	 * flush after each element. This is well suited for exposing a stream to
	 * browser clients. WebClient supports reading SSE streams as well.
	 * 
	 * ---
	 * 
	 * returning a Mono object that wraps the entity that was created. If you look
	 * into the Mono class definition, you will see that it extends a class called
	 * Publisher: a Publisher is a reactive stream that may send data to one or more
	 * subscribers. The subscribers may use the information as it becomes
	 * available--that is, they react as the publisher provides information.
	 */

	@Autowired
	private StatusRepository statusRepository;

	// produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	// Server Sent Events
	@GetMapping(value = "/sse/flux/stream/status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Status> streamAllTransfers() {
		return statusRepository.findAll();
	}

	@GetMapping(value = "/mono/status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	// @GetMapping("/mono/status/{id}")
	public Mono<ResponseEntity<Status>> getStatusById(@PathVariable(value = "id") String statusId) {
		return statusRepository.findById(statusId).map(t -> ResponseEntity.ok(t))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// https://octoperf.com/blog/2019/10/09/kraken-server-sent-events-reactive/
	@GetMapping(value = "/randomNumbers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<Integer>> randomNumbers() {
		return Flux.interval(Duration.ofSeconds(1)).map(seq -> Tuples.of(seq, ThreadLocalRandom.current().nextInt()))
				.map(data -> ServerSentEvent.<Integer>builder().event("random").id(Long.toString(data.getT1()))
						.data(data.getT2()).build());
	}

	@GetMapping(value = "/randomStatus", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<Status>> randomStatus() {
		
        ArrayList<String> statusList = new ArrayList<String>();
        statusList.add("Iniciado");
        statusList.add("Recusado");
        statusList.add("Sucesso");
        
//        for (int i = 0; i < 100; i++) {
//            int x = ThreadLocalRandom.current().nextInt(0, statusList.size());
//            System.out.println(statusList.get(x));
//        }
        
//		return Flux.interval(Duration.ofSeconds(1))
//				.map(seq -> Tuples.of(seq, ThreadLocalRandom.current().nextInt()))
//				.map(data -> ServerSentEvent.<Status>builder().event("random").data(new Status(Long.toString(data.getT1()), ThreadLocalRandom.current().nextInt(0, statusList.size() )
//						.data(data.getT2()).build())));
        
		return Flux.interval(Duration.ofSeconds(1))
				.map(seq -> Tuples.of(seq, ThreadLocalRandom.current().nextInt()))
				.map(data -> ServerSentEvent.<Status>builder().data(
						new Status(Long.toString(data.getT1()),statusList.get(ThreadLocalRandom.current().nextInt(0, statusList.size() ))
						))
						.build());
		
	}
	
}

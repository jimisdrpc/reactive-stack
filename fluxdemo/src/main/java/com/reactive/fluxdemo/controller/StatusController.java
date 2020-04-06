package com.reactive.fluxdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.reactive.fluxdemo.domain.Status;
import com.reactive.fluxdemo.domain.Transfer;
import com.reactive.fluxdemo.repository.StatusRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class StatusController {

	@Autowired
	private StatusRepository statusRepository;

	// Server Sent Events
	@GetMapping(value = "/sse/flux/stream/status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Status> streamAllTransfers() {
		return statusRepository.findAll();
	}

	//@GetMapping(value = "/mono/status/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@GetMapping("/mono/status/{id}")
	public Mono<ResponseEntity<Status>> getStatusById(@PathVariable(value = "id") String statusId) {
		return statusRepository.findById(statusId).map(t -> ResponseEntity.ok(t))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}

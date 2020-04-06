package com.reactive.fluxdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reactive.fluxdemo.domain.Transfer;
import com.reactive.fluxdemo.repository.TransferRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

/*
 * Adapted from https://www.callicoder.com/reactive-rest-apis-spring-webflux-reactive-mongo/
All the controller endpoints return a Publisher in the form of a Flux or a Mono. 
The last endpoint is very interesting where we set the content-type to text/event-stream. 
It sends the Transfer in the form of Server Sent Events to a browser like this -

data: {"id":"59ba5389d2b2a85ed4ebdafa","text":"tweet1","createdAt":1505383305602}
data: {"id":"59ba5587d2b2a85f93b8ece7","text":"tweet2","createdAt":1505383814847}
Now that we’re talking about event-stream, You might ask that doesn’t the following endpoint also return a Stream?

@GetMapping("/tweets")
public Flux<Tweet> getAllTweets() {
    return tweetRepository.findAll();
}
And the answer is Yes. Flux<Tweet> represents a stream of tweets. 
But, by default, it will produce a JSON array because If a stream of individual JSON objects is sent to the browser 
then It will not be a valid JSON document as a whole. 
A browser client has no way to consume a stream other than using Server-Sent-Events or WebSocket.

However, Non-browser clients can request a stream of JSON by setting the Accept header to application/stream+json, 
and the response will be a stream of JSON similar to Server-Sent-Events but without extra formatting :

{"id":"59ba5389d2b2a85ed4ebdafa","text":"tweet1","createdAt":1505383305602}
{"id":"59ba5587d2b2a85f93b8ece7","text":"tweet2","createdAt":1505383814847}
*/
@RestController
public class TransferController {

	@Autowired
	private TransferRepository transferRepository;

	@GetMapping("/transfer")
	public Flux<Transfer> getAllTransfers() {
		return transferRepository.findAll();
	}

	@PostMapping("/transfer")
	public Mono<Transfer> createTransfer(@Valid @RequestBody Transfer transfer) {
		//extratoRepository.save(e).flatMap(result -> { // do something with the result }).subscribe()
		return transferRepository.save(transfer);
	}

	@GetMapping("/transfer/{id}")
	public Mono<ResponseEntity<Transfer>> getTransferById(@PathVariable(value = "id") String transferId) {
		return transferRepository.findById(transferId).map(savedTransfer -> ResponseEntity.ok(savedTransfer))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/transfer/{id}")
	public Mono<ResponseEntity<Transfer>> updateTransfer(@PathVariable(value = "id") String transferId,
			@Valid @RequestBody Transfer transfer) {
		return transferRepository.findById(transferId).flatMap(existingTransfer -> {
			existingTransfer.setStatus(transfer.getStatus());
			return transferRepository.save(existingTransfer);
		}).map(updatedTransfer -> new ResponseEntity<>(updatedTransfer, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/transfer/{id}")
	public Mono<ResponseEntity<Void>> deleteTransfer(@PathVariable(value = "id") String transferId) {

		return transferRepository.findById(transferId)
				.flatMap(existingTransfer -> transferRepository.delete(existingTransfer)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Server Sent Events
	@GetMapping(value = "/stream/transfers", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Transfer> streamAllTransfers() {
		return transferRepository.findAll();
	}

}

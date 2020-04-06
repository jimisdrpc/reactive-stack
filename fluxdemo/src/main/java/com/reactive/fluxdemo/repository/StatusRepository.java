package com.reactive.fluxdemo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.fluxdemo.domain.Status;

@Repository
public interface StatusRepository extends ReactiveMongoRepository<Status, String> {

}

package com.reactive.fluxdemo.repository;

import org.springframework.stereotype.Repository;
import com.reactive.fluxdemo.domain.*;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

@Repository
public interface TransferRepository extends ReactiveMongoRepository<Transfer, String> {

}

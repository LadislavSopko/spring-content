package it.zeroics.strg.api;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.zeroics.strg.model.Medium;
import it.zeroics.strg.model.MediumRepoBase;

@Repository
public interface MediumReopository extends MongoRepository<Medium, String>, MediumRepoBase {}

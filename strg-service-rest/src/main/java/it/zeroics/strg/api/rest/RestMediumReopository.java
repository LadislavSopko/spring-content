package it.zeroics.strg.api.rest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import it.zeroics.strg.model.Medium;
import it.zeroics.strg.model.MediumRepoBase;

@Repository
@RepositoryRestResource(path="medium", collectionResourceRel="medium")
public interface RestMediumReopository extends MongoRepository<Medium, String>, MediumRepoBase {}
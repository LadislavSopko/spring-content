package it.zeroics.strg.api.rest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import it.zeroics.strg.model.Medium;
import it.zeroics.strg.model.MediumRepoBase;


@RepositoryRestResource(path="media", collectionResourceRel="media")
public interface RestMediumReopository extends MediumRepoBase, MongoRepository<Medium, String>{
}
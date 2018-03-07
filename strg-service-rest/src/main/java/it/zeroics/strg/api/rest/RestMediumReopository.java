package it.zeroics.strg.api.rest;

import java.io.Serializable;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import it.zeroics.strg.api.MediumReopository;

@Repository
@RepositoryRestResource(path="medium", collectionResourceRel="medium")
public interface RestMediumReopository<S, SID extends Serializable> extends MediumReopository<S, SID> {
}

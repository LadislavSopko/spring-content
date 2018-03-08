package it.zeroics.strg.api.rest;

import org.springframework.stereotype.Repository;

import internal.org.springframework.content.rest.StoreRestResource;
import it.zeroics.strg.api.MediumContentStore;

@Repository
@StoreRestResource(path="medium")
public interface RestMediumContentStore extends MediumContentStore {}

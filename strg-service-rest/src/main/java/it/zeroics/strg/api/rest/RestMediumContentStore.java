package it.zeroics.strg.api.rest;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import internal.org.springframework.content.rest.StoreRestResource;
import it.zeroics.strg.api.MediumContentStore;

@Repository
@StoreRestResource(path="medium")
public interface RestMediumContentStore<M, SID extends Serializable> extends MediumContentStore<M, SID> {}

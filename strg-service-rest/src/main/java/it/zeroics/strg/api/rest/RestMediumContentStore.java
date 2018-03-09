package it.zeroics.strg.api.rest;

import org.springframework.content.commons.renditions.Renderable;
import org.springframework.content.commons.repository.ContentStore;

import internal.org.springframework.content.rest.StoreRestResource;
import it.zeroics.strg.model.Medium;

@StoreRestResource(path="media")
public interface RestMediumContentStore extends ContentStore<Medium, String>, Renderable<Medium> {}
package it.zeroics.strg.api;


import org.springframework.content.commons.renditions.Renderable;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.stereotype.Repository;

import it.zeroics.strg.model.Medium;

@Repository
public interface MediumContentStore extends ContentStore<Medium, String>, Renderable<Medium> {}

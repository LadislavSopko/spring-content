package it.zeroics.strg.api;

import java.io.Serializable;

import org.springframework.content.commons.renditions.Renderable;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.stereotype.Repository;

@Repository
public interface MediumContentStore<M, SID extends Serializable> extends ContentStore<M, SID>, Renderable<M> {}

package it.zeroics.strg.api;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MediumReopository<S, SID extends Serializable> extends MongoRepository<S, SID> {
	@Query("{$or:[{'_id':?0}, {'contentId':?0}]}")
	S findOne(SID id);
}

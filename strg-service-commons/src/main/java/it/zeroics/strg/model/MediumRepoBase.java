package it.zeroics.strg.model;

import org.springframework.data.mongodb.repository.Query;
import it.zeroics.strg.model.Medium;

public interface MediumRepoBase{
	@Query("{$or:[{'_id':?0}, {'contentId':?0}]}")
	Medium findOne(String id);
}

package it.zeroics.strg.cache;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;



@Component
@CacheConfig(cacheNames = { "files" }) // 2
public class TestCache implements SomeSvc {

	@Cacheable
	@Override
	public ChachedResult go(int i) {
		return new ChachedResult(i, "abc");
	}
	
}

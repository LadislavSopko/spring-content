package it.zeroics.strgtesting;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
//import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import it.zeroics.strg.cache.StrgCacheManager;

@EnableCaching
@SpringBootApplication
// @EnableFilesystemStores(basePackageClasses=it.zeroics.strg.api.rest.RestMediumContentStore.class)
public class StrgRestApplication {

	@Bean // 3
	public CacheManagerCustomizer<StrgCacheManager> cacheManagerCustomizer() {
		return (StrgCacheManager cacheManager) -> cacheManager.setCacheNames(Arrays.asList("files"));
	}

	public static void main(String[] args) {
		SpringApplication.run(StrgRestApplication.class, args);
	}
}

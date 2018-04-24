package it.zeroics.strgtesting;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
//import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import it.zeroics.strg.cache.StrgCache;
import it.zeroics.strg.cache.StrgCacheManager;

@EnableCaching
@SpringBootApplication(scanBasePackages= {"it.zeroics.strg.cache"})
// @ComponentScan(basePackages="it.zeroics.strg.renditions.providers")
// @EnableFilesystemStores(basePackageClasses=it.zeroics.strg.api.rest.RestMediumContentStore.class)
public class StrgRestApplication {

	
	@Bean
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
		return new StrgCacheManager("files");
    }

	
	@Bean // 3
	public CacheManagerCustomizer<StrgCacheManager> cacheManagerCustomizer() {
		return (StrgCacheManager cacheManager) -> cacheManager.setCacheNames(Arrays.asList("files"));
	}

	public static void main(String[] args) {
		SpringApplication.run(StrgRestApplication.class, args);
	}
}

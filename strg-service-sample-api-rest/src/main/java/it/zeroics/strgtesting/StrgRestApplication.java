package it.zeroics.strgtesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.content.fs.config.EnableFilesystemStores;

@SpringBootApplication
@EnableFilesystemStores(basePackageClasses=it.zeroics.strg.api.rest.RestMediumContentStore.class)
public class StrgRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrgRestApplication.class, args);
	}
}

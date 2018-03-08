package it.zeroics.strg.api.rest;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigurationPackage
@Import(it.zeroics.strg.model.ModelConfiguration.class)
public class RestConfiguration {
	public RestConfiguration() {
		System.out.println("Rest config instantiated!");
	}
}

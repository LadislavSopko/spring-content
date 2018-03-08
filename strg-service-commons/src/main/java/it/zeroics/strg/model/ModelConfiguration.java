package it.zeroics.strg.model;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigurationPackage
public class ModelConfiguration {
	public ModelConfiguration() {
		System.out.println("Rest config instantiated!");
	}
}

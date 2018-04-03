package it.zeroics.strg.api;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

// this must add package in packages , so early db repos scan will see it!!
@Configuration
@AutoConfigurationPackage
@EnableMongoRepositories(basePackageClasses=MediumReopository.class)
public class AutoConfigure {
	
}

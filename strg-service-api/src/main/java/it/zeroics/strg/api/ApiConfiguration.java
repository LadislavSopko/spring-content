package it.zeroics.strg.api;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"it.zeroics.strg.model", "it.zeroics.strg.api"})
public class ApiConfiguration {
	
}

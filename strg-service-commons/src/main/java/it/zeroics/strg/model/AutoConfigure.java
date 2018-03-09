package it.zeroics.strg.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigurationPackage
public class AutoConfigure {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	public AutoConfigure() {
		log.debug("Model Auto configure considered.");
	}
}

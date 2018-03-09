package it.zeroics.strg.renditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("it.zeroics.strg.renditions.providers")
public class AutoConfigure {	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	public AutoConfigure() {
		log.debug("Renditions Auto configure considered.");
	}
}


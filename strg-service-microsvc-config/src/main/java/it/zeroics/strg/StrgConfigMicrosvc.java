package it.zeroics.strg;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.micrometer.core.instrument.config.MissingRequiredConfigurationException;

@SpringBootApplication
@EnableConfigServer
public class StrgConfigMicrosvc {

	public static void main(String[] args) {
		SpringApplication.run(StrgConfigMicrosvc.class, args);
	}
	
	@Configuration	
	public static class ApplicationConfig {
		private final Log log = LogFactory.getLog(this.getClass());
		
		@Autowired
		private Environment environment;
		
		
		
		@PostConstruct
	    public void init() {
			System.out.println(System.getProperty("user.dir"));
	    }
	}
}

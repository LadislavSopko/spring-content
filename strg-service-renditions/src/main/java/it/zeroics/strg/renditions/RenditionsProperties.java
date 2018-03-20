package it.zeroics.strg.renditions;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "it.zeroics.strg.renditions", ignoreInvalidFields = false, ignoreUnknownFields = false)
public class RenditionsProperties {	
	private static String imagikCall ;

	public static String getImagikCall() {
		return imagikCall;
	}

	public static void setImagikCall(String imagikCall) {
		RenditionsProperties.imagikCall = imagikCall;
	}
}


package it.zeroics.strg.renditions;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "it.zeroics.strg.renditions", ignoreInvalidFields = false, ignoreUnknownFields = false)
public class RenditionsProperties {	
	private static String imagickCall ;
	private static String imagickMetaCall;

	public static String getImagickCall() {
		return imagickCall;
	}

	public static void setImagickCall(String imagickCall) {
		RenditionsProperties.imagickCall = imagickCall;
	}

	public static String getImagickMetaCall() {
		return imagickMetaCall;
	}

	public static void setImagickMetaCall(String imagickMetaCall) {
		RenditionsProperties.imagickMetaCall = imagickMetaCall;
	}
}


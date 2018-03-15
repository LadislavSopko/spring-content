package it.zeroics.strg.api;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;

// this must add package in packages , so early db repos scan will see it!!
@Configuration
@AutoConfigurationPackage
public class AutoConfigure {
	
}

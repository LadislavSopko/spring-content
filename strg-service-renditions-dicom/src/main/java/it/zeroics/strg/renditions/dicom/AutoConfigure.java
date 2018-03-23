package it.zeroics.strg.renditions.dicom;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.zeroics.strg.renditions.RenditionsProperties;

@Configuration
@ComponentScan("it.zeroics.strg.renditions.dicom.providers")
@Import(RenditionsProperties.class)
public class AutoConfigure {	
}


package it.zeroics.strg.renditions;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("it.zeroics.strg.renditions.providers")
@Import(RenditionsProperties.class)
public class AutoConfigure {	
}


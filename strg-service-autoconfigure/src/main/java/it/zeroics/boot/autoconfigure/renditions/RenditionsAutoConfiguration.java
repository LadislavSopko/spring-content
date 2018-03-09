package it.zeroics.boot.autoconfigure.renditions;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/*
 * Note: This class is selected to be in spring AUTO CONFIG CHAIN see: src/main/resources/META-INF/spring.factories
 * 		 It will import it.zeroics.strg.renditions.AutoConfigure Which declare @ComponentScan annotation
 * 		 so we can tell spring that it have to load all our renditions.
 */

@Configuration
@ConditionalOnClass({it.zeroics.strg.renditions.AutoConfigure.class})
@Import(it.zeroics.strg.renditions.AutoConfigure.class)	
public class RenditionsAutoConfiguration {
}

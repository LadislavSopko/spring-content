package it.zeroics.boot.autoconfigure.model;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mongodb.Mongo;


/*
 * Note: This class is selected to be in spring AUTO CONFIG CHAIN see: src/main/resources/META-INF/spring.factories
 * 		 It will import it.zeroics.strg.model.AutoConfigure Which declare @AutoConfigurationPackage annotation
 * 		 so we can tell spring that it have to load all our repositories.
 */

@Configuration
@ConditionalOnClass({Mongo.class, it.zeroics.strg.model.AutoConfigure.class})
@Import(it.zeroics.strg.model.AutoConfigure.class)
public class ModelAutoConfiguration {
}

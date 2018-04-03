package it.zeroics.boot.autoconfigure.api;

//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
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
@ConditionalOnClass({Mongo.class, it.zeroics.strg.api.AutoConfigure.class})
//@AutoConfigureBefore({internal.org.springframework.content.mongo.boot.autoconfigure.MongoContentAutoConfiguration.class})
@Import(it.zeroics.strg.api.AutoConfigure.class)	
public class ApiContentAutoConfiguration {
}

package it.zeroics.boot.autoconfigure.api;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mongodb.Mongo;


@Configuration
@ConditionalOnClass({Mongo.class, it.zeroics.strg.api.ApiConfiguration.class})

// we have to skip this config if there is rest
// rest will configure inherited interface!!!
@ConditionalOnMissingClass(value="it.zeroics.strg.api.rest.config.RestConfiguration")
@Import(it.zeroics.strg.api.ApiConfiguration.class) // non rest apis	
public class ApiContentAutoConfiguration {
}

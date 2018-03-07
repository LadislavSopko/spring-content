package it.zeroics.boot.autoconfigure.java;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mongodb.Mongo;


@Configuration
@ConditionalOnClass({Mongo.class})

// we have to skip this config if there is rest
// rest will configure inherited interface!!!
@ConditionalOnMissingClass(value="it.zeroics.strg.api.rest.config.RestConfiguration")
@ComponentScan(basePackages = {"it.zeroics.strg.api"}) // non rest apis	
public class JavaContentAutoConfiguration {
}

package it.zeroics.boot.autoconfigure.rest;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.mongodb.Mongo;

import internal.org.springframework.content.mongo.boot.autoconfigure.MongoContentAutoConfiguration;
import it.zeroics.strg.api.rest.config.RestConfiguration;


@Configuration
@ConditionalOnClass({Mongo.class, RestConfiguration.class})
@AutoConfigureBefore({MongoContentAutoConfiguration.class})
//@ComponentScan(basePackages = {"it.zeroics.strg.model", "it.zeroics.strg.api.rest"}) // non rest apis	
public class RestContentAutoConfiguration {
}

package it.zeroics.boot.autoconfigure.rest;


import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.mongodb.Mongo;


@Configuration
@ConditionalOnClass({Mongo.class, it.zeroics.strg.api.rest.RestConfiguration.class})
@AutoConfigureBefore({internal.org.springframework.content.mongo.boot.autoconfigure.MongoContentAutoConfiguration.class, org.springframework.content.rest.config.RestConfiguration.class})
@Import(it.zeroics.strg.api.rest.RestConfiguration.class)	
public class RestContentAutoConfiguration {
}

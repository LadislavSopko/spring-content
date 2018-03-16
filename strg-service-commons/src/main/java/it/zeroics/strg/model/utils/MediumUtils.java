package it.zeroics.strg.model.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.zeroics.strg.model.Medium;

public class MediumUtils {

		public static String serializePretty(Medium m) throws JsonProcessingException {
			return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(m);
		}
		
		public static String serialize(Medium m) throws JsonProcessingException {
			return new ObjectMapper().writeValueAsString(m);
		}
		
		public static Medium deserialize(String m) throws JsonParseException, JsonMappingException, IOException {
			return new ObjectMapper().readValue(m, Medium.class);
		}
}

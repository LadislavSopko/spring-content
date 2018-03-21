package it.zeroics.strg.renditions.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Oggetto contenente i metadati estratti da un file elaborato da un converter.
 * 
 * See it.3di.fcs.entry.Metadata.java
 */
public class Metadata {

	private Map<String, String> meta;
	
	public Metadata(String extension, long size) {
		this.meta = new LinkedHashMap<String, String>();
	}
	
	public void addMeta(String key, String value) {
		this.meta.put(key, value);
	}

	public Map<String, String> getMeta() {
		return meta;
	}

	// Output
	public String toString() {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writerWithDefaultPrettyPrinter().writeValueAsString(meta) ;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public void serialize(OutputStream out) throws IOException {
		String jsonOutString = toString() ;
		out.write(jsonOutString.getBytes()) ;
	}
}

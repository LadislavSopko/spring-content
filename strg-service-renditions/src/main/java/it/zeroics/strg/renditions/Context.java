package it.zeroics.strg.renditions;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

 @Component
public class Context {

    private static Log logger = LogFactory.getLog(Context.class);
	 
	private Set<BasicRenderer> currentConversions = new HashSet<BasicRenderer>();
	/*
	private Map<String,String> converters = new HashMap<String,String>();

	// Utils
	private String mimeToKey(String fromMime, String toMime) {
		return fromMime+"=>"+toMime;
	}
	
	// Prepare
	public void setRecipe(String fromMime, String toMime, String className) {
		synchronized(Context.class) {
			converters.put(mimeToKey(fromMime, toMime), className) ;
	    	logger.debug("Setting recipe for class " + className + " able to convert form " + fromMime + " to " + toMime);
		}		
	}
	*/
	
	// Work
	public InputStream DoWork(InputStream is, BasicRenderer converter) {
		// Choose the converter according from and to Mime
		currentConversions.add(converter); // hold instance so we will remove it when done
		new Thread(converter).start();		
		return converter.getInputStream(); 
	}

	public void WorkerDone(BasicRenderer c) {
		currentConversions.remove(c);
	}
	
	// SINGLETON
	private static Context singleton = null;
	private Context() {}
	public static Context getInstance() {
	  if(singleton == null) {
	     synchronized(Context.class) {
	       if(singleton == null) {
	         singleton = new Context();
	       }
	    }
	  }
	  return singleton;
	}
}

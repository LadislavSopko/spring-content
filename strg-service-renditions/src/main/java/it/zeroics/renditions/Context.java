package it.zeroics.renditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import internal.org.springframework.content.commons.utils.InputContentStream;


public class Context {
	// work
	
	private TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

    private Metadata metadata = new Metadata();
	
	class Converter implements Runnable{
    	
    	PipedInputStream in = null;
		PipedOutputStream out = null;
		
		InputContentStream result = null;
	
		TikaConfig tikaConfig = null;
		Metadata metadata = null;
		InputStream is = null;	
    	
    	Converter(InputStream is, TikaConfig tikaConfig,  Metadata metadata){
    		this.is = is;
    		this.tikaConfig = tikaConfig;
    		this.metadata = metadata;
    		
    		in = new PipedInputStream();
    		try {
				out = new PipedOutputStream(in);
				InputContentStream ics = (InputContentStream)is;
				if(ics != null) {
					result = new InputContentStream(in, ics.getEntity(), "text/plain");
				}else {
					result = new InputContentStream(in, null, "text/plain");
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	public void run(){
    		AutoDetectParser parser = new AutoDetectParser(tikaConfig);
    		BodyContentHandler handler = new BodyContentHandler(out);
    		TikaInputStream stream = TikaInputStream.get(is); //, metadata);
    		
	    	try {
				parser.parse(stream, handler, metadata, new ParseContext());
				out.close();
				//out.flush();
			} catch (IOException | SAXException | TikaException e) {
				// TODO Auto-generated catch block
				e. printStackTrace();
			}
	    	
	    	// remove worker!!!
	    	Context.getInstance().WorkerDone(this);
	    }
    	
    	public InputStream getInputStream() {
    		return result;
    	}
    }
	
	private Set<Converter> currentConversions = new HashSet<Converter>();
	public void WorkerDone(Converter c) {
		currentConversions.remove(c);
	}
	
	public InputStream DoWork(InputStream is) {
		Converter c = new Converter(is, tikaConfig, metadata);
		currentConversions.add(c); // hold instance so we will remove it when done
		new Thread(c).start();		
		return c.getInputStream(); 
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

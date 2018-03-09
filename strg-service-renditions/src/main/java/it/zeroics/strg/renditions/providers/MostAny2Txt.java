package it.zeroics.strg.renditions.providers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;


import org.apache.tika.io.TikaInputStream;
import org.apache.tika.parser.ParseContext;

import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.stereotype.Service;
//import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import org.xml.sax.SAXException;

import it.zeroics.strg.renditions.Context;
import it.zeroics.strg.renditions.RenditionException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

@Service
public class MostAny2Txt implements RenditionProvider {

    private static Log logger = LogFactory.getLog(MostAny2Txt.class);

    
    public MostAny2Txt() {
        
    };

    

    @Override
    public String consumes() {
        return "*/*"; //"application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    }

    @Override
    public String[] produces() {
        return new String[] {"text/plain"};
    }
    
    @Override
	public Boolean isCapable(String fromMimeType, String toMimeType) {
    	logger.debug("Mime check: " + fromMimeType + " -> " + toMimeType);
    	return MimeType.valueOf(toMimeType).includes(MimeType.valueOf("text/plain")) && MimeType.valueOf("*/*").includes(MimeType.valueOf(fromMimeType));
	}

    @SuppressWarnings("resource")
    @Override
    public InputStream convert(InputStream fromInputSource, String toMimeType) {

        Assert.notNull(fromInputSource, "input source must not be null");

        
        try {
        	
        	return Context.getInstance().DoWork(fromInputSource);
        	            
        } catch (Exception e) {
            throw new RenditionException(String.format("Unexpected error reading input attempting to get mime-type rendition %s", toMimeType), e);
        }
        
    }



	
}

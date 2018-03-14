package it.zeroics.strg.renditions.providers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.stereotype.Service;
//import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

import it.zeroics.strg.renditions.BasicRenderer;
import it.zeroics.strg.renditions.Context;
import it.zeroics.strg.renditions.RenditionException;
import it.zeroics.strg.renditions.TikaRenderer;

import java.io.InputStream;

@Service
public class MostAny2Txt extends BasicProvider {

    private static Log logger = LogFactory.getLog(MostAny2Txt.class);

    
    public MostAny2Txt() {
		super() ;
    };

    @Override
	public RenditionCapability isCapable(String fromMimeType, String toMimeType) {
    	logger.debug("Mime check: " + fromMimeType + " -> " + toMimeType);
    	MimeType fromMime = MimeType.valueOf(fromMimeType) ;
    	MimeType toMime = MimeType.valueOf(toMimeType) ;
    	
    	if (toMime.includes(MimeType.valueOf("text/plain")) &&
    			MimeType.valueOf("*/*").includes(fromMime) ) {
    		return RenditionCapability.BEST_FIT;
    	}
    	if (BasicRenderer.justMeta(toMime) &&
    			!fromMime.getSubtype().equals("dicom") && // TODO: Tika is able but not very able, should return a "vote".
    			MimeType.valueOf("*/*").includes(fromMime) ) {
			return RenditionCapability.PRETTY_CAPABLE ;
    	}
		return RenditionCapability.NOT_CAPABLE;
	}

    @SuppressWarnings("resource")
    @Override
    public InputStream convert(InputStream fromInputSource, String toMimeType) {

        Assert.notNull(fromInputSource, "input source must not be null");

        
        try {
    		BasicRenderer converter = new TikaRenderer(fromInputSource, MimeType.valueOf(toMimeType));
        	return Context.getInstance().DoWork(fromInputSource, converter);
        	            
        } catch (Exception e) {
            throw new RenditionException(String.format("Unexpected error reading input attempting to get mime-type rendition %s", toMimeType), e);
        }
    }
}

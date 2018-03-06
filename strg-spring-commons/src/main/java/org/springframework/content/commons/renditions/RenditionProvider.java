package org.springframework.content.commons.renditions;

import java.io.InputStream;

public interface RenditionProvider {

	@Deprecated
    public String consumes();
	@Deprecated
    public String[] produces();
	
	
    public InputStream convert(InputStream fromInputSource, String toMimeType);
    public Boolean isCapable(String fromMimeType, String toMimeType);
    
	  
}

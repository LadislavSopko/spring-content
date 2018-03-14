package org.springframework.content.commons.renditions;

import java.io.InputStream;

public interface RenditionProvider {

	public static final int NOT_CAPABLE = 0;
	public static final int LOW_CAPABILITY = 1;
	public static final int PRETTY_CAPABLE = 2;
	public static final int GOOD_CAPABILITY = 3;
	public static final int BEST_FIT = 4;	
	
	@Deprecated
    public String consumes();
	@Deprecated
    public String[] produces();
	
	
    public InputStream convert(InputStream fromInputSource, String toMimeType);
    public RenditionCapability isCapable(String fromMimeType, String toMimeType);
    
	  
}

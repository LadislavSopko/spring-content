package org.springframework.content.commons.renditions;

import java.io.InputStream;

import org.springframework.core.io.Resource;

public interface RenditionProvider {

	public String consumes();
    public Boolean consumes(String fromMimeType);
    public String[] produces();
	
    public RenditionCapability isCapable(String fromMimeType, String toMimeType);
	
    @Deprecated
    public InputStream convert(InputStream fromInputSource, String toMimeType);
    //public Resource convert(Resource fromResource, String toMimeType);
}

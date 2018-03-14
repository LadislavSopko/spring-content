package internal.org.springframework.content.commons.renditions;

import java.io.InputStream;

import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.util.MimeType;

public class DummyRenderer implements RenditionProvider{
	@Override
    public String consumes() {
		throw new UnsupportedOperationException();
    }

    @Override
    public String[] produces() {
    	throw new UnsupportedOperationException();
    }
    
    @Override
   	public RenditionCapability isCapable(String fromMimeType, String toMimeType) {
   		if (MimeType.valueOf(toMimeType).includes(MimeType.valueOf("something/else"))  && 
   			MimeType.valueOf("one/thing").includes(MimeType.valueOf(fromMimeType))) return RenditionCapability.GOOD_CAPABILITY;
   		return RenditionCapability.NOT_CAPABLE;
   	}

    @SuppressWarnings("resource")
    @Override
    public InputStream convert(InputStream fromInputSource, String toMimeType) {

        
        return null;
    }
}

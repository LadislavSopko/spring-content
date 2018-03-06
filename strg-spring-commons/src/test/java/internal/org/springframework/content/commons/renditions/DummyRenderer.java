package internal.org.springframework.content.commons.renditions;

import java.io.InputStream;

import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.util.MimeType;

public class DummyRenderer implements RenditionProvider{
	@Override
    public String consumes() {
        return "one/thing";
    }

    @Override
    public String[] produces() {
        return new String[] {"something/else"};
    }
    
    @Override
   	public Boolean isCapable(String fromMimeType, String toMimeType) {
   		return MimeType.valueOf(toMimeType).includes(MimeType.valueOf("something/else"))  && 
   			   MimeType.valueOf("one/thing").includes(MimeType.valueOf(fromMimeType));
   	}

    @SuppressWarnings("resource")
    @Override
    public InputStream convert(InputStream fromInputSource, String toMimeType) {

        
        return null;
    }
}

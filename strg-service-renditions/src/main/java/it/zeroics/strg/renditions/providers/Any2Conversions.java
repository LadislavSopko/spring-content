package it.zeroics.strg.renditions.providers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.stereotype.Service;
//import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

import internal.org.springframework.content.commons.renditions.BasicRenderer;
import internal.org.springframework.content.commons.renditions.RenditionContext;
import it.zeroics.strg.renditions.CapabilityRenderer;
import it.zeroics.strg.renditions.DicomRenderer;
import it.zeroics.strg.renditions.RenditionException;
import it.zeroics.strg.renditions.utils.MimeHelper;

import java.io.InputStream;

@Service
public class Any2Conversions extends BasicProvider {

	private static Log logger = LogFactory.getLog(Any2Conversions.class);

	public Any2Conversions() {
		super() ;
	};

	@Override
	public Boolean consumes(String fromMimeType) {
		return true;
	}
	
	@Override
	public String[] produces() {
		return new String[]{MimeHelper.CAPABILITY_MIMETYPE} ;
	}

	@Override
	public RenditionCapability isCapable(String fromMimeType, String toMimeType) {
		logger.debug("Mime check: " + fromMimeType + " -> " + toMimeType);
		MimeType toMime = MimeType.valueOf(toMimeType) ;
		if ( MimeHelper.isCapability(toMime) ) {
			return RenditionCapability.BEST_FIT;
		}
		return RenditionCapability.NOT_CAPABLE;
	}

	@SuppressWarnings("resource")
	@Override
	public InputStream convert(InputStream fromInputSource, String toMimeType) {

		Assert.notNull(fromInputSource, "input source must not be null");

		try {
    		BasicRenderer converter = new CapabilityRenderer(fromInputSource, MimeType.valueOf(toMimeType));
			return RenditionContext.getInstance().DoWork(fromInputSource, converter);

		} catch (Exception e) {
			throw new RenditionException(String
					.format("Unexpected error reading input attempting to get mime-type rendition %s", toMimeType), e);
		}

	}

}

package it.zeroics.strg.renditions.providers;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.stereotype.Service;
//import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

import it.zeroics.strg.renditions.BasicRenderer;
import it.zeroics.strg.renditions.Context;
import it.zeroics.strg.renditions.ImageRenderer;
import it.zeroics.strg.renditions.RenditionException;

@Service
public class AnyImage2Any extends BasicProvider {

	private static Log logger = LogFactory.getLog(AnyImage2Any.class);

	public AnyImage2Any() {
		super() ;
	};

	@Override
	public RenditionCapability isCapable(String fromMimeType, String toMimeType) {
		logger.debug("Mime check: " + fromMimeType + " -> " + toMimeType);
		MimeType toMime = MimeType.valueOf(toMimeType) ;
		if (toMime.includes(MimeType.valueOf("application/pdf")) || toMime.getType().equals("image")) {
			if ( MimeType.valueOf(fromMimeType).getType().equals("image") ) {
				return RenditionCapability.GOOD_CAPABILITY;
			}
		}
		return RenditionCapability.NOT_CAPABLE;
	}

	@SuppressWarnings("resource")
	@Override
	public InputStream convert(InputStream fromInputSource, String toMimeType) {

		Assert.notNull(fromInputSource, "input source must not be null");

		try {
    		BasicRenderer converter = new ImageRenderer(fromInputSource, MimeType.valueOf(toMimeType));
			return Context.getInstance().DoWork(fromInputSource, converter);

		} catch (Exception e) {
			throw new RenditionException(String
					.format("Unexpected error reading input attempting to get mime-type rendition %s", toMimeType), e);
		}

	}

}

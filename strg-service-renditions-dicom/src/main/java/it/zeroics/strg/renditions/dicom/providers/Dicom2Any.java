package it.zeroics.strg.renditions.dicom.providers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
//import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

import internal.org.springframework.content.commons.renditions.BasicRenderer;
import internal.org.springframework.content.commons.renditions.RenditionContext;
import it.zeroics.strg.renditions.RenditionException;
import it.zeroics.strg.renditions.dicom.DicomRenderer;
import it.zeroics.strg.renditions.providers.BasicProvider;
import it.zeroics.strg.renditions.utils.MimeHelper;

@Service
public class Dicom2Any extends BasicProvider {

	private static Log logger = LogFactory.getLog(Dicom2Any.class);

	public Dicom2Any() {
		super();
	};

	@Override
	public Boolean consumes(String fromMimeType) {
		if (MimeType.valueOf(fromMimeType).getSubtype().equals("dicom")) {
			return true;
		}
		return false;
	}

	@Override
	public String[] produces() {
		return new String[] { MimeHelper.METADATA_MIMETYPE };
	}

	@Override
	public RenditionCapability isCapable(String fromMimeType, String toMimeType) {
		logger.debug("Mime check: " + fromMimeType + " -> " + toMimeType);
		MimeType toMime = MimeType.valueOf(toMimeType);
		if (MimeHelper.isMeta(toMime) && consumes(fromMimeType)) {
			return RenditionCapability.BEST_FIT;
		}
		return RenditionCapability.NOT_CAPABLE;
	}

	@SuppressWarnings("resource")
	@Override
	public Resource convert(Resource fromInputSource, String toMimeType) {

		Assert.notNull(fromInputSource, "input source must not be null");

		try {
			BasicRenderer converter = new DicomRenderer(fromInputSource, MimeType.valueOf(toMimeType));
			return RenditionContext.getInstance().DoWork(converter);

		} catch (Exception e) {
			throw new RenditionException(String
					.format("Unexpected error reading input attempting to get mime-type rendition %s", toMimeType), e);
		}

	}

}

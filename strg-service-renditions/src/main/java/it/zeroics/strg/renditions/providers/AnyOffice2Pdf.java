package it.zeroics.strg.renditions.providers;

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
import it.zeroics.strg.renditions.PdfRenderer;
import it.zeroics.strg.renditions.RenditionException;

@Service
public class AnyOffice2Pdf extends BasicProvider {

	private static Log logger = LogFactory.getLog(AnyOffice2Pdf.class);

	public AnyOffice2Pdf() {
		super();
	};

	@Override
	public Boolean consumes(String fromMimeType) {
		if (fromMimeType.startsWith("application/vnd.oasis.opendocument.") || // MimeType.valueOf("application/vnd.oasis.opendocument.*").includes(MimeType.valueOf(fromMimeType))
																				// || // [Open|Libre]Office
				fromMimeType.startsWith("application/vnd.openxmlformats-officedocument.") || // MimeType.valueOf("application/vnd.openxmlformats-officedocument.*").includes(MimeType.valueOf(fromMimeType))
																								// || // Modern MSOffice
																								// .<ext>x
				MimeType.valueOf(fromMimeType).includes(MimeType.valueOf("application/msword")) || // Microsoft Word
																									// .doc
				MimeType.valueOf(fromMimeType).includes(MimeType.valueOf("application/vnd.ms-powerpoint")) || // Microsoft
																												// Power
																												// Point
																												// .ppt
				MimeType.valueOf(fromMimeType).includes(MimeType.valueOf("application/vnd.ms-excel")) // Microsoft Power
																										// Point .xls
		) {
			return true;
		}
		return false;
	}

	@Override
	public String[] produces() {
		return new String[] { "application/pdf" };
	}

	@Override
	public RenditionCapability isCapable(String fromMimeType, String toMimeType) {
		logger.debug("Mime check: " + fromMimeType + " -> " + toMimeType);
		MimeType toMime = MimeType.valueOf(toMimeType);
		if (toMime.includes(MimeType.valueOf("application/pdf"))) {
			/*
			 * if ( MimeType.valueOf(fromMimeType).isCompatibleWith(MimeType.valueOf(
			 * "application/vnd.oasis.opendocument.*")) ) {
			 * logger.debug("Compatibile with application/vnd.oasis.opendocument.*"); } if (
			 * MimeType.valueOf(fromMimeType).isCompatibleWith(MimeType.valueOf(
			 * "application/vnd.openxmlformats-officedocument.*")) ) { logger.
			 * debug("Compatibile with application/vnd.openxmlformats-officedocument.*"); }
			 */
			if (consumes(fromMimeType)) {
				return RenditionCapability.BEST_FIT;
			}
		}
		return RenditionCapability.NOT_CAPABLE;
	}

	@SuppressWarnings("resource")
	@Override
	public Resource convert(Resource fromInputSource, String toMimeType) {

		Assert.notNull(fromInputSource, "input source must not be null");

		try {
			BasicRenderer converter = new PdfRenderer(fromInputSource, MimeType.valueOf(toMimeType));
			return RenditionContext.getInstance().DoWork(converter);

		} catch (Exception e) {
			throw new RenditionException(String
					.format("Unexpected error reading input attempting to get mime-type rendition %s", toMimeType), e);
		}

	}

}

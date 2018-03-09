package it.zeroics.strg.renditions.providers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.stereotype.Service;
//import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

import it.zeroics.strg.renditions.BasicRenderer;
import it.zeroics.strg.renditions.Context;
import it.zeroics.strg.renditions.DicomRenderer;
import it.zeroics.strg.renditions.RenditionException;

import java.io.InputStream;

@Service
public class Dicom2Any implements RenditionProvider {

	private static Log logger = LogFactory.getLog(Dicom2Any.class);

	public Dicom2Any() {

	};

	@Override
	public String consumes() {
		return "*/dicom"; // "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	}

	@Override
	public String[] produces() {
		return new String[] { "application/json" };
	}

	@Override
	public Boolean isCapable(String fromMimeType, String toMimeType) {
		logger.debug("Mime check: " + fromMimeType + " -> " + toMimeType);
		MimeType fromMime = MimeType.valueOf(fromMimeType) ;
		MimeType toMime = MimeType.valueOf(toMimeType) ;
		if ( BasicRenderer.justMeta(toMime) && fromMime.getSubtype().equals("dicom")) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("resource")
	@Override
	public InputStream convert(InputStream fromInputSource, String toMimeType) {

		Assert.notNull(fromInputSource, "input source must not be null");

		try {
    		BasicRenderer converter = new DicomRenderer(fromInputSource, MimeType.valueOf(toMimeType));
			return Context.getInstance().DoWork(fromInputSource, converter);

		} catch (Exception e) {
			throw new RenditionException(String
					.format("Unexpected error reading input attempting to get mime-type rendition %s", toMimeType), e);
		}

	}

}

package it.zeroics.strg.renditions.providers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.stereotype.Service;

import java.io.InputStream;

//@Service
public class BasicProvider implements RenditionProvider {

	private static Log logger = LogFactory.getLog(BasicProvider.class);

	public BasicProvider() {

	};

	@Override
	public String consumes() {
		throw new UnsupportedOperationException("Deprecated method:use isCapable instead");
	}

	@Override
	public String[] produces() {
		throw new UnsupportedOperationException("Deprecated method:use isCapable instead");
	}

	@Override
	public RenditionCapability isCapable(String fromMimeType, String toMimeType) {
		throw new UnsupportedOperationException("Deprecated method:use isCapable instead");
	}

	@SuppressWarnings("resource")
	@Override
	public InputStream convert(InputStream fromInputSource, String toMimeType) {
		throw new UnsupportedOperationException("Deprecated method:use isCapable instead");
	}

}

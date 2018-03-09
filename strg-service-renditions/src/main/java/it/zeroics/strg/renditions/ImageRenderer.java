package it.zeroics.strg.renditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.document.DocumentFamily;
import org.jodconverter.document.DocumentFormat;
//import org.jodconverter.document.DocumentFormat;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

//import gettingstarted.springcontentfs.File;
import internal.org.springframework.content.commons.utils.InputContentStream;

@Component
public class ImageRenderer extends BasicRenderer {
	private static final Log logger = LogFactory.getLog(ImageRenderer.class);
	
	public ImageRenderer(InputStream is, MimeType mt) {
		super(is, mt);
	}
	
	@Override
	public void run() {

		// remove worker!!!
		Context.getInstance().WorkerDone(this);
	}
}

package it.zeroics.strg.renditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.data.VR;
import org.dcm4che3.tool.common.DicomFiles;
import org.springframework.content.commons.renditions.RenditionService;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import internal.org.springframework.content.commons.renditions.BasicRenderer;
import internal.org.springframework.content.commons.renditions.RenditionContext;
//import gettingstarted.springcontentfs.File;
import internal.org.springframework.content.commons.utils.InputContentStream;
import it.zeroics.strg.renditions.utils.Metadata;
import it.zeroics.strg.model.Medium;


@Component
public class CapabilityRenderer extends BasicRenderer {
	private static final Log logger = LogFactory.getLog(TikaRenderer.class);
	private Metadata meta ;
	
	public CapabilityRenderer(InputStream is, MimeType mt) {
		super(is, mt);
	}
	
	@Override
	public void run() {
		File dicomFile = null ;

		try {
			// Obtain RenditionService implementation and ask for RenditionProviders capabilities
			RenditionService rs = RenditionContext.getInstance().getRenditionService() ;
			
			String[] outputMimeTypes = rs.conversions(((Medium)((InputContentStream)is).getEntity()).getMimeType());
			
			ObjectMapper om = new ObjectMapper();
			try {
				String jsonOutString = om.writerWithDefaultPrettyPrinter().writeValueAsString(outputMimeTypes) ;
				out.write(jsonOutString.getBytes()) ;
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.close();
		} catch(IllegalArgumentException | IOException | SecurityException e) {
			e. printStackTrace();
		}
		finally {
			if ( null != dicomFile ) {
				dicomFile.delete();
			}
		}

		// remove worker!!!
		RenditionContext.getInstance().WorkerDone(this);
	}
}

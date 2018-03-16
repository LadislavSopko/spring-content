package it.zeroics.strg.renditions;

import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import internal.org.springframework.content.commons.renditions.BasicRenderer;
import internal.org.springframework.content.commons.renditions.RenditionContext;
//import gettingstarted.springcontentfs.File;
import internal.org.springframework.content.commons.utils.InputContentStream;
import it.zeroics.strg.renditions.utils.Metadata;
import it.zeroics.strg.model.Medium;


@Component
public class DicomRenderer extends BasicRenderer {
	private static final Log logger = LogFactory.getLog(TikaRenderer.class);
	private Metadata meta ;
	
    public class myVisitor implements Attributes.Visitor {
        public boolean visit(Attributes attrs, int tag, VR vr, Object value) throws Exception {
        	// String key = TagUtils.toString(tag);
        	String key = ElementDictionary.keywordOf(tag, null) ;
        	// key += " (" + vr.toString() + ")";
        	StringBuilder val = new StringBuilder() ;
        	vr.prompt(value, attrs.bigEndian(), attrs.getSpecificCharacterSet(vr), 1024, val) ;
        	meta.addMeta(key, val.toString());
        	return true;
        }
    }
	public class myCallback implements DicomFiles.Callback {
        public boolean dicomFile(File f, Attributes fmi, long dsPos, Attributes ds) throws Exception {
        	fmi.accept(new myVisitor(), true) ;
        	ds.accept(new myVisitor(), true) ;
        	meta.addMeta("Summary", fmi.toString()+ds.toString());
        	return true;
        }
	}
	
	public DicomRenderer(InputStream is, MimeType mt) {
		super(is, mt);
	}
	
	@Override
	public void run() {
		File dicomFile = null ;

		try {
			// See code from fca-fcs
			// Create output file than generate input stream from it.
			//String mediaMimeType = ((gettingstarted.springcontentfs.File)((InputContentStream)is).getEntity()).getMimeType() ;
			String mediaName = ((Medium)((InputContentStream)is).getEntity()).getName();
			String inputExtension = FilenameUtils.getExtension(mediaName);
			
			dicomFile = File.createTempFile("dicom-", "."+inputExtension);
			FileUtils.copyInputStreamToFile(is, dicomFile);
			meta = new Metadata(dicomFile.getAbsolutePath(), dicomFile.getUsableSpace()) ;

			List<String> l = new Vector<String>() ;
			l.add(dicomFile.getAbsolutePath()) ;
			DicomFiles.scan(l, new DicomRenderer.myCallback()) ;
	        meta.serialize(out);
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

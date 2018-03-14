package internal.org.springframework.content.docx4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.docx4j.TextUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

@Service
public class WordToTextRenditionProvider implements RenditionProvider {

	@Override
	public String consumes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String[] produces() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public RenditionCapability isCapable(String fromMimeType, String toMimeType) {
		if (MimeType.valueOf(toMimeType).includes(MimeType.valueOf("text/plain")) && MimeType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document").includes(MimeType.valueOf(fromMimeType))) return RenditionCapability.GOOD_CAPABILITY;
		return RenditionCapability.NOT_CAPABLE;
	}


	@Override
	public InputStream convert(InputStream fromInputSource, String toMimeType) {
		try {
			WordprocessingMLPackage pkg = WordprocessingMLPackage.load(fromInputSource);
	
			MainDocumentPart documentPart = pkg.getMainDocumentPart();		
			
			org.docx4j.wml.Document wmlDocumentEl = (org.docx4j.wml.Document)documentPart.getJaxbElement();
			
			OutputStream os = new FileOutputStream("/tmp/temp.txt");
			Writer out = new OutputStreamWriter(os);
			
			TextUtils.extractText(wmlDocumentEl, out);
			out.close();
			
			if (pkg.getMainDocumentPart().getFontTablePart()!=null) {
				pkg.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
			}		
			// This would also do it, via finalize() methods
			pkg = null;

			return new FileInputStream("/tmp/temp.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}

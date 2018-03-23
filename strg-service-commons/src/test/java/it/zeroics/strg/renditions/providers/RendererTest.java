package it.zeroics.strg.renditions.providers;

import internal.org.springframework.content.commons.utils.InputContentStream;
import it.zeroics.strg.model.Medium;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.renditions.RenditionProvider;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;


public class RendererTest {
	private static final Log LOGGER = LogFactory.getLog(RendererTest.class);

	public static Boolean testDeprecated(RenditionProvider provider) {
		try {
			provider.consumes();
			return false ;
		}
		catch(UnsupportedOperationException e) {
			// OK
		}
		return true ;
	}

    public InputStream callConverterFromInputStream(InputStream is, String fileIn, String mimeTypeIn, String mimeTypeOut, RenditionProvider provider) throws Exception {
		Medium m = new Medium() ;
		m.setMimeType(mimeTypeIn);
		m.setName(fileIn);
		InputContentStream ics = new InputContentStream(is, m) ;
		return provider.convert(ics, mimeTypeOut);
    }

    public InputStream callConverterFromFileName(String fileIn, String mimeTypeIn, String mimeTypeOut, RenditionProvider provider) throws Exception {
    	return callConverterFromInputStream(this.getClass().getResourceAsStream("/" + fileIn), fileIn, mimeTypeIn, mimeTypeOut, provider) ;
    }

    public Boolean compareAsString(InputStream converted, String fileOut) throws Exception {
		StringWriter convertedWriter = new StringWriter();
		IOUtils.copy(converted, convertedWriter, "UTF-8");
		String convertedString = convertedWriter.toString();
				
		InputStream is = this.getClass().getResourceAsStream("/" + fileOut);
		StringWriter compareWriter = new StringWriter();
		IOUtils.copy(is, compareWriter, "UTF-8");
		String compareString = compareWriter.toString();
		
		// Little norm.
		convertedString = convertedString.replaceAll("\r", "") ;
		compareString = compareString.replaceAll("\r", "") ;

		return convertedString.equals(compareString);
    }
    
    public Boolean compareAsByteArray(InputStream converted, String fileOut) throws Exception {
    	byte[] a = IOUtils.toByteArray(converted) ;
    	byte[] b = IOUtils.toByteArray(this.getClass().getResourceAsStream("/" + fileOut));
    	return Arrays.equals(a, b) ;
    	// return Arrays.equals(IOUtils.toByteArray(converted), IOUtils.toByteArray(this.getClass().getResourceAsStream("/" + fileOut))) ;
    }
}

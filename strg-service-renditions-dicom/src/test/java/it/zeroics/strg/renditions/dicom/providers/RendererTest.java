package it.zeroics.strg.renditions.dicom.providers;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.io.MedializedResource;
import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class RendererTest {
	private static final Log LOGGER = LogFactory.getLog(RendererTest.class);

	public static Boolean testDeprecated(RenditionProvider provider) {
		try {
			provider.consumes();
			return false;
		} catch (UnsupportedOperationException e) {
			// OK
		}
		return true;
	}

	public Resource callConverterFromInputStream(Resource ir, String fileIn, String mimeTypeIn, String mimeTypeOut,
			RenditionProvider provider) throws Exception {
		return provider.convert(new MedializedResource(ir, mimeTypeIn, fileIn), mimeTypeOut);
	}

	public Resource callConverterFromFileName(String fileIn, String mimeTypeIn, String mimeTypeOut,
			RenditionProvider provider) throws Exception {
		return callConverterFromInputStream(new ClassPathResource("/" + fileIn), fileIn, mimeTypeIn, mimeTypeOut,
				provider);
	}

	public Boolean compareAsString(Resource converted, String fileOut) throws Exception {
		StringWriter convertedWriter = new StringWriter();
		IOUtils.copy(converted.getInputStream(), convertedWriter, "UTF-8");
		String convertedString = convertedWriter.toString();

		InputStream is = this.getClass().getResourceAsStream("/" + fileOut);
		StringWriter compareWriter = new StringWriter();
		IOUtils.copy(is, compareWriter, "UTF-8");
		String compareString = compareWriter.toString();

		// Little norm.
		convertedString = convertedString.replaceAll("\r", "");
		compareString = compareString.replaceAll("\r", "");

		return convertedString.equals(compareString);
	}

	public Boolean compareAsByteArray(InputStream converted, String fileOut) throws Exception {
		byte[] a = IOUtils.toByteArray(converted);
		byte[] b = IOUtils.toByteArray(this.getClass().getResourceAsStream("/" + fileOut));
		return Arrays.equals(a, b);
		// return Arrays.equals(IOUtils.toByteArray(converted),
		// IOUtils.toByteArray(this.getClass().getResourceAsStream("/" + fileOut))) ;
	}
}

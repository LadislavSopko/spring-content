package internal.org.springframework.content.docx4j;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.util.MimeType;

public class JpegToPngRenditionProvider implements RenditionProvider {

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
		if (MimeType.valueOf(toMimeType).includes(MimeType.valueOf("image/png")) && MimeType.valueOf("image/jpeg").includes(MimeType.valueOf(fromMimeType))) return RenditionCapability.GOOD_CAPABILITY;
		return RenditionCapability.NOT_CAPABLE;
	}
	
	@Override
	public InputStream convert(InputStream fromInputSource, String toMimeType) {
		try {
			// read a jpeg from a inputFile
			BufferedImage bufferedImage = ImageIO.read(fromInputSource);
	
			// write the bufferedImage back to outputFile
			ImageIO.write(bufferedImage, "png", new File("/tmp/temp.png"));
	
			return new FileInputStream("/tmp/temp.png");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fromInputSource);
		}
		return null;
	}

	
}

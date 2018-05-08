package it.zeroics.strg.renditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.jodconverter.document.DocumentFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.content.commons.io.DefaultMediaResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import com.sun.star.form.binding.IncompatibleTypesException;

import internal.org.springframework.content.commons.renditions.BasicRenderer;
import internal.org.springframework.content.commons.renditions.RenditionContext;
import it.zeroics.strg.renditions.utils.MimeHelper;

@Component
public class ImageRenderer extends BasicRenderer {
	@Value("${imagik.call}")
	private static String iMagikCall;

	private static final Log logger = LogFactory.getLog(ImageRenderer.class);

	private static final String SOURCE_FILE_REPLACE_IN_COMMAND = "%SOURCE_FILE%";
	private static final String DEST_FILE_REPLACE_IN_COMMAND = "%DEST_FILE%";
	private static final String OPTIONS_REPLACE_IN_COMMAND = "%OPTIONS%";

	public ImageRenderer(Resource ir, MimeType mt) {
		super(ir, mt);
		RenditionContext.getInstance().setSupportedExtension("application/pdf", ".pdf");
		RenditionContext.getInstance().setSupportedExtension("image/gif", ".gif");
		RenditionContext.getInstance().setSupportedExtension("image/png", ".png");
		RenditionContext.getInstance().setSupportedExtension("image/x-rgb", ".rgb");
		RenditionContext.getInstance().setSupportedExtension("image/x-windows-bmp", ".bmp");
		RenditionContext.getInstance().setSupportedExtension("image/bmp", ".bmp");
		RenditionContext.getInstance().setSupportedExtension("image/x-portable-bitmap", ".pbm");
		RenditionContext.getInstance().setSupportedExtension("image/x-icon", ".ico");
		RenditionContext.getInstance().setSupportedExtension("image/pjpeg", ".jpg");
		RenditionContext.getInstance().setSupportedExtension("image/jpeg", ".jpg");
		RenditionContext.getInstance().setSupportedExtension("image/pjpg", ".jpg");
		RenditionContext.getInstance().setSupportedExtension("image/jpg", ".jpg");
		RenditionContext.getInstance().setSupportedExtension("image/tiff", ".tif");
		RenditionContext.getInstance().setSupportedExtension("image/x-tiff", ".tif");
		RenditionContext.getInstance().setSupportedExtension("image/tif", ".tif");
		RenditionContext.getInstance().setSupportedExtension("image/x-tif", ".tif");
		RenditionContext.getInstance().setSupportedExtension(MimeHelper.METADATA_MIMETYPE, ".json");
	}

	@Override
	public void run() {
		File originalFile = null;
		File outputFile = null;
		InputStream procOutput = null;
		try {
			// See code from fca-fcs
			// Create output file than generate input stream from it.
			// String mediaMimeType =
			// ((gettingstarted.springcontentfs.File)((InputContentStream)is).getEntity()).getMimeType()
			// ;
			String mediaName = ((DefaultMediaResource) ir).getName();
			String inputExtension = FilenameUtils.getExtension(mediaName);

			originalFile = File.createTempFile("image-", "." + inputExtension);
			FileUtils.copyInputStreamToFile(ir.getInputStream(), originalFile);
			// Create file according to out mime type.
			String ext = outputMimeType.getSubtype();
			switch (ext) {
			case MimeHelper.METADATA_MIMESUBTYPE:
				ext = "json";
				break;
			case "pdf":
				break;
			case "gif":
				break;
			case "png":
				break;
			case "x-rgb":
				ext = "rgb";
			case "x-windows-bmp":
				ext = "bmp";
			case "bmp":
				break;
			case "x-portable-bitmap":
				ext = "pbm";
				break;
			case "x-icon":
				ext = "ico";
				break;
			case "pjpeg":
			case "jpeg":
			case "pjpg":
			case "jpg":
				ext = "jpg";
				break;
			case "tiff":
			case "x-tiff":
			case "tif":
			case "x-tif":
				ext = "tif";
				break;
			default:
				throw new IncompatibleTypesException();
			}

			outputFile = File.createTempFile(ext + "-", "." + ext);
			long startTime = System.currentTimeMillis();
			if (logger.isDebugEnabled())
				logger.debug("ImageRenderer.run(): from " + originalFile.getAbsolutePath() + " to "
						+ outputFile.getAbsolutePath());

			String commandLine = RenditionsProperties.getImagickCall();
			String originalFileName = originalFile.getAbsolutePath();
			String outputFileName = outputFile.getAbsolutePath();
			String options = "";
			if (MimeHelper.isMeta(outputMimeType)) {
				outputFileName = "json:" + outputFileName;
			} else {
				if (MimeHelper.isGrayscale(outputMimeType)) {
					options += " -colorspace Gray ";
				}
				if (MimeHelper.isThumb(outputMimeType)) {
					options += " -thumbnail " + MimeHelper.getThumbMode(outputMimeType);
				}
			}

			commandLine = commandLine.replace(SOURCE_FILE_REPLACE_IN_COMMAND, originalFileName);
			commandLine = commandLine.replace(DEST_FILE_REPLACE_IN_COMMAND, outputFileName);
			commandLine = commandLine.replace(OPTIONS_REPLACE_IN_COMMAND, options);

			// Call ImageMagik
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(commandLine);

			// long convTimeout = FcsConfig.getInstance().getFcsConversionTimout();
			long convTimeout = 10000;
			procOutput = proc.getInputStream();

			int exitValue = -1;
			if (convTimeout > 0) {
				boolean done = proc.waitFor(convTimeout, TimeUnit.MILLISECONDS);
				if (!done)
					proc.destroy();
				exitValue = proc.exitValue();
			} else {
				// Esecuzione del comando di conversione senza Timeout
				exitValue = proc.waitFor();
			}

			if (logger.isInfoEnabled())
				logger.info("ImageRenderer.run(): conversion tooks " + (System.currentTimeMillis() - startTime)
						+ " millis.");

			// ATTENTION: Image Magick identify command, up to 6.9.0 version, returns 1
			// instead of 0, so exitValue can't be tested
			if (exitValue == 0 && outputFile != null && outputFile.isFile() && outputFile.exists()) {
				// Verifico che effettivamente il file sia stato creato nella directory di
				// destinazione
				// output the file
				FileInputStream readstream = new FileInputStream(outputFile);

				byte[] buffer = new byte[4096];

				int length;
				/*
				 * copying the contents from input stream to output stream using read and write
				 * methods
				 */
				while ((length = readstream.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}

				// Closing the input/output file streams
				readstream.close();
			}
			out.close();
		} catch (IllegalArgumentException | IOException | SecurityException e) {
			e.printStackTrace();
		} catch (IncompatibleTypesException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// close output stream so rest of the world will finish
			try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (null != procOutput) {
				try {
					procOutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != originalFile) {
				originalFile.delete();
			}
			if (null != outputFile) {
				outputFile.delete();
			}
		}

		// remove worker!!!
		RenditionContext.getInstance().WorkerDone(this);
	}
}

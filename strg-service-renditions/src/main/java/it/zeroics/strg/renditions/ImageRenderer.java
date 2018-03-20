package it.zeroics.strg.renditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.jodconverter.document.DocumentFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import com.sun.star.form.binding.IncompatibleTypesException;

import internal.org.springframework.content.commons.renditions.BasicRenderer;
import internal.org.springframework.content.commons.renditions.RenditionContext;
//import gettingstarted.springcontentfs.File;
import internal.org.springframework.content.commons.utils.InputContentStream;
import it.zeroics.strg.model.Medium;
import it.zeroics.strg.renditions.utils.Metadata;
import it.zeroics.strg.renditions.utils.MimeHelper;

@Component
public class ImageRenderer extends BasicRenderer {
	@Value("${imagik.call}") private static String iMagikCall;
	
	private static final Log logger = LogFactory.getLog(ImageRenderer.class);
	
	public ImageRenderer(InputStream is, MimeType mt) {
		super(is, mt);
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
		File originalFile = null ;
		File outputFile = null ;
		InputStream procOutput = null ;
		try {
			// See code from fca-fcs
			// Create output file than generate input stream from it.
			//String mediaMimeType = ((gettingstarted.springcontentfs.File)((InputContentStream)is).getEntity()).getMimeType() ;
			String mediaName = ((Medium)((InputContentStream)is).getEntity()).getName();
			String inputExtension = FilenameUtils.getExtension(mediaName);
			
			originalFile = File.createTempFile("image-", "."+inputExtension);
			FileUtils.copyInputStreamToFile(is, originalFile);
			// Create file according to out mime type.
			String ext = outputMimeType.getSubtype();
			switch(ext) {
				case MimeHelper.METADATA_MIMESUBTYPE:
					ext = "json";
					break ;
				case "pdf":
					break ;
				case "gif":
					break ;
				case "png":
					break ;
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
					throw new IncompatibleTypesException() ;
			}

			outputFile = File.createTempFile(ext+"-", "."+ext);
			long startTime = System.currentTimeMillis();
			if (logger.isDebugEnabled())
				logger.debug("ImageRenderer.run(): from " + originalFile.getAbsolutePath() + " to " + outputFile.getAbsolutePath());

			String commandLine = "" ;
	        if ( MimeHelper.isMeta(outputMimeType) ) {
				commandLine = RenditionsProperties.getImagickMetaCall() ;
				
				// Compose Command line according to mime parameters
				commandLine += " \"" + originalFile + "\" > \"" + outputFile + "\"" ;
	        }
	        else {
				commandLine = RenditionsProperties.getImagickCall() ;
				// String commandLine = "\"C:/Program Files/ImageMagick-6.8.1-Q16/convert\" -limit memory 250mb -limit map 500mb " ;
				if ( MimeHelper.isGrayscale(outputMimeType) ) {
					commandLine += " -colorspace Gray " ;
				}
				if ( MimeHelper.isThumb(outputMimeType) ) {
					commandLine += " -thumbnail " + MimeHelper.getThumbMode(outputMimeType) ;
				}
				// Compose Command line according to mime parameters
				commandLine += " \"" + originalFile + "\" \"" + outputFile + "\"" ;
	        }
			// Call ImageMagik
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(commandLine);
			
			
			// long convTimeout = FcsConfig.getInstance().getFcsConversionTimout();
			long convTimeout = 10000 ;
			procOutput = proc.getInputStream() ;
			
			int exitValue = -1;
			if (convTimeout > 0) {
				boolean done = proc.waitFor(convTimeout, TimeUnit.MILLISECONDS);
				if (!done)
					proc.destroy();
				exitValue = proc.exitValue();
			}
			else {
				// Esecuzione del comando di conversione senza Timeout
				exitValue = proc.waitFor();
			}
			
			if (logger.isInfoEnabled())
				logger.info("ImageRenderer.run(): conversion tooks " + (System.currentTimeMillis()-startTime) + " millis.");

			// ATTENTION: Image Magick identify command, up to 6.9.0 version, returns 1 instead of 0, so exitValue can't be tested
			if ( MimeHelper.isMeta(outputMimeType) ) {
	        	// Convert Metadata to json.
	        	imagickMetaToJson(outputFile, procOutput, out) ;
			}
			else if (exitValue == 0 && outputFile != null && outputFile.isFile() && outputFile.exists()) {
			// Verifico che effettivamente il file sia stato creato nella directory di destinazione
				// output the file
	    	    FileInputStream readstream = new FileInputStream(outputFile);
	 
	    	    byte[] buffer = new byte[4096];
	 
	    	    int length;
	    	    /*copying the contents from input stream to
	    	     * output stream using read and write methods
	    	     */
	    	    while ((length = readstream.read(buffer)) > 0){
	    	    	out.write(buffer, 0, length);
	    	    }

	    	    //Closing the input/output file streams
	    	    readstream.close();
	        }
        	out.close() ;
		} catch(IllegalArgumentException | IOException | SecurityException e) {
			e. printStackTrace();
		} catch (IncompatibleTypesException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if ( null != procOutput ) {
				try {
					procOutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if ( null != originalFile ) {
				originalFile.delete();
			}
			if ( null != outputFile ) {
				outputFile.delete();
			}
		}

		// remove worker!!!
		RenditionContext.getInstance().WorkerDone(this);
	}

	private void imagickMetaToJson(File outputFile, InputStream in, PipedOutputStream out) throws IOException {
		// TODO Auto-generated method stub
		Metadata meta = new Metadata(outputFile.getCanonicalPath(), outputFile.length()) ;
    	Scanner input = new Scanner(in);
    	Map<Integer, String> levels = new TreeMap<Integer, String>() ;
    	
    	while(input.hasNextLine()) {
    		String thisLine = input.nextLine();
    		Integer spaces = 0 ;
			while( thisLine.startsWith(" ")) {
				spaces++;
				thisLine = thisLine.substring(1);
			}
			
			// Split.
			String[] parts = thisLine.split(": ") ;
			
			if ( parts.length == 1 || parts[1].trim().length() == 0 ) {
				// Preserve this line for future usage.
				String part = parts[0].trim();
				while ( part.endsWith(":") ) {
					part = part.substring(0, part.length()-1);
				}
				levels.put(spaces, part) ;
			}
			else {
				String PrefixString = "" ;
				for(Map.Entry<Integer, String> entry: levels.entrySet()) {
					if ( entry.getKey() >= spaces ) break;
					PrefixString += entry.getValue() + ":";
				}
				meta.addMeta(PrefixString+parts[0].trim(), parts[1].trim());
			}
    	}
    	input.close();
    	
    	meta.serialize(out);
	}
}

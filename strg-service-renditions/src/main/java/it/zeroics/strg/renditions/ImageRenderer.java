package it.zeroics.strg.renditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileInputStream;

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

//import gettingstarted.springcontentfs.File;
import internal.org.springframework.content.commons.utils.InputContentStream;
import it.zeroics.strg.model.Medium;

@Component
public class ImageRenderer extends BasicRenderer {
	@Value("${imagik.call}") private static String iMagikCall;
	
	private static final Log logger = LogFactory.getLog(ImageRenderer.class);
	
	public ImageRenderer(InputStream is, MimeType mt) {
		super(is, mt);
	}
	
	@Override
	public void run() {
		File originalFile = null ;
		File outputFile = null ;
		try {
			// See code from fca-fcs
			// Create output file than generate input stream from it.
			//String mediaMimeType = ((gettingstarted.springcontentfs.File)((InputContentStream)is).getEntity()).getMimeType() ;
			String mediaName = ((Medium)((InputContentStream)is).getEntity()).getName();
			String inputExtension = FilenameUtils.getExtension(mediaName);
			
			originalFile = File.createTempFile("image-", "."+inputExtension);
			FileUtils.copyInputStreamToFile(is, originalFile);
			// Create file according to out mime type.
			Map<String, String> parms = outputMimeType.getParameters();

			String ext = outputMimeType.getSubtype();
			switch(ext) {
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
					ext = "jpg";
					break;
				case "tiff":
				case "x-tiff":
					ext = "tif";
					break;
				default:
					throw new IncompatibleTypesException() ;
			}

			outputFile = File.createTempFile(ext+"-", "."+ext);
			long startTime = System.currentTimeMillis();
			if (logger.isDebugEnabled())
				logger.debug("ImageRenderer.run(): from " + originalFile.getAbsolutePath() + " to " + outputFile.getAbsolutePath());

			//String commandLine = new String(iMagikCall) ;
			String commandLine = "\"C:/Program Files/ImageMagick-6.8.1-Q16/convert\" -limit memory 250mb -limit map 500mb " ;
			// Compose Command line according to mime parameters
			commandLine += " \"" + originalFile + "\" \"" + outputFile + "\"" ; 			
			// Call ImageMagik
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(commandLine);
			
			// long convTimeout = FcsConfig.getInstance().getFcsConversionTimout();
			long convTimeout = 10000 ;
			
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
			
			// Verifico che effettivamente il file sia stato creato nella directory di destinazione
			if (exitValue == 0 && outputFile != null && outputFile.isFile() && outputFile.exists()) {
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
	    	    out.close();
			}
			
		} catch(IllegalArgumentException | IOException | SecurityException e) {
			e. printStackTrace();
		} catch (IncompatibleTypesException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if ( null != originalFile ) {
				originalFile.delete();
			}
			if ( null != outputFile ) {
				outputFile.delete();
			}
		}

		// remove worker!!!
		Context.getInstance().WorkerDone(this);
	}
}

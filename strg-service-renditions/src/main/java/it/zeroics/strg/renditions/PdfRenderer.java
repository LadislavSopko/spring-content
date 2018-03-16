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

import internal.org.springframework.content.commons.renditions.BasicRenderer;
import internal.org.springframework.content.commons.renditions.RenditionContext;
//import gettingstarted.springcontentfs.File;
import internal.org.springframework.content.commons.utils.InputContentStream;
import it.zeroics.strg.model.Medium;
import it.zeroics.strg.renditions.utils.MimeHelper;

@Component
public class PdfRenderer extends BasicRenderer {
	private static final Log logger = LogFactory.getLog(TikaRenderer.class);
	
	public PdfRenderer(InputStream is, MimeType mt) {
		super(is, mt);
	}

	/**
	 * Ritorna l'oggetto DocumentFormat relativo ad una conversione in PDF/A
	 * @param from File di origine per il quale si richiede la conversione in PDF/A
	 * 
	 * See it.tredi.fcs.command.conversion.OpenOfficeConversionExecutor.java
	 */
	private DocumentFormat getPdfaDocumentFormat(File from) {
		DocumentFormat format = null;
		if (from != null) {
			Map<String, Object> filterData = new HashMap<String, Object>();

			// Specifies printing of the document:
			// 0: PDF document cannot be printed
			// 1: PDF document can be printed at low resolution only
			// 2: PDF document can be printed at maximum resolution.
			filterData.put("Printing", 2);
			
			// Specifies the PDF version that should be generated:
			// 0: PDF 1.4 (default selection)
			// 1: PDF/A-1 (ISO 19005-1:2005)
			filterData.put("SelectPdfVersion", 1);

			if (logger.isDebugEnabled())
				logger.debug("PdfConverter.getPdfaDocumentFormat(): check doc family for file " + from.getName());
			
			String filterName = null;
			String inputExtension = FilenameUtils.getExtension(from.getName());
			DocumentFormat sourceFormat = DefaultDocumentFormatRegistry.create().getFormatByExtension(inputExtension);
			DocumentFamily docFamily = DocumentFamily.TEXT;
			if (sourceFormat != null)
				docFamily = sourceFormat.getInputFamily();
			
			if (logger.isDebugEnabled())
				logger.debug("PdfConverter.getPdfaDocumentFormat(): docFamily = " + docFamily.name());
			
			if (docFamily == DocumentFamily.TEXT)
				filterName = "writer_pdf_Export";
			else if (docFamily == DocumentFamily.SPREADSHEET)
				filterName = "calc_pdf_Export";
			else if (docFamily == DocumentFamily.PRESENTATION)
				filterName = "impress_pdf_Export";
			else if (docFamily == DocumentFamily.DRAWING)
				filterName = "draw_pdf_Export";
			else
				filterName = "writer_pdf_Export"; // caso di default: writer (doc)
			
			Map<String, Object> properties = new HashMap<>();
			properties.put("FilterName", filterName);
			properties.put("FilterData", filterData);
	
			format = new DocumentFormat("PDF/A", "pdf", "application/pdf");
			format.setStoreProperties(docFamily, properties);
			//format.setStoreProperties(DocumentFamily.TEXT, properties);
			//format.setStoreProperties(DocumentFamily.SPREADSHEET, properties);
			//format.setStoreProperties(DocumentFamily.PRESENTATION, properties);
			//format.setStoreProperties(DocumentFamily.DRAWING, properties);
		}
		return format;
	}
	
	@Override
	public void run() {
		File officeFile = null ;
		File pdfFile = null ;
		try {
			// See code from fca-fcs
			// Create output file than generate input stream from it.
			//String mediaMimeType = ((gettingstarted.springcontentfs.File)((InputContentStream)is).getEntity()).getMimeType() ;
			String mediaName = ((Medium)((InputContentStream)is).getEntity()).getName();
			String inputExtension = FilenameUtils.getExtension(mediaName);
			DocumentFormat pdfDocumentFormat = null ;
			
			officeFile = File.createTempFile("office-", "."+inputExtension);
			FileUtils.copyInputStreamToFile(is, officeFile);
			// Create file according to out mime type.

			pdfFile = File.createTempFile("pdf-", ".pdf");
			if (logger.isDebugEnabled())
				logger.debug("PdfConverter.run(): from " + officeFile.getAbsolutePath() + " to " + pdfFile.getAbsolutePath());
			
			if ( MimeHelper.isPdfA(outputMimeType) ) {
				pdfDocumentFormat = getPdfaDocumentFormat(officeFile) ;
			}

			OfficeManager officeManager = OfficeManagerOSC.getInstance().getOfficeManager();
			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
			
			long startTime = System.currentTimeMillis();
			if (logger.isInfoEnabled())
				logger.info("PdfConverter.run(): from " + officeFile.getAbsolutePath() + " to " + pdfFile.getAbsolutePath());
			
			if (pdfDocumentFormat != null)
				converter.convert(officeFile, pdfFile, pdfDocumentFormat);
			else
				converter.convert(officeFile, pdfFile);
			
			if (logger.isInfoEnabled())
				logger.info("PdfConverter.run(): conversion tooks " + (System.currentTimeMillis()-startTime) + " millis.");
			
			// Verifico che effettivamente il file sia stato creato nella directory di destinazione
			if (pdfFile != null && pdfFile.isFile() && pdfFile.exists()) {
				// output the file
	    	    FileInputStream readstream = new FileInputStream(pdfFile);
	 
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
			
		} catch(IllegalArgumentException | IOException | SecurityException | OfficeException e) {
			e. printStackTrace();
		}
		finally {
			if ( null != officeFile ) {
				officeFile.delete();
			}
			if ( null != pdfFile ) {
				pdfFile.delete();
			}
		}

		// remove worker!!!
		RenditionContext.getInstance().WorkerDone(this);
	}
}

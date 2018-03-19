package it.zeroics.strg.renditions;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.xml.sax.SAXException;

import it.zeroics.strg.model.Medium;
import it.zeroics.strg.renditions.utils.MimeHelper;
import internal.org.springframework.content.commons.renditions.BasicRenderer;
import internal.org.springframework.content.commons.renditions.RenditionContext;
import internal.org.springframework.content.commons.utils.InputContentStream;

@Component
public class TikaRenderer extends BasicRenderer {

    private static Log logger = LogFactory.getLog(TikaRenderer.class);

	private TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

    private Metadata metadata = new Metadata();
	
	public TikaRenderer(InputStream is, MimeType mt) {
		super(is, mt) ;
		RenditionContext.getInstance().setSupportedExtension("text/plain", ".txt");
		RenditionContext.getInstance().setSupportedExtension(MimeHelper.METADATA_MIMETYPE, ".json");
	}

	private void addTikaMeta(it.zeroics.strg.renditions.utils.Metadata m, org.apache.tika.metadata.Metadata tikaMetadata, String tikaMetadataProperty) {
		String value = tikaMetadata.get(tikaMetadataProperty);
		if (value != null && !value.isEmpty()) {
			m.addMeta(tikaMetadataProperty, value);
		}
	}
	private void addTikaMeta(it.zeroics.strg.renditions.utils.Metadata m, org.apache.tika.metadata.Metadata tikaMetadata, Property tikaMetadataProperty) {
		String value = tikaMetadata.get(tikaMetadataProperty);
		if (value != null && !value.isEmpty()) {
			m.addMeta(tikaMetadataProperty.getName().toString(), value);
		}
	}
	
	public void addTikaMeta(it.zeroics.strg.renditions.utils.Metadata m, org.apache.tika.metadata.Metadata tikaMetadata) {
		if (tikaMetadata != null) {
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTRIBUTOR);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.COVERAGE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CREATOR);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.DESCRIPTION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.FORMAT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.IDENTIFIER);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LANGUAGE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MODIFIED);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.NAMESPACE_PREFIX_DELIMITER);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.PUBLISHER);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.RELATION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.RIGHTS);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.SOURCE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.SUBJECT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.TITLE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.TYPE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.ACKNOWLEDGEMENT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.APPLICATION_NAME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.APPLICATION_VERSION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.AUTHOR);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CATEGORY);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.COMMAND_LINE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.COMMENT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.COMMENTS);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.COMPANY);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTACT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTENT_DISPOSITION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTENT_ENCODING);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTENT_LANGUAGE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTENT_LENGTH);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTENT_LOCATION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTENT_MD5);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTENT_STATUS);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONTENT_TYPE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CONVENTIONS);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CHARACTER_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CHARACTER_COUNT_WITH_SPACES);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.EDIT_TIME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.EMBEDDED_RELATIONSHIP_ID);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.EMBEDDED_RESOURCE_TYPE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.EMBEDDED_STORAGE_CLASS_ID);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.EXPERIMENT_ID);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.HISTORY);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.INSTITUTION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.KEYWORDS);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LAST_AUTHOR);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LICENSE_LOCATION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LICENSE_URL);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LOCATION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MANAGER);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_BCC);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_CC);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_FROM);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_PREFIX);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_RAW_HEADER_PREFIX);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_RECIPIENT_ADDRESS);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_TO);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MIME_TYPE_MAGIC);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MODEL_NAME_ENGLISH);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.NOTES);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.PRESENTATION_FORMAT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.PROGRAM_ID);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.PROJECT_ID);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.PROTECTED);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.REALIZATION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.REFERENCES);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.RESOURCE_NAME_KEY);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.REVISION_NUMBER);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.SECURITY);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.TABLE_ID);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.TEMPLATE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.TIKA_MIME_FILE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.TOTAL_TIME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.USER_DEFINED_METADATA_NAME_PREFIX);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.VERSION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.WORK_TYPE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.DATE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.ALTITUDE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.BITS_PER_SAMPLE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CHARACTER_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CHARACTER_COUNT_WITH_SPACES);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.CREATION_DATE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.EQUIPMENT_MAKE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.EQUIPMENT_MODEL);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.EXPOSURE_TIME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.F_NUMBER);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.FLASH_FIRED);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.FOCAL_LENGTH);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.IMAGE_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.IMAGE_LENGTH);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.IMAGE_WIDTH);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.ISO_SPEED_RATINGS);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LAST_MODIFIED);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LAST_PRINTED);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LAST_SAVED);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LATITUDE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LINE_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.LONGITUDE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_BCC_DISPLAY_NAME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_BCC_EMAIL);

			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_BCC_NAME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_CC_DISPLAY_NAME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_CC_EMAIL);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_CC_NAME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_FROM_EMAIL);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_FROM_NAME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_TO_DISPLAY_NAME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_TO_EMAIL);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.MESSAGE_TO_NAME);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.OBJECT_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.ORIENTATION);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.ORIGINAL_DATE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.PAGE_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.PARAGRAPH_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.RESOLUTION_HORIZONTAL);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.RESOLUTION_UNIT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.RESOLUTION_VERTICAL);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.SAMPLES_PER_PIXEL);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.SLIDE_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.SOFTWARE);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.TABLE_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.WORD_COUNT);
			addTikaMeta(m, tikaMetadata, org.apache.tika.metadata.Metadata.RESOURCE_NAME_KEY);
		}
	}
	
	@Override
	public void run(){

		Parser parser = tikaConfig.getParser();
		String mediaMimeType = ((Medium)((InputContentStream)is).getEntity()).getMimeType() ;
    	logger.debug("The MIME type is: [" + mediaMimeType + "]");
		AutoDetectParser autoParser = null;
        
		// TODO:	Identify mime type according to file name extension, file content with magic or anything else
		//			See https://github.com/apache/tika/blob/master/tika-example/src/main/java/org/apache/tika/example/MyFirstTika.java
		//			Use AutoDetectParser only if mime can't be obtained elsewere.
        if ( mediaMimeType.equals("application/pkcs7-mime") ) {
        	autoParser = new AutoDetectParser(tikaConfig);
        	metadata.set(Metadata.CONTENT_TYPE, mediaMimeType);
        }
        else {
        	metadata.set(Metadata.CONTENT_TYPE, mediaMimeType);
        }
        
        if ( MimeHelper.isMeta(outputMimeType) ) {
			Metadata tikaMetadata = new Metadata();
	        try {
	        	Tika t = new Tika() ;
	    		t.parseToString(is, tikaMetadata, 0);
	    		it.zeroics.strg.renditions.utils.Metadata m = new it.zeroics.strg.renditions.utils.Metadata(((Medium)((InputContentStream)is).getEntity()).getId(), ((Medium)((InputContentStream)is).getEntity()).getContentLength() );
		        addTikaMeta(m, tikaMetadata) ;
		        m.serialize(out);
				out.close();
				//out.flush();
	        } catch(IOException | TikaException e) {
	        	logger.error("Error on tika parse file.");
				e.printStackTrace();
	        }
        }
        else {
			BodyContentHandler handler = new BodyContentHandler(out);
			TikaInputStream stream = TikaInputStream.get(is); //, metadata);
			
	    	try {
	    		if ( null == autoParser ) {
	    			parser.parse(stream, handler, metadata, new ParseContext());
	    		}
	    		else {
	    			autoParser.parse(stream, handler, metadata, new ParseContext());
	    		}
	    		out.close();
	    		//out.flush();
			} catch (IOException | SAXException | TikaException e) {
				// TODO Auto-generated catch block
				e. printStackTrace();
			}
        }
    	
    	// remove worker!!!
    	RenditionContext.getInstance().WorkerDone(this);
    }
}

package it.zeroics.strg.renditions.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.MimeType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Oggetto contenente i metadati estratti da un file elaborato da un converter.
 * 
 * See it.3di.fcs.entry.Metadata.java
 */
public class MimeHelper {
	
	public static final String METADATA_MIMETYPE = "application/it.zeroics.meta";
	public static final String CAPABILITY_MIMETYPE = "application/it.zeroics.capability";
	
	private String type;
	private String subType;
	private Map<String, String> parms;

	// Parameter Key & Values
	private static final String METADATA_KEY = "meta" ;
	private static final String GRAYSCALE_KEY = "gray" ;
	private static final String PDFA_KEY = "pdfA" ;
	private static final String THUMB_KEY = "thumb" ;
	private static final String CAPABILITY_KEY = "capable" ;
	//
	private static final String TRUE_VALUE = "true" ;
	
	public MimeHelper() {
		this.type = "application" ;
		this.subType = "octet-stream" ;
		parms = Collections.<String, String>emptyMap();
	}
	public MimeHelper(String type, String subType) {
		this.type = type ;
		this.subType = subType ;
		parms = Collections.<String, String>emptyMap();
	}
	public MimeHelper(String mimeType) {
		MimeType m = MimeType.valueOf(mimeType) ;
		this.type = m.getType();
		this.subType = m.getSubtype();
		parms = m.getParameters();
	}
	
	// Parameters
	
	// Metadata
	public Boolean isMeta() {
        // Should I bring text or meta?
        if ( toMime().isCompatibleWith(MimeType.valueOf(METADATA_MIMETYPE)) ) {
       		return true ;
        }
        return false ;
	}
	public static Boolean isMeta(MimeType m) {
        // Should I bring text or meta?
        if ( m.isCompatibleWith(MimeType.valueOf(METADATA_MIMETYPE)) ) {
       		return true ;
        }
        return false ;
	}
	
	// Capability
	public Boolean isCapability() {
        if ( toMime().isCompatibleWith(MimeType.valueOf(CAPABILITY_MIMETYPE)) ) {
        	return true;
        }
        return false ;
	}
	public static Boolean isCapability(MimeType m) {
        if ( m.isCompatibleWith(MimeType.valueOf(CAPABILITY_MIMETYPE)) ) {
        	return true;
        }
        return false ;
	}

	// Grayscale
	public void requireGrayscale() {
		parms.put(GRAYSCALE_KEY, TRUE_VALUE) ;
	}
	public Boolean isGrayscale() {
		try {
			if ( parms.get(GRAYSCALE_KEY).equals(TRUE_VALUE) ) return true ; 
		}
		catch(Exception e) {}
		return false ;
	}
	public static Boolean isGrayscale(MimeType m) {
		try {
			if ( m.getParameter(GRAYSCALE_KEY).equals(TRUE_VALUE) ) return true ; 
		}
		catch(Exception e) {}
		return false ;
	}
	
	// PdfA
	public void requirePdfA() {
		parms.put(PDFA_KEY, TRUE_VALUE) ;
	}
	public Boolean isPdfA() {
		try {
			if ( parms.get(PDFA_KEY).equals(TRUE_VALUE) ) return true ; 
		}
		catch(Exception e) {}
		return false ;
	}
	public static Boolean isPdfA(MimeType m) {
		try {
			if ( m.getParameter(PDFA_KEY).equals(TRUE_VALUE) ) return true ; 
		}
		catch(Exception e) {}
		return false ;
	}
	
	// Thumb
	public void requireThumb(int width, int height) {
		parms.put(THUMB_KEY, String.valueOf(width)+"x"+String.valueOf(height)) ;
	}
	public Boolean isThunmb() {
		try {
			if ( !parms.get(THUMB_KEY).isEmpty() ) return true ; 
		}
		catch(Exception e) {}
		return false ;
	}
	public static Boolean isThumb(MimeType m) {
		try {
			if ( !m.getParameter(THUMB_KEY).isEmpty() ) return true ; 
		}
		catch(Exception e) {}
		return false ;
	}
	public String getThumbMode() {
		try {
			return parms.get(THUMB_KEY); 
		}
		catch(Exception e) {}
		return "" ;
	}
	public static String getThumbMode(MimeType m) {
		try {
			return m.getParameter(THUMB_KEY); 
		}
		catch(Exception e) {}
		return "" ;
	}
	
	// Output
	public MimeType toMime() {
		MimeType m = new MimeType(type, subType, parms) ;
		return m;
	}
	
	public String toString() {
		return toMime().toString();
	}
}

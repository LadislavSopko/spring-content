package it.zeroics.strg.renditions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jodconverter.office.DefaultOfficeManagerBuilder;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.springframework.stereotype.Component;

 @Component
public class OfficeManagerOSC {

    private static Log logger = LogFactory.getLog(OfficeManagerOSC.class);
 
	private OfficeManager officeManager = null;
	
	public OfficeManager getOfficeManager() throws OfficeException {
		if ( null == officeManager ) {
			boolean done = false;
			try {
				this.officeManager = new DefaultOfficeManagerBuilder().build();
				this.officeManager.start();
				done = true;
			}catch(Exception ex) {
				// skip exception
			}
			
			if(!done) {
				this.officeManager = new DefaultOfficeManagerBuilder().setOfficeHome("C:\\Program Files (x86)\\OpenOffice 4").build();
				this.officeManager.start();
			}
		}
		return this.officeManager;
	}
	
	public void finalize() {
		try {
			if (officeManager != null) {
				officeManager.stop();

				if (logger.isInfoEnabled())
					logger.info("PdfConverter.stopOpenOfficeManager(): OfficeManager stopped!");
			}
		}
		catch (OfficeException e) {
			logger.error("PdfConverter.stopOpenOfficeManager(): got exception on OpenOffice closure... " + e.getMessage(), e);
		}
	}
    
	// SINGLETON
	private static OfficeManagerOSC singleton = null;
	
	private OfficeManagerOSC() {
		officeManager = null;
	}
	
	public static OfficeManagerOSC getInstance() {
	  if(singleton == null) {
	     synchronized(OfficeManagerOSC.class) {
	       if(singleton == null) {
	         singleton = new OfficeManagerOSC();
	       }
	    }
	  }
	  return singleton;
	}
}

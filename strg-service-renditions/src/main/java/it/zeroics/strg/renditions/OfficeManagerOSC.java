package it.zeroics.strg.renditions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jodconverter.office.DefaultOfficeManagerBuilder;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.springframework.stereotype.Component;

 @Component
public class OfficeManagerOSC {

    private static Log logger = LogFactory.getLog(OfficeManagerOSC.class);
 
	private OfficeManager officeManager = null;
	
	public OfficeManager getOfficeManager() throws OfficeException {
		if ( null == officeManager ) {
			DefaultOfficeManagerBuilder officeManagerBuild = new DefaultOfficeManagerBuilder();

			// Evenutale path alla home directory di OpenOffice (o LibreOffice)
			/*
			if (FcsConfig.getInstance().getFcsConversionDocOpenOfficeHomeDir() != null && !FcsConfig.getInstance().getFcsConversionDocOpenOfficeHomeDir().isEmpty())
				officeManagerBuild.setOfficeHome(FcsConfig.getInstance().getFcsConversionDocOpenOfficeHomeDir());

			if (FcsConfig.getInstance().getFcsConversionDocOpenOfficePorts() != null && FcsConfig.getInstance().getFcsConversionDocOpenOfficePorts().length > 0)
				officeManagerBuild.setPortNumbers(FcsConfig.getInstance().getFcsConversionDocOpenOfficePorts());
			if (FcsConfig.getInstance().getFcsConversionTimout() > 0)
				officeManagerBuild.setTaskExecutionTimeout(FcsConfig.getInstance().getFcsConversionTimout());
			*/

			this.officeManager = officeManagerBuild.build();
			this.officeManager.start();
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

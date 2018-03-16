package internal.org.springframework.content.commons.renditions;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.renditions.RenditionService;
import org.springframework.stereotype.Component;

@Component
public class RenditionContext {

	private static Log logger = LogFactory.getLog(RenditionContext.class);

	private RenditionService rs = null;

	private Set<BasicRenderer> currentConversions = new HashSet<BasicRenderer>();

	// Set Rendition Provider
	public void setRenditionService(RenditionService rs) {
		if (this.rs == null) {
			synchronized (RenditionContext.class) {
				if (this.rs == null) {
					this.rs = rs;
				}
			}
		}
	}
	
	public RenditionService getRenditionService() {
		return rs;
	}

	// Work
	public InputStream DoWork(InputStream is, BasicRenderer converter) {
		// Choose the converter according from and to Mime
		currentConversions.add(converter); // hold instance so we will remove it when done
		new Thread(converter).start();
		return converter.getInputStream();
	}

	public void WorkerDone(BasicRenderer c) {
		currentConversions.remove(c);
	}

	// SINGLETON
	private static RenditionContext singleton = null;

	private RenditionContext() {
	}

	public static RenditionContext getInstance() {
		if (singleton == null) {
			synchronized (RenditionContext.class) {
				if (singleton == null) {
					singleton = new RenditionContext();
				}
			}
		}
		return singleton;
	}
}

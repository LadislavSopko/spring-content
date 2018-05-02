package internal.org.springframework.content.commons.renditions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.renditions.RenditionService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class RenditionContext {

	private static Log logger = LogFactory.getLog(RenditionContext.class);

	private RenditionService rs = null;

	private Map<String, String> mimeToExt;

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

	public void setSupportedExtension(String mimeType, String ext) {
		if (!ext.startsWith("."))
			ext = "." + ext; // Force dot in front
		mimeToExt.putIfAbsent(mimeType, ext);
	}

	public String getSupportedExtension(String mimeType) {
		return mimeToExt.getOrDefault(mimeType, ".unknown");
	}

	// Work
	public Resource DoWork(BasicRenderer converter) {
		// Choose the converter according from and to Mime
		currentConversions.add(converter); // hold instance so we will remove it when done
		new Thread(converter).start();
		return converter.getResult();
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
					singleton.mimeToExt = new HashMap<String, String>();
				}
			}
		}
		return singleton;
	}
}

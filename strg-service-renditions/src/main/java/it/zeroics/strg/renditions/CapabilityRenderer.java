package it.zeroics.strg.renditions;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.content.commons.io.DefaultMediaResource;
import org.springframework.content.commons.renditions.RenditionService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import internal.org.springframework.content.commons.renditions.BasicRenderer;
import internal.org.springframework.content.commons.renditions.RenditionContext;
import it.zeroics.strg.renditions.utils.Metadata;
import it.zeroics.strg.renditions.utils.MimeHelper;

@Component
public class CapabilityRenderer extends BasicRenderer {
	private static final Log logger = LogFactory.getLog(TikaRenderer.class);
	private Metadata meta;

	public CapabilityRenderer(Resource ir, MimeType mt) {
		super(ir, mt);
		RenditionContext.getInstance().setSupportedExtension(MimeHelper.CAPABILITY_MIMETYPE, ".json");
	}

	@Override
	public void run() {
		try {
			// Obtain RenditionService implementation and ask for RenditionProviders
			// capabilities
			RenditionService rs = RenditionContext.getInstance().getRenditionService();

			String[] outputMimeTypes = rs.conversions(((DefaultMediaResource) ir).getMime());

			ObjectMapper om = new ObjectMapper();
			try {
				String jsonOutString = om.writerWithDefaultPrettyPrinter().writeValueAsString(outputMimeTypes);
				out.write(jsonOutString.getBytes());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.close();
		} catch (IllegalArgumentException | IOException | SecurityException e) {
			e.printStackTrace();
		}

		// remove worker!!!
		RenditionContext.getInstance().WorkerDone(this);
	}
}

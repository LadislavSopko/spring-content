package internal.org.springframework.content.commons.renditions;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.springframework.content.commons.io.DefaultMediaResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

@Component
public class BasicRenderer implements Runnable {
	protected PipedInputStream in = null;
	protected PipedOutputStream out = null;

	protected DefaultMediaResource result = null;

	protected Resource ir = null;
	protected MimeType outputMimeType = null;

	private void init(Resource ir, MimeType mt) {
		this.ir = ir;
		this.outputMimeType = mt;

		in = new PipedInputStream();
		try {
			out = new PipedOutputStream(in);

			DefaultMediaResource mr = (DefaultMediaResource) ir;
			if (mr != null) {
				result = new DefaultMediaResource(new InputStreamResource(in), mt.toString(), mr.getName());
			} else {
				result = new DefaultMediaResource(new InputStreamResource(in), mt.toString(), "");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Should be called as super(is)
	protected BasicRenderer(Resource ir, MimeType mt) {
		init(ir, mt);
	}

	protected BasicRenderer(Resource ir) {
		init(ir, null);
	}

	// Must Override
	public void run() {
		/*
		 * AutoDetectParser parser = new AutoDetectParser(tikaConfig);
		 * BodyContentHandler handler = new BodyContentHandler(out); TikaInputStream
		 * stream = TikaInputStream.get(is); //, metadata);
		 * 
		 * try { parser.parse(stream, handler, metadata, new ParseContext());
		 * out.close(); //out.flush(); } catch (IOException | SAXException |
		 * TikaException e) { // TODO Auto-generated catch block e. printStackTrace(); }
		 */
		RenditionContext.getInstance().WorkerDone(this);
	}

	// Should not Override
	public Resource getResult() {
		// remove worker only when Input stream is consumed!!!
		return result;
	}

}

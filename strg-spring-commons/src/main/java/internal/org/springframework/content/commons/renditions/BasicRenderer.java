package internal.org.springframework.content.commons.renditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

@Component
public class BasicRenderer implements Runnable {
	protected PipedInputStream in = null;
	protected PipedOutputStream out = null;

	protected InputStream is = null;	
	protected MimeType outputMimeType = null;

	private void init(InputStream is, MimeType mt){
		this.is = is;
		this.outputMimeType = mt ;
		
		in = new PipedInputStream();
		try {
			out = new PipedOutputStream(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// Should be called as super(is)
	protected BasicRenderer(InputStream is, MimeType mt){
		init(is, mt) ;
	}
	
	protected BasicRenderer(InputStream is) {
		init(is, null);
	}

	// Must Override
	public void run(){
		/*
		AutoDetectParser parser = new AutoDetectParser(tikaConfig);
		BodyContentHandler handler = new BodyContentHandler(out);
		TikaInputStream stream = TikaInputStream.get(is); //, metadata);
		
    	try {
			parser.parse(stream, handler, metadata, new ParseContext());
			out.close();
			//out.flush();
		} catch (IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			e. printStackTrace();
		}
		*/
    	RenditionContext.getInstance().WorkerDone(this);
    }

	// Should not Override
	public InputStream getInputStream() {
    	// remove worker only when Input stream is consumed!!!
		return in;
	}
	
}

package it.zeroics.strgshell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.fasterxml.jackson.core.JsonProcessingException;

import internal.org.springframework.content.commons.renditions.RenditionContext;
import it.zeroics.strg.api.MediumContentStore;
import it.zeroics.strg.api.MediumReopository;
import it.zeroics.strg.model.Medium;
import it.zeroics.strg.model.utils.MediumUtils;

@ShellComponent
public class Commands {

	@Autowired private MediumReopository mediumRepo;
	@Autowired private MediumContentStore contentStore;
	
	
	// lets hold some state
	Page<Medium> currentDataPage = null;
	String currentPath;
	Tika tika = null;
	
	private static TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
	private static MimeTypes allMimeTypes = tikaConfig.getMimeRepository();
	
	
	public Commands() {
		try {
			currentPath = Files.createTempDirectory("").toString();
		} catch (IOException e) {
			// take working dir
			currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		}
	}
	
	private void displayPage() {
		int i = 0;
		for(Medium m : currentDataPage) {
			System.out.println(String.format("%03d\t%s %s", i++, m.getId(), m.getName()));
		}
	}
	
	private String getMime(String fName) {
		Metadata tikaMetadata = new Metadata();
	    try {
		     
	    	 if(tika == null) {
	    		 tika = new Tika(); 
	    	 }
	    	
	    	 tika.parseToString(new FileInputStream(fName), tikaMetadata, 0); // 'is' is the InputStream
		     return tikaMetadata.get(org.apache.tika.metadata.Metadata.CONTENT_TYPE);
	    } catch(IOException | TikaException e) {
		     e.printStackTrace();
	    }
	    
	    return "application/octet-stream";
	}
	
	@ShellMethod("Get curret working path.")
    public void getDir() {
		System.out.println(String.format("Path: %s", currentPath));
    }
	
	@ShellMethod("Set curret working path.")
    public void setDir(String p) {
		currentPath = p;
		System.out.println(String.format("Path: %s", currentPath));
    }
	
	
	@ShellMethod("Get page of Media.")
    public void getPage(int page, @ShellOption(defaultValue="20") int size) {
		currentDataPage = mediumRepo.findAll(PageRequest.of(page, size));
		displayPage();
    }
	
	@ShellMethod("Get next page of Media.")
    public void getNextPage() {
		if(currentDataPage == null) return;
		
		if(currentDataPage.hasNext()) {
			currentDataPage = mediumRepo.findAll(currentDataPage.nextPageable());
			displayPage();
		}else {
			System.out.println("-- End --");
		}		
    }
	
	@ShellMethod("Get prev page of Media.")
    public void getPrevPage() {
		if(currentDataPage == null) return;
		
		if(currentDataPage.hasPrevious()) {
			currentDataPage = mediumRepo.findAll(currentDataPage.previousPageable());
			displayPage();
		}else {
			System.out.println("-- End --");
		}		
    }	
	
	@ShellMethod("Display Meta from current page.")
    public void metaByPos(int ix) {
		if(currentDataPage == null) return;
		
		try {
			System.out.println(MediumUtils.serializePretty(currentDataPage.getContent().get(ix)));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	
	@ShellMethod("Display Meta by Id.")
    public void metaById(String id) {
		Optional<Medium> m = mediumRepo.findById(id);
		
		m.ifPresent(x->{
			try {
				System.out.println(MediumUtils.serializePretty(x));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }
	
	
	
	
	@ShellMethod("Add media from specified path and set its summary")
	public void addMedia(String fName, @ShellOption(defaultValue="") String summary) {
		
		
		//0: check file exists
		File f = new File(fName);
		
		//0.1: try from current dir
		if(!f.exists()) {
			f = new File(currentPath + File.separator + fName);
		}
		// now it should exists
		if(f.exists()) {			
			
			try {
				//1 : create media
				Medium m = new Medium();
				m.setName(f.getName());
				m.setMimeType(getMime(f.getAbsolutePath()));
				// insert
				mediumRepo.save(m);
				
				// add attach
				contentStore.setContent(m, new FileInputStream(f.getAbsolutePath()));
				
				// resave
				mediumRepo.save(m);
				
				// added
				System.out.println(MediumUtils.serializePretty(m));
			} catch (FileNotFoundException | JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
		}else {
			System.out.println("-- Not found --");
		}
	}
	
	
	
	private void saveMedia(Optional<Medium>m, String mime, String fName) {
		m.ifPresent(x->{
			try {
				System.out.println(MediumUtils.serializePretty(x));
				
				InputStream is = null;
				String mtExt = "";
				
				if(!mime.equals("")) {
					is = contentStore.getRendition(x, mime);
					
					mtExt = RenditionContext.getInstance().getSupportedExtension(mime.toString()) ;
					// mtExt = allMimeTypes.forName(mime.toString()).getExtension();
					
				}else {
					is = contentStore.getContent(x);
				}
				
				String fileName = !fName.equals("") ? fName : x.getName();
				
				File tf = new File(currentPath + File.separator + fileName + mtExt);
				FileUtils.copyInputStreamToFile(is, tf);
				
				System.out.println("Saved -> " + tf.getAbsolutePath());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	@ShellMethod("Save media by Id and eventually conver it, in current path")
	public void getMediaById(String id, @ShellOption(defaultValue="")String mime, @ShellOption(defaultValue="")String fName) {
		saveMedia(mediumRepo.findById(id), mime, fName);
	}
	
	@ShellMethod("Save media by Pos and eventually conver it, in current path")
	public void getMediaByPos(int pos, @ShellOption(defaultValue="")String mime, @ShellOption(defaultValue="")String fName) {
		saveMedia(Optional.of(currentDataPage.getContent().get(pos)), mime, fName);
	}
	
	@ShellMethod("Delete media by Id")
	public void deleteMediaById(String id) {
		Optional<Medium> m = mediumRepo.findById(id);
		
		m.ifPresent(x->{
			try {
				System.out.println("Deleting: ->");
				System.out.println(MediumUtils.serializePretty(x));
				
				contentStore.unsetContent(x);
				mediumRepo.delete(x);
				
				System.out.println("<-- Deleted!");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
	}
}

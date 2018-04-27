package it.zeroics.strg.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.ContentName;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "media-meta")
@Getter @Setter @NoArgsConstructor
public class Medium {

    @Id 
    private String id;
    
    @ContentName(httpHeader = "x-file-name") 
    private String name;
    
    @ContentId 
    private String contentId;
    
    @ContentLength 
    private long contentLength;
    
    @MimeType 
    private String mimeType = "application/octet-stream";

    private Date created = new Date();
    private String summary;
    
   
    private Set<Tag> tags = new HashSet<Tag>();

    
    @Override
    public String toString() {
    	return "M-" + contentId;
    }
}
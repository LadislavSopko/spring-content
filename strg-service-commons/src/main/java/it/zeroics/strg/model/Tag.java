package it.zeroics.strg.model;


import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Tag {

	@Id 
    private String id;
	
	
	private String Key;
	private String Value;
}

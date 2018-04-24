package internal.org.springframework.content.commons.utils;

public class CacheKey {
	public Object entity;
	public String key;
	public String mime;
	
	public CacheKey(Object entity, String key, String mime) {
		super();
		this.entity = entity;
		this.key = key;
		this.mime = mime;
	}
}

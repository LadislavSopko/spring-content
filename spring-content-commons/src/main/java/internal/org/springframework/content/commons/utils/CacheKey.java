package internal.org.springframework.content.commons.utils;

public class CacheKey {
	public String name;
	public String key;
	public String mime;

	public CacheKey(String name, String key, String mime) {
		super();
		this.name = name;
		this.key = key;
		this.mime = mime;
	}
}

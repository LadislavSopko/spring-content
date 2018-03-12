package internal.org.springframework.content.rest.mappings;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.content.commons.repository.Store;
import org.springframework.content.commons.storeservice.ContentStoreInfo;
import org.springframework.content.commons.storeservice.ContentStoreService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

import internal.org.springframework.content.rest.annotations.ContentRestController;
import internal.org.springframework.content.rest.utils.ContentStoreUtils;

public class ContentHandlerMapping extends RequestMappingHandlerMapping {
	
	private static MediaType hal = MediaType.parseMediaType("application/hal+json");
	private static MediaType json = MediaType.parseMediaType("application/json");

	private ContentStoreService contentStores;
	
	public ContentHandlerMapping(ContentStoreService contentStores) {
		this.contentStores = contentStores;
		setOrder(Ordered.LOWEST_PRECEDENCE - 200);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#isHandler(java.lang.Class)
	 */
	@Override
	protected boolean isHandler(Class<?> beanType) {
		return AnnotationUtils.findAnnotation(beanType, ContentRestController.class) != null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#lookupHandlerMethod(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) 
			throws Exception {
		
		// is a content property, if so look up a handler method?
		String[] path = lookupPath.split("/");
		if (path.length < 3)
			return null;
		
		ContentStoreInfo info2 = ContentStoreUtils.findStore(contentStores, path[1]);
		if (info2 != null && isHalOrJsonRequest(request) == false) {
			return super.lookupHandlerMethod(lookupPath, request);
		}
		return null; 
	}
	
	
	private static class CorsConfigurationUtils{
		/* Cors specific due to changes of spring 5.0 */
		private static final List<String> CORS_DEFAULT_PERMIT_ALL =
				Collections.unmodifiableList(Arrays.asList(CorsConfiguration.ALL));

		private static final List<String> CORS_DEFAULT_PERMIT_METHODS =
				Collections.unmodifiableList(Arrays.asList(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name()));
		
		public static CorsConfiguration combine(@Nullable CorsConfiguration first, @Nullable CorsConfiguration other) {
			if (other == null) {
				return first;
			}
			CorsConfiguration config = new CorsConfiguration(first);
			config.setAllowedOrigins(corsCombine(first.getAllowedOrigins(), other.getAllowedOrigins()));
			config.setAllowedMethods(corsCombine(first.getAllowedMethods(), other.getAllowedMethods()));
			config.setAllowedHeaders(corsCombine(first.getAllowedHeaders(), other.getAllowedHeaders()));
			config.setExposedHeaders(corsCombine(first.getExposedHeaders(), other.getExposedHeaders()));
			Boolean allowCredentials = other.getAllowCredentials();
			if (allowCredentials != null) {
				config.setAllowCredentials(allowCredentials);
			}
			Long maxAge = other.getMaxAge();
			if (maxAge != null) {
				config.setMaxAge(maxAge);
			}
			return config;
		}
		
		

		private static List<String> corsCombine(@Nullable List<String> source, @Nullable List<String> other) {
			if (other == null) {
				return (source != null ? source : Collections.emptyList());
			}
			if (source == null) {
				return other;
			}
			if (source.equals(CORS_DEFAULT_PERMIT_ALL) || source.equals(CORS_DEFAULT_PERMIT_METHODS)) {
				return other;
			}
			if (other.equals(CORS_DEFAULT_PERMIT_ALL) || other.equals(CORS_DEFAULT_PERMIT_METHODS)) {
				return source;
			}
			if (source.contains(CorsConfiguration.ALL) || other.contains(CorsConfiguration.ALL)) {
				return new ArrayList<>(Collections.singletonList(CorsConfiguration.ALL));
			}
			Set<String> combined = new LinkedHashSet<>(source);
			combined.addAll(other);
			return new ArrayList<>(combined);
		}
	}

	@Override
	protected CorsConfiguration getCorsConfiguration(Object handler, HttpServletRequest request) {
		CorsConfiguration corsConfiguration = super.getCorsConfiguration(handler, request);
		String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);

		String[] path = lookupPath.split("/");
		if (path.length < 3)
			return corsConfiguration;
		
		ContentStoreInfo info2 = ContentStoreUtils.findStore(contentStores, path[1]);
		if (info2 == null) {
			return corsConfiguration;
		}

		CorsConfigurationBuilder builder = new CorsConfigurationBuilder();
		CorsConfiguration storeCorsConfiguration = builder.build(info2.getInterface());  
		
		/*
		 * public CorsConfiguration combine(@Nullable CorsConfiguration other)
		 * was changed in spring 5.0 it will combine list values in non override but in 
		 * sort of sum, where "*" win any way, so we have to do override manually
		 * cause we want if present list value in CorsAnnotation it must win
		 * 
		 * NOTE: original spring doc note <<
		 * 
		 * 	Combine the non-null properties of the supplied CorsConfiguration with this one.
		 *  When combining single values like allowCredentials or maxAge, this properties are overridden by non-null other properties if any.
		 *  Combining lists like allowedOrigins, allowedMethods, allowedHeaders or exposedHeaders is done in an additive way. 
		 *  For example, combining ["GET", "POST"] with ["PATCH"] results in ["GET", "POST", "PATCH"], but keep in mind that combining ["GET", "POST"] with ["*"] results in ["*"].
		 *  
		 *  Notice that default permit values set by applyPermitDefaultValues() are overridden by any value explicitly defined.
		 *  
		 *  >>
		 */
		return corsConfiguration == null ? storeCorsConfiguration
				: CorsConfigurationUtils.combine(corsConfiguration, storeCorsConfiguration);
				//: corsConfiguration.combine(storeCorsConfiguration);
	}
	
	
	
	

	private boolean isHalOrJsonRequest(HttpServletRequest request) {
		String method = request.getMethod();
		if ("GET".equals(method) || "DELETE".equals(method)) {
			String accept = request.getHeader("Accept");
			if (accept != null) {
				try {
					List<MediaType> mediaTypes = MediaType.parseMediaTypes(accept);
					for (MediaType mediaType : mediaTypes) {
						if (mediaType.equals(hal) || mediaType.equals(json)) {
							return true;
						}
					}
				} catch (InvalidMediaTypeException imte) {
					return true;
				}
			}
		} else if ("PUT".equals(method) || "POST".equals(method)) {
			String contentType = request.getHeader("Content-Type");
			if (contentType != null) {
				try {
					List<MediaType> mediaTypes = MediaType.parseMediaTypes(contentType);
						for (MediaType mediaType : mediaTypes) {
						if (mediaType.equals(hal) || mediaType.equals(json)) {
							return true;
						}
					}
				} catch (InvalidMediaTypeException imte) {
					return true;
				}
			}
		} 
		return false;
	}
	
	@Override
	protected void detectHandlerMethods(final Object handler) {
		super.detectHandlerMethods(handler);
	}
	
	/**
	 * Store requests have to be handled by different RequestMappings
	 * based on whether the request is targeting a Store or content associated
	 * with an Entity
	 */
	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		StoreType typeAnnotation = AnnotationUtils.findAnnotation(method, StoreType.class);
		if (typeAnnotation != null) {
			return new StoreCondition(typeAnnotation, this.contentStores);
		}
		return null;
	}

	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public static @interface StoreType {
		String value() default "store";
	}
	
	public static class StoreCondition implements RequestCondition<StoreCondition> {
		
		private String storeType = "store";
		private ContentStoreService stores;
		
		public StoreCondition(StoreType typeAnnotation, ContentStoreService stores) {
			storeType = typeAnnotation.value();
			this.stores = stores;
		}

		@Override
		public StoreCondition combine(StoreCondition other) {
			throw new UnsupportedOperationException();
		}

		@Override
		public StoreCondition getMatchingCondition(HttpServletRequest request) {
			String path = new UrlPathHelper().getPathWithinApplication(request);
			String[] segments = path.split("/");
			if (segments.length < 3) {
				return null;
			}
			ContentStoreInfo info = ContentStoreUtils.findStore(stores, segments[1]);
			if (info != null && 
					(Store.class.isAssignableFrom(info.getInterface()) && "store".equals(storeType)) ||
					(ContentStore.class.isAssignableFrom(info.getInterface()) && "contentstore".equals(storeType))
				) {
				return this;
			}

			return null;
		}

		@Override
		public int compareTo(StoreCondition other, HttpServletRequest request) {
			if (this.isMappingForRequest(request) && other.isMappingForRequest(request) == false)
				return 1;
			else if (this.isMappingForRequest(request) == false && other.isMappingForRequest(request))
				return -1;
			else {
				String path = new UrlPathHelper().getPathWithinApplication(request);
				String filename = FilenameUtils.getName(path);
				String extension = FilenameUtils.getExtension(filename);
				if (extension != null && "store".equals(storeType)) {
					return -1;
				} else if (extension != null && "contentstore".equals(storeType)) {
					return 1;
				}
				return 0;
			}
		}
		
		public boolean isMappingForRequest(HttpServletRequest request) {
			String path = new UrlPathHelper().getPathWithinApplication(request);
			String[] segments = path.split("/");
			if (segments.length < 3) {
				return false;
			}
			ContentStoreInfo info = ContentStoreUtils.findStore(stores, segments[1]);
			if (info != null && 
					(Store.class.isAssignableFrom(info.getInterface()) && "store".equals(storeType)) ||
					(ContentStore.class.isAssignableFrom(info.getInterface()) && "contentstore".equals(storeType))
				) {
				return true;
			} 
			return false;
		}
		
		
		
		
		
	}
}

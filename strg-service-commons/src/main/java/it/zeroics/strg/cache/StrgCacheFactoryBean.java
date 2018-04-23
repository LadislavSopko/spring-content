package it.zeroics.strg.cache;

import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class StrgCacheFactoryBean implements FactoryBean<it.zeroics.strg.cache.StrgCache>, BeanNameAware, InitializingBean {

	private String name = "";

	@Nullable
	private ConcurrentMap<Object, Object> store;

	private boolean allowNullValues = true;

	@Nullable
	private it.zeroics.strg.cache.StrgCache cache;

	/**
	 * Specify the name of the cache.
	 * <p>
	 * Default is "" (empty String).
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Specify the ConcurrentMap to use as an internal store (possibly
	 * pre-populated).
	 * <p>
	 * Default is a standard {@link java.util.concurrent.ConcurrentHashMap}.
	 */
	public void setStore(ConcurrentMap<Object, Object> store) {
		this.store = store;
	}

	/**
	 * Set whether to allow {@code null} values (adapting them to an internal null
	 * holder value).
	 * <p>
	 * Default is "true".
	 */
	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	@Override
	public void setBeanName(String beanName) {
		if (!StringUtils.hasLength(this.name)) {
			setName(beanName);
		}
	}

	@Override
	public void afterPropertiesSet() {
		this.cache = (this.store != null ? new it.zeroics.strg.cache.StrgCache(this.name, this.store, this.allowNullValues)
				: new it.zeroics.strg.cache.StrgCache(this.name, this.allowNullValues));
	}

	@Override
	@Nullable
	public it.zeroics.strg.cache.StrgCache getObject() {
		return this.cache;
	}

	@Override
	public Class<?> getObjectType() {
		return it.zeroics.strg.cache.StrgCache.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}

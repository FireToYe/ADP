package com.zhilink.manager.common.cache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.RequestToViewNameTranslator;

import com.zhilink.manager.framework.common.utils.SpringContextHolder;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 
 * @ClassName: EhCacheUtils
 * @Description: ehcache实现累加方法，保持原子性，未来替换缓存需要重写incr方法
 * @author an48huf
 * @date 2018年1月2日
 *
 */
public class EhCacheUtils {

	private static CacheManager cacheManager = ((CacheManager) SpringContextHolder.getBean("cacheManager"));

	private static final String SYS_CACHE = "sysCache";

	/**
	 * 流水号+1
	 * 
	 * @param cacheName
	 * @param key
	 */
	public static Long incr(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		Cache cache = getCache(SYS_CACHE);
		cache.acquireWriteLockOnKey(key);
		Element element = cache.get(key);
		if (element == null) {
			cache.releaseWriteLockOnKey(key);
			return null;
		}
		Object value = element.getObjectValue();
		if (value != null) {
			Long valueOf = null;
			try {
				valueOf = Long.valueOf(String.valueOf(value));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if (valueOf != null) {
				long newNum = valueOf.longValue() + 1;
				put(key, newNum);
			}
			cache.releaseWriteLockOnKey(key);
			return valueOf;
		} else {
			cache.releaseWriteLockOnKey(key);
			return new Long(1);
		}
	}


	/**
	 * 获取SYS_CACHE缓存
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}

	/**
	 * 写入SYS_CACHE缓存
	 * 
	 * @param key
	 * @return
	 */
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	/**
	 * 从SYS_CACHE缓存中移除
	 * 
	 * @param key
	 * @return
	 */
	private static void remove(String key) {
		remove(SYS_CACHE, key);
	}

	/**
	 * 获取缓存
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	private static Object get(String cacheName, String key) {
		Element element = getCache(cacheName).get(key);
		return element == null ? null : element.getObjectValue();
	}

	/**
	 * 写入缓存
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	private static void put(String cacheName, String key, Object value) {
		Element element = new Element(key, value);
		getCache(cacheName).put(element);
	}

	/**
	 * 从缓存中移除
	 * 
	 * @param cacheName
	 * @param key
	 */
	private static void remove(String cacheName, String key) {
		getCache(cacheName).remove(key);
	}

	/**
	 * 获得一个Cache，没有则创建一个。
	 * 
	 * @param cacheName
	 * @return
	 */
	private static Cache getCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			cacheManager.addCache(cacheName);
			cache = cacheManager.getCache(cacheName);
			cache.getCacheConfiguration().setEternal(true);
		}
		return cache;
	}

	public static CacheManager getCacheManager() {
		return cacheManager;
	}

}

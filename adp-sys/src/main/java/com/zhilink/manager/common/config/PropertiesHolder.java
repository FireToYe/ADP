package com.zhilink.manager.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.context.ServletContextAware;


/**
 * 可以按照不同的运行模式启用相应的配置
 * 
 */
public class PropertiesHolder extends PropertySourcesPlaceholderConfigurer
		implements InitializingBean, ServletContextAware, ApplicationContextAware {
	private static final String PRODUCTION_MODE = "production.mode";

	/** 开发环境 **/
	public static final String DEVELOPMENT_MODE = "development.mode";

	/** 测试环境 **/
	public static final String TEST_MODE = "test.mode";

	// 缓存所有的属性配置
	private Properties properties;
	@SuppressWarnings("unused")
	private ApplicationContext applicationContext;

	/**
	 * @return the mode
	 */
	public String getMode() {
		return properties.getProperty(PRODUCTION_MODE);
	}

	@Override
	protected Properties mergeProperties() throws IOException {
		String pName = getADPHome() + File.separator + "conf" + File.separator + "adp.properties";
		Properties mergeProperties = super.mergeProperties();
		// 根据路由原则，提取最终生效的properties
		this.properties = new Properties();
		
		// 保存当前激活的profiles
		String activeProfile = mergeProperties.getProperty("profiles.active");
		if (StringUtils.isBlank(activeProfile) || "${profiles.active}".equals(activeProfile)) {
			activeProfile = PRODUCTION_MODE;
		}
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, activeProfile);
		File pFile = new File(pName);
		if (!pFile.exists()) {
			return mergeProperties;
			// throw new RuntimeException("not find properties file :" + pName);
		}
		properties.load(new FileInputStream(pFile));
		Set<Entry<Object, Object>> es = mergeProperties.entrySet();
		for (Entry<Object, Object> entry : es) {
			String key = (String) entry.getKey();
			if (!properties.containsKey(key)) {
				Object value = null;
				value = mergeProperties.get(key);
				if (value != null) {
					properties.put(key, value);
				}
			} else {
				Object value = null;
				value = properties.get(key);
				mergeProperties.put(key, key);
			}
		}
		
		//读取common配置，并合并配置文件
		String common = getADPHome() + "/conf/common.properties";
		File commonFile = new File(common);
		if (commonFile.exists()) {
			Properties commonProperties = new Properties();
			commonProperties.load(new FileInputStream(common));
			Set<Entry<Object, Object>> commonSet = commonProperties.entrySet();
			for (Entry<Object, Object> entry : commonSet) {
				String key = (String) entry.getKey();
				if (!properties.containsKey(key)) {
					Object value = null;
					value = commonProperties.get(key);
					if (value != null) {
						properties.put(key, value);
					}
				}
			}
		}
		if("2".equals(properties.getProperty("dbType"))){
			properties.put("jdbc.url", "jdbc:h2:file:"+getADPHome()+"/db/ica_db;AUTO_SERVER=TRUE;MODE=MySQL");
			properties.put("jdbc.driver", "org.h2.Driver");
			properties.put("jdbc.username", "zhilink");
			properties.put("jdbc.password", "r4l{qwSLxdjreiress6p");
		}
		
		return properties;
	}

	public void setDefaultValue(String key, String value) {
		String pvalue = (String) this.properties.get(key);
		if ("".equals(pvalue) || null == pvalue) {
			this.properties.setProperty(key, value);
		}
	}

	/**
	 * 开放此方法给需要的业务
	 * 
	 * @param key
	 * @return
	 */
	// public String getProperty(String key) {
	// return resolvePlaceholder(key, properties);
	// }

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public void setServletContext(ServletContext servletContext) {
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public static String getADPHome() {
		String adphome = System.getenv("ADP_HOME");
		if ("".equals(adphome) || null == adphome) {
			return System.getProperty("catalina.home");
		} else {
			return adphome;
		}
	}

	public Properties getProperties() {
		return properties;
	}


	
}

package com.zhilink.sys.manager;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String activeProfile = "dev-t100";
		if (args != null && args.length > 0) {
			activeProfile = args[0];
		}

		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, activeProfile);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:spring-context.xml", "spring-context-shiro.xml", "classpath:spring-init.xml",
						"classpath:spring-context-jedis.xml" });

		context.start();
		context.close();
		// 退出
		// System.exit(0);
	}
}
 
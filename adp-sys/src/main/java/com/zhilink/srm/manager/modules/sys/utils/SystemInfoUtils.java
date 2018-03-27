package com.zhilink.srm.manager.modules.sys.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;


public class SystemInfoUtils {

	public static Map SystemProperty() {
		Map<String, Comparable> monitorMap = new HashMap();
		Runtime r = Runtime.getRuntime();
		Properties props = System.getProperties();
		InetAddress addr = null;
		String ip = "";
		String hostName = "";
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			ip = "无法获取主机IP";
			hostName = "无法获取主机名";
		}
		if (null != addr) {
			try {
				ip = addr.getHostAddress();
			} catch (Exception e) {
				ip = "无法获取主机IP";
			}
			try {
				hostName = addr.getHostName();
			} catch (Exception e) {
				hostName = "无法获取主机名";
			}
		}
		
		List list = new ArrayList();
		Enumeration<NetworkInterface> nets=null;
		try {
			nets = NetworkInterface
					.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		for (NetworkInterface netint : Collections.list(nets)) {
			byte[] mac=null;
			try {
				mac = netint.getHardwareAddress();
			} catch (SocketException e) {
				e.printStackTrace();
			}
			StringBuilder sb = new StringBuilder();
			if (mac != null) {
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i],
							(i < mac.length - 1) ? "-" : ""));
				}
				list.add(sb);
			}
		}
        String macs = StringUtils.join(list, ",");
		monitorMap.put("macs", macs);
		monitorMap.put("hostIp", ip);// 本地ip地址
		monitorMap.put("hostName", hostName);// 本地主机名
		monitorMap.put("osName", props.getProperty("os.name"));// 操作系统的名称
		monitorMap.put("arch", props.getProperty("os.arch"));// 操作系统的构架
		monitorMap.put("osVersion", props.getProperty("os.version"));// 操作系统的版本
		monitorMap.put("processors", r.availableProcessors());// JVM可以使用的处理器个数
		monitorMap.put("javaVersion", props.getProperty("java.version"));// Java的运行环境版本
		monitorMap.put("vendor", props.getProperty("java.vendor"));// Java的运行环境供应商
		monitorMap.put("javaUrl", props.getProperty("java.vendor.url"));// Java供应商的URL
		monitorMap.put("javaHome", props.getProperty("java.home"));// Java的安装路径
		monitorMap.put("tmpdir", props.getProperty("java.io.tmpdir"));// 默认的临时文件路径
		return monitorMap;
	}
	
}

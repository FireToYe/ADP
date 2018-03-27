package com.zhilink.manager.common.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.manager.modules.sys.verify.VerifyLicense;

/**
 * 
 * @author jaray 此功能 用于 将jar包中的资源文件 复制到 WEB-INF 目录之中
 */
public class ResourceJarOuterBean implements InitializingBean, ApplicationListener<ApplicationEvent> {
	// private static final Log logger =
	// LogFactory.getLog(ResourceJarOuterBean.class);
	private static Logger logger = LoggerFactory.getLogger(ResourceJarOuterBean.class);
	private Resource[] resourceFiles; // 资源文件
	private String webRoot;// WEB-IFO
	public static final String VIEW_ROOT = "views";
	public static final String JVP_ENDFIX = ".jvp";// 当为jvp时 不会替换 但钱工程下的页面 也就是说
													// 可以自行独立定制
	public static final String JSP_ENDFIX = ".jsp"; // 最终以jsp的形态存在

	public void afterPropertiesSet() throws Exception {
		this.webRoot = getWebRootPath();
		InputStream in = null;
		// 解压文件
		for (Resource rs : this.resourceFiles) {
			try {
				String tpath = rs.toString();
				String fileName = tpath.substring(tpath.indexOf(VIEW_ROOT), tpath.length() - 1);
				FileOutputStream fo = null;

				File outFile = null;
				if (fileName.indexOf(JSP_ENDFIX) == -1 && fileName.indexOf(JVP_ENDFIX) == -1)
					continue;// 暂时只接受这两种格式的文件
				if (fileName.indexOf(JVP_ENDFIX) != -1) {
					fileName = fileName.replace(JVP_ENDFIX, JSP_ENDFIX);
					outFile = new File(this.webRoot + fileName);
					if (outFile.exists()) {
						continue; // 当为jvp的时候 存在相同的jsp页面 则不使用jvp页面
					}
				} else {
					outFile = new File(this.webRoot + fileName);
				}
				if (!outFile.getParentFile().exists()) {
					boolean result = outFile.getParentFile().mkdirs();
					if (!result) {
						logger.warn(" create path file,pleace  check  dir  !");
					}
				}
				fo = new FileOutputStream(outFile);
				in = rs.getInputStream();// 得到对应的文件通道
				copy(in, fo);
			} catch (IOException e) {
				logger.error("move jar Resource to WEB-INF error  !", e);
			}
		}
		// license信息初始化
		try {
			VerifyLicense.createInstance().verify();
			logger.info("验证Licenses结束 !");
			logger.info("wmsAppAmount=" + Global.getLicenseContent().getWmsAppAmount());
			logger.info("consumerAmount=" + Global.getLicenseContent().getConsumerAmount());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("验证Licenses不成功 !", e);
		}
	}

	public Resource[] getResourceFiles() {
		return resourceFiles;
	}

	public void setResourceFiles(Resource[] resourceFiles) {
		this.resourceFiles = resourceFiles;
	}

	public void onApplicationEvent(ApplicationEvent event) {

	}

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		// System.out.println("===========os.name:" +
		// System.getProperties().getProperty("os.name"));
		// System.out.println(File.separator);
		// System.out.println(getWebRootPath());
		// String file =
		// "jar:file:/D:/wp/srm/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/srm-manager/WEB-INF/lib/srm-sys-0.1.0-SNAPSHOT.jar!/views/modules/wms/views.jsp";
		//
		// System.out.println(file.substring(file.indexOf("views"), file.length()));

		String str = "D:\\wp\\srm\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\srm-manager\\WEB-INF\\views\\modules\\sys\\userModifyPwd.jsp";
		File f = new File(str);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}

	}

	public static String getWebRootPath() {

		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		path = path.replace('/', '\\'); // 将/换成\
		path = path.replace("file:", ""); // 去掉file:
		path = path.replace("classes\\", ""); // 去掉class\

		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			path = path.substring(1); // 去掉第一个\,如 \D:\JavaWeb...
		} else {
			path = path.replace("\\", File.separator); // 换成linux 风格
		}
		return path;
	}

	/**
	 * copy file
	 * 
	 */
	public static void copy(InputStream in, OutputStream out) throws Exception {
		int length = 2048;
		byte[] buffer = new byte[length];
		while (true) {
			int ins = in.read(buffer);
			if (ins == -1) {
				in.close();
				out.flush();
				out.close();
				break;
			} else
				out.write(buffer, 0, ins);
		}
	}

}

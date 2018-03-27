package com.zhilink.srm.manager.modules.sys.verify;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.prefs.Preferences;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.zhilink.manager.common.config.PropertiesHolder;
import com.zhilink.manager.common.web.ResourceJarOuterBean;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/***
 * @ClassName: VerifyLicense
 * @Description: License 验证类，兼容两种license，
 *               1、tomcat/conf或者项目WEB-INF/classes或者项目WEB-INF/
 *               存放zhilink.license、PublicCerts.store文件,此时读取默认的STOREPWD
 *               2、tomcat/conf或者项目WEB-INF/classes或者项目WEB-INF/
 *               存放zhilink.lic文件,这种情况下，可自定义STOREPWD公钥仓库密码，可以自定义PUBLICALIAS，SUBJECT,licPath
 * @author an48huf
 * @date 2017年11月29日

 */
public class VerifyLicense {

	private static Logger logger = LoggerFactory.getLogger(VerifyLicense.class);

	// common param
	private static String PUBLICALIAS = "publiccert";
	private static String STOREPWD = "389090e2a6d544689dd65ad05c293bdc";
	private static String SUBJECT = "ZLicense";
	private static String licPath = "zhilink.license";
	private static String pubPath = "PublicCerts.store";
	private static final String PROPERTIESPATH = "zhilink.properties";
	private static final String PARTPATH = "classes\\";

	// license压缩文件
	private static final String COMPRESSFILE = "zhilink.lic";
	// 验证参数文件
	private static final String VERIFYPARAMFILE = "verifyparam.properties";
	// 公钥文件
	private static final String PUBLICCERTSFILE = "PublicCerts.store";
	// license文件
	private static final String LICENSEFILE = "zhilink.license";

	/**
	 * PublicCerts.store的输入流
	 */
	private static InputStream storeLocalInputStream;
	
	private static ZipFile zipFile;

	/**
	 * zhilink.license 转换的字节码
	 */
	private static byte[] licenseBytes;

	/**
	 * LicenseContent对象,屏蔽set方法，其他应用使用
	 */
	private static LicenseContent licenseContent;

	private VerifyLicense() {
	}

	private static VerifyLicense verifyLicense = new VerifyLicense();

	public static VerifyLicense createInstance() {
		
		return verifyLicense;
	}

	private static Properties prop = new Properties();

	/**
	 * 对外使用，其他应用包使用该方法获取LicenseContent
	 * 
	 * @return
	 */
	public static LicenseContent getLicenseContent() {
		if (licenseContent == null) {
			return new LicenseContent();
		}
		return licenseContent;
	}

	private boolean setParam() throws IOException {

		// 先寻找压缩包
		String realConfigPath = getRealConfigPath(COMPRESSFILE);
		if (StringUtils.isNotBlank(realConfigPath)) {
			decompressLicense(realConfigPath);
			return setParam(null);
		}
		// 没有压缩包
		return setParam(PROPERTIESPATH);
	}

	/**
	 * 解压zhilink.lic
	 * 
	 * @param realConfigPath
	 *            zhilink.lic文件真实路径
	 * @throws IOException
	 */
	private void decompressLicense(String realConfigPath) throws IOException {

		zipFile = new ZipFile(realConfigPath);
		FileInputStream fileStream = new FileInputStream(realConfigPath);
		ZipInputStream zis = new ZipInputStream(fileStream);


        for( Enumeration entries = zipFile.getEntries() ; entries.hasMoreElements() ; ){
            ZipEntry entry = (ZipEntry)entries.nextElement() ;

			logger.debug("decompress file :" + entry.getName());

			// 1.加载验证参数文件
			if (VERIFYPARAMFILE.equals(entry.getName())) {

				BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
				prop.load(bis);
				bis.close();
			} else if (PUBLICCERTSFILE.equals(entry.getName())) {// 公钥文件
				storeLocalInputStream = zipFile.getInputStream(entry);
			} else if (LICENSEFILE.equals(entry.getName())) {// license文件

				int size = Math.min((int) entry.getSize(), 1024 * 1024);
				licenseBytes = new byte[size];

				InputStream in = zipFile.getInputStream(entry);
				BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
				try {
					bis.read(licenseBytes);
				} finally {
					if(bis!= null){
						bis.close();
					}if(in!=null){
						in.close();
					}
				}
			}

		}
        if(zis!=null){
    		zis.close();
        }
        if(fileStream != null){
        	fileStream.close();
        }
	}

	/**
	 * 获取license路劲
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	private String getRealConfigPath(String fileName) {

		String realConfigPath = "";
		String webRootPath = ResourceJarOuterBean.getWebRootPath();
		File file = new File(webRootPath + fileName);
		if (file.exists()) {
			realConfigPath = file.getPath();
		} else {
			file = new File(webRootPath + PARTPATH + fileName);
			if (file.exists()) {
				realConfigPath = file.getPath();
			} else {
				file = new File(PropertiesHolder.getADPHome() + File.separator + "conf" + File.separator + fileName);
				if (file.exists()) {
					realConfigPath = file.getPath();
				} else {
					logger.info(fileName + "文件不存在!");
				}
			}
		}
		return realConfigPath;
	}

	/**
	 * 设置客户配置参数
	 * 
	 * @param propertiesPath
	 * @return
	 * @throws IOException
	 */
	private boolean setParam(String propertiesPath) throws IOException {

		if (StringUtils.isNotBlank(propertiesPath)) {
			System.out.println("propertiesPath=" + propertiesPath);
			prop.load(VerifyLicense.class.getClassLoader().getResourceAsStream(propertiesPath));
			// 客户加密信息
			licPath = getRealConfigPath(licPath);
			if (StringUtils.isBlank(licPath)) {
				logger.info("license licPath文件不存在!");
				return false;
			}
			logger.debug("licPath=" + licPath);
			pubPath = StringUtils.defaultIfBlank(prop.getProperty("pubPath"), pubPath);
			// 公钥
			pubPath = getRealConfigPath(pubPath);
			if (StringUtils.isBlank(pubPath)) {
				logger.info("公钥pubPath文件不存在!");
				return false;
			}
			logger.debug("pubPath=" + pubPath);
		}

		PUBLICALIAS = StringUtils.defaultIfBlank(prop.getProperty("PUBLICALIAS"), PUBLICALIAS);
		STOREPWD = StringUtils.defaultIfBlank(prop.getProperty("STOREPWD"), STOREPWD);
		SUBJECT = StringUtils.defaultIfBlank(prop.getProperty("SUBJECT"), SUBJECT);
		licPath = StringUtils.defaultIfBlank(prop.getProperty("licPath"), licPath);

		return true;
	}

	/**
	 * license验证方法，公共方法对外开放
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean verify() throws Exception {

		if (!setParam()) {
			return false;
		}
		LicenseManager licenseManager = LicenseManagerHolder.getLicenseManager(initLicenseParams());
		// install license file
		try {

			licenseContent = licenseManager.install(licenseBytes);
			// licenseContent = licenseManager.install(new
			// File("D:/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp2/wtpwebapps/srm-manager/WEB-INF/classes/zhilink.license"));

			logger.info("License file instal successfully!");
			System.out.println(JSON.toJSONString(licenseContent));
		} catch (Exception e) {
			// e.printStackTrace();\
			if(storeLocalInputStream!=null){
				storeLocalInputStream.close();
			}
			if (zipFile != null){
				zipFile.close();
			}
			String moreInfo = "License file instal failure";
			logger.warn(licPath);
			logger.warn(moreInfo);
			throw e;
		}
		// verify license file
		try {
			licenseManager.verify();
			logger.info("License file verify successfully!");
		} catch (Exception e) {
			// e.printStackTrace();
			String moreInfo = "License file verify failure";
			logger.warn(moreInfo);
			throw e;
		}finally {
			
			if(storeLocalInputStream!=null){
				storeLocalInputStream.close();
			}
			if (zipFile != null){
				zipFile.close();
			}
		}
		return true;
	}

	//
	private static LicenseParam initLicenseParams() {

		Preferences preference = Preferences.userNodeForPackage(VerifyLicense.class);
		CipherParam cipherParam = new DefaultCipherParam(STOREPWD);

		KeyStoreParam privateStoreParam = null;

		if (storeLocalInputStream != null) {
			privateStoreParam = new DefaultKeyStoreParam(VerifyLicense.class, storeLocalInputStream, PUBLICALIAS,
					STOREPWD, null);
		} else {
			privateStoreParam = new DefaultKeyStoreParam(VerifyLicense.class, pubPath, PUBLICALIAS, STOREPWD, null);
		}

		LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT, preference, privateStoreParam, cipherParam);
		return licenseParams;
	}
	
	public static void main(String[] args) {
		String path="C:\\Users\\Administrator\\Desktop\\license (29).lic";
		try {
			VerifyLicense.createInstance().verify();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


package com.zhilink.srm.manager.modules.sys.verify;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.prefs.Preferences;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;


public class RequestVerifyLicense {
	//common param
	private static String PUBLICALIAS = "";
	private static String STOREPWD = "";
	private static String SUBJECT = "";
	private static String licPath = "";
	private static String pubPath = "";
	

	
	public void setParam(String propertiesPath) throws IOException {
		// 
		Properties prop = new Properties();
		System.out.println("propertiesPath="+propertiesPath);
/*		InputStream in = getClass().getResourceAsStream(propertiesPath);		
		in = new FileInputStream(propertiesPath);	*/
		prop.load(VerifyLicense.class.getClassLoader().getResourceAsStream(propertiesPath));
		//in = request.getSession().getServletContext().getResourceAsStream("/images/someimage.jpg");
/*		prop.load(in);	*/	
		PUBLICALIAS = prop.getProperty("PUBLICALIAS");
		STOREPWD = prop.getProperty("STOREPWD");
		SUBJECT = prop.getProperty("SUBJECT");
		licPath = prop.getProperty("licPath");
		pubPath = prop.getProperty("pubPath"); 
	}

	public boolean verify()  {		

		LicenseManager licenseManager = LicenseManagerHolder
				.getLicenseManager(initLicenseParams());
		// install license file
		try {
			licenseManager.install(new File(licPath));
					
		} catch (Exception e) {
			//e.printStackTrace();			
			String moreInfo ="License file verify failure";
			System.out.println("moreInfo="+moreInfo);
		}
		// verify license file
		try {
			licenseManager.verify();
			
		} catch (Exception e) {
			//e.printStackTrace();			
			String moreInfo ="License file verify failure";
			System.out.println("moreInfo="+moreInfo);
		}
		return true;
	}

	//
	private static LicenseParam initLicenseParams() {
		Preferences preference = Preferences
				.userNodeForPackage(VerifyLicense.class);
		CipherParam cipherParam = new DefaultCipherParam(STOREPWD);

		KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
				VerifyLicense.class, pubPath, PUBLICALIAS, STOREPWD, null);
		LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT,
				preference, privateStoreParam, cipherParam);
		return licenseParams;
	}
}
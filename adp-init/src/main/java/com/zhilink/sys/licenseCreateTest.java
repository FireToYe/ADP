package com.zhilink.sys;

import com.zhilink.srm.manager.modules.sys.verify.VerifyLicense;



public class licenseCreateTest {
	public static void main(String[] args){
		CreateLicense cLicense = new CreateLicense();
		
		cLicense.setParam("createparam.properties");
        //生成证书
		cLicense.create();
		
        VerifyLicense vLicense = VerifyLicense.createInstance();
        //获取参数
 //       vLicense.setParam("createparam.properties");
        try {
			vLicense.verify();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

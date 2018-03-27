package com.zhilink.srm.manager.modules.sys.web;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhilink.srm.manager.modules.sys.utils.SystemInfoUtils;
import com.zhilink.srm.manager.modules.sys.verify.VerifyLicense;

@Controller
@RequestMapping("${adminPath}/sys/systemInfo")
public class SystemInfoController  {

	@RequiresPermissions("sys:systemInfo:view")
	@RequestMapping("show")
	public String systemInfo(Model model) throws Exception {
		
		model.addAttribute("licenseInfo", VerifyLicense.getLicenseContent());
		model.addAttribute("systemInfo", SystemInfoUtils.SystemProperty());
		return "modules/sys/systemInfo";
	
	}
}

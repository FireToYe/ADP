package com.zhilink.srm.manager.modules.sys.verify;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zhilink.manager.common.config.PropertiesHolder;
import com.zhilink.manager.common.utils.FileUtils;
import com.zhilink.manager.common.web.ResourceJarOuterBean;
import com.zhilink.srm.common.web.BaseController;

@Controller  
@RequestMapping(value = "${adminPath}/fileupload/")
public class UploadLicense extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(UploadLicense.class);
	private static final String PARTPATH = "classes\\";
	
	    @RequestMapping(consumes = "multipart/form-data", value = "upload", method = RequestMethod.POST)  
	    public String uploadFile(HttpServletRequest request, @RequestParam(required = false) MultipartFile file,RedirectAttributes redirectAttributes) throws IOException {  
	          
	        if (!file.isEmpty()) {
	        	String fileName = file.getOriginalFilename();
	        	String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
	        	if(!"lic".equals(fileType)){
	        		addMessage(redirectAttributes,"只能上传.lic类型的文件");
	        		return "redirect:" + adminPath + "/sys/systemInfo/show?repage";
	        	}
	            try {  
	                String path = getRealConfigPath(file.getOriginalFilename());
	                if(!StringUtils.isBlank(path)){
	                	file.transferTo(new File(path+File.separator+file.getOriginalFilename()));
	                	VerifyLicense.createInstance().verify();
	                	addMessage(redirectAttributes,"上传成功!");
	                }else{
	                	addMessage(redirectAttributes,"找不到存放的路径");
	                }
	            } catch (Exception e) {
	            	addMessage(redirectAttributes,"上传证书出错");
	            	e.printStackTrace();
	            } 
	        }
	        return "redirect:" + adminPath + "/sys/systemInfo/show?repage";
	          
	    }  
	    
	//获取证书要存放的路径,如果证书已经存在，则备份原来的证书
    private String getRealConfigPath(String fileName) {

		String realConfigPath = PropertiesHolder.getADPHome() + File.separator + "conf" + File.separator;
		String webRootPath = ResourceJarOuterBean.getWebRootPath();
		
		File file = new File(realConfigPath+fileName);
		if(file.getParentFile().isDirectory()){
			FileUtils.copyFileCover(file.getPath(), file.getPath()+".bak", true);
			FileUtils.deleteFile(file.getPath());
		}else{
			realConfigPath = webRootPath;
			file = new File(realConfigPath+fileName);
			if(file.getParentFile().isDirectory()){
				FileUtils.copyFileCover(file.getPath(), file.getPath()+".bak", true);
				FileUtils.deleteFile(file.getPath());
			}else{
				realConfigPath = webRootPath + PARTPATH;
				file = new File(realConfigPath+fileName);
				if(file.getParentFile().isDirectory()){
					FileUtils.copyFileCover(file.getPath(), file.getPath()+".bak", true);
					FileUtils.deleteFile(file.getPath());
				}else{
					realConfigPath="";
					logger.info(fileName+"上传路径不存在");
				}
			}
		}
		
		return realConfigPath;
	}
    
}
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.common.service.PrintService;
import com.zhilink.srm.manager.modules.sys.entity.PrintTemplate;
import com.zhilink.srm.manager.modules.sys.utils.PrintUtils;
import com.zhilink.srm.manager.modules.sys.dao.PrintTemplateDao;

/**
 * 打印模板Service
 * @author sushengshun
 * @version 2017-10-12
 */
@Service
@Transactional(readOnly = true)
public class PrintTemplateService extends CrudService<PrintTemplateDao, PrintTemplate> {

	@Autowired
	private PrintService printService;
	@Autowired
	private PrintTemplateDao printTemplateDao;
	
	public PrintTemplate get(String id) {
		return super.get(id);
	}
	
	public List<PrintTemplate> findList(PrintTemplate printTemplate) {
		return super.findList(printTemplate);
	}
	
	public Page<PrintTemplate> findPage(Page<PrintTemplate> page, PrintTemplate printTemplate) {
		return super.findPage(page, printTemplate);
	}
	
	@Transactional(readOnly = false)
	public void save(PrintTemplate printTemplate) {
		
		//同一模板标识下，只能同时启用一个
		//当保存时候对status为1：启用的做处理
		if(printTemplate.getStatus() == 1) {
			editStatusToStop(printTemplate.getTemplateCode());
		}
		super.save(printTemplate);
	}
	
	@Transactional(readOnly = false)
	public void delete(PrintTemplate printTemplate) {
		super.delete(printTemplate);
	}
	
	/**
	 * 打印预览
	 * @param response
	 * @param jsonString
	 * @param tplContent
	 */
	public void printShow(HttpServletResponse response ,String jsonString , String tplContent) {
		//生成一个文件名
		Random rand = new Random(25);
		int roundNum = rand.nextInt(100);
		String fileName = String.valueOf(new Date().getTime()) + roundNum;
		//加模板内使用到的java方法
		printService.addClass("PrintUtils", PrintUtils.class);
		printService.printPdfFromTplContent(response, tplContent, jsonString, fileName);
	}
	
	/**
	 * 对模板状态改为 未启用
	 * @param tplCode
	 */
	public void editStatusToStop(String tplCode) {
		if(StringUtils.isBlank(tplCode)) {
			return;
		}
		printTemplateDao.editStatusToStop(tplCode);
	}
	
	/**
	 * 通过模板标识获取启用的模板内容
	 * @param tplCode
	 * @return
	 */
	public String getTplContent(String tplCode) {
		if(StringUtils.isBlank(tplCode)) {
			return "";
		}
		PrintTemplate printTemplateParam = new PrintTemplate();
		printTemplateParam.setStatus(1);
		printTemplateParam.setTemplateCode(tplCode);
		List<PrintTemplate> printTemplates = printTemplateDao.findListNoLike(printTemplateParam);
		if(printTemplates == null || printTemplates.size()==0) {
			return "";
		}
		PrintTemplate printTemplateResult = printTemplates.get(0);
		String tplContent = printTemplateResult.getTemplateContent();
		tplContent = StringEscapeUtils.unescapeHtml4(tplContent);
		return tplContent;
	}
	/**
	 * 预览html
	 * @param jsonString
	 * @param tplContent
	 * @param number 数量
	 */
	public void getHtml(String jsonString , String tplContent,Integer number){
		//加模板内使用到的java方法
		printService.addClass("PrintUtils", PrintUtils.class);
		JSONObject obj = JSONObject.parseObject(jsonString);
		JSONArray jsonArray = obj.getJSONArray("list");
		for (int i = 0; i < number-1; i++) {
			jsonArray.add(i+1, jsonArray.get(0));
		}
		jsonString = obj.toString();
		printService.printHtmlFromTplContent(tplContent, jsonString);
		
	}
}
package com.zhilink.srm.common.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.zhilink.manager.framework.common.print.PrintHtmlTpl;
import com.zhilink.manager.framework.common.utils.DownloadUtils;
import com.zhilink.manager.framework.common.utils.FileUtils;

/**
 * 打印
 *
 */
public class PrintService{
	
	private Map<String, Class<?>> mapClass;
	
	public PrintService() {
		mapClass=new HashMap<String, Class<?>>();
	}
	
	public void addClass(String className, Class<?> T){
		mapClass.put(className, T);
	}
	
	/**
	 * 注入一些方法到模板页面
	 * @param context
	 */
	private void initPrintHtmlTpl(PrintHtmlTpl context){
		if(mapClass !=null && mapClass.size()>0){
			for(Entry<String, Class<?>> kv : mapClass.entrySet()){
				context.addClass(kv.getKey(), kv.getValue());
			}
		}
	}
	
	private String tempFilePath;
	
	public String getTempFilePath() {
		return tempFilePath;
	}


	public void setTempFilePath(String tempFilePath) {
		if(System.getProperty("os.name").toLowerCase().contains("windows")){
			tempFilePath="D:"+tempFilePath;
		}
		this.tempFilePath = tempFilePath;
		File file = new File(tempFilePath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}	
	
	/**
	 * 打印html格式
	 * @param response
	 * @param tpl
	 * @param json
	 * @param fileName
	 */
	public void printHtml(HttpServletResponse response, String tpl, String json,String fileName){
		String htmlPath=tempFilePath+File.separator+fileName+".html";

		PrintHtmlTpl p=new PrintHtmlTpl();
		initPrintHtmlTpl(p);
		p.makeViewHtml(tpl, htmlPath, json);
		
		DownloadUtils.download(response,htmlPath,"1","temp.html");
		
		FileUtils.deleteFile(htmlPath);
		
	}
	
	/**
	 * 打印pdf格式
	 * @param response
	 * @param tpl
	 * @param json
	 * @param fileName
	 */
	public void printPdf(HttpServletResponse response, String tpl, String json,String fileName){
		String pdfPath=tempFilePath+File.separator+fileName+".pdf";
		
		PrintHtmlTpl p=new PrintHtmlTpl();
		initPrintHtmlTpl(p);
		p.makeViewPdf(tpl, pdfPath, json);
		
		DownloadUtils.download(response,pdfPath,"1","temp.pdf");
		
		FileUtils.deleteFile(pdfPath);
	}
	
	/**
	 * 打印pdf格式，通过传入模板内容
	 * @param response
	 * @param tplContent
	 * @param json
	 * @param fileName
	 */
	public void printPdfFromTplContent(HttpServletResponse response, String tplContent, String json,String fileName){
		String pdfPath=tempFilePath+File.separator+fileName+".pdf";
		
		PrintHtmlTpl p=new PrintHtmlTpl();
		initPrintHtmlTpl(p);
		p.makeViewPdfFromTplContent(tplContent, pdfPath, json);
		
		DownloadUtils.download(response,pdfPath,"1","temp.pdf");
		
		FileUtils.deleteFile(pdfPath);
	}
	/**
	 * 生成html写入文件
	 * @param tplContent
	 * @param json
	 */
	public void printHtmlFromTplContent(String tplContent, String json){
		//生成的html路径
		String temp = Thread.currentThread().getContextClassLoader().getResource("/").getPath().replaceAll("WEB-INF/classes/", "static/printview/preview.html");
		//使用静态图片模拟二维码图片
		String regex = "(?is)<img[\\s]+class=\"qrcode\"[\\s]+src=\"\"[\\s]+value=\"([^\"]*?)\"[\\s]+width=\"([\\d]+?)px\"[^<>]*?>";
		Matcher match = Pattern.compile(regex, 10).matcher(tplContent);
		while(match.find()){
			String oldImg = match.group(0);
			String newImg = oldImg.replace("src=\"\"", "src=\"pic.jpg\"");
			tplContent = tplContent.replace(oldImg, newImg);
		}
		PrintHtmlTpl p=new PrintHtmlTpl();
		initPrintHtmlTpl(p);
		String html = p.createHtmlFromTplContent(tplContent,json);
	    FileUtils.deleteFile(temp);
		try {
			File f = new File(temp);
			if (!f.exists()) {
			    f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(html);
			writer.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
}
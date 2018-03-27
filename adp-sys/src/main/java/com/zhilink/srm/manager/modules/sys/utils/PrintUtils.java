package com.zhilink.srm.manager.modules.sys.utils;

import java.io.UnsupportedEncodingException;

import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.zhilink.manager.framework.common.utils.StringUtils;


public class PrintUtils {

	/**
	 * 手动为打印pdf内容加空格
	 * @param val
	 * @param len
	 * @return
	 */
	public static String pdfPrintManulWrap(String val,int len) {
		if(StringUtils.isEmpty(val)) {
			return "";
		}
		//给长度一个默认值
		len = len <=0 ? 10: len;
		if(val.length()<=len) {
			return val;
		}
		StringBuffer stringBuffer = new StringBuffer();
		int lastIndex = 0;
		for(int i=0;i<=val.length();i+=len) {
			if((i+len)>val.length()) {
				lastIndex = val.length();
			}else {
				lastIndex = i+len;
			}
			stringBuffer.append(val.substring(i, lastIndex)).append("\n");
		}
		return stringBuffer.toString();
	}	
	
	/**
	 * 按字节长度加换行符,中文两个字节
	 * @param str
	 * @param subSLength
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String pdfPrintManulWrap2(String str,int subSLength){
		try {
			if(StringUtils.isEmpty(str)) {
				return "";
			}
			int strLen = str.length();
			int beginIndex = 0;
			int lastIndex = 0;
			StringBuffer stringBuffer = new StringBuffer();
			while(beginIndex<strLen) {
	            int tempSubLength = subSLength;//截取字节数  
	            if((beginIndex+subSLength)>str.length()) {
	            	lastIndex = str.length();
	            }else {
	            	lastIndex = beginIndex+subSLength;
	            }
	            String subStr = str.substring(beginIndex, lastIndex);//截取的子串 
	            int subStrByetsL = subStr.getBytes("GBK").length;//截取子串的字节长度   
	            // 说明截取的字符串中包含有汉字    
	        	int lastIndex2 = lastIndex;
	            while (subStrByetsL > tempSubLength){
	                --lastIndex2;  
	                subStr = str.substring(beginIndex, lastIndex2);    
	                subStrByetsL = subStr.getBytes("GBK").length; 
	            }
	            beginIndex = lastIndex2;
	            stringBuffer.append(subStr).append("\n");
			}
			return stringBuffer.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ""; 
		}
	}
}

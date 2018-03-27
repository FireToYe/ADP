package com.zhilink.adp.client.test;

import java.util.List;

import feign.Param;
import feign.RequestLine;

public interface TestInterface {
	@RequestLine("GET /api/getNewsList")
	public Object getNewsList();
	
	@RequestLine("GET /api/wms/entSiteList")
	public List<EntSite> entSite();
	
	@RequestLine("GET /api/ossproxy/proxyClientNodes/openClientVisit?domain={domain}")
	public void openClientVisit(@Param("domain")String domain);
	
	@RequestLine("GET /api/ossproxy/clientNodes")
	public Object clientNodes();
}

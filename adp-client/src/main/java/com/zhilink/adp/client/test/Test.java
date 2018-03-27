package com.zhilink.adp.client.test;

import java.util.List;

import com.zhilink.adp.client.core.AdpClient;

/**
 * Hello world!
 *
 */
public class Test 
{
    public static void main( String[] args )
    {

    	// AdpClient client=new AdpClient("http://172.31.75.184:10088/adpweb","admin","admin");
    	//AdpClient client=new AdpClient("http://172.31.75.222:8082/adpweb","admin","admin");
    	//AdpClient client=new AdpClient("http://172.31.75.184:10088/adpweb","admin","admin");
//      client.refreshToken();
    	//AdpClient client=new AdpClient("http://172.31.75.153:8080/adpweb","admin","admin");
    	AdpClient client=new AdpClient("http://localhost:8080/oss-adpweb","admin","admin");
    	//172.31.75.153:8080
    	TestInterface inter=client.getObject(TestInterface.class);
    	inter.clientNodes();
//    	long t=System.currentTimeMillis();
//    	List<EntSite> list1=   inter.entSite();
//    	long t1=System.currentTimeMillis();
//    	System.out.println(t1-t);
//    	client.InvalidToken();;
//    	List<EntSite> list=   inter.entSite();
//    	long t2=System.currentTimeMillis();
//    	System.out.println(t2-t1);
//    	//List<EntSite> list12=   inter.entSite();
//    	// client.refreshToken();
//    	long t3=System.currentTimeMillis();
//    	System.out.println(t3-t2);
//    	for(EntSite e:list){
//    		System.out.println(e.getEntName());
//    	}
    	
    	//AdpClient test=new AdpClient("http://localhost:8080/adp-manager","xxy","123");
    	//TestInterface inter=client.getInterface(TestInterface.class);
    	//inter.getNewsList();
    	
    	
    	
    	
    }
}

package com.zhilink.manager.common.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zhilink.manager.framework.common.utils.MD5Tools;
import com.zhilink.srm.common.utils.JdbcUtils;
import com.zhilink.srm.common.utils.Mysql2H2Util;

public class ResourceSqlBean implements InitializingBean, ApplicationListener<ApplicationEvent> {
	
	private static Logger logger = LoggerFactory.getLogger(ResourceSqlBean.class);
	private Resource[] resourceFiles; // 资源文件
	private String startUpdateVersion="N";
	//private String webRoot;// WEB-IFO
	public static final String SQL_ENDFIX = ".sql";
	public static final String APP_SYS="adp-sys";
	private Map<String, List<String>> mapFile;
	private Map<String, String> mapApps;
	
	private boolean isH2db = false;
	private static final String H2_DRIVER = "org.h2.Driver";
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Value("${jdbc.driver}")
	private String driver;
	
	private CountDownLatch latch = null ;
	private ConcurrentLinkedQueue<String> queueError;
	private int threadNum=30;
	
	public Resource[] getResourceFiles() {
		return resourceFiles;
	}

	public void setResourceFiles(Resource[] resourceFiles) {
		this.resourceFiles = resourceFiles;
	}
	
	public String getStartUpdateVersion() {
		return startUpdateVersion;
	}

	public void setStartUpdateVersion(String startUpdateVersion) {
		this.startUpdateVersion = startUpdateVersion;
	}
	
	public static String getWebRootPath() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		path = path.replace('/', '\\'); // 将/换成\
		path = path.replace("file:", ""); // 去掉file:
		path = path.replace("classes\\", ""); // 去掉class\

		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			path = path.substring(1); // 去掉第一个\,如 \D:\JavaWeb...
		} else {
			path = path.replace("\\", File.separator); // 换成linux 风格
		}
		return path;
	}

	
	public void afterPropertiesSet() throws Exception {
		//this.webRoot = getWebRootPath();	
		if(H2_DRIVER.equals(driver)){
			if("H2 JDBC Driver".equals(jdbcTemplate.getDataSource().getConnection().getMetaData().getDriverName())){
				isH2db=true;
			}
		}
		mapFile=new HashMap<String, List<String>>();
		if(this.resourceFiles==null){
			return;
		}
		try {
			checkSysSqlTable();
			getMapApps();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("检测异常:", e1.getMessage());
			return;
		}
		// 解压文件
		for (Resource rs : this.resourceFiles) {
			String tpath = rs.getURI().toString();
			try {
				//System.out.println("file:"+tpath);
				if(!tpath.endsWith(SQL_ENDFIX)){
					continue;
				}
				
				String appName=tpath.replaceAll(".*?lib/(.*?)\\-[v]*[\\d]+\\.[\\d]+\\.[\\d]+\\-[\\w]+\\.jar.*", "$1");
				if(mapFile.containsKey(appName)){
					mapFile.get(appName).add(rs.getFilename());
				}else{
					List<String> files=new ArrayList<String>();
					files.add(rs.getFilename());
					mapFile.put(appName, files);
					if(!mapApps.containsKey(appName)){
						mapApps.put(appName, "v0.0");
					}
				}
				
				//将sql文件写入数据库
				saveFileSql(appName, rs.getFilename());
				
			} catch (Exception e) {
				logger.error("读取sql资源出错：" + tpath);
				logger.error("read *.sql Resource error  !", e);
			}
		}
		

		logger.info("自动更新启用状态为:"+this.startUpdateVersion);
		if("Y".equals(this.startUpdateVersion)){
			//启动时检查版本sql执行
			runUpdateSql();	
		}
	}
	
	private void getMapApps() throws Exception{
		mapApps =new HashMap<String, String>();
		int c = 0;
		if(!isH2db){
			c= JdbcUtils.getTableCount(jdbcTemplate, "sys_app_version");
		}else{
			c=JdbcUtils.getTableCountForH2(jdbcTemplate, "sys_app_version");
		}
		if(c>0){
			String sql="SELECT appName, version from sys_app_version  ;";
			List<Map<String, Object>> apps= jdbcTemplate.queryForList(sql);
			if(apps !=null && apps.size()>0){
				for(Map<String, Object> app : apps){
					String appName=app.get("appName").toString();
					String currentVersion=app.get("version").toString();
					mapApps.put(appName, currentVersion);
				}
			}else{
				throw new Exception("读取版本号失败...");
			}
		}
	}
	
	private void checkSysSqlTable() {
		if(!isH2db){
			StringBuffer sb=new StringBuffer();
			sb.append("CREATE TABLE IF NOT EXISTS `sys_app_sql` (");
			sb.append("		  `id` varchar(32) NOT NULL DEFAULT '' COMMENT '唯一hash',");
			sb.append("		  `appName` varchar(50) NOT NULL COMMENT '应用项目名称',");
			sb.append("		  `version` varchar(20) NOT NULL COMMENT '当前版本',");
			sb.append("		  `sqlText` longtext NOT NULL COMMENT 'sql脚本',");
			sb.append("		  `rollbackText` longtext COMMENT '反向sql脚本',");
			sb.append("		  `sqlError` longtext COMMENT '出错sql脚本',");
			sb.append("		  PRIMARY KEY (`id`)");
			sb.append("		) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用版本SQL管理表';");
			
			jdbcTemplate.execute(sb.toString());
			
			if(!JdbcUtils.existsColumns(jdbcTemplate, "sys_app_sql", "sqlError")){
				sb=new StringBuffer();
				sb.append("ALTER TABLE sys_app_sql ADD sqlError longtext COMMENT '出错sql脚本' ;");
				jdbcTemplate.execute(sb.toString());
			}
		}else{
			StringBuffer sb=new StringBuffer();
			sb.append("CREATE TABLE IF NOT EXISTS `sys_app_sql` (");
			sb.append("		  `id` varchar(32) NOT NULL DEFAULT '' COMMENT STRINGDECODE('唯一hash'),");
			sb.append("		  `appName` varchar(50) NOT NULL COMMENT STRINGDECODE('应用项目名称'),");
			sb.append("		   `version` varchar(20) NOT NULL COMMENT STRINGDECODE('当前版本'),");
			sb.append("		  `sqlText` longtext NOT NULL COMMENT STRINGDECODE('sql脚本'),");
			sb.append("		  `rollbackText` longtext COMMENT STRINGDECODE('反向sql脚本'),");
			sb.append("		  `sqlError` longtext COMMENT STRINGDECODE('出错sql脚本'),");
			sb.append("		  PRIMARY KEY (`id`)	) ");
			
			jdbcTemplate.execute(sb.toString());
			
			if(!JdbcUtils.existsColumnsForH2(jdbcTemplate, "sys_app_sql", "sqlError")){
				sb=new StringBuffer();
				sb.append("ALTER TABLE sys_app_sql ADD sqlError longtext COMMENT STRINGDECODE('出错sql脚本') ;");
				jdbcTemplate.execute(sb.toString());
			}
		}
	}
	
	private void saveFileSql(String appName, String fileName){
		//不存在则写入
		String version = fileName.replace(".sql", "");
		String hash=MD5Tools.MD5(appName+version);
		
		String sqlText= readFile(appName, fileName).replaceAll("-- [^(\\r\\n)]*?\r\n", "");
		sqlText=sqlText.replace("\\", "\\\\").replace("'", "\\'");
		if(isH2db){
			sqlText = Mysql2H2Util.sql2H2(sqlText);
		}
		String sql = "";
		if(!isH2db){
			sql = "INSERT IGNORE INTO sys_app_sql (id,appName,version,sqlText) values ('%s','%s','%s','%s') ;";
		}else{
			sql = "INSERT INTO sys_app_sql (id,appName,version,sqlText) values ('%s','%s','%s','%s') ON DUPLICATE KEY UPDATE  id = id";
		}
		sql = String.format(sql, hash, appName, version, sqlText);
		jdbcTemplate.execute(sql);
		
	}
	
	public void runUpdateSql(){
		//启动时检查版本sql执行
		logger.info("正在检查db自动升级...");
		try {
			//String sql="SELECT appName, version from sys_app_version  ;";
			//List<Map<String, Object>> apps= jdbcTemplate.queryForList(sql);
			if(mapApps==null || mapApps.size()==0){
				return;
			}
			
			//优先执行adp-sys
			String[] apps = new String[mapApps.size()];
			int a = 0;
			if (mapApps.containsKey(APP_SYS)) {
				apps[a] = APP_SYS;
				a++;
			}
			for (Entry<String, String> kv : mapApps.entrySet()) {
				if (APP_SYS.equals(kv.getKey())) {
					continue;
				}
				apps[a] = kv.getKey();
				a++;
			}
			
			
			for(String app : apps){
				String appName = app;
				String currentVersion = mapApps.get(app);
				/*if("adp-sys".equals(appName)){
					System.out.println("pass  "+appName +" update");
					continue;
				}*/
				
				List<String> newVersions = getNewVersions(appName, currentVersion);
				if(newVersions !=null && newVersions.size()>0){
//					if(APP_SYS.equals(appName)){
//						newVersions.add("menu");						
//					}
					for(String newVer : newVersions){
						long t1=System.currentTimeMillis();
						logger.info("runing "+appName +"  "+ newVer);					
						String sqlContent= readDbSqlText(appName,  newVer).trim();
						//去注释
						sqlContent=sqlContent.replaceAll("-- [^(\\r\\n)]*?\r\n", "");
						if(!StringUtils.isBlank(sqlContent)){
							queueError = new ConcurrentLinkedQueue<String>();
							//boolean result=JdbcUtils.executeBatchSqls(jdbcTemplate, sqlContent);
							boolean result = false;
							if(!isH2db){
								result=JdbcUtils.executeVersionSqls(jdbcTemplate, sqlContent, queueError);
							}else{
								result=JdbcUtils.executeVersionSqlsForH2(jdbcTemplate, sqlContent, queueError);
							}
							
							///boolean result=executeVersionSqls(jdbcTemplate, sqlContent);//多线程执行
							//保存出错的sql
							saveErrorSql(appName,  newVer);
							if(result && !"menu".equals(newVer)){
								//更新版本号
								String upVer="INSERT INTO sys_app_version (appName, version, uptime) values ('"+appName+"','"+newVer+"',now()) "
										+ " ON DUPLICATE KEY UPDATE version=VALUES(version),uptime=NOW();";
								logger.info("更新版本:"+upVer);
								jdbcTemplate.execute(upVer);
								//jdbcTemplate.execute("update sys_app_version set version='"+newVer+"', uptime=now() where appName='"+appName+"'; ");						
							}
							if(!result){
								logger.error("runing "+appName +"  "+ newVer+" 出错跳出升级...");
								break;
							}
						}
						logger.info("finish "+appName +"  "+ newVer+" time="+(System.currentTimeMillis()-t1));
					}
				}
				
			}
		} catch (Exception e) {
			logger.error("db 升级出错:", e);
			e.printStackTrace();
		}
		logger.info("完成检查db自动升级...");
	}
	
	
	public void onApplicationEvent(ApplicationEvent event) {
		/*if(!(event.getSource() instanceof XmlWebApplicationContext)){
			return;
		}
		if(((XmlWebApplicationContext)event.getSource()).getParent() != null){
			//root application context 没有parent，他就是老大.
	        //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
			return;
	    }*/
		
	}

	
	/**
	 * 读取资源文件内容
	 * @param rs
	 * @return
	 */
	public String readFile(Resource rs){
		String content=null;
		try {
			if(rs !=null){
				content = IOUtils.toString(rs.getInputStream(),"UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * 读取资源文件内容
	 * @param appName 模块名称
	 * @param fileNme 文件名
	 * @return
	 */
	public String readFile(String appName, String fileName){
		String content=null;
		try {
			for (Resource rs : this.resourceFiles) {
				String tpath = rs.getURI().toString();
				if(tpath.contains(appName) && tpath.contains(fileName)){
					content = IOUtils.toString(rs.getInputStream(),"UTF-8");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public String readDbSqlText(String appName, String version){
		String content=null;
		try {
			String sql="SELECT sqlText from sys_app_sql where appName='%s' and version='%s' LIMIT 1 ;";
			sql=String.format(sql, appName, version);
			
			content=jdbcTemplate.queryForObject(sql, String.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * 获取新版本
	 * @param appName 模块名称
	 * @param currentVersion 当前版本号
	 * @return
	 */
	public List<String> getNewVersions(String appName, String currentVersion){
		List<String> newVersions=new ArrayList<String>();
		List<String> allVersions = mapFile.get(appName);
		if(allVersions ==null){
			return newVersions;
		}
		for(String ver : allVersions){
			if("menu.sql".equals(ver)){
				continue;
			}
			ver=ver.replace(".sql", "");
			/*if(ver.compareTo(currentVersion)>0){
				newVersions.add(ver);
			}*/
			if(compareVer(ver, currentVersion)>0){
				newVersions.add(ver);
			}
		}
		//Collections.sort(newVersions);
		Collections.sort(newVersions, new Comparator<String>() {
		    public int compare(String s1, String s2) {
		        return compareVer(s1, s2);
		    }
		});
		
		return newVersions;
	}
	
	/**
	 * 获取所有版本
	 * @param appName 模块名称
	 * @return
	 */
	public List<String> getAllVersions(String appName){
		List<String> newVersions=new ArrayList<String>();
		List<String> allVersions = mapFile.get(appName);
		if(allVersions ==null){
			return newVersions;
		}
		for(String ver : allVersions){
			if("menu.sql".equals(ver)){
				continue;
			}
			newVersions.add(ver.replace(".sql", ""));
		}
		Collections.sort(newVersions, new Comparator<String>() {
		    public int compare(String s1, String s2) {
		        return compareVer(s1, s2);
		    }
		});
		
		return newVersions;
	}
	
	/**
	 * 版本比较
	 * @param ver1
	 * @param ver2
	 * @return
	 */
	private int compareVer(String ver1, String ver2){
		String[] _ver2=ver2.replace("v", "").split("\\.");
		int cv1=Integer.parseInt(_ver2[0]);
		int cv2=Integer.parseInt(_ver2[1]);
		
		String[] _ver1=ver1.replace("v", "").split("\\.");
		int v1=Integer.parseInt(_ver1[0]);
		int v2=Integer.parseInt(_ver1[1]);
		if(v1>cv1 || (v1==cv1 && v2>cv2)){
			return 1;
		}
		if(v1==cv1 && v2==cv2){
			return 0;
		}
		return -1;
	}
	
	/**
	 * 升级到指定版本(由低版本升级到高版本)
	 * @param appName 模板名称
	 * @param currentVersion 当前版本
	 * @param toVersion 指定版本
	 */
	public void updateToVersion(String appName, String currentVersion, String toVersion){
		List<String> newVersions = getNewVersions(appName, currentVersion);
		if(newVersions !=null){
			for(String newVer : newVersions){
				logger.info("runing "+appName +"  "+ newVer);
				String sqlContent= readFile(appName,  newVer+".sql").trim();
				if(!StringUtils.isBlank(sqlContent)){
					boolean result=JdbcUtils.executeBatchSqls(jdbcTemplate, sqlContent);
					if(result){
						//更新版本号
						String upVer="INSERT INTO sys_app_version (appName, version, uptime) values ('"+appName+"','"+newVer+"',now()) "
								+ " ON DUPLICATE KEY UPDATE version=VALUES(version),uptime=NOW();";
						jdbcTemplate.execute(upVer);						
					}
					if(!result || newVer.equals(toVersion)){
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 执行指定版本
	 * @param appName 模板名称
	 * @param currentVersion 当前版本
	 * @param toVersion 指定版本
	 */
	public void updateToVersion(String appName, String toVersion){
		logger.info("runing "+appName +"  "+ toVersion);
		String sqlContent= readFile(appName,  toVersion+".sql").trim();
		if(!StringUtils.isBlank(sqlContent)){
			boolean result=JdbcUtils.executeBatchSqls(jdbcTemplate, sqlContent);
			if(result){
				//更新版本号
				String upVer="INSERT INTO sys_app_version (appName, version, uptime) values ('"+appName+"','"+toVersion+"',now()) "
						+ " ON DUPLICATE KEY UPDATE version=VALUES(version),uptime=NOW();";
				jdbcTemplate.execute(upVer);						
			}
		}
	}
	
	/**
	 * 执行指定sql文件，不更新版本号
	 * @param appName 模块名称
	 * @param fileNme 文件名
	 * @return
	 */
	public boolean executeSqlFile(String appName, String fileNme){
		boolean result=false;
		try {
			String content=null;
			for (Resource rs : this.resourceFiles) {
				String tpath = rs.getURI().toString();
				if(tpath.contains(appName) && tpath.contains(fileNme)){
					content = IOUtils.toString(rs.getInputStream(),"UTF-8");
					break;
				}
			}
			if(!StringUtils.isBlank(content)){
				result=JdbcUtils.executeBatchSqls(jdbcTemplate, content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public boolean executeVersionSqls(JdbcTemplate jdbcTemplate, String sql){
		boolean result=false;
		
		try {
			String[] _sqls=sql.split(";[\\s]*(\\r\\n|\\n)");
			if(_sqls.length>0){
				List<String> tableSqls = new ArrayList<String>();
				List<String> dataSqls = new ArrayList<String>();
				for (String s : _sqls) {
					if (JdbcUtils.isDataSql(s)) {
						dataSqls.add(s);
					} else {
						tableSqls.add(s);
					}
				}
				//先执行修改表结构
				for(String _sql : tableSqls){
					try {
						jdbcTemplate.execute(_sql);
					} catch (Exception e) {
						logger.error("执行sql出错："+ _sql, e.getMessage());
						//记录到另一个字段
						queueError.add(_sql);
					}
				}				
				//再用多线程执行数据sql
				latch = new CountDownLatch(threadNum);
				MyThread[] ths = new MyThread[threadNum];
				int i = 0;
				for (String _sql : dataSqls) {
					_sql = _sql.trim();
					if (StringUtils.isBlank(_sql)) {
						continue;
					}
					if (ths[i] == null) {
						ths[i] = new MyThread();
					}
					ths[i].addSql(_sql);
					i++;
					if (i >= ths.length) {
						i = 0;
					}
				}
				for (MyThread th : ths) {
					if (th == null) {
						th = new MyThread();
					}
					Thread.sleep(2);
					th.start();
				}
				latch.await();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result=true;//出错也修改版本号
		return result;
	}
	
	public class MyThread extends Thread{		
		List<String> listSql=new ArrayList<String>();
				
		public MyThread(){
			
		}
		
		public void addSql(String sql){
			listSql.add(sql);
		}
		
		public void run() {
			for(String _sql : listSql){
				try {
					jdbcTemplate.execute(_sql);
				} catch (Exception e) {
					logger.error("执行sql出错："+ _sql, e.getMessage());
					//记录到另一个字段
					queueError.add(_sql);
				}
			}
			latch.countDown(); 
		}
	}
	
	

	private void saveErrorSql(String appName, String version){
		StringBuffer sbError=new StringBuffer("");
		if(!queueError.isEmpty()){
			Iterator<String> iter = queueError.iterator();
			while(iter.hasNext()){
				sbError.append(iter.next()+ ";\r\n");
			}
		}
		String sqlError=sbError.toString().replace("\\", "\\\\").replace("'", "\\'");
		
		String sql=String.format(" update sys_app_sql set sqlError='%s' where appName='%s' and version='%s' ; ", sqlError, appName, version);
		if(isH2db){
			sql=Mysql2H2Util.sql2H2(String.format(" update sys_app_sql set sqlError='%s' where appName='%s' and version='%s' ; ", sqlError, appName, version));
		}
		jdbcTemplate.execute(sql);
	}
	
	
	

}

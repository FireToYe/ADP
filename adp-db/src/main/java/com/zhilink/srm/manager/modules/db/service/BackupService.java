package com.zhilink.srm.manager.modules.db.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.zhilink.manager.framework.common.utils.StringUtils;


public class BackupService {
	
	protected static Logger logger = LoggerFactory.getLogger(BackupService.class);
	
	private JdbcTemplate jdbcTemplate;
	/**
	 * 程序中用到的其它变量
	 */
	private static String osName = System.getProperty("os.name");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
	
	private String username ; // 连接数据库的用户名
	private String password ; // 连接数据库的密码
	private String database; // 备份数据库的名字
	private String host = "localhost"; // 连接的主机名
	private String port = "3306"; // 数据库的接口
	private String charsetName = "utf8"; // 数据库的字符集，默认为utf8
	private String mysqlPath = ""; // mysql的安装目录，配置环境变量的情况下可以不设置值
	private String exportPath = ""; // 数据库备份的输出路径
		
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getMysqlPath() {
		return mysqlPath;
	}

	public void setMysqlPath(String mysqlPath) {
		this.mysqlPath = mysqlPath;
	}
	
	public String getExportPath() {
		return exportPath;
	}

	public void setExportPath(String exportPath) {
		if(exportPath==null || "".equals(exportPath)){
			exportPath="/opt/db_backup/";
			if(System.getProperty("os.name").toLowerCase().contains("windows")){
				exportPath="D:"+exportPath;
			}
		}
		this.exportPath = exportPath;
	}
	
		
	
	
	public BackupService(){
		
	}
	
	/**
	 * 初始化属性
	 */
	public void init(){
		try {
//			String pName = getADPHome() + File.separator + "conf" + File.separator + "adp.properties";
//			Properties properties = new Properties();
//			File pFile = new File(pName);
//			if (pFile.exists()) {
//				// throw new RuntimeException("not find properties file :" + pName);
//				try {
//					properties.load(new FileInputStream(pFile));
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			Connection conn=jdbcTemplate.getDataSource().getConnection();
			String connUrl= conn.getMetaData().getURL();
//			this.username = properties.getProperty("jdbc.username");
//			this.password = properties.getProperty("jdbc.password");
			//jdbc:mysql://172.31.75.126:3306/adp?useUnicode=true&characterEncoding=utf-8
			this.database= connUrl.replaceAll(".*?[\\d]+/([\\w\\_]+)\\?.*", "$1");
			this.host=connUrl.replaceAll(".*?//([\\d]+\\.[\\d]+\\.[\\d]+\\.[\\d]+)\\:[\\d]+/.*", "$1");
			this.port=connUrl.replaceAll(".*?//[\\d]+\\.[\\d]+\\.[\\d]+\\.[\\d]+\\:([\\d]+)/.*", "$1");
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 执行备份和保存
	 * @param fileName 导出文件名，不带后缀, 不传时自动以日期命名
	 * @param tables 要备份的表，不传则表示全库备份
	 */
	public void doBackUpAndSave(String fileName, String tables) {
		//创建文件夹与文件
		File file = new java.io.File(exportPath);
		if (!file.exists()) {
			file.mkdirs();
		} else {
			if (!file.isDirectory()) {
				file.delete();
				file.mkdirs();
			}
		}		
		fileName += ".sql";
		File exportFile = new File(exportPath + fileName);
		if (exportFile.exists()) {
			exportFile.delete();
		}
		try {
			exportFile.createNewFile();
		} catch (IOException e1) {
			logger.error("MySQL数据库进行备份，" + exportPath + fileName + "文件创建失败！");
		}
		
		// 判断平台是因为在linux和window下的命令不同，window下存在mysqlPath时，不加cmd /c会出错
		String mysqldump = "";
		if (osName.toLowerCase().startsWith("windows")) {
			mysqldump = "cmd /c \"" + mysqlPath + "mysqldump\"";
		} else {
			mysqldump = mysqlPath + "mysqldump";
		}
		StringBuffer sbCommand=new StringBuffer();
		sbCommand.append(mysqldump) // mysql安装路径 \"作用为：防止路径中包含空格,单在linux中不能出现"作用的命令名，否则将不承认是个命令
			.append(" -u").append(username) // 用户名
			.append(" -p").append(password) // 密码
			.append(" -h").append(host) // 主机
			.append(" -P").append(port) // 端口号
			.append(" --default-character-set=").append(charsetName)// 字符集
			.append(" -B ").append(database); // 数据库名称
		if(null != tables && !"".equals(tables)){
			sbCommand.append(" --tables ").append(tables); // 数据表 多个用空格分开
		}
		sbCommand.append(" -r").append(exportPath + fileName); // 导出路径
				
		logger.info("\nMySQL数据库进行备份：" + "\n\tuser:" + username + "\n\tpassword:" + password + "\n\thost:" + host
				+ "\n\tport:" + port + "\n\tdatabase:" + database + "\n\texportPath:" + exportPath);
		
		try {
			exeCommand(sbCommand.toString());
			doSave(fileName);
		} catch (IOException e) {
			logger.error("MySQL数据库进行备份，备份命令执行错误！" + e.getMessage());
		}
	}
	
	/**
	 * 执行全库备份
	 * @param fileName 导出文件名，不带后缀, 不传时自动以日期命名
	 */
	public void doBackUpAll(String fileName) {
		doBackUpAndSave(fileName, null);
	}
	/**
	 * 执行全库备份
	 * @param fileName 导出文件名，不带后缀, 不传时自动以日期命名
	 */
	public void doBackUpAll() {
		String fileName=dateFormat.format(new Date());
		doBackUpAll(fileName);
	}
	
	/**
	 * 执行选表备份
	 * @param fileName 导出文件名，不带后缀, 不传时自动以日期命名
	 */
	public void doBackUpTables(String fileName, String... tables) {
		String strTables= StringUtils.join(tables, " ");
		doBackUpAndSave(fileName, strTables);
	}
	/**
	 * 执行选表备份
	 * @param fileName 导出文件名，不带后缀, 不传时自动以日期命名
	 */
	public void doBackUpTables(String... tables) {
		String fileName=dateFormat.format(new Date());
		doBackUpTables(fileName, tables);
	}

	/**
	 * 执行命令行命令
	 * @param command
	 * @return
	 * @throws IOException
	 */
	private void exeCommand(String command) throws IOException {
		logger.info("MySQL数据库正执行命令：" + command);
		Runtime runtime = Runtime.getRuntime();
		Process exec = runtime.exec(command);
		try {
			exec.waitFor();
		} catch (InterruptedException e) {
			logger.error("MySQL数据库执行命令出错："+e.getMessage(),e);
		}
	}

	/**
	 * 将数据库输出的文件，保存为压缩文件，并将sql文件删除
	 */
	private void doSave(String fileName) throws IOException {

		File exportFile = new File(exportPath + fileName);
		FileInputStream inputStream = new FileInputStream(exportFile);
		String fileStr = exportPath + fileName.substring(0, fileName.lastIndexOf("."));
		if (osName.startsWith("Windows")) {
			fileStr = fileStr + ".zip";
			logger.info("MySQL数据库进行备份，备份路径为：" + fileStr);
			try {
				saveAsZip(inputStream, fileStr, fileName);
			} catch (Exception e) {
				logger.error("MySQL数据库进行备份，生成备份文件失败！");
			}
		} else {
			fileStr = fileStr + ".gz";
			logger.info("MySQL数据库进行备份，备份路径为：" + fileStr);
			try {
				saveAsGZip(inputStream, fileStr);
			} catch (Exception e) {
				logger.error("MySQL数据库进行备份，生成备份文件失败！");
			}
		}
		inputStream.close();
		exportFile.delete();
	}
		
	/**
	 * 还原
	 * @param fileName 文件名带后缀
	 */
	public void doRestore(String fileName){
		String sqlFile=fileName;
		
		//解压文件
		if(fileName.endsWith(".zip")){
			sqlFile=sqlFile.replace(".zip", ".sql");
			try {
				unZip(fileName);
			} catch (IOException e) {
				logger.error("解压zip备份文件"+fileName+"出错！");
			}
		}
		if(fileName.endsWith(".gz")){
			sqlFile=sqlFile.replace(".gz", ".sql");
			try {
				unGz(fileName, sqlFile);
			} catch (IOException e) {
				logger.error("解压gz备份文件"+fileName+"出错！");
			}
		}
		
		//还原脚本 判断平台是因为在linux和window下的命令不同，window下存在mysqlPath时，不加cmd /c会出错
		String mysqldump = "";
		if (osName.toLowerCase().startsWith("windows")) {
			mysqldump = "cmd /c \"" + mysqlPath + "mysql\"";
		} else {
			mysqldump = mysqlPath + "mysql";
		}
		StringBuffer sbCommand=new StringBuffer();
		sbCommand.append(mysqldump) // mysql安装路径 \"作用为：防止路径中包含空格,单在linux中不能出现"作用的命令名，否则将不承认是个命令
			.append(" -u").append(username) // 用户名
			.append(" -p").append(password) // 密码
			.append(" -h").append(host) // 主机
			.append(" -P").append(port) // 端口号
			.append(" -B ").append(database) // 数据库名称
			.append(" < ").append(exportPath+sqlFile); // 文件名
						
		logger.info("\nMySQL数据库进行还原：" + "\n\tuser:" + username + "\n\tpassword:" + password + "\n\thost:" + host
				+ "\n\tport:" + port + "\n\tdatabase:" + database + "\n\texportPath:" + exportPath+"\n\tfile:"+sqlFile);
		
		try {
			exeCommand(sbCommand.toString());
		} catch (IOException e) {
			logger.error("MySQL数据库进行还原，还原命令执行错误！" + e.getMessage());
		}
		
	}

	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 将输入流保存为Zip文件格式，包含sql文件
	 * @param inputStream
	 * @param fileStr
	 * @throws IOException
	 */
	private void saveAsZip(FileInputStream inputStream, String fileStr, String fileName) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(fileStr);
		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
		byte[] b = new byte[1024];
		int length = 0;
		zipOutputStream.putNextEntry(new ZipEntry(fileName));
		while ((length = inputStream.read(b)) != -1) {
			zipOutputStream.write(b, 0, length);
		}
		zipOutputStream.closeEntry();
		zipOutputStream.finish();
		zipOutputStream.flush();
		zipOutputStream.close();

		fileOutputStream.close();
	}

	/**
	 * 将输入流保存为GZip文件
	 * @param inputStream
	 * @param fileStr
	 * @throws IOException
	 */
	private void saveAsGZip(FileInputStream inputStream, String fileStr) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(fileStr);
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
		byte[] b = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(b)) != -1) {
			gzipOutputStream.write(b, 0, length);
		}
		gzipOutputStream.finish();
		gzipOutputStream.flush();
		gzipOutputStream.close();

		fileOutputStream.close();
	}

	/**
	 * zip解压
	 * @param zipPath
	 * @throws IOException
	 */
	private void unZip(String zipPath) throws IOException {
		ZipInputStream Zin = new ZipInputStream(new FileInputStream(exportPath+zipPath));
		BufferedInputStream Bin = new BufferedInputStream(Zin);
		String Parent = exportPath; // 输出路径（文件夹目录）
		File Fout = null;
		ZipEntry entry;
		while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
			Fout = new File(Parent, entry.getName());
			if (!Fout.exists()) {
				(new File(Fout.getParent())).mkdirs();
			}
			FileOutputStream out = new FileOutputStream(Fout);
			BufferedOutputStream Bout = new BufferedOutputStream(out);
			int b;
			while ((b = Bin.read()) != -1) {
				Bout.write(b);
			}
			Bout.close();
			out.close();
		}
		Bin.close();
		Zin.close();
	}
	
	/**
	 * gz解压
	 * @param gzPath
	 * @param ouputfile
	 * @throws IOException
	 */
	private void unGz(String gzPath, String ouputfile) throws IOException {
		try {
			FileInputStream fin = new FileInputStream(exportPath + gzPath);
			GZIPInputStream gzin = new GZIPInputStream(fin);
			File f=new File(exportPath);
			if(!f.exists()){
				f.mkdirs();
			}
			/*f=new File(exportPath+ouputfile);
			if(f.exists()){
				f.delete();
			}*/
			FileOutputStream fout = new FileOutputStream(exportPath + ouputfile);
			int num;
			byte[] buf = new byte[1024];
			while ((num = gzin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, num);
			}
			gzin.close();
			fout.close();
			fin.close();
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
	}
	
	
	
	/**
	 * 获取数据表
	 * @return
	 */
	public List<String> getTables(){
		List<String> list=new ArrayList<String>();
		try {
			Connection conn=jdbcTemplate.getDataSource().getConnection();
			//String connUrl= conn.getMetaData().getURL();
			//String db=connUrl.replaceAll(".*?[\\d]+/([\\w\\_]+)\\?.*", "$1");
			String tableNamePattern = null;
			ResultSet rs  = conn.getMetaData().getTables(null, this.database, tableNamePattern, new String[]{"TABLE"});
			while(rs.next()){
				list.add(rs.getString("TABLE_NAME"));
				System.out.println(rs.getString("TABLE_NAME"));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 读取备份记录文件
	 * @return
	 */
	public List<String> getBackupRecord(){
		List<String> list=new ArrayList<String>();
		File file=new File(exportPath);
		if(file.exists()){
			File[] fileList = file.listFiles();
			Arrays.sort(fileList,new Comparator<File>() {
	
				@Override
				public int compare(File o1, File o2) {
					// TODO Auto-generated method stub
					long diff = o2.lastModified()-o1.lastModified();
					if(diff>0){
						return 1;
					}
					if(diff<0){
						return -1;
					}
					return 0;
				}
				
			});
			for(File f : fileList){
				list.add(f.getName());
				System.out.println(f.getName());
			}
		}
		return list;
	}
	
	public void deleteByName(String fileName,int count){
		File file=new File(exportPath);
		if(file.exists()){
			File[] fileList = file.listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					return pathname.getName().startsWith(fileName+"_");
				}
			});
			if(fileList!=null&&fileList.length>count){
				Arrays.sort(fileList,new Comparator<File>() {
	
					@Override
					public int compare(File o1, File o2) {
						// TODO Auto-generated method stub
						long diff = o2.lastModified()-o1.lastModified();
						if(diff>0){
							return 1;
						}
						if(diff<0){
							return -1;
						}
						return 0;
					}
				});
				for(int i=count-1;i<fileList.length;i++){
					fileList[i].delete();
				}
			}
		}
	}
}

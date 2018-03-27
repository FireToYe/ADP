package com.zhilink.srm.common.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zhilink.manager.framework.common.utils.StringUtils;

public class JdbcUtils {
	
	private static Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
	
	/*public static boolean executeSqlFile(JdbcTemplate jdbcTemplate, String sqlFilePath){
		boolean result=false;
		
		try {
			Resource resource = new ClassPathResource(sqlFilePath);
			String sql = IOUtils.toString(resource.getInputStream(),"UTF-8");
			String[] _sqls=sql.split(";[\\s]*(\\r\\n|\\n)");
			if(_sqls.length>0){
				Connection conn=jdbcTemplate.getDataSource().getConnection();
				conn.setAutoCommit(false);
				try {
					for(String _sql : _sqls){
						_sql=_sql.trim();
						if(_sql.length()>0){
							jdbcTemplate.execute(_sql);
						}
					}
					conn.commit();
					result=true;
				} catch (Exception e) {
					e.printStackTrace();
					conn.rollback();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}*/
	
	/*public static boolean executeSqls(JdbcTemplate jdbcTemplate, String sql){
		boolean result=false;
		
		try {
			String[] _sqls=sql.split(";[\\s]*(\\r\\n|\\n)");
			if(_sqls.length>0){
				Connection conn=jdbcTemplate.getDataSource().getConnection();
				conn.setAutoCommit(false);
				try {
					for(String _sql : _sqls){
						_sql=_sql.trim();
						if(_sql.length()>0){
							jdbcTemplate.execute(_sql);
						}
					}
					conn.commit();
					result=true;
				} catch (Exception e) {
					e.printStackTrace();
					conn.rollback();
				}finally{
					conn.setAutoCommit(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}*/
	
	public static boolean executeVersionSqls(JdbcTemplate jdbcTemplate, String sql, ConcurrentLinkedQueue<String> queueError){
		boolean result=false;
		
		try {
			String[] _sqls=sql.split(";[\\s]*(\\r\\n|\\n)");
			if(_sqls.length>0){
				for(String _sql : _sqls){
					_sql=_sql.trim();
					if(!StringUtils.isBlank(_sql)){
						try {
							jdbcTemplate.execute(_sql);
						} catch (Exception e) {
							logger.error("执行sql出错："+ _sql, e.getMessage());
							//记录到另一个字段
							queueError.add(_sql);
						}						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result=true;//出错也修改版本号
		return result;
	}
	
	public static boolean executeVersionSqlsForH2(JdbcTemplate jdbcTemplate, String sql, ConcurrentLinkedQueue<String> queueError){
		boolean result=false;
		
		try {
			String[] _sqls=sql.split(";[\\s]*(\\r\\n|\\n)");
			if(_sqls.length>0){
				for(String _sql : _sqls){
					_sql=_sql.trim();
					if(!StringUtils.isBlank(_sql)){
						try {
							_sql =Mysql2H2Util. executeSql2H2(_sql);
							jdbcTemplate.execute(_sql);
						} catch (Exception e) {
							logger.error("执行sql出错："+ _sql, e.getMessage());
							//记录到另一个字段
							queueError.add(_sql);
						}						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result=true;//出错也修改版本号
		return result;
	}
	//批量执行
	public static boolean executeBatchSqls(JdbcTemplate jdbcTemplate, String sql){
		boolean result=false;
		
		try {
			String[] _sqls=sql.split(";[\\s]*(\\r\\n|\\n)");
			//分离 数据变化与表结构变化
			List<String> tableSqls=new ArrayList<String>();
			List<String> dataSqls=new ArrayList<String>();
			for(String s : _sqls){
				if(isDataSql(s)){
					dataSqls.add(s);
				}else{
					tableSqls.add(s);
				}
			}
			
			result= doExecuteBatch(jdbcTemplate, tableSqls);
			if(result){
				result= doExecuteBatch(jdbcTemplate, dataSqls);
			}
			
		} catch (Exception e) {
			logger.error("执行sql出错：", e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static boolean isDataSql(String sql){
		
		String regex="(?is)^[\\s]*(INSERT|UPDATE|DELETE)[\\s]+";
		Matcher match= Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(sql);
		if(match.find()){
			return true;
		}
		return false;
	}
	
	public static boolean doExecuteBatch(JdbcTemplate jdbcTemplate, List<String> _sqls) throws SQLException{
		boolean result=false;
		if(_sqls.size()==0){
			return true;
		}
		
		Connection conn=jdbcTemplate.getDataSource().getConnection();
		conn.setAutoCommit(false);
		Statement st=conn.createStatement();
		try {
			for(String _sql : _sqls){
				if(checkAddColumns(jdbcTemplate, _sql)){
					continue;
				}
				if(!StringUtils.isBlank(_sql.trim())){
					st.addBatch(_sql);
				}
			}
			st.executeBatch();
			conn.commit();
			result=true;
		} catch (Exception e) {
			logger.error("批量执行sql出错：", e);
			conn.rollback();
			e.printStackTrace();
		}finally{
			conn.setAutoCommit(true);
		}
		
		return result;
	}
	
	
	public static int getTableCount(JdbcTemplate jdbcTemplate, String name){
		int c=0;
		
		try {
			Connection conn=jdbcTemplate.getDataSource().getConnection();
			
			
			/*String sql="SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA='"+conn.getSchema()+"' and TABLE_NAME like '"+tableName+"%';";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				c++;
			}
			rs.close();
            stmt.close();*/
			
			String connUrl= conn.getMetaData().getURL();
			String db=connUrl.replaceAll(".*?[\\d]+/([\\w\\_]+)\\?.*", "$1");
			ResultSet rs  = conn.getMetaData().getTables(null, db,  null, new String[]{"TABLE"} );			
			while(rs.next()){
				String tableName=rs.getString("TABLE_NAME");
				if(tableName.startsWith(name)){
					c++;
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return c;
	}
	
	public static int getTableCountForH2(JdbcTemplate jdbcTemplate, String name){
		int c=0;
		
		try {
			String sql = "SELECT count(1) FROM information_schema.TABLES WHERE table_name ='xxxxx'".replace("xxxxx", name);
			c = jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return c;
	}
	/**
	 * 检查添加字段时数据库是否已存在
	 * @param jdbcTemplate
	 * @param _sql
	 * @return true 存在
	 */
	private static boolean checkAddColumns(JdbcTemplate jdbcTemplate, String _sql){
		boolean bResult=false;
		
		try {
			String regex="(?is)ALTER[\\s]+TABLE[\\s]+([\\w\\_`]+)[\\s]+ADD[\\s]+([\\w\\_`]+)[\\s]+";
			Matcher match= Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(_sql);
			if(match.find()){
				String tableName=match.group(1).replace("`", "").trim();
				String columName=match.group(2).replace("`", "").trim();
				
				/*Connection conn=jdbcTemplate.getDataSource().getConnection();
				String connUrl= conn.getMetaData().getURL();
				String db=connUrl.replaceAll(".*?[\\d]+/([\\w\\_]+)\\?.*", "$1");
				ResultSet rs  = conn.getMetaData().getColumns(null, db, tableName, columName);			
				while(rs.next()){
					bResult=true;
				}
				rs.close();*/
				
				bResult = existsColumns(jdbcTemplate, tableName, columName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bResult;
	}
	
	/**
	 * 检查数据表是否存在某字段
	 * @param jdbcTemplate
	 * @param tableName
	 * @param columName
	 * @return
	 */
	public static boolean existsColumns(JdbcTemplate jdbcTemplate, String tableName, String columName){
		boolean bResult=false;
		
		try {
						
			Connection conn=jdbcTemplate.getDataSource().getConnection();
			String connUrl= conn.getMetaData().getURL();
			String db=connUrl.replaceAll(".*?[\\d]+/([\\w\\_]+)\\?.*", "$1");
			ResultSet rs  = conn.getMetaData().getColumns(null, db, tableName, columName);			
			while(rs.next()){
				bResult=true;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bResult;
	}
	
	public static boolean existsColumnsForH2(JdbcTemplate jdbcTemplate, String tableName, String columName){
		boolean bResult = false;
		String sql = "SELECT count(1) FROM information_schema.TABLES WHERE table_name ='xxxxx'".replace("xxxxx", tableName);
		int count = jdbcTemplate.queryForInt(sql);
		if(count>0){
			bResult =  true;
		}
		return bResult;
	}
	

}

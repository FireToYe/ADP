package com.zhilink.srm.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mysql2H2Util {

	public static String sql2H2(String msql) {
		String sql = transSql(msql);
		return sql;
	}
	public static String executeSql2H2(String msql) {
		if(msql.trim().contains("DELETE")||(msql.trim().contains("delete"))){
			msql = msql.replaceAll("\"", "'");
		}
		if(msql.trim().startsWith("/*")){
			msql =  msql.replaceAll("(?<!:)\\/\\/.*|\\/\\*(\\s|.)*?\\*\\/", "");
		}
		if(msql.contains("utf8mb4 COLLATE utf8mb4_general_ci")){
			msql = msql.replaceAll("utf8mb4 COLLATE utf8mb4_general_ci", "");
		}
		if(msql.trim().contains("ALTER")||msql.trim().contains("alter")){
			if(msql.contains("AFTER")){
				msql = msql.substring(0,msql.indexOf("AFTER"));
			}
		}
		String sql = transSql(msql);
		String lowerSql = sql.toLowerCase();
		StringBuilder sb = new StringBuilder();
		if (lowerSql.startsWith("alter")) {
			if (count(lowerSql, "add ") > 1) {
				String first = lowerSql.substring(0,
						lowerSql.indexOf("add ") + 4);
				String secend = lowerSql.substring(lowerSql.indexOf("add ") + 4,
						lowerSql.length());
				String lastString = secend.replaceAll("add ", " ");
				if(lastString.contains(";")){
					lastString = lastString.replace(";", ");");
				}else{
					lastString = lastString+")";
				}
				sb.append(first).append(" (");
				sb.append(lastString);
				return sb.toString();
			}
		}
		if(sql.contains("IGNORE")){
			sql.replaceAll("IGNORE", "");
			if(sql.contains(";")){
				sql = sql.replace(";", " ON DUPLICATE KEY  update id = id;");
			}else{
				sql = sql+" ON DUPLICATE KEY  update id = id;";
			}
		}
		return sql.replaceAll("IGNORE", "").replaceAll("IGNORE".toLowerCase(), "").replaceAll("DEFAULT NULL DEFAULT '0'","DEFAULT '0'").replaceAll("utf8mb4", "");
	}
	public static int count(String text, String sub) {
		int count = 0, start = 0;
		while ((start = text.indexOf(sub, start)) >= 0) {
			start += sub.length();
			count++;
			if (count > 1) {
				return count;
			}
		}
		return count;
	}

	private static String transSql(String content) {

		content = content.replace("'0000-00-00 00:00:00'",
				"'0001-01-01 00:00:00'");

		content = content.replaceAll("COLLATE utf8mb4_bin", " ");

		content = content.replaceAll("ENGINE=InnoDB.*?;", ";");

		content = content.replace("\\\"", "\"");

		content = content.replaceAll("CHARACTER SET .*?", " ");

		content = content.replace("\\'", "''");

		content = content.replaceAll(
				",\\r\\n.*?KEY `.*?` \\(`.*?`\\) USING BTREE", " ");

		Pattern p = Pattern.compile("0x(.*?),");
		Matcher m = p.matcher(content);
		while (m.find()) {
			String hexStr = m.group(1);
			String str = hexStringToString(hexStr);
			str = str.replace("'", "''");
			str = str.replaceAll("\r\n", "\\\\r\\\\n");
			str = str.replaceAll("\n", "\\\\n");
			content = content.replace("0x" + hexStr + ",", "'" + str + "',");
		}
		content = StringDecode(content);
		return content;
	}

	public static String hexStringToString(String s) {
		if ((s == null) || (s.equals(""))) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = ((byte) (0xFF & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "UTF8");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	private static String StringDecode(String sql) {
		Pattern p = Pattern.compile("'");
		Matcher m = p.matcher(sql);
		int index = 0;
		int beginIndex = index;
		int endIndex = index;
		List<String> valueList = new ArrayList();
		int tempIndex;
		while (m.find()) {
			index++;
			if (index % 2 != 0) {
				beginIndex = m.end();
			} else {
				endIndex = m.start();

				String fieldValue = sql.substring(beginIndex, endIndex);
				if (!fieldValue.equals("")) {
					char pre = sql.charAt(endIndex - 1);
					char next = '\000';
					if (endIndex + 1 < sql.length()) {
						next = sql.charAt(endIndex + 1);
					}
					if (next == '\'') {
						index--;
					} else {
						if ((pre == '\\') || (pre == '\'')) {
							tempIndex = endIndex - 2;
							char temp = sql.charAt(tempIndex);
							int count = 1;
							while (((temp == '\\') || (temp == '\''))
									&& (tempIndex > beginIndex)) {
								count++;
								tempIndex--;
								temp = sql.charAt(tempIndex);
							}
							if (count % 2 == 1) {
								index--;
								continue;
							}
						}
						if (!fieldValue.matches("[\\w-\\s'\\.\\:]+")) {
							valueList.add(fieldValue);
						}
					}
				}
			}
		}
		StringBuilder sqlDest = new StringBuilder();
		StringBuilder sqlSrc = new StringBuilder(sql);
		for (String fieldValue : valueList) {
			// System.out.println(fieldValue);
			fieldValue = "'" + fieldValue + "'";

			int indexField = sqlSrc.indexOf(fieldValue);
			sqlDest.append(sqlSrc.substring(0, indexField) + "STRINGDECODE("
					+ fieldValue + ")");
			sqlSrc.delete(0, indexField + fieldValue.length());
		}
		sqlDest.append(sqlSrc);
		return sqlDest.toString();
	}
}

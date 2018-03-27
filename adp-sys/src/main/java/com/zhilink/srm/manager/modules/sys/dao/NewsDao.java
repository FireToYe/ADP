/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.News;

/**
 * 新闻通告DAO接口
 * @author an48huf
 * @version 2017-11-07
 */
@MyBatisDao
public interface NewsDao extends CrudDao<News> {
	//查询某个用户的未读通知的数量
	public int notifyNum(Map map);
	public int count(News news);
}
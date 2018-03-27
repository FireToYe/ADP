/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.manager.common.utils.FileUtils;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.sys.dao.NewsDao;
import com.zhilink.srm.manager.modules.sys.entity.News;

/**
 * 新闻通告Service
 * 
 * @author an48huf
 * @version 2017-11-07
 */
@Service
@Transactional(readOnly = true)
public class NewsService extends CrudService<NewsDao, News> {

	@Autowired
	private NewsDao newsDao;

	public News get(String id) {
		return super.get(id);
	}

	public List<News> findList(News news) {
		return super.findList(news);
	}

	public Page<News> findPage(Page<News> page, News news) {
		return super.findPage(page, news);
	}

	@Transactional(readOnly = false)
	public void save(News news) {
		// 附件ID，添加附件名称
		if (!StringUtils.isBlank(news.getAttachmentId())) {
			String[] paths = news.getAttachmentId().split("\\|");
			List<String> fileNames = new ArrayList<String>();
			File file = null;
			for (String path : paths) {
				if (path.length() == 0) {
					fileNames.add("");
				} else {
					file = new File(path);
					fileNames.add(file.getName());
				}
			}
			news.setAttachmentName(StringUtils.join(fileNames, "|"));
		}
		if (StringUtils.isBlank(news.getId())) {
			news.setTypeId("1");// 默认类型1
			news.setTop("0");//
			news.preInsert();
			newsDao.insert(news);
		} else {
			news.preUpdate();
			newsDao.update(news);
		}
	}

	@Transactional(readOnly = false)
	public void delete(News news) {
		super.delete(news);
	}
	public int notifyNum(Map map){
		return newsDao.notifyNum(map);
	}
	//点击查看消息时更新消息的状态
		@Transactional(readOnly = false)
		public void updateStatus(News news){
			newsDao.update(news);
		}

}
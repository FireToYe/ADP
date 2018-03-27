/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.TreeDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * @author jaray
 * 
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
	public int selectLevelCompanyCount();
	
	public int judgeExist(@Param("code") String code);
	
	public List<Office> findSupplier(Office office);
	
	//新增机构和修改机构时判断是否存在相同的数据
	public int isExistSameCode(@Param("code") String code,@Param("id")String id);
}

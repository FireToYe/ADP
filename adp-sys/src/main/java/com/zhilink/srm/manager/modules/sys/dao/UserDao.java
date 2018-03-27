/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.Role;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.enums.UserTypeEnum;

/**
 * 用户DAO接口
 * 
 * @author jaray
 * 
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {

	/**
	 * 根据登录名称查询用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);

	/**
	 * 查询全部用户数目
	 * 
	 * @return
	 */
	public long findAllCount(User user);

	/**
	 * 更新用户密码
	 * 
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);

	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * 
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * 
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);

	/**
	 * 插入用户角色关联数据
	 * 
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);

	/**
	 * 获取erp的员工编号不为空的用户
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user);

	/**
	 * 获取当前用户的角色
	 * 
	 * @param id
	 * @return
	 */
	public List<String> findtUserRole(String id);

	/**
	 * 根据角色找到对应的用户
	 * 
	 * @param roleId
	 * @return
	 */
	public List findUserByRole(String[] roleId);

	/**
	 * 查找所有的用户id
	 * 
	 * @return
	 */
	public List findAllUserId(char delFlag);

	/**
	 * 根据officeid查找所有的用户id
	 * 
	 * @param id
	 * @return
	 */
	public List findUserIdByOfficeId(@Param("officeID") String officeID, @Param("companyID") String companyID,
			@Param("delFlag") char delFalg);

	/**
	 * 根据用户id查询所有的用户名
	 * 
	 * @param userIds
	 * @return
	 */
	public String getNameById(String[] userIds);

	
	/**
	 * 根据组织架构ID获取所有关联(当前组织和所有子组织)的用户
	 * @param parentOfficeId
	 * @return
	 */
	public List<User> findUserByParentOfficeId(@Param("officeId") String parentOfficeId,@Param("from") int from,@Param("to") int to,@Param("includeUserTypes")List<String> includeUserTypes);
	
	public int countByParentOfficeId(@Param("officeId") String parentOfficeId,@Param("includeUserTypes")List<String> includeUserTypes);
	
	/**
	 * 更新语言
	 * @param user
	 * @param lang
	 */
	public void updateLangById(@Param("id")String userId,@Param("lang")String lang);


}

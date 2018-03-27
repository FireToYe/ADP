/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.Role;
import com.zhilink.srm.manager.modules.sys.entity.User;

/**
 * 角色DAO接口
 * @author jaray
 * 
 */
@MyBatisDao
public interface RoleDao extends CrudDao<Role> {

	public Role getByName(Role role);
	
	public Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);
	/**
	 * 删除角色关联的角色用户中间表
	 * @param role
	 * @return
	 */
	public int deleteUserRole(Role role);
	
	public void updateById(Map<String, String> map);
	public int insertRoleMenu(Role role);
	
	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	public int deleteRoleOffice(Role role);

	public int insertRoleOffice(Role role);
	
	/**
	 * 获取组织角色,某一个组织下的所有角色和所有公共角色，其中有当前组织下的角色、当前组织下的所有孩子组织的角色、公共角色
	 * @param orgId
	 * @return
	 */
	public List<Role> findRoleListForOrg(@Param("orgId") String orgId);
	/***
	 * 获取已经使用过指定角色的用户列表,删除角色前校验
	 * @param roleId 角色ID
	 * @return
	 */
	public List<User> findUserUsedRole(@Param("roleId") String roleId);
	
	/**
	 * 查询引用了角色的用户数
	 * @param roleId
	 * @return
	 */
	public int countRoleIfUsed(@Param("roleId") String roleId);
	
	public List<Role> findRoleName(User user);

}

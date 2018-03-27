package com.zhilink.sys.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhilink.srm.manager.modules.sys.dao.MenuDao;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.sys.manager.dao.NewMenuDao;
import com.zhilink.sys.manager.entity.NewMenu;

@Service
public class Init implements InitializingBean {

	private Log logger = LogFactory.getLog(getClass());
	@Autowired
	private SystemService systemService;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private NewMenuDao newNenuDao;

	private void setMenuNewId() {

		NewMenu rootMenu = null;
		List<NewMenu> rootMenus = newNenuDao.findByParentId("0");
		if (rootMenus != null && !rootMenus.isEmpty()) {
			rootMenu = rootMenus.get(0);
		}
		// 一级菜单
		List<NewMenu> lev1Menus = newNenuDao.findByParentId(rootMenu.getId());
		if (lev1Menus != null && !lev1Menus.isEmpty()) {

			createMenuIds(lev1Menus, 4);

			for (NewMenu lev1 : lev1Menus) {
				List<NewMenu> lev2Menus = newNenuDao.findByParentId(lev1.getId());
				createMenuIds(lev2Menus, 4);
			}
		}
	}

	private void createMenuIds(List<NewMenu> menus, int parts) {
		if (menus != null && !menus.isEmpty()) {
			int size = menus.size();
			for (int k = 0; k < size; k++) {
				NewMenu newMenu = new NewMenu();
				NewMenu menu = menus.get(k);
				newMenu.setId(menu.getId());
				String newMenuId = menu.getParent().getNewId() + StringUtils.leftPad("" + (k + 1), parts, "0");
				newMenu.setNewId(newMenuId);
				newMenu.setNewParentId(menu.getParent().getNewId() );

				if ("1".equals(menu.getParentId())) {
					newMenu.setNewParentIds("0,1,");
				} else {
					newMenu.setNewParentIds(menu.getParent().getNewParentIds() + menu.getParent().getNewId() + ",");
				}

				System.err.println("id=" + newMenuId + ",parentId=" + menu.getParent().getId());

				newNenuDao.updateNewId(newMenu.getId(), newMenu.getNewId(), newMenu.getNewParentId(),
						newMenu.getNewParentIds());

				List<NewMenu> levMenus = newNenuDao.findByParentId(menu.getId());
				if (levMenus != null && !levMenus.isEmpty()) {
					createMenuIds(levMenus, 4);
				}

			}
		}

	}

	public void afterPropertiesSet() throws Exception {

		logger.info("初始化开始…………");
		setMenuNewId();
		
		//List<NewMenu> rootMenus = newNenuDao.findByParentId("0");
		logger.info("初始化结束");

	}

	public static void main(String[] args) {
		String leftPad = StringUtils.leftPad("1", 3, "0");

		System.err.println(leftPad);
	}

}

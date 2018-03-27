/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Menu;
import com.zhilink.srm.manager.modules.sys.entity.Office;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.enums.LanguageEnum;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 菜单Controller
 * 
 * @author jaray
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends BaseController {

	@Autowired
	private SystemService systemService;

	@ModelAttribute("menu")
	public Menu get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			Menu menu = systemService.getMenu(id);
			User user = UserUtils.getUser();
			if (user != null) {
				getI18nMenuName(menu, user);
			}
			return menu;
		} else {
			return new Menu();
		}
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = { "list", "" })
	public String list(Model model) {
		List<Menu> list = Lists.newArrayList();
		List<Menu> sourcelist = systemService.findAllMenu();
		Menu.sortList(list, sourcelist, Menu.getRootId(), true);
		model.addAttribute("list", list);
		return "modules/sys/menuList";
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "form")
	public String form(Menu menu, Model model) {
		if (menu.getParent() == null || menu.getParent().getId() == null) {
			menu.setParent(new Menu(Menu.getRootId()));
		}
		// 获取排序号，最末节点排序号+30
		if (StringUtils.isBlank(menu.getId())) {
			List<Menu> list = Lists.newArrayList();
			List<Menu> sourcelist = systemService.findAllMenu();
			Menu.sortList(list, sourcelist, menu.getParentId(), false);
			if (list.size() > 0) {
				menu.setSort(list.get(list.size() - 1).getSort() + 30);
			}
		}
		model.addAttribute("menu", menu);
		return "modules/sys/menuForm";
	}

	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "save")
	public String save(Menu menu, Model model, RedirectAttributes redirectAttributes) {

		User user = UserUtils.getUser();
		if (!user.isAdmin()) {
			String i18nTip = I18n.i18nMessage("menu.overPowerTip");
			addMessage(redirectAttributes, i18nTip);
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/menu/";
		}
		if (!beanValidator(model, menu)) {
			return form(menu, model);
		}
		// 转换国际化语言菜单名称
		convertI18nMenuName(menu, user);
		systemService.saveMenu(menu);
		String i18nTip = I18n.i18nMessage("menu.saveMenuSuccessfulTip");
		String[] params = { menu.getName() };
		String content = MessageFormat.format(i18nTip, params);
		addMessage(redirectAttributes, content);
		return "redirect:" + adminPath + "/sys/menu/";
	}

	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "delete")
	public String delete(Menu menu, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/menu/";
		}
		// if (Menu.isRoot(id)){
		// addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
		// }else{
		systemService.deleteMenu(menu);
		String i18nTip = I18n.i18nMessage("menu.deleteMenuSuccessfulTip");
		addMessage(redirectAttributes, i18nTip);
		// }
		return "redirect:" + adminPath + "/sys/menu/";
	}

	@RequiresPermissions("user")
	@RequestMapping(value = "tree")
	public String tree() {
		return "modules/sys/menuTree";
	}

	@RequiresPermissions("user")
	@RequestMapping(value = "treeselect")
	public String treeselect(String parentId, Model model) {
		model.addAttribute("parentId", parentId);
		return "modules/sys/menuTreeselect";
	}

	/**
	 * 批量修改菜单排序
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/menu/";
		}
		for (int i = 0; i < ids.length; i++) {
			Menu menu = new Menu(ids[i]);
			menu.setSort(sorts[i]);
			systemService.updateMenuSort(menu);
		}
		String i18nTip = I18n.i18nMessage("menu.saveMenuOrderSuccessfulTip");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:" + adminPath + "/sys/menu/";
	}

	/**
	 * isShowHide是否显示隐藏菜单
	 * 
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = systemService.findAllMenu();
		for (int i = 0; i < list.size(); i++) {
			Menu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				if (isShowHide != null && isShowHide.equals("0") && e.getIsShow().equals("0")) {
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 构造菜单的属性结构的json
	 * 
	 * @param extId
	 * @param isShowHide
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeJSONData")
	public List<Map<String, Object>> treeJSONData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> menuList = systemService.findAllMenu();
		List<Menu> returnMenus = Lists.newArrayList();
		if (menuList != null && menuList.size() > 0) {
			for (Menu menu : menuList) {
				if (menu.getMenuType() == 0) {
					returnMenus.add(menu);
				}
			}
		}
		for (int i = 0; i < returnMenus.size(); i++) {
			Menu e = returnMenus.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				if (isShowHide != null && isShowHide.equals("0") && e.getIsShow().equals("0")) {
					continue;
				}
				if ("0".equals(e.getParentId())) {
					JSONObject map = new JSONObject();
					map.put("id", e.getId());
					map.put("pId", e.getParentId());
					map.put("text", e.getName());
					mapList.add(map);
					fillTreeData(returnMenus, map, extId, isShowHide);
				}
			}
		}
		return mapList;
	}

	private void convertI18nMenuName(Menu menu, User user) {
		// 名称转对应的国际化
		if (user.getLanguage() != null) {
			if (LanguageEnum.US.getCode().equals(user.getLanguage())) {
				menu.setNameEnUS(menu.getName());
			} else if (LanguageEnum.SIMPLIFIED_CHINESE.getCode().equals(user.getLanguage())) {
				menu.setNameZhCN(menu.getName());
			} else if (LanguageEnum.TRADITIONAL_CHINESE.getCode().equals(user.getLanguage())) {
				menu.setNameZhTW(menu.getName());
			} else {
				menu.setNameZhCN(menu.getName());
			}
		}
	}

	private void getI18nMenuName(Menu menu, User user) {
		// 名称转对应的国际化
		if (user.getLanguage() != null) {
			if (LanguageEnum.US.getCode().equals(user.getLanguage())) {
				if (StringUtils.isNotBlank(menu.getNameEnUS())) {
					menu.setName(menu.getNameEnUS());
				}
			} else if (LanguageEnum.SIMPLIFIED_CHINESE.getCode().equals(user.getLanguage())) {
				if (StringUtils.isNotBlank(menu.getNameZhCN())) {
					menu.setName(menu.getNameZhCN());
				}
			} else if (LanguageEnum.TRADITIONAL_CHINESE.getCode().equals(user.getLanguage())) {
				if (StringUtils.isNotBlank(menu.getNameZhTW())) {
					menu.setName(menu.getNameZhTW());
				}
			} else {
				menu.setName(menu.getNameZhCN());
			}
			if (menu.getParent() != null) {
				if (LanguageEnum.US.getCode().equals(user.getLanguage())) {
					if (StringUtils.isNotBlank(menu.getParent().getNameEnUS())) {
						menu.getParent().setName(menu.getParent().getNameEnUS());
					}
				} else if (LanguageEnum.SIMPLIFIED_CHINESE.getCode().equals(user.getLanguage())) {
					if (StringUtils.isNotBlank(menu.getParent().getNameZhCN())) {
						menu.getParent().setName(menu.getParent().getNameZhCN());
					}
				} else if (LanguageEnum.TRADITIONAL_CHINESE.getCode().equals(user.getLanguage())) {
					if (StringUtils.isNotBlank(menu.getParent().getNameZhTW())) {
						menu.getParent().setName(menu.getParent().getNameZhTW());
					}
				} else {
					menu.getParent().setName(menu.getParent().getName());
				}
			}
		}
	}

	private void fillTreeData(List<Menu> list, JSONObject map, String extId, String isShowHide) {
		Object id = null;
		List<JSONObject> children = null;
		for (int i = 0; i < list.size(); i++) {
			Menu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				if (isShowHide != null && isShowHide.equals("0") && e.getIsShow().equals("0")) {
					continue;
				}
				id = map.get("id");
				if (id != null && id.toString().equals(e.getParentId())) {
					if (children == null) {
						children = new ArrayList<JSONObject>();
					}
					JSONObject child = new JSONObject();
					child.put("id", e.getId());
					child.put("pId", e.getParentId());
					child.put("text", e.getName());
					children.add(child);
					fillTreeData(list, child, extId, isShowHide);
				}
			}
		}
		if (children != null) {
			map.put("nodes", children);
		}
	}
}

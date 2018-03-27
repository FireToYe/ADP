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
import com.zhilink.manager.framework.common.security.Digests;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Area;
import com.zhilink.srm.manager.modules.sys.service.AreaService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 区域Controller
 * 
 * @author jaray
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;

	@ModelAttribute("area")
	public Area get(@RequestParam(required = false) String id) {
		Digests dig = null;
		if (StringUtils.isNotBlank(id)) {
			return areaService.get(id);
		} else {
			return new Area();
		}
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = { "list", "" })
	public String list(Area area, Model model) {
		model.addAttribute("list", areaService.findAll());
		return "modules/sys/areaList";
	}

	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		if (area.getParent() == null || area.getParent().getId() == null) {
			area.setParent(UserUtils.getUser().getOffice().getArea());
		}else{
			area.setParent(areaService.get(area.getParent().getId()));
		}
		// // 自动获取排序号
		// if (StringUtils.isBlank(area.getId())){
		// int size = 0;
		// List<Area> list = areaService.findAll();
		// for (int i=0; i<list.size(); i++){
		// Area e = list.get(i);
		// if (e.getParent()!=null && e.getParent().getId()!=null
		// && e.getParent().getId().equals(area.getParent().getId())){
		// size++;
		// }
		// }
		// area.setCode(area.getParent().getCode() +
		// StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
		// }
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}

	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	public String save(Area area, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area";
		}
		if (!beanValidator(model, area)) {
			return form(area, model);
		}
		areaService.save(area);
		String i18nTip = I18n.i18nMessage("area.saveAreaSuccessfulTip");
		
		String[] params = {area.getName()};
		String content = MessageFormat.format(i18nTip, params);
		addMessage(redirectAttributes, content);
		return "redirect:" + adminPath + "/sys/area/";
	}

	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	public String delete(Area area, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area";
		}
		// if (Area.isRoot(id)){
		// addMessage(redirectAttributes, "删除区域失败, 不允许删除顶级区域或编号为空");
		// }else{
		areaService.delete(area);
		String i18nTip = I18n.i18nMessage("area.deleteAreaFailerTip");
		addMessage(redirectAttributes, i18nTip);
		// }
		return "redirect:" + adminPath + "/sys/area/";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeDataList")
	public List<Map<String, Object>> treeDataList(@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findAll();
		for (int i = 0; i < list.size(); i++) {
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
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
	 * 返回树结构
	 * 
	 * @param extId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<JSONObject> treeData(@RequestParam(required = false) String extId, HttpServletResponse response) {
		List<JSONObject> mapList = new ArrayList<JSONObject>();
		List<Area> list = areaService.findAll();
		for (int i = 0; i < list.size(); i++) {
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				if ("0".equals(e.getParentId())) {
					JSONObject map = new JSONObject();
					map.put("id", e.getId());
					map.put("pId", e.getParentId());
					map.put("text", e.getName());
					mapList.add(map);
					fillTreeData(list, map, extId);
				}
			}
		}
		return mapList;
	}

	// 构造树
	private void fillTreeData(List<Area> list, JSONObject map, String extId) {

		Object id = null;
		List<JSONObject> children = null;
		for (int i = 0; i < list.size(); i++) {
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {

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
					fillTreeData(list, child, extId);
				}
			}
		}
		if (children != null) {
			map.put("nodes", children);
		}
	}
}

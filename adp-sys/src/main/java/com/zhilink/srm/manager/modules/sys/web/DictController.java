/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Dict;
import com.zhilink.srm.manager.modules.sys.enums.LanguageEnum;
import com.zhilink.srm.manager.modules.sys.service.DictService;

/**
 * 字典Controller 支持动态国际化语言显示、保存
 * 
 * @author jaray
 * @author an48huf
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;

	@ModelAttribute
	public Dict get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return dictService.get(id);
		} else {
			return new Dict();
		}
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = { "list", "" })
	public String list(Dict dict, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<String> typeList = dictService.findTypeList();
		model.addAttribute("typeList", typeList);
		Page<Dict> page = dictService.findPage(new Page<Dict>(request, response), dict);
		// 字典支持国际化显示
		if (page != null && page.getList() != null) {
			String currentLanguage = I18n.getCurrentLanguage();
			page.getList().stream().forEach(d -> showI18nDictLabel(currentLanguage, d));
		}
		model.addAttribute("page", page);
		return "modules/sys/dictList";
	}

	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict dict, Model model) {
		// 支持国际化显示
		showI18nDictLabel(I18n.getCurrentLanguage(), dict);
		model.addAttribute("dict", dict);
		return "modules/sys/dictForm";
	}

	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "save") // @Valid
	public String save(Dict dict, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/dict/?repage&type=" + dict.getType();
		}
		if (!beanValidator(model, dict)) {
			return form(dict, model);
		}
		// 根据当前用户的语言动态赋值字段
		setI18nDictLabel(I18n.getCurrentLanguage(), dict);
		dictService.save(dict);
		String i18nTip = I18n.i18nMessage("dict.saveSuccessful");
		String[] params = {dict.getLabel()};
		String content = MessageFormat.format(i18nTip, params);
		addMessage(redirectAttributes, content);
		return "redirect:" + adminPath + "/sys/dict/?repage&type=" + dict.getType();
	}

	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	public String delete(Dict dict, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/dict/?repage";
		}
		dictService.delete(dict);
		String i18nTip = I18n.i18nMessage("dict.deleteSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:" + adminPath + "/sys/dict/?repage&type=" + dict.getType();
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String type,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Dict dict = new Dict();
		dict.setType(type);
		List<Dict> list = dictService.findList(dict);
		String currentLanguage = I18n.getCurrentLanguage();
		for (int i = 0; i < list.size(); i++) {
			Dict e = list.get(i);
			// 根据当前用户显示国际化label名称
			showI18nDictLabel(currentLanguage, e);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}

	@ResponseBody
	@RequestMapping(value = "listData")
	public List<Dict> listData(@RequestParam(required = false) String type) {
		Dict dict = new Dict();
		dict.setType(type);
		List<Dict> findList = dictService.findList(dict);
		// 字典支持国际化显示
		if (findList != null && findList.size() > 0) {
			String currentLanguage = I18n.getCurrentLanguage();
			findList.stream().forEach(d -> showI18nDictLabel(currentLanguage, d));
		}
		return findList;
	}

	/**
	 * 展示当前语言的label名称
	 * 
	 * @param areaCode
	 * @param dict
	 */
	private void showI18nDictLabel(String areaCode, Dict dict) {

		String currentLanguage = I18n.getCurrentLanguage();
		if (LanguageEnum.SIMPLIFIED_CHINESE.getCode().equals(currentLanguage)) {
			if (StringUtils.isNotBlank(dict.getLabelZhCN())) {
				dict.setLabel(dict.getLabelZhCN());
			}
		} else if (LanguageEnum.TRADITIONAL_CHINESE.getCode().equals(currentLanguage)) {
			if (StringUtils.isNotBlank(dict.getLabelZhTW())) {
				dict.setLabel(dict.getLabelZhTW());
			}
		} else if (LanguageEnum.US.getCode().equals(currentLanguage)) {
			if (StringUtils.isNotBlank(dict.getLabelEnUS())) {
				dict.setLabel(dict.getLabelEnUS());
			}
		}
	}

	/**
	 * 动态赋值当前语言的label名称
	 * 
	 * @param areaCode
	 * @param dict
	 */
	private void setI18nDictLabel(String areaCode, Dict dict) {

		String currentLanguage = I18n.getCurrentLanguage();
		if (LanguageEnum.SIMPLIFIED_CHINESE.getCode().equals(currentLanguage)) {
			dict.setLabelZhCN(dict.getLabel());
		} else if (LanguageEnum.TRADITIONAL_CHINESE.getCode().equals(currentLanguage)) {
			dict.setLabelZhTW(dict.getLabel());
		} else if (LanguageEnum.US.getCode().equals(currentLanguage)) {
			dict.setLabelEnUS(dict.getLabel());
		}
	}

}

/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.exception.CommonException;
import com.zhilink.srm.common.utils.excel.ExportExcel;
import com.zhilink.srm.common.utils.excel.ImportExcel;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Area;
import com.zhilink.srm.manager.modules.sys.entity.Office;
import com.zhilink.srm.manager.modules.sys.service.OfficeService;
import com.zhilink.srm.manager.modules.sys.utils.DictUtils;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

import de.schlichtherle.license.LicenseContent;

/**
 * 机构Controller
 * 
 * @author jaray
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;

	@ModelAttribute("office")
	public Office get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return officeService.get(id);
		} else {
			return new Office();
		}
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "" })
	public String index(Office office, Model model) {
		// model.addAttribute("list", officeService.findAll());
		return "modules/sys/officeIndex";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "list" })
	public String list(Office office, Model model) {
		List<Office> findList = Lists.newArrayList();
		List<Office> children = officeService.findList(office);
		if (office != null && StringUtils.isNoneEmpty(office.getId())) {
			findList.add(office);
		}
		if (children != null && children.size() > 0) {
			findList.addAll(children);
		}
		model.addAttribute("list", findList);
		return "modules/sys/officeList";
	}

	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "messageOfficeList" })
	public String messageOfficeList(Office office, Model model) {
		index(office, model);
		List<Office> findList = Lists.newArrayList();
		List<Office> children = officeService.findList(office);
		if (office != null && StringUtils.isNoneEmpty(office.getId())) {
			findList.add(office);
		}
		if (children != null && children.size() > 0) {
			findList.addAll(children);
		}
		model.addAttribute("list", findList);
		return "modules/sys/messageOfficeList";
	}
	
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {

		office.setParent(officeService.get(office.getParent().getId()));
		if (office.getArea() == null && office.getParent() != null) {
			office.setArea(office.getParent().getArea());
		}
		// 自动获取排序号
		if (StringUtils.isBlank(office.getId()) && office.getParent() != null
				&& !"".equals(office.getParent().getId())) {
			int size = 0;
			List<Office> list = officeService.findAll();
			for (int i = 0; i < list.size(); i++) {
				Office e = list.get(i);
				if (e.getParent() != null && e.getParent().getId() != null
						&& e.getParent().getId().equals(office.getParent().getId())) {
					size++;
				}
			}
			office.setCode(((office.getParent().getCode() == null) ? "" : office.getParent().getCode())
					+ StringUtils.leftPad(String.valueOf(size > 0 ? size + 1 : 1), 3, "0"));
		}
		model.addAttribute("office", office);
		return "modules/sys/officeForm";
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/office/";
		}
		if (!beanValidator(model, office)) {
			return form(office, model);
		}
		if (office != null && StringUtils.isBlank(office.getId())) {
			LicenseContent licenseContent = Global.getLicenseContent();
			if (licenseContent != null && licenseContent.getAmount() > 0) {
				int maxAmount = licenseContent.getConsumerAmount();// 最大可添加架构数量
				int exCount = officeService.selectLevelCompanyCount();
				if ((exCount + 1) > maxAmount) {
					String i18nTip = I18n.i18nMessage("office.maxAllowNumTip");
					int[] params = {maxAmount};
					String content = MessageFormat.format(i18nTip, params);
					addMessage(redirectAttributes, content);
					return "redirect:" + adminPath + "/sys/office/list";
				}
			}
		}
		officeService.save(office);

		if (office.getChildDeptList() != null) {
			Office childOffice = null;
			for (String id : office.getChildDeptList()) {
				childOffice = new Office();
				childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
				childOffice.setParent(office);
				childOffice.setArea(office.getArea());
				childOffice.setType("2");
				childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade()) + 1));
				childOffice.setUseable(Global.YES);
				officeService.save(childOffice);
			}
		}

		String i18nTip = I18n.i18nMessage("office.saveOfficeSuccessfulTip");
		String[] params = {office.getName()};
		String content = MessageFormat.format(i18nTip, params);
		addMessage(redirectAttributes, content);
		String id = "0".equals(office.getParentId()) ? "" : office.getParentId();
		return "redirect:" + adminPath + "/sys/office/list?id=" + id + "&parentIds=" + office.getParentIds();
	}

	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(Office office, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/office/list";
		}
		// if (Office.isRoot(id)){
		// addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
		// }else{
		officeService.delete(office);
		String i18nTip = I18n.i18nMessage("office.deleteSuccessfulTip");
		addMessage(redirectAttributes, i18nTip);
		// }
		String id = "0".equals(office.getParentId()) ? "" : office.getParentId();
		return "redirect:" + adminPath + "/sys/office/list?id=" + id + "&parentIds="
				+ (office.getParentIds() == null ? "" : office.getParentIds());
	}

	/**
	 * 获取机构JSON数据。
	 * 
	 * @param extId
	 *            排除的ID
	 * @param type
	 *            类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade
	 *            显示级别
	 * @param response
	 * @param companyid
	 *            已经选中的公司的id
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeDataList")
	public List<Map<String, Object>> treeDataList(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
			@RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findList(isAll);
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if ((StringUtils.isBlank(extId)
					|| (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				if (type != null && "3".equals(type)) {
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

	// StringUtils.isNotBlank(companyid) && companyid != null &&
	// companyid.equals(e.getId())
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<JSONObject> treeData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
			@RequestParam(required = false) Boolean isAll, @RequestParam(required = false) String companyid,
			HttpServletResponse response) {
		List<JSONObject> mapList = new ArrayList<JSONObject>();
		List<Office> list = officeService.findList(isAll);
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if ((StringUtils.isBlank(extId)
					|| (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())) {
				// 一级组织架构
				JSONObject map = new JSONObject();
				if (StringUtils.isNotBlank(companyid) && companyid != null && type.equals("2")) {
					if (companyid.equals(e.getId())) {
						map.put("type", e.getType());// 机构类型
						map.put("id", e.getId());
						map.put("pId", e.getParentId());
						map.put("pIds", e.getParentIds());
						map.put("text", e.getName());
						if (type != null && "3".equals(type)) {
							map.put("isParent", true);
						}
						mapList.add(map);
						fillTreeData(list, map, extId, type, grade);
						return mapList;
					}
				} else if ("0".equals(e.getParentId())) {
					map.put("type", e.getType());// 机构类型
					map.put("id", e.getId());
					map.put("pId", e.getParentId());
					map.put("pIds", e.getParentIds());
					map.put("text", e.getName());
					if (type != null && "3".equals(type)) {
						map.put("isParent", true);
					}
					mapList.add(map);
					fillTreeData(list, map, extId, type, grade);
				}

			}
		}
		return mapList;
	}

	// 构造树
	private void fillTreeData(List<Office> list, JSONObject map, String extId, String type, Long grade) {

		Object id = null;
		List<JSONObject> children = null;
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if ((StringUtils.isBlank(extId)
					|| (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					&& Global.YES.equals(e.getUseable())) {

				id = map.get("id");
				if (id != null && id.toString().equals(e.getParentId())) {
					if (children == null) {
						children = new ArrayList<JSONObject>();
					}
					JSONObject child = new JSONObject();
					child.put("type", e.getType());// 机构类型
					child.put("id", e.getId());
					child.put("pId", e.getParentId());
					child.put("pIds", e.getParentIds());
					child.put("text", e.getName());
					if (type != null && "3".equals(type)) {
						child.put("isParent", true);
					}
					children.add(child);
					fillTreeData(list, child, extId, type, grade);
				}
			}
		}
		if (children != null) {
			map.put("nodes", children);
		}
	}
	
	
	
	/**
	 * 导入机构数据，只导入一级机构
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		
		String redirectPath = "redirect:" + adminPath + "/sys/office/list?id=&parentIds=";
		try {	
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Office> officeList = ei.getDataList(Office.class);
			if(officeList == null || officeList.size() == 0) {
				addMessage(redirectAttributes, I18n.i18nMessage("office.importDataNotNull"));
				return redirectPath;
			}
			
			//检验licenses数量
			int officeSize = officeList.size();
			LicenseContent licenseContent = Global.getLicenseContent();
			if (licenseContent != null && licenseContent.getAmount() > 0) {
				int maxAmount = licenseContent.getConsumerAmount();// 最大可添加架构数量
				int exCount = officeService.selectLevelCompanyCount();
				if ((exCount + officeSize) > maxAmount) {
					String i18nTip = I18n.i18nMessage("office.maxAllowNumTip");
					Object[] params = {maxAmount};
					String content = MessageFormat.format(i18nTip, params);
					addMessage(redirectAttributes, content);
					return redirectPath;
				}
			}
			
			Set<String> CodeSet = Sets.newHashSet();
			//验证机构编码唯一且不能为空
			for (Office office : officeList) {
				String code = office.getCode();
				if(StringUtils.isBlank(office.getName()) || StringUtils.isBlank(code)) {
					addMessage(redirectAttributes, I18n.i18nMessage("office.importNameOrCodeNull"));
					return redirectPath;
				}
				
				//数据库验证机构编码唯一
				int count = officeService.judgeExist(code);
				
				if(count > 0) {
					String i18nTip = I18n.i18nMessage("office.importCodeExist");
					Object[] params = {code};
					String content = MessageFormat.format(i18nTip, params);
					addMessage(redirectAttributes, content);
					return redirectPath;
				}
				CodeSet.add(code);
				
				//设置基础数据
				office.setType("1");
				office.setGrade("1");
				office.setUseable("1");
				
				Area area = new Area();
				area.setId("1");
				office.setArea(area);
			}
			
			//判断excel中的记录是否存在code一样的
			if(CodeSet.size() != officeList.size()) {
				addMessage(redirectAttributes, I18n.i18nMessage("office.importExcelCodeExist"));
				return redirectPath;
			}
			
			//数据校验后开始保存
			officeService.importSave(officeList);
			
			String i18nTip = I18n.i18nMessage("office.importSuccess");
			Object[] params = {officeList.size()};
			String content = MessageFormat.format(i18nTip, params);
			addMessage(redirectAttributes, content);
		} catch (Exception e) {
			String i18nTip = I18n.i18nMessage("user.importUserFailer");
			Object[] params = {e.getMessage()};
			String content = MessageFormat.format(i18nTip, params);
			addMessage(redirectAttributes, content);
		}
		return redirectPath;
	}

	/**
	 * 下载导入机构数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "/import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("office.importOfficeTemplate")+".xlsx";
			List<Office> list = Lists.newArrayList();
			list.add(UserUtils.getUser().getCompany());
			new ExportExcel(I18n.i18nMessage("office.importOfficeData"), Office.class, 2).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			String i18nTip = I18n.i18nMessage("user.importFailer");
			Object[] params = {e.getMessage()};
			String content = MessageFormat.format(i18nTip, params);
			addMessage(redirectAttributes, content);
		}
		return "redirect:" + adminPath + "/sys/office?repage";
	}
	
	/**
	 * 机构编码唯一性校验
	 * @param code
	 */
	@RequestMapping(value = "isExistCode")
	@ResponseBody
	public int isExistCode(String code,String id){
		int count = officeService.isExistSameCode(code, id);
		return count;
	}
	
	@RequestMapping("findSupplier")
	@ResponseBody
	public ResultModel findSupplier(Office office){
		ResultModel resultModel = new ResultModel();
		try {
			List<Office> list = officeService.findSupplier(office);
			resultModel.setBody(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(e.getMessage());
		}
		return resultModel;
	}
}

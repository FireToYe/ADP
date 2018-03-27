/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.mapper.JsonMapper;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.manager.framework.common.utils.SpringContextHolder;
import com.zhilink.srm.manager.modules.sys.dao.DictDao;
import com.zhilink.srm.manager.modules.sys.entity.Dict;
import com.zhilink.srm.manager.modules.sys.enums.LanguageEnum;

/**
 * 字典工具类
 * 
 * @author jaray
 * @author an48huf
 */
public class DictUtils {

	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);

	public static final String CACHE_DICT_MAP = "dictMap";

	public static String getDictLabel(String value, String type, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
			String currentLanguage = I18n.getCurrentLanguage();
			for (Dict dict : getDictList(type)) {
				if (type.equals(dict.getType()) && value.equals(dict.getValue())) {
					// label支持国际化展示
					showI18nDictLabel(currentLanguage, dict);
					if (StringUtils.isNoneBlank(dict.getLabel())) {
						return dict.getLabel();
					}
				}
			}
		}
		return defaultValue;
	}

	public static String getDictLabels(String values, String type, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)) {
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")) {
				valueList.add(getDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
			for (Dict dict : getDictList(type)) {
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())) {
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}

	public static List<Dict> getDictList(String type) {
		@SuppressWarnings("unchecked")
		Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>) CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap == null) {
			dictMap = Maps.newHashMap();
			for (Dict dict : dictDao.findAllList(new Dict())) {
				List<Dict> dictList = dictMap.get(dict.getType());
				if (dictList != null) {
					dictList.add(dict);
				} else {
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<Dict> dictList = dictMap.get(type);

		if (dictList == null) {
			dictList = Lists.newArrayList();
		} else {
			// 字典支持国际化显示
			String currentLanguage = I18n.getCurrentLanguage();
			dictList.stream().forEach(d -> showI18nDictLabel(currentLanguage, d));

		}
		return dictList;
	}

	/**
	 * 返回字典列表（JSON）
	 * 
	 * @param type
	 * @return
	 */
	public static String getDictListJson(String type) {
		return JsonMapper.toJsonString(getDictList(type));
	}

	/**
	 * 展示当前语言的label名称
	 * 
	 * @param areaCode
	 * @param dict
	 */
	private static void showI18nDictLabel(String areaCode, Dict dict) {

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
}

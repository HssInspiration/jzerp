/**
 * Copyright &copy; 2015-2020 <a href="http://www.dingchang.co/">ccpt-dckj</a> All rights reserved.
 */
package co.dc.ccpt.modules.sys.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import co.dc.ccpt.common.utils.CacheUtils;
import co.dc.ccpt.common.utils.SpringContextHolder;
import co.dc.ccpt.modules.sys.entity.DictType;
import co.dc.ccpt.modules.sys.entity.DictValue;
import co.dc.ccpt.modules.sys.service.DictTypeService;

/**
 * 字典工具类
 * 
 * @author dckj
 * @version 2016-5-29
 */
public class DictUtils {

	private static DictTypeService dictTypeService = SpringContextHolder.getBean(DictTypeService.class);

	public static final String CACHE_DICT_MAP = "dictMap";

	/**
	 * 
	 * @param value
	 * @param type
	 * @param defaultLabel
	 * @return
	 */
	public static String getDictLabel(String value, String type, String defaultLabel) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
			for (DictValue dictValue : getDictList(type)) {
				if (value.equals(dictValue.getValue())) {
					return dictValue.getLabel();
				}
			}
		}
		return defaultLabel;
	}

	/**
	 * 
	 * @param values
	 * @param type
	 * @param defaultValue
	 * @return
	 */
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

	/**
	 * 
	 * @param label
	 * @param type
	 * @param defaultLabel
	 * @return
	 */
	public static String getDictValue(String label, String type, String defaultLabel) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
			for (DictValue dictValue : getDictList(type)) {
				if (label.equals(dictValue.getLabel())) {
					return dictValue.getValue();
				}
			}
		}
		return defaultLabel;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static List<DictValue> getDictList(String type) {
		// 通过字典类型从缓存中获取字典表数据集合
		@SuppressWarnings("unchecked")
		Map<String, List<DictValue>> dictMap = (Map<String, List<DictValue>>) CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap == null) {
			dictMap = Maps.newHashMap();
			// 查询所有字典集合并遍历
			for (DictType dictType : dictTypeService.findList(new DictType())) {
				// 通过类型获取对应的字典值集合
				List<DictValue> dictList = dictMap.get(dictType.getType());
				// 通过字典类型id查询对应的字典类型对象
				dictType = dictTypeService.get(dictType.getId());
				// 如果字典值集合非空，将字典值集合添加到字典集合中去
				if (dictList != null) {
					dictList.addAll(dictType.getDictValueList());
				} else {// 如果字典值集合为空那就将通过字典id查询出的字典值集合添加到字典集合中
					dictMap.put(dictType.getType(), Lists.newArrayList(dictType.getDictValueList()));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		// 通过字典类型获取字典值集合
		List<DictValue> dictList = dictMap.get(type);
		if (dictList == null) {
			dictList = Lists.newArrayList();
		}
		return dictList;
	}

	/**
	 * 反射根据对象和属性名获取属性值
	 * 
	 * @param obj
	 * @param filed
	 * @return
	 */
	public static Object getValue(Object obj, String filed) {
		try {
			Class clazz = obj.getClass();
			PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);
			Method getMethod = pd.getReadMethod();// 获得get方法

			if (pd != null) {

				Object o = getMethod.invoke(obj);// 执行get方法返回一个Object
				return o;

			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}
}

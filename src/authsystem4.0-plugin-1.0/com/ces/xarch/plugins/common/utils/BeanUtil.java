package com.ces.xarch.plugins.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ces.xarch.plugins.common.annotation.ExportIgnore;
import com.ces.xarch.plugins.common.annotation.MapIgnore;

/**
 * 
 * bean工具类的扩展.
 * <p>
 * 描述:对框架提供的bean工具类的扩展
 * </p>
 * <p>
 * Company:上海中信信息发展股份有限公司
 * </p>
 * 
 * @author 方俊新(fang.junxin@cesgroup.com.cn)
 * @date 2015-3-19 13:14:34
 * @version 1.0.2015.0319
 */
public class BeanUtil {

	public static <T> Map<String, T> bean2Map(Object target) {
		return bean2Map(target, false);
	}

	public static <T> List<Map<String, T>> bean2MapKey2Up(List targets) {
		List<Map<String, T>> list = new ArrayList<Map<String, T>>();
		for (Object target : targets) {
			Map<String, T> map = bean2Map(target);
			list.add(transKey(map));
		}
		return list;
	}

	public static <T> Map<String, T> bean2MapKey2Low(Object target) {
		Map<String, T> map = bean2Map(target);
		map = transKey2Low(map);
		return map;
	}

	/**
	 * 将目标对象的所有属性转换成Map对象
	 * 
	 * @param target
	 *            目标对象
	 * @param ignoreParent
	 *            是否忽略父类的属性
	 * 
	 * @return Map
	 */
	public static <T> Map<String, T> bean2Map(Object target, boolean ignoreParent) {
		return bean2Map(target, ignoreParent, false);
	}

	/**
	 * 将目标对象的所有属性转换成Map对象
	 * 
	 * @param target
	 *            目标对象
	 * @param ignoreParent
	 *            是否忽略父类的属性
	 * @param ignoreEmptyValue
	 *            是否不把空值添加到Map中
	 * 
	 * @return Map
	 */
	public static <T> Map<String, T> bean2Map(Object target, boolean ignoreParent, boolean ignoreEmptyValue) {
		return bean2MapByMapIgnore(target, ignoreParent, ignoreEmptyValue, new String[0]);
	}

	/**
	 * 将目标对象的所有属性转换成Map对象
	 * 
	 * @param target
	 *            目标对象
	 * @param ignoreParent
	 *            是否忽略父类的属性
	 * @param ignoreEmptyValue
	 *            是否不把空值添加到Map中
	 * @param ignoreProperties
	 *            不需要添加到Map的属性名
	 */
	private static <T> Map<String, T> bean2MapByMapIgnore(Object target, boolean ignoreParent, boolean ignoreEmptyValue, String... ignoreProperties) {
		
		return bean2Map(target, ignoreParent, ignoreEmptyValue,MapIgnore.class,ignoreProperties);
	}
	/**
	 * 导出标记忽略转换
	 * @param target
	 * @return
	 */
	public static <T> Map<String, T> bean2MapByExportIgnore(Object target) {
		return bean2Map(target, false, false,ExportIgnore.class,new String[0]);
	}
	
	/**
	 * 将目标对象的所有属性转换成Map对象
	 * 
	 * @param target
	 *            目标对象
	 * @param ignoreParent
	 *            是否忽略父类的属性
	 * @param ignoreEmptyValue
	 *            是否不把空值添加到Map中
	 * @param annotationIgnoreClass
	 * 			  忽略标记
	 * @param ignoreProperties
	 *            不需要添加到Map的属性名
	 */
	private static <T> Map<String, T> bean2Map(Object target, boolean ignoreParent, boolean ignoreEmptyValue,Class annotationIgnoreClass, String... ignoreProperties) {
		Map<String, T> map = new HashMap<String, T>();

		List<Field> fieldList = getFields(target.getClass(), ignoreParent);
		for (Iterator<Field> it = fieldList.iterator(); it.hasNext();) {
			String fieldName = it.next().getName();
			T value = null;
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getMethodName = "get" + firstLetter + fieldName.substring(1);
			Method method;

			try {
				method = target.getClass().getMethod(getMethodName, new Class[] {});
				if (method.isAnnotationPresent(annotationIgnoreClass)) {
					continue;
				}
				value = (T) method.invoke(target, new Object[] {});
			} catch (Exception e) {
				continue;
			}

			if (ignoreEmptyValue
					&& ((value == null || value.toString().equals("")) || (value instanceof Collection && ((Collection<?>) value).isEmpty()) || (value instanceof Map && ((Map<?, ?>) value)
							.isEmpty()))) {
				continue;
			}
			map.put(fieldName, value);
		}

		return map;
	}

	/**
	 * 将驼峰标识的变量转换为下划线
	 * 
	 * @param key
	 * @return
	 */
	public static String transKey(String key) {
		String newKey = "";
		for (int i = 0; i < key.length(); i++) {
			char ch = key.charAt(i);
			if (Character.isUpperCase(ch)) {
				newKey += "_";
			}
			newKey += ch;
		}
		return newKey;
	}

	public static <T> Map<String, T> transKey(Map<String, T> map) {
		Map<String, T> newMap = new HashMap<String, T>();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String newKey = transKey(key);
			newKey = newKey.toUpperCase();
			newMap.put(newKey, map.get(key));
		}
		return newMap;
	}

	public static <T> Map<String, T> transKey2Low(Map<String, T> map) {
		Map<String, T> newMap = new HashMap<String, T>();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String newKey = transKey(key);
			newKey = newKey.toLowerCase();
			newMap.put(newKey, map.get(key));
		}
		return newMap;
	}

	/**
	 * 获取object中的所有field
	 * 
	 * @param target
	 * @param ignoreParent
	 * @return
	 */
	public static List<Field> getFields(Class target, boolean ignoreParent) {
		if(target == null){
			return null;
		}
		if (target.getName().endsWith("JacksonBean"))
			return null;
		List<Field> fieldList = new ArrayList<Field>();
		Field[] fields = target.getDeclaredFields();
		for (Field field : fields) {
			fieldList.add(field);
		}
		if (!ignoreParent) {
			List<Field> fieldListTemp = getFields(target.getSuperclass(), ignoreParent);
			if (fieldListTemp != null) {
				fieldList.addAll(fieldListTemp);
			}
		}
		return fieldList;
	}
	/**
	 * 将map上的key对应的value值，赋值给t对象上
	 * @param obj
	 * @param map
	 * @return
	 */
	public static Object setBeanByMap(Object obj,Map map){
		if(map==null)return obj;
		Method[] methods = obj.getClass().getMethods();
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			String firstLetter = key.substring(0, 1).toUpperCase();
			String setMethodName = "set" + firstLetter + key.substring(1);

			for(Method method:methods){
				if(method.getName().equals(setMethodName)){
					try {
						method.invoke(obj, new Object[] {map.get(key)});
					} catch (Exception e) {
						continue;
					}
				}
			}

		}
		return obj;
	}

	
}

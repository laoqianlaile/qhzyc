/**
. * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.plugins.authsystem.utils</p>
 * <p>文件名:BeanUtil.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-07-24 15:13:23
 */
package com.ces.xarch.plugins.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.ces.xarch.core.security.entity.SysSystem;
import com.ces.xarch.core.security.vo.SystemRoleVO;
import com.ces.xarch.core.utils.SearchFilter;
import com.ces.xarch.core.utils.SearchSpecification;

/**
 * 系统管理平台实体工具类.
 * <p>描述:负责将系统管理平台中的实体和sdk实体类互相转换</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015-6-9 21:02:07
 * @version 1.0.2013.0609
 */
public class BeanConvertUtil {
	
	private static final boolean IGNORE_PARENT = true;
	
	
	/**
	 * 两个有相同字段名的实体类互相转换, (字段名不同该字段将不会被赋值)
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月6日 上午12:54:45
	 * @param fromObj 待转换实体对象
	 * @param toObj 转换成的实体对象
	 * @return Object 转换成的实体对象
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Object converter(Object fromObj, Object toObj){
		List<Field> fields = BeanUtil.getFields(fromObj.getClass(), !IGNORE_PARENT);
		for (Field field : fields) {
			String fieldName = field.getName();
			if(fieldName.equals("serialVersionUID")){
				continue;
			}
			Class fieldType = field.getType();
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			
			String setMethodName = "set"+firstLetter+fieldName.substring(1);
			String getMethodName = "get"+firstLetter+fieldName.substring(1);
			
			Method getMethod;
			try {//在for里用try性能消耗较大, 后期可考虑优化
				getMethod = fromObj.getClass().getMethod(getMethodName, new Class[] {} );
				Method setMethod = toObj.getClass().getMethod(setMethodName, fieldType );
				
				Object result = getMethod.invoke(fromObj,new Object[] {});
				setMethod.invoke(toObj, result);
			} catch (Exception e) {
				continue;
			}
			
		}
		fromObj = null;
		return toObj;
	}
	
	

	/**
	 * 将框架里面search filter转换为map
	 * @author 方俊新(fang.junxin@cesgroup.com.cn)
	 * @date 2015-03-30 14:32:04
	 * @param spec
	 * @return
	 */
	public static Map<String, String> getFilter(Specification spec) {
		if(!(spec instanceof SearchSpecification)){
			throw new RuntimeException("非SearchSpecification对象");
		}
		SearchSpecification searchspec = (SearchSpecification) spec;
		Collection<SearchFilter> searchFilters = searchspec.getFilters();
		Map<String, String> param = new HashMap<String, String>();
		for (SearchFilter filter : searchFilters) {
			param.put(filter.fieldName, filter.value.toString());
		}
		return param;
	}
	
	public static SystemRoleVO convert(SysSystem sysSystem, SystemRoleVO systemRoleVO){
		if(sysSystem != null){
			systemRoleVO = new SystemRoleVO(sysSystem.getId(),sysSystem.getName(),sysSystem.getCode());
		}
		return systemRoleVO;
	}
}

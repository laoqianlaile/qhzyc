/**
 * <p>Copyright:Copyright(c) 2014</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.core.web.frame.utils</p>
 * <p>文件名:FrameDataModuleHandling.java</p>
 * <p>类更新历史信息</p>
 * @todo <a href="mailto:yangmujiang@sohu.com">Reamy(杨木江)</a> 创建于 2014-05-27 15:16:44
 */
package com.ces.config.datamodel.page.utils;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.utils.StringUtils;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * 
 * <p>描述: 自定义配置展现</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-11 下午6:56:03
 *
 */
@Component
public class FramePageModuleHandling {
	/** 框架数据模型转换处理类后缀. */
	private static final String DATAMODULEHANDLING_SUF = "PageModuleHandling";
	
	/**
	 * qiucs 2014-7-11 
	 * <p>描述: 根据前端UI获取相应的数据模型.</p>
	 * @param  modelName
	 * @return Object    返回类型   
	 */
	public static DefaultPageModel getPageModel(String frameName, String modelName, Class<?> modelClass, String id) {
		Object frameHandling = findFrameDataModuleHandling(frameName);
		
		if (frameHandling != null && !StringUtils.isEmpty(modelName)) {
			Method method = ReflectionUtils.findMethod(frameHandling.getClass(), modelName, Class.class, String.class);
			if (method != null) {
				return (DefaultPageModel)ReflectionUtils.invokeMethod(method, frameHandling, modelClass, id);
			}
		}
		
		return new DefaultPageModel();
	}
	
	/**
	 * qiucs 2014-7-11 
	 * <p>描述: 获取对应的前端表示层框架数据模型转换处理对象.</p>
	 * @param  frameName
	 */
	private static final Object findFrameDataModuleHandling(String frameName) {
		
		if (!StringUtils.isEmpty(frameName)) {
			return XarchListener.getBeanNotRequired(frameName+DATAMODULEHANDLING_SUF);
		}
		
		return null;
	}
}

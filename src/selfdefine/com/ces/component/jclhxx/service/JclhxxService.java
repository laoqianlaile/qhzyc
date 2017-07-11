package com.ces.component.jclhxx.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class JclhxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	/**
	 * 默认权限过滤
	 * 
	 * @return
	 */
	public String defaultCode() {
		String code = SerialNumberUtil.getInstance().getCompanyCode();
		Date date = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String compareDate = df.format(new Date(date.getTime()-4*24*60*60*1000));
		String defaultCode = " ";
		if (code != null && !"".equals(code))
			defaultCode = AppDefineUtil.RELATION_AND + " PFSCBM = '" + code
					+ "' " + AppDefineUtil.RELATION_AND + "JCRQ>='"+compareDate+"'";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
		return defaultCode();
	}
}
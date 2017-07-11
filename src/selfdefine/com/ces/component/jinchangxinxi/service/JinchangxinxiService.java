package com.ces.component.jinchangxinxi.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.gongzuorenyuandangan.dao.GongzuorenyuandanganDao;
import com.ces.component.gongzuorenyuandangan.entity.GongzuorenyuandanganEntity;
import com.ces.component.jinchangxinxi.dao.JinchangxinxiDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class JinchangxinxiService extends
		TraceShowModuleDefineDaoService<StringIDEntity, JinchangxinxiDao> {

	@Override
	public void setDao(JinchangxinxiDao dao) {
		super.setDao(dao);
	}

	public String getJcsfbh() {
		String jcsfbhSequence = getDao().getJcsfbhSequence().toString();// 获取sequence
		String qybm = getDao().getQybm().toString();// 获取企业编码
		int length = jcsfbhSequence.length();
		while (length < 11) {
			jcsfbhSequence = "0" + jcsfbhSequence;
			length++;
		}
		return qybm + jcsfbhSequence;
	}

	/**
	 * 默认权限过滤
	 * 
	 * @return
	 */
	public String defaultCode() {
		String code = SerialNumberUtil.getInstance().getCompanyCode();
		String defaultCode = " ";
		if (code != null && !"".equals(code))
			defaultCode = AppDefineUtil.RELATION_AND + " PFSCBM = '" + code
					+ "' ";
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

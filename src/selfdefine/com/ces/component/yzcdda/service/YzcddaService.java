package com.ces.component.yzcdda.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.yzcdda.dao.YzcddaDao;
import com.ces.component.yzcdda.entity.YzcddaEntity;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;

@Component
public class YzcddaService extends TraceShowModuleDefineDaoService<YzcddaEntity, YzcddaDao> {

    
	/**
	 * 通过登录者信息获得账号编码
	 * @return
	 */
	public String  getQybm(){
		return SerialNumberUtil.getInstance().getCompanyCode();
	}
	/**
	 * 通过当前登录者的账号编码获得产地档案
	 * @return CddaEntity
	 */
	public YzcddaEntity getByQybm(){
		return getDao().getByQybm(getQybm());
	}
	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode=" AND ZHBH = '"+code+"' ";
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

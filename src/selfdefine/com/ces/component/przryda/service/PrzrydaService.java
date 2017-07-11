package com.ces.component.przryda.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class PrzrydaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@Override
	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
		return AppDefineUtil.RELATION_AND + "TZCBM='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
    }
}
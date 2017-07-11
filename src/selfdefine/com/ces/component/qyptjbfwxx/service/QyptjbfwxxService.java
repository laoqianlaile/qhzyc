package com.ces.component.qyptjbfwxx.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class QyptjbfwxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {


	@Override
	@Transactional
	protected void processBeforeSave(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
		String zhbh = dataMap.get("ZHBH");
		String sql = "update " + tableName + " t set t.sfdqfw = '2' where t.zhbh = '" + zhbh + "'";
		DatabaseHandlerDao.getInstance().executeSql(sql);
	}

}
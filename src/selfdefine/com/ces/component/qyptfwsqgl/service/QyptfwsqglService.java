package com.ces.component.qyptfwsqgl.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class QyptfwsqglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	/**
	 * 保存前设置审核时间为当前时间
	 * @param  tableName
	 * @param  dataMap
	 * @param paramMap
	 */
	@Override
	protected void processBeforeSave(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
		dataMap.put("SHSJ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}

}
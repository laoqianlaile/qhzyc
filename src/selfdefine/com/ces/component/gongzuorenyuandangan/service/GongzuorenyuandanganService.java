package com.ces.component.gongzuorenyuandangan.service;

import java.util.List;
import java.util.Map;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import ces.workflow.wapi.define.Model;

import com.ces.component.gongzuorenyuandangan.dao.GongzuorenyuandanganDao;
import com.ces.component.gongzuorenyuandangan.entity.GongzuorenyuandanganEntity;
import com.ces.component.moduledemo.dhtmlx.dao.ModuleCategoryDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class GongzuorenyuandanganService extends TraceShowModuleDefineDaoService<GongzuorenyuandanganEntity, GongzuorenyuandanganDao> {
	
	@Override
	 protected void processBeforeSave(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
	        // dataMap中 key 为字段名，如ID/NAME...；value 为字段对应的值
	        // dataMap.put("USER_NAME", "qiucs")
		//	dataMap.put("YGBM", getMaxYgbm());
			
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
				defaultCode = AppDefineUtil.RELATION_AND + " PFSCBM = '" + code+ "' ";
			return defaultCode;
		}

		@Override
		protected String buildCustomerFilter(String tableId,
				String componentVersionId, String moduleId, String menuId,
				Map<String, Object> paramMap) {
			// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
			return defaultCode();
		}



    public Object checkDuplicate(String id,String sfzh){
        StringBuffer sql = new StringBuffer("");
        if("".equals(id)){
            sql.append("SELECT * FROM T_PC_GZRYDA T WHERE ID IS NOT NULL");
        }else{
            sql.append("SELECT * FROM T_PC_GZRYDA T WHERE ID != '" + id + "'");
        }
        sql.append(" AND SFZH = '" + sfzh + "' AND PFSCBM = '" + SerialNumberUtil.getInstance().getCompanyCode() +"' AND IS_DELETE <> '1'");
        List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        return dataList;
    }
}

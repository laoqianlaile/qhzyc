package com.ces.component.sdszyyypjc.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.sdszyyypjc.dao.SdszyyypjcDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class SdszyyypjcService extends TraceShowModuleDefineDaoService<StringIDEntity, SdszyyypjcDao> {
    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        // 1. 获取所有关联表的要删除的IDS
        String tableName = "t_cs_scjcxx";
        String[] idDatas=ids.split(",");
        StringBuffer newIds=new StringBuffer("");
        for (int i = 0; i < idDatas.length; i++) {
            if(i >0 && i<=idDatas.length-1){
                newIds.append(",");
            }
            String string = idDatas[i];
            newIds.append("'"+string.split("_")[0]+"'");
        }
        String filter    = "DELETE FROM "+tableName + " WHERE ID IN (" + newIds.toString() + ")";
        DatabaseHandlerDao.getInstance().executeSql(filter);
    }
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap)
    {
        StringBuffer filter = new StringBuffer(" AND  is_delete = '0'");

        filter.append(" AND  csbm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'");
        return filter.toString();
    }
}

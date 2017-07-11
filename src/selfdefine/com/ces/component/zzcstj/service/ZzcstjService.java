package com.ces.component.zzcstj.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.TableUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class ZzcstjService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        StringBuffer filter = new StringBuffer();
        String tableName = TableUtil.getTableName(tableId);
        boolean isQybmColumnExist = DatabaseHandlerDao.getInstance().columnExists(tableName, "qybm");
        if (isQybmColumnExist) {
            String qybm = SerialNumberUtil.getInstance().getCompanyCode();
            filter.append(AppDefineUtil.RELATION_AND + " qybm = '" + qybm + "'");
        }
        return filter.toString();
    }
}
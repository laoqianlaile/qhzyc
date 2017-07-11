package com.ces.component.zlwtqd.service;

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
public class ZlwtqdService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
          String qybm=SerialNumberUtil.getInstance().getCompanyCode();
        StringBuffer filter = new StringBuffer(AppDefineUtil.RELATION_AND + " (wtbj = '1' or ccwtbj= '1') and qybm = '"+qybm+"'");
//        String tableName = TableUtil.getTableName(tableId);
//        boolean isQybmColumnExist = DatabaseHandlerDao.getInstance().columnExists(tableName, "qybm");
//        if (isQybmColumnExist) {
//            String qybm = SerialNumberUtil.getInstance().getCompanyCode();
//            filter.append(AppDefineUtil.RELATION_AND + " qybm = '" + qybm + "'");
//        }
//        log.debug("表" + tableName + "的过滤条件：" + filter.toString());
        return filter.toString();


         //int i=0;
       // return "";
    }
    
}
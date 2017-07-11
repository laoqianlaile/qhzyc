package com.ces.component.sdszyysmjc.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import enterprise.endpoints.EnterpriseService;
import org.springframework.jca.cci.core.InteractionCallback;
import org.springframework.stereotype.Component;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.sdszyysmjc.dao.SdszyysmjcDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class SdszyysmjcService extends TraceShowModuleDefineDaoService<StringIDEntity, SdszyysmjcDao> {
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        // 1. 获取所有关联表的要删除的IDS
        String tableName = "t_cs_scjcxx";
        String tableNamezb = "t_cs_scjcmxxx";
        String[] idDatas=ids.split(",");
        StringBuffer newIds=new StringBuffer("");
        for (int i = 0; i < idDatas.length; i++) {
            if(i >0 && i<=idDatas.length-1){
                newIds.append(",");
            }
            String string = idDatas[i];
            newIds.append("'"+string.split("_")[0]+"'");
        }
        String sql    = "select pid FROM "+tableNamezb + " WHERE ID IN (" + newIds.toString() + ")";
        Map<String, Object> pMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        String pid = pMap.get("PID").toString();
        String filter    = "DELETE FROM "+tableNamezb + " WHERE ID IN (" + newIds.toString() + ")";
        DatabaseHandlerDao.getInstance().executeSql(filter);
        String sqlz    = "select count(pid) num FROM "+tableNamezb + " WHERE pid=?";
        Map<String, Object> pzMap = DatabaseHandlerDao.getInstance().queryForMap(sqlz, new String[]{pid});
        String num =  pzMap.get("NUM").toString();
        if(!num.isEmpty())
        {
            if(Integer.parseInt(num)==0)
            {
                String sqlzb    = "DELETE FROM "+tableName + " WHERE ID IN ('" + pid + "')";
                DatabaseHandlerDao.getInstance().executeSql(sqlzb);
                sendDelTradeInService(pid);
            }
        }
    }
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap)
    {
        StringBuffer filter = new StringBuffer(" AND  is_delete = '0'");

        filter.append(" AND  csbm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'");
        return filter.toString();
    }
    public void sendDelTradeInService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTradeIn(id);
    }
}

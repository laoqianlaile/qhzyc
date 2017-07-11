package com.ces.component.qhpfyljy.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import enterprise.endpoints.EnterpriseService;
import org.springframework.stereotype.Component;

import com.ces.component.qhpfyljy.dao.QhpfyljyDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QhpfyljyService extends TraceShowModuleDefineDaoService<StringIDEntity, QhpfyljyDao> {
    public Map<String,Object> getQhpfyljy(){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_qh_yljygl where is_delete = '0' and qybm=?";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{qybm});
        return getGridTypeData(list);
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    //    @Override
//    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
//        // 1. 获取所有关联表的要删除的IDS
//        Map<String, DeleteFilter> filterMap = Maps.newLinkedHashMap();
//        DeleteFilter dFilter = new DeleteFilter();
//        String tableName = getTableName(tableId);
//        String filter    = tableName + ".ID IN ('" + ids.replace(",", "','") + "')";
//        dFilter.setTableName(tableName);
//        dFilter.setFilter(filter);
//        filterMap.put(tableId, dFilter);
//
//        String[] dTableIdArr = StringUtil.isNotEmpty(dTableIds) ? dTableIds.split(",") : new String[] {};
//
//        StringBuffer tableNames = new StringBuffer(tableName);
//        StringBuffer filters    = new StringBuffer(filter);
//
//        for (int i = 0; i < dTableIdArr.length; i++) {
//            String relation = null, mTableId = null, dTableId = dTableIdArr[i], dTableName = getTableName(dTableId);
//            dFilter = new DeleteFilter();
//            dFilter.setTableName(dTableName);
//            if (i == 0) {
//                mTableId = tableId;
//            } else {
//                mTableId = dTableIdArr[i - 1];
//            }
//
//            relation = getTableRelation(mTableId, tableName, dTableId, dTableName);
//            filters.append(AppDefineUtil.RELATION_AND).append(relation);
//            //filter = "EXISTS(SELECT 1 FROM " + tableNames + " WHERE " + filters + ")";
//            dFilter.setRelateTableNames(tableNames.toString());
//            dFilter.setFilter(filters.toString());
//
//            tableNames.append(",").append(dTableName);
//            tableName = dTableName;
//
//            filterMap.put(dTableId, dFilter);
//        }
//        // 2. 删除所关联的表数据
//        for (int i = dTableIdArr.length - 1; i >= 0; i--) {
//            String dTableId = dTableIdArr[i];
//            deleteByTableId(dTableId, filterMap.get(dTableId), false, isLogicalDelete);
//        }
//        // 3. 删除自身的表数据
//        deleteByTableId(tableId, filterMap.get(tableId), false, isLogicalDelete);
//    }


    public void delete(String id){
        String sql="delete from t_qh_jcbgfjb where pid= '"+id+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        //删除省平台数据
       EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTest(id);
        // sendDelTraceOutService(id);
        }
}

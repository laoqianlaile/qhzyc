package com.ces.component.sdzycymsy.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycymsy.dao.SdzycymsyDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class SdzycymsyService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycymsyDao> {


    public Map<String,Object> getYmbh(){
        String sql = "select YMBH,YMPZ from T_SDZYC_YMXX where is_delete = '0' and qybm=?";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{SerialNumberUtil.getInstance().getCompanyCode()}));
    }

    public Map<String,Object> getDkbh() {//是否使用状态为   1 代表使用   2代表空闲
        String sql = "select DKBH,DKMJ,DKWZ from T_SDZYC_SYTXX where is_delete = '0' and qybm=? and sfsy='2'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()}));
    }

    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    public Map<String,Object>  processDkzt(String dkbh){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        String sql = "update T_SDZYC_SYTXX set sfsy='1' where dkbh='"+dkbh+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        return dataMap;
    }

    /**
     * 复写删除方法
     * 删除试验田育苗数据 同时更新试验田使用状态
     * @param  tableId
     * @param dTableIds
     * @param  ids    设定参数
     * @param isLogicalDelete
     * @param paramMap
     */
    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        // 1. 获取所有关联表的要删除的IDS
        String tableName = "T_SDZYC_YMSY";
        String[] idDatas=ids.split(",");
        StringBuffer newIds=new StringBuffer("");
        for (int i = 0; i < idDatas.length; i++) {
            if(i >0 && i<=idDatas.length-1){
                newIds.append(",");
            }
            String string = idDatas[i];
            //更新试验田使用状态
            String sfsy_sql = "update t_sdzyc_sytxx set sfsy='2' where dkbh=(select dkbh from T_SDZYC_YMSY where id='"+string+"') and qybm='"+qybm+"'";
            DatabaseHandlerDao.getInstance().executeSql(sfsy_sql);
            newIds.append("'" + string.split("_")[0]+"'");
        }
        String filter = "DELETE FROM "+tableName + " WHERE ID IN (" + newIds.toString() + ")";
        DatabaseHandlerDao.getInstance().executeSql(filter);

    }


}

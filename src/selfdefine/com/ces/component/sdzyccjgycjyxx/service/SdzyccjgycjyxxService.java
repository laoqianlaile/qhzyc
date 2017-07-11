package com.ces.component.sdzyccjgycjyxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import enterprise.endpoints.EnterpriseService;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgycjyxx.dao.SdzyccjgycjyxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyccjgycjyxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgycjyxxDao> {

    /**
     *默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }

    /**
     * 获取药材入库下拉列表数据
     * @return
     */
    public Map<String,Object> searchycjyxxComboGridData(){
        String sql = "select * from t_sdzyc_cjg_ycjxx where 1=1 "+defaultCode();
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String,Object> searchycjyxxComboGridData1(){
        String sql = "select * from t_sdzyc_cjg_ycjxx where 1=1 "+defaultCode();
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }


    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public Map<String,Object> searchycjyxxByjybh(String jybh){
        String sql =  "select * from t_sdzyc_cjg_ycjxx where jybh=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{jybh});
    }
    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        String delChild = "delete from t_sdzyc_cjg_ycjyxxxx t2 where  t2.xsddh  in ( select t1.xsddh from T_SDZYC_CJG_YCJYXX t1 where t1.ID IN ('" + ids.replace(",", "','") + "'))";
        String delFather = "delete from T_SDZYC_CJG_YCJYXX t1 where t1.ID IN ('" + ids.replace(",", "','") + "')";
        DatabaseHandlerDao.getInstance().executeSql(delChild);
        DatabaseHandlerDao.getInstance().executeSql(delFather);
        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            sendDelTraceOutService(id[i]);
        }
    }
   /* @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        // 1. 获取所有关联表的要删除的IDS
        String tableName = "T_SDZYC_CJG_YCJYXX";
        String[] idDatas=ids.split(",");
        StringBuffer newIds=new StringBuffer("");
        for (int i = 0; i < idDatas.length; i++) {
            if(i >0 && i<=idDatas.length-1){
                newIds.append(",");
            }
            String string = idDatas[i];
            newIds.append("'"+string.split("_")[0]+"'");
        }
        String filter = "DELETE FROM "+tableName + " WHERE ID IN (" + newIds.toString() + ")";
        DatabaseHandlerDao.getInstance().executeSql(filter);
    }*/
    public Map<String,Object> searchDataByPch(String pch){
        String sql ="select * from t_sdzyc_cjg_ycjyxxxx where  pch='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    private void sendDelTraceOutService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTradeOut(id);
    }
}




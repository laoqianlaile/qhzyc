package com.ces.component.sdzyccjgylllxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.*;
import com.ces.xarch.core.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgylllxx.dao.SdzyccjgylllxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyccjgylllxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgylllxxDao> {
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
     * 获取原料领料的领料单号
     * @return
     */
    public Map<String,Object> searchylllxxComboGridData(){
        String sql = "select * from t_sdzyc_cjg_ylllxx t where issc='0' "+defaultCode()+" order by llsj desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public Map<String,Object> searchylllxxBylldh(String lldh){
        String sql =  "select * from t_sdzyc_cjg_ylllxx where lldh=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{lldh});
    }



  /*  @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        // 1. 获取所有关联表的要删除的IDS
        String tableName = "T_SDZYC_CJG_YLLLXX";
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
    }*/
    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        String delChild = "delete from t_sdzyc_cjg_ylllxxxx t2 where  t2.lldh  in ( select t1.lldh from T_SDZYC_CJG_YLLLXX t1 where t1.ID IN ('" + ids.replace(",", "','") + "'))";
        String delFather = "delete from T_SDZYC_CJG_YLLLXX t1 where t1.ID IN ('" + ids.replace(",", "','") + "')";
        DatabaseHandlerDao.getInstance().executeSql(delChild);
        DatabaseHandlerDao.getInstance().executeSql(delFather);
    }
    public Map<String,Object> searchDataByPch(String pch){
        String sql ="select * from v_sdzyc_cjg_ylllxx where  pch='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Object getLlbmxx() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();//000000824
        String sql="select t.BMBH,t.BMMC from T_SDZYC_CJG_LLBMXX t where t.qybm = ? and t.is_delete = '0'";
        // List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[] {qybm});
        // List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;
    }

    public Object getGgxx() {
            String qybm = SerialNumberUtil.getInstance().getCompanyCode();//000000824
        String sql="select t.GGBH,t.GG from T_SDZYC_CJG_GGXX t where t.qybm = ? and t.is_delete = '0'";
        // List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[] {qybm});
        // List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;
    }
}

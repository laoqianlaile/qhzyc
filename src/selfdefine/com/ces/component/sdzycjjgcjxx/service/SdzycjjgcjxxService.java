package com.ces.component.sdzycjjgcjxx.service;

import com.ces.component.sdzycjjgcjxx.dao.SdzycjjgcjxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycjjgcjxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgcjxxDao> {

    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//����������
            String cjgcjbh= SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCJGCJBH", false);
            dataMap.put("CJBH",cjgcjbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    /**
     * 查询精加工车间信息
     * @return
     */
    public Map<String,Object> searchJggcjxx(){
        String sql = "select * from t_sdzyc_jjg_cjxx where qybm=?";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return getGridTypeData(list);
    }

    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    /**
     *
     * @return
     */
    public Map<String,Object> searchcdzmxxComboGridData(){
        String sql = "select * from t_sdzyc_jjg_Cdzmw where 1=1";///+ " order by llsj desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * ��ȡ����ͼƬ����  ͨ���ϴ�ͼƬ
     * @param sctpmc
     * @return
     */
    public Map<String,Object> searchcdzmxxData(String sctpmc){
        String sql ="select * from t_sdzyc_jjg_Cdzmw where sctpmc='"+sctpmc+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    //    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
//        Map<String,Object> dataMap = new HashMap<String, Object>();
//        dataMap.put("data",list);
//        return  dataMap;
//    }
    public Map<String,Object> searchcdzmxxBylldh(String sctpmc){
        String sql =  "select * from t_sdzyc_jjg_Cdzmw";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{sctpmc});
    }
    /**
     *Ĭ��Ȩ�޹���
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" SCTPMC= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
}

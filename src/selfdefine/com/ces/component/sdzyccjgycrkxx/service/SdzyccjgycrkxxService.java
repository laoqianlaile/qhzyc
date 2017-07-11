package com.ces.component.sdzyccjgycrkxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgycrkxx.dao.SdzyccjgycrkxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyccjgycrkxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgycrkxxDao> {

    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String ycrkbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCJGYCRKBH", false);
            dataMap.put("RKBH",ycrkbh);
        }
        id = save(tableName, dataMap, paramMap);
        //保存成功同步处理打印赋码信息更改为已入库状态
        String sql = "update t_sdzyc_cjg_ycjgxx set sfrk='1' where JGPCH=?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataMap.get("PCH")});
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    //企业批次号下拉框数据   药材名称  入库重量   库存(不为零)
    public Map<String,Object> findQypchGridData(){
        String sql = "select * from v_sdzyc_cjg_kcglxx where kc > 0 and qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"' order by rksj desc";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return getGridTypeData(list);
    }

    /**
     * 获得药材信息
     * @param qypch
     * @return
     */
    public Map<String,Object> getYcxx(String qypch){
        String sql = "select * from t_Sdzyc_Cjg_Dyfmxx where qypch='"+qypch+ "' and qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }
    /**
     * 获得精加工根据采购订单号获得粗加工药材交易数据中的药材批次号数据
     * @return
     */
    public List<Map<String,Object>> searchYcrkxxComboGridData(String cgdh){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select T.QYPCH,T.CSPCH,t.ycdm,t.cdmc,t.jyzl,t.ycmc,t.pch,t.qybm,t.xsddh,m.QYXSDDH,t.yptzsm from T_SDZYC_CJG_YCJYXXXX  t inner join T_sdzyc_cjg_ycjyxx m on t.xsddh=m.xsddh where m.QYXSDDH= '"+cgdh+"'and t.qybm='"+companyCode+"'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public Map<String,Object> searchycjyxxBypch(){
        String sql="select t.cspch, t.qybm,t.pch,t.ycmc,t.rkzl,t.cdmc,m.ycdm,t.qypch from T_SDZYC_CJG_YCRKXX t inner join t_Sdzyc_Cjg_Dyfmxx m on t.pch=m.scpch and t.qybm=?";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return getGridTypeData(list);
    }
    /**保存导入数据
     * @param dataList
     * @return
     */
    @Transactional
    public Object importXls(List<Map<String,String>> dataList){
        Map<String,String> resultMap = new HashMap<String, String>();
        Map<String,Object> shareMap;
        if(dataList.size() == 0){
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","模版里无数据！");
            return resultMap;
        }
        resultMap.put("RESULT","SUCCESS");
        resultMap.put("MSG","成功导入数据！");
        StringBuilder sql ;
        if("SUCCESS".equals(resultMap.get("RESULT"))){
            for(Map<String,String> map:dataList){
                saveOne("t_sdzyc_cjg_ycrkxx", map);
            }
        }
        return resultMap;
    }
    public void delete(String pch,String id){
        String sql = " delete from t_sdzyc_cjg_ycrkxx where id  = '" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        String Updatesql = "update t_sdzyc_cjg_ycjgxx  set sfrk='0' where jgpch=?";
        DatabaseHandlerDao.getInstance().executeSql(Updatesql, new Object[]{pch});

    }
}

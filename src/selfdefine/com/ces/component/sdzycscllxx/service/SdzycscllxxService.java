package com.ces.component.sdzycscllxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycscllxx.dao.SdzycscllxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycscllxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycscllxxDao> {

    /**
     * 获取精加工生产领料原料批次号下拉列表数据
     * @return
     */
    public Map<String,Object> searchYlpchxxComboGridData(){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from T_SDZYC_JJG_YYCRKXX t where t.qybm= '"+companyCode+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    /**
     * 获取精加工生产领料企业批次号下拉框数据
     * @return
     */
    public Map<String,Object> getJjgYlpchGridData(){
        String sql = "select * from v_sdzyc_jjg_ylkcglxx where kc > 0 and qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"' order by create_time desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String,Object> getFlck(String pch){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select nvl(drck,slck) as flck,b.pch,b.cspch from t_sdzyc_jjg_ycdbxx a right join t_sdzyc_jjg_yycrkxx b on a.pch = b.pch where b.qybm = '"+qybm+"' and b.pch='"+pch+"'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }
    /**
     * 山东中药材精加工领料单号自动生成 保存
     * @param tableId
     * @param entityJson
     * @param paramMap --参数Map（具体参数要求请查看ShowModuleDefineServiceDaoController.getMarkParamMap方法说明）
     * @return
     */
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String jjggysbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGSCLLDH", true);
            dataMap.put("LLDH",jjggysbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        String delChild = "delete from t_sdzyc_jjg_scllxxxx t2 where  t2.lldh  in ( select t1.lldh from t_sdzyc_jjg_scllxx t1 where t1.ID IN ('" + ids.replace(",", "','") + "'))";
        String delFather = "delete from t_sdzyc_jjg_scllxx t1 where t1.ID IN ('" + ids.replace(",", "','") + "')";
        DatabaseHandlerDao.getInstance().executeSql(delChild);
        DatabaseHandlerDao.getInstance().executeSql(delFather);
    }

    public Object getLlbmxx() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();//000000824
        String sql="select t.BMBH,t.BMMC from T_SDZYC_JJG_LLBMXX t where t.qybm = ? and t.is_delete = '0'";
        // List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[] {qybm});
        // List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;
    }

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
    public Map<String,Object> searchDataByPch(String pch){
        String sql ="select * from v_sdzyc_jjg_scllxx where  pch='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
}

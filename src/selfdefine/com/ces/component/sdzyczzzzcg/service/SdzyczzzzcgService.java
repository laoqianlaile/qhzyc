package com.ces.component.sdzyczzzzcg.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyczzzzcg.dao.SdzyczzzzcgDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyczzzzcgService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyczzzzcgDao> {
    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String zzcgbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCZZCGBH", false);
            dataMap.put("ZZCGBH",zzcgbh);
        }
        id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    //获取种子种苗信息
    public  Object getZzzmxx(){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();//000000824
        String sql="select t.ZZZMMC,t.ZZZMBH from t_sdzyc_zzzm t where t.qybm = ? and t.is_delete = '0'";
       // List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[] {qybm});
       // List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;

    }

    public Map<String, Object> getResultData(List<Map<String, Object>> data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", data);
        return result;
    }

    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code + "' and is_delete = '0' ";
        return defaultCode;
    }


}

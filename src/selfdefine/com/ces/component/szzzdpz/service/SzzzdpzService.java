package com.ces.component.szzzdpz.service;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SzzzdpzService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 品类底下是否存在品种
     */
    public List<String> hasPz(String[] plids) {
        //返回存在品种的品类名称
        List<String> data = new ArrayList<String>();
        for (String plid : plids) {
            String countSql = "select count(*) as count from t_zz_dpzxx t, t_zz_xpzxx s where t.plbh = s.plbh and t.qybm = s.qybm and s.is_delete = '0' and t.id = ?";
            Object count = DatabaseHandlerDao.getInstance().queryForObject(countSql, new Object[]{plid});
            if (Integer.parseInt(String.valueOf(count)) > 0) {
                String sql = "select t.pl from t_zz_dpzxx t where t.id = ?";
                Object pl = DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{plid});
                data.add(String.valueOf(pl));
            }
        }
        return data;
    }
    /**
     *  获取商品名称和商品编码
     */
    public Map<String,Object> getSpxx(){
    	String sql = "select t.SPMC,t.SPBM from t_common_scspxx t where t.is_delete =0";
    	List<Map<String,Object>> listData = DatabaseHandlerDao.getInstance().queryForMaps(sql);
    	Map<String,Object> dataMap = new HashMap<String, Object>();
    	dataMap.put("data", listData);
    	return dataMap;
    }

    
    /**
     *  获取商品编码
     */
    public Map<String,Object> getSpbm(String pl){
    	String sql = "select t.SPBM from t_common_scspxx t where T.SPMC='"+pl+"' and T.is_delete = '0'";
    	List<Map<String,Object>> listData = DatabaseHandlerDao.getInstance().queryForMaps(sql);
    	Map<String,Object> dataMap = new HashMap<String, Object>();
    	dataMap.put("data", listData);
    	return dataMap;
    }
    
    @Override
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String pl = dataMap.get("PL");
        try {

//            pl = new String(pl.getBytes("iso8859-1"),"utf-8");
        }catch (Exception e){

        }

        dataMap.put("PL",pl);
        String id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
}
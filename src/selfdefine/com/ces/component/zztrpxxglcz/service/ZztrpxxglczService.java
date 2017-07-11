package com.ces.component.zztrpxxglcz.service;

import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wngyu on 15/11/16.
 */
@Component
public class ZztrpxxglczService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object getLx(){
//        Li
        List<Map<String,Object>> maps=new ArrayList<Map<String,Object>>();
        String sql="select SJMC,SJBM from t_common_sjlx_code where lxbm ='ZZTRPLX' ";
        maps= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }
    public Object getFllx(){
        List<Map<String,Object>> maps=new ArrayList<Map<String,Object>>();
        String sql="select SJMC,SJBM from t_common_sjlx_code where lxbm ='ZZFLLX' ";
        maps= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }
    public Object getBzggdw(){
        List<Map<String,Object>> maps=new ArrayList<Map<String,Object>>();
        String sql="select SJMC,SJBM from t_common_sjlx_code where lxbm ='ZZBZGGDW' ";
        maps= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }

    @Transactional
    public Map<String, String> save(String entityJson) {
        String tableName = "T_ZZ_TRPJBXX"  ;
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(id == "" || "".equals(id)){//是新增操作
            String rklsh = SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZRKBH", false);
//            dataMap.put("RKLSH",rklsh);
            dataMap.put("TRPBH",rklsh);
            String qybm=SerialNumberUtil.getInstance().getCompanyCode();
            dataMap.put("QYBM",qybm);
        }
//        dataMap.put("KCSL",dataMap.get("RKSL"));

        id = saveOne(tableName, dataMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    public Object gettheFormdata(String id){
        Map<String, Object> dataMap=new HashMap<String, Object>();
        String sql="select * from T_ZZ_TRPJBXX where id='"+id+"'";
        dataMap=DatabaseHandlerDao.getInstance().queryForMap(sql);
        return dataMap;
    }
}
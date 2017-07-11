package com.ces.component.tcsscjyplxx.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TcsscjyplxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Map<String,Object> getJyplGrid(){
        String sql ="select t.* from T_SC_JYPLXX t";
        List<Map<String,Object>> mapList= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("data",mapList);
        return map;
    }
}
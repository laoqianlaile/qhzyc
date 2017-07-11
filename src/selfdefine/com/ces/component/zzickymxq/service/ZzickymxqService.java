package com.ces.component.zzickymxq.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class ZzickymxqService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    public Map<String,Object> initForm(String id){
        String sql="select * from t_zz_ickxxgl where id='"+id+"'";
        Map<String,Object> dataMap= DatabaseHandlerDao.getInstance().queryForMap(sql);
        return dataMap;
    }

    
}
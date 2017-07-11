package com.ces.component.tzjyxxxx.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class TzjyxxxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object getXgdate(String id){
        String sql="select t.SPBM,K.CDBM from T_TZ_JYXX t,T_COMMON_CDXX k where  t.id  = '"+id+"' and  k.CDMC = t.DDD";
        Map<String,Object> map= DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }

    
}
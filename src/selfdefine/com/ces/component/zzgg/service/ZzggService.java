package com.ces.component.zzgg.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class ZzggService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object deleteGg(String dataId) {
        String sql = "update t_zz_gg set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }

    public Object deleteGgTrp(String dataId) {
        String sql = "update t_zz_ggtrp set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }
}
package com.ces.component.zzsffa.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class ZzsffaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object deleteSffa(String dataId) {
        String sql = "update t_zz_sf set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }

    public Object deleteSffaTrp(String dataId) {
        String sql = "update t_zz_sftrp set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }
}
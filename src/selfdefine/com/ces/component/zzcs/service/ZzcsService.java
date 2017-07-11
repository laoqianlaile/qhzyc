package com.ces.component.zzcs.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class ZzcsService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object deleteCs(String dataId) {
        String sql = "update t_zz_cs set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }

    public Object deleteCsTrp(String dataId) {
        String sql = "update t_zz_cstrp set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }
}
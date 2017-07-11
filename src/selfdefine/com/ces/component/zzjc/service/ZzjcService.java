package com.ces.component.zzjc.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class ZzjcService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object deleteJc(String dataId) {
        String sql = "update t_zz_jc set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }

    public Object deleteJcTrp(String dataId) {
        String sql = "update t_zz_jctrp set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }
}
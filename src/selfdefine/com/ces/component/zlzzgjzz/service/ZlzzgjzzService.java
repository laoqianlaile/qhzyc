package com.ces.component.zlzzgjzz.service;

import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * Created by Administrator on 2015/9/14.
 */
public class ZlzzgjzzService extends TraceShowModuleDefineDaoService<StringIDEntity,TraceShowModuleDao> {
    public int changeWtbj(String id,String bj){
        String sql;
        if("标记问题".equals(bj)){
             sql="update T_ZZ_CSGL set WTBJ ='1' where id='"+id+"'";
        }
            //return DatabaseHandlerDao.getInstance().executeSql(sql);}
        else {
            sql="update T_ZZ_CSGL set WTBJ ='0' where id='"+id+"'";

        }
        return DatabaseHandlerDao.getInstance().executeSql(sql);

    }
}
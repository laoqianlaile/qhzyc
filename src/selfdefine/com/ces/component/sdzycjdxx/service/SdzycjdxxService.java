package com.ces.component.sdzycjdxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjdxx.dao.SdzycjdxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class SdzycjdxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjdxxDao> {


    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_jdxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    public int updateCdzm(String id) {
        String sql =  "update t_sdzyc_jdxx set cdzm='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
}

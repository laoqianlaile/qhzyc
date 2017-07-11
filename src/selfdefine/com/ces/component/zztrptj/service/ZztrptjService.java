package com.ces.component.zztrptj.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZztrptjService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {

        return defaultCode();
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM = '"+code+"' ";
        return defaultCode;
    }

    public Object getJyzdaGrid(){
        String sql="select DKBH,DKMC from t_zz_dkxx where qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
//        DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        list= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("data",list);
        return map;
    }



    
}
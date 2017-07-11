package com.ces.component.tcszzkhxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TcszzkhxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM = '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }

    /**
     * 获取客户信息下拉列表数据
     * @return
     */
    public Map<String,Object> searchKhxxComboGridData(){
        String sql = "select * from t_zz_khxx where 1=1 "+ defaultCode();
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public Map<String,Object> searchKhxxByKhbh(String khbh){
        String sql =  "select * from t_zz_khxx where khbh=? "+ defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{khbh});
    }
}
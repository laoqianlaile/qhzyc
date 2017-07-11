package com.ces.component.zzccbzcpxx.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZzccbzcpxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object getBzcpxx(String traceCode) {
        //String sql = "select t.* from t_zz_bzgl t where t.id = (select pid from t_zz_bzgldymx where cpzsm = ? and is_out <> '1')";
        //String zlSql = "select sum(djzl) as zl from t_Zz_Bzglplxx t where id = (select pid from t_zz_bzgldymx where cpzsm = ?)";
        String sql="select * from t_zz_bzgl where cpzsm = '"+traceCode+"'";
        //Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{traceCode});
        //Map<String,Object> zlMap = DatabaseHandlerDao.getInstance().queryForMap(zlSql,new Object[]{traceCode});
        //map.put("ZL",zlMap.get("ZL"));
        Map<String,Object> map=DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }

    public Object getBzxx(String id){
        String sql="select * from t_zz_bzgl where id = '"+id+"'";
        Map<String,Object> listmap=new HashMap<String,Object>();
        listmap=DatabaseHandlerDao.getInstance().queryForMap(sql);
        return listmap;
    }

    public Object getBzjs(String cpzsm){
        StringBuilder sql = new StringBuilder("select kcjs from t_zz_bzgl t where t.cpzsm = '" + cpzsm + "' and t.is_delete <> '1'");
        Map<String,Object> resultMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
        return Double.parseDouble(String.valueOf(resultMap.get("KCJS")) == "" ? "0":String.valueOf(resultMap.get("KCJS")));
    }

}
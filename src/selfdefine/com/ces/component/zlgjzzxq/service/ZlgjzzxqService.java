package com.ces.component.zlgjzzxq.service;

import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wngyu on 15/11/24.
 */
@Component
public class ZlgjzzxqService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    public Object getScdaxx(String id){
        String sql1="select scdabh,cssj,pl,pz from t_zz_csgl where id = (select pid from t_zz_csnzwxq where pch = (select pch  from v_zlzz_tpzz where id= '"+id+"'))";
        String sql ="select scdabh,cssj,pl,pz from t_zz_csgl where cslsh in  (select distinct cslsh from t_zz_bzglplxx where pid = (  select id from t_zz_bzgl where cpzsm =( select cpzsm from v_zlzz_tpzz where id= '"+id+"')))";
        List<Map<String,Object>> listMap=new ArrayList<Map<String,Object>>();
        listMap= DatabaseHandlerDao.getInstance().queryForMaps(sql1);
        int i=1;
//        if(){}
        //1001000103201502
        //返回生产档案id或档案编号,以及采收pz
        return listMap;
    }

    public Object getScdaid(String id){
        String sql="select id from t_zz_scda where scdabh = (select scdabh from v_zlzz_tpzz where id = '"+id+"')";
        Map<String,Object> map=new HashMap<String,Object>();
        map=DatabaseHandlerDao.getInstance().queryForMap(sql);
        int i=1;
        return map;
    }
}
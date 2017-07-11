package com.ces.component.sczzddtj.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.List;
import java.util.Map;

@Component
public class SczzddtjService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 获取客户类型
     * @return
     */
    public Object getKhlx(){

        String sql="select SJMC from T_COMMON_SJLX_CODE where lxbm='KHLX'";
        List<Map<String,Object>> maps= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }

    /**
     *获取产品名称
     * @return
     */
    public Object getCpmc(){
        String qybm= SerialNumberUtil.getInstance().getCompanyCode();
       String sql = "select distinct cpmc from t_zz_cpxxgl where is_delete <> '1' and qybm = '"+qybm+"'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }

}
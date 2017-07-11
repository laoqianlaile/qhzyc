package com.ces.component.zzgysxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZzgysxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    /**
     * 保证供应商名称不能重复
     * @param gysmc 供应商名称
     * @param id id
     * @return
     */
    public Object checkGYSMC(String gysmc,String id){
        String qybm= SerialNumberUtil.getInstance().getCompanyCode();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String sql = "";
        if("".equals(String.valueOf(id))){
            sql="select count(t.gysmc) as GYSMC from T_ZZ_GYSXX t where t.is_delete <> '1' and qybm='"+qybm+"' and t.gysmc='"+gysmc+"'";
        }else{
            sql="select count(t.gysmc) as GYSMC from T_ZZ_GYSXX t where t.is_delete <> '1' and qybm='"+qybm+"' and t.gysmc='"+gysmc+"' and id != '" + id + "'";
        }
        dataMap= DatabaseHandlerDao.getInstance().queryForMap(sql);
        if(!"0".equals(String.valueOf(dataMap.get("GYSMC")))){
            dataMap.put("msg","请输入其它供应商名称！");
            dataMap.put("result","ERROR");
        }
        return dataMap;

    }

    @Transactional
    public Object getGyslx(){
        StringBuilder sql = new StringBuilder("select t.SJMC from t_common_sjlx_code t where t.lxbm = 'GYSLX' order by t.SJBM");
        List<Map<String, Object>> listData = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        return listData;
    }

    @Transactional
    public Object getGyslxById(String id){
        StringBuilder sql = new StringBuilder("SELECT GYSLX FROM T_ZZ_GYSXX T WHERE T.ID = '" + id + "'");
        Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
        return dataMap;
    }

    
}
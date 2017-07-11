package com.ces.component.zztrpjbxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.Map;

@Component
public class ZztrpjbxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 设置投入品名称不能重复的方法
     * @return
     */
    public Object checkTRPMC(String trpmc,String id){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String sql="select count(t.TRPMC) as TRPMC from T_ZZ_TRPJBXX t where t.is_delete <> '1' and qybm='"+qybm+"' and t.TRPMC='"+trpmc+"'";
        if(!"".equals(id)){
            sql="select count(t.TRPMC) as TRPMC from T_ZZ_TRPJBXX t where t.is_delete <> '1' and qybm='"+qybm+"' and id <> '"+id+"' and t.TRPMC='"+trpmc+"'";
        }
        dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if(!"0".equals(String.valueOf(dataMap.get("TRPMC")))){
            dataMap.put("msg","请选择其它投入品名称！");
            dataMap.put("result","ERROR");
        }
        return dataMap;
    }

    /**
     *确定同一个通用名不能对应2个类型。
     * @param tym 通用名
     * @param lx 类型
     * @return
     */
    public Object checkTYM(String tym,String lx,String id){
        String qybm=SerialNumberUtil.getInstance().getCompanyCode();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String sql = "";
        if("".equals(String.valueOf(id))){
            sql="select count(t.tym) as TYM from T_ZZ_TRPJBXX t where t.is_delete <> '1' and qybm='"+qybm+"' and t.lx <> '"+lx+"' and t.tym='"+tym+"'";
        }else{
            sql="select count(t.tym) as TYM from T_ZZ_TRPJBXX t where t.is_delete <> '1' and qybm='"+qybm+"' and t.lx <> '"+lx+"' and t.tym='"+tym+"' and id != '" + id + "'";
        }
        dataMap=DatabaseHandlerDao.getInstance().queryForMap(sql);
        if(!"0".equals(String.valueOf(dataMap.get("TYM")))){
            dataMap.put("msg","请选择其它通用名！");
            dataMap.put("result","ERROR");
        }
        return dataMap;

    }



    
}
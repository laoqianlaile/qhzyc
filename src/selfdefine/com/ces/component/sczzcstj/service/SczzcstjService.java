package com.ces.component.sczzcstj.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.List;
import java.util.Map;

@Component
public class SczzcstjService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    String qybm = SerialNumberUtil.getInstance().getCompanyCode();

    /**
     *获取品种信息
     * @return
     */
    public Object getPzxx(){
        String sql = "select distinct pz from t_zz_xpzxx t where t.qybm = ? and t.is_delete <> '1'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{qybm});
        return maps;
    }

    /**
     *获取区域名称
     * @return
     */
    public Object getQymc(){
        String sql = "select distinct qymc from t_zz_qyxx t where t.qybm = ? and t.is_delete <> '1'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{qybm});
        return maps;
    }

    /**
     * 获取地块编号
     * @return
     */
    public Object getDkbh(){
        String sql= "select distinct dkbh from t_zz_dkxx t where t.qybm = ? and t.is_delete <> '1'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{qybm});
        return maps;
    }

    
}
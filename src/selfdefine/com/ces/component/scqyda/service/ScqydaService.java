package com.ces.component.scqyda.service;

import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class ScqydaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Map<String , Object> getQyda(){
        String qybm = qybm();
        String qymc = qymc();
        String sql = "select t.* from  t_sc_qyda t where t.qybm="+qybm+" and t.qymc='"+qymc+"' ";
        Map<String , Object> map=DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }
    public String qybm(){
        return SerialNumberUtil.getInstance().getCompanyCode();
    }
    public String qymc(){
        return CompanyInfoUtil.getInstance().getCompanyName("SC", SerialNumberUtil.getInstance().getCompanyCode());
    }
}
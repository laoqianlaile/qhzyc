package com.ces.component.qyda.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.qyda.dao.QydaDao;
import com.ces.component.qyda.entity.QydaEntity;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.menu.MenuDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;

@Component("qydaService")
public class QydaService extends TraceShowModuleDefineDaoService<QydaEntity, QydaDao> {

    public String getIdByQybm(){
    	String qybm = SerialNumberUtil.getInstance().getCompanyCode();
    	return getDao().getIdByQybm(qybm);
    }
    
    public Object getQyda(String prefix){
    	String qybm = SerialNumberUtil.getInstance().getCompanyCode();
    	String pfscmc = CompanyInfoUtil.getInstance().getCompanyName(prefix, qybm);
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("pfscbm", qybm);
    	map.put("pfscmc", pfscmc);
    	return map;
    }
}

package com.ces.component.prqyda.service;

import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class PrqydaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object getQydaInfo(String qybm){
    	String sql = "select T.ID,T.QYBM,T.QYMC,T.GSZCDJZH,T.BARQ,T.FRDB,T.JYDZ,T.LXDH,T.CZ,T.LSXZQHDM from T_PR_QYDA T where T.QYBM ='"+qybm+"'";
    	return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }
}
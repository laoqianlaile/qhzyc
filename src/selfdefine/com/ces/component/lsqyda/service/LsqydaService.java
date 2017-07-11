package com.ces.component.lsqyda.service;

import org.springframework.stereotype.Component;

import com.ces.component.lsqyda.dao.LsqydaDao;
import com.ces.component.lsqyda.entity.LsqydaEntity;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class LsqydaService extends TraceShowModuleDefineDaoService<StringIDEntity, LsqydaDao> {


    public LsqydaEntity getByQybm(String qybm) {
        return getDao().getByQybm(qybm);
//	    	return new QydaEntity();
    }
}

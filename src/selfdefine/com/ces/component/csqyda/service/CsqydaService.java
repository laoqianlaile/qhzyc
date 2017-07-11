package com.ces.component.csqyda.service;

import org.springframework.stereotype.Component;

import com.ces.component.csqyda.dao.CsqydaDao;
import com.ces.component.csqyda.entity.CsqydaEntity;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class CsqydaService extends TraceShowModuleDefineDaoService<StringIDEntity, CsqydaDao> {

    public CsqydaEntity getByQybm(String zzjgdm){
    	return getDao().getByQybm(zzjgdm);
//    	return new QydaEntity();
    }
    
}

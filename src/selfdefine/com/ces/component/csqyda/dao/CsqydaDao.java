package com.ces.component.csqyda.dao;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.csqyda.entity.CsqydaEntity;
import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface CsqydaDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	@Query
	public CsqydaEntity getByQybm(String zzjgdm);

}

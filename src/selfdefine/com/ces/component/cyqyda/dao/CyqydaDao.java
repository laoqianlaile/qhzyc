package com.ces.component.cyqyda.dao;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface CyqydaDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	@Query(value="select T.ID from T_CY_QYDA T where T.QYBM = ?",nativeQuery=true)
	public String getIdByQybm(String qybm); 
}

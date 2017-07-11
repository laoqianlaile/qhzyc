package com.ces.component.ttqyda.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface TtqydaDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	@Query(value="select T.ID from T_TT_QYDA T where T.QYBM = ?",nativeQuery=true)
	public String getIdByQybm(String qybm); 
}

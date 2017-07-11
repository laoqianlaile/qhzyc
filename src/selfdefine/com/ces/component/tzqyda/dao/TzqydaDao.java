package com.ces.component.tzqyda.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface TzqydaDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	
	@Query(value="select T.ID from T_TZ_QYDA T where T.QYBM=:qybm",nativeQuery=true)
	public String getIdByQybm(@Param("qybm")String qybm); 
	
	@Query(value="select T.QYMC,T.QYBM from T_TZ_QYDA T where T.QYBM=:qybm",nativeQuery=true)
	public Object[] getQyda(@Param("qybm")String qybm);
}

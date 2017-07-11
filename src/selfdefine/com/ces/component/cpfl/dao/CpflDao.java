package com.ces.component.cpfl.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface CpflDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	@Query(value="select T.spmc from T_COMMON_SCSPXX T where T.spbm=:spbm",nativeQuery=true)
	public String getSpmc(@Param("spbm")String spbm);
}

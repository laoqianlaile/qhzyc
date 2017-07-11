package com.ces.component.tzjyzda.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface TzjyzdaDao extends TraceShowModuleStringIDDao<StringIDEntity> {

	@Query(value="select T.SPDDD from T_TZ_JYZDA T where T.JYZBM=:jyzbh and rownum<=1",nativeQuery=true)
	public String getDddByJyzbh(@Param("jyzbh")String jyzbh);
	
}

package com.ces.component.tzszjyxx.dao;

import java.math.BigDecimal;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface TzszjyxxDao extends TraceShowModuleStringIDDao<StringIDEntity> {

	@Query(value="select T.HZMC,T.HZBM,T.JYZJCSL,T.JCPCH from T_TZ_SZJCXX T where T.SZCDJYZH=:szcdjyzh and rownum <= 1",nativeQuery=true)
	public Object[] getSzjcxx(@Param("szcdjyzh")String szcdjyzh);
	
}
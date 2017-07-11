package com.ces.component.tzjyxxxz.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface TzjyxxxzDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	
	@Query(value="select T.HZMC,T.HZBM,T.JCPCH from T_TZ_RPJYJYXX T where T.SZCDJYZH=:szcdjyzh and rownum <= 1",nativeQuery=true)
	public Object[] getRpjyxx(@Param("szcdjyzh")String szcdjyzh);
	
}

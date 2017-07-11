package com.ces.component.tzrpjyjyxz.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface TzrpjyjyxzDao extends TraceShowModuleStringIDDao<StringIDEntity> {

	@Query(value="select T.HZMC,T.HZBM,P.SJJCSL,T.JCPCH from T_TZ_SZJYXX T,T_TZ_SZJCXX P where T.SZCDJYZH=:szcdjyzh and T.SZCDJYZH = P.SZCDJYZH and rownum <= 1",nativeQuery=true)
	public Object[] getSzjyxx(@Param("szcdjyzh")String szcdjyzh);
	
}

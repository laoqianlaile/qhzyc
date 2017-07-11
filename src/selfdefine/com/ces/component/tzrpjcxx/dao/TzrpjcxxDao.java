package com.ces.component.tzrpjcxx.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface TzrpjcxxDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	@Query(value="select T.HZMC,T.HZBM from T_TZ_SZJYXX T where T.SZCDJYZH=:szcdjyzh and rownum <= 1",nativeQuery=true)
	public Object[] getSzjyxx(@Param("szcdjyzh")String szcdjyzh);
}

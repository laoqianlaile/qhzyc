package com.ces.component.zznyda.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface ZznydaDao extends TraceShowModuleStringIDDao<StringIDEntity> {

	@Query(value = "select T.NCQ  from T_ZZ_NYDA t where t.QYBM=:qybm and t.NYBH=:nybh",nativeQuery=true)
	public Object getNcqData(@Param("qybm")String qybm,@Param("nybh")String nybh);
}

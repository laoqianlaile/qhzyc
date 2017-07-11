package com.ces.component.lsjcxxxz.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface LsjcxxxzDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	@Query(value = "select T.ID from T_LS_SCJCXX T where T.SCJCBH =?",nativeQuery = true)
	public String getIdByJcbh(String jcbh);
	
	@Query(value = "select t.spbm,t.spmc from t_common_scspxx t",nativeQuery = true)
	public List getAllSpmc();

}

package com.ces.component.ttscjcxx.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface TtscjcxxDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	@Query(value = "select t.spbm,t.spmc from t_common_scspxx t",nativeQuery = true)
	public List getAllSpmc();

}

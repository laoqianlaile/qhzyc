package com.ces.component.csrpjc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface CsrpjcDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	@Query(value = "select t.spbm,t.spmc from t_common_rpspxx t",nativeQuery = true)
	public List getAllRpmc();
}

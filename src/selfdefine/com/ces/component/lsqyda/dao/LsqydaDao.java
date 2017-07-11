package com.ces.component.lsqyda.dao;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.lsqyda.entity.LsqydaEntity;
import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface LsqydaDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	/**
	 * 
	 * @param zzjgdm 企业组织机构代码
	 * @return StringIDEntity 返回类型
	 */
	@Query
	public LsqydaEntity getByQybm(String qybm);
}

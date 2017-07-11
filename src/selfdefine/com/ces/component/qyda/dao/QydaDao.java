package com.ces.component.qyda.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.qyda.entity.QydaEntity;
import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;

public interface QydaDao extends TraceShowModuleStringIDDao<QydaEntity> {
	
	/**
	 * 
	 * @param zzjgdm 企业组织机构代码
	 * @return QydaEntity 返回类型
	 */
	@Query
	public QydaEntity getByZzjgdm(String zzjgdm);
	
	@Query(value="select T.ID from T_PC_QYDA T where T.QYBM=:qybm",nativeQuery=true)
	public String getIdByQybm(@Param("qybm")String qybm); 
	
}

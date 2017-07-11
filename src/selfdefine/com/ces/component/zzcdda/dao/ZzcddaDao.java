package com.ces.component.zzcdda.dao;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.zzcdda.entity.ZzcddaEntity;
import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;

public interface ZzcddaDao extends TraceShowModuleStringIDDao<ZzcddaEntity> {

	
	/**
	 * 
	 * 根据登录者账户编码获得种植企业信息
	 * @param zhbh 账号编码
	 * @return CddaEntity
	 */
	@Query
	public ZzcddaEntity getByQybm(String qybm);
}

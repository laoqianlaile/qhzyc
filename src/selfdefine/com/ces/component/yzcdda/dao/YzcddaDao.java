package com.ces.component.yzcdda.dao;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.yzcdda.entity.YzcddaEntity;
import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;

public interface YzcddaDao extends TraceShowModuleStringIDDao<YzcddaEntity> {
	/**
	 * 
	 * 根据登录者账户编码获得种植企业信息
	 * @param zhbh 账号编码
	 * @return CddaEntity
	 */
	@Query
	public YzcddaEntity getByQybm(String qybm);
}

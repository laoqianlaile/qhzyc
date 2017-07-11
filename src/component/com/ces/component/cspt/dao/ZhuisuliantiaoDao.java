package com.ces.component.cspt.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
@Component
public interface ZhuisuliantiaoDao extends StringIDDao<TCsptZsxxEntity> {
	/**
	 * 正向追溯DAO
	 * @param jhpch 进货批次号
	 * @param sql  拼接SQL
	 * @return
	 */
	@Query(value="select t.* from T_CSPT_JYXX t start with t.zsm = :zsm connect by t.zsm = prior t.jypzh", nativeQuery = true)
	public List<Map<String,Object>> getFxZhuiSuLianTiao(@Param("zsm")String zsm);
	@Query(value="SELECT T.* FROM T_CSPT_JYXX T WHERE T.REFID = :REFID", nativeQuery = true)
	public TCsptZsxxEntity getTCsptZsxxByRefId(@Param("REFID")String refId);
}

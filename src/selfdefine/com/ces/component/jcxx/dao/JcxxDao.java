package com.ces.component.jcxx.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface JcxxDao extends TraceShowModuleStringIDDao<StringIDEntity> {

	@Query(value = "select T.JCLHBH,T.ID from T_PC_JCLHXX T where T.PFSBM=:pfsbm order by JCLHBH asc", nativeQuery = true)
	public List<Object[]> getPfsbm(@Param("pfsbm")
	String pfsbm);

	@Query(value = "select T.ID from T_PC_JCLHXX T where T.JCLHBH =:jclhbh",nativeQuery = true)
	public List<Object[]> getIdByJclhbh(@Param("jclhbh")String jclhbh);
	
	@Query(value = "select T.SPMC,T.SPBM from T_PC_JCLHMXXX T where T.pid=(select T.ID from T_PC_JCLHXX T where T.JCLHBH=:jhpch) order by T.SPBM asc", nativeQuery = true)
	public List<Object[]> getJhpch(@Param("jhpch")
	String jhpch);
	
	@Query(value = "select T.JCLHBH from T_PC_JCLHXX T where T.JCLHBH=:jhpch order by T.JCLHBH asc", nativeQuery = true)
	public Object getJypzh(@Param("jhpch")
	String jhpch);

}

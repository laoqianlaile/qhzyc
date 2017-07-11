package com.ces.component.jinchangxinxi.dao;

import org.springframework.data.jpa.repository.Query;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface JinchangxinxiDao extends TraceShowModuleStringIDDao<StringIDEntity> {

	/**
     * <p>标题: getJcsfbhSequence</p>
     * @return Integer 返回类型
     */
	@Query(value="select JCSFBH_SEQUENCE.nextval from DUAL",nativeQuery=true)
    public Object getJcsfbhSequence();
	
	/**
     * <p>标题: getQybm</p>
     * @return Integer 返回类型
     */
	@Query(value="select max(QYBM) from T_PC_QYDA",nativeQuery=true)
    public Object getQybm();
}

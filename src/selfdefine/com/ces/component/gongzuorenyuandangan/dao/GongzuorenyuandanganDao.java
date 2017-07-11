package com.ces.component.gongzuorenyuandangan.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.ces.component.gongzuorenyuandangan.entity.GongzuorenyuandanganEntity;
import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;

@Component
public interface GongzuorenyuandanganDao extends TraceShowModuleStringIDDao<GongzuorenyuandanganEntity> {
	
	/**
     * <p>标题: getMaxYgbm</p>
     * @return String 返回类型
     */
	@Query(value="select max(GZRYBH) from T_PC_GZRYDA",nativeQuery=true)
    public Object getMaxYgbm(); 
	
//	@Query("select XX from GongzuorenyuandanganEntity g where g.name =?1 and g.id=?2")
//	public List<GongzuorenyuandanganEntity> findA(String name, String id);
	
}

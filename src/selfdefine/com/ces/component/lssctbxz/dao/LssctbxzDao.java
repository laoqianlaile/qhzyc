package com.ces.component.lssctbxz.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface LssctbxzDao extends TraceShowModuleStringIDDao<StringIDEntity> {
	
	//根据蔬菜进场编号获得商品名称
	@Query(value = "select T.SPMC,T.SPBM from T_LS_SCJCMXXX T where PID = (select J.ID from T_LS_SCJCXX J where J.SCJCBH =?)" , nativeQuery = true)
	public List<Object[]> getSpmcByLhbh(String jhbh);
	
	//获得所有商品名称
	@Query(value = "select t.spmc,t.spbm from t_common_scspxx t order by t.spbm" , nativeQuery = true)
	public List<Object[]> getAllSpmc();
	
	//获取所有状态开启的经营者
	@Query(value = "select t.jyzmc,t.jyzbm from t_ls_jyzda t where t.zt = '1'" , nativeQuery = true)
	public List<Object[]> getJyzByZt();
	
	//获取所有进场编号
	@Query(value = "select t.scjcbh,t.id from t_ls_scjcxx t" , nativeQuery = true)
	public List<Object[]> getAllScjcbh();
	
	//根据经营者编码获取进场编号
	@Query(value = "select t.scjcbh,t.id from t_ls_scjcxx t where t.pfsbm = ?" , nativeQuery = true)
	public List<Object[]> getScjcbhByJyzbm(String jyzbm);
	
	//根据蔬菜进场理货编号获得Id
	@Query(value = "select t.id from t_ls_scjcxx t where t.scjcbh = ?" , nativeQuery = true)
	public String getIdByScjcbh(String scjcbh);

}

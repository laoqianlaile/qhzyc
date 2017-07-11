package com.ces.component.jiaoyixinxixinzeng.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface JiaoyixinxixinzengDao extends TraceShowModuleStringIDDao<StringIDEntity> {
//	@Query(value = "Select LSH from T_PC_LSHBM where ZL = :zl and QZ = :qz",nativeQuery=true)
//    public Object[] getLsh(@Param("zl")String zl,@Param("qz")String qz);
//	
//	@Query(value = "insert into T_PC_LSHBM (ZL,QZ,CD,LSH) values (?1,?2,?3,0)",nativeQuery=true)
//	public void creatLsh(String zl,String qz,String cd);
//	
//	@Query(value = "update T_PC_LSHBM set LSH = ?1",nativeQuery = true)
//	public void updateLsh(int lsh);
	
	@Query(value = "select T.ID from T_PC_JCLHXX T where T.JCLHBH =:jclhbh",nativeQuery = true)
	public List<Object[]> getIdByJclhbh(@Param("jclhbh")String jclhbh);
	
	@Query(value = "select T.JCLHBH,T.ID from T_PC_JCLHXX T where T.PFSBM =:pfsbm" , nativeQuery = true)
	public List<Object[]> getJclhbhByPfsbm(@Param("pfsbm")String pfsbm);
	
	@Query(value = "select T.SPMC,T.SPBM from T_PC_JCLHMXXX T where PID = (select T.ID from T_PC_JCLHXX T where T.JCLHBH =:jclhbh)" , nativeQuery = true)
	public List<Object[]> getSpmcByJclhbh(@Param("jclhbh")String jclhbh);
	
	@Query(value = "select T.JYPZH,T.JHPCH from T_PC_JCLHMXXX T where SPBM =:spbm and PID = (select T.ID from T_PC_JCLHXX T where JCLHBH =:jclhbh)" , nativeQuery = true)
	public List<Object[]> getInfoByJclhbh(@Param("spbm")String spbm,@Param("jclhbh")String jclhbh);
	
	@Query(value = "select T.SPDDD from T_PC_JYZDA T where JYZBM =:lssbm",nativeQuery = true)
	public String getDddByLssbm(@Param("lssbm")String lssbm);
	
//	@Query(value = "select T.* from T_PC_JCLHMXXX T where T.PID=:jclhid and T.SPBM=:spbm and rownum=1",nativeQuery = true)
//	public Object[] getJhpch(@Param("jclhid")String jclhid,@Param("spbm")String spbm);
	
}
package com.ces.component.qyptqtxxlb.service;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class QyptqtxxlbService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	@PersistenceContext
	private EntityManager entityManager;
	@Override
	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
	    // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
	    // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
		return AppDefineUtil.RELATION_AND + "ZHBH = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
	}
    
	/**
	 * 修改消息为已阅读
	 * @param id
	 * @return
	 */
	public boolean changeYdFlag(String id){
		String sql = "UPDATE T_QYPT_XXLB SET YD='1' WHERE ID='"+id+"' ";
		try{
			Query query = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class);
			query.executeUpdate();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("修改消息状态:"+e.getMessage());
			return false;
		}
	}
}
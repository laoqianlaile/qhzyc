package com.ces.component.qyptqtzh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.facade.OrgInfoFacade;
import ces.sdk.system.facade.UserInfoFacade;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.service.StringIDDefineDaoService;
@Service
public class QyptqtzhService extends StringIDDefineDaoService<StringIDEntity, TraceShowModuleDao>{
	@PersistenceContext
	private EntityManager entitymanager;
	
	@Autowired
	@Override
    public void setDao(TraceShowModuleDao dao){
		super.setDao(dao);
	}
	
	/**
	 * 获取企业账户信息
	 * @param zhbh
	 * @return
	 */
	public Map<String, Object> getEnterInfo(String zhbh) {
		OrgInfoFacade orgInfoFacade = FacadeUtil.getOrgInfoFacade();
		UserInfoFacade userInfoFacade = FacadeUtil.getUserInfoFacade();
		Map<String, Object> mapValue = new HashMap<String, Object>();
		//获取企业账户基本信息
		String sql1 = "SELECT T.QYBM,T.QYMC,T.YYZZ,T.LXR,T.YX,T.SJ,T.ZJ,T.CZ,T.DZ,T.SJYX,T.SXSJ,T.ZHSHSJ,T.ZHCJSJ,T.AUTH_ID FROM T_QYPT_ZHGL T" +
				" WHERE t.zhbh='" + zhbh + "'";
		List<Map<String, Object>> list = querySQLForObject(sql1);
		if (list.size() > 0) {
			mapValue = list.get(0);
		}
		String sql2 = "SELECT  G.FWMC,J.QYSJ,J.DQSJ " +
				" FROM T_QYPT_JBFWXX J, t_qypt_fwgl g" +
				" WHERE j.zhbh = '" + zhbh + "' and g.fwbh = j.fwbh and j.sfdqfw='1'";
		List<Map<String, Object>> jbfwList = querySQLForObject(sql2);
		if (jbfwList.size() > 0) {
			mapValue.put("jbfw", jbfwList.get(0));
		}
		//获取企业增值服务
		String sql3 = "SELECT G.FWMC,T.ZZFWZ,G.GG from T_QYPT_ZZFWXX t,t_qypt_fwgl g where g.fwbh=t.zzfwbh and t.zhbh = '" + zhbh + "'";
		List zzfwList = querySQLForObject(sql3);
		if (zzfwList.size() > 0) {
			mapValue.put("zzfwList", zzfwList);
		}
		//获取企业用户数
		int userNum = 0;
		String authId = String.valueOf(mapValue.get("AUTH_ID"));
		userNum += userInfoFacade.findUsersByOrgId(authId).size();
		List<OrgInfo> childsOrgInfos = orgInfoFacade.findChildsByParentId(authId);
		for (OrgInfo childsOrgInfo : childsOrgInfos) {
			userNum += userInfoFacade.findUsersByOrgId(childsOrgInfo.getId()).size();
		}
		mapValue.put("DQYHS", userNum);
		return mapValue;
	}
	/**
	 * 根据SQL查询
	 * @param sql
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> querySQL(String sql){
		SQLQuery query = entitymanager.createNativeQuery(sql).unwrap(SQLQuery.class);
		List<Map<String,String>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	/**
	 * @param sql
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> querySQLForObject(String sql){
		SQLQuery query = entitymanager.createNativeQuery(sql).unwrap(SQLQuery.class);
		List<Map<String,Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
}
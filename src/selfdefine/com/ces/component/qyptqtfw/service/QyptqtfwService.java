package com.ces.component.qyptqtfw.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.service.StringIDDefineDaoService;
@Service
public class QyptqtfwService extends StringIDDefineDaoService<StringIDEntity, TraceShowModuleDao>{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	@Override
    public void setDao(TraceShowModuleDao dao){
		super.setDao(dao);
	}
	
	/**
	 * 获取企业基本信息
	 * @param zhbh 企业编码（企业编号）
	 * @return
	 */
	public Map<String,String> getZhxx(String zhbh){
		String sql = "select t.* from T_QYPT_ZHGL t WHERE T.ZHBH = '"+zhbh+"'";
		if(querySQL(sql).size()>0){
			Map<String, String> map = (Map<String, String>) querySQL(sql).get(0);
			return map;
		}else{
			return null;
		}
	}
	/**
	 * 获取企业基本服务
	 * @param zhbh 企业编号
	 * @return
	 */
	public Map<String,String> getJbfw(String zhbh){
		String sql = "SELECT G.FWMC,G.FWNR,T.QYSJ,T.DQSJ FROM T_QYPT_JBFWXX T,T_QYPT_FWGL G WHERE G.FWBH=T.FWBH AND T.SFDQFW = '1' AND T.ZHBH = '"+zhbh+"'";
		List<Map<String, String>> maps = querySQL(sql);
		if(maps.size()>0){
			Map<String, String> data = new HashMap<String, String>();
			StringBuffer fwmc = new StringBuffer();
			StringBuffer fwnr = new StringBuffer();
			for (Map<String, String> map : maps) {
				fwmc.append(map.get("FWMC")).append(",");
				fwnr.append(map.get("FWNR")).append(",");
				data.put("QYSJ", map.get("QYSJ"));
				data.put("DQSJ", map.get("DQSJ"));
			}
			if (fwmc.length() > 0) {
				fwmc.deleteCharAt(fwmc.length() - 1);
			}
			if (fwnr.length() > 0) {
				fwnr.deleteCharAt(fwnr.length() - 1);
			}
			data.put("FWMC", fwmc.toString());
			data.put("FWNR", fwnr.toString());
			return data;
		}else{
			return new HashMap<String, String>();
		}
	}
	/**
	 * 获取企业基本服务名称
	 * @param zhbh
	 * @return
	 */
	public String getJbfwmc(String zhbh){
		String sql = "SELECT G.FWMC FROM T_QYPT_JBFWXX T,T_QYPT_FWGL G WHERE G.FWBH=T.FWBH AND T.SFDQFW = '1' AND T.ZHBH = '"+zhbh+"'";
		List<Map<String, String>> maps = querySQL(sql);
		if(maps.size()>0){
			StringBuffer fwmc = new StringBuffer();
			for (Map<String, String> map : maps) {
				fwmc.append(map.get("FWMC")).append(",");
			}
			if (fwmc.length() > 0) {
				fwmc.deleteCharAt(fwmc.length() - 1);
			}
			return fwmc.toString();
		}else{
			return "";
		}
	}
	/**
	 * 获取企业增值服务列表
	 * @param pageRequest
	 * @param zhbh
	 * @return
	 */
	public Page getZzfwPage(PageRequest pageRequest,String zhbh){
		String sql = "SELECT G.FWMC,G.FWNR,G.GG,T.ZZFWZ,G.CREATE_TIME FROM T_QYPT_ZZFWXX T,T_QYPT_FWGL G WHERE T.ZZFWBH = G.FWBH AND T.SFDQFW = '1' AND T.ZHBH='"+zhbh+"'";
		return queryPage(pageRequest,sql);
	}
	/*public Page getZzfwPage(String qybh){
		String sql = "SELECT G.FWMC,G.FWNR,G.GG,T.ZZFWZ,G.CREATE_TIME FROM T_QYPT_ZZFWXX T,T_QYPT_FWGL G WHERE T.ZZFWBH =  G.FWBH AND T.QYBH=?";
		JdbcDataUtil.
	}*/
	
	/**
	 * 服务申请
	 * @param zhbh 账户编号
	 * @param sqsm 申请说明
	 */
	public void createFwsq(String zhbh,String sqsm,String sqlx,String xdsj,String fwlx,String fwmc){
		Map<String,String> qyMap = getZhxx(zhbh);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into t_qypt_fwsqgl (ID,CREATE_USER,CREATE_TIME,QYMC,SQSM,SQSJ,SHZT,ZHBH,QYBH,SQLX,XDSJ,FWSQBH,FWLX,FWMC) " +
				"values(sys_guid(),'"+qyMap.get("ID")+"', +'"+df.format(new Date())+"', '"+qyMap.get("QYMC")+
				"', '"+sqsm+"','"+ df.format(new Date())+"','1','"+qyMap.get("ZHBH")+"','"+qyMap.get("QYBM")+
				"','"+sqlx+"','"+xdsj+"','"+System.currentTimeMillis()+"','"+fwlx+"','"+fwmc+"')";
		SQLQuery query = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class);
		query.executeUpdate();
	}
	/**
	 * 获取企业服务
	 * @return
	 */
	public List<Map<String,String>> getZzfwList(){
		String sql = "select t.fwmc from T_QYPT_FWGL t where t.qyzt='1' and t.fwlx='2'";
		return querySQL(sql);
	}
	
	/**
	 * 根据SQL查询
	 * @param sql
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> querySQL(String sql){
		SQLQuery query = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class);
		List<Map<String,String>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	/**
	 * @param sql
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> querySQLForObject(String sql){
		SQLQuery query = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class);
		List<Map<String,Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	/**
	 * 分页查询
	 * @param pageRequest
	 * @param sql
	 * @return Object
	 */
	public Page queryPage(PageRequest pageRequest,String sql) {
		//查总数
		String count = "select count(*) as count from ("+sql+")";
		Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
		//总数
		  long total = Long.parseLong( map.get("COUNT").toString());
		  int begin = pageRequest.getOffset();
	      int end   = begin + pageRequest.getPageSize();
	      if (begin > total) {
	            int remainder = (int) (total%pageRequest.getPageSize());
	            end = (int) total;
	            begin = (int) (total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
	        }

		List<Map<String, Object>> content = DatabaseHandlerDao.getInstance().pageMaps(sql, begin, end);
		return new PageImpl<Map<String, Object>>(content, pageRequest, total);
	}
}
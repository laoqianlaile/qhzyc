package com.ces.component.trace.utils;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.xarch.core.web.listener.XarchListener;
import org.apache.struts2.ServletActionContext;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class SerialNumberUtil {

	@PersistenceContext
	private EntityManager entityManager;

	public static SerialNumberUtil getInstance() {
		return XarchListener.getBean(SerialNumberUtil.class);
	}

	/**
	 * <p>
	 * 描述: 取当前登录人员的企业主体码
	 * </p>
	 *
	 * @return String 返回类型
	 * @throws
	 */
	public String getCompanyCode() {
		//先从session里取，如没有从request里取
		if (ServletActionContext.getRequest().getSession().getAttribute("_companyCode_") != null) {
			return (String) ServletActionContext.getRequest().getSession().getAttribute("_companyCode_");
		} else if (ServletActionContext.getRequest().getParameter("companyCode") != null) {
			return ServletActionContext.getRequest().getParameter("companyCode");
		}
		throw new RuntimeException("没有企业编码");
	}

	/**
	 * 获取企业平台-账户管理表中id
	 *
	 * @return
	 */
	public String getIdByCompanyCode(){
		String qybm = getCompanyCode();
		String sql = "select t.id from t_qypt_zhgl t where t.zhbh = '"+qybm+"'";
		Map<String,Object> zhidMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
		return zhidMap.get("ID").toString();
	}

	/**
	 * 获取账户对应authId
	 * @return authId
	 */
	public String getAuthId(){
		String sql = "SELECT T.AUTH_ID FROM T_QYPT_ZHGL T WHERE T.ZHBH = ?";
		Map map = DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{getCompanyCode()});
		return map.get("AUTH_ID").toString();
	}

	/**
	 * 获取流水号编码
	 * @param sysName
	 * @param type
	 * @param needPrefix
	 * @return
	 */
	@Transactional
	public String getSerialNumber(String sysName, String type, boolean needPrefix) {
		return getSerialNumber(sysName, getCompanyCode(), type, needPrefix);
	}
	@Transactional
	public String getSerialNumberByQybm(String sysName,String qybm, String type, boolean needPrefix) {
		return getSerialNumber(sysName, qybm, type, needPrefix);
	}
	public String getSerialNumber(String sysName,String type,boolean needPrefix,boolean isCommon){
		if(isCommon==true){
			return getSerialNumber(sysName, "COMMON", type, needPrefix);
		}else{
			return getSerialNumber(sysName, getCompanyCode(), type, needPrefix);
		}
	}

	/**
	 * 获取流水号编码（使用存储过程）
	 * @param sysName
	 * @param prefix
	 * @param type
	 * @param needPrefix
	 * @return
	 */
	public String getSerialNumber(String sysName, String prefix, String type, boolean needPrefix) {
		String tableName = TableNameUtil.getLshbmTableName(sysName);
		String sql = "select * from PRO_T_LSHBH('"+tableName+"','"+prefix+"','"+type+"')";
//		CallableStatement stmt = null;
//		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
//		Connection conn = session.connection();
		try {
//			stmt = conn.craecreateStatement(); //调用存储过程（传参）
//			stmt.setString(1, tableName);
//			stmt.setString(2, prefix);
//			stmt.setString(3, type);
//			stmt.registerOutParameter(4, Types.VARCHAR);
//			stmt.registerOutParameter(5, Types.VARCHAR);
//			stmt.execute();

			Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);

			String cd = String.valueOf( map.get("LSH_CD"));//与参数顺序一致
			String value = String.valueOf( map.get("LSH_NUMBER"));//与参数顺序一致
			if (needPrefix) {
				return prefix + String.format("%0" + cd + "d", Integer.parseInt(value));
			} else {
				return String.format("%0" + cd + "d", Integer.parseInt(value));
			}
		} catch (Exception e) {
			throw new RuntimeException("获取流水号编码错误" + e.getMessage());
		}
	}



	public String getStringDate(Date data){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String str=sdf.format(data).substring(2);
		return str;

	}
	/**
	 @Transactional
	 private String getSerialNumberOld(String sysName, String prefix, String type, boolean needPrefix) {
	 String tableName = TableNameUtil.getLshbmTableName(sysName);
	 check(tableName, prefix, type);
	 DatabaseHandlerDao.getInstance().executeSql(
	 "update " + tableName
	 + " t set t.LSH = t.LSH + 1 where t.ZL = '" + type
	 + "'" + "and t.QZ = '" + prefix + "'");
	 String sql = "select * from "
	 + tableName + " t where t.ZL='" + type + "'" + "and t.QZ = '"
	 + prefix + "'";
	 List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
	 if (list.size() == 0) {
	 return "";
	 }
	 Map<String, Object> map = list.get(0);
	 String completion = String.valueOf(map.get("CD"));
	 Integer value = Integer.parseInt(String.valueOf(map.get("LSH")));
	 if (needPrefix) {
	 return prefix + String.format("%0" + completion + "d", value);
	 } else {
	 return String.format("%0" + completion + "d", value);
	 }
	 }
	 */
	/**
	 * 查看是否存在该企业的流水号编码
	 *
	 * @param tableName 子系统表名
	 * @param code      企业编码
	 */
	/*
	@Transactional
	private void check(String tableName, String code, String type) {
		String sql = "select count(1) from " + tableName + " t where t.QZ = '" + code + "' and t.ZL = '" + type + "'";
		try {
			Object obj = DatabaseHandlerDao.getInstance().queryForObject(sql);
			if (obj == null || obj.toString().equals("0")) {
				createCode(tableName, code, type);
			}
		} catch (Exception e) {
			//报错说明没有记录，要新增该企业的流水号编码
			createCode(tableName, code, type);
		}

	}

	private void createCode(String tableName, String code, String type) {
		ShowModuleDefineDaoService showService = XarchListener.getBean(ShowModuleDefineDaoService.class);
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("QZ", code);
		dataMap.put("LSH", "0");
		dataMap.put("ZL", type);//批次号
		dataMap.put("CD", getCodeLength(type));
		showService.save(tableName, dataMap, null);
	}

	private String getCodeLength(String type) {
		Assert.hasLength(type, "必须有流水号种类");
		String sql = "select t.CD from T_COMMON_LSHLX t where t.ZL = '" + type + "'";
		Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
		if (map.size() == 0) {
			throw new RuntimeException("不存在类型为" + type + "的流水号编码");
		}
		return String.valueOf(map.get("CD"));
	}
	*/

}

package com.ces.component.trace.utils;

import ces.coral.lang.StringUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.web.listener.XarchListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 获取企业相关信息
 *
 * @author bdz
 */
@Component
public class CompanyInfoUtil {

	public static CompanyInfoUtil getInstance() {
		return XarchListener.getBean(CompanyInfoUtil.class);
	}

	/**
	 * 查询企业名称
	 *
	 * @param prefix 表前缀（除去T_）
	 * @param value  企业编码
	 * @return 门店名称
	 */
	@Transactional
	public String getCompanyName(String prefix, String value) {
		if (StringUtil.isBlank(prefix) || StringUtil.isBlank(value))
			return null;
		String tableName = TableNameUtil.getQydaTableName(prefix);
		String fieldName = TableNameUtil.getMdmcColumnName(prefix);
		String sql="select "+fieldName+" FROM "+tableName+" where QYBM = '"+value+"'";
//		String sql = "SELECT " + fieldName + " FROM " + tableName + " WHERE QYBM = '" + value + "'";
		Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
		if (map.isEmpty())
			return null;
//		return String.valueOf(map.get(fieldName.toUpperCase())
		return String.valueOf(map.get(fieldName));
	}
	@Transactional
	public String getCompanyName_sdzyc(String qybm,String dwlx) {
		String sql="select qymc FROM t_sdzyc_qyda where QYBM = ? and dwlx=?";
		Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{qybm,dwlx});
		if (map.isEmpty())
			return null;
		return String.valueOf(map.get("QYMC"));
	}
	/**
	 * 根据账户编号判断是不是一个企业
	 * @return
	 */
	public boolean isQy() {
		if (CommonUtil.isSuperRole()) {
			return true;
		}
		String zhbh = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select t.auth_parent_id from t_qypt_zhgl t where t.zhbh = ?";
		String authParentId = String.valueOf(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{zhbh}));
		return "-1".equals(authParentId) ? true : false;
	}

	/**
	 * 获取城市信息
	 *
	 * @return
	 */
	public String getCityInfo() {
		String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select * from t_qypt_zhgl t where t.zhbh = ?";
		Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{companyCode});
		String csxx = String.valueOf(data.get("CSXX"));
		if (csxx.startsWith("31")) {//上海
			return "31";
		} else if (csxx.startsWith("11")) {//北京
			return "11";
		} else if (csxx.startsWith("12")) {//天津
			return "12";
		} else if (csxx.startsWith("50")) {//重庆
			return "50";
		} else {
			return csxx.substring(0, 4);
		}
	}

	public String getCompanyXtlx(){
		String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select z.name as xtlx from T_QYPT_ZHGL t,,T_XTPZ_CODE z where z.code_type_code ='CSPTXTLX' and t.xtlx=z.value and t.zhubh=?";
		List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{companyCode});
		if(dataList!=null && !dataList.isEmpty() && dataList.size()>0){
			Map<String, Object>  obj = dataList.get(0);
			return obj.get("XTLX").toString();
		}
		return null;
	}

}

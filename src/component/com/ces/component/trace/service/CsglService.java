package com.ces.component.trace.service;

import ces.sdk.util.StringUtil;
import com.ces.component.trace.dao.CsglDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import oracle.sql.DATE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import javax.xml.crypto.Data;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bdz on 2015/7/17.
 */
@Component
public class CsglService extends TraceShowModuleDefineDaoService<StringIDEntity, CsglDao> {

	@Autowired
	@Override
	public void setDao(CsglDao dao) {
		super.setDao(dao);
	}

	/**
	 * 蔬菜来源对比
	 * @param city
	 * @param spbm
	 * @return
	 */
	public List<Object[]> getSclyCharts(String city, String spbm) {
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		sql.append("select * from");
		sql.append("(select substr(t.cdbm,0,2) as jc_cdbm,sum(t.lhzzl) as count from t_pc_jclhxx t where t.pfscbm in(select z.zhbh from T_QYPT_ZHGL z where 1=1");
		if (com.ces.config.utils.StringUtil.isNotEmpty(spbm)) {
			sql.append(" and t.id in (select pid from t_pc_jclhmxxx where spbm = ?) ");
			param.add(spbm);
		}
		sql.append(" and z.auth_parent_id != '-1' and z.xtlx = '3' and z.csxx like ? ) group by substr(t.cdbm,0,2))a,");
		param.add(city + "%");
		sql.append("(select substr(s.cdbm,0,2) as cd_cdbm,s.cdbm,s.cdmc from t_common_cdxx s where s.cdbm like '%0000')b");
		sql.append(" where a.jc_cdbm = cd_cdbm");
		List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), param.toArray());
		List<Object[]> list = new ArrayList<Object[]>();
		for (Map<String, Object> map : maps) {
			list.add(new Object[]{String.valueOf(map.get("CDMC")), Integer.parseInt(String.valueOf(map.get("COUNT")))});
		}
		return list;
	}

	/**
	 * 肉品来源对比
	 * @param city
	 * @return
	 */
	public List<Object[]> getRplyCharts(String city) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from");
		sql.append("(select substr(t.cdbm,0,2) as jc_cdbm,sum(t.sjjczl) as count from t_tz_szjcxx t where t.tzcbm in(select z.zhbh from T_QYPT_ZHGL z where z.auth_parent_id != '-1' and z.xtlx = '4' and z.csxx like ? ) group by substr(t.cdbm,0,2))a,");
		sql.append("(select substr(s.cdbm,0,2) as cd_cdbm,s.cdbm,s.cdmc from t_common_cdxx s where s.cdbm like '%0000')b");
		sql.append(" where a.jc_cdbm = cd_cdbm");
		List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{city + "%"});
		List<Object[]> list = new ArrayList<Object[]>();
		for (Map<String, Object> map : maps) {
			list.add(new Object[]{String.valueOf(map.get("CDMC")), Integer.parseInt(String.valueOf(map.get("COUNT")))});
		}
		return list;
	}

	/**
	 * 获得肉品进出对比的市场
	 */
	public Object getAllPrsc(){
        String csxx = CompanyInfoUtil.getInstance().getCityInfo();
		String sql = "select distinct pfscmc,pfscbm from v_rpjc_rpcc where xzqhdm like '"+csxx+"%'";
		List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return list;
	}

	/**
	 * 肉品进出场对比
	 * @param id
	 * @return
	 */
	public Object getRpjccdbChartByweek(String id) {
//		String sql = "select v.pfscbm from v_rpjc_rpcc v where v.id = '"+id+"'";
//		Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
//		String pfscbm = map.get("PFSCBM").toString();
//		String sql1 = "select v.pfscmc,v.pfscbm,v.jczl,v.cczl,v.rq from v_rpjc_rpcc v where v.pfscbm = '"+pfscbm+"' and v.rq<=to_char(sysdate,'yyyy-mm-dd') and v.rq>=to_char(sysdate-7,'yyyy-mm-dd') order by v.rq asc";
//		List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql1);
        String pfscbm = id;
        String sql1 = "select v.pfscmc,v.pfscbm,v.jczl,v.cczl,v.rq from v_rpjc_rpcc v where v.pfscbm = '"+pfscbm+"' and v.rq<=to_char(sysdate,'yyyy-mm-dd') and v.rq>=to_char(sysdate-7,'yyyy-mm-dd') order by v.rq asc";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql1);
		return maps;
	}

    /**
     * 获得菜品进出对比的市场
     */
    public Object getAllPcsc(){
		String csxx = CompanyInfoUtil.getInstance().getCityInfo();
        String sql = "select distinct pfscmc ,pfscbm from v_cpjc_cpcc where xzqhdm like '"+csxx+"%'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }

	/**
	 * 菜品进出场对比
	 * @param id
	 * @return
	 */
	public Object getCpjccdbChartByweek(String id) {
//		String sql = "select v.pfscbm from v_cpjc_cpcc v where v.id = '"+id+"'";
//		Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
//		String pfscbm = map.get("PFSCBM").toString();
//		String sql1 = "select v.pfscmc,v.pfscbm,v.jczl,v.cczl,v.rq from v_cpjc_cpcc v where v.pfscbm = '"+pfscbm+"' and v.rq<=to_char(sysdate,'yyyy-mm-dd') and v.rq>=to_char(sysdate-7,'yyyy-mm-dd') order by v.rq asc";
//		List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql1);
		String pfscbm = id;
		String sql1 = "select v.pfscmc,v.pfscbm,v.jczl,v.cczl,v.rq from v_cpjc_cpcc v where v.pfscbm = '"+pfscbm+"' and v.rq<=to_char(sysdate,'yyyy-mm-dd') and v.rq>=to_char(sysdate-7,'yyyy-mm-dd') order by v.rq asc";
		List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql1);
		return maps;
	}

	/**
	 * 获取所有的蔬菜商品信息
	 * @return
	 */
	public List<Map<String, Object>> getAllScFoods() {
		String sql = "select t.spbm as value, t.spmc as text from t_common_scspxx t";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		for (Map<String, Object> map : list) {
			map.put("value", map.get("VALUE"));
			map.put("text", map.get("TEXT"));
			map.remove("VALUE");
			map.remove("TEXT");
		}
		return list;
	}
	/**
	 * 获取所有的肉品商品信息
	 * @return
	 */
	public List<Map<String, Object>> getAllRpFoods() {
		String sql = "select t.spbm as value, t.spmc as text from t_common_rpspxx t";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		for (Map<String, Object> map : list) {
			map.put("value", map.get("VALUE"));
			map.put("text", map.get("TEXT"));
			map.remove("VALUE");
			map.remove("TEXT");
		}
		return list;
	}

    /**
     * 出场价格统计
     * @param year      年份
     * @param month     月份
     * @return
     */
    public Map queryCcjgtj(String year,String month){
		String csxx = CompanyInfoUtil.getInstance().getCityInfo();
		String date = getNowDate(year,month);
		//蔬菜出场单价统计
		String scSql = "SELECT '" + date + "-" + "'||SUBSTR(J.JYRQ, 9, 2) AS RQ, ROUND(SUM(T.DJ) / COUNT(T.ID), 2) AS PJDJ" +
				"  FROM T_PC_JYMXXX T,T_PC_JYXX J,T_QYPT_ZHGL Q" +
				"  WHERE J.JYRQ LIKE ?" +
				"  AND T.T_PC_JYXX_ID = J.ID" +
				" AND J.PFSCBM = Q.ZHBH AND Q.CSXX like ? "+
					" GROUP BY SUBSTR(J.JYRQ, 9, 2)" +
					" ORDER BY SUBSTR(J.JYRQ, 9, 2)";
			Map allMap = new HashMap();
			allMap.put("蔬菜", DatabaseHandlerDao.getInstance().queryForMaps(scSql,new Object[]{date+"%",csxx+"%"}));
        //肉类出场单价统计
        String rlSql = "SELECT '"+date+"-"+"'||SUBSTR(T.JYRQ, 9, 2) AS RQ, ROUND(SUM(T.DJ) / COUNT(T.ID), 2) AS PJDJ" +
                "  FROM T_TZ_JYXX T,T_QYPT_ZHGL Q" +
                " WHERE T.JYRQ LIKE ? " +
				" AND T.TZCBM = Q.ZHBH AND Q.CSXX like ? "+
                " GROUP BY SUBSTR(T.JYRQ, 9, 2)" +
                " ORDER BY SUBSTR(T.JYRQ, 9, 2)";
        allMap.put("肉类",DatabaseHandlerDao.getInstance().queryForMaps(rlSql,new Object[]{date+"%",csxx+"%"}));
        return allMap;
    }



	/**
	 * 根据种类和商品编码查询
	 * @param year
	 * @param month
	 * @param zl
	 * @param spbm
	 * @return
	 */
	public Map queryCcjgtj(String year,String month,String zl,String spbm){
		String date = getNowDate(year,month);
		String csxx = CompanyInfoUtil.getInstance().getCityInfo();
		Map allMap = new HashMap();
		if("1".equals(zl)){//蔬菜
			String scSql = "SELECT T.SPMC,'2015-07-' || SUBSTR(J.JYRQ, 9, 2) AS RQ, " +
					"       ROUND(SUM(T.DJ) / COUNT(T.ID), 2) AS PJDJ " +
					"  FROM T_PC_JYMXXX T, T_PC_JYXX J, T_QYPT_ZHGL Q " +
					" WHERE J.JYRQ LIKE ? " +
					"   AND T.T_PC_JYXX_ID = J.ID " +
					"   AND J.PFSCBM = Q.ZHBH " +
					"	AND Q.CSXX LIKE ?" +
					"   AND T.SPBM=? " +
					" GROUP BY SUBSTR(J.JYRQ, 9, 2),T.SPBM,T.SPMC " +
					" ORDER BY SUBSTR(J.JYRQ, 9, 2) ";
			List dataList = DatabaseHandlerDao.getInstance().queryForMaps(scSql,new Object[]{date+"%",csxx+"%",spbm});
			if(dataList.size()>0) {
				Map map = (Map) dataList.get(0);
				allMap.put(map.get("SPMC"), dataList);
			}
		}else{//肉类
			//肉类出场单价统计
			String rlSql = "SELECT T.SPMC,'"+date+"-"+"'||SUBSTR(T.JYRQ, 9, 2) AS RQ, ROUND(SUM(T.DJ) / COUNT(T.ID), 2) AS PJDJ" +
					"  FROM T_TZ_JYXX T,T_QYPT_ZHGL Q" +
					" WHERE T.JYRQ LIKE ? " +
					" AND T.TZCBM = Q.ZHBH AND Q.CSXX LIKE ? "+
					" AND T.SPBM=? " +
					" GROUP BY SUBSTR(T.JYRQ, 9, 2),T.SPBM,T.SPMC" +
					" ORDER BY SUBSTR(T.JYRQ, 9, 2)";
			List dataList = DatabaseHandlerDao.getInstance().queryForMaps(rlSql,new Object[]{date+"%",csxx+"%",spbm});
			if(dataList.size()>0) {
				Map map = (Map) dataList.get(0);
				allMap.put(map.get("SPMC"), dataList);
			}
		}
		return allMap;
	}

	/**
	 * 获取当前年月
	 * @param year
	 * @param month
	 * @return
	 */
	public String getNowDate(String year,String month){
		//日期获取
		Calendar c = Calendar.getInstance();
		DecimalFormat df = new DecimalFormat("0000");
		//默认为当年
		if(StringUtil.isBlank(year)){
			year = df.format(c.get(Calendar.YEAR));
		}
		//默认为当月
		df = new DecimalFormat("00");
		if(StringUtil.isBlank(month)){
			month = df.format(c.get(Calendar.MONTH)+1);
		}
		String date = year+"-"+month;
		return date;
	}
}

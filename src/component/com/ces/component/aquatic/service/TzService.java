package com.ces.component.aquatic.service;

import com.ces.component.aquatic.dao.TzDao;
import com.ces.component.aquatic.entity.TzEntity;
import com.ces.component.aquatic.utils.AquaticCommonUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.service.StringIDDefineDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/7/7.
 */
@Component
public class TzService extends StringIDDefineDaoService<TzEntity, TzDao> {

	@Override
	@Autowired
	public void setDao(TzDao dao) {
		super.setDao(dao);
	}

	/**
	 * 查询台帐数据
	 *
	 * @param spbh        商户编号
	 * @param shmc        商户名称
	 * @param sbSjqs      申报时间范围起始
	 * @param sbSjjs      申报时间范围结束
	 * @param pageRequest 分页参数
	 * @return
	 */
	public Page queryTzList(String spbh, String shmc,String qybm,String tzlx, String jypl, String sbSjqs, String sbSjjs, PageRequest pageRequest) {
		/*
		String sql = "select T.QYMC,T.TZLX,T.SHID,T.SHMC,T.GXSJ,T.SBSJ from T_SC_TZ t,T_SC_JYSH S WHERE T.SHID=S.ID";
		if (StringUtil.isNotEmpty(qybm)) {
			sql += " AND T.QYBM = '" + qybm + "'";
		}
		if (StringUtil.isNotEmpty(spbh)) {
			sql += " AND T.SPBH like '%" + spbh + "%'";
		}
		if (StringUtil.isNotEmpty(shmc)) {
			sql += " AND T.SHMC like '%" + shmc + "%'";
		}
		if (StringUtil.isNotEmpty(sbSjqs)) {
			sql += " AND T.SBSJ > '" + sbSjqs + "'";
		}
		if (StringUtil.isNotEmpty(sbSjjs)) {
			sql += " AND T.SBSJ < '" + sbSjjs + "'";
		}
		*/
		StringBuffer sql = new StringBuffer();
		sql.append("select a.shid,a.spbh,a.qybm,a.shmc,a.tzsl,b.zhgxsj,c.tpsl from ");
		sql.append("(select t.shid,s.qybm,s.spbh,s.shmc,count(*) as tzsl from t_sc_tz t, t_sc_jysh s where t.shid = s.id ");
		if (StringUtil.isNotEmpty(tzlx)) {
			sql.append(" and t.tzlx = '" + tzlx + "'");
		}
		if (StringUtil.isNotEmpty(jypl)) {
			sql.append(" and t.tzlx like '%" + jypl + "%'");
		}
		if (StringUtil.isNotEmpty(sbSjqs)) {
			sql.append(" and t.sbsj > '" + sbSjqs + "'");
		}
		if (StringUtil.isNotEmpty(sbSjjs)) {
			sql.append(" and t.sbsj < '" + sbSjjs + "'");
		}
		sql.append(" group by t.shid,s.qybm,s.spbh,s.shmc) a,");
		sql.append(" (select t.shid,max(t.gxsj) as zhgxsj from t_sc_tz t,t_sc_jysh s where t.shid = s.id group by t.shid) b,");
		sql.append(" (select t.shid,count(m.tplj) as tpsl from t_sc_tz t,t_sc_jysh s,t_sc_sctzxx m where t.shid = s.id and t.id = m.tzid group by t.shid) c");
		sql.append(" where a.shid = b.shid and a.shid = c.shid");
		if (StringUtil.isNotEmpty(qybm)) {
			sql.append(" and a.qybm = '" + qybm + "'");
		}
		if (StringUtil.isNotEmpty(spbh)) {
			sql.append(" and a.spbh like '%" + spbh + "%'");
		}
		if (StringUtil.isNotEmpty(shmc)) {
			sql.append(" and a.shmc like '%" + shmc + "%'");
		}
		return AquaticCommonUtil.getInstance().queryPage(pageRequest, sql.toString());
	}

	public List<Map<String, String>> getTzxx(String tzid,String tzlx,String date) {
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		sql.append("select * from t_sc_tz t where 1=1 ");
//		String sql = "select * from t_sc_tz t where to_char(to_date(t.sbsj,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') = ? and  t.tzlx = ? and t.shid = ?";
		if (StringUtil.isNotEmpty(date)) {
			sql.append(" and to_char(to_date(t.sbsj,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') = ? ");
			param.add(date);
		}
		if (StringUtil.isNotEmpty(tzlx)) {
			sql.append(" and t.tzlx = ? ");
			param.add(tzlx);
		}
		if (StringUtil.isNotEmpty(tzid)) {
			sql.append(" and t.id = ? ");
			param.add(tzid);
		}
		String sql2 = "select * from (select * from t_sc_sctzxx t where t.tzid = ? order by to_date(t.SCRQ,'yyyy-mm-dd hh24:mi;ss')) where 1 = 1";
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), param.toArray());
		for (Map<String, Object> map : maps) {
			String id = String.valueOf(map.get("ID"));
			List<Map<String, Object>> data = DatabaseHandlerDao.getInstance().queryForMaps(sql2, new Object[]{id});
			for(Map<String, Object> detailMap : data){
				Map<String, String> result = new HashMap<String, String>();
				result.put("tzid", id);
				result.put("tplj", String.valueOf(detailMap.get("TPLJ")));
				result.put("tpmc", String.valueOf(detailMap.get("TPMC")));
				results.add(result);
			}
		}
		return results;
	}

    public  Page searchSptzxx(String shid,int pageNumber,int pageSize){
        String sql ="select tx.tpmc, ty.id, ty.sbsj, num" +
                "  from t_sc_sctzxx tx," +
                "       (select id, min(scrq) scrq, count(*) num, sbsj" +
                "          from (select t1.id, t1.sbsj sbsj, t2.scrq scrq, tpmc tpmc" +
                "                  from t_sc_tz t1, t_sc_sctzxx t2" +
                "                 where t1.id = t2.tzid and t1.shid='"+shid+"')" +
                "         group by id, sbsj) ty" +
                " where tx.tzid = ty.id" +
                "   and tx.scrq = ty.scrq";
        return AquaticCommonUtil.getInstance().queryPage(new PageRequest(pageNumber,pageSize),sql);
    }

	/**
	 * 商户日期分类台账查询
	 * @param shid
	 * @param tzlx
	 * @param pageRequest
	 * @return
	 */
	public Page queryTzList(String shid,String tzlx,PageRequest pageRequest){
		String sql = "select tx.tpmc, tx.tplj, ty.id, ty.sbsj, num\n" +
				"  from t_sc_sctzxx tx,\n" +
				"       (select id , min(scrq) scrq, count(*) num,sbsj\n" +
				"          from (select t1.id , t1.sbsj sbsj, t2.scrq scrq, tpmc tpmc\n" +
				"                  from t_sc_tz t1, t_sc_sctzxx t2\n" +
				"                 where t1.id = t2.tzid ";
		if(StringUtil.isNotEmpty(shid)){
			sql += " and t1.shid = '" + shid +  "'";
		}
		if(StringUtil.isNotEmpty(tzlx)){
			sql += " and t1.tzlx = '" + tzlx +  "'";
		}
		sql += ") group by id,sbsj) ty  where tx.tzid=ty.id and tx.scrq =ty.scrq";
		return AquaticCommonUtil.getInstance().queryPage(pageRequest, sql);//分页查询
	}

	public Map<String,Object> getTzDetail(String tzid){
		Map<String,Object> map = new HashMap<String, Object>();
		String sql = "select t.tplj from t_sc_sctzxx t where t.tzid = '"+tzid+"'";
		List list = DatabaseHandlerDao.getInstance().queryForList(sql);
		map.put("tzid",tzid);
		map.put("tplj",list);
		return map;
	}

	/**
	 * 获取当天的台帐ID，没有返回null
	 * @return
	 */
	public String getDayTzId(String shid, String tzlx) {
		String sql = "select * from t_sc_tz t where to_char(to_date(t.sbsj,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd') and t.shid = ? and t.tzlx = ?";
		Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{shid, tzlx});
		if (data.get("ID") != null) {
			return String.valueOf(data.get("ID"));
		}
		return null;
	}

	/**
	 * 商户日期分类台账查询
	 * @param tzlx
	 * @param pageRequest
	 * @return
	 */
	public Page queryTzListAll(String tzlx, PageRequest pageRequest) {
		String sql = "select tx.tpmc, tx.tplj, ty.id, ty.sbsj, num\n" +
				"  from t_sc_sctzxx tx,\n" +
				"       (select id , min(scrq) scrq, count(*) num,sbsj\n" +
				"          from (select t1.id , t1.sbsj sbsj, t2.scrq scrq, tpmc tpmc\n" +
				"                  from t_sc_tz t1, t_sc_sctzxx t2\n" +
				"                 where t1.id = t2.tzid ";
		if(StringUtil.isNotEmpty(tzlx)){
			sql += " and t1.tzlx = '" + tzlx +  "'";
		}
		sql += ") group by id,sbsj) ty  where tx.tzid=ty.id and tx.scrq =ty.scrq";
		return AquaticCommonUtil.getInstance().queryPage(pageRequest, sql);//分页查询
	}

	public List<Map<String, String>> getTzxxAll(String tzlx, String date) {
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>();
		sql.append("select * from t_sc_tz t where 1=1 ");
//		String sql = "select * from t_sc_tz t where to_char(to_date(t.sbsj,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') = ? and  t.tzlx = ? ";
		if (StringUtil.isNotEmpty(date)) {
			sql.append(" and to_char(to_date(t.sbsj,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd') = ? ");
			param.add(date);
		}
		if (StringUtil.isNotEmpty(tzlx)) {
			sql.append(" and t.tzlx = ? ");
			param.add(tzlx);
		}
		String sql2 = "select * from (select * from t_sc_sctzxx t where t.tzid = ? order by to_date(t.SCRQ,'yyyy-mm-dd hh24:mi:ss')) where rownum <= 1";
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), param.toArray());
		for (Map<String, Object> map : maps) {
			String id = String.valueOf(map.get("ID"));
			Map<String, String> result = new HashMap<String, String>();
			result.put("tzid", id);
			Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql2, new Object[]{id});
			result.put("tplj", String.valueOf(data.get("TPLJ")));
			result.put("tpmc", String.valueOf(data.get("TPMC")));
			results.add(result);
		}
		return results;
	}
}

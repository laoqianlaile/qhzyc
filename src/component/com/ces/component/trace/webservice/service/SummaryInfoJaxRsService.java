package com.ces.component.trace.webservice.service;

import com.ces.component.trace.utils.ResponseUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.utils.CodeUtil;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/5/24.
 */
@Component
@Path("/summary")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SummaryInfoJaxRsService {

	@GET
	@Path("/getFoodSummaryByCode")
	public Response getFoodSummaryByCode(@QueryParam("code") String code) {
		String sql = "select substr(t.create_time, 6, 2) as month, t.spmc, avg(t.dj) as dj from t_pc_jymxxx t where substr(t.create_time, 1, 4) = '2015' and t.spbm = '" + code + "' group by substr(t.create_time, 6, 2), t.spmc";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return ResponseUtil.generateResponse(list);
	}

	@GET
	@Path("/getAllFoods")
	public Response getAllFoods() {
		String sql = "select t.spbm as value, t.spmc as text from t_common_scspxx t";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		for (Map<String, Object> map : list) {
			map.put("value", map.get("VALUE"));
			map.put("text", map.get("TEXT"));
			map.remove("VALUE");
			map.remove("TEXT");
		}
		return ResponseUtil.generateResponse(list);
	}

	@GET
	@Path("/getAllOrgInfo")
	public Response getAllOrgInfo() {
		String sql = "select t.zhbh as value, t.qymc as text from t_qypt_zhgl t where t.auth_parent_id = '-1'";
		List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		for (Map<String, Object> map : list) {
			map.put("value", map.get("VALUE"));
			map.put("text", map.get("TEXT"));
			map.remove("VALUE");
			map.remove("TEXT");
		}
		return ResponseUtil.generateResponse(list);
	}

	@GET
	@Path("/getStockAndDealSummaryByOrgCode")
	public Response getStockAndDealSummaryByOrgCode(@QueryParam("zhbh") String zhbh) {
		List<Object[]> list = new ArrayList<Object[]>();
		String sql = "select count(1) from t_cspt_jyxx t where t.zsm is not null and t.qybm = '" + zhbh + "'";
		Object in = DatabaseHandlerDao.getInstance().queryForObject(sql);
		list.add(new Object[]{"交易数量", Integer.parseInt(String.valueOf(in))});
		sql = "select count(1) from t_cspt_jyxx t where t.zsm is null and t.qybm = '" + zhbh + "'";
		Object out = DatabaseHandlerDao.getInstance().queryForObject(sql);
		list.add(new Object[]{"进场数量", Integer.parseInt(String.valueOf(out))});
		return ResponseUtil.generateResponse(list);
	}

	@GET
	@Path("/getAllSystem")
	public Response getAllSystem() {
		List<Code> codeList = CodeUtil.getInstance().getCodeList("CSPTXTLX");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Code code : codeList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", code.getValue());
			map.put("text", code.getName());
			list.add(map);
		}
		return ResponseUtil.generateResponse(list);
	}

	@GET
	@Path("/getStockAndDealSummaryBySystem")
	public Response getStockAndDealSummaryBySystem(@QueryParam("systemCode") String systemCode) {
		List<Object[]> list = new ArrayList<Object[]>();
		String sql = "select count(1) from t_cspt_jyxx t where t.zsm is not null and t.xtlx = '" + systemCode + "'";
		Object in = DatabaseHandlerDao.getInstance().queryForObject(sql);
		list.add(new Object[]{"交易数量", Integer.parseInt(String.valueOf(in))});
		sql = "select count(1) from t_cspt_jyxx t where t.zsm is null and t.xtlx = '" + systemCode + "'";
		Object out = DatabaseHandlerDao.getInstance().queryForObject(sql);
		list.add(new Object[]{"进场数量", Integer.parseInt(String.valueOf(out))});
		return ResponseUtil.generateResponse(list);
	}

	@GET
	@Path("/getSystemSummary")
	public Response getSystemSummary() {
		Map<String, Object> returnDatas = new HashMap<String, Object>();

		List<String> sysNames = new ArrayList<String>();

		List<Object> sysData = new ArrayList<Object>();
		Map<String, Object> in = new HashMap<String, Object>();
		in.put("name", "进场");
		Map<String, Object> out = new HashMap<String, Object>();
		out.put("name", "交易");


		List<Code> codeList = CodeUtil.getInstance().getCodeList("CSPTXTLX");
		String sql = "select t.xtlx, case when t.zsm is not null then '1' else '0' end as newzsm,count(*) as count from t_cspt_jyxx t group by t.xtlx,case when t.zsm is not null then '1' else '0' end order by t.xtlx";
		List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);

		int[] inCount = new int[codeList.size()];
		int[] outCount = new int[codeList.size()];
		int index = 0;
		for (Code code : codeList) {
			sysNames.add(code.getName());
			String xtlx = code.getValue();
			for (Map<String, Object> map : maps) {
				if (map.get("XTLX").toString().equals(xtlx)) {
					if (map.get("NEWZSM").toString().equals("1")) {//交易
						outCount[index] = Integer.parseInt(String.valueOf(map.get("COUNT")));
					} else if (map.get("NEWZSM").toString().equals("0")) {//进场
						inCount[index] = Integer.parseInt(String.valueOf(map.get("COUNT")));
					}
				}
			}
			index++;
		}
		in.put("data", inCount);
		out.put("data", outCount);
		sysData.add(in);
		sysData.add(out);
		returnDatas.put("sysNames", sysNames);
		returnDatas.put("sysData", sysData);

		return ResponseUtil.generateResponse(returnDatas);
	}

}

package com.ces.component.aquatic.utils;

import com.ces.component.aquatic.dto.JyshDto;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.utils.TokenUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/7/1.
 * 水产系统加载用户工具类
 */
public class AquaticUserUtil {

	/**
	 * 商户用户类型
	 */
	public static final String CommercialUserType = "1";

	/**
	 * 监管员用户类型
	 */
	public static final String SupervisorUserType = "2";

	/**
	 * 根据用户名和用户类型加载用户
	 * @param username
	 * @param userType
	 * @return
	 */
	public static JyshDto loadUser(String username, String userType) {
		String table = null;
		if (CommercialUserType.equals(userType)) {
			table = "t_sc_jysh";
		} else if (SupervisorUserType.equals(userType)) {
			table = "t_sc_jgy";
		} else {
			return null;
		}
		String sql = "select * from " + table + " t where t.dlm = ?";
		Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{username});
		JyshDto jyshDto = new JyshDto();
		jyshDto.setShid(String.valueOf(data.get("ID")));
		jyshDto.setQybm(String.valueOf(data.get("QYBM")));
		jyshDto.setQymc(String.valueOf(data.get("QYMC")));
		jyshDto.setShmc(String.valueOf(data.get("SHMC")));
		jyshDto.setPassword(String.valueOf(data.get("MM")));
		jyshDto.setSjh(String.valueOf(data.get("SJH")));
		String[] jyplbms = String.valueOf(data.get("JYPLBM")).split(",");
		String[] jyplmcs = String.valueOf(data.get("JYPL")).split(",");
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < jyplbms.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("jyplbm",jyplbms[i]);
			map.put("jyplmc", jyplmcs[i]);
			list.add(map);
		}
		jyshDto.setJypls(list);
		return jyshDto;
	}

	/**
	 * 更新用户密码
	 * @param username
	 * @param newPass
	 * @param userType
	 */
	public static void updateUserPass(String username, String newPass, String userType) {
		String table = null;
		if (CommercialUserType.equals(userType)) {
			table = "t_sc_jysh";
		} else if (SupervisorUserType.equals(userType)) {
			table = "t_sc_jgy";
		}
		String sql = "update " + table + " t set t.mm = '" + newPass + "' where t.dlm = '" + username + "'";
		DatabaseHandlerDao.getInstance().executeSql(sql);
	}

	/**
	 * 根据token提取用户企业编码
	 * @param token
	 * @return
	 */
	public static String loadQybmByToken(String token, String userType) throws IOException {
		String table = null;
		if (CommercialUserType.equals(userType)) {
			table = "t_sc_jysh";
		} else if (SupervisorUserType.equals(userType)) {
			table = "t_sc_jgy";
		}
		String[] tokens = TokenUtils.decodeToken(token);
		String sql = "select t.qybm from " + table + " t where t.dlm = ?";
		return String.valueOf(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{tokens[0]}));
	}

}

package com.ces.component.sensor.utils;

import ces.sdk.system.bean.OrgUserInfo;
import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.dao.OrgUserInfoDao;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.factory.SdkDaoFactory;
import com.ces.component.farm.dto.TyyhDto;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 海岛田园用户工具类
 *
 * @author dell-pc
 */
public class SensorUserUtil {

	/**
	 * 工作人员用户类型
	 */
	public static final String PersonnelUserType = "1";

	/**
	 * 客户用户类型
	 */
	public static final String CustomerUserType = "2";

	/**
	 * 根据用户名加载用户
	 *
	 * @param username
	 * @throws SystemFacadeException
	 */
	public static TyyhDto loadUser(String username) throws SystemFacadeException {
		TyyhDto tyyhDto = null;
		OrgUserInfoDao orgUserInfoDao = SdkDaoFactory.createOrgUserDao();
		//以门店的形式登录
		UserInfo userInfo = CommonUtil.getUserInfoFacade().findByLoginName(username);
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", userInfo.getId());
		List<OrgUserInfo> orgUserInfos = orgUserInfoDao.findByCondition(param);
		OrgUserInfo orgUserInfo = orgUserInfos.get(0);
		boolean isAppUser = true;
		if (userInfo != null) {
			List<RoleInfo> roleList = CommonUtil.getRoleInfoFacade().findRoleInfosByUserId(userInfo.getId());
			for (RoleInfo role : roleList) {
				if ("ZZSJDJS".equals(role.getName())) {
					isAppUser = true;
					break;
				}
			}
			if (isAppUser) {
				String authId = userInfo.getBelongOrgId();
				if (StringUtil.isNotEmpty(authId)) {
					String sql = "select t.* from t_qypt_zhgl t where t.auth_id = ? and t.is_delete != '1'";
					Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{orgUserInfo.getOrgId()});
					tyyhDto = new TyyhDto();
					tyyhDto.setPassword(userInfo.getPassword());
					tyyhDto.setQybm(map.get("ZHBH") == null ? "" : String.valueOf(map.get("ZHBH")));
					tyyhDto.setQyId(map.get("ID") == null ? "" : String.valueOf(map.get("ID")));
					tyyhDto.setQylx(map.get("XTLX") == null ? "" : String.valueOf(map.get("XTLX")));
					tyyhDto.setQymc(map.get("QYMC") == null ? "" : String.valueOf(map.get("QYMC")));
					tyyhDto.setUserType(PersonnelUserType);
					tyyhDto.setYhm(username);
				}
			}
			return tyyhDto;
		}

		//用户类型及信息
//		String customerSql = "select t.qybm,t.mm,md.id as mdid from t_zz_khxx t,t_zz_khmdxx md where t.id = md.pid and t.yhm = ? and t.is_delete != '1' and t.sfktzh = '1' and md.is_delete != '1'";
		String customerSql = "select t.id,t.qybm,t.mm from t_zz_khmdxx t where t.yhm = ? and t.is_delete = '0'";

		//企业信息
		String qyxxSql = "select * from t_zz_cdda t where t.qybm = ?";

		List customerList = DatabaseHandlerDao.getInstance().queryForList(customerSql, new Object[]{username});

		if (customerList != null && customerList.size() > 0) {
			tyyhDto = new TyyhDto();
			Map<String, Object> userData = DatabaseHandlerDao.getInstance().queryForMap(customerSql, new Object[]{username});
			Map<String, Object> qyxxData = DatabaseHandlerDao.getInstance().queryForMap(qyxxSql, new Object[]{userData.get("QYBM")});
			tyyhDto.setUserType(CustomerUserType);
			tyyhDto.setMdId(userData.get("ID") == null ? "" : String.valueOf(userData.get("ID")));
			tyyhDto.setQybm(userData.get("QYBM") == null ? "" : String.valueOf(userData.get("QYBM")));
			tyyhDto.setPassword(userData.get("MM") == null ? "" : String.valueOf(userData.get("MM")));
			tyyhDto.setYhm(username);
			tyyhDto.setQyId(qyxxData.get("ID") == null ? "" : String.valueOf(qyxxData.get("ID")));
			tyyhDto.setQymc(qyxxData.get("QYMC") == null ? "" : String.valueOf(qyxxData.get("QYMC")));
			tyyhDto.setQylx(qyxxData.get("DWLX") == null ? "" : String.valueOf(qyxxData.get("DWLX")));
		} else {
			return tyyhDto;
		}

//		Map<String, Object> userData = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{username});
		//若为客户，添加门店ID
//		if(CustomerUserType.equals(tyyhDto.getUserType())){
//			tyyhDto.setMdId(userData.get("MDID")==null?"":String.valueOf(userData.get("MDID")));
//		}
//		tyyhDto.setQybm(userData.get("QYBM")==null?"":String.valueOf(userData.get("QYBM")));
//		tyyhDto.setPassword(userData.get("MM")==null?"":String.valueOf(userData.get("MM")));
//		tyyhDto.setYhm(username);

//		Map<String,Object> qyxxData = DatabaseHandlerDao.getInstance().queryForMap(qyxxSql, new Object[]{userData.get("QYBM")});
//		tyyhDto.setQyId(qyxxData.get("ID")==null?"":String.valueOf(qyxxData.get("ID")));
//		tyyhDto.setQymc(qyxxData.get("QYMC")==null?"":String.valueOf(qyxxData.get("QYMC")));
//		tyyhDto.setQylx(qyxxData.get("DWLX")==null?"":String.valueOf(qyxxData.get("DWLX")));

		return tyyhDto;

	}

}

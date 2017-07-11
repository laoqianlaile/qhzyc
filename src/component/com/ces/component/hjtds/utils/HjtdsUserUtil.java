package com.ces.component.hjtds.utils;

import ces.sdk.system.bean.OrgUserInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.dao.OrgUserInfoDao;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.factory.SdkDaoFactory;
import com.ces.component.farm.dto.TyyhDto;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.utils.TokenUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/7/1.
 * 水产系统加载用户工具类
 */
public class HjtdsUserUtil {

	/**
	 * 商户用户类型
	 */
	public static final String CommercialUserType = "1";

	/**
	 * 监管员用户类型
	 */
	public static final String SupervisorUserType = "2";

//	/**
//	 * 根据用户名和用户类型加载用户
//	 * @param username
//	 *
//	 * @return
//	 */
//	public static nsxDto loadUser(String username) {
//		String table = "auth_user";
//		String sql = "select * from " + table + " t where t.dlm = ?";
//		Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{username});
//        nsxDto nsxDto = new nsxDto();
//        nsxDto.setId(String.valueOf(data.get("ID")));
//        nsxDto.setLoginName(String.valueOf(data.get("LOGIN_NAME")));
//        nsxDto.setTelephone(String.valueOf(data.get("QYMC")));
//        nsxDto.setPassword(String.valueOf(data.get("PASSWORD")));
//
//		return nsxDto;
//	}

	/**
	 * 更新用户密码
	 * @param username
	 * @param newPass
	 */
	public static void updateUserPass(String username, String newPass) {
		String table = "T_auth_user";
		String sql = "update " + table + " t set t.password = '" + newPass + "' where t.login_name = '" + username + "'";
		DatabaseHandlerDao.getInstance().executeSql(sql);
	}

	/**
	 * 根据token提取用户企业编码
	 * @param token
	 * @return
	 */
	public static String loadQybmByToken(String token, String userType) throws IOException {
		String table = "t_auth_user";
		String[] tokens = TokenUtils.decodeToken(token);
		String sql = "select t.qybm from " + table + " t where t.dlm = ?";
		return String.valueOf(DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{tokens[0]}));
	}


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
//            List<RoleInfo> roleList = CommonUtil.getRoleInfoFacade().findRoleInfosByUserId(userInfo.getId());
//            for (RoleInfo role : roleList) {
//                if ("ZZSJDJS".equals(role.getName())) {
//                    isAppUser = true;
//                    break;
//                }
//            }
            if (isAppUser) {
                String authId = orgUserInfo.getOrgId();
                if (StringUtil.isNotEmpty(authId)) {
                    String sql = "select t.* from t_qypt_zhgl t where t.auth_id = ? and t.is_delete != '1'";
                    Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{orgUserInfo.getOrgId()});
                    tyyhDto = new TyyhDto();
                    tyyhDto.setPassword(userInfo.getPassword());
                    tyyhDto.setPassword(userInfo.getPassword());
                    tyyhDto.setQybm(map.get("ZHBH") == null ? "" : String.valueOf(map.get("ZHBH")));
                    tyyhDto.setQyId(map.get("ID") == null ? "" : String.valueOf(map.get("ID")));
                    tyyhDto.setQylx(map.get("XTLX") == null ? "" : String.valueOf(map.get("XTLX")));
                    tyyhDto.setQymc(map.get("QYMC") == null ? "" : String.valueOf(map.get("QYMC")));
                    tyyhDto.setYhm(username);
                }
            }
            return tyyhDto;
        }

        //用户类型及信息
//		String customerSql = "select t.qybm,t.mm,md.id as mdid from t_zz_khxx t,t_zz_khmdxx md where t.id = md.pid and t.yhm = ? and t.is_delete != '1' and t.sfktzh = '1' and md.is_delete != '1'";
        String customerSql = "select t.id,t.qybm,t.password from t_auth_user t where t.login_name = ? and t.is_delete = '0'";

        //企业信息
        String qyxxSql = "select * from t_zz_cdda t where t.qybm = ?";

        List customerList = DatabaseHandlerDao.getInstance().queryForList(customerSql, new Object[]{username});

        if (customerList != null && customerList.size() > 0) {
            tyyhDto = new TyyhDto();
            Map<String, Object> userData = DatabaseHandlerDao.getInstance().queryForMap(customerSql, new Object[]{username});
            Map<String, Object> qyxxData = DatabaseHandlerDao.getInstance().queryForMap(qyxxSql, new Object[]{userData.get("QYBM")});
           // tyyhDto.setUserType(CustomerUserType);
            tyyhDto.setQyId(userData.get("ID") == null ? "" : String.valueOf(userData.get("ID")));
            tyyhDto.setQybm(userData.get("QYBM") == null ? "" : String.valueOf(userData.get("QYBM")));
            tyyhDto.setPassword(userData.get("PASSWORD") == null ? "" : String.valueOf(userData.get("PASSWORD")));
            tyyhDto.setYhm(username);
            tyyhDto.setQyId(qyxxData.get("ID") == null ? "" : String.valueOf(qyxxData.get("ID")));
            tyyhDto.setQymc(qyxxData.get("QYMC") == null ? "" : String.valueOf(qyxxData.get("QYMC")));
            tyyhDto.setQylx(qyxxData.get("DWLX") == null ? "" : String.valueOf(qyxxData.get("DWLX")));
        } else {
            return tyyhDto;
        }
        return tyyhDto;

    }
}

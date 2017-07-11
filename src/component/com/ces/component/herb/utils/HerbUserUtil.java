package com.ces.component.herb.utils;

import ces.sdk.system.exception.SystemFacadeException;
import com.ces.component.farm.dto.TyyhDto;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.utils.TokenUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/7/1.
 * 水产系统加载用户工具类
 */
public class HerbUserUtil {

	/**
	 * 商户用户类型
	 */
	public static final String CommercialUserType = "1";

	/**
	 * 监管员用户类型
	 */
	public static final String SupervisorUserType = "2";


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
//        OrgUserInfoDao orgUserInfoDao = SdkDaoFactory.createOrgUserDao();
        //以门店的形式登录
//        UserInfo userInfo = CommonUtil.getUserInfoFacade().findByLoginName(username);
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("userId", userInfo.getId());
//        List<OrgUserInfo> orgUserInfos = orgUserInfoDao.findByCondition(param);
//        OrgUserInfo orgUserInfo = orgUserInfos.get(0);
//        boolean isAppUser = true;
        if (username != null) {
                    String sql = "select t.MM,t.ID,t.QYZT,t.QYBM,t.GZRYBH,t.GW from t_zz_gzryda t where t.xm = ? and t.is_delete != '1'";
                    List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{username});
                    Map<String,Object>  map =  maps.get(0);
                    if(maps.size() >1){//用户名出现同名
                        map = maps.get(0);
                    }
                    tyyhDto = new TyyhDto();
                    tyyhDto.setPassword(String.valueOf(map.get("MM")));
                    tyyhDto.setUserType(map.get("GZRYBH") == null ? "" : String.valueOf(map.get("GZRYBH")));
                    tyyhDto.setQyId(map.get("ID") == null ? "" : String.valueOf(map.get("ID")));
                    tyyhDto.setQylx(map.get("QYZT") == null ? "" : String.valueOf(map.get("QYZT")));
                    tyyhDto.setQybm(map.get("QYBM") == null ? "" : String.valueOf(map.get("QYBM")));
                    tyyhDto.setUserType(map.get("GW") == null ? "" : String.valueOf(map.get("GW")));
                    tyyhDto.setYhm(username);
            return tyyhDto;
        }
        return tyyhDto;



    }
}

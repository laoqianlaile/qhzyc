package com.ces.config.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import ces.sdk.system.bean.OpLogInfo;
import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.dbbean.DbOpLogInfo;
import ces.sdk.system.facade.OpLogInfoFacade;
import ces.sdk.system.facade.OrgInfoFacade;
import ces.sdk.system.facade.ResourceInfoFacade;
import ces.sdk.system.facade.RoleInfoFacade;
import ces.sdk.system.facade.UserInfoFacade;
import ces.sdk.system.factory.SystemFacadeFactory;

import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.utils.TokenUtils;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.core.web.listener.XarchListener;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;

public class CommonUtil {

    private static Log log = LogFactory.getLog(CommonUtil.class);

    /** 超级系统管理员ID. */
    public final static String SUPER_ADMIN_ID = "1";

    /** 系统ID */
    private static String systemId = null;

    /**
     * qiucs 2013-9-24
     * <p>描述: 取当前登录人员</p>
     * 
     * @return SysUser 返回类型
     * @throws
     */
    public static SysUser getUser() {
        SysUser user = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    /**
     * qiucs 2014-1-23
     * <p>描述: 获取当前用户的ID</p>
     */
    @Deprecated
    public static String getUserId() {
        return getCurrentUserId();
    }

    /**
     * qiucs 2015-1-30 下午1:26:26
     * <p>描述: 获取当前用户的ID </p>
     * 
     * @return String
     */
    public static String getCurrentUserId() {
        return getUser().getId();
    }

    /**
     * qiucs 2015-1-30 下午1:29:11
     * <p>描述: 获取当前部门的ID</p>
     */
    public static String getCurrentDeptId() {
        return getUser().getBelongOrgId();
    }

    /**
     * wl 2015-4-14
     * <p>描述: 获取上层部门的ID</p>
     */
    public static OrgInfo getCurrentUpperOrg(String orgType) {
        try {
            List<OrgInfo> orgInfoList = getOrgInfoFacade().findOrgByTypeIdAndUserID(getCurrentUserId(), orgType);
            if (CollectionUtils.isNotEmpty(orgInfoList)) {
                return orgInfoList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * qiucs 2014-1-23
     * <p>描述: 获取当前用户的角色IDS</p>
     */
    public static String getRoleIds() {
        return getUser().getRoleIds();
    }

    /**
     * qiucs 2014-1-23
     * <p>描述: 获取当前用户的角色KEYS</p>
     */
    public static String getRoleKeys() {
    	List<GrantedAuthority> list = getUser().getAuthorities();
    	StringBuilder sb = new StringBuilder(50);
    	for (int i = 0, len = list.size(); i < len; i++) {
    		sb.append(",").append(list.get(i).getAuthority());
    	}
    	if (sb.length() > 0) sb.deleteCharAt(0);
        return sb.toString();
    }

    /**
     * qiucs 2013-10-28
     * <p>描述: 用户信息SDK接口</p>
     * 
     * @return UserInfoFacade 返回类型
     */
    public static UserInfoFacade getUserInfoFacade() {
        return SystemFacadeFactory.newInstance().createUserInfoFacade();
    }

    /**
     * qiucs 2013-10-28
     * <p>描述: 机构组织SDK接口</p>
     * 
     * @return OrgInfoFacade 返回类型
     */
    public static OrgInfoFacade getOrgInfoFacade() {
        return SystemFacadeFactory.newInstance().createOrgInfoFacade();
    }

    /**
     * qiucs 2013-10-28
     * <p>描述: 机构组织SDK接口</p>
     * 
     * @return OrgInfoFacade 返回类型
     */
    public static ResourceInfoFacade getResourceInfoFacade() {
        return SystemFacadeFactory.newInstance().createResourceInfoFacade();
    }

    /**
     * qiucs 2013-10-28
     * <p>描述: 机构组织SDK接口</p>
     * 
     * @return OrgInfoFacade 返回类型
     */
    public static RoleInfoFacade getRoleInfoFacade() {
        return SystemFacadeFactory.newInstance().createRoleInfoFacade();
    }

    /**
     * qiucs 2013-10-28
     * <p>描述: 根据ID获取用户名称</p>
     * 
     * @param userId
     * @return String 返回类型
     */
    public static String getUserNameById(String userId) {
        UserInfo info = getUserInfoFacade().findByID(userId);
        return ((null == info) ? "" : info.getName());
    }

    /**
     * qiucs 2013-10-28
     * <p>描述: 根据ID获取组织机构名称</p>
     * 
     * @param orgnizeId
     * @return String 返回类型
     */
    public static String getOrganizeNameById(String orgnizeId) {
        OrgInfo info = getOrgInfoFacade().findByID(orgnizeId);
        return ((info == null) ? "" : info.getName());
    }

    /**
     * qiucs 2013-10-28
     * <p>描述: 根据编码类型名称和编码值获取编码名称</p>
     * 
     * @param key
     * @param codeValue
     * @return String 返回类型
     */
    public static String getCodeName(String key, String codeValue) {
        if (StringUtil.isEmpty(key))
            return "";
        if (StringUtil.isEmpty(codeValue))
            return "";

        String codeName = CodeUtil.getInstance().getCodeName(key, codeValue);
        return StringUtil.null2empty(codeName);
    }

    /**
     * qiucs 2014-3-6
     * <p>描述: 获取项目的绝对路径目录</p>
     */
    public static String getAppRootPath() {
        String appRootPath = XarchListener.appAbsolutepath.replaceAll("\\\\", "/");
        if (!appRootPath.endsWith("/")) {
            appRootPath += "/";
        }
        return appRootPath;
    }

    /**
     * 生成前台页面元素的ID
     * 
     * @param prefix 前缀
     * @return String
     */
    public static String generateUIId(String prefix) {
        return prefix + "_" + System.currentTimeMillis();
    }

    /**
     * 获取Token
     * 
     * @return String
     */
    public static String getToken() {
        SysUser user = getUser();
        return TokenUtils.getToken(user.getLoginName(), user.getPassword());
    }

    /**
     * qiucs 2014-9-11
     * <p>描述: 查询条件</p>
     * 
     * @param filterTag 为 tableId + timestamp
     * @return String 返回类型
     * @throws
     */
    public static String getQueryFilter(String filterTag) {
        return StringUtil.null2empty(ServletActionContext.getRequest().getSession().getAttribute(ConstantVar.QUERY_FILTER + filterTag));
    }

    /**
     * qiucs 2014-9-11
     * <p>描述: 排序条件</p>
     * 
     * @param filterTag 为 tableId + timestamp
     * @return String 返回类型
     * @throws
     */
    public static String getQuerySort(String filterTag) {
        return StringUtil.null2empty(ServletActionContext.getRequest().getSession().getAttribute(ConstantVar.QUERY_SORT + filterTag));
    }

    /**
     * 查询语句in表达式有长度限制（1000），将字符串转换成每个长度为1000的字符串List
     * 
     * @param inStr
     * @return
     */
    public static List<String> parseSqlIn(String inStr) {
        List<String> list = new ArrayList<String>();
        String[] strs = inStr.split(",");
        if (strs.length <= 1000) {
            list.add(inStr);
        } else {
            StringBuilder sb = null;
            for (int i = 0; i < strs.length; i++) {
                if (i % 1000 == 0) {
                    if (sb != null && sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                        list.add(sb.toString());
                    }
                    sb = new StringBuilder();
                }
                sb.append(strs[i]).append(",");
            }
            if (sb != null && sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
                list.add(sb.toString());
            }
        }
        return list;
    }

    /**
     * 是否是超级管理员
     * 
     * @return boolean
     */
    public static boolean isSuperRole() {
        boolean isSuperRole = false;
        String roleIds = CommonUtil.getRoleIds();
        if (StringUtil.isNotEmpty(roleIds)) {
            String[] roleIdArray = roleIds.split(",");
            for (String roleId : roleIdArray) {
                if ("1".equals(roleId)) {
                    isSuperRole = true;
                    break;
                }
            }
        }
        return isSuperRole;
    }

    /**
     * 获取当前系统的ID
     * 
     * @return String
     */
    public static String getSystemId() {
        if (systemId == null) {
            if (CfgCommonUtil.isReleasedSystem()) {
                List<Menu> systemList = XarchListener.getBean(MenuService.class).getRootMenuList();
                if (systemList != null && systemList.size() == 1) {
                    systemId = systemList.get(0).getId();
                }
            } else {
                systemId = "";
            }
        }
        return systemId;
    }
    
    /**
     * 添加操作日志
     * 
     * @param type 模块
     * @param operate 操作
     * @param message 详情
     */
    public static void addOperateLog(String type, String operate, String message) {
        try {
            OpLogInfoFacade logInfoFacade = FacadeUtil.getLogInfoFacade();
            OpLogInfo opLogInfo = new DbOpLogInfo();
            opLogInfo.setType(type);
            opLogInfo.setLogDate(new Date());
            opLogInfo.setMessage(message);
            opLogInfo.setOperate(operate);
            opLogInfo.setUserId(CommonUtil.getCurrentUserId());
            opLogInfo.setStatus(1);
            opLogInfo.setUserName(CommonUtil.getUser().getUsername());
            logInfoFacade.addOpLogInfo(opLogInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

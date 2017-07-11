package com.ces.config.datamodel.page.coral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;

import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
/**
 * <p>描述: 组织用户项树</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-17 上午10:12:34
 *
 */
public class UserPageModel extends DefaultPageModel {
    
    public static final String P_EMPTY = "P_EMPTY";
    
    private String id;
    
    public void init() {
        setData(cast2tree(new ArrayList<Map<String, Object>>(), "-1"));
    }
    /**
     * qiucs 2014-7-17 
     * <p>描述: 代码项转换成下拉框选项</p>
     * @return Object    返回类型   
     * @throws
     */
    protected List<Map<String, Object>> cast2tree(List<Map<String, Object>> list, String id) {
        try {
            Map<String, Object> item = new HashMap<String, Object>();
            List<OrgInfo> orgInfos = FacadeUtil.getOrgInfoFacade().findChildsByParentId(id);
            for (OrgInfo org : orgInfos) {
                item.put("id", org.getId());
                item.put("name", org.getName());
                item.put("isParent", true);
                item.put("type", "D");
                item.put("children", getChildren(org.getId()));
                list.add(item);
            }
            list.add(item);
        } catch (Exception e) {
            throw new BusinessException("生成用户树失败！", e);
        }
        
        return (list);
    }
    
    protected Map<String, List<Map<String, Object>>> getChildren(String organizeID) {
        Map<String, List<Map<String, Object>>> children = new HashMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.addAll(getChildrenOrg(organizeID));
        list.addAll(getUsers(organizeID));
        children.put("children", list);
        return children;
    }
    
    protected List<Map<String, Object>> getChildrenOrg(String organizeID) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Map<String, Object> item = new HashMap<String, Object>();
            List<OrgInfo> orgInfos = FacadeUtil.getOrgInfoFacade().findChildsByParentId(organizeID);
            for (OrgInfo org : orgInfos) {
                item.put("id", org.getId());
                item.put("name", org.getName());
                item.put("isParent", true);
                item.put("type", "D");
                item.put("children", getChildren(org.getId()));
                list.add(item);
            }
            list.add(item);
        } catch (Exception e) {
            throw new BusinessException("生成用户树失败！", e);
        }
        
        return (list);
    }
    
    protected List<Map<String, Object>> getUsers(String organizeID) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            List<UserInfo> userInfos = FacadeUtil.getUserInfoFacade().findUsersByOrgId(organizeID);
            for (UserInfo user : userInfos) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("id", user.getId());
                item.put("name", user.getName());
                item.put("isParent", false);
                item.put("type", "U");
                list.add(item);
            }
        } catch (Exception e) {
            throw new BusinessException("用户树：获取用户信息失败！", e);
        }
        return list;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}

package com.ces.config.datamodel.page.coral;

import java.util.List;
import java.util.Map;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;

import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
/**
 * <p>描述: 代码项下拉框</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-17 上午10:12:34
 *
 */
public class OrganizePageModel extends DefaultPageModel {
    
    public static final String P_EMPTY = "P_EMPTY";
    public static final String P_ASYNC = "P_ASYNC";
    public static final String P_OPEN  = "P_OPEN";
    public static final String P_CHECKED = "P_CHECKED";
    public static final String P_NOCHECK = "P_NOCHECK";
    public static final String TYPE_USER = "U";
    public static final String TYPE_ORG  = "D";
    
    private String id;
    private String type; // 
    private boolean async;
    private boolean open;
    private boolean nocheck; // 组织节点是否显示复选框或单选框
    private String checked;
    
    public void init() {
        if (isAsync()) {
            setData(cast2tree(getId()));
        } else {
           if (null == getId()) {
               setData(cast2tree("-1"));
           } else {
               setData(Lists.newArrayList());
           }
        }
    }
    /**
     * qiucs 2014-7-17 
     * <p>描述: 生成组织用户树</p>
     * @return Object    返回类型   
     * @throws
     */
    protected Object cast2tree(String id) {
        return getChildren(id);
    }
    
    /**
     * qiucs 2014-7-17 
     * <p>描述: 根据ID获取子组织</p>
     * @param  organizeID
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    protected List<Map<String, Object>> getChildrenOrg(String organizeID) {
        List<Map<String, Object>> list = Lists.newArrayList();
        try {
            List<OrgInfo> orgInfos = FacadeUtil.getOrgInfoFacade().findChildsByParentId(organizeID);
            for (OrgInfo org : orgInfos) {
                Map<String, Object> item = Maps.newHashMap();
                if ("-1".equals(org.getId())) continue;
                item.put("id", org.getId());
                item.put("name", org.getName());
                item.put("isParent", true);
                item.put("treeType", TYPE_USER.equals(getType()) ? "USER" : "DEPT");
                item.put("nodeType", TYPE_ORG);
                if (isOpen()) item.put("open", true);
                if (isNocheck()) item.put("nocheck", true);
                if (!isAsync()) item.put("children", getChildren(org.getId()));
                if (isCheckDept()) item.put("checked", true);
                list.add(item);
            }
        } catch (Exception e) {
            throw new BusinessException("生成用户树失败！", e);
        }
        
        return (list);
    }
    
    /**
     * qiucs 2014-7-17 
     * <p>描述: 根据ID获取子组织和该组织下的所有用户</p>
     * @param  organizeID
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    protected List<Map<String, Object>> getChildren(String organizeID) {
        List<Map<String, Object>> list = Lists.newArrayList();
        list.addAll(getChildrenOrg(organizeID));
        if (TYPE_USER.equals(getType()) && !"-1".equals(organizeID)) {
            list.addAll(getUsers(organizeID));
        }
        return list;
    }
    
    /**
     * qiucs 2014-7-17 
     * <p>描述: 根据ID获取该组织下的所有用户</p>
     * @param  organizeID
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    protected List<Map<String, Object>> getUsers(String organizeID) {
        List<Map<String, Object>> list = Lists.newArrayList();
        try {
            List<UserInfo> userInfos = FacadeUtil.getUserInfoFacade().findUsersByOrgId(organizeID);
            for (UserInfo user : userInfos) {
                Map<String, Object> item = Maps.newHashMap();
                item.put("id", user.getId());
                item.put("name", user.getName());
                item.put("isParent", false);
                item.put("treeType", "USER");
                item.put("nodeType", TYPE_USER);
                if (isCheckUser()) {
                    item.put("checked", true);
                }
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public boolean isAsync() {
        return async;
    }
    public void setAsync(boolean async) {
        this.async = async;
    }
    public String getChecked() {
        return checked;
    }
    public void setChecked(String checked) {
        this.checked = checked;
    }
    protected boolean isCheckUser() {
        return "all".equals(getChecked()) || "user".equals(getChecked());
    }
    protected boolean isCheckDept() {
        return "all".equals(getChecked()) || "org".equals(getChecked());
    }
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public boolean isNocheck() {
		return nocheck;
	}
	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}
}

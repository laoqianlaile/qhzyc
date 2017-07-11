/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-1-21 上午9:26:00
 * <p>描述: 用户编码工具类</p>
 */
package com.ces.config.application.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author qiucs
 *
 */
public class UserCodeApplication extends CodeApplication {
	
	private static Log log = LogFactory.getLog(UserCodeApplication.class);

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeValue(java.lang.String)
	 */
	@Override
	public String getCodeValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeName(java.lang.String)
	 */
	@Override
	public String getCodeName(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeList()
	 */
	@Override
	public List<Code> getCodeList(String codeTypeCode) {
		List<Code> list = new ArrayList<Code>();
		List<UserInfo> userInfos = FacadeUtil.getUserInfoFacade().findAll();
		int i = 0, len = userInfos.size();
		Code code = null;
		UserInfo user = null;
		for (; i < len; i++) {
			user = userInfos.get(i);
			code = new Code();
			code.setCodeTypeCode(codeTypeCode);
			code.setValue(user.getId());
			code.setName(user.getName());
			code.setShowOrder(i + 1);
			list.add(code);
		}
		code = new Code();
		code.setCodeTypeCode(codeTypeCode);
		code.setValue(String.valueOf(1));
		code.setName("超级管理员");
		code.setShowOrder(i + 1);
		list.add(code);
		code = new Code();
		code.setCodeTypeCode(codeTypeCode);
		code.setValue(String.valueOf(Integer.MIN_VALUE + 1));
		code.setName("系统");
		code.setShowOrder(i + 2);
		list.add(code);
		code = new Code();
		code.setCodeTypeCode(codeTypeCode);
		code.setValue(String.valueOf(Integer.MIN_VALUE));
		code.setName("");
		code.setShowOrder(i + 3);
		list.add(code);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeTree()
	 */
	@Override
	public Object getCodeTree(String codeTypeCode) {
		return getChildrenOrg("-1");
	}

	/* (non-Javadoc)
	 * @see com.ces.config.application.CodeApplication#getCodeTree()
	 */
	@Override
	public Object getCodeGrid(String codeTypeCode) {
		return null;
	}
	
	/**
	 * qiucs 2015-1-21 上午9:42:43
	 * <p>描述: 根据ID获取子组织 </p>
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String, Object>> getChildrenOrg(String organizeID) {
        List<Map<String, Object>> list = Lists.newArrayList();
        try {
            List<OrgInfo> orgInfos = FacadeUtil.getOrgInfoFacade().findChildsByParentId(organizeID);
            List<Map<String, Object>> children = null;
            for (OrgInfo org : orgInfos) {
                Map<String, Object> item = Maps.newHashMap();
                if ("-1".equals(org.getId())) continue;
                item.put("id", "D_" + org.getId());
                item.put("name", org.getName());
                
				// 选择节点时，判断使用（判断是否可以选择）
                item.put("treeType", "USER"); // 树标记：user表示用户组织树
                item.put("nodeType", "D");    // 节点标记：D表示部门
                children = getChildren(org.getId());
                if (children.isEmpty()) {
                	item.put("isParent", false);
                } else {
                	item.put("isParent", true);
                	item.put("children", children);
                }
                list.add(item);
            }
        } catch (Exception e) {
            log.error("生成用户树失败！", e);
        }
        
        return (list);
    }
	
	/**
	 * qiucs 2015-1-21 上午9:42:50
	 * <p>描述: 根据ID获取子组织和该组织下的所有用户 </p>
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String, Object>> getChildren(String organizeID) {
        List<Map<String, Object>> list = Lists.newArrayList();
        list.addAll(getChildrenOrg(organizeID));
        if (!"-1".equals(organizeID)) list.addAll(getUsers(organizeID));
        return list;
    }
	
	/**
	 * qiucs 2015-1-21 上午9:55:25
	 * <p>描述: 根据ID获取该组织下的所有用户 </p>
	 * @return List<Map<String,Object>>
	 */
	private List<Map<String, Object>> getUsers(String organizeID) {
		List<Map<String, Object>> list = Lists.newArrayList();
		try {
			List<UserInfo> userInfos = FacadeUtil.getUserInfoFacade().findUsersByOrgId(organizeID);
			Map<String, Object> item = null;
			for (UserInfo user : userInfos) {
				item = Maps.newHashMap();
				item.put("id", user.getId());
				item.put("name", user.getName());
				item.put("isParent", false);
				// 选择节点时，判断使用（判断是否可以选择）
                item.put("treeType", "USER"); // 树标记：user表示用户组织树
                item.put("nodeType", "U");    // 节点标记：U表示用户
				list.add(item);
			}
			if ("1".equals(organizeID)) {
				item = Maps.newHashMap();
				item.put("id", 1);
				item.put("name", "超级管理员");
				item.put("isParent", false);
				// 选择节点时，判断使用（判断是否可以选择）
                item.put("treeType", "USER"); // 树标记：user表示用户组织树
                item.put("nodeType", "U");    // 节点标记：U表示用户
				list.add(item);
			}
		} catch (Exception e) {
			log.error("系统管理平台用户转为用户树出错", e);
		}
		return list;
	}

}

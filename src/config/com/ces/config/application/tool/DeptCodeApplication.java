/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-1-21 上午9:26:45
 * <p>描述: 组织编码工具类</p>
 */
package com.ces.config.application.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.system.bean.OrgInfo;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author qiucs
 *
 */
public class DeptCodeApplication extends CodeApplication {
	
	private static Log log = LogFactory.getLog(DeptCodeApplication.class);

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
		try {
			List<OrgInfo> orgInfos = FacadeUtil.getOrgInfoFacade().findAll();
			int i = 0, len = orgInfos.size();
			Code code = null;
			for (; i < len; i++) {
				OrgInfo org = orgInfos.get(i);
				code = new Code();
				code.setCodeTypeCode(codeTypeCode);
				code.setValue(String.valueOf(org.getId()));
				code.setName(org.getName());
				code.setShowOrder(i + 1);
				list.add(code);
			}
		} catch (Exception e) {
			log.error("系统管理平台部门转换为编码出错！", e);
		}
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
            for (OrgInfo org : orgInfos) {
                Map<String, Object> item = Maps.newHashMap();
                if ("-1".equals(org.getId())) continue;
                item.put("id", org.getId());
                item.put("name", org.getName());
                item.put("isParent", "0".equals(org.getIsLeaf()));
                item.put("type", "D");
                item.put("children", getChildren(org.getId()));
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
        return list;
    }

}

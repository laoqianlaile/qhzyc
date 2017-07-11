package com.ces.config.datamodel.page.utils;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.config.datamodel.page.coral.CodePageModel;
import com.ces.config.datamodel.page.coral.FormPageModel;
import com.ces.config.datamodel.page.coral.GridPageModel;
import com.ces.config.datamodel.page.coral.OrganizePageModel;
import com.ces.config.datamodel.page.coral.SearchPageModel;
import com.ces.config.datamodel.page.coral.TbarPageModel;
import com.ces.config.utils.StringUtil;

/**
 * <p>描述: 组件库4.0数据模型处理</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-4 下午5:38:22
 *
 */
@Component
public class CoralPageModuleHandling {
    
    protected String getParameter(String param) {
        return ServletActionContext.getRequest().getParameter(param);
    }
    
	/**
	 * qiucs 2014-7-4 
	 * <p>描述: 转换DataGrid数据模型.</p>
	 * @return DefaultPageModel    返回类型   
	 */
	public  FormPageModel form(Class<?> modelClass, String id) {
	    
	    FormPageModel model = new FormPageModel();
	    
	    model.setTimestamp(getParameter(FormPageModel.P_TIMESTAMP));
        model.setMasterTableId(getParameter(FormPageModel.P_M_TABLE_ID));
        model.setProcessInstanceId(getParameter(FormPageModel.P_PROCESS_INSTANCE_ID));
        model.setActivityId(getParameter(FormPageModel.P_ACTIVITY_ID));
	    
        return model;
	}
	
	/**
	 * qiucs 2014-7-4 
	 * <p>描述: 转换Tree数据模型.</p>
	 * @return DefaultPageModel    返回类型   
	 */
	public DefaultPageModel grid(Class<?> modelClass, String id) {
		GridPageModel model = new GridPageModel();

		return model;
	}
	
	/**
	 * qiucs 2014-9-29 
	 * <p>描述: 工具条模型</p>
	 * @return DefaultPageModel    返回类型   
	 * @throws
	 */
	public DefaultPageModel tbar(Class<?> modelClass, String id) {
	    TbarPageModel model = new TbarPageModel();
        
        model.setType(getParameter(TbarPageModel.P_TBAR_TYPE));
        model.setOperation(getParameter(TbarPageModel.P_OP));
        model.setAreaIndex(getParameter(TbarPageModel.P_AREAINDEX));
        
	    return model;
	}
	
	/**
	 * qiucs 2014-7-16 
	 * <p>描述: 检索区配置</p>
	 * @return DefaultPageModel    返回类型   
	 * @throws
	 */
	public DefaultPageModel search(Class<?> modelClass, String id) {
        SearchPageModel model = new SearchPageModel();
        
        //HttpServletRequest request = ServletActionContext.getRequest();
        model.setTimestamp(getParameter(SearchPageModel.P_TIMESTAMP));
        model.setType(getParameter(SearchPageModel.P_TYPE));
        
        return model;
    }
	
	/**
	 * qiucs 2014-7-17 
	 * <p>描述: 代码项下拉框</p>
	 * @param  modelClass
	 * @param  id
	 * @return DefaultPageModel    返回类型   
	 * @throws
	 */
	public DefaultPageModel code(Class<?> modelClass, String id) {
	    CodePageModel model = new CodePageModel();
	    model.setId(id);
	    String empty = getParameter(CodePageModel.P_EMPTY);
	    if (StringUtil.isEmpty(empty)) {
	        model.setEmpty(true);
	    } else {
	    	model.setEmpty(StringUtil.isBooleanTrue(empty));
	    }
	    model.setComboType(getParameter(CodePageModel.P_COMBO_TYPE));
	    model.setSelected(getParameter(CodePageModel.P_SELECTED));
	    return model;
	}
    
    /**
     * qiucs 2014-7-17 
     * <p>描述: 用户树</p>
     * @param  modelClass
     * @param  id
     * @return DefaultPageModel    返回类型   
     * @throws
     */
    public DefaultPageModel user(Class<?> modelClass, String id) {
        OrganizePageModel model = new OrganizePageModel();
        model.setId(id);
        model.setType(OrganizePageModel.TYPE_USER);
        String async = getParameter(OrganizePageModel.P_ASYNC);
        if (StringUtil.isBooleanTrue(async)) {
            model.setAsync(true);
        }
        model.setOpen(StringUtil.isBooleanTrue(getParameter(OrganizePageModel.P_OPEN)));
        model.setNocheck(StringUtil.isBooleanTrue(getParameter(OrganizePageModel.P_NOCHECK)));
        model.setChecked(getParameter(OrganizePageModel.P_CHECKED));
        return model;
    }
    
    /**
     * qiucs 2014-7-17 
     * <p>描述: 组织树</p>
     * @param  modelClass
     * @param  id
     * @return DefaultPageModel    返回类型   
     * @throws
     */
    public DefaultPageModel org(Class<?> modelClass, String id) {
        OrganizePageModel model = new OrganizePageModel();
        model.setId(id);
        model.setType(OrganizePageModel.TYPE_ORG);
        String async = getParameter(OrganizePageModel.P_ASYNC);
        if (StringUtil.isBooleanTrue(async)) {
            model.setAsync(true);
        }
        model.setChecked(getParameter(OrganizePageModel.P_CHECKED));
        
        return model;
    }
	
}

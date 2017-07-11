package com.ces.config.dhtmlx.action.appmanage;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.jpa.domain.Specification;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppSearchConditionDao;
import com.ces.config.dhtmlx.entity.appmanage.AppSearchCondition;
import com.ces.config.dhtmlx.service.appmanage.AppSearchConditionService;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.entity.BaseEntity;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.utils.SearchFilter;
import com.ces.xarch.core.utils.SearchHelper;
import com.ces.xarch.core.utils.ServletUtils;

public class AppSearchConditionController extends ConfigDefineServiceDaoController<AppSearchCondition, AppSearchConditionService, AppSearchConditionDao> {

    private static final long serialVersionUID = -1823535721860777124L;
    
    private static Log log = LogFactory.getLog(AppSearchConditionController.class);
    
    @Override
    protected void initModel() {
        setModel(new AppSearchCondition());
    }

    /**
     * qiucs 2014-3-3 
     * <p>描述: 保存</p>
     */
    public Object save() {
        try {
            model = getService().save(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return NONE;
    }

    /**
     * qiucs 2014-2-28 
     * <p>描述: 查询用户保存过的高级查询条件</p>
     */
    public Object userCondition() {
        try {
            String tableId  = getParameter("P_tableId");
            String moduleId = getParameter("P_moduleId");
            setReturnData(getService().getUserConditions(tableId, moduleId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return NONE;
    }

    @Override
    public <OID extends Serializable, OT extends BaseEntity<OID>> Specification<OT> buildSpecification(Class<OT> otClass)
            throws FatalException {
        HttpServletRequest request = ServletActionContext.getRequest();
        Map<String, SearchFilter> filterMap = SearchFilter.parse(ServletUtils.getParametersStartingWith(request,
                SearchFilter.searchPre));
        filterMap.put("EQ_userId", new SearchFilter("userId", SearchFilter.Operator.EQ, CommonUtil.getUser().getId()));
        return SearchHelper.buildSpecification(filterMap.values(),otClass);
    }
    
    

}

package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppSearchDao;
import com.ces.config.dhtmlx.entity.appmanage.AppSearch;
import com.ces.config.dhtmlx.service.appmanage.AppSearchService;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.exception.FatalException;

public class AppSearchController extends ConfigDefineServiceDaoController<AppSearch, AppSearchService, AppSearchDao> {

    private static final long serialVersionUID = -3719708828839459892L;
    
    private static final Log log = LogFactory.getLog(AppSearchController.class);
    
    @Override
    protected void initModel() {
        setModel(new AppSearch());
    }
    
    /*
     * (非 Javadoc)   
     * <p>标题: setService</p>   
     * <p>描述: 注入服务层(Service)</p>   
     * @param service   
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("appSearchService")
    protected void setService(AppSearchService service) {
        super.setService(service);
    }
    
    /**
     * <p>标题: defaultSearchColumn</p>
     * <p>描述: 处理表中字段为检索的字段</p>
     * @return Object    返回类型   
     * @throws FatalException 
     */
    public Object defaultColumn() throws FatalException {

        try {
            list = getDataModel(getModelTemplate());
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String userId = CommonUtil.getUser().getId();
            list.setData(getService().findDefaultList(tableId, componentVersionId, menuId, userId));
        } catch (Exception e) {
            log.error("可选检索字段出错", e);
        }
        
        return NONE;
    }
    
    /**
     * <p>标题: defineSearchColumn</p>
     * <p>描述: 处理表中字段不是检索的字段</p>
     * @return Object    返回类型   
     * @throws FatalException 
     */
    public Object defineColumn() throws FatalException {

        try {
            list = getDataModel(getModelTemplate());
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String userId = CommonUtil.getUser().getId();
            list.setData(getService().findDefineList(tableId, componentVersionId, menuId, userId));
        } catch (Exception e) {
            log.error("已选检索字段出错", e);
        }
        
        return NONE;
    }
}

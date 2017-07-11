package com.ces.config.dhtmlx.action.appmanage;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppFormElementDao;
import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.config.dhtmlx.service.appmanage.AppFormElementService;
import com.ces.xarch.core.logger.Logger;

public class AppFormElementController extends ConfigDefineServiceDaoController<AppFormElement, AppFormElementService, AppFormElementDao> {

    private static final long serialVersionUID = -4313330037101516397L;
    
    private static Log log = LogFactory.getLog(AppFormElementController.class);

    @Override
    protected void initModel() {
        setModel(new AppFormElement());
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
    @Qualifier("appFormElementService")
    protected void setService(AppFormElementService service) {
        super.setService(service);
    }
    
    @Logger(model="界面字段配置",action="界面可配置字段加载")
    public Object elements() {
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            setReturnData(getService().elements(tableId, componentVersionId, menuId));
        } catch (Exception e) {
            log.error("加载表单页面字段加载出错", e);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2014-12-23 下午4:15:10
     * <p>描述: 获取表单已配置字段 </p>
     * @return Object
     */
    public Object defineColumn() {
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            setReturnData(getService().findDefineList(tableId, componentVersionId, menuId));
        } catch (Exception e) {
            log.error("获取表单已配置字段 出错", e);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

}

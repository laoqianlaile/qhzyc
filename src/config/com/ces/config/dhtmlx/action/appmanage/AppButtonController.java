package com.ces.config.dhtmlx.action.appmanage;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppButtonDao;
import com.ces.config.dhtmlx.entity.appmanage.AppButton;
import com.ces.config.dhtmlx.service.appmanage.AppButtonService;
import com.ces.xarch.core.exception.FatalException;

public class AppButtonController extends ConfigDefineServiceDaoController<AppButton, AppButtonService, AppButtonDao> {

    private static final long serialVersionUID = -5615872005536395078L;
    
    private static Log log = LogFactory.getLog(AppButtonController.class);

    @Override
    protected void initModel() {
        setModel(new AppButton());
    }

    /**
     * qiucs 2013-10-16 
     * <p>描述: </p>
     */
    public Object save() {
        try {
            String tableId   = getParameter("P_tableId");
            String componentVersionId  = getParameter("P_componentVersionId");
            String menuId    = getParameter("P_menuId");
            String type      = getParameter("P_type");
            String rowsValue = getParameter("P_rowsValue");
            setReturnData(getService().save(tableId, componentVersionId, menuId, type, rowsValue));
        } catch (Exception e) {
            log.error("保存按钮配置出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: 清空按钮配置</p>
     */
    public Object clear() {
        try {
            String tableId   = getParameter("P_tableId");
            String componentVersionId  = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String type   = getParameter("P_type");
            setReturnData(getService().clear(tableId, componentVersionId, menuId, type));
        } catch (Exception e) {
        	log.error("清空按钮配置出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-12-23 下午2:28:18
     * <p>描述: 查询已配置按钮 </p>
     * @return Object
     */
    public Object defineButton() throws FatalException {
    	try {
    		String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String type = getParameter("P_type");
            setReturnData(getService().findByFk(tableId, componentVersionId, menuId, type));
		} catch (Exception e) {
			log.error("查询已配置按钮出错", e);
		}
        
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
}

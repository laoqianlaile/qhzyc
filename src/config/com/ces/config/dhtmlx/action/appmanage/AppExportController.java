package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppExportDao;
import com.ces.config.dhtmlx.entity.appmanage.AppExport;
import com.ces.config.dhtmlx.service.appmanage.AppExportService;
import com.ces.xarch.core.exception.FatalException;

public class AppExportController extends ConfigDefineServiceDaoController<AppExport, AppExportService, AppExportDao> {
	
private static final long serialVersionUID = 1L;
    
    private static Log log = LogFactory.getLog(AppExportController.class);

    @Override
    protected void initModel() {
        setModel(new AppExport());
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
    @Qualifier("appExportService")
    protected void setService(AppExportService service) {
        super.setService(service);
    }
    
    /**
     * <p>描述: 显示表中字段未配置需要导出的字段</p>
     * @return Object    返回类型   
     * @throws FatalException 
     */
    public Object defaultColumn() throws FatalException {

        try {
            list = getDataModel(getModelTemplate());
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String columnType = getParameter("P_columnType");
            list.setData(getService().findDefaultList(tableId, componentVersionId, menuId, columnType));
        } catch (Exception e) {
            log.error("可选列表字段出错", e);
        }
        
        return NONE;
    }
    
    /**
     * <p>描述: 显示表中字段已配置需要导出的字段</p>
     * @return Object    返回类型   
     * @throws FatalException 
     */
    public Object defineColumn() throws FatalException {

        try {
            list = getDataModel(getModelTemplate());
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String columnType = getParameter("P_columnType");
            list.setData(getService().findDefineList(tableId, componentVersionId, menuId, columnType));
        } catch (Exception e) {
            log.error("可选列表字段出错", e);
        }
        
        return NONE;
    }
    
    /**
     * <p>描述: 保存查询区配置</p>
     */
    public Object save() {
        try {
            String rowsValue = getParameter("P_rowsValue");
            getService().save(model, rowsValue);
            setReturnData(new MessageModel(Boolean.TRUE, "OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(Boolean.FALSE, "ERROR"));
        }
        return NONE;
    }
    
    /**
     * <p>描述: 验证配置是否存在</p>
     */
    public Object isSet() {
        try {
        	String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            if (CollectionUtils.isNotEmpty(getService().findDefineList(tableId, componentVersionId, menuId, null))) {
            	setReturnData(new MessageModel(Boolean.TRUE, "OK"));
			} else {
				setReturnData(new MessageModel(Boolean.FALSE, "ERROR"));
			}
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(Boolean.FALSE, "ERROR"));
        }
        return NONE;
    }

}

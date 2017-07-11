package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppFilterDao;
import com.ces.config.dhtmlx.entity.appmanage.AppFilter;
import com.ces.config.dhtmlx.service.appmanage.AppFilterService;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

public class AppFilterController extends ConfigDefineServiceDaoController<AppFilter, AppFilterService, AppFilterDao>{

    private static final long serialVersionUID = -6704602479147093427L;
    
    private static Log log = LogFactory.getLog(AppFilterController.class);

    @Override
    protected void initModel() {
        setModel(new AppFilter());
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
    @Qualifier("appFilterService")
    protected void setService(AppFilterService service) {
        super.setService(service);
    }

    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 可选字段</p>
     * @return Object    返回类型   
     */
    public Object defaultColumn() throws FatalException {
        
        try {
            list = getDataModel(getModelTemplate());
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            System.out.println(tableId);
            System.out.println(componentVersionId);
            System.out.println(menuId);
            list.setData(getService().findDefaultList(tableId, componentVersionId, menuId));
        } catch (Exception e) {
            log.error("获取可选字段（过滤条件）出错", e);
        }

        return NONE;
    }
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 已选字段</p>
     * @return Object    返回类型   
     */
    public Object defineColumn() throws FatalException {
        
        try {
            list = getDataModel(getModelTemplate());
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            list.setData(getService().findDefineList(tableId, componentVersionId, menuId));
        } catch (Exception e) {
            log.error("获取已选字段（过滤条件）出错", e);
        }

        return NONE;
    }
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 保存条件过滤配置</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object save() {
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String rowsValue = getParameter("P_rowsValue");
            getService().save(tableId, componentVersionId, menuId, rowsValue);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            log.error("保存条件过滤配置出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2013-10-17 
     * <p>描述: 删除条件过滤配置</p>
     * @return Object    返回类型   
     */
    public Object clear() throws FatalException {
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            getService().clear(tableId, componentVersionId, menuId);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            log.error("删除条件过滤配置出错", e);
        }
        return NONE;
    }
    
    public Object checkSql() {
        String sql = getParameter("P_sql");
        try {
            getService().checkSql(sql);
            setReturnData(Boolean.TRUE);
        } catch (RuntimeException e) {
        	setReturnData(Boolean.FALSE);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    public Object selectOption(){
    	String type = getParameter("P_type");
    	String codeTypeCode = StringUtil.null2empty(getParameter("P_codeTypeCode"));
    	try {
			setReturnData(getService().getSelectOption(type, codeTypeCode));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
}

package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppGreatSearchDao;
import com.ces.config.dhtmlx.entity.appmanage.AppGreatSearch;
import com.ces.config.dhtmlx.service.appmanage.AppGreatSearchService;
import com.ces.xarch.core.exception.FatalException;

public class AppGreatSearchController extends ConfigDefineServiceDaoController<AppGreatSearch, AppGreatSearchService, AppGreatSearchDao> {

	private static final long serialVersionUID = 1L;
	
	private static final Log log = LogFactory.getLog(AppGreatSearchController.class);
	
	@Override
    protected void initModel() {
        setModel(new AppGreatSearch());
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
    @Qualifier("appGreatSearchService")
    protected void setService(AppGreatSearchService service) {
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
            list.setData(getService().findDefaultList(tableId, componentVersionId, menuId));
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
            list.setData(getService().findDefineList(tableId, componentVersionId, menuId));
        } catch (Exception e) {
            log.error("已选检索字段出错", e);
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
     * <p>描述: 清除查询区配置</p>
     */
    public Object clear() throws FatalException {
        try {
            getService().clear(model);
            setReturnData(new MessageModel(Boolean.TRUE, "OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(Boolean.FALSE, "ERROR"));
        }
        return NONE;
    }

}

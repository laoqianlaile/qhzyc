package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppColumnDao;
import com.ces.config.dhtmlx.entity.appmanage.AppColumn;
import com.ces.config.dhtmlx.service.appmanage.AppColumnService;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.exception.FatalException;

public class AppColumnController extends ConfigDefineServiceDaoController<AppColumn, AppColumnService, AppColumnDao> {

    private static final long serialVersionUID = 53023223948280023L;
    
    private static Log log = LogFactory.getLog(AppColumnController.class);

    @Override
    protected void initModel() {
        setModel(new AppColumn());
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
    @Qualifier("appColumnService")
    protected void setService(AppColumnService service) {
        super.setService(service);
    }
    
    /**
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
            log.error("可选列表字段出错", e);
        }
        
        return NONE;
    }
    
    /**
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
            log.error("可选列表字段出错", e);
        }
        
        return NONE;
    }
    
    /**
     * <p>标题: dhtmlxGrid</p>
     * <p>描述: dhtmlxGrid配置信息</p>
     * @return Object    返回类型   
     * @throws FatalException
     */
    public Object dhtmlxGrid() throws FatalException {
        try {
            String tableId  = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String userId = CommonUtil.getUser().getId();
            setReturnData(getService().getDhtmlxGrid(tableId, componentVersionId, menuId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-3-5 
     * <p>描述: 用户个性化设置: 调整宽度</p>
     */
    public Object setUserWidth() {
        try {
            String tableId   = getParameter("P_tableId");
            String componentVersionId  = getParameter("P_componentVersionId");
            String menuId    = getParameter("P_menuId");
            String columnId  = getParameter("P_columnId");
            String showOrder = getParameter("P_showOrder");
            String width     = getParameter("P_width");
            setReturnData(getService().setUserWidth(tableId, componentVersionId, menuId, columnId, showOrder, width));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-3-5 
     * <p>描述: 用户个性化设置: 调整表头位置</p>
     */
    public Object setUserColumnPosition() {
        try {
            String tableId   = getParameter("P_tableId");
            String componentVersionId  = getParameter("P_componentVersionId");
            String menuId    = getParameter("P_menuId");
            String sourcePos = getParameter("P_sourcePos");
            String targetPos = getParameter("P_targetPos");
            setReturnData(getService().setUserColumnPosition(tableId, componentVersionId, menuId, sourcePos, targetPos));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }
}

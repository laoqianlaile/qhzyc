package com.ces.config.dhtmlx.action.appmanage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.action.base.StringIDConfigDefineServiceDaoController;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.AppGridDao;
import com.ces.config.dhtmlx.entity.appmanage.AppGrid;
import com.ces.config.dhtmlx.service.appmanage.AppColumnService;
import com.ces.config.dhtmlx.service.appmanage.AppGridService;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.CommonUtil;

public class AppGridController extends StringIDConfigDefineServiceDaoController<AppGrid, AppGridService, AppGridDao> {

    private static final long serialVersionUID = 2851155033680321041L;
    
    private static Log log = LogFactory.getLog(AppGridController.class);
    
    @Override
    protected void initModel() {
        setModel(new AppGrid());
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
    @Qualifier("appGridService")
    protected void setService(AppGridService service) {
        super.setService(service);
    }
    
    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#edit()
     */
    public Object edit() {
        try {
        	this.findOne();
            String userId = CommonUtil.getUser().getId();
            model = getService().findDefineEntity(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            if (null == model) initModel();
        } catch (Exception e) {
            log.error("编辑出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-9-24 
     * <p>描述: 获取列表配置</p>
     * @throws
     */
    public Object show() {
        try {
        	 String userId = CommonUtil.getUser().getId();
             model = getService().findDefineEntity(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
             if (null == model) initModel();
        } catch (Exception e) {
            log.error("查看出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-9-24 
     * <p>描述: 保存列表配置</p>
     * @throws
     */
    public Object save() {
        try {
            String rowsValue = getParameter("P_rowsValue");
            String isDefault = getParameter("P_isDefault");
            String userId = model.getUserId();
            if (!CommonUtil.SUPER_ADMIN_ID.equals(userId)) userId = CommonUtil.getCurrentUserId();
            setReturnData(getService().save(model, rowsValue, userId, isDefault));
            // 更新缓存
            AppUtil.getInstance().removeAppGrid(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            AppUtil.getInstance().putAppGrid(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId, model);
            AppUtil.getInstance().removeAppColumn(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            List<Object[]> defineColumns = getService(AppColumnService.class).getDefineColumns(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            AppUtil.getInstance().putAppColumn(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId,
                    defineColumns);
        } catch (Exception e) {
            log.error("保存列表配置出错", e);
        }
        return NONE;
    }
    
    /***
     * <p>描述: 保存列表高级配置</p>
     * @return
     */
    public Object saveHighSetting() {
    	try {
    		String userId = CommonUtil.getUser().getId();
    		setReturnData(getService().saveHighSetting(model, userId));
    		// 更新缓存
            AppUtil.getInstance().removeAppGrid(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            AppUtil.getInstance().putAppGrid(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId, model);
            AppUtil.getInstance().removeAppColumn(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            List<Object[]> defineColumns = getService(AppColumnService.class).getDefineColumns(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            AppUtil.getInstance().putAppColumn(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId,
                    defineColumns);
    	}catch (Exception e) {
    		log.error("保存列表高级配置出错", e);
    	}
    	return NONE;
    }
    
    /**
     * qiucs 2014-9-24 
     * <p>描述: 清空列表配置</p>
     */
    public Object clear() {
        try {
        	model.setUserId(CommonUtil.getCurrentUserId());
            setReturnData(getService().clear(model));
        } catch (Exception e) {
            log.error("清空列表配置出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-9-24 
     * <p>描述: 列表宽度用户个性化设置</p>
     */
    public Object setUserWidths() {
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId= getParameter("P_componentVersionId");
            String menuId= getParameter("P_menuId");
            String widths  = getParameter("P_widths");
            String userId  = CommonUtil.getUser().getId();
            MessageModel mm = getService().setUserWidths(tableId, componentVersionId, menuId, userId, widths);
            componentVersionId = mm.getMessage();
            // 更新缓存
            AppUtil.getInstance().removeAppColumn(tableId, componentVersionId, menuId, userId);
            AppUtil.getInstance().putAppColumn(tableId, componentVersionId, menuId, userId,
                    getService(AppColumnService.class).getDefineColumns(tableId, componentVersionId, menuId, userId));
            setReturnData(mm);
        } catch (Exception e) {
            log.error("列表宽度用户个性化设置出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-9-24 
     * <p>描述: 列表表头用户个性化设置</p>
     */
    public Object setUserHeaders() {
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId= getParameter("P_componentVersionId");
            String menuId= getParameter("P_menuId");
            String indexes = getParameter("P_indexes");
            String userId  = CommonUtil.getUser().getId();
            MessageModel mm = (getService().setUserHeaders(tableId, componentVersionId, menuId, userId, indexes));
            componentVersionId = mm.getMessage();
            // 更新缓存
            AppUtil.getInstance().removeAppColumn(tableId, componentVersionId, menuId, userId);
            AppUtil.getInstance().putAppColumn(tableId, componentVersionId, menuId, userId,
                    getService(AppColumnService.class).getDefineColumns(tableId, componentVersionId, menuId, userId));
            setReturnData(mm);
        } catch (Exception e) {
            log.error("列表宽度用户个性化设置出错", e);
        }
        return NONE;
    }
}

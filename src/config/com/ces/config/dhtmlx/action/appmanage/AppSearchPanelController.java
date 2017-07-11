package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppSearchPanelDao;
import com.ces.config.dhtmlx.entity.appmanage.AppSearchPanel;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.service.appmanage.AppSearchPanelService;
import com.ces.config.dhtmlx.service.appmanage.AppSearchService;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.exception.FatalException;

public class AppSearchPanelController extends ConfigDefineServiceDaoController<AppSearchPanel, AppSearchPanelService, AppSearchPanelDao> {

    private static final long serialVersionUID = -3719708828839459892L;
    
    private static Log log = LogFactory.getLog(AppSearchPanelController.class);
    
    @Override
    protected void initModel() {
        setModel(new AppSearchPanel());
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
    @Qualifier("appSearchPanelService")
    protected void setService(AppSearchPanelService service) {
        super.setService(service);
    }
    
    /*
     * (非 Javadoc)   
     * @see com.ces.xarch.core.web.struts2.BaseController#show()
     */
    public Object edit() {
        try {
            String userId = CommonUtil.getUser().getId();
            model = getService().findDefineEntity(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            if (null == model) initModel();
        } catch (Exception e) {
            log.error("编辑检索面板配置信息出错", e);
        }
        return NONE;
    }
    
    /*
     * (非 Javadoc)   
     * @see com.ces.xarch.core.web.struts2.BaseController#show()
     */
    public Object show() {
        try {
            String userId = CommonUtil.getUser().getId();
            model = getService().findDefineEntity(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            if (null == model) initModel();
        } catch (Exception e) {
            log.error("查看检索面板配置信息出错", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 保存查询区配置</p>
     */
    public Object save() {
        try {
            String rowsValue = getParameter("P_rowsValue");
            String userId = model.getUserId();
            if (!CommonUtil.SUPER_ADMIN_ID.equals(userId)) userId = CommonUtil.getCurrentUserId();
            model.setUserId(userId);
            getService().save(model, userId, rowsValue);
            setReturnData(new MessageModel(Boolean.TRUE, "OK"));
            // 更新缓存
            AppUtil.getInstance().removeAppSearchPanel(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            AppUtil.getInstance().putAppSearchPanel(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId, model);
            AppUtil.getInstance().removeAppSearch(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId);
            AppUtil.getInstance().putAppSearch(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId,
                    getService(AppSearchService.class).getDefineColumns(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), userId));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(Boolean.FALSE, "ERROR"));
        }
        return NONE;
    }
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 清除查询区配置</p>
     */
    public Object clear() throws FatalException {
        try {
        	model.setUserId(CommonUtil.getCurrentUserId());
            getService().clear(model);
            setReturnData(new MessageModel(Boolean.TRUE, "OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(Boolean.FALSE, "ERROR"));
        }
        return NONE;
    }
    
}

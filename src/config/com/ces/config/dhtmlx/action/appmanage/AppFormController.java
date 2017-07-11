package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppFormDao;
import com.ces.config.dhtmlx.entity.appmanage.AppForm;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.service.appmanage.AppFormElementService;
import com.ces.config.dhtmlx.service.appmanage.AppFormService;
import com.ces.config.utils.AppUtil;
import com.ces.xarch.core.logger.Logger;

public class AppFormController extends ConfigDefineServiceDaoController<AppForm, AppFormService, AppFormDao> {

    private static final long serialVersionUID = -4313330037101516397L;
    
    private static Log log = LogFactory.getLog(AppFormController.class);

    @Override
    protected void initModel() {
        setModel(new AppForm());
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
    @Qualifier("appFormService")
    protected void setService(AppFormService service) {
        super.setService(service);
    }
    
    /**
     * <p>标题: save</p>
     * <p>描述: 界面配置</p>
     * @return Object    返回类型   
     * @throws
     */
    @Logger(model="表单配置",action="保存表单配置")
    public Object save() {
        try {
            String elementsValue = getParameter("P_elementsValue");
            model = getService().save(model, elementsValue);
            setReturnData(MessageModel.trueInstance("OK"));
            // 更新缓存
            AppUtil.getInstance().removeAppForm(model.getTableId(), model.getComponentVersionId(), model.getMenuId());
            AppUtil.getInstance().putAppForm(model.getTableId(), model.getComponentVersionId(), model.getMenuId(), model);
            AppUtil.getInstance().removeAppFormElement(model.getTableId(), model.getComponentVersionId(), model.getMenuId());
            AppUtil.getInstance().putAppFormElement(model.getTableId(), model.getComponentVersionId(), model.getMenuId(),
                    getService(AppFormElementService.class).getDefineColumns(model.getTableId(), model.getComponentVersionId(), model.getMenuId()));
        } catch (Exception e) {
            log.error("保存表单配置出错", e);
        }
        return NONE;
    }
    
    /**
     * <p>描述: 删除表单配置</p>
     * @return Object    返回类型   
     * @throws
     */
     @Logger(action="删除表单配置")
    public Object clear() {
        try {
            getService().clear(model);
            setReturnData(new MessageModel(Boolean.TRUE, "OK"));
        } catch (Exception e) {
            log.error("删除表单配置出错", e);
        }
        return NONE;
    }

    /*
     * (非 Javadoc)   
     * <p>标题: edit</p>   
     * <p>描述: </p>   
     * @return   
     * @see com.ces.xarch.core.web.struts2.BaseController#edit()
     */
    public Object edit() {
        try {
            model = getService().findDefineEntity(model.getTableId(), model.getComponentVersionId(), model.getMenuId());
            if (null == model) initModel();
        } catch (Exception e) {
            log.error("编辑表单配置出错", e);
        }
        return SUCCESS;
    }

    /*
     * (非 Javadoc)   
     * <p>标题: show</p>   
     * <p>描述: </p>   
     * @return   
     * @see com.ces.xarch.core.web.struts2.BaseController#edit()
     */
    public Object show() {
        try {
            model = getService().findDefineEntity(model.getTableId(), model.getComponentVersionId(), model.getMenuId());
            if (null == model) initModel();
        } catch (Exception e) {
            log.error("查看表单配置出错", e);
        }
        return SUCCESS;
    }
     
    /**
     * <p>标题: copyDefault</p>
     * <p>描述: 复制默认表单配置</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object copyDefault() {
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            getService().copyDefault(tableId, componentVersionId, menuId);
        } catch (Exception e) {
            setStatus(false);
            setLogger(e.getMessage());
        }
        return SUCCESS;
    }
    
    /**
     * <p>标题: dhtmlxForm</p>
     * <p>描述: 预览界面配置</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object dhtmlxForm() {
        try {
            String tableId  = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            setReturnData(getService().getDhtmlxForm(tableId, componentVersionId, menuId));
         } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
         }
         return SUCCESS;
    }
    
    /**
     * <p>标题: domain</p>
     * <p>描述: 预览界面宽度与高度</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object domain() {
        try {
            String tableId  = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            setReturnData(getService().getDomain(tableId, componentVersionId, menuId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return SUCCESS;
    }
    
    /**
     * qiucs 2013-11-18 
     * <p>描述: 表单保存测试</p>
     * @return Object    返回类型   
     * @throws
     */
    
    public Object test() {
        try {
            setReturnData(new MessageModel(Boolean.TRUE, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }
}

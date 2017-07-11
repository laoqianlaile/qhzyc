package com.ces.config.dhtmlx.action.appmanage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.AppSortDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppSort;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.service.appmanage.AppSortService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.exception.FatalException;

public class AppSortController extends ConfigDefineServiceDaoController<AppSort, AppSortService, AppSortDao> {

    private static final long serialVersionUID = 53023223948280023L;

    private static Log log = LogFactory.getLog(AppSortController.class);
    
    @Override
    protected void initModel() {
        setModel(new AppSort());
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
    @Qualifier("appSortService")
    protected void setService(AppSortService service) {
        super.setService(service);
    }
    
    /**
     * <p>标题: defaultSearchColumn</p>
     * <p>描述: 可选排序字段</p>
     * @return Object    返回类型   
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
            log.error("可选排序字段出错", e);
        }
        
        return NONE;
    }
    
    /**
     * <p>描述: 已选排序字段</p>
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
            log.error("获取选排序字段出错", e);
        }
        
        return NONE;
    }
    
    /**
     * <p>标题: saveColumn</p>
     * <p>描述: 保存配置好的列表字段</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object save() {
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String rowsValue = getParameter("P_rowsValue");
            String userId = getParameter("P_userId");
            if (!CommonUtil.SUPER_ADMIN_ID.equals(userId)) userId = CommonUtil.getUser().getId();
            getService().save(tableId, componentVersionId, menuId, userId, rowsValue);
            
            // 当时菜单ID为-1且构件ID不为-1时，配置为基础构件上的（即自义构件上的配置，非组合构件上的配置）
            if (AppDefine.DEFAULT_DEFINE_ID.equals(menuId) && !AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
            	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
            	if (null != version && version.getComponent().getType().equals("9")) {
            		componentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
        		}
            }
            // 更新缓存
            AppUtil.getInstance().removeAppSort(tableId, componentVersionId, menuId, userId);
            AppUtil.getInstance().putAppSort(tableId, componentVersionId, menuId, userId,getService(). getDefineColumns(tableId, componentVersionId, menuId, userId));
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(MessageModel.falseInstance("ERROR"));
        }
        return NONE;
    }
    
    /**
     * <p>标题: clearColumn</p>
     * <p>描述: TODO(这里用一句话描述这个方法的作用)</p>
     * @return Object    返回类型   
     * @throws FatalException
     */
    public Object clear() throws FatalException {
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String userId = CommonUtil.getUser().getId();
            getService().clear(tableId, componentVersionId, menuId, userId);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(MessageModel.falseInstance("ERROR"));
        }
        return NONE;
    }

}

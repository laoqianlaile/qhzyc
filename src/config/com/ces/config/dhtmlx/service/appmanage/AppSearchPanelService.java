package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppSearchPanelDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppSearchPanel;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.exception.FatalException;

@Component
public class AppSearchPanelService extends ConfigDefineDaoService<AppSearchPanel, AppSearchPanelDao> {

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appSearchPanelDao")
    @Override
    protected void setDaoUnBinding(AppSearchPanelDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 根据外键获取配置信息</p>
     * @return AppSearchPanel    返回类型   
     */
    public AppSearchPanel findByFk(String tableId, String componentVersionId, String menuId, String userId) {
        return getDao().findByFk(tableId, componentVersionId, menuId, userId);
    }
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 查找查询区</p>
     */
    public AppSearchPanel findDefineEntity(String tableId, String componentVersionId, String menuId, String userId) {
        // 用户按菜单个性化设置
        AppSearchPanel entity = AppUtil.getInstance().getAppSearchPanel(tableId, componentVersionId, menuId, userId);
        if (null != entity) return entity;
        if (StringUtil.isEmpty(menuId)) {
        	menuId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {            
            if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                // 用户按菜单默认设置
                entity = AppUtil.getInstance().getAppSearchPanel(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
                if (null != entity) return entity;
            }
            if (!CommonUtil.SUPER_ADMIN_ID.equals(userId)) {
                // 超级管理员按菜单个性化设置
                entity = AppUtil.getInstance().getAppSearchPanel(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
                if (null != entity) return entity;
                
                if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                    // 超级管理员按菜单默认设置
                    entity = AppUtil.getInstance().getAppSearchPanel(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
                    if (null != entity) return entity;
                }
            }
        }
        /***************************************(基础构件)***************************************/
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && !WorkflowUtil.isBox(componentVersionId)) {
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
    		}
        	// 超级管理员按构件个性化设置
        	entity = AppUtil.getInstance().getAppSearchPanel(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
        	if (null != entity) return entity;
        }
        /*// 用户按构件个性化设置
        entity = AppUtil.getInstance().getAppSearchPanel(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId);
        if (null != entity) return entity;
        // 用户按构件默认设置
        if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)){
            entity = AppUtil.getInstance().getAppSearchPanel(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, userId);
            if (null != entity) return entity;
        }       
        if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)){
            // 超级管理员按构件个性化设置
            entity = AppUtil.getInstance().getAppSearchPanel(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            if (null != entity) return entity;
            // 超级管理员按构件默认设置
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)){
                entity = AppUtil.getInstance().getAppSearchPanel(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            }
        }*/
        return AppUtil.getInstance().getAppSearchPanel(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 保存查询区配置</p>
     */
    @Transactional
    public void save(AppSearchPanel entity, String userId, String rowsValue) {
        String tableId  = entity.getTableId();
        String componentVersionId = entity.getComponentVersionId();
        String menuId = entity.getMenuId();
        // 当时菜单ID为-1且构件ID不为-1时，配置为基础构件上的（即自义构件上的配置，非组合构件上的配置）
        if (AppDefine.DEFAULT_DEFINE_ID.equals(menuId) && !AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		componentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
        		entity.setComponentVersionId(componentVersionId);
    		}
        }
        AppSearchPanel oldEntity = getDao().findByFk(tableId, componentVersionId, menuId, userId);
        if (null != oldEntity) {
            entity.setId(oldEntity.getId());
        }
        entity.setUserId(userId);
        // 1. save form master configuration
        save(entity);
        // 2. 保存基本检索定义
        getService(AppSearchService.class).save(entity, rowsValue);
        // 3. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, userId, 
                AppDefine.TYPE_SEARCH, AppDefine.DEFINE_YES);
    }
    
    /**
     * liaomingsong 2014-5-8 
     * <p>描述: 复制构件查询区配置信息到目标构件</p>
     */
    @Transactional
    public void copy(AppSearchPanel entity, String userId, String appToComponentVersionId, String rowsValue) {
        String componentVersionId = appToComponentVersionId;
        entity.setId("");
        entity.setComponentVersionId(componentVersionId);
        entity.setUserId(userId);
        // 1. save form master configuration
        save(entity);
        // 2. 
        getService(AppSearchService.class).save(entity, rowsValue);
        // 3. update AppDefine
        getService(AppDefineService.class).updateAppDefine(entity.getTableId(), componentVersionId, entity.getMenuId(), CommonUtil.getUser().getId(), 
                AppDefine.TYPE_SEARCH, AppDefine.DEFINE_YES);
    }
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 清除查询区配置</p>
     * @param  tableId
     * @param  componentVersionId
     */
    @Transactional
    public void clear(AppSearchPanel entity) throws FatalException {
        String tableId = entity.getTableId();
        String componentVersionId= entity.getComponentVersionId();
        String menuId  = entity.getMenuId();
        String userId  = entity.getUserId();
        // 当时菜单ID为-1且构件ID不为-1时，配置为基础构件上的（即自义构件上的配置，非组合构件上的配置）
        if (AppDefine.DEFAULT_DEFINE_ID.equals(menuId) && !AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		componentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
        		entity.setComponentVersionId(componentVersionId);
    		}
        }
        
        // 1. clear all search columns
        if (StringUtil.isEmpty(menuId)) {
            getDao().deleteByFk(tableId, componentVersionId, userId);
        } else {
            getDao().deleteByFk(tableId, componentVersionId, menuId, userId);
        }
        // 2. 清空基本检索定义
        getService(AppSearchService.class).clear(entity);
        // 3. update AppDefine
        if (!getService(AppGreatSearchService.class).judgeStep(tableId, componentVersionId, menuId) && 
        	!getService(AppIntegrationSearchService.class).judgeStep(tableId, componentVersionId, menuId)) {
        	getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, userId, 
                    AppDefine.TYPE_SEARCH, AppDefine.DEFINE_NO);
		}
        
        // 更新缓存
        AppUtil.getInstance().removeAppSearchPanel(entity.getTableId(), componentVersionId, entity.getMenuId(), userId);
        AppUtil.getInstance().removeAppSearch(entity.getTableId(), componentVersionId, entity.getMenuId(), userId);
    }
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        getDao().deleteByTableId(tableId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_SEARCH_PANEL, tableId, AppUtil.TABLE_ID);
    }
    
    /**
     * qiucs 2013-12-26 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        // search column
        getService(AppSearchService.class).deleteByComponentVersionId(componentVersionId);
        // search configuration
        getDao().deleteByComponentVersionId(componentVersionId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_SEARCH_PANEL, componentVersionId, AppUtil.COMPONENT_VERSION_ID);
    }
    
    /**
     * qiucs 2014-12-12 
     * <p>描述: 根据menu ID删除配置</p>
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        // delete app search
        getService(AppSearchService.class).deleteByMenuId(menuId);
        // delete self
        getDao().deleteByMenuId(menuId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_SEARCH_PANEL, menuId, AppUtil.MENU_ID);
    }
    
    /**
     * qiucs 2015-4-27 下午5:43:30
     * <p>描述: 工作流基本检索（从一个版本复制到另一个版本） </p>
     * @return void
     */
    @Transactional
    public void copyWorkflow(String tableId, String fromVersionId, String toVersionId) {
    	List<AppSearchPanel> list = find("EQ_tableId=" + tableId + ";EQ_menuId=" + fromVersionId + ";EQ_userId=" + CommonUtil.SUPER_ADMIN_ID);
    	int i = 0, len = list.size();
    	AppSearchPanel entity = null;
    	Set<String> boxSet  = new HashSet<String>();
    	Iterator<String> it = null;
    	List<AppSearchPanel> destList = new ArrayList<AppSearchPanel>();
    	for (; i < len; i++) {
    		entity = new AppSearchPanel();
    		BeanUtils.copy(list.get(i), entity);
    		entity.setId(null);
    		entity.setMenuId(toVersionId);
    		destList.add(entity);
    		boxSet.add(entity.getComponentVersionId());
    	}
    	list = null;
    	if (!destList.isEmpty()) {
    		save(destList);
    		// copy appsearch
        	getService(AppSearchService.class).copyWorkflow(tableId, fromVersionId, toVersionId);
        	//
        	it = boxSet.iterator();
        	while (it.hasNext()) {
        		getService(AppDefineService.class).updateAppDefine(tableId, it.next(), toVersionId, CommonUtil.SUPER_ADMIN_ID, 
                        AppDefine.TYPE_SEARCH, AppDefine.DEFINE_YES);
    		}
        	it = null;
    	}
    	boxSet = null;
    	destList = null;
    }
    
    /**
     * qiucs 2015-6-26 上午10:42:48
     * <p>描述: 应用到 </p>
     * @return void
     */
    @Transactional
    public void applyTo (String tableId, String componentVersionId, String menuId, String toComponentVersionId, String toMenuId) {
    	String userId = CommonUtil.getCurrentUserId();
    	AppSearchPanel entity = findByFk(tableId, componentVersionId, menuId, userId);
    	
    	AppSearchPanel distEntity = new AppSearchPanel();
    	if (null != entity) {
    		BeanUtils.copy(entity, distEntity);
    		distEntity.setId(null);
    	}
    	distEntity.setTableId(tableId);
    	distEntity.setUserId(userId);
    	distEntity.setComponentVersionId(toComponentVersionId);
    	distEntity.setMenuId(toMenuId);
    	// 清空历史配置
    	clear(distEntity);
    	if (null == entity) return ;
    	// 保存拷贝配置
    	save(distEntity);
    	// 拷贝明细配置
    	getService(AppSearchService.class).applyTo(tableId, componentVersionId, menuId, toComponentVersionId, toMenuId);
    	
    	getService(AppDefineService.class).updateAppDefine(tableId, toComponentVersionId, toMenuId, userId, 
                AppDefine.TYPE_SEARCH, AppDefine.DEFINE_YES);
    }
}

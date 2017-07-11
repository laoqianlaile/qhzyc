package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.AppGridDao;
import com.ces.config.dhtmlx.entity.appmanage.AppColumn;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppGrid;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.service.base.StringIDConfigDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.web.listener.XarchListener;

@Component
public class AppGridService extends StringIDConfigDefineDaoService<AppGrid, AppGridDao> {

    private static Log log = LogFactory.getLog(AppGridService.class);
    
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appGridDao")
    @Override
    protected void setDaoUnBinding(AppGridDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 根据外键来查找</p>
     * @return AppGrid    返回类型   
     */
    public AppGrid findByFk(String tableId, String componentVersionId, String menuId, String userId) {
        return getDao().findByFk(tableId, componentVersionId, menuId, userId);
    }

    /**
     * qiucs 2014-12-11 
     * <p>描述: 获取当前用户的配置信息</p>
     * @return AppGrid    返回类型   
     */
    public AppGrid findDefineEntity(String tableId, String componentVersionId, String menuId, String userId) {
        // 用户按菜单个性化设置
        AppGrid entity = AppUtil.getInstance().getAppGrid(tableId, componentVersionId, menuId, userId);
        if (null != entity || CodeUtil.getInstance().hasCodeType(componentVersionId)) {
        	return entity;
        }
        if (StringUtil.isEmpty(menuId)) {
        	menuId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {            
            if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                // 用户按菜单默认设置
                entity = AppUtil.getInstance().getAppGrid(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
                if (null != entity) return entity;
            }
            if (!CommonUtil.SUPER_ADMIN_ID.equals(userId)) {
                // 超级管理员按菜单个性化设置
                entity = AppUtil.getInstance().getAppGrid(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
                if (null != entity) return entity;
                
                if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                    // 超级管理员按菜单默认设置
                    entity = AppUtil.getInstance().getAppGrid(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
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
        	entity = AppUtil.getInstance().getAppGrid(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
        	if (null != entity) return entity;
        }
        /*// 用户按构件个性化设置
        entity = AppUtil.getInstance().getAppGrid(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId);
        if (null != entity) return entity;
        // 用户按构件默认设置
        if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)){
            entity = AppUtil.getInstance().getAppGrid(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, userId);
            if (null != entity) return entity;
        }       
        if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)){
            // 超级管理员按构件个性化设置
            entity = AppUtil.getInstance().getAppGrid(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            if (null != entity) return entity;
            // 超级管理员按构件默认设置
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)){
                entity = AppUtil.getInstance().getAppGrid(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            }
        }
        return entity;*/
        return AppUtil.getInstance().getAppGrid(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }
    
    /**
     * qiucs 2014-9-24 
     * <p>描述: 保存配置</p>
     * @return MessageModel    返回类型   
     * @throws
     */
    @Transactional
    public MessageModel save(AppGrid entity, String rowsValue, String userId, String isDefault) {
        String tableId  = entity.getTableId();
        String componentVersionId = entity.getComponentVersionId();
        String menuId   = entity.getMenuId();
        int hasRowNumber = entity.getHasRowNumber();
        // 当时菜单ID为-1且构件ID不为-1时，配置为基础构件上的（即自义构件上的配置，非组合构件上的配置）
        if (AppDefine.DEFAULT_DEFINE_ID.equals(menuId) && !AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		componentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
        		entity.setComponentVersionId(componentVersionId);
    		}
        }
        AppGrid oldEntity = getDao().findByFk(tableId, componentVersionId, menuId, userId);
        if (null != oldEntity) {
        	BeanUtils.copy(oldEntity, entity);
        	if (isDefault.equals("1")) {//前台保存配置会改变高级配置
        		entity.setHasRowNumber(hasRowNumber);
			}
        }
        entity.setUserId(userId);
        // 1. save grid
        save(entity);
        // 2. save columns
        getService(AppColumnService.class).save(entity, rowsValue);
        // 3. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, userId, AppDefine.TYPE_COLUMN, AppDefine.DEFINE_YES);
        return MessageModel.trueInstance("OK");
    }
    /***
     * <p>描述: 保存高级配置</p>
     * @param entity
     * @param userId
     * @return 
     */
    @Transactional
    public MessageModel saveHighSetting(AppGrid entity, String userId) {
    	String tableId = entity.getTableId();
    	String componentVersionId = entity.getComponentVersionId();
    	String menuId = entity.getMenuId();
    	AppGrid oldEntity = getDao().findByFk(tableId, componentVersionId, menuId, userId);
    	if( null != oldEntity) {
    		entity.setId(oldEntity.getId());
    	}
    	entity.setUserId(userId);
    	save(entity);
    	AppColumn oldColumn = getService(AppColumnService.class).findOpeColByAppGrid(entity);
    	if (oldColumn != null) {
			AppColumn column = new AppColumn();
			BeanUtils.copy(oldColumn, column);
			column.setShowName(entity.getOpeColName());
			column.setWidth(entity.getOpeColWidth());
			if (!(column.getShowOrder() == 1 && entity.getOpeColPosition() == 0) && !(column.getShowOrder() != 1 && entity.getOpeColPosition() == 1)) {
				getService(AppColumnService.class).updateOpeColPosition(entity, column.getShowOrder());
			}
			getService(AppColumnService.class).save(column);
		}
    	getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, userId, AppDefine.TYPE_COLUMN, AppDefine.DEFINE_YES);
    	return MessageModel.trueInstance("OK");
    }
    
    /**
     * qiucs 2014-9-24 
     * <p>描述: 删除配置</p>
     * @return MessageModel    返回类型   
     * @throws
     */
    @Transactional
    public MessageModel clear(AppGrid entity) {
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
        //
        getDao().deleteByFk(tableId, componentVersionId, menuId, userId);
        //
        getService(AppColumnService.class).clear(entity);
        // 2. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, userId, AppDefine.TYPE_COLUMN, AppDefine.DEFINE_NO);
        
        // 更新缓存
        AppUtil.getInstance().removeAppGrid(tableId, componentVersionId, menuId, userId);
        AppUtil.getInstance().removeAppColumn(tableId, componentVersionId, menuId, userId);
        return MessageModel.trueInstance("OK");
        
    }

    /**
     * qiucs 2014-9-24 
     * <p>描述: 列表宽度用户个性化设置</p>
     */
    @Transactional
    public MessageModel setUserWidths(String tableId, String componentVersionId, String menuId, String userId, String widths) {
        try {
            if (AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            	componentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
            }
            List<AppColumn> list = getService(AppColumnService.class).findUserList(tableId, componentVersionId, menuId, userId);
            AppColumn cEntity = list.get(0);
            boolean copy = false;
            if (!cEntity.getMenuId().equals(menuId) 
            		|| !cEntity.getComponentVersionId().equals(componentVersionId) 
            		|| !cEntity.getUserId().equals(userId)) {
                copy = true;
            }
            AppGrid gEntity    = findDefineEntity(tableId, componentVersionId, menuId, userId);
            AppGrid userEntity = new AppGrid();
            // 初始化用户列表配置或拷贝默认配置
            if (null == gEntity) {
                userEntity.setTableId(tableId);
            } else {
                BeanUtils.copy(gEntity, userEntity);
            }
            userEntity.setComponentVersionId(componentVersionId);
            userEntity.setMenuId(menuId);
            userEntity.setUserId(userId);                
            
            String[] widthArr = widths.split(",");
            //int begin = widthArr.length - list.size();
            int begin = 1, i =0, len = list.size();
            if (1 == gEntity.getHasRowNumber()) {
            	begin = 2;
            }
            boolean linkReserveZoneConfig = isLinkReserveZoneConfig(tableId, componentVersionId);
            /*if (!linkReserveZoneConfig) {
                begin += 1;
            }*/
            if (copy) {
                userEntity.setId(null);
                for (i = 0; i < len; i++) {
                    cEntity = new AppColumn();
                    BeanUtils.copy(list.get(i), cEntity);
                    cEntity.setId(null);
                    cEntity.setComponentVersionId(componentVersionId);
                    if (StringUtil.isEmpty(menuId)) {
                        cEntity.setMenuId(AppDefine.DEFAULT_DEFINE_ID);
                    } else {
                        cEntity.setMenuId(menuId);
                    }
                    cEntity.setUserId(userId);
                    if ((!"3".equals(cEntity.getColumnType()) && !AppColumn.Type.HIDDEN.equals(cEntity.getType())) ||
                    		(linkReserveZoneConfig && "3".equals(cEntity.getColumnType()))) {
                        cEntity.setWidth(Integer.parseInt(widthArr[begin]));
                        begin ++;
                    }
                    list.remove(i);
                    list.add(i, cEntity);
                }
            } else {
                for (i = 0; i < len; i++) {
                    cEntity = list.get(i);
                    if ((!linkReserveZoneConfig && "3".equals(cEntity.getColumnType())) ||
                    		AppColumn.Type.HIDDEN.equals(cEntity.getType())) {
                        continue;
                    }
                    cEntity.setWidth((int)Math.floor(Double.parseDouble(widthArr[begin])));
                    begin ++;
                }
            }
            if (StringUtil.isEmpty(userEntity.getId())) save(userEntity);
            getService(AppColumnService.class).save(list);
            AppDefine appDefine = getService(AppDefineService.class).findByFk(tableId, componentVersionId, menuId, userId);
            if (appDefine == null) {
                appDefine = new AppDefine();
                appDefine.setTableId(tableId);
                appDefine.setComponentVersionId(componentVersionId);
                if (StringUtil.isEmpty(menuId)) {
                    appDefine.setMenuId(AppDefine.DEFAULT_DEFINE_ID);
                } else {
                    appDefine.setMenuId(menuId);
                }
                appDefine.setUserId(userId);
            }
            appDefine.setColumned("1");
            getService(AppDefineService.class).save(appDefine);
        } catch (Exception e) {
            log.error("列表宽度用户个性化设置出错", e);
        }
        return MessageModel.trueInstance(componentVersionId);
    }

    /**
     * qiucs 2014-9-24 
     * <p>描述: 列表宽度用户个性化设置</p>
     */
    @Transactional
    public MessageModel setUserHeaders(String tableId, String componentVersionId, String menuId, String userId, String indexes) {
        try {
            if (AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            	componentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
            }
            List<AppColumn> list = getService(AppColumnService.class).findUserList(tableId, componentVersionId, menuId, userId);
            AppColumn cEntity = list.get(0);
            boolean copy = false;
            if (!cEntity.getMenuId().equals(menuId) 
            		|| !cEntity.getComponentVersionId().equals(componentVersionId) 
            		|| !cEntity.getUserId().equals(userId)) {
                copy = true;
            }
            AppGrid gEntity    = findDefineEntity(tableId, componentVersionId, menuId, userId);
            AppGrid userEntity = new AppGrid();
            if (null == gEntity) {
                userEntity.setTableId(tableId);
            } else {
                BeanUtils.copy(gEntity, userEntity);
            }
            userEntity.setComponentVersionId(componentVersionId);
            userEntity.setUserId(userId);
            userEntity.setMenuId(menuId);
            boolean linkReserveZoneConfig = isLinkReserveZoneConfig(tableId, componentVersionId);
            int begin = 1, differ =0, i =0, len = list.size();
            if (1 == gEntity.getHasRowNumber()) {
            	begin = 2;
            	differ = 1;
            }
            if (copy) {
                userEntity.setId(null);
                for (i = 0; i < len; i++) {
                	/*AppColumn col = list.get(i);
                    if (!linkReserveZoneConfig && "3".equals(col.getColumnType())) {
                        continue;
                    }*/
                    cEntity = new AppColumn();
                    BeanUtils.copy(list.get(i), cEntity);
                    cEntity.setId(null);
                    cEntity.setComponentVersionId(componentVersionId);
                    if (StringUtil.isEmpty(menuId)) {
                        cEntity.setMenuId(AppDefine.DEFAULT_DEFINE_ID);
                    } else {
                        cEntity.setMenuId(menuId);
                    }
                    cEntity.setUserId(userId);
                    cEntity.setShowOrder(i + 1);
                    list.remove(i);
                    list.add(i, cEntity);
                }
            }
            String[] indexArr = indexes.split(",");
            indexArr = (String[])ArrayUtils.subarray(indexArr, begin, indexArr.length);
            Integer showOrder = null;
            Integer[] showOrderArr = new Integer[len];
            for (i = 0; i < len; i++) {
            	showOrderArr[i] = Integer.parseInt(indexArr[i]) - differ;
            	list.get(i).setShowOrder(i + 1);
            }
            for (i = 0; i < len; i++) {
                cEntity = list.get(i);
                if (!linkReserveZoneConfig && "3".equals(cEntity.getColumnType())) {
                	cEntity.setShowOrder(len);
                } else {
                	showOrder = cEntity.getShowOrder();
                	showOrder = ArrayUtils.indexOf(showOrderArr, showOrder) + 1;
                	cEntity.setShowOrder(showOrder);
                }
            }
            if (StringUtil.isEmpty(userEntity.getId())) save(userEntity);
            getService(AppColumnService.class).save(list);
            AppDefine appDefine = getService(AppDefineService.class).findByFk(tableId, componentVersionId, menuId, userId);
            if (appDefine == null) {
                appDefine = new AppDefine();
                appDefine.setTableId(tableId);
                appDefine.setComponentVersionId(componentVersionId);
                if (StringUtil.isEmpty(menuId)) {
                    appDefine.setMenuId(AppDefine.DEFAULT_DEFINE_ID);
                } else {
                    appDefine.setMenuId(menuId);
                }
                appDefine.setUserId(userId);
            }
            appDefine.setColumned("1");
            getService(AppDefineService.class).save(appDefine);
        } catch (Exception e) {
            log.error("列表表头用户个性化设置出错", e);
        }
        return MessageModel.trueInstance(componentVersionId);
    }

    /**
     * 是否配置了超链接
     * 
     * @param tableId 表ID
     * @param componentVersionId 构件版本ID
     * @return boolean
     */
    private boolean isLinkReserveZoneConfig(String tableId, String componentVersionId) {
        List<ConstructDetail> constructDetailList = null;
        ComponentVersion componentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(componentVersionId);
        if (componentVersion != null && ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            Construct construct = XarchListener.getBean(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
            String baseComponentVersionId = construct.getBaseComponentVersionId();
            ComponentVersion baseComponentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(baseComponentVersionId);
            if (baseComponentVersion != null) {
                PhysicalTableDefine physicalTableDefine = XarchListener.getBean(PhysicalTableDefineService.class).getByID(tableId);
                if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                    if (StringUtil.isNotEmpty(physicalTableDefine.getLogicTableCode())) {
                        String commonReserveZoneName = AppDefineUtil.getCommonZoneName(physicalTableDefine.getLogicTableCode(), null, "LINK", "0");
                        ComponentReserveZone commonReserveZone = XarchListener.getBean(ComponentReserveZoneService.class).getCommonReserveZoneByName(commonReserveZoneName);
                        constructDetailList = XarchListener.getBean(ConstructDetailService.class).getByConstructIdAndReserveZoneId(construct.getId(), commonReserveZone.getId());
                    }
                } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(baseComponentVersion.getComponent().getType())) {
                    List<ComponentReserveZone> reserveZoneList = XarchListener.getBean(ComponentReserveZoneService.class).getByComponentVersionId(baseComponentVersionId);
                    if (CollectionUtils.isNotEmpty(reserveZoneList)) {
                        for (ComponentReserveZone reserveZone : reserveZoneList) {
                            if (ConstantVar.Component.ReserveZoneType.GRID_LINK.equals(reserveZone.getType()) && reserveZone.getName().indexOf(tableId) != -1) {
                                constructDetailList = XarchListener.getBean(ConstructDetailService.class).getByConstructIdAndReserveZoneId(construct.getId(), reserveZone.getId());
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * qiucs 2014-12-12 
     * <p>描述: 根据 menu id 删除配置</p>
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        // delete app column
        getService(AppColumnService.class).deleteByMenuId(menuId);
        // delete self
        getDao().deleteByMenuId(menuId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_GRID, menuId, AppUtil.MENU_ID);
    }
    
    /**
     * qiucs 2015-4-27 下午5:43:30
     * <p>描述: 工作流基本检索（从一个版本复制到另一个版本） </p>
     * @return void
     */
    @Transactional
    public void copyWorkflow(String tableId, String fromVersionId, String toVersionId) {
    	List<AppGrid> list = find("EQ_tableId=" + tableId + ";EQ_menuId=" + fromVersionId + ";EQ_userId=" + CommonUtil.SUPER_ADMIN_ID);
    	int i = 0, len = list.size();
    	AppGrid entity = null;
    	Set<String> boxSet  = new HashSet<String>();
    	Iterator<String> it = null;
    	List<AppGrid> destList = new ArrayList<AppGrid>();
    	for (; i < len; i++) {
    		entity = new AppGrid();
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
    		getService(AppColumnService.class).copyWorkflow(tableId, fromVersionId, toVersionId);
    		//
    		it = boxSet.iterator();
    		while (it.hasNext()) {
    			getService(AppDefineService.class).updateAppDefine(tableId, it.next(), toVersionId, CommonUtil.SUPER_ADMIN_ID, 
    					AppDefine.TYPE_COLUMN, AppDefine.DEFINE_YES);
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
    	AppGrid entity = findByFk(tableId, componentVersionId, menuId, userId);
    	
    	AppGrid distEntity = new AppGrid();
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
    	getService(AppColumnService.class).applyTo(tableId, componentVersionId, menuId, toComponentVersionId, toMenuId);
    	
    	getService(AppDefineService.class).updateAppDefine(tableId, toComponentVersionId, toMenuId, userId, 
                AppDefine.TYPE_COLUMN, AppDefine.DEFINE_YES);
    }
}

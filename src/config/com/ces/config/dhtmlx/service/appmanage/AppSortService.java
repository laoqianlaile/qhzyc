package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppSortDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppSort;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

@Component
public class AppSortService extends ConfigDefineDaoService<AppSort, AppSortDao> {
    
    private static Log log = LogFactory.getLog(AppSortService.class);

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appSortDao")
    @Override
    protected void setDaoUnBinding(AppSortDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取排序字段数据
     * @return
     */
    public List<AppSort> findByFk(String tableId, String componentVersionId, String menuId, String userId) {
        return getDao().findByFk(tableId, componentVersionId, menuId, userId);
    }
    
    /**
     * qiucs 2014-4-3
     * <p>标题: judgeStep</p>
     * <p>描述: 判断执行步骤</p>
     * @return boolean    返回类型   
     */
    public boolean judgeStep(String tableId, String componentVersionId, String menuId, String userId) {
        Long count = getDao().count(tableId, componentVersionId, menuId, userId);
        if (count == 0) {
            return false;
        }
        return true;
    }
    
    /**
     * <p>标题: getDefaultColumn</p>
     * <p>描述: 可选排序字段数据</p>
     * @return Object    返回类型   
     * @throws
     */
    private List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId, String userId) {
        // 用户按菜单个性化设置
        if(judgeStep(tableId, componentVersionId, menuId, userId) || CodeUtil.getInstance().hasCodeType(componentVersionId)) {
            return  getDao().getDefaultColumn(tableId, componentVersionId, menuId, userId);
        }
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (StringUtil.isEmpty(menuId)) {
        	menuId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) &&
                    judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId)){
                // 用户按菜单默认设置
                return  getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
            }
            if(!CommonUtil.SUPER_ADMIN_ID.equals(userId) && 
                    judgeStep(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID)) {
                // 超级管理员按菜单个性化设置
                return getDao().getDefaultColumn(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
            }
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && 
                    !CommonUtil.SUPER_ADMIN_ID.equals(userId) && 
                    judgeStep(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID)) {
                // 超级管理员按菜单默认设置
                return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
            }
        }
        /***************************************(基础构件)***************************************/
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && !WorkflowUtil.isBox(componentVersionId)) {
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
			}
        	// 超级管理员按构件个性化设置
        	if(judgeStep(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID)){
        		return  getDao().getDefaultColumn(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
        	}
        }
        /*// 用户按构件个性化设置
        if(judgeStep(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId)){
            return  getDao().getDefaultColumn(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId);
        }
        // 用户按构件默认设置
        if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)){
            if(judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, userId)){
                return  getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, userId);
            }
        }       
        if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)){
            if(judgeStep(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID)){
                // 超级管理员按构件个性化设置
                return getDao().getDefaultColumn(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            }
        }*/
        // 超级管理员按构件默认设置
        return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }
    
    /**
     * <p>标题: getAllDefineColumn</p>
     * <p>描述: 获取所有的列定义，初始化缓存时使用</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    public List<Object[]> getAllDefineColumn() {
        return getDao().getAllDefineColumn();
    }
    
    /**
     * <p>描述: 已选列表字段数据，在更新缓存时使用</p>
     */
    public List<Object[]> getDefineColumns(String tableId, String componentVersionId, String menuId, String userId) {
        return getDao().getDefineColumn(tableId, componentVersionId, menuId, userId);
    }
    
    /**
     * qiucs 2014-4-3 
     * <p>描述: 已选排序字段数据</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    private List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId, String userId) {
     // 用户按菜单个性化设置
        List<Object[]> list = AppUtil.getInstance().getAppSort(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isNotEmpty(list) || CodeUtil.getInstance().hasCodeType(componentVersionId)) {
        	return list;
        }
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
        }
        if (StringUtil.isEmpty(menuId)) {
        	menuId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            // 用户按菜单默认设置
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)){
                list = AppUtil.getInstance().getAppSort(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
                if (CollectionUtils.isNotEmpty(list)) return list;
            }   
            if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)){
                // 超级管理员按菜单个性化设置
                list = AppUtil.getInstance().getAppSort(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
                if (CollectionUtils.isNotEmpty(list)) return list;
                
                if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                    // 超级管理员按菜单默认设置
                    list = AppUtil.getInstance().getAppSort(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
                    if (CollectionUtils.isNotEmpty(list)) return list;
                }
            }
        }
        /***************************************(基础构件)***************************************/
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && !WorkflowUtil.isBox(componentVersionId)) {
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
			}
        	// 超级管理员按构件个性化设置
        	list = AppUtil.getInstance().getAppSort(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
        	if (CollectionUtils.isNotEmpty(list)) return list;
        }
        /*// 用户按构件个性化设置
        list = AppUtil.getInstance().getAppSort(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId);
        if (CollectionUtils.isNotEmpty(list)) return list;
        // 用户按构件默认设置
        if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
            list = AppUtil.getInstance().getAppSort(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, userId);
            if (CollectionUtils.isNotEmpty(list)) return list;
        }       
        if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)) {
            // 超级管理员按构件个性化设置
            list = AppUtil.getInstance().getAppSort(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            if (CollectionUtils.isNotEmpty(list)) return list;
            // 超级管理员按构件默认设置
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                list = AppUtil.getInstance().getAppSort(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            }
        }
        return list;*/
        return AppUtil.getInstance().getAppSort(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 可选排序字段数据(前台列表数据)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppSort> findDefaultList(String tableId, String componentVersionId, String menuId, String userId) {
        List<AppSort> list = new ArrayList<AppSort>();
        
        List<Object[]> rlist = getDefaultColumn(tableId, componentVersionId, menuId, userId);
        
        for(Object[] oArr : rlist) {
            // t_cd.id as column_id, t_cd.show_name
            AppSort item = new AppSort();
            item.setId(UUIDGenerator.uuid());
            item.setColumnId(String.valueOf(oArr[0]));
            item.setShowName(String.valueOf(oArr[1]));
            list.add(item);
        }
        
        return list;
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 已选排序字段数据(前台列表数据)</p>
     * @return List<AppSort> 返回类型   
     * @throws
     */
    public List<AppSort> findDefineList(String tableId, String componentVersionId, String menuId, String userId) {
        List<AppSort> list = new ArrayList<AppSort>();
        
        List<Object[]> rlist = getDefineColumn(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isNotEmpty(rlist)) {
            for(Object[] oArr : rlist) {
                // t_app.id as id, t_cd.id as column_id, t_cd.show_name, t_app.sort_type, t_cd.column_name
                AppSort item = new AppSort();
                item.setId(String.valueOf(oArr[0]));
                item.setColumnId(String.valueOf(oArr[1]));
                item.setShowName(String.valueOf(oArr[2]));
                item.setSortType(String.valueOf(oArr[3]));
                item.setColumnName(String.valueOf(oArr[4]));
                list.add(item);
            }
        }
        return list;
    }
    
    /**
     * <p>描述: 保存列表的配置信息</p>
     */
    @Transactional
    public void save(String tableId, String componentVersionId, String menuId, String userId, String rowsValue) throws FatalException {
        // 1. delete all grid column by table id and menu id

        // 当时菜单ID为-1且构件ID不为-1时，配置为基础构件上的（即自义构件上的配置，非组合构件上的配置）
        if (AppDefine.DEFAULT_DEFINE_ID.equals(menuId) && !AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		componentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
    		}
        }
        getDao().deleteByFk(tableId, componentVersionId, menuId, userId);
        // 2. save grid columns
        String[] rowsArr = rowsValue.split(";");
        AppSort entity = null;
        List<AppSort> list = Lists.newArrayList();
        for (int i = 0; i <rowsArr.length; i++) {
            String oneRowValue = rowsArr[i];
            entity = new AppSort();
            entity.setShowOrder(new Integer(i + 1));
            entity.setTableId(tableId);
            entity.setComponentVersionId(componentVersionId);
            entity.setMenuId(menuId);
            entity.setUserId(userId);
            entity.setOneRowValue(oneRowValue);
            list.add(entity);
        }
        getDao().save(list);
        // 3. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, userId, AppDefine.TYPE_SORT, "1");
    }
    
    /**
     * <p>描述: 清除列表配置信息</p>
     */
    @Transactional
    public void clear(String tableId, String componentVersionId, String menuId, String userId) throws FatalException {
        // 1. clear all grid columns

        // 当时菜单ID为-1且构件ID不为-1时，配置为基础构件上的（即自义构件上的配置，非组合构件上的配置）
        if (AppDefine.DEFAULT_DEFINE_ID.equals(menuId) && !AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		componentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
    		}
        }
        getDao().deleteByFk(tableId, componentVersionId, menuId, userId);
        // 2. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, userId, AppDefine.TYPE_SORT, "0");
        
        // 更新缓存
        AppUtil.getInstance().removeAppSort(tableId, componentVersionId, menuId, userId);
    }

    /**
     * qiucs 2014-12-11 
     * <p>描述: 当前用户的配置信息(实例)</p>
     * @return List<AppSort>    返回类型   
     */
    public List<AppSort> findUserList(String tableId, String componentVersionId, String menuId, String userId) {
     // 用户按菜单个性化设置
        List<AppSort> list = getDao().findByFk(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isNotEmpty(list)) return list;
        if (StringUtil.isEmpty(menuId)) {
        	menuId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                // 用户按菜单默认设置
                list = getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
                if (CollectionUtils.isNotEmpty(list)) return list;
            }       
            if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)){
                // 超级管理员个性化设置
                list = getDao().findByFk(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
                if (CollectionUtils.isNotEmpty(list)) return list;
                // 超级管理员按菜单默认设置
                if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)){
                    list = getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
                    if (CollectionUtils.isNotEmpty(list)) return list;
                }
            }
        }
        // 用户按构件个性设置
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && !WorkflowUtil.isBox(componentVersionId)) {
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
			}
        	// 超级管理员按构件个性化设置
        	list = getDao().findByFk(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
        	if (CollectionUtils.isNotEmpty(list)) return list;
        }
        /*list = getDao().findByFk(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId);
        if (CollectionUtils.isNotEmpty(list)) return list;
        // 用户按构件默认设置
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && !WorkflowUtil.isBox(componentVersionId)) {
            list = getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, userId);
            if (CollectionUtils.isNotEmpty(list)) return list;
        }       
        if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)){
            // 超级管理员按构件个性化设置
            list = getDao().findByFk(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            if (CollectionUtils.isNotEmpty(list)) return list;
            // 超级管理员按构件默认设置
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)){
                list = getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            }
        }*/
        return getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        getDao().deleteByTableId(tableId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_SORT, tableId, AppUtil.TABLE_ID);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_SORT, componentVersionId, AppUtil.COMPONENT_VERSION_ID);
    }
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDao().deleteByComponentVersionId(menuId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_SORT, menuId, AppUtil.MENU_ID);
    }

	/**
	 * qiucs 2015-4-27 下午7:46:55
	 * <p>描述: 工作流基本检索（从一个版本复制到另一个版本） </p>
	 * @return void
	 */
    @Transactional
	public void copyWorkflow(String tableId, String fromVersionId, String toVersionId) {
		List<AppSort> list = find("EQ_tableId=" + tableId + ";EQ_menuId=" + fromVersionId + ";EQ_userId=" + CommonUtil.SUPER_ADMIN_ID);
    	int i = 0, len = list.size();
    	AppSort entity = null;
    	Set<String> boxSet = new HashSet<String>();
    	Iterator<String> it = null;
    	List<AppSort> destList = new ArrayList<AppSort>(); 
    	for (; i < len; i++) {
    		entity = new AppSort();
    		BeanUtils.copy(list.get(i), entity);
    		entity.setId(null);
    		entity.setMenuId(toVersionId);
    		destList.add(entity);
			boxSet.add(entity.getComponentVersionId());
    	}
    	list = null;
    	if (!destList.isEmpty()) {
    		save(destList);
    		//  更新按钮配置标记
    		it = boxSet.iterator();
    		while (it.hasNext()) {
    			getService(AppDefineService.class).updateAppDefine(tableId, it.next(), toVersionId, CommonUtil.SUPER_ADMIN_ID, 
    					AppDefine.TYPE_SORT, AppDefine.DEFINE_YES);
    		}
    		it = null;
    	}
    	boxSet = null;
    	destList = null;
	}
    
    /**
     * qiucs 2015-6-26 上午10:43:47
     * <p>描述: 应用到 </p>
     * @return void
     */
    @Transactional
    public void applyTo(String tableId, String componentVersionId, String menuId, String toComponentVersionId, String toMenuId) {
    	String userId = CommonUtil.getCurrentUserId();
    	List<AppSort> list = findByFk(tableId, componentVersionId, menuId, userId);
    	
    	int i = 0, len = list.size();
    	AppSort entity = null;
    	List<AppSort> destList = new ArrayList<AppSort>();
    	for (; i < len; i++) {
    		entity = new AppSort();
    		BeanUtils.copy(list.get(i), entity);
    		entity.setId(null);
    		entity.setComponentVersionId(toComponentVersionId);
    		entity.setMenuId(toMenuId);
    		destList.add(entity);
    	}
    	list = null;
    	clear(tableId, toComponentVersionId, toMenuId, userId);
    	if (!destList.isEmpty()) { 
    		save(destList);
    		getService(AppDefineService.class).updateAppDefine(tableId, toComponentVersionId, toMenuId, userId, 
					AppDefine.TYPE_SORT, AppDefine.DEFINE_YES);
    	}
    }
}

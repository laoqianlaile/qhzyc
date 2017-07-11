package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import com.ces.config.dhtmlx.dao.appmanage.AppSearchDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppSearch;
import com.ces.config.dhtmlx.entity.appmanage.AppSearchPanel;
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
public class AppSearchService extends ConfigDefineDaoService<AppSearch, AppSearchDao> {

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appSearchDao")
    @Override
    protected void setDaoUnBinding(AppSearchDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * <p>描述: 判断执行步骤</p>
     * @return boolean    返回类型   
     * @throws
     */
    public boolean judgeStep(String tableId, String componentVersionId, String menuId, String userId) {
        Long count = getDao().count(tableId, componentVersionId, menuId, userId);
        if (count == 0) {
            return false;
        }
        return true;
    }
    /**
     * <p>标题: findByFk</p>
     * <p>描述: 获取检索字段信息</p>
     * @param tableId 表定义ID
     * @param componentVersionId 模块ID
     * @return List<AppSearch> 返回类型
     */
    public List<AppSearch> findByFk(String tableId, String componentVersionId, String menuId, String userId) {
        return getDao().findByFk(tableId, componentVersionId, menuId, userId);
    }
    
    /**
     * <p>描述: 可选检索字段数据</p>
     * @return Object    返回类型   
     */
    private List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId, String userId) {
        // 用户按菜单个性化设置
        if(judgeStep(tableId, componentVersionId, menuId, userId) 
        		|| CodeUtil.getInstance().hasCodeType(componentVersionId)) {
            return  getDao().getDefaultColumn(tableId, componentVersionId, menuId, userId);
        }
        if (StringUtil.isEmpty(menuId)) {
        	menuId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && 
                    judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId)) {
                // 用户按菜单默认设置
                return  getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
            }
            if(!CommonUtil.SUPER_ADMIN_ID.equals(userId) &&
                    judgeStep(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID)) {
                // 超级管理员按菜单个性化设置
                return  getDao().getDefaultColumn(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
            }
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) &&
                    !CommonUtil.SUPER_ADMIN_ID.equals(userId) &&
                    judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID)) {
                // 超级管理员按菜单默认设置
                return  getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
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
        	// 超级管理员按基础构件个性化设置
        	if(judgeStep(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId)){
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
     * <p>描述: 已选检索字段数据</p>
     * @return Object    返回类型   
     */
    private List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId, String userId) {
        // 用户按菜单个性化设置
        List<Object[]> list = AppUtil.getInstance().getAppSearch(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isNotEmpty(list) 
        		|| CodeUtil.getInstance().hasCodeType(componentVersionId)) return list;

        if (StringUtil.isEmpty(menuId)) {
        	menuId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if(!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                // 用户按菜单默认化设置
                list = AppUtil.getInstance().getAppSearch(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
                if (CollectionUtils.isNotEmpty(list)) return list;
            }
            if (!CommonUtil.SUPER_ADMIN_ID.equals(userId)) {
                // 超级管理员按菜单个性化设置
                list = AppUtil.getInstance().getAppSearch(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
                if (CollectionUtils.isNotEmpty(list)) return list;
                
                if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                    // 超级管理员按菜单默认设置
                    list = AppUtil.getInstance().getAppSearch(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
                    if (CollectionUtils.isNotEmpty(list)) return list;
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
        	list = AppUtil.getInstance().getAppSearch(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
        	if (CollectionUtils.isNotEmpty(list)) return list;
        }
        /*// 用户按构件个性化设置
        list = AppUtil.getInstance().getAppSearch(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId);
        if (CollectionUtils.isNotEmpty(list)) return list;
        // 用户按构件默认设置
        if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
            list = AppUtil.getInstance().getAppSearch(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, userId);
            if (CollectionUtils.isNotEmpty(list)) return list;
        }       
        if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)) {
            // 超级管理员按构件个性化设置
            list = AppUtil.getInstance().getAppSearch(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            if (CollectionUtils.isNotEmpty(list)) return list;
            // 超级管理员按构件默认设置
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                list = AppUtil.getInstance().getAppSearch(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            }
        }
        return list;*/
        return AppUtil.getInstance().getAppSearch(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 可选列表字段数据(前台列表数据)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppSearch> findDefaultList(String tableId, String componentVersionId, String menuId, String userId) {
        List<AppSearch> list = new ArrayList<AppSearch>();
        List<Object[]> rlt = getDefaultColumn(tableId, componentVersionId, menuId, userId);
        for (Object[] oArr : rlt) {
            // t_cd.id as column_id, t_cd.show_name, t_cd.column_name,t_cd.filter_type
            AppSearch item = new AppSearch();
            item.setId(UUIDGenerator.uuid());
            item.setColumnId(String.valueOf(oArr[0]));
            item.setShowName(String.valueOf(oArr[1]));
            item.setColumnName(String.valueOf(oArr[2]));
            item.setFilterType(StringUtil.null2empty(oArr[3]));
            
            list.add(item);
        }
        
        return list;
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 已选列表字段数据(前台列表数据)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppSearch> findDefineList(String tableId, String componentVersionId, String menuId, String userId) {
        List<AppSearch> list = new ArrayList<AppSearch>();
        List<Object[]> rlt = getDefineColumn(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isEmpty(rlt)) return list;
        for (Object[] oArr : rlt) {
            // t_as.id,  t_cd.id as column_id, t_cd.show_name, t_cd.column_name,t_as.filter_type, t_cd.data_type, t_cd.data_type_extend, t_cd.code_type_code, t_cd.input_type, t_cd.input_option
            AppSearch item = new AppSearch();
            item.setId(String.valueOf(oArr[0]));
            item.setColumnId(String.valueOf(oArr[1]));
            item.setShowName(String.valueOf(oArr[2]));
            item.setColumnName(String.valueOf(oArr[3]));
            item.setFilterType(StringUtil.null2empty(oArr[4]));
            item.setDataType(StringUtil.null2empty(oArr[5]));
            item.setDataTypeExtend(StringUtil.null2empty(oArr[6]));
            item.setCodeTypeCode(StringUtil.null2empty(oArr[7]));
            item.setInputType(StringUtil.null2empty(oArr[8]));
            item.setInputOption(StringUtil.null2empty(oArr[9]));
            list.add(item);
        }
        return list;
    }
    
    /**
     * <p>描述: 保存检索的配置信息</p>
     * @return void    返回类型   
     */
    @Transactional
    public void save(AppSearchPanel master, String rowsValue) throws FatalException {
        String tableId = master.getTableId();
        String componentVersionId= master.getComponentVersionId();
        String menuId  = master.getMenuId();
        String userId  = master.getUserId();
        // 1. delete all search column by table id and menu id
        getDao().deleteByFk(tableId, componentVersionId, menuId, userId);
        // 2. save search columns
        if (StringUtil.isNotEmpty(rowsValue)) {
        	String[] rowsArr = rowsValue.split(";");
        	AppSearch entity = null;
            List<AppSearch> list = Lists.newArrayList();
            for (int i = 0; i <rowsArr.length; i++) {
            	String[] oneRowArray = rowsArr[i].split(",");
                String columnId = oneRowArray[0];
                entity = new AppSearch();
                entity.setTableId(tableId);
                entity.setComponentVersionId(componentVersionId);
                entity.setMenuId(menuId);
                entity.setUserId(userId);
                entity.setColumnId(columnId);
                entity.setFilterType(oneRowArray[1]);
                entity.setShowName(oneRowArray[2]);
                entity.setShowOrder(new Integer(i + 1));
                list.add(entity);
            }
            getDao().save(list);
		}
    }
    
    /**
     * <p>描述: 清除检索配置信息</p>
     * @return void    返回类型   
     */
    @Transactional
    public void clear(AppSearchPanel master) throws FatalException {
        // 1. clear all search columns
        getDao().deleteByFk(master.getTableId(), master.getComponentVersionId(), master.getMenuId(), master.getUserId());
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
        AppUtil.getInstance().removeApp(AppUtil.APP_SEARCH, tableId, AppUtil.TABLE_ID);
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
        AppUtil.getInstance().removeApp(AppUtil.APP_SEARCH, componentVersionId, AppUtil.COMPONENT_VERSION_ID);
    }
    
    /**
     * qiucs 2014-12-12 
     * <p>描述: 根据菜单ID删除配置</p>
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDao().deleteByComponentVersionId(menuId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_SEARCH, menuId, AppUtil.MENU_ID);
    }

	/**
	 * qiucs 2015-4-27 下午6:00:52
	 * <p>描述: 工作流基本检索（从一个版本复制到另一个版本） </p>
	 * @return void
	 */
    @Transactional
	public void copyWorkflow(String tableId, String fromVersionId, String toVersionId) {
		List<AppSearch> list = find("EQ_tableId=" + tableId + ";EQ_menuId=" + fromVersionId + ";EQ_userId=" + CommonUtil.SUPER_ADMIN_ID);
    	int i = 0, len = list.size();
    	AppSearch entity = null;
    	List<AppSearch> destList = new ArrayList<AppSearch>();
    	for (; i < len; i++) {
    		entity = new AppSearch();
    		BeanUtils.copy(list.get(i), entity);
    		entity.setId(null);
    		entity.setMenuId(toVersionId);
    		destList.add(entity);
    	}
    	list = null;
    	if (!destList.isEmpty()) { 
    		save(destList);
    	}
    	destList = null;
	}
    
    /**
     * qiucs 2015-6-26 上午10:43:47
     * <p>描述: 应用到 </p>
     * @return void
     */
    @Transactional
    public void applyTo(String tableId, String componentVersionId, String menuId, String toComponentVersionId, String toMenuId) {
    	List<AppSearch> list = findByFk(tableId, componentVersionId, menuId, CommonUtil.getCurrentUserId());
    	
    	int i = 0, len = list.size();
    	AppSearch entity = null;
    	List<AppSearch> destList = new ArrayList<AppSearch>();
    	for (; i < len; i++) {
    		entity = new AppSearch();
    		BeanUtils.copy(list.get(i), entity);
    		entity.setId(null);
    		entity.setComponentVersionId(toComponentVersionId);
    		entity.setMenuId(toMenuId);
    		destList.add(entity);
    	}
    	list = null;
    	if (!destList.isEmpty()) { 
    		save(destList);
    	}
    }
}

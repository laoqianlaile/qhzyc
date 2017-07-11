package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.AppColumnDao;
import com.ces.config.dhtmlx.dao.appmanage.AppDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.AppFilterDao;
import com.ces.config.dhtmlx.dao.appmanage.AppFormElementDao;
import com.ces.config.dhtmlx.dao.appmanage.AppGreatSearchDao;
import com.ces.config.dhtmlx.dao.appmanage.AppIntegrationSearchDao;
import com.ces.config.dhtmlx.dao.appmanage.AppSearchDao;
import com.ces.config.dhtmlx.dao.appmanage.AppSortDao;
import com.ces.config.dhtmlx.entity.appmanage.AppColumn;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppFilter;
import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.config.dhtmlx.entity.appmanage.AppGreatSearch;
import com.ces.config.dhtmlx.entity.appmanage.AppIntegrationSearch;
import com.ces.config.dhtmlx.entity.appmanage.AppSearch;
import com.ces.config.dhtmlx.entity.appmanage.AppSort;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;

@Component
public class AppDefineService extends ConfigDefineDaoService<AppDefine, AppDefineDao> {
    
    /*
     * (非 Javadoc)   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appDefineDao")
    @Override
    protected void setDaoUnBinding(AppDefineDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiucs 2014-12-10 
     * <p>描述: 按表ID,菜单ID,用户ID获取应用自定义列表</p>
     */
    public Object query(String tableId, String menuId, String userId) {
        // get all define module
        AppDefine distDefaultAppDefine = new AppDefine();
        AppDefine defaultAppDefine     = getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
        if (null == defaultAppDefine && !CommonUtil.SUPER_ADMIN_ID.equals(userId)) {
            defaultAppDefine = getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
        }
        if (null != defaultAppDefine) {
            // 防止数据被更新
            BeanUtils.copy(defaultAppDefine, distDefaultAppDefine);
        } else {
            distDefaultAppDefine.setId(AppDefine.DEFAULT_DEFINE_ID);
            distDefaultAppDefine.setTableId(tableId);
            distDefaultAppDefine.setComponentVersionId(AppDefine.DEFAULT_DEFINE_ID);
            distDefaultAppDefine.setMenuId(menuId);
            distDefaultAppDefine.setUserId(userId);
        }
        // 如果默认设置没有个性化设置
        if (!userId.equals(distDefaultAppDefine.getUserId())) {
            distDefaultAppDefine.setSearched(AppDefine.DEFINE_NO);
            distDefaultAppDefine.setColumned(AppDefine.DEFINE_NO);
            distDefaultAppDefine.setSorted(AppDefine.DEFINE_NO);
        }
        List<AppDefine> list = new ArrayList<AppDefine>();
        list.add(distDefaultAppDefine);
        // 获取用到该表的componentVersionId
        Set<String> componentVersionIdSet = new HashSet<String>();
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
        	distDefaultAppDefine.setName("<font color=red>本菜单默认设置</font>");
        	// 获取菜单ID下所有自定义构件
        	return getMenuComponentList(list, tableId, menuId, userId);
        } else {
            distDefaultAppDefine.setName("<font color=red>所有构件</font>");
            List<Module> mlist = getService(ModuleService.class).getByTableId(tableId);
            for (Module m : mlist) {
                componentVersionIdSet.add(m.getComponentVersionId());
            }
        }
        
        List<Object[]> objList = getDao().findModuleByFk(tableId, menuId, userId);
        for (int i = 0; i < objList.size(); i++) {
            Object[] objArr = objList.get(i);
            // 过滤构件，只保留使用该表的构件
            if (!componentVersionIdSet.contains(StringUtil.null2empty(objArr[1]))) {
                continue;
            }
            AppDefine entity = toAppDefine(objArr, tableId, menuId, userId);
            list.add(entity);
        }

        return list;
    }
    
    /**
     * qiucs 2014-12-19 下午5:50:39
     * <p>描述: 按菜单获取构件 </p>
     * @return List<AppDefine>
     */
    private List<AppDefine> getMenuComponentList(List<AppDefine> list, String tableId, String menuId, String userId) {
    	Map<String, AppDefine> map = new HashMap<String, AppDefine>();
    	List<Object[]> objList = getDao().findMenuByFk(tableId, menuId, userId);
        for (int i = 0; i < objList.size(); i++) {
            Object[] objArr = objList.get(i);
            // 过滤构件，只保留使用该表的构件
            AppDefine entity = toAppDefine(objArr, tableId, menuId, userId);
            map.put(entity.getComponentVersionId(), entity);
        }
        // 获取菜单ID下所有自定义构件
    	String cvId = getService(MenuService.class).getComponentVersionIdById(menuId);
    	
    	ComponentVersion cv = getService(ComponentVersionService.class).getByID(cvId);
    	
    	return getMenuComponentList(list, cv, tableId, 0, map);
    }
    
    /**
     * qiucs 2014-12-19 下午5:51:05
     * <p>描述: 递归获取构件上组装的构件 </p>
     * @return List<AppDefine>
     */
    private List<AppDefine> getMenuComponentList(List<AppDefine> list, ComponentVersion cv, String tableId, int level, Map<String, AppDefine> map) {
    	//if (!ConstantVar.Component.Type.TREE.equals(cv.getComponent().getType())) {
    	if (!getService(ConstructService.class).isTreeComponent(cv.getId())) {
    		boolean contained = getService(ComponentVersionService.class).containTable(cv, tableId, true);
    		if (contained) {
    			list.add(toAppDefine(cv, tableId, level, map));
        		level ++;
    		}
    	}
    	List<ComponentVersion> cvlist = getService(ConstructService.class).getAssemblesOfAssemble(cv.getId());
    	Set<String> cvSet = new HashSet<String>();
    	for (ComponentVersion cvo : cvlist) {
    		if (cvSet.contains(cvo.getId())) continue;
    		cvSet.add(cvo.getId());
    		getMenuComponentList(list, cvo, tableId, level, map);
    	}
    	return list;
    }
    
    /**
     * qiucs 2014-12-19 下午5:51:56
     * <p>描述: 把版本构件封闭成AppDefine对象 </p>
     * @return AppDefine
     */
    private AppDefine toAppDefine(ComponentVersion cv, String tableId, int level, Map<String, AppDefine> map) {
    	AppDefine entity = new AppDefine();
    	if (map.containsKey(cv.getId())) {
    		BeanUtils.copy(map.get(cv.getId()), entity);
    	}
    	entity.setId(UUIDGenerator.uuid());
    	entity.setComponentVersionId(cv.getId());
    	entity.setName(getPrefixSpace(level) + cv.getComponent().getAlias());
    	
    	boolean contained = getService(ComponentVersionService.class).containTable(cv, tableId, false);
    	if (!contained) {
    		entity.disable();
    	}
    	return entity;
    }
    
    /**
     * qiucs 2014-12-19 下午5:53:26
     * <p>描述: 把查询结果封装为AppDefine对象 </p>
     * @return AppDefine
     */
    private AppDefine toAppDefine(Object[] objArr, String tableId, String menuId, String userId) {
    	AppDefine entity = new AppDefine();
        entity.setId(StringUtil.null2empty(objArr[0]));
        entity.setTableId(tableId);
        entity.setComponentVersionId(StringUtil.null2empty(objArr[1]));
        entity.setMenuId(menuId);
        entity.setUserId(userId);
        entity.setName(StringUtil.null2empty(objArr[2]) + "_" + StringUtil.null2empty(objArr[11]));
        entity.setSearched(StringUtil.null2empty(objArr[3]));
        entity.setColumned(StringUtil.null2empty(objArr[4]));
        entity.setSorted(StringUtil.null2empty(objArr[5]));
        entity.setFiltered(StringUtil.null2empty(objArr[6]));
        entity.setGridButtoned(StringUtil.null2empty(objArr[7]));
        entity.setFormed(StringUtil.null2empty(objArr[8]));
        entity.setFormButtoned(StringUtil.null2empty(objArr[9]));
        entity.setReported(StringUtil.null2empty(objArr[10]));
    	return entity;
    }
    
    /**
     * qiucs 2014-12-19 下午5:54:10
     * <p>描述: 获取层级的缩进前缀 </p>
     * @return String
     */
    private String getPrefixSpace(int level) {
    	String str = "";
    	for (int i = 0; i < level; i++) str += "　  ";
    	return str;
    }
    
    /**
     * <p>描述: 根据表ID、模块ID插入或更新AppDefine</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  type
     *         0-检索，1-字段，2-排序，3-列表按钮，4-界面，5-界面按钮，6-列表工具条，7-界面工具条
     * @param  value  
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public AppDefine updateAppDefine(String tableId, String componentVersionId, String menuId, String userId, String type, String value) {
        if (StringUtil.isEmpty(type)) return null;
        
        AppDefine entity =  getDao().findByFk(tableId, componentVersionId, menuId, userId);
        
        if (null == entity) {
            entity = new AppDefine();
            entity.setTableId(tableId);
            entity.setComponentVersionId(componentVersionId);
            entity.setMenuId(menuId);
            entity.setUserId(userId);
        }
        
        if (AppDefine.TYPE_SEARCH.equals(type)) {
            entity.setSearched(value);
        } else if (AppDefine.TYPE_COLUMN.equals(type)) {
            entity.setColumned(value);
        } else if (AppDefine.TYPE_SORT.equals(type)) {
            entity.setSorted(value);
        } else if (AppDefine.TYPE_GRID_BUTTON.equals(type)) {
            entity.setGridButtoned(value);
        } else if (AppDefine.TYPE_FORM.equals(type)) {
            entity.setFormed(value);
        } else if (AppDefine.TYPE_FORM_BUTTON.equals(type)) {
            entity.setFormButtoned(value);
        } else if (AppDefine.TYPE_FILTER.equals(type)) {
            entity.setFiltered(value);
        } else if (AppDefine.TYPE_REPORT.equals(type)) {
            entity.setReported(value);
        }
        return getDao().save(entity);
    }
    
    /**
     * qiucs 2013-9-6 
     * <p>描述: 判断是否使用个性化模板</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  type
     * @return boolean    返回类型   
     * @throws
     */
    public boolean isUseSpecial(String tableId, String componentVersionId, String menuId, String type) {
        
        AppDefine entity = getDao().findByFk(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
        if (null == entity) {
            return false;
        }
        
        if (AppDefine.TYPE_SEARCH.equals(type)) {
            if (AppDefine.DEFINE_YES.equals(entity.getSearched())) {
                return true;
            }
        } else if (AppDefine.TYPE_COLUMN.equals(type)) {
            if (AppDefine.DEFINE_YES.equals(entity.getColumned())) {
                return true;
            }
        } else if (AppDefine.TYPE_SORT.equals(type)) {
            if (AppDefine.DEFINE_YES.equals(entity.getSorted())) {
                return true;
            }
        } else if (AppDefine.TYPE_GRID_BUTTON.equals(type)) {
            if (AppDefine.DEFINE_YES.equals(entity.getGridButtoned())) {
                return true;
            }
        } else if (AppDefine.TYPE_FORM.equals(type)) {
            if (AppDefine.DEFINE_YES.equals(entity.getFormed())) {
                return true;
            }
        } else if (AppDefine.TYPE_FORM_BUTTON.equals(type)) {
            if (AppDefine.DEFINE_YES.equals(entity.getFormButtoned())) {
                return true;
            }
        } else if (AppDefine.TYPE_FILTER.equals(type)) {
            return true;
        } else if (AppDefine.TYPE_REPORT.equals(type)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * qiucs 2013-9-29 
     * <p>描述: 根据表ID、模块ID和用户ID查找对象</p>
     * @return AppDefine    返回类型   
     * @throws
     */
    public AppDefine findByFk(String tableId, String componentVersionId, String menuId, String userId) {
        return getDao().findByFk(tableId, componentVersionId, menuId, userId);
    }
    
    /**
     * qiucs 2012-12-10 
     * <p>描述: 根据表ID、用户ID查询AppDefine</p>
     */
    public List<AppDefine> findByTableIdAndUserId(String tableId, String userId) {
        return getDao().findByTableIdAndUserId(tableId, userId);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        //检索配置
        getService(AppSearchService.class).deleteByTableId(tableId);
        getService(AppSearchPanelService.class).deleteByTableId(tableId);
        //列表配置
        getService(AppColumnService.class).deleteByTableId(tableId);
        //排序配置
        getService(AppSortService.class).deleteByTableId(tableId);
        //过滤配置
        getService(AppFilterService.class).deleteByTableId(tableId);
        /** 应用定义中按钮配置放在构件组装中配置里，此处AppButton的操作删除掉 */
        //按钮配置
        //getService(AppButtonService.class).deleteByTableId(tableId);
        //表单配置
        getService(AppFormService.class).deleteByTableId(tableId);
        //报表配置
        getService(AppReportService.class).deleteByTableId(tableId);
        //应用定义
        getDao().deleteByTableId(tableId);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        //检索配置
        getService(AppSearchPanelService.class).deleteByComponentVersionId(componentVersionId);
        //列表配置
        getService(AppColumnService.class).deleteByComponentVersionId(componentVersionId);
        //排序配置
        getService(AppSortService.class).deleteByComponentVersionId(componentVersionId);
        //过滤配置
        getService(AppFilterService.class).deleteByComponentVersionId(componentVersionId);
        /** 应用定义中按钮配置放在构件组装中配置里，此处AppButton的操作删除掉 */
        //按钮配置
        getService(AppButtonService.class).deleteByComponentVersionId(componentVersionId);
        //表单配置
        getService(AppFormService.class).deleteByComponentVersionId(componentVersionId);
        //报表配置
        getService(AppReportService.class).deleteByComponentVersionId(componentVersionId);
        //应用定义
        getDao().deleteByComponentVersionId(componentVersionId);
    }
    
    /**
     * qiucs 2014-12-12 
     * <p>描述: 根据菜单ID删除应用定义</p>
     */
    public void deleteByMenuId(String menuId) {
        // search
        getService(AppSearchPanelService.class).deleteByMenuId(menuId);
        // grid column
        getService(AppGridService.class).deleteByMenuId(menuId);
        // app sort
        getService(AppSortService.class).deleteByMenuId(menuId);
        // filter
        getService(AppFilterService.class).deleteByMenuId(menuId);
        // form 
        getService(AppFormService.class).deleteByMenuId(menuId);
        // report 
        getService(AppReportService.class).deleteByMenuId(menuId);
        // app button
        getService(AppButtonService.class).deleteByMenuId(menuId);
    }
    
    /**
     * qiucs 2013-12-2 
     * <p>描述: 根据字段ID删除相关的字段配置信息</p>
     * @param  columnId    字段ID
     */
    @Transactional
    public void deleteByColumnId(String columnId) {
        // 列表配置
        getDao(AppColumnDao.class, AppColumn.class).deleteByColumnId(columnId);
        AppUtil.getInstance().deleteColumn(getService(ColumnDefineService.class).getByID(columnId), AppUtil.APP_COLUMN);
        // 过滤条件 
        getDao(AppFilterDao.class, AppFilter.class).deleteByColumnId(columnId);
        // 表单元素
        getDao(AppFormElementDao.class, AppFormElement.class).deleteByColumnId(columnId);
        AppUtil.getInstance().deleteColumn(getService(ColumnDefineService.class).getByID(columnId), AppUtil.APP_FORM_ELEMENT);
        // 检索
        getDao(AppSearchDao.class, AppSearch.class).deleteByColumnId(columnId);
        getDao(AppGreatSearchDao.class,AppGreatSearch.class).deleteByColumnId(columnId);
        getDao(AppIntegrationSearchDao.class,AppIntegrationSearch.class).deleteByColumnId(columnId);
        AppUtil.getInstance().deleteColumn(getService(ColumnDefineService.class).getByID(columnId), AppUtil.APP_SEARCH);
        // 排序
        getDao(AppSortDao.class, AppSort.class).deleteByColumnId(columnId);
        AppUtil.getInstance().deleteColumn(getService(ColumnDefineService.class).getByID(columnId), AppUtil.APP_SORT);
    }

	/**
	 * qiucs 2014-12-22 上午10:48:30
	 * <p>描述: 工作流定义中的应用定义 </p>
	 * @return Object
	 */
	public Object coflowQuery(String workflowVersionId, String userId) {
		List<AppDefine> list = new ArrayList<AppDefine>();
		WorkflowVersion flowVersion = getService(WorkflowVersionService.class).getByID(workflowVersionId);
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(flowVersion.getWorkflowId());
		String tableId = WorkflowUtil.getViewId(flowVersion.getWorkflowId());
		String menuId  = workflowVersionId;
		AppDefine defaultAppDefine = getDefaultAppDefine(tableId/*视图ID*/, menuId/*流程版本ID*/, userId);
		defaultAppDefine.setName("所有工作箱");
		list.add(defaultAppDefine);
		
		Map<String, AppDefine> map = new HashMap<String, AppDefine>();
		// 工作箱对应的应用定义配置
    	List<Object[]> objList = getDao().findMenuByFk(tableId, menuId, userId);
        for (int i = 0; i < objList.size(); i++) {
            Object[] objArr = objList.get(i);
            AppDefine entity = toAppDefine(objArr, tableId, menuId, userId);
            map.put(entity.getComponentVersionId(), entity);
        }
        // 获取该表对应工作流的所有工作箱
        List<String> boxes = getService(WorkflowDefineService.class).getCoflowBoxes(flowDefine);
        
        for (String box : boxes) {
        	AppDefine entity = new AppDefine();
        	if (map.containsKey(box)) {
        		entity = map.get(box);
        	} else {
        		entity.setId(UUIDGenerator.uuid());
        		entity.setTableId(tableId);
        		entity.setComponentVersionId(box);
        		entity.setMenuId(menuId);
        		entity.setUserId(userId);
        	}
        	entity.setName(WorkflowUtil.getBoxName(box));
        	list.add(entity);
        }
		return list;
	}
	
	/**
	 * qiucs 2014-12-23 上午11:41:37
	 * <p>描述: 工作流定义中所有工作箱配置 </p>
	 * @return AppDefine
	 */
	private AppDefine getDefaultAppDefine(String tableId, String menuId, String userId) {
		// get all define module
        AppDefine distDefaultAppDefine = new AppDefine();
        AppDefine defaultAppDefine     = getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
        if (null == defaultAppDefine && !CommonUtil.SUPER_ADMIN_ID.equals(userId)) {
            defaultAppDefine = getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
        }
        if (null != defaultAppDefine) {
            // 防止数据被更新
            BeanUtils.copy(defaultAppDefine, distDefaultAppDefine);
        } else {
            distDefaultAppDefine.setId(AppDefine.DEFAULT_DEFINE_ID);
            distDefaultAppDefine.setTableId(tableId);
            distDefaultAppDefine.setComponentVersionId(AppDefine.DEFAULT_DEFINE_ID);
            distDefaultAppDefine.setMenuId(menuId);
            distDefaultAppDefine.setUserId(userId);
        }
        // 如果默认设置没有个性化设置
        if (!userId.equals(distDefaultAppDefine.getUserId())) {
            distDefaultAppDefine.setSearched(AppDefine.DEFINE_NO);
            distDefaultAppDefine.setColumned(AppDefine.DEFINE_NO);
            distDefaultAppDefine.setSorted(AppDefine.DEFINE_NO);
        }
        return distDefaultAppDefine;
	}

	/**
	 * qiucs 2015-6-26 上午10:27:00
	 * <p>描述: TODO(这里用一句话描述这个方法的作用) </p>
	 * @return Object
	 */
	@Transactional
	public MessageModel appApplyTo(String tableId, String componentVersionId, String menuId, String toMenuIds) {
		MessageModel messageModel = MessageModel.trueInstance("OK");
		try {
			String[] toMenuArr = toMenuIds.split(",");
			String toMenuId = null;
			final String toComponentVersionId = AppDefine.DEFAULT_DEFINE_ID;
			for (int i = 0, len = toMenuArr.length; i < len; i++) {
				toMenuId = toMenuArr[i];
				getService(AppSearchPanelService.class).applyTo(tableId, componentVersionId, menuId, toComponentVersionId, toMenuId);
				getService(AppGridService.class).applyTo(tableId, componentVersionId, menuId, toComponentVersionId, toMenuId);
				getService(AppSortService.class).applyTo(tableId, componentVersionId, menuId, toComponentVersionId, toMenuId);
				getService(AppFormService.class).applyTo(tableId, componentVersionId, menuId, toComponentVersionId, toMenuId);
				getService(AppReportService.class).applyTo(tableId, componentVersionId, menuId, toComponentVersionId, toMenuId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			messageModel = MessageModel.falseInstance("ERROR");
		}
		
		return messageModel;
	}
}

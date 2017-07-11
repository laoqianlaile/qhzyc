package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppIntegrationSearchDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppIntegrationSearch;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

@Component
public class AppIntegrationSearchService extends ConfigDefineDaoService<AppIntegrationSearch, AppIntegrationSearchDao> {
	
	/*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appIntegrationSearchDao")
    @Override
    protected void setDaoUnBinding(AppIntegrationSearchDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * <p>描述: 判断执行步骤</p>
     * @return boolean    返回类型   
     * @throws
     */
    public boolean judgeStep(String tableId, String componentVersionId, String menuId) {
        Long count = getDao().count(tableId, componentVersionId, menuId);
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
     * @return List<AppIntegrationSearch> 返回类型
     */
    public List<AppIntegrationSearch> findByFk(String tableId, String componentVersionId, String menuId) {
        return getDao().findByFk(tableId, componentVersionId, menuId);
    }
    
    /**
     * <p>描述: 可选检索字段数据</p>
     * @return Object    返回类型   
     */
    private List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId) {
    	boolean special = false;
        // 按菜单
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            special = judgeStep(tableId, componentVersionId, menuId);
            if (special) {
                // 按菜单自身设置
                return getDao().getDefaultColumn(tableId, componentVersionId, menuId);
            } 
            special = judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId);
            if (special) {
                // 按菜单默认设置
                return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId);
            }
        }
        // 按构件
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
        	// 基础构件
            String baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
            special = judgeStep(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID);
            if (special) {
                // 按构件自身设置
                return getDao().getDefaultColumn(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID);
            }
        }
        // 按构件默认设置
        return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID);
    }
    
    /**
     * <p>描述: 已选检索字段数据</p>
     * @return Object    返回类型   
     */
    private List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId) {
    	boolean special = false;
        // 按菜单
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            special = judgeStep(tableId, componentVersionId, menuId);
            if (special) {
                // 按菜单自身设置
                return getDao().getDefineColumn(tableId, componentVersionId, menuId);
            } 
            special = judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId);
            if (special) {
                // 按菜单默认设置
                return getDao().getDefineColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId);
            }
        }        
        // 按构件
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
            // 基础构件
            String baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
            special = judgeStep(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID);
            if (special) {
                // 按构件自身设置
                return getDao().getDefineColumn(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID);
            }
        }
        // 按构件默认设置
        return getDao().getDefineColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID);
    }
    
    /**
     * <p>描述: 可选列表字段数据(前台列表数据)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppIntegrationSearch> findDefaultList(String tableId, String componentVersionId, String menuId) {
        List<AppIntegrationSearch> list = new ArrayList<AppIntegrationSearch>();
        List<Object[]> rlt = getDefaultColumn(tableId, componentVersionId, menuId);
        for (Object[] oArr : rlt) {
            // t_cd.id as column_id, t_cd.show_name, t_cd.column_name,t_cd.filter_type
        	AppIntegrationSearch item = new AppIntegrationSearch();
            item.setId(UUIDGenerator.uuid());
            item.setColumnId(String.valueOf(oArr[0]));
            item.setShowName(String.valueOf(oArr[1]));
            item.setColumnName(String.valueOf(oArr[2]));
            
            list.add(item);
        }
        
        return list;
    }
    
    /**
     * <p>描述: 已选列表字段数据(前台列表数据)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppIntegrationSearch> findDefineList(String tableId, String componentVersionId, String menuId) {
        List<AppIntegrationSearch> list = new ArrayList<AppIntegrationSearch>();
        List<Object[]> rlt = getDefineColumn(tableId, componentVersionId, menuId);
        for (Object[] oArr : rlt) {
            // t_as.id,  t_cd.id as column_id, t_cd.show_name, t_cd.column_name,t_as.filter_type, t_cd.data_type, t_cd.code_type_code
        	AppIntegrationSearch item = new AppIntegrationSearch();
            item.setId(String.valueOf(oArr[0]));
            item.setColumnId(String.valueOf(oArr[1]));
            item.setShowName(String.valueOf(oArr[2]));
            item.setColumnName(String.valueOf(oArr[3]));
            
            list.add(item);
        }
        return list;
    }
    
    /**
     * <p>描述: 保存检索的配置信息</p>
     * @return void    返回类型   
     */
    @Transactional
    public void save(AppIntegrationSearch master, String rowsValue) throws FatalException {
        String tableId = master.getTableId();
        String componentVersionId= master.getComponentVersionId();
        String menuId  = master.getMenuId();
        // 1. delete all search column by table id and menu id
        getDao().deleteByFk(tableId, componentVersionId, menuId);
        // 2. save search columns
        String[] rowsArr = rowsValue.split(";");
        AppIntegrationSearch entity = null;
        List<AppIntegrationSearch> list = Lists.newArrayList();
        for (int i = 0; i <rowsArr.length; i++) {
            String columnId = rowsArr[i];
            entity = new AppIntegrationSearch();
            entity.setTableId(tableId);
            entity.setComponentVersionId(componentVersionId);
            entity.setMenuId(menuId);
            entity.setColumnId(columnId);
            entity.setShowOrder(new Integer(i + 1));
            list.add(entity);
        }
        getDao().save(list);
     // update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID, 
                AppDefine.TYPE_SEARCH, AppDefine.DEFINE_YES);
    }
    
    /**
     * <p>描述: 清除查询区配置</p>
     * @param  tableId
     * @param  componentVersionId
     */
    @Transactional
    public void clear(AppIntegrationSearch entity) throws FatalException {
        String tableId = entity.getTableId();
        String componentVersionId= entity.getComponentVersionId();
        String menuId  = entity.getMenuId();
        // 1. 清除高级检索定义
        if (StringUtil.isEmpty(menuId)) {
            getDao().deleteByFk(tableId, componentVersionId);
        } else {
            getDao().deleteByFk(tableId, componentVersionId, menuId);
        }
        // 2. update AppDefine
        if (!getService(AppSearchService.class).judgeStep(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID) && 
        	!getService(AppGreatSearchService.class).judgeStep(tableId, componentVersionId, menuId)) {
        	getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID, 
                    AppDefine.TYPE_SEARCH, AppDefine.DEFINE_NO);
		}
    }
}

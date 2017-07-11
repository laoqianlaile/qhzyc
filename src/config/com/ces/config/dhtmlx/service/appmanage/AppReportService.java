package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppReportDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppReport;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

@Component
public class AppReportService extends ConfigDefineDaoService<AppReport, AppReportDao> {

    /*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appReportDao")
    @Override
    protected void setDaoUnBinding(AppReportDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * <p>描述: 判断执行步骤</p>
     * 
     * @return boolean 返回类型
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
     * <p>标题: getByComponentVersionId</p>
     * <p>描述: 根据模块ID获取报表信息</p>
     * 
     * @param tableId 表定义ID
     * @param componentVersionId 模块ID
     * @return List<AppReport> 返回类型
     */
    public List<AppReport> findByFk(String tableId, String componentVersionId, String menuId, String userId) {
        return getDao().findByFk(tableId, componentVersionId, menuId, userId);
    }

    /**
     * <p>描述: 可选报表数据</p>
     * 
     * @return Object 返回类型
     */
    private List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId, String userId) {
        // 用户按菜单个性化设置
        if (judgeStep(tableId, componentVersionId, menuId, userId)) {
            return getDao().getDefaultColumn(tableId, componentVersionId, menuId, userId);
        }
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId)) {
                // 用户按菜单默认设置
                return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
            }
            if (!CommonUtil.SUPER_ADMIN_ID.equals(userId) && judgeStep(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID)) {
                // 超级管理员按菜单个性化设置
                return getDao().getDefaultColumn(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
            }
            if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && !CommonUtil.SUPER_ADMIN_ID.equals(userId)
                    && judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID)) {
                // 超级管理员按菜单默认设置
                return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
            }
        }
        /*************************************** (基础构件) ***************************************/
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion entity = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (entity.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
			}
            // 超级管理员按基础构件个性化设置
            if (judgeStep(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId)) {
                return getDao().getDefaultColumn(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            }
        }
        // 超级管理员按构件默认设置
        return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }

    /**
     * <p>描述: 已选报表数据</p>
     * 
     * @return Object 返回类型
     */
    private List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId, String userId) {
        // 用户按菜单个性化设置
        List<Object[]> list = getDao().getDefineColumn(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isNotEmpty(list))
            return list;

        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                // 用户按菜单默认化设置
                list = getDao().getDefineColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
                if (CollectionUtils.isNotEmpty(list))
                    return list;
            }
            if (!CommonUtil.SUPER_ADMIN_ID.equals(userId)) {
                // 超级管理员按菜单个性化设置
                list = getDao().getDefineColumn(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
                if (CollectionUtils.isNotEmpty(list))
                    return list;

                if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                    // 超级管理员按菜单默认设置
                    list = getDao().getDefineColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
                    if (CollectionUtils.isNotEmpty(list))
                        return list;
                }
            }
        }
        /*************************************** (基础构件) ***************************************/
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion entity = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (entity.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
			}
            list = getDao().getDefineColumn(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            if (CollectionUtils.isNotEmpty(list))
                return list;
        }
        return getDao().getDefineColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }

    /**
     * <p>描述: 可选列表字段数据(前台列表数据)</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public List<AppReport> findDefaultList(String tableId, String componentVersionId, String menuId, String userId) {
        List<AppReport> list = new ArrayList<AppReport>();
        List<Object[]> rlt = getDefaultColumn(tableId, componentVersionId, menuId, userId);
        for (Object[] oArr : rlt) {
            AppReport item = new AppReport();
            item.setId(UUIDGenerator.uuid());
            item.setReportId(String.valueOf(oArr[0]));
            item.setShowName(String.valueOf(oArr[1]));
            list.add(item);
        }
        return list;
    }

    /**
     * <p>描述: 已选列表字段数据(前台列表数据)</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public List<AppReport> findDefineList(String tableId, String componentVersionId, String menuId, String userId) {
        List<AppReport> list = new ArrayList<AppReport>();
        List<Object[]> rlt = getDefineColumn(tableId, componentVersionId, menuId, userId);
        for (Object[] oArr : rlt) {
            AppReport item = new AppReport();
            item.setId(String.valueOf(oArr[0]));
            item.setReportId(String.valueOf(oArr[1]));
            item.setShowName(String.valueOf(oArr[2]));
            list.add(item);
        }
        return list;
    }

    /**
     * <p>描述: 保存报表的配置信息</p>
     * 
     * @return void 返回类型
     */
    @Transactional
    public void save(String tableId, String componentVersionId, String menuId, String rowsValue) throws FatalException {
        // 1. delete all search column by table id and menu id
        getDao().deleteByFk(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
        // 2. save search columns
        String[] rowsArr = rowsValue.split(";");
        AppReport entity = null;
        List<AppReport> list = Lists.newArrayList();
        for (int i = 0; i < rowsArr.length; i++) {
            String reportId = rowsArr[i];
            entity = new AppReport();
            entity.setTableId(tableId);
            entity.setComponentVersionId(componentVersionId);
            entity.setMenuId(menuId);
            //entity.setUserId(CommonUtil.getUser().getId());
            entity.setUserId(CommonUtil.SUPER_ADMIN_ID);   // 超级系统管理员ID
            entity.setReportId(reportId);
            entity.setShowOrder(new Integer(i + 1));
            list.add(entity);
        }
        getDao().save(list);
        // update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID, AppDefine.TYPE_REPORT,
                AppDefine.DEFINE_YES);
    }

    /**
     * <p>描述: 清除检索配置信息</p>
     * 
     * @return void 返回类型
     */
    @Transactional
    public void clear(String tableId, String componentVersionId, String menuId) throws FatalException {
        // 1. clear all search columns
        getDao().deleteByFk(tableId, componentVersionId, menuId, CommonUtil.getUser().getId());
        // 2. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID, AppDefine.TYPE_REPORT,
                AppDefine.DEFINE_NO);
    }

    /**
     * <p>描述: 根据表ID删除配置</p>
     * 
     * @param tableId 设定参数
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        getDao().deleteByTableId(tableId);
    }

    /**
     * <p>描述: 根据模块ID删除配置</p>
     * 
     * @param componentVersionId 设定参数
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
    }

    /**
     * <p>描述: 根据菜单ID删除配置</p>
     * 
     * @param componentVersionId 设定参数
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDao().deleteByComponentVersionId(menuId);
    }
    
    /**
     * qiucs 2015-6-26 上午10:43:47
     * <p>描述: 应用到 </p>
     * @return void
     */
    @Transactional
    public void applyTo(String tableId, String componentVersionId, String menuId, String toComponentVersionId, String toMenuId) {
    	StringBuilder sb = new StringBuilder(50);
    	sb.append("EQ_tableId=").append(tableId)
    	  .append(";EQ_componentVersionId=").append(componentVersionId)
    	  .append(";EQ_menuId=").append(menuId);
    	List<AppReport> list = find(sb.toString());
    	
    	int i = 0, len = list.size();
    	AppReport entity = null;
    	List<AppReport> destList = new ArrayList<AppReport>();
    	for (; i < len; i++) {
    		entity = new AppReport();
    		BeanUtils.copy(list.get(i), entity);
    		entity.setId(null);
    		entity.setComponentVersionId(toComponentVersionId);
    		entity.setMenuId(toMenuId);
    		destList.add(entity);
    	}
    	list = null;
    	clear(tableId, toComponentVersionId, toMenuId);
    	if (!destList.isEmpty()) { 
    		save(destList);
    		// 2. update AppDefine
            getService(AppDefineService.class).updateAppDefine(tableId, toComponentVersionId, toMenuId, CommonUtil.SUPER_ADMIN_ID,
            		AppDefine.TYPE_REPORT, AppDefine.DEFINE_YES);
    	}
    }
}

package com.ces.config.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.sf.ehcache.Cache;

import org.apache.commons.collections.CollectionUtils;

import com.ces.config.dhtmlx.entity.appmanage.AppForm;
import com.ces.config.dhtmlx.entity.appmanage.AppGrid;
import com.ces.config.dhtmlx.entity.appmanage.AppSearchPanel;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.service.appmanage.AppColumnService;
import com.ces.config.dhtmlx.service.appmanage.AppFormElementService;
import com.ces.config.dhtmlx.service.appmanage.AppFormService;
import com.ces.config.dhtmlx.service.appmanage.AppGridService;
import com.ces.config.dhtmlx.service.appmanage.AppSearchPanelService;
import com.ces.config.dhtmlx.service.appmanage.AppSearchService;
import com.ces.config.dhtmlx.service.appmanage.AppSortService;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * 应用定义权限，key都是tableId$componentVersionId$menuId$userId
 */
public class AppUtil {

    /** * AppSearchPanel在Ehcache中的cache名称 */
    public static final String APP_SEARCH_PANEL = "APP_SEARCH_PANEL";

    /** * AppSearch在Ehcache中的cache名称 */
    public static final String APP_SEARCH = "APP_SEARCH";

    /** * AppGrid在Ehcache中的cache名称 */
    public static final String APP_GRID = "APP_GRID";

    /** * AppColumn在Ehcache中的cache名称 */
    public static final String APP_COLUMN = "APP_COLUMN";

    /** * AppSort在Ehcache中的cache名称 */
    public static final String APP_SORT = "APP_SORT";

    /** * AppForm在Ehcache中的cache名称 */
    public static final String APP_FORM = "APP_FORM";

    /** * AppFormElement在Ehcache中的cache名称 */
    public static final String APP_FORM_ELEMENT = "APP_FORM_ELEMENT";

    public static final String TABLE_ID = "1";

    public static final String COMPONENT_VERSION_ID = "2";

    public static final String MENU_ID = "3";

    public static final String USER_ID = "4";

    private static String split = "$";

    private static AppUtil instance = null;

    private AppUtil() {

    }

    public static AppUtil getInstance() {
        if (instance == null) {
            synchronized (AuthorityUtil.class) {
                if (instance == null) {
                    instance = new AppUtil();
                }
            }
        }
        return instance;
    }

    private String getKey(String tableId, String componentVersionId, String menuId, String userId) {
        return tableId + split + componentVersionId + split + menuId + split + userId;
    }

    private String getKey(String tableId, String componentVersionId, String menuId) {
        return tableId + split + componentVersionId + split + menuId;
    }

    public AppSearchPanel getAppSearchPanel(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        return (AppSearchPanel) EhcacheUtil.getCache(APP_SEARCH_PANEL, key);
    }

    public void putAppSearchPanel(String tableId, String componentVersionId, String menuId, String userId, AppSearchPanel appSearchPanel) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        EhcacheUtil.setCache(APP_SEARCH_PANEL, key, appSearchPanel);
    }

    public void removeAppSearchPanel(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        EhcacheUtil.removeCache(APP_SEARCH_PANEL, key);
    }

    @SuppressWarnings("unchecked")
    public Vector<Object[]> getAppSearch(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        return (Vector<Object[]>) EhcacheUtil.getCache(APP_SEARCH, key);
    }

    public void putAppSearch(String tableId, String componentVersionId, String menuId, String userId, List<Object[]> appSearchList) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        Vector<Object[]> appSearchVector =  new Vector<Object[]>(appSearchList);
        EhcacheUtil.setCache(APP_SEARCH, key, appSearchVector);
    }

    public void putAppSearch(String tableId, String componentVersionId, String menuId, String userId, Object[] appSearch) {
        Vector<Object[]> appSearchVector = getAppSearch(tableId, componentVersionId, menuId, userId);
        if (appSearchVector == null) {
            appSearchVector = new Vector<Object[]>();
            appSearchVector.add(appSearch);
            putAppSearch(tableId, componentVersionId, menuId, userId, appSearchVector);
        } else {
        	appSearchVector.add(appSearch);
		}
    }

    public void removeAppSearch(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        EhcacheUtil.removeCache(APP_SEARCH, key);
    }

    public AppGrid getAppGrid(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        return (AppGrid) EhcacheUtil.getCache(APP_GRID, key);
    }

    public void putAppGrid(String tableId, String componentVersionId, String menuId, String userId, AppGrid appGrid) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        EhcacheUtil.setCache(APP_GRID, key, appGrid);
    }

    public void removeAppGrid(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        EhcacheUtil.removeCache(APP_GRID, key);
    }

    @SuppressWarnings("unchecked")
    public Vector<Object[]> getAppColumn(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        return (Vector<Object[]>) EhcacheUtil.getCache(APP_COLUMN, key);
    }

    public void putAppColumn(String tableId, String componentVersionId, String menuId, String userId, List<Object[]> appColumnList) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        Vector<Object[]> appColumnVector = new Vector<Object[]>(appColumnList);
        EhcacheUtil.setCache(APP_COLUMN, key, appColumnVector);
    }

    public void putAppColumn(String tableId, String componentVersionId, String menuId, String userId, Object[] appColumn) {
        Vector<Object[]> appColumnVector = getAppColumn(tableId, componentVersionId, menuId, userId);
        if (appColumnVector == null) {
            appColumnVector = new Vector<Object[]>();
            appColumnVector.add(appColumn);
            putAppColumn(tableId, componentVersionId, menuId, userId, appColumnVector);
        } else {
        	appColumnVector.add(appColumn);
		}
    }

    public void removeAppColumn(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        EhcacheUtil.removeCache(APP_COLUMN, key);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> getAppSort(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        return (List<Object[]>) EhcacheUtil.getCache(APP_SORT, key);
    }

    public void putAppSort(String tableId, String componentVersionId, String menuId, String userId, List<Object[]> appSortList) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        Vector<Object[]> appSortVector = new Vector<Object[]>(appSortList);
        EhcacheUtil.setCache(APP_SORT, key, appSortVector);
    }

    public void putAppSort(String tableId, String componentVersionId, String menuId, String userId, Object[] appSort) {
        List<Object[]> appSorts = getAppSort(tableId, componentVersionId, menuId, userId);
        if (appSorts == null) {
            appSorts = new Vector<Object[]>();
            appSorts.add(appSort);
            String key = getKey(tableId, componentVersionId, menuId, userId);
            EhcacheUtil.setCache(APP_SORT, key, appSorts);
        } else {
        	appSorts.add(appSort);
		}
    }

    public void removeAppSort(String tableId, String componentVersionId, String menuId, String userId) {
        String key = getKey(tableId, componentVersionId, menuId, userId);
        EhcacheUtil.removeCache(APP_SORT, key);
    }

    public AppForm getAppForm(String tableId, String componentVersionId, String menuId) {
        String key = getKey(tableId, componentVersionId, menuId);
        return (AppForm) EhcacheUtil.getCache(APP_FORM, key);
    }

    public void putAppForm(String tableId, String componentVersionId, String menuId, AppForm appForm) {
        String key = getKey(tableId, componentVersionId, menuId);
        EhcacheUtil.setCache(APP_FORM, key, appForm);
    }

    public void removeAppForm(String tableId, String componentVersionId, String menuId) {
        String key = getKey(tableId, componentVersionId, menuId);
        EhcacheUtil.removeCache(APP_FORM, key);
    }

    @SuppressWarnings("unchecked")
    public Vector<Object[]> getAppFormElement(String tableId, String componentVersionId, String menuId) {
        String key = getKey(tableId, componentVersionId, menuId);
        return (Vector<Object[]>) EhcacheUtil.getCache(APP_FORM_ELEMENT, key);
    }

    public void putAppFormElement(String tableId, String componentVersionId, String menuId, List<Object[]> appFormElementList) {
        String key = getKey(tableId, componentVersionId, menuId);
        Vector<Object[]> appFormElementVector = new Vector<Object[]>(appFormElementList);
        EhcacheUtil.setCache(APP_FORM_ELEMENT, key, appFormElementVector);
    }

    public void putAppFormElement(String tableId, String componentVersionId, String menuId, Object[] appFormElement) {
        Vector<Object[]> appFormElementVector = getAppFormElement(tableId, componentVersionId, menuId);
        if (appFormElementVector == null) {
            appFormElementVector = new Vector<Object[]>();
            appFormElementVector.add(appFormElement);
            putAppFormElement(tableId, componentVersionId, menuId, appFormElementVector);
        } else {
        	appFormElementVector.add(appFormElement);
		}
    }

    public void removeAppFormElement(String tableId, String componentVersionId, String menuId) {
        String key = getKey(tableId, componentVersionId, menuId);
        EhcacheUtil.removeCache(APP_FORM_ELEMENT, key);
    }

    @SuppressWarnings("unchecked")
    public void removeApp(String cacheName, String subKey, String type) {
        Cache cache = EhcacheUtil.getCache(cacheName);
        if (cache != null) {
            List<String> keys = cache.getKeys();
            if (CollectionUtils.isNotEmpty(keys)) {
                for (String key : keys) {
                    String[] strs = key.split(split);
                    if (strs.length == 4) {
                        if (TABLE_ID.equals(type) && strs[0].equals(subKey)) {
                            cache.remove(key);
                        } else if (COMPONENT_VERSION_ID.equals(type) && strs[1].equals(subKey)) {
                            cache.remove(key);
                        } else if (MENU_ID.equals(type) && strs[2].equals(subKey)) {
                            cache.remove(key);
                        } else if (USER_ID.equals(type) && strs[3].equals(subKey)) {
                            cache.remove(key);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 2015-4-30 下午2:34:11
     * <p>描述: 加载应用定义缓存 </p>
     * @return void
     */
    public void load() {
    	AppSearchPanelService appSearchPanelService =  XarchListener.getBean(AppSearchPanelService.class);
        List<AppSearchPanel> appSearchPanelList = appSearchPanelService.findAll();
        if (CollectionUtils.isNotEmpty(appSearchPanelList)) {
            for (AppSearchPanel appSearchPanel : appSearchPanelList) {
                putAppSearchPanel(appSearchPanel.getTableId(), appSearchPanel.getComponentVersionId(), appSearchPanel.getMenuId(),
                        appSearchPanel.getUserId(), appSearchPanel);
            }
        }
        AppSearchService appSearchService = XarchListener.getBean(AppSearchService.class);
        List<Object[]> appSearchList = appSearchService.getAllDefineColumn();
        if (CollectionUtils.isNotEmpty(appSearchList)) {
            for (Object[] objs : appSearchList) {
                putAppSearch(String.valueOf(objs[10]), String.valueOf(objs[11]), String.valueOf(objs[12]), String.valueOf(objs[13]), objs);
            }
        }
        AppGridService appGridService = XarchListener.getBean(AppGridService.class);
        List<AppGrid> appGridList = appGridService.findAll();
        if (CollectionUtils.isNotEmpty(appGridList)) {
            for (AppGrid appGrid : appGridList) {
                putAppGrid(appGrid.getTableId(), appGrid.getComponentVersionId(), appGrid.getMenuId(), appGrid.getUserId(), appGrid);
            }
        }
        AppColumnService appColumnService = XarchListener.getBean(AppColumnService.class);
        List<Object[]> appColumnList = appColumnService.getAllDefineColumn();
        if (CollectionUtils.isNotEmpty(appColumnList)) {
            for (Object[] objs : appColumnList) {
                putAppColumn(String.valueOf(objs[13]), String.valueOf(objs[14]), String.valueOf(objs[15]), String.valueOf(objs[16]), objs);
            }
        }
        AppSortService appSortService = XarchListener.getBean(AppSortService.class);
        List<Object[]> appSortList = appSortService.getAllDefineColumn();
        if (CollectionUtils.isNotEmpty(appSortList)) {
            for (Object[] objs : appSortList) {
                putAppSort(String.valueOf(objs[5]), String.valueOf(objs[6]), String.valueOf(objs[7]), String.valueOf(objs[8]), objs);
            }
        }
        AppFormService appFormService = XarchListener.getBean(AppFormService.class);
        List<AppForm> appFormList = appFormService.findAll();
        if (CollectionUtils.isNotEmpty(appFormList)) {
            for (AppForm appForm : appFormList) {
                putAppForm(appForm.getTableId(), appForm.getComponentVersionId(), appForm.getMenuId(), appForm);
            }
        }
        AppFormElementService appFormElementService = XarchListener.getBean(AppFormElementService.class);
        List<Object[]> appFormElementList = appFormElementService.getAllDefineColumn();
        if (CollectionUtils.isNotEmpty(appFormElementList)) {
            for (Object[] objs : appFormElementList) {
                putAppFormElement(String.valueOf(objs[22]), String.valueOf(objs[23]), String.valueOf(objs[24]), objs);
            }
        }
    }
    
    /**
     * qiucs 2015-4-30 下午2:33:49
     * <p>描述: 清空应用定义缓存 </p>
     * @return void
     */
    public void clear() {
    	EhcacheUtil.getCache(APP_COLUMN).removeAll();
    	EhcacheUtil.getCache(APP_FORM).removeAll();
    	EhcacheUtil.getCache(APP_FORM_ELEMENT).removeAll();
    	EhcacheUtil.getCache(APP_GRID).removeAll();
    	EhcacheUtil.getCache(APP_SEARCH).removeAll();
    	EhcacheUtil.getCache(APP_SEARCH_PANEL).removeAll();
    	EhcacheUtil.getCache(APP_SORT).removeAll();
    }
    
    /**
     * qiucs 2015-4-30 下午2:33:26
     * <p>描述: 重新加载应用定义缓存 </p>
     * @return void
     */
    public void reload() {
    	clear();
    	load();
    }
    
    /**
     * qiucs 2015-4-8 下午5:53:53
     * <p>描述: 更新表单栏位显示类型 </p>
     * @return void
     */
    @SuppressWarnings("unchecked")
	public void updateColumn(ColumnDefine column) {
    	String tableId = column.getTableId(), 
    			columnName = column.getColumnName(), 
    			//showName = column.getShowName(),
    			dataType = column.getDataType(),
    			dataTypeExtend = column.getDataTypeExtend(),
    			inputType = column.getInputType(), 
    			codeTypeCode = column.getCodeTypeCode(),
    			inputOption = column.getInputOption(),
    			length = column.getLength().toString();
    	// 表单
    	Cache cache = EhcacheUtil.getCache(APP_FORM_ELEMENT);
    	List<String> cacheKeys = cache.getKeysNoDuplicateCheck();
    	int i = 0, len = 0, j = 0, alen = 0;
    	String key;
    	List<Object[]> apps = null;
    	for (i = 0, len = cacheKeys.size(); i < len; i++) {
    		key = cacheKeys.get(i);
    		if (key.startsWith(tableId)) {
    			apps = (List<Object[]>) EhcacheUtil.getCache(APP_FORM_ELEMENT, key);
    			for (j = 0, alen = apps.size(); j < alen; j++) {
    				if (columnName.equals(apps.get(j)[2])) {
    					//apps.get(j)[1] = showName; // 表单自身可以修改showName
    					apps.get(j)[3] = dataType;
    					apps.get(j)[4] = length;
    					apps.get(j)[5] = codeTypeCode;
    					apps.get(j)[11] = inputType;
    					apps.get(j)[18] = dataTypeExtend;
    					apps.get(j)[19] = inputOption;
    				}
    			}
    		}
    	}
    	// 列表
    	cache = EhcacheUtil.getCache(APP_COLUMN);
    	cacheKeys = cache.getKeysNoDuplicateCheck();
    	for (i = 0, len = cacheKeys.size(); i < len; i++) {
    		key = cacheKeys.get(i);
    		if (key.startsWith(tableId)) {
    			apps = (List<Object[]>) EhcacheUtil.getCache(APP_COLUMN, key);
    			for (j = 0, alen = apps.size(); j < alen; j++) {
    				if (columnName.equals(apps.get(j)[6])) {
    					//apps.get(j)[1] = showName; // 列表自身可以修改showName
    					apps.get(j)[7] = dataType;
    					apps.get(j)[8] = codeTypeCode;
    					apps.get(j)[12] = inputType;
    				}
    			}
    		}
    	}
    	// 检索
    	cache = EhcacheUtil.getCache(APP_SEARCH);
    	cacheKeys = cache.getKeysNoDuplicateCheck();
    	for (i = 0, len = cacheKeys.size(); i < len; i++) {
    		key = cacheKeys.get(i);
    		if (key.startsWith(tableId)) {
    			apps = (List<Object[]>) EhcacheUtil.getCache(APP_SEARCH, key);
    			for (j = 0, alen = apps.size(); j < alen; j++) {
    				if (columnName.equals(apps.get(j)[3])) {
    					//apps.get(j)[2] = showName; // 检索自身可以修改showName
    					apps.get(j)[5] = dataType;
    					apps.get(j)[6] = dataTypeExtend;
    					apps.get(j)[7] = codeTypeCode;
    					apps.get(j)[8] = inputType;
    				}
    			}
    		}
    	}
    }
    /**
     * qiujinwei 2015-08-25 
     *      * <p>描述: 删除字段的应用定义缓存 </p>
     * @return void
     */
    @SuppressWarnings("unchecked")
    public void deleteColumn(ColumnDefine column, String relationType) {
    	String tableId = column.getTableId(), 
    			columnName = column.getColumnName();
    	int	namePosition = 0;//列名相对位置
    	if (relationType.equals(APP_FORM_ELEMENT)) {//表单
    		namePosition = 2;
		} else if (relationType.equals(APP_COLUMN)) {//列表
			namePosition = 6;
		} else if (relationType.equals(APP_SEARCH)) {//检索
			namePosition = 3;
		} else if (relationType.equals(APP_SORT)) {//列表排序
			namePosition = 4;
		}
    	Cache cache = EhcacheUtil.getCache(relationType);
    	List<String> cacheKeys = cache.getKeysNoDuplicateCheck();
    	int i = 0, len = 0, j = 0, alen = 0;
    	String key;
    	List<Object[]> apps = null;
    	for (i = 0, len = cacheKeys.size(); i < len; i++) {
    		key = cacheKeys.get(i);
    		if (key.startsWith(tableId)) {
    			apps = (List<Object[]>) EhcacheUtil.getCache(relationType, key);
    	    	List<Object[]> removeApps = new ArrayList<Object[]>();
    			for (j = 0, alen = apps.size(); j < alen; j++) {
    				if (columnName.equals(apps.get(j)[namePosition])) {
    					removeApps.add(apps.get(j));
    				}
    			}
    			apps.removeAll(removeApps);
    		}
    	}
    }

}

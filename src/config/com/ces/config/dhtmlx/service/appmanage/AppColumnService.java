package com.ces.config.dhtmlx.service.appmanage;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppColumnDao;
import com.ces.config.dhtmlx.entity.appmanage.AppColumn;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppGrid;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.json.entity.appmanage.DhtmlxGrid;
import com.ces.config.datamodel.message.MessageModel;
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
public class AppColumnService extends ConfigDefineDaoService<AppColumn, AppColumnDao> {
	
    private static Log log = LogFactory.getLog(AppColumnService.class);

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appColumnDao")
    @Override
    protected void setDaoUnBinding(AppColumnDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * <p>标题: findByComponentVersionId</p>
     * <p>描述: 根据模块ID获取列表字段信息</p>
     * @param tableId 表定义ID
     * @param componentVersionId 模块ID
     * @return List<AppColumn> 返回类型
     */
    public List<AppColumn> findByFk(String tableId, String componentVersionId, String menuId, String userId) {
        return getDao().findByFk(tableId, componentVersionId, menuId, userId);
    }
    
    /**
     * <p>描述: 判断执行步骤</p>
     * @return boolean    返回类型   
     */
    private boolean judgeStep(String tableId, String componentVersionId, String menuId, String userId) {
        Long count = getDao().count(tableId, componentVersionId, menuId, userId);
        if (count == 0) {
            return false;
        }
        return true;
    }
    
    /**
     * <p>标题: getDefaultColumn</p>
     * <p>描述: 可选列表字段数据</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  defaulted
     * @return Object    返回类型   
     * @throws
     */
    private List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId, String userId) {
        // 用户按菜单个性化设置
    	if(judgeStep(tableId, componentVersionId, menuId, userId) || CodeUtil.getInstance().hasCodeType(componentVersionId)) {
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
                return getDao().getDefaultColumn(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
            }
    	    if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && 
    	            !CommonUtil.SUPER_ADMIN_ID.equals(userId) && 
                    judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID)) {
                // 超级管理员按菜单默认设置
                return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
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
            if(judgeStep(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId)) {
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
     * <p>描述: 已选列表字段数据</p>
     */
    private List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId, String userId) {
    	// 用户按菜单个性化设置
    	List<Object[]> list = AppUtil.getInstance().getAppColumn(tableId, componentVersionId, menuId, userId);
    	if (CollectionUtils.isNotEmpty(list) || CodeUtil.getInstance().hasCodeType(componentVersionId)) {
    		return list;
    	}
    	if (StringUtil.isEmpty(menuId)) {
    		menuId = AppDefine.DEFAULT_DEFINE_ID;
    	}
    	if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
    	    // 用户按菜单默认设置
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)){
                list = AppUtil.getInstance().getAppColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
                if (CollectionUtils.isNotEmpty(list)) return list;
            }   
            if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)){
                // 超级管理员按菜单个性化设置
                list = AppUtil.getInstance().getAppColumn(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
                if (CollectionUtils.isNotEmpty(list)) return list;
                
                if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
                    // 超级管理员按菜单默认设置
                    list = AppUtil.getInstance().getAppColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
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
    		// 超级管理员按构件个性化设置
    		list = AppUtil.getInstance().getAppColumn(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    		if (CollectionUtils.isNotEmpty(list)) return list;
    	}
    	/*// 用户按构件个性化设置
    	list = AppUtil.getInstance().getAppColumn(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId);
        if (CollectionUtils.isNotEmpty(list)) return list;
    	// 用户按构件默认设置
    	if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
	    	list = AppUtil.getInstance().getAppColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, userId);
	    	if (CollectionUtils.isNotEmpty(list)) return list;
    	}    	
    	if(!CommonUtil.SUPER_ADMIN_ID.equals(userId)) {
    		// 超级管理员按构件个性化设置
	    	list = AppUtil.getInstance().getAppColumn(tableId, componentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
	    	if (CollectionUtils.isNotEmpty(list)) return list;
	    	// 超级管理员按构件默认设置
	    	if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
	    		list = AppUtil.getInstance().getAppColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
	    	}
    	}
    	return list;*/
        return AppUtil.getInstance().getAppColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 可选列表字段数据(前台列表数据)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppColumn> findDefaultList(String tableId, String componentVersionId, String menuId, String userId) {
        List<AppColumn> list = new ArrayList<AppColumn>();
        List<Object[]> rlt = getDefaultColumn(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isNotEmpty(rlt)) {
            for (Object[] oArr : rlt) {
                // t_cd.id as column_id, t_cd.show_name, t_cd.width, t_cd.align, t_ac.type, t_cd.column_name
                AppColumn item = new AppColumn();
                item.setId(String.valueOf(UUIDGenerator.uuid()));
                item.setColumnId(String.valueOf(oArr[0]));
                item.setShowName(String.valueOf(oArr[1]));
                item.setWidth(Integer.parseInt(StringUtil.null2zero(oArr[2])));
                item.setAlign(StringUtil.null2empty(oArr[3]));
                item.setType(StringUtil.null2empty(oArr[4]));
                item.setColumnName(String.valueOf(oArr[5]));
                
                list.add(item);
            }
        }
        return list;
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 已选列表字段数据(后台列表数据)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppColumn> findDefineList(String tableId, String componentVersionId, String menuId, String userId) {
        List<AppColumn> list = new ArrayList<AppColumn>();
        List<Object[]> rlt = getDefineColumn(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isNotEmpty(rlt)) {
            for (Object[] oArr : rlt) {
                // select t_ac.id, t_ac.show_name, t_ac.width, t_ac.align, t_ac.type, t_ac.column_id," +  // 0~5
                // (case when (t_ac.column_type != 0 or t_cd.column_name is null) then t_ac.column_name else t_cd.column_name end) column_name," + // 6
                // t_cd.data_type, t_cd.code_type_code, t_ac.url, t_ac.column_alias, t_ac.column_type, t_ac.column_type
                if (oArr[1].equals("操作")) continue;
            	AppColumn item = new AppColumn();
                item.setId(String.valueOf(oArr[0]));
                item.setShowName(String.valueOf(oArr[1]));
                item.setWidth(Integer.parseInt(StringUtil.null2zero(oArr[2])));
                item.setAlign(StringUtil.null2empty(oArr[3]));
                item.setType(StringUtil.null2empty(oArr[4]));
                item.setColumnId(StringUtil.null2empty(oArr[5]));
                item.setColumnName(String.valueOf(oArr[6]));
                item.setDataType(StringUtil.null2empty(oArr[7]));
                item.setCodeTypeCode(StringUtil.null2empty(oArr[8]));
                item.setUrl(StringUtil.null2empty(oArr[9]));
                item.setColumnAlias(StringUtil.null2empty(oArr[10]));
                item.setColumnType(StringUtil.null2empty(oArr[11]));
                item.setInputType(StringUtil.null2empty(oArr[12]));
                
                list.add(item);
            }
        }
        return list;
    }
    
    /**
     * qiujinwei 2015-06-18 
     * <p>描述: 已选列表字段数据(前台列表数据)</p>
     * @return Object    返回类型   
     * @throws
     */
    public List<AppColumn> findAllDefineList(String tableId, String componentVersionId, String menuId, String userId) {
        List<AppColumn> list = new ArrayList<AppColumn>();
        List<Object[]> rlt = getDefineColumn(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isNotEmpty(rlt)) {
            for (Object[] oArr : rlt) {
                // select t_ac.id, t_ac.show_name, t_ac.width, t_ac.align, t_ac.type, t_ac.column_id," +  // 0~5
                // (case when (t_ac.column_type != 0 or t_cd.column_name is null) then t_ac.column_name else t_cd.column_name end) column_name," + // 6
                // t_cd.data_type, t_cd.code_type_code, t_ac.url, t_ac.column_alias, t_ac.column_type, t_ac.column_type
            	AppColumn item = new AppColumn();
                item.setId(String.valueOf(oArr[0]));
                item.setShowName(String.valueOf(oArr[1]));
                item.setWidth(Integer.parseInt(StringUtil.null2zero(oArr[2])));
                item.setAlign(StringUtil.null2empty(oArr[3]));
                item.setType(StringUtil.null2empty(oArr[4]));
                item.setColumnId(StringUtil.null2empty(oArr[5]));
                item.setColumnName(String.valueOf(oArr[6]));
                item.setDataType(StringUtil.null2empty(oArr[7]));
                item.setCodeTypeCode(StringUtil.null2empty(oArr[8]));
                item.setUrl(StringUtil.null2empty(oArr[9]));
                item.setColumnAlias(StringUtil.null2empty(oArr[10]));
                item.setColumnType(StringUtil.null2empty(oArr[11]));
                item.setInputType(StringUtil.null2empty(oArr[12]));
                item.setDataTypeExtend(StringUtil.null2empty(oArr[17]));
                
                list.add(item);
            }
        }
        return list;
    }
    
    /**
     * <p>描述: 保存列表的配置信息</p>
     */
    @Transactional
    public MessageModel save(AppGrid master, String rowsValue) throws FatalException {
        try {
            String tableId = master.getTableId();
            String componentVersionId= master.getComponentVersionId();
            String menuId  = master.getMenuId();
            String userId  = master.getUserId();
           // 1. delete all grid column by table id and menu id
            getDao().deleteByFk(tableId, componentVersionId, menuId, userId);
            // 2. save grid columns
            //System.out.println(rowsValue);
            rowsValue = URLDecoder.decode(rowsValue, "utf-8");
            //System.out.println(rowsValue);
            rowsValue = rowsValue.replace("&amp;", "&");
            //System.out.println(rowsValue);
            String[] rowsArr = rowsValue.split(";");
            List<AppColumn> list = Lists.newArrayList();
            int i = 0;
            boolean hasLinkReserveZone = false;
            for (; i <rowsArr.length; i++) {
                String oneRowValue = rowsArr[i];
                list.add(newInstance(master, oneRowValue, (i + 1)));
                if (oneRowValue.endsWith("3")) {
                    hasLinkReserveZone = true;
                }
            }
            if (!CodeUtil.getInstance().hasCodeType(componentVersionId) && !hasLinkReserveZone) {
            	if (master.getOpeColPosition() == 1) {
                    list.add(newInstance(master, "-1||" + master.getOpeColName() + "|" + master.getOpeColWidth() + "|center|ro||3", (i + 1)));
				} else if (master.getOpeColPosition() == 0) {
					for (int j = 0; j < list.size(); j++) {
						list.get(j).setShowOrder(j + 2);
					}
					list.add(newInstance(master, "-1||" + master.getOpeColName() + "|" + master.getOpeColWidth() + "|center|ro||3", 1));
				}
            }
            getDao().save(list);
        } catch (Exception e) {
            log.error("保存列表配置出错！", e);
            return MessageModel.falseInstance("ERROR");
        }
        
        return MessageModel.trueInstance("OK");
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 创建一个实例</p>
     * @return AppColumn    返回类型   
     */
    private AppColumn newInstance(AppGrid master, String oneRowValue, Integer showOrder) {
        AppColumn entity = new AppColumn();
        entity.setTableId(master.getTableId());
        entity.setComponentVersionId(master.getComponentVersionId());
        entity.setMenuId(master.getMenuId());
        entity.setUserId(master.getUserId());
        entity.setShowOrder(showOrder);
        
        String[] propArr = oneRowValue.split("\\|");
        entity.setColumnId(propArr[0]);
        entity.setColumnName(propArr[1]);
        entity.setShowName(propArr[2]);
        entity.setWidth(StringUtil.isEmpty(propArr[3]) ? null : Integer.parseInt(StringUtil.null2zero(propArr[3])));
        entity.setAlign(propArr[4]);
        entity.setType(propArr[5]);
        entity.setUrl((propArr.length > 6 ? StringUtil.null2empty(propArr[6]) : null));
        entity.setColumnType((propArr.length > 7 ? StringUtil.null2empty(propArr[7]) : "0"));
        return entity;
    }
    
    /**
     * <p>标题: clearColumn</p>
     * <p>描述: 清除列表配置信息</p>
     * @param  tableId
     * @param  componentVersionId
     * @return void    返回类型   
     * @throws FatalException
     */
    @Transactional
    public void clear(AppGrid entity) throws FatalException {
        getDao().deleteByFk(entity.getTableId(), entity.getComponentVersionId(), entity.getMenuId(), entity.getUserId());
    }

    /**
     * qiucs 2013-9-10 
     * <p>标题: getDhtmlxGrid</p>
     * <p>描述: </p>
     * @param  tableId
     * @param  componentVersionId
     * @param  @return    设定参数   
     * @return Object    返回类型   
     * @throws
     */
    public Object getDhtmlxGrid(String tableId, String componentVersionId, String menuId, String userId) {
        DhtmlxGrid grid = new DhtmlxGrid();
        try {
        	List<Object[]> list = getDefineColumn(tableId, componentVersionId, menuId, userId);	
        	if (CollectionUtils.isNotEmpty(list)) {
        		String type = null;
                for (int i = 0; i < list.size(); i++) {
                    Object[] obj = list.get(i);
                    type = cast2type(StringUtil.null2empty(obj[4]));
                    if (AppColumn.Type.HIDDEN.equals(type)) continue;
                    grid.getHeaders().add("<center>" + StringUtil.null2empty(obj[1]) + "</center>");
                    grid.getWidths().add(StringUtil.isEmpty(obj[2]) ? "*" : StringUtil.null2zero(obj[2]));
                    grid.getAligns().add(StringUtil.null2empty(obj[3]));
                    grid.getTypes().add(type);
                    grid.getColumnIds().add(StringUtil.null2empty(obj[5]));
                    //grid.getTypes().add("ro");
                    grid.getColumns().add(columnHandle(obj));
                    grid.getDatatypes().add(StringUtil.null2empty(obj[7]));
                    grid.getCodetypes().add(StringUtil.null2empty(obj[8]));
                    grid.getUrls().add(URLEncoder.encode(StringUtil.null2empty(obj[9]).replace(",", "|"), "utf-8"));
                }
        	}
            /*List<AppSort> apps = getService(AppSortService.class).getDefineColumn(tableId, componentVersionId, userId);
            for (AppSort app : apps) {
            	grid.getOrders().add(app.getColumnName());
            	grid.getOrders().add(app.getSortType());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return grid;
    }
    
    private String cast2type(String type) {
        if (AppColumn.Type.CARD.equals(type) || AppColumn.Type.TEXT_CARD.equals(type)
        		 || AppColumn.Type.VALUE.equals(type)
        		 || AppColumn.Type.EDITABLE.equals(type)) {
            return AppColumn.Type.TEXT;
        }
        return type;
    }
    
    /**
     * qiucs 2014-4-3 
     * <p>描述: SQL字段处理</p>
     * @return String    返回类型   
     * @throws
     */
    private String columnHandle(Object[] obj) {
        if (AppColumn.SPECIAL_VALUE.equals(StringUtil.null2empty(obj[5]))
                || !"0".equals(StringUtil.null2empty(obj[11]))) {
            if ("2".equals(StringUtil.null2empty(obj[11]))) { // 固定值
                return "'" + StringUtil.null2empty(obj[6]) + "' AS " + StringUtil.null2empty(obj[10]);
            }
            // 自定义SQL语句
            return "(" + StringUtil.null2empty(obj[6]) + ") AS " + StringUtil.null2empty(obj[10]);
        } 
        return StringUtil.null2empty(obj[6]);
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
        AppUtil.getInstance().removeApp(AppUtil.APP_COLUMN, tableId, AppUtil.TABLE_ID);
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
        AppUtil.getInstance().removeApp(AppUtil.APP_COLUMN, componentVersionId, AppUtil.COMPONENT_VERSION_ID);
    }
    
    /**
     * qiucs 2014-12-12 
     * <p>描述: 根据 menu id 删除配置</p>
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDao().deleteByMenuId(menuId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_COLUMN, menuId, AppUtil.MENU_ID);
    }
    
    /**
     * qiucs 2014-3-5 
     * <p>描述: 获取当前用户的列表配置信息</p>
     */
    protected List<AppColumn> findUserList(String tableId, String componentVersionId, String menuId, String userId) {
        // 用户按菜单个性化设置
        List<AppColumn> list = getDao().findByFk(tableId, componentVersionId, menuId, userId);
        if (CollectionUtils.isNotEmpty(list)) return list;
        
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)&&
    	            judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId)) {
                // 用户按菜单默认设置
                return getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, userId);
            }       
            if(!CommonUtil.SUPER_ADMIN_ID.equals(userId) && 
    	            judgeStep(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID)){
                // 超级管理员个性化设置
                return getDao().findByFk(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID);
            }
            if(!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && 
    	            !CommonUtil.SUPER_ADMIN_ID.equals(userId) && 
                    judgeStep(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID)) {
                // 超级管理员按菜单默认设置
                return getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, CommonUtil.SUPER_ADMIN_ID);
            }
        }
        /***************************************(基础构件)***************************************/
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId)) {
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion entity = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (entity.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
    		}
            // 超级管理员按构件个性化设置
            if(judgeStep(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, userId)) {
                return  getDao().findByFk(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
            }
		}
        
        return getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID, CommonUtil.SUPER_ADMIN_ID);
    }
    
    /**
     * qiucs 2014-3-5 
     * <p>描述: 用户个性化设置宽度</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  columnId
     * @param  showOrderStr 显示顺序(显示顺序的值=索引位置 + 1)
     * @param  width        宽度
     */
    @Transactional
    public Object setUserWidth(String tableId, String componentVersionId, String menuId, String columnId, String showOrderStr, String width) {
        try {
            String userId = CommonUtil.getUser().getId();
            Integer showOrder = Integer.parseInt(showOrderStr);
            // 如果没有菜单 则是基本构件
        	String cvId = AppDefine.DEFAULT_DEFINE_ID.equals(menuId) ? 
        			getService(ConstructService.class).getBaseComponentVersionId(componentVersionId) : componentVersionId;
        			
            AppColumn entity = getDao().findByFk(tableId, cvId, menuId, userId, columnId, showOrder);
            if (null != entity) {
                entity.setWidth(Integer.parseInt(width));
                save(entity);
            } else {
                List<AppColumn> list = findUserList(tableId, cvId, menuId, userId);
                for (int i = 0; i < list.size(); i++) {
                    entity = new AppColumn();
                    BeanUtils.copy(list.get(i), entity);
                    entity.setId("");
                    entity.setComponentVersionId(cvId);
                    entity.setUserId(userId);
                    if (columnId.equals(entity.getColumnId()) && showOrder == entity.getShowOrder()) {
                        entity.setWidth(Integer.parseInt(width));
                    }
                    list.remove(i);
                    list.add(i, entity);
                }
                save(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MessageModel.falseInstance(e.getMessage());
        }
        return MessageModel.trueInstance("OK");//new MessageModel(Boolean.TRUE, "OK");
    }
    
    /**
     * qiucs 2014-3-5 
     * <p>描述: 用户个性化设置：表头调整</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  sourcePosStr 原位置
     * @param  targetPosStr 目标位置
     */
    @Transactional
    public Object setUserColumnPosition(String tableId, String componentVersionId, String menuId, String sourcePosStr, String targetPosStr) {
        try {
            //System.out.println("spos = " + sourcePosStr);
            //System.out.println("tpos = " + targetPosStr);
            String userId = CommonUtil.getUser().getId();
            // 如果没有菜单 则是基本构件
        	String cvId = AppDefine.DEFAULT_DEFINE_ID.equals(menuId) ? 
        			getService(ConstructService.class).getBaseComponentVersionId(componentVersionId) : componentVersionId;

            List<AppColumn> list = findUserList(tableId, cvId, menuId, userId);
            if (list.isEmpty()) return MessageModel.falseInstance("");
            AppColumn entity = list.get(0);
            /* 是否为公共配置
             * 如果是公共配置，则需要拷贝一份作为当前用户的个性化配置，最后再调整表头位置  
             **/
            boolean isCommon = !(componentVersionId.equals(entity.getComponentVersionId()) && userId.equals(entity.getUserId())); 
            Integer sPos = Integer.parseInt(sourcePosStr);
            Integer tPos = Integer.parseInt(targetPosStr);
            if (isCommon) {
                for (int i = 0; i < list.size(); i++) {
                    entity = new AppColumn();
                    BeanUtils.copy(list.get(i), entity);
                    entity.setId("");
                    entity.setComponentVersionId(cvId);
                    entity.setUserId(userId);
                    list.remove(i);
                    list.add(i, entity);
                }
            }
            // 调整表头位置
            entity = list.get(sPos);
            list.add((tPos > sPos ? tPos + 1 : tPos), entity);
            list.remove((sPos > tPos ? sPos + 1 : sPos));
            for (int i = 0; i < list.size(); i++) {
                entity = list.get(i);
                entity.setShowOrder((i + 1));
            }
            // 保存调整后的配置
            save(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MessageModel.falseInstance(e.getMessage());
        }
        return MessageModel.trueInstance("OK");
    }

	/**
	 * qiucs 2015-4-27 下午6:03:37
	 * <p>描述: 工作流基本检索（从一个版本复制到另一个版本） </p>
	 * @return void
	 */
    @Transactional
	public void copyWorkflow(String tableId, String fromVersionId, String toVersionId) {
    	String filters = "EQ_tableId=" + tableId + ";EQ_menuId=" + fromVersionId + ";EQ_userId=" + CommonUtil.SUPER_ADMIN_ID;
		List<AppColumn> list = find(filters);
    	int i = 0, len = list.size();
    	AppColumn entity = null;
    	List<AppColumn> destList = new ArrayList<AppColumn>();
    	for (; i < len; i++) {
    		entity = new AppColumn();
    		BeanUtils.copy(list.get(i), entity);
    		entity.setId(null);
    		entity.setMenuId(toVersionId);
    		destList.add(entity);
    	}
    	list = null;
    	if (!destList.isEmpty()) save(destList);
    	destList = null;
	}

    /**
	 * qiujinwei 2015-06-26
	 * <p>描述: 根据AppGrid获取操作列 </p>
	 * @return void
	 */
    public AppColumn findOpeColByAppGrid(AppGrid appGrid) {
    	String filters = "EQ_tableId=" + appGrid.getTableId() + ";EQ_menuId=" + appGrid.getMenuId() + ";EQ_userId=" + appGrid.getUserId() + 
    			";EQ_componentVersionId=" + appGrid.getComponentVersionId() + ";EQ_columnType=3";
    	List<AppColumn> list = find(filters);
    	if (list != null) {
			return list.get(0);
		}
    	else {
			return null;
		}
    }
    
    /**
	 * qiujinwei 2015-06-26
	 * <p>描述: 更新操作列位置 </p>
	 * @return void
	 */
    @Transactional
    public void updateOpeColPosition(AppGrid appGrid, int position) {
    	List<AppColumn> list = findByFk(appGrid.getTableId(), appGrid.getComponentVersionId(), appGrid.getMenuId(), appGrid.getUserId());
    	if (appGrid.getOpeColPosition() == 0) {
    		for (int i = 0; i < position - 1; i++) {
    			AppColumn entity = list.get(i);
    			entity.setShowOrder(i + 2);
			}
    		list.get(position - 1).setShowOrder(1);
		} else {
			for (int i = position; i < list.size(); i++) {
    			AppColumn entity = list.get(i);
    			entity.setShowOrder(i);
			}
			list.get(position - 1).setShowOrder(list.size());
		}
    	save(list);
    }
    
    /**
     * qiucs 2015-6-26 上午10:43:47
     * <p>描述: 应用到 </p>
     * @return void
     */
    @Transactional
    public void applyTo(String tableId, String componentVersionId, String menuId, String toComponentVersionId, String toMenuId) {
    	
    	List<AppColumn> list = findByFk(tableId, componentVersionId, menuId, CommonUtil.getCurrentUserId());
    	
    	int i = 0, len = list.size();
    	AppColumn entity = null;
    	List<AppColumn> destList = new ArrayList<AppColumn>();
    	for (; i < len; i++) {
    		entity = new AppColumn();
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

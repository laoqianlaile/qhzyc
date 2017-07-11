package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppFormElementDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppForm;
import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;
import com.google.common.collect.Lists;

@Component
public class AppFormElementService extends ConfigDefineDaoService<AppFormElement, AppFormElementDao> {
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appFormElementDao")
    @Override
    protected void setDaoUnBinding(AppFormElementDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * <p>标题: defaultColumns</p>
     * <p>描述: 可选界面字段</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  defaulted
     * @return Object    返回类型   
     * @throws
     */
    private List<Object[]> getDefaultColumn(String tableId, String componentVersionId, String menuId) {
        boolean special = false;
        // 按菜单
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            special = getService(AppDefineService.class).isUseSpecial(tableId, componentVersionId, menuId, AppDefine.TYPE_FORM);
            if (special) {
                // 按菜单自身设置
                return getDao().getDefaultColumn(tableId, componentVersionId, menuId);
            } 
            special = getService(AppDefineService.class).isUseSpecial(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, AppDefine.TYPE_FORM);
            if (special) {
                // 按菜单默认设置
                return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId);
            }
        }
        // 按构件
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && !WorkflowUtil.isBox(componentVersionId)) {
        	// 基础构件
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion entity = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (entity.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
			}
            special = getService(AppDefineService.class).isUseSpecial(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.TYPE_FORM);
            if (special) {
                // 按构件自身设置
                return getDao().getDefaultColumn(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID);
            }
        }
        // 按构件默认设置
        return getDao().getDefaultColumn(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID);
    }
    
    /**
     * <p>标题: getAllDefineColumn</p>
     * <p>描述: 获取所有已配置的界面字段，初始化缓存时使用</p>
     * @return List<Object[]>    返回类型   
     * @throws
     */
    public List<Object[]> getAllDefineColumn() {
        return getDao().getAllDefineColumn();
    }
    
    /**
     * <p>描述: 已选界面字段，在更新缓存时使用</p>
     */
    public List<Object[]> getDefineColumns(String tableId, String componentVersionId, String menuId) {
        return getDao().getDefineColumn(tableId, componentVersionId, menuId);
    }
    
    /**
     * <p>标题: defineColumns</p>
     * <p>描述: 已选界面字段</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  defaulted
     *                  --如果没有,是否查询默认配置
     * @return Object    返回类型   
     * @throws
     */
    private List<Object[]> getDefineColumn(String tableId, String componentVersionId, String menuId) {
        boolean special = false;
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (StringUtil.isEmpty(menuId)) {
        	menuId = AppDefine.DEFAULT_DEFINE_ID;
		}
        // 按菜单
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(menuId)) {
            special = getService(AppDefineService.class).isUseSpecial(tableId, componentVersionId, menuId, AppDefine.TYPE_FORM);
            if (special) {
                // 按菜单自身设置
                return AppUtil.getInstance().getAppFormElement(tableId, componentVersionId, menuId);
            } 
            special = getService(AppDefineService.class).isUseSpecial(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId, AppDefine.TYPE_FORM);
            if (special) {
                // 按菜单默认设置
                return AppUtil.getInstance().getAppFormElement(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId);
            }
        }        
        // 按构件
        if (StringUtil.isEmpty(componentVersionId)) {
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
		}
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && !WorkflowUtil.isBox(componentVersionId)) {
            // 基础构件
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
    		}
            special = getService(AppDefineService.class).isUseSpecial(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.TYPE_FORM);
            if (special) {
                // 按构件自身设置
                return AppUtil.getInstance().getAppFormElement(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID);
            }
        }
        // 按构件默认设置
        return AppUtil.getInstance().getAppFormElement(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID);
    }
    
    /**
     * qiucs 2013-11-11 
     * <p>描述: </p>
     * @param  tableId
     * @param  componentVersionId
     * @param  defaulted
     * @return List<AppFormElement>    返回类型   
     */
    public List<AppFormElement> elements(String tableId, String componentVersionId, String menuId) {
        List<AppFormElement> elements = new ArrayList<AppFormElement>();
        elements.addAll(assembleElements(getDefineColumn(tableId, componentVersionId, menuId), true));
        elements.addAll(assembleElements(getDefaultColumn(tableId, componentVersionId, menuId), true));
        return elements;
    }
    
    /**
     * qiucs 2013-12-4 
     * <p>描述: </p>
     * @param  list
     * @param  columnNameShowed --显示名称中是否显示字段名称：true-是，false-否
     * @return List<AppFormElement>    返回类型   
     * @throws
     */
    private List<AppFormElement> assembleElements(List<Object[]> list, boolean columnNameShowed) {
        List<AppFormElement> elements = new ArrayList<AppFormElement>();
        if (null != list && !list.isEmpty()) {
            for (Object[] obj : list) {
                AppFormElement element = new AppFormElement();
                String columnId = StringUtil.null2empty(obj[0]);
                //t_cd.id, t_cd.show_name, t_cd.column_name, t_cd.data_type, t_cd.length, t_cd.code_type_code
                //t_fe.id, t_fe.colspan, t_fe.required, t_fe.readonly, t_fe.hidden, t_fe.textarea, t_fe.default_value, t_fe.kept, t_fe.validation, t_fe.tooltip
                element.setId(UUIDGenerator.uuid());
                //if (columnNameShowed) {
                //    element.setShowName(StringUtil.null2empty(obj[1]) + " (" + StringUtil.null2empty(obj[2]) + ")");
                //} else {
                    element.setShowName(StringUtil.null2empty(obj[1]));
                //}
                if (columnNameShowed && 
                		(AppFormElement.SUBFIELD_ID.equals(columnId) || AppFormElement.PLACEHOLDER_ID.equals(columnId))) {
                	if (columnNameShowed) element.setShowName("<font color=\"blue\">" + element.getShowName() + "</font>");
                }   
                element.setColumnName(StringUtil.null2empty(obj[2]));
                element.setDataType(StringUtil.null2empty(obj[3]));
                element.setLength(StringUtil.null2empty(obj[4]));
                element.setCodeTypeCode(StringUtil.null2empty(obj[5]));
                element.setFormable((StringUtil.isEmpty(obj[6]) ? "0" : "1"));
                element.setColspan(Short.parseShort(StringUtil.null2zero(obj[7])));
                element.setRequired(StringUtil.null2zero(obj[8]));
                element.setReadonly(StringUtil.null2zero(obj[9]));
                element.setHidden(StringUtil.null2zero(obj[10]));
                element.setInputType(StringUtil.null2empty(obj[11]));
                element.setDefaultValue(StringUtil.null2empty(obj[12]));
                element.setKept(StringUtil.null2zero(obj[13]));
                element.setValidation(StringUtil.null2empty(obj[14]));
                element.setTooltip(StringUtil.null2empty(obj[15]));
                element.setIncrease(StringUtil.null2zero(obj[16]));
                element.setInherit(StringUtil.null2zero(obj[17]));
                element.setDataTypeExtend(StringUtil.null2empty(obj[18]));
                element.setInputOption(StringUtil.null2empty(obj[19]));
                element.setPattern(StringUtil.null2empty(obj[20]));
                element.setSpacePercent(Short.parseShort(StringUtil.isEmpty(obj[21]) ? "100" : StringUtil.null2empty(obj[21])));
                element.setColumnId(StringUtil.null2empty(obj[0]));
                //if (AppFormElement.SUBFIELD_ID.equals(columnId)) {
                //	if (columnNameShowed) element.setShowName("<font color=\"blue\">分栏符</font>");
                //} else if (AppFormElement.PLACEHOLDER_ID.equals(columnId)) {
                //    element.setShowName("<font color=\"blue\">占位符</font>");
                //}
                elements.add(element);
            }
        }
        return elements;
    }
    
    /**
     * <p>标题: save</p>
     * <p>描述: 保存表单元素配置</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  rowsValue
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void save(AppForm master, String rowsValue) {
        // 1. clear old configuration
        getDao().deleteByFk(master.getTableId(), master.getComponentVersionId(), master.getMenuId());
        // 2. save current configuration
        List<AppFormElement> list = Lists.newArrayList();
        String[] rowValueArr = rowsValue.split(";");
        int showOrder = 0;
        for (String oneRowValue : rowValueArr) {
            list.add(newInstance(master, oneRowValue, ++showOrder));
        }
        getDao().save(list);
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 删除配置</p>
     */
    @Transactional
    public void clear(AppForm master) {
        // 1. clear old configuration
        getDao().deleteByFk(master.getTableId(), master.getComponentVersionId(), master.getMenuId());
    }
    /**
     * <p>描述: 设置属性值</p>
     * @param  tableId
     *         表ID
     * @param  componentVersionId
     *         模块ID
     * @param  oneRowValue
     *         "字段,占用列,必须输入,只读,隐藏,文本域,默认值,连续录入,校验,提示信息"
     * @return void    返回类型   
     * @throws
     */
    private AppFormElement newInstance(AppForm master, String props, Integer showOrder) {
        AppFormElement entity = new AppFormElement();
        String[] columnsValue = props.split(",");
        int idx = 0, len = columnsValue.length;
        
        entity.setTableId(master.getTableId());
        entity.setComponentVersionId(master.getComponentVersionId());
        entity.setMenuId(master.getMenuId());
        //["显示名称","字段ID","占用列","必须输入","只读","隐藏","文本域","默认值","连续录入","校验","提示信息"]
        entity.setShowName(columnsValue[idx++]);
        entity.setColumnId(columnsValue[idx++]);
        entity.setColspan(Short.parseShort(columnsValue[idx++]));
        entity.setRequired(columnsValue[idx++]);
        entity.setReadonly(columnsValue[idx++]);
        entity.setHidden(columnsValue[idx++]);
        
        /*entity.setTextarea(columnsValue[5]);
        entity.setDefaultValue(columnsValue[6]);
        entity.setValidation(columnsValue[7]);
        entity.setTooltip(columnsValue[8]);
        entity.setKept(len > 9 ? columnsValue[9] : "0");
        entity.setIncrease(len > 10 ? columnsValue[10] : "0");
        entity.setInherit(len > 11 ? columnsValue[11] : "0");*/

        entity.setDefaultValue(columnsValue[idx++]);
        entity.setValidation(columnsValue[idx++]);
        entity.setTooltip(columnsValue[idx++]);
        entity.setKept(StringUtil.null2zero(columnsValue[idx++]));
        entity.setIncrease(StringUtil.null2zero(columnsValue[idx++]));
        entity.setInherit(len > idx ? StringUtil.null2zero(columnsValue[idx++]) : "0");
        entity.setPattern(len > idx ? StringUtil.null2empty(columnsValue[idx++]).replace("，", ",") : null);
        if (len > idx) {
        	String percent = StringUtil.null2empty(columnsValue[idx++]);
        	if (!"".equals(percent)) {
        		entity.setSpacePercent(Short.parseShort(percent));
        	}
        }
        entity.setShowOrder(showOrder);
        
        return entity;
    }
    
    /**
     * qiucs 2013-9-5 
     * <p>标题: copyDefault</p>
     * <p>描述: </p>
     * @param  tableId
     * @param  componentVersionId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void copyDefault(String tableId, String componentVersionId, String menuId) {
        try {
            // 1. delete old configuration
            getDao().deleteByFk(tableId, componentVersionId, menuId);
            // 2. find default module(-1) AppFormElement and save them to current module
            List<AppFormElement> list = getDao().findByFk(tableId, "-1", menuId);
            if (null == list || list.isEmpty()) {
                return;
            }
            for(int i = 0; i < list.size(); i++) {
                AppFormElement element = (AppFormElement)list.get(i).clone();
                element.setId(null);
                element.setComponentVersionId(componentVersionId);
                list.remove(i);
                list.add(i, element);
            }
            super.save(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * qiucs 2015-4-27 下午5:32:26
     * <p>描述: 工作流表单复制（从一个版本复制到另一个版本） </p>
     * @return void
     */
    @Transactional
    public void copyWorkflow(String tableId, String formVersionId, String toVersionId) {
        // 1. delete old configuration
        getDao().deleteByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, toVersionId);
        // 2. find default module(-1) AppFormElement and save them to current module
        List<AppFormElement> list = getDao().findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, formVersionId);
        List<AppFormElement> destList = new ArrayList<AppFormElement>();
        AppFormElement entity = null;
        for(int i = 0, len = list.size(); i < len; i++) {
        	entity = new AppFormElement();
            BeanUtils.copy(list.get(i), entity);
            entity.setId(null);
            entity.setMenuId(toVersionId);
            destList.add(i, entity);
        }
        list = null;
        if (!destList.isEmpty()) {
        	super.save(destList);
        }
        destList = null;
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 获取已选表单字段</p>
     * @return List<AppFormElement>    返回类型   
     */
    public List<AppFormElement> findDefineList(String tableId, String componentVersionId, String menuId) {
        List<Object[]> list = getDefineColumn(tableId, componentVersionId, menuId);
        return assembleElements(list, false);
    }
    
    /**
     * <p>标题: findByFk</p>
     * @param  tableId
     * @param  componentVersionId
     * @return List<AppFormElement>    返回类型   
     */
    public List<AppFormElement> findByFk(String tableId, String componentVersionId, String menuId) {
        return getDao().findByFk(tableId, componentVersionId, menuId);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    public void deleteByTableId(String tableId) {
        getDao().deleteByTableId(tableId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_FORM_ELEMENT, tableId, AppUtil.TABLE_ID);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  componentVersionId    设定参数   
     */
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_FORM_ELEMENT, componentVersionId, AppUtil.COMPONENT_VERSION_ID);
    }
    
    /**
     * qiucs 2014-12-12
     * <p>描述: 根据 menu id 删除配置</p>
     */
    public void deleteByMenuId(String menuId) {
        getDao().deleteByMenuId(menuId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_FORM_ELEMENT, menuId, AppUtil.MENU_ID);
    }
    
    /**
     * qiucs 2015-6-26 上午10:43:47
     * <p>描述: 应用到 </p>
     * @return void
     */
    @Transactional
    public void applyTo(String tableId, String componentVersionId, String menuId, String toComponentVersionId, String toMenuId) {
    	
    	List<AppFormElement> list = findByFk(tableId, componentVersionId, menuId);
    	
    	int i = 0, len = list.size();
    	AppFormElement entity = null;
    	List<AppFormElement> destList = new ArrayList<AppFormElement>();
    	for (; i < len; i++) {
    		entity = new AppFormElement();
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

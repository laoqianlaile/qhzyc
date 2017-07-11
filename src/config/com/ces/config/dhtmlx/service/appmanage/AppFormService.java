package com.ces.config.dhtmlx.service.appmanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.AppFormDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppForm;
import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.json.entity.appmanage.DhtmlxForm;
import com.ces.config.dhtmlx.json.entity.appmanage.Domain;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.AppUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FormUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;

@Component
public class AppFormService extends ConfigDefineDaoService<AppForm, AppFormDao> {
    
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("appFormDao")
    @Override
    protected void setDaoUnBinding(AppFormDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 根据表ID，模块ID查找AppForm</p>
     */
    public AppForm findByFk(String tableId, String componentVersionId, String menuId) {
        return getDao().findByFk(tableId, componentVersionId, menuId);
    }
    
    /**
     * <p>标题: clear</p>
     * <p>描述: 删除整个表单配置</p>
     * @param  tableId
     * @param  componentVersionId
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void clear(AppForm entity) {
        // 1. delete form element configuration
        getService(AppFormElementService.class).clear(entity);        
        // 2. delete form master configuration
        getDao().deleteByFk(entity.getTableId(), entity.getComponentVersionId(), entity.getMenuId());
        // 3. update AppDefine
        getService(AppDefineService.class).updateAppDefine(entity.getTableId(), entity.getComponentVersionId(), entity.getMenuId(), CommonUtil.SUPER_ADMIN_ID, 
                AppDefine.TYPE_FORM, AppDefine.DEFINE_NO);
        
        // 更新缓存
        AppUtil.getInstance().removeAppForm(entity.getTableId(), entity.getComponentVersionId(), entity.getMenuId());
        AppUtil.getInstance().removeAppFormElement(entity.getTableId(), entity.getComponentVersionId(), entity.getMenuId());
    }
    
    /**
     * <p>标题: save</p>
     * <p>描述: 保存整个表单配置</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  formValue
     * @param  elementsValue    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public AppForm save(AppForm entity, String elementsValue) {
        String tableId  = entity.getTableId();
        String componentVersionId = entity.getComponentVersionId();
        String menuId = entity.getMenuId();
        AppForm oldEntity = getDao().findByFk(tableId, componentVersionId, menuId);
        if (null != oldEntity) {
            entity.setId(oldEntity.getId());
        }
        // 1. save form master configuration
        save(entity);
        // 2. save form element configuration
        getService(AppFormElementService.class).save(entity, elementsValue);
        // 3. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID, 
                AppDefine.TYPE_FORM, AppDefine.DEFINE_YES);
        return entity;
    }
    
    /**
     * qiucs 2014-12-11 
     * <p>描述: 获取表单配置,如果自身没配置,则返回默认配置</p>
     * @return AppForm    返回类型   
     */
    public AppForm findDefineEntity(String tableId, String componentVersionId, String menuId) {
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
                return AppUtil.getInstance().getAppForm(tableId, componentVersionId, menuId);
            } 
            special = getService(AppDefineService.class).isUseSpecial(tableId,  AppDefine.DEFAULT_DEFINE_ID, menuId, AppDefine.TYPE_FORM);
            if (special) {
                // 按菜单默认设置
                return AppUtil.getInstance().getAppForm(tableId,  AppDefine.DEFAULT_DEFINE_ID, menuId);
            }
        }
        // 按构件
        if (!AppDefine.DEFAULT_DEFINE_ID.equals(componentVersionId) && !WorkflowUtil.isBox(componentVersionId)) {
        	// 基础构件
        	String baseComponentVersionId = componentVersionId;
        	ComponentVersion version = getService(ComponentVersionService.class).getByID(componentVersionId);
        	if (null != version && version.getComponent().getType().equals("9")) {
        		baseComponentVersionId = getService(ConstructService.class).getBaseComponentVersionId(componentVersionId);
    		}
            special = getService(AppDefineService.class).isUseSpecial(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.TYPE_FORM);
            if (special) {
                // 按构件自身
                return AppUtil.getInstance().getAppForm(tableId, baseComponentVersionId, AppDefine.DEFAULT_DEFINE_ID);
            }
        }
        // 按构件默认
        return AppUtil.getInstance().getAppForm(tableId, AppDefine.DEFAULT_DEFINE_ID, AppDefine.DEFAULT_DEFINE_ID);
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
        // 1. delete old configuration
        getDao().deleteByFk(tableId, componentVersionId, menuId);
        // 2. find default module(-1) AppForm and save it to current module
        AppForm entity = findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, menuId);
        if (null == entity) {
            return;
        }
        entity = (AppForm)entity.clone();
        entity.setId(null);
        entity.setComponentVersionId(componentVersionId);
        entity = super.save(entity);
        // 3. find default module(-1) AppFormElement and save them to current module
        getService(AppFormElementService.class).copyDefault(tableId, componentVersionId, menuId);
        // 4. update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID, 
                AppDefine.TYPE_FORM, AppDefine.DEFINE_YES);
    }
    
    /**
     * qiucs 2015-4-27 下午5:35:04
     * <p>描述: 工作流表单复制（从一个版本复制到另一个版本） </p>
     * @return void
     */
    @Transactional
    public void copyWorkflow(String tableId, String fromVersionId, String toVersionId) {
        AppForm entity = findByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, fromVersionId);
        if (null == entity) {
            return;
        }
        getDao().deleteByFk(tableId, AppDefine.DEFAULT_DEFINE_ID, toVersionId);
        AppForm destEntity = new AppForm();
        BeanUtils.copy(entity, destEntity);
        destEntity.setId(null);
        destEntity.setMenuId(toVersionId);
        super.save(destEntity);
        // copy AppFormElement and save them to current module
        getService(AppFormElementService.class).copyWorkflow(tableId, fromVersionId, toVersionId);
        // update AppDefine
        getService(AppDefineService.class).updateAppDefine(tableId, AppDefine.DEFAULT_DEFINE_ID, toVersionId, CommonUtil.SUPER_ADMIN_ID, 
                AppDefine.TYPE_FORM, AppDefine.DEFINE_YES);
    }
    
    public Object preViewForm(String tableId, String componentVersionId, String menuId) {
        
        return getDhtmlxForm(tableId, componentVersionId, menuId);
    }
    
    /**
     * qiucs 2013-9-11 
     * <p>标题: getDhtmlxForm</p>
     * <p>描述: 获得DHTMLXFORM整个表单JSON格式</p>
     * @param  tableId
     * @param  componentVersionId
     * @param  @return    设定参数   
     * @return Object    返回类型   
     * @throws
     */
    public Object getDhtmlxForm(String tableId, String componentVersionId, String menuId) {
        StringBuffer formJson = new StringBuffer();
        // start
        formJson.append("[");
        // 1. setting
        formJson.append("{type: \"settings\", position: \"label-left\", labelWidth: " +
                FormUtil.WIDTH_LABEL + ", inputWidth: " + FormUtil.WIDTH_INPUT + ", labelAlign: \"right\", offsetTop:\"2\"}");
        // 2. form colspan
        AppForm form = findByFk(tableId, componentVersionId, menuId);
        if (null == form) { return ""; }
        StringBuffer formBody = new StringBuffer();
        Integer fColspan = form.getColspan();
        // 3. form elements
        List<AppFormElement> elements = getService(AppFormElementService.class).findDefineList(tableId, componentVersionId, menuId);
        // 3-1. has not elements, return null
        if (elements.isEmpty()) {return "";}
        // 3-2. add hidden elements
        boolean isNotFirst = false;
        StringBuffer block = FormUtil.newFormBlock(fColspan, "hidden");
        block.append("{type: \"hidden\", name: \"ID\"}"); //, value: \"402884e94111fa87014111fa87c70000\"
        StringBuffer defaultValues = new StringBuffer();
        for (int i = 0; i < elements.size(); i++) {
            AppFormElement element = elements.get(i);
            if (StringUtil.isNotEmpty(element.getDefaultValue())) {
                defaultValues.append(',').append(element.getColumnName()).append(',').append(element.getDefaultValue());
            }
            if (FormUtil.FT_HIDDEN.equals(convert2type(element))) {
                block.append(", ").append(convert2item(form, element));
                elements.remove(i);
                i--;
                isNotFirst = true;
            } else if (AppFormElement.SUBFIELD_ID.equals(element.getColumnId())) {
                elements.remove(i);
                i--;
            }
        }
        block.append("]}");
        formBody.append(block);
        // 3-3. add input elements
        isNotFirst = false;
        block = FormUtil.newFormBlock(fColspan, "start");
        String newColumn = "{type: \"newcolumn\"}";
        StringBuffer keptColumns = new StringBuffer();
        StringBuffer increaseColumns = new StringBuffer();
        StringBuffer inheritColumns = new StringBuffer();
        float ttlColspan = 0;
        for (int i = 0; i < elements.size(); i++) {
            AppFormElement element = elements.get(i);
            float eColspan = getElementColspan(form, element);
            ttlColspan += eColspan;
            // 连续录入
            System.out.println(element.getColumnName());
            if ("1".equals(element.getKept())) {
                keptColumns.append(",").append(element.getColumnName());
            }
            if ("1".equals(element.getIncrease())) {
                increaseColumns.append(",").append(element.getColumnName());
            }
            if ("1".equals(element.getInherit())) {
                inheritColumns.append(",").append(element.getColumnName());
            }
            /* 如果表单元素占用列大于表单设置占用列，则把前面累计放入一行中 */
            if (ttlColspan > fColspan) {
                block.append("]}");
                formBody.append(", ").append(block);
                // reset
                block = FormUtil.newFormBlock(fColspan, String.valueOf(i));
                isNotFirst = false;
                ttlColspan = eColspan;
            }
            if (isNotFirst) {
                block.append(", ").append(newColumn).append(", ");
            }
            isNotFirst = true;

            block.append(convert2item(form, element));
            
            if ((i + 1) == elements.size()) {
                block.append("]}");
                formBody.append(", ").append(block);
                break;
            }
        }
        // end
        StringBuffer bodyBlock = FormUtil.newFormBlock(fColspan, "form_body").append(formBody).append("]}");
        formJson.append(",").append(bodyBlock).append("]");
        if (keptColumns.length() > 0) keptColumns.append(",");
        if (increaseColumns.length() > 0) increaseColumns.append(",");
        if (inheritColumns.length() > 0) inheritColumns.append(",");
        //System.out.println(keptColumns);
        //System.out.println("formJson = " + formJson);
        return new DhtmlxForm(String.valueOf(formJson), "", "", "", "", String.valueOf(keptColumns), String.valueOf(increaseColumns), String.valueOf(inheritColumns), String.valueOf(defaultValues));
    }
    
    /**
     * qiucs 2013-9-11 
     * <p>标题: getElementColspan</p>
     * <p>描述: 获取表单元素占用列</p>
     * @param  form
     * @param  element
     * @return int    返回类型   
     * @throws
     */
    protected float getElementColspan(AppForm form, AppFormElement element) {
        if (FormUtil.FT_TEXTAREA.equals(convert2type(element))) {
            return form.getColspan();
        }
        return element.getColspan();
    }
    /**
     * qiucs 2013-9-11 
     * <p>标题: convert2item</p>
     * <p>描述: 转换为DHTMLXFORM元素格式</p>
     * @param  form
     * @param  element
     * @return String    返回类型   
     * @throws
     */
    protected String convert2item(AppForm form, AppFormElement element) {
        StringBuffer itemJson = new StringBuffer();
        String type = convert2type(element);
        itemJson.append("{");
        itemJson.append("name:\"").append(element.getColumnName()).append("\""); // name
        if (FormUtil.FT_HIDDEN.equals(type)) {
            itemJson.append(",type:\"hidden\"");  // type
        } else {
            String label = element.getShowName();
            float colspan = getElementColspan(form, element);
            // required
            if ("1".endsWith(element.getRequired())) {
                itemJson.append(",required:true"); 
            } else {
                label = (label + "：");
            }
            if ("1".equals(form.getBorder())) {
                label = ("<strong>" + label + "</strong>");
            }
            // label
            itemJson.append(",label:\"").append(label).append("\"");  
            // readonly
            if ("1".equals(element.getReadonly())) {
                itemJson.append(",readonly:true"); 
            }
            // type
            if (FormUtil.FT_CALENDAR.equals(type)) {
                itemJson.append(",type:\"").append(FormUtil.FT_CALENDAR).append("\",dateFormat:\"%Y-%m-%d\"");
            } else if (FormUtil.FT_COMBO.equals(type)) {
                itemJson.append(",type:\"").append(FormUtil.FT_COMBO).append("\"");
                // 下拉框值
                StringBuffer codes = new StringBuffer("[");
                codes.append("{value:\"\", text:\"请选择\"}");
                if (ConstantVar.DataType.ENUM.equals(element.getDataType()) && StringUtil.isNotEmpty(element.getCodeTypeCode())) {
                    List<Code> list = CodeUtil.getInstance().getCodeList(element.getCodeTypeCode());
                    if (null != list) {
                        for (Code code : list) {
                            codes.append(",").append("{value:\"").append(code.getValue())
                                          .append("\",text:\"").append(code.getName()).append("\"}");
                        }
                    }
                }else if(ConstantVar.DataType.USER.equals(element.getDataType())){
                	/*try {
						@SuppressWarnings({"unchecked" })
						List<UserInfo> ulist=SystemFacadeFactory.newInstance().createUserInfoFacade().getAllUserInfo();
						if(null != ulist){
							for (UserInfo info : ulist) {
								codes.append(",").append("{value:\"").append(info.getUserID())
											.append("\",text:\"").append(info.getUserName()).append("\"}");
							}
						}
					} catch (SystemFacadeException e) {
						 e.printStackTrace();
				         throw new RuntimeException(e.getMessage());
					}*/
                }else if(ConstantVar.DataType.PART.equals(element.getDataType())){
                	/*try {
                        List<OrgInfo> olist=SystemFacadeFactory.newInstance().createOrgInfoFacade().getAllOrg();
                        if(null != olist){
                            for (OrgInfo orgInfo : olist) {
                                codes.append(",").append("{value:\"").append(orgInfo.getOrganizeID())
                                            .append("\",text:\"").append(orgInfo.getOrganizeName()).append("\"}");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
                codes.append("]");
                itemJson.append(",options:").append(codes);
            } else if (FormUtil.FT_INPUT.equals(type)) {
                itemJson.append(",type:\"").append(FormUtil.FT_INPUT).append("\"");
            } else if (FormUtil.FT_NUMBER.equals(type)) {
                itemJson.append(",type:\"").append(FormUtil.FT_INPUT).append("\"");
            } else if (FormUtil.FT_TEXTAREA.equals(type)) {
                itemJson.append(",type:\"").append(FormUtil.FT_INPUT).append("\"").append(",rows:3");
            }
            // width
            if (colspan > 1) {
                itemJson.append(",width:").append((FormUtil.WIDTH_INPUT + FormUtil.WIDTH_LABEL) * colspan - FormUtil.WIDTH_LABEL);  
            }
        }
        // value
        if (StringUtil.isNotEmpty(element.getDefaultValue())) {
            itemJson.append(",value:\"").append(element.getDefaultValue()).append("\"");
        }
        // maxLength
        if (StringUtil.isNotEmpty(element.getLength())) {
            itemJson.append(",maxLength:").append(element.getLength());
        }
        // validate
        if (StringUtil.isNotEmpty(element.getValidation())) {
            itemJson.append(",validate:\"").append(element.getValidation()).append("\"");
        }
        // validate
        if (StringUtil.isNotEmpty(element.getTooltip())) {
            itemJson.append(",tooltip:\"").append(element.getTooltip()).append("\"");
        }
        itemJson.append("}");
        return itemJson.toString();
    }
    /**
     * qiucs 2013-9-11 
     * <p>标题: convert2type</p>
     * <p>描述: 转换为DHTMLXFORM元素类型</p>
     * @param  element
     * @return String    返回类型   
     * @throws
     */
    protected String convert2type(AppFormElement element) {
        /*if ("1".equals(element.getHidden())) {
            return FT_HIDDEN;
        }
        if (ConstantVar.DataType.CHAR.equals(element.getDataType()) ) {
            if  ("1".equals(element.getTextarea())) {
                return FT_TEXTAREA;
            } else {
                return FT_INPUT;
            }
        }
        if (ConstantVar.DataType.DATE.equals(element.getDataType())) {
            return FT_CALENDAR;
        }
        if (ConstantVar.DataType.NUMBER.equals(element.getDataType())) {
            return FT_NUMBER;
        }
        return FT_COMBO;//*/
        return FormUtil.getItemType("1".equals(element.getHidden()), element.getInputType());
    }

    /**
     * qiucs 2013-9-22 
     * <p>标题: getDomain</p>
     * <p>描述: </p>
     * @param  tableId
     * @param  componentVersionId
     * @return Object    返回类型   
     * @throws
     */
    public Object getDomain(String tableId, String componentVersionId, String menuId) {
        Domain domain = new Domain();
        try {
            AppForm form = findByFk(tableId, componentVersionId, menuId);
            if (null != form) {
                int width = FormUtil.getWidth(form.getColspan());
                domain.setWidth(width);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return domain;
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        getService(AppFormElementService.class).deleteByTableId(tableId);
        getDao().deleteByTableId(tableId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_FORM, tableId, AppUtil.TABLE_ID);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getService(AppFormElementService.class).deleteByComponentVersionId(componentVersionId);
        getDao().deleteByComponentVersionId(componentVersionId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_FORM, componentVersionId, AppUtil.COMPONENT_VERSION_ID);
    }
    
    /**
     * qiucs 2014-12-12 
     * <p>描述: 根据 menu id 删除配置</p>
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        // form element
        getService(AppFormElementService.class).deleteByMenuId(menuId);
        // form configuration
        getDao().deleteByMenuId(menuId);
        // 更新缓存
        AppUtil.getInstance().removeApp(AppUtil.APP_COLUMN, menuId, AppUtil.MENU_ID);
    }
    
    /**
     * qiucs 2015-6-26 上午10:42:48
     * <p>描述: 应用到 </p>
     * @return void
     */
    @Transactional
    public void applyTo (String tableId, String componentVersionId, String menuId, String toComponentVersionId, String toMenuId) {
    	AppForm entity = findByFk(tableId, componentVersionId, menuId);
    	
    	AppForm distEntity = new AppForm();
    	if (null != entity) {
    		BeanUtils.copy(entity, distEntity);
    		distEntity.setId(null);
    	}
    	distEntity.setTableId(tableId);
    	distEntity.setComponentVersionId(toComponentVersionId);
    	distEntity.setMenuId(toMenuId);
    	// 清空历史配置
    	clear(distEntity);
    	
    	if (null == entity) return ;
    	
    	// 保存拷贝配置
    	save(distEntity);
    	// 拷贝明细配置
    	getService(AppFormElementService.class).applyTo(tableId, componentVersionId, menuId, toComponentVersionId, toMenuId);
    	// 更新配置标记
    	getService(AppDefineService.class).updateAppDefine(tableId, toComponentVersionId, toMenuId, CommonUtil.SUPER_ADMIN_ID, 
                AppDefine.TYPE_FORM, AppDefine.DEFINE_YES);
    }
}

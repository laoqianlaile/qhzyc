package com.ces.config.datamodel.page.coral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.config.dhtmlx.entity.appmanage.AppButton;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.service.appmanage.AppButtonService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowButtonSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.AppDefineUtil.ButtonUI;
import com.ces.config.utils.ComponentInfoUtil;
import com.ces.config.utils.ModuleUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.web.listener.XarchListener;

public class TbarPageModel extends DefaultPageModel {
    
    public static final String P_TBAR_TYPE = "P_type";
    /* 菜单ID参数名称.*/
    public static final String P_OP = "P_op";
    /* 工具条索引参数名称.*/
    public static final String P_AREAINDEX = "P_areaIndex";
    
    public static final String OP_CREATE = "0";
    public static final String OP_UPDATE = "1";
    public static final String OP_VIEW   = "2";
    
    private String type;
    private String areaIndex;
    private String operation;
    private String processInstanceId;
    private String activityId;
    
    public void init() {
    	Map<String, List<Object>> toolbarMap = new HashMap<String, List<Object>>();
    	List<Object> topList = buttons2tbar("0");
    	List<Object> bottomList = null;
        if (AppDefineUtil.L_FORM.equals(getType())) {
        	if (StringUtil.isNotEmpty(getBox())) {
        		if (!topList.isEmpty()) {
        			bottomList = new ArrayList<Object>(topList);
        			bottomList.add(0, "->");
        			bottomList.add(0, "");
        		}
        	} else {
        		bottomList = buttons2tbar("1");
        	}
        }
        if (null != topList) {
        	toolbarMap.put("top", topList);
        }
        if (null != bottomList) {
            toolbarMap.put("bottom", bottomList);
        }
        setData(toolbarMap);
    }
    
    /**
     * qiucs 2014-7-31 
     * <p>描述: 判断是否为查看页面按钮</p>
     * @param  entity
     * @return boolean    返回类型   
     * @throws
     */
    private boolean isViewButton(String code) {
        return (ButtonUI.Code.FIRST_RECORD.equals(code) 
                || ButtonUI.Code.PREVIOUS_RECORD.equals(code)
                || ButtonUI.Code.NEXT_RECORD.equals(code) 
                || ButtonUI.Code.LAST_RECORD.equals(code));
    }
    /**
     * qiucs 2014-8-11 
     * <p>描述: 工作流按钮处理</p>
     * @param  list
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    private List<Object> proccessWorkflow() {
    	List<Object> tbarList = new ArrayList<Object>();
    	List<Object> leftList = new ArrayList<Object>();
    	List<Object> rightList = new ArrayList<Object>();
        Map<String, Object> item = null;
        String tableId = WorkflowUtil.getViewId(getWorkflowId());
        String componentVersionId = getBox();
        String menuId = null;
        //Map<String, Object> rightData = new HashMap<String, Object>();
        if (AppButton.BUTTON_FORM.equals(getType()) && StringUtil.isNotEmpty(getProcessInstanceId())) {
        	menuId = XarchListener.getBean(WorkflowVersionService.class).getFormVersionId(getWorkflowId(), getProcessInstanceId());
        } else {
        	menuId = XarchListener.getBean(WorkflowVersionService.class).getRunningVersionId(getWorkflowId());
        }
        List<AppButton> blist = XarchListener.getBean(AppButtonService.class).findByFk(tableId, componentVersionId, menuId, getType());
        
        if (WorkflowUtil.Box.todo.equals(getBox())) {
        	List<String> hlist = XarchListener.getBean(WorkflowButtonSettingService.class).getHiddenButtons(menuId, componentVersionId, getType());
        	int len = blist.size();
        	for (int i = len - 1; i > -1; i--) {
				AppButton btn = blist.get(i);
				if (hlist.contains(btn.getButtonCode())) {
					blist.remove(i);
				}
			}
        }
        
        for (AppButton btn : blist) {
        	if (ButtonUI.Code.BASE_SEARCH.equals(btn.getButtonCode())
        			|| ButtonUI.Code.INTEGRATION_SEARCH.equals(btn.getButtonCode())) {
        		//rightData.put(btn.getButtonCode(), true);
        		item = new HashMap<String, Object>();
        		item.put("id", btn.getButtonCode());
        		item.put("label", btn.getShowName());
        		item.put("cls", btn.getButtonClass());
        		item.put("type", "button");
        		rightList.add(item);
        	} else {
        		item = new HashMap<String, Object>();
        		item.put("id", btn.getButtonCode());
        		item.put("label", btn.getShowName());
        		item.put("cls", btn.getButtonClass());
        		item.put("type", "button");
        		item.put("assembleType", "1");
        		leftList.add(item);
        	}
		}
        // 分组处理
        if (!rightList.isEmpty()) {
        	leftList.add("");
        }
        leftList.add("->");
        
        tbarList.addAll(leftList);
        tbarList.addAll(rightList);
        
        return tbarList;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public String getAreaIndex() {
        return areaIndex;
    }
    
    public void setAreaIndex(String areaIndex) {
        this.areaIndex = areaIndex;
    }
    
    public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	/**
     * qiucs 2014-12-4 
     * <p>描述: 转换为工具条</p>
     * @return Object    返回类型   
     * @throws
     */
    private List<Object> buttons2tbar(String pos) {
        if (StringUtil.isNotEmpty(getBox())) {
            return proccessWorkflow();
        }
        // TODO
        List<ConstructDetail> cblist = getConstructDetails(pos);
        if (null == cblist || cblist.isEmpty()) return null;
        // 组装构件中的权限按钮
        List<String> notAuthorityButtons = AuthorityUtil.getInstance().getConstructButtonAuthority(getMenuId(), getComponentVersionId());
        for (String code : notAuthorityButtons) {
            for (ConstructDetail cb : cblist) {
                if (code.equals(cb.getId())) {
                    cblist.remove(cb);
                    break;
                }
            }
        }
        // 
        List<Object> tbarList = new ArrayList<Object>();    // 工具条

        List<Object> leftList = new ArrayList<Object>();
        List<Object> centerList = new ArrayList<Object>();
        List<Object> rightList = new ArrayList<Object>();
        
        String parentButtonCode = "";
        String position = "";
        Object btnItem = null;
        
        for (ConstructDetail cb : cblist) {
            parentButtonCode = (StringUtil.isEmpty(cb.getParentButtonCode()) || ConstructDetail.COMBOBOX_SEARCH.equals(cb.getButtonCode()))
                    ? "-1" : cb.getParentButtonCode();
            cb.setParentButtonCode(parentButtonCode);
            // 表单按钮控制
            if (AppDefineUtil.L_FORM.equals(getType())) {
                if (OP_CREATE.equals(getOperation())) {
                    if (isViewButton(cb.getButtonCode())) continue;
                } else if (OP_VIEW.equals(getOperation())) {
                    if (!isViewButton(cb.getButtonCode()))continue;
                } else if (OP_UPDATE.equals(getOperation()) && 
                		(ButtonUI.Code.SAVE_AND_CREATE.equals(cb.getButtonCode()) ||
                				ButtonUI.Code.CREATE.equals(cb.getButtonCode()))) {
                	continue;
                }
            }
            // 打开方式
            if (ButtonUI.Code.BASE_SEARCH.equals(cb.getButtonCode())) {
            	String stype = SystemParameterUtil.getInstance().getSystemParamValue("检索区显示方式");
            	if ("1".equals(stype)) cb.setPosition("0");
            }
            position = cb.getPosition();
            btnItem = _2map(cb);
            if ("-1".equals(parentButtonCode)) {
            	if ("0".equals(position)) {
            		leftList.add(btnItem);
            	} else if ("1".equals(position)) {
            		centerList.add(btnItem);
            	} else if ("2".equals(position)) {
            		rightList.add(btnItem);
            	}
            } else {
            	if ("0".equals(position)) {
            		findOwnerGroup(leftList, btnItem, parentButtonCode);
            	} else if ("1".equals(position)) {
            		findOwnerGroup(centerList, btnItem, parentButtonCode);
            	} else if ("2".equals(position)) {
            		findOwnerGroup(rightList, btnItem, parentButtonCode);
            	}
            	/*boolean isHandle = findOwnerGroup(leftList, btnItem, parentButtonCode);
            	if (!isHandle) {
            		isHandle = findOwnerGroup(centerList, btnItem, parentButtonCode);
            	}
            	if (!isHandle) {
            		isHandle = findOwnerGroup(rightList, btnItem, parentButtonCode);
            	}*/
            }
        }
        // 分组处理
        if (!centerList.isEmpty() || !rightList.isEmpty()) {
        	if (leftList.isEmpty()) {
        		leftList.add("");
        	}
        	leftList.add("->");
        }
        if (!centerList.isEmpty()) {
        	if (rightList.isEmpty()) {
        		rightList.add("->");
        		rightList.add("");
        	} else {
        		centerList.add("->");
        	}
        }
        
        tbarList.addAll(leftList);
        tbarList.addAll(centerList);
        tbarList.addAll(rightList);
        
        return tbarList;
    }
    
    /**
     * qiucs 2015-8-26 上午11:49:15
     * <p>描述: TODO(这里用一句话描述这个方法的作用) </p>
     * @return boolean
     */
    private boolean findOwnerGroup(List<Object> list, Object item, String parentCode) {
    	Map<String, Object> itemMap = null;
    	for (int i = 0, len = list.size(); i < len; i++) {
    		itemMap = (Map<String, Object>) list.get(i);
    		if (parentCode.equals(itemMap.get("id"))) {
    			List<Object> subItems = null;
    			if (itemMap.containsKey("data")) {
    				subItems = (List<Object>) itemMap.get("data");
    			} else {
    				subItems = new ArrayList<Object>();
    				itemMap.put("data", subItems);
    			}
    			if (itemMap.get("e-type") != null) {
    			    itemMap.put("type", itemMap.get("e-type"));
    			} else {
    			    itemMap.put("type", "menubutton");
    			}
    			subItems.add(item);
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * qiucs 2014-12-4 
     * <p>描述: 工具条中的下拉框检索配置</p>
     * @param  str
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    private List<Map<String, Object>> _2options(String str) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        String[] optArr = str.split(";");
        String[] termArr= null;
        boolean selected = false;
        for (String opt : optArr) {
            Map<String, Object> item = new HashMap<String, Object>();
            termArr = opt.split(":");
            if (1 == termArr.length) {
                item.put("value", termArr[0]);
                item.put("text" , termArr[0]);
            } else {
                item.put("value", termArr[0]);
                item.put("text" , termArr[1]);
            }
            if (StringUtil.isEmpty(item.get("value"))) {
                item.put("selected", Boolean.TRUE);
                selected = true;
            }
            data.add(item);
        }
        // 默认选中第一个
        if (!selected && data.size() > 0) data.get(0).put("selected", Boolean.TRUE);
        return data;
    }

    /**
     * qiucs 2014-12-4 
     * <p>描述: 构件组装按钮</p>
     * @return List<ConstructDetail>    返回类型   
     * @throws
     */
    private List<ConstructDetail> getConstructDetails(String pos) {
        String zoneName = null;
        Module m = ModuleUtil.getModule(getModuleId());
        if (null == m) return new ArrayList<ConstructDetail>();
        
        if ("5".equals(m.getType())) {
            // 逻辑表构件
            zoneName = AppDefineUtil.getCommonZoneName(TableUtil.getLogicTableCode(getTableId()), type, null, pos);
        } else {
            // 5-物理表构件  或  6-通用表构件
            zoneName = AppDefineUtil.getZoneName(("6".equals(m.getType()) ? "" : getTableId()), type, Integer.parseInt(areaIndex), null, pos);
        }
        List<ConstructDetail> list = ComponentInfoUtil.getInstance().getAppConstructDetails(getComponentVersionId(), zoneName);// XarchListener.getBean(ConstructDetailService.class).getAppConstructDetails(getComponentVersionId(), zoneName);
        /*if (null == list || list.isEmpty()) {
            list = getDefaultButtons();
        }*/
        Collections.sort(list);
        return list;
    }
    /**
     * qiucs 2014-12-4 
     * <p>描述: 自定义默认按钮</p>
     * @return List<ConstructDetail>    返回类型   
     * @throws
     */
    private List<ConstructDetail> getDefaultButtons() {
        if (AppDefineUtil.isForm(type)) {
            return XarchListener.getBean(ConstructDetailService.class).getFormDefaultButtonList();
        }
        return XarchListener.getBean(ConstructDetailService.class).getGridDefaultButtonList();
    }
    
    /**
     * qiucs 2014-12-4 
     * <p>描述: 转换为coral40工具条按钮</p>
     * @param  cb
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> _2map(ConstructDetail cb) {
    	
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("id", cb.getButtonCode());
        item.put("assembleType", cb.getAssembleType());
        if ("-1".equals(cb.getParentButtonCode())) {
            item.put("label", cb.getButtonDisplayName());
            if (StringUtil.isNotEmpty(cb.getButtonCls())) {
            	item.put("cls", cb.getButtonCls());
            }
            if (StringUtil.isNotEmpty(cb.getButtonIcon())) {
                item.put("icon", cb.getButtonIcon());
            }
            item.put("type", "button");
            if ("1".equals(cb.getButtonType())) {
                if (StringUtil.isNotEmpty(cb.getComponentVersionId())) {
                    item.put("e-type", "splitbutton");
                } else if (cb.getButtonCode().startsWith("CD_BUTTON") || "more".equals(cb.getButtonCode())) {
                    item.put("e-type", "menubutton");
                } else {
                    item.put("e-type", "splitbutton");
                }
            }
            if (ConstructDetail.COMBOBOX_SEARCH.equals(cb.getButtonCode())) {
            	if (StringUtil.isNotEmpty(cb.getButtonDisplayName())) {
                	item.put("label", cb.getButtonDisplayName());
                }
            	item.put("columnName", AppDefineUtil.getItemName(cb.getButtonName(), AppDefineUtil.Operator.Val.IN));
            	item.put("options", _2options(cb.getSearchComboOptions()));
            }
        } else {
        	item.put("name", cb.getButtonDisplayName());
            item.put("items", new ArrayList<Map<String, Object>>());
        }
        
        return item;
    }
    
    protected List<Map<String, Object>> processCoflowButton() {
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	
    	return list;
	}
}

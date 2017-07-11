package com.ces.config.datamodel.page.coral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.workflow.wapi.define.WFDefineException;

import com.ces.config.application.CodeApplication;
import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.config.dhtmlx.dao.component.ComponentReserveZoneDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailDao;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppForm;
import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowActivity;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowFormSetting;
import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.service.appmanage.AppFormElementService;
import com.ces.config.dhtmlx.service.appmanage.AppFormService;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowFormSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FormUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * <p>描述: coral4.0表单</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-14 上午10:39:53
 *
 */
public class FormPageModel extends DefaultPageModel {
	
	private static Log log = LogFactory.getLog(FormPageModel.class);
    
    public static String P_TIMESTAMP  = "P_timestamp";
    public static String P_M_TABLE_ID  = "P_M_tableId";
    public static String P_PROCESS_INSTANCE_ID = "P_processInstanceId";
    public static String P_ACTIVITY_ID = "P_activityId";
    
    private double width;
    private double height;
    private int colspan;
    private String type;
    private String timestamp;
    private String masterTableId;
    private String processInstanceId;
    private String activityId;
    // 表单初始化默认值
    private Map<String, String> defaultData = new HashMap<String, String>();
    // 流程程结节默认值
    private Map<String, String> flowDefaultData = new HashMap<String, String>();
    private List<String> keptItems = new ArrayList<String>();
    private List<String> increaseItems = new ArrayList<String>();
    private List<String> inheritItems = new ArrayList<String>();
    // 是否为流程第一个节点（即开始节点后面一个节点）
    private boolean firstActivity;
    private Map<String, List<String>> relationMap; // 主从表关联关系
    // 标记前一个幸免元素是否为textarea(占位符要根据前面一个元素来设置高度)
    //private boolean isPreTextarea = false;  // false--非textarea, true--textarea
    // 
    private boolean readonly = false;
    
    private boolean isSameCol = false;
    
    private int percent = 0;
    
    public void init() {
    	try {
    		setData(cast2html());
		} catch (Exception e) {
			log.error("获取表单界面元素出错", e);
		}
    }
    /**
     * @throws WFDefineException 
     * qiucs 2014-7-11 
     * <p>描述: 将配置信息转换为coral4.0表单</p>
     * @return String    返回类型   
     * @throws
     */
    public String cast2html() throws WFDefineException {
        StringBuffer html = new StringBuffer();
        double totalColspan = 0;
        String componentVersionId = getComponentVersionId();
        String menuId = getMenuId();
        String firstActivityId = null;
        Map<String, WorkflowFormSetting> formSettingMap = new HashMap<String, WorkflowFormSetting>();
        String workflowCode = null;
        Map<String, List<String>> relationMap = null;
        Set<String> hiddenColumnSet = new HashSet<String>(10);
        WorkflowFormSetting formSetting = null;
        Set<String> formSettingKeySet = null;
		hiddenColumnSet.add(ConstantVar.ColumnName.ID);
        if (StringUtil.isNotEmpty(getWorkflowId())) {
        	// 工作流编码
        	workflowCode = WorkflowUtil.getWorkflowCode(getWorkflowId());
        	// 工作流应用定义规则
        	componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
        	setTableId(WorkflowUtil.getBusinessTableId(getWorkflowId()));
        	// WorkflowVersion.id
        	if (StringUtil.isNotEmpty(getProcessInstanceId())) {
        		menuId = XarchListener.getBean(WorkflowVersionService.class).getFormVersionId(getWorkflowId(), getProcessInstanceId());
        	} else {
        		menuId = XarchListener.getBean(WorkflowVersionService.class).getRunningVersionId(getWorkflowId());
        	}
        	if (WorkflowUtil.Box.todo.equals(getBox())) {
        		firstActivityId = XarchListener.getBean(WorkflowVersionService.class).getFirstActivityId(menuId);
            	setFirstActivity(getActivityId().equals(firstActivityId));
        	}
        	formSettingMap = XarchListener.getBean(WorkflowFormSettingService.class).getFormSettingMap(menuId, getActivityId());
        	formSettingKeySet = formSettingMap.keySet();
        	// 工作流创建者字段
            if (WorkflowActivity.START_ACTIVITY.equals(getActivityId())) {
            	hiddenColumnSet.add(WorkflowUtil.C_BELONG_WORKFLOW_CODE);
            	hiddenColumnSet.add(WorkflowUtil.C_REGISTER_USER_ID);
            	hiddenColumnSet.add(WorkflowUtil.C_PROCESS_INSTANCE_ID);
            }
        }
        
        if (StringUtil.isNotEmpty(getMasterTableId())) {
            relationMap = TableUtil.getTableRelation(getMasterTableId() , getTableId());
            hiddenColumnSet.addAll(relationMap.get(getTableId()));// = new ArrayList<String>(relationMap.get(getTableId()));
            setRelationMap(relationMap);
        }
        
        // 表单
        AppForm appForm = XarchListener.getBean(AppFormService.class).findDefineEntity(getTableId(), componentVersionId, menuId);
        if (null == appForm) return "";
        Boolean isBorder = "1".equals(appForm.getBorder()); // 是否加粗
        setColspan(appForm.getColspan());
        setType(appForm.getType());
        // 表单元素
        List<AppFormElement> elements = XarchListener.getBean(AppFormElementService.class).findDefineList(getTableId(), componentVersionId, menuId);
        
        html.append("<form class=\"coralui-form\" id=\"" + getFormId() + "\" data-options=\"heightStyle:&quot;fill&quot;\">\n");
        
        html.append("<div class=\"fillwidth colspan" + getColspan() + " clearfix\">\n");
        
        StringBuffer oneRow = new StringBuffer("<div class=\"clearfix\">");
        float rowColspan = 0;
        for (int i = 0, size = elements.size(); i < size; i++) {
            AppFormElement ele = elements.get(i);
            if (StringUtil.isNotEmpty(ele.getDefaultValue())) {
            	if (isCalendar(ele)) {
    				ele.setDefaultValue(FormUtil.processDefaultValue(ele.getDefaultValue(), getSrcDateFormat(ele)));
            	} else if (isTextbox(ele)) {
            		ele.setDefaultValue(FormUtil.processDefaultValue(ele.getDefaultValue(), "name"));
            	} else {
            		ele.setDefaultValue(FormUtil.processDefaultValue(ele.getDefaultValue(), "code"));
            	}
                defaultData.put(ele.getColumnName(), ele.getDefaultValue());
            }
            if (StringUtil.isBooleanTrue(ele.getKept())) {
                keptItems.add(ele.getColumnName());
            }
            if (StringUtil.isBooleanTrue(ele.getIncrease())) {
                increaseItems.add(ele.getColumnName());
            }
            if (StringUtil.isBooleanTrue(ele.getInherit())) {
                inheritItems.add(ele.getColumnName());
            }
            if (hiddenColumnSet.contains(ele.getColumnName())) {
            	hiddenColumnSet.remove(ele.getColumnName());
            }
            if (null != formSettingKeySet && formSettingKeySet.contains(ele.getColumnId())) {
            	formSetting = formSettingMap.get(ele.getColumnId());
            	if (StringUtil.isBooleanTrue(formSetting.getDisabled())) {
            		ele.setDisabled("1");
            	}
            	if (StringUtil.isNotEmpty(formSetting.getDefaultValue())) {
            		flowDefaultData.put(ele.getColumnName(), formSetting.getDefaultValue());
            	}
            }
            totalColspan += ele.getColspan();
        	percent = (percent + ele.getSpacePercent()) % 100; 
        	// 1/3 列
        	if (percent == 99) percent = 0;
            if (isSubfield(ele)) {
            	html.append(cast2input(ele, isBorder));
            } else {
            	if (!isHidden(ele)) {
            		rowColspan += (ele.getColspan() * ele.getSpacePercent() / 100f); 
            	}

            	if (rowColspan > appForm.getColspan()) {
            		oneRow.append("</div>");
            		html.append(oneRow);
            		oneRow = new StringBuffer("<div class=\"clearfix\">\n");
            		rowColspan = (ele.getColspan() * ele.getSpacePercent() / 100f);
            	}
            	oneRow.append(cast2input(ele, isBorder));
            	if (i == (size - 1)) {
            		oneRow.append("</div>");
            		html.append(oneRow);
            		oneRow = null;
            		rowColspan = 0;
            	}
            }
            isSameCol = (percent == 0 ? false : true);
        }
        
        html.append("</div>\n");
        // 隐藏字段（包含主表关联字段、系统默认字段）
        if (!hiddenColumnSet.isEmpty()) {
        	//System.out.println("hidden column set: " + hiddenColumnSet);
            for (String col : hiddenColumnSet) {
            	html.append("<input id=\"").append(getElementId(col)).append("\" name=\"" + col + "\" type=\"hidden\"");
            	if (WorkflowUtil.C_REGISTER_USER_ID.equals(col)) {
            		html.append(" value=\"").append(CommonUtil.getCurrentUserId()).append("\"");
            	} else if (WorkflowUtil.C_PROCESS_INSTANCE_ID.equals(col)) {
            		html.append(" value=\"0\"");
            	}  else if (WorkflowUtil.C_BELONG_WORKFLOW_CODE.equals(col)) {
            		html.append(" value=\"").append(workflowCode).append("\"");
            	} 
            	html.append(" class=\"coralui-textbox\">\n");
            }
        }
        
        html.append("</form>");
        
        // 表单高度
        setHeight(Math.ceil(totalColspan / appForm.getColspan()) * 40);
        
        //System.out.println(html);
        
        return String.valueOf(html);
    }
    /**
     * qiucs 2014-7-11 
     * <p>描述: 配置信息转换为coral4.0表单元素</p>
     * @param  element
     * @param  isBorder
     * @return String    返回类型   
     * @throws
     */
    protected String cast2input(AppFormElement element, Boolean isBorder) {
        StringBuffer inputHTML = new StringBuffer();
        
        if (isHidden(element)) {
            inputHTML.append("<input id=\"").append(getElementId(element.getColumnName()))
                     .append("\" name=\"" + element.getColumnName() + "\" type=\"hidden\" class=\"coralui-textbox\">\n");
        } else {
            String elementId = null;
            
            if (isSubfield(element) || isPlaceholder(element)) {
            	String idPre = element.getDefaultValue();
            	if (StringUtil.isEmpty(idPre)) {
            		idPre = StringUtil.mixRandom(6);
            	}
            	elementId = (getElementId(idPre));
            } else {
            	elementId = (getElementId(element.getColumnName()));
            }

            // start form element div
            if (!isSameCol) {
            	int colspan = element.getColspan();
        		colspan = (int) (AppFormElement.SUBFIELD_ID.equals(element.getColumnId()) ? 12 : (12 * colspan) / getColspan());
        		inputHTML.append("<div id=\"").append(elementId).append("_DIV\" class=\"app-inputdiv" + colspan + "\">");
            	// label
            	if (!isSubfield(element) && !isPlaceholder(element)) {
            		inputHTML.append("<label id=\"").append(elementId).append("_LABEL\" class=\"app-input-label\">")
            		.append(isBorder ? "<strong>" : "")
            		.append(element.getShowName()).append("：")
            		.append(isBorder ? "</strong>" : "")
            		.append("</label>");
            	}
            }
            // element
            
            inputHTML.append(getStartTag(element));
            inputHTML.append(" id=\"").append(elementId);
            inputHTML.append("\" data-options=\"").append(getDataOptions(element)).append("\"");
            inputHTML.append(getEndTag(element));
            // end form element div
            if (percent == 0) {
            	inputHTML.append("</div>\n");
            }
        }
        return String.valueOf(inputHTML);
    }
    /**
     * qiucs 2014-7-11 
     * <p>描述: 开始标签</p>
     * @param  element
     * @return String    返回类型   
     * @throws
     */
    private String getStartTag(AppFormElement element) {
        StringBuffer startTag = new StringBuffer();
        
        if (isTextbox(element)) {
            startTag.append("<input class=\"coralui-textbox\" type=\"text\"");
        } else if (isTextarea(element)) {
            startTag.append("<textarea class=\"coralui-textbox\"");
        } else if (isCombobox(element)) {
            startTag.append("<input class=\"coralui-combobox\"");
        } else if (isCombotree(element)) {
            startTag.append("<input class=\"coralui-combotree\"");
        } else if (isCalendar(element)) {
            startTag.append("<input class=\"coralui-datepicker\" type=\"text\"");
        } else if (isRadio(element)) {
        	startTag.append("<input class=\"coralui-radiolist\"");
        } else if (isCheckbox(element)) {
        	startTag.append("<input class=\"coralui-checkboxlist\"");
        } else if (isSubfield(element)) {
            startTag.append("<div class=\"coralui-subfield\"");
        } else if (isPlaceholder(element)) {
        	startTag.append("<span class=\"signmark coral-textbox-default appform-placeholder\" ");
        	/*if (isPreTextarea) {
        		startTag.append("<span class=\"coral-textbox-textarea\" ");
        	} else {
        		startTag.append("<span class=\"coral-textbox-default\"");
        	}*/
        } else if (isCombogrid(element)) {
        	startTag.append("<input class=\"coralui-combogrid\"");
        }
        
        return String.valueOf(startTag);
    }
    /**
     * qiucs 2014-7-11 
     * <p>描述: 结束标签</p>
     * @param  element
     * @return String    返回类型   
     */
    private String getEndTag(AppFormElement element) {
        StringBuffer endTag = new StringBuffer();
        
        if (isTextarea(element)) {
            endTag.append("></textarea>");
        }/* else if (isPlaceholder(element)) {
            endTag.append("/></span>");
        }*/ else {
            endTag.append("/>");
        }
        
        return String.valueOf(endTag);
    }
    /**
     * qiucs 2014-7-11 
     * <p>描述: 标签中的data-options属性值</p>
     * @param  element
     * @return String    返回类型   
     * @throws
     */
    private String getDataOptions(AppFormElement element) {
        StringBuffer dataOptions = new StringBuffer();
        
        if (isPlaceholder(element)) return dataOptions.toString();
        // name
        dataOptions.append("name:&quot;").append(element.getColumnName()).append("&quot;");
        // value
        String defaultValue = element.getDefaultValue();
        if (StringUtil.isNotEmpty(defaultValue)) {
        	dataOptions.append(",").append("value:&quot;").append(defaultValue).append("&quot;");
        }
        if (element.getSpacePercent() < 100) {
        	dataOptions.append(",").append("componentCls:&quot;pct").append(element.getSpacePercent()).append("&quot;");
        }
        // height
        if (isTextarea(element)) {
        	dataOptions.append(",").append("height:&quot;").append(element.getDataTypeExtend()).append("px&quot;");
		}
        // required
        if ("1".equals(element.getRequired())) {
            dataOptions.append(",").append("required:true");
        }
        // disabled
        if ("1".equals(element.getDisabled())) {
            dataOptions.append(",").append("disabled:true");
        }
        // readonly
        if ("1".equals(element.getReadonly())) {
            dataOptions.append(",").append("readonly:true");
        }
        // max length
        if (isTextbox(element) || isTextarea(element)) {
            dataOptions.append(",").append("maxlength:").append(element.getLength());
        }
        // validate
        if (isNumberbox(element) && StringUtil.isEmpty(element.getValidation())) {
            if (isFloatingLabel(element)) { 
            	dataOptions.append(",").append("pattern:&quot;//^(-?[0-9]+)(\\.[0-9]{0," + StringUtil.null2zero(element.getPrecision()) + "})?$//&quot;");
            	dataOptions.append(",").append("errMsg:&quot;不符合浮点型格式或精度过大&quot;");
			} else if (isFloating(element)) {
				dataOptions.append(",").append("pattern:&quot;//^(-?[0-9]+)(\\.[0-9]{0," + StringUtil.null2zero(element.getDataTypeExtend()) + "})?$//&quot;");
				dataOptions.append(",").append("errMsg:&quot;不符合浮点型格式或精度过大&quot;");
			} else {
				dataOptions.append(",").append("validType:&quot;number&quot;");
			}
        } else if (StringUtil.isNotEmpty(element.getValidation())) {
        	if (isCalendar(element)) {
        		String date = FormUtil.processDefaultValue(ConstantVar.CurrentValue.DATE, "yyyy-MM-dd");
        		if (element.getValidation().equals(ConstantVar.Validation.MAXDATE)) {//设置最大日期
        			dataOptions.append(",").append("maxDate:&quot;" + date + "&quot;");
				} else if (element.getValidation().equals(ConstantVar.Validation.MINDATE)) {//设置最小日期
					dataOptions.append(",").append("minDate:&quot;" + date + "&quot;");
				}
			} else if (element.getValidation().equals(ConstantVar.Validation.PATTERN)) {//设置正则表达式
				dataOptions.append(",").append("pattern:&quot;" + element.getPattern() + "&quot;");
			} else {
				dataOptions.append(",").append("validType:&quot;" + element.getValidation() + "&quot;");
			}
        }
        // tooltip
        if (StringUtil.isNotEmpty(element.getTooltip())) {
        	addTooltip(dataOptions, element);
        	dataOptions.append("]");
		}
        // combobox options
        if (isCombobox(element) && StringUtil.isNotEmpty(element.getCodeTypeCode())) {
            /*dataOptions.append(",").append("url:&quot;")
                       .append("{action}!page.json?E_frame_name=coral&E_model_name=code&id=").append(element.getCodeTypeCode())
                       .append("&P_menuId=").append(getMenuId())
                       .append("&P_componentVersionId=").append(getComponentVersionId());
            if (StringUtil.isNotEmpty(element.getDefaultValue())) {
                dataOptions.append("&P_SELECTED=").append(element.getDefaultValue());
            }
            dataOptions.append("&quot;");*/
        	dataOptions.append(",data:").append(code2data(element.getCodeTypeCode(), element.getDefaultValue())).append("");
        } else if (isCombotree(element)) {
            dataOptions.append(",popupDialog:true")
                       .append(",beforeClick:&quot;CFG_combotreeControl&quot;")
                       .append(",url:&quot;")
                       .append("{action}!page.json?E_frame_name=coral&E_model_name=code&P_COMBO_TYPE=combotree&id=")
                       .append(element.getCodeTypeCode())
                       .append("&P_menuId=").append(getMenuId())
                       .append("&P_componentVersionId=").append(getComponentVersionId())
                       .append("&quot;");
        } else if (isSubfield(element)) {
            dataOptions.append(",").append("title:&quot;")
                       .append(element.getShowName())
                       .append("&quot;");
        } else if (isCheckbox(element) || isRadio(element)) {
        	dataOptions.append(",repeatLayout:&quot;flow&quot;")
        	           .append(",data:&quot;")
        	           .append(code2data(element.getCodeTypeCode()))
        	           .append("&quot;");
        } else if (isCalendar(element)) {
        	dataOptions.append(",showOn:&quot;button&quot;");
			dataOptions.append(",dateFormat:&quot;" + getDateFormat(element) + "&quot;");//显示日期格式
			dataOptions.append(",srcDateFormat:&quot;" + getSrcDateFormat(element) + "&quot;");//存入日期格式
        } else if (isCombogrid(element) || isTextboxButton(element)) {
        	if (isCombogrid(element)) {
        		dataOptions.append(",enableSearch:true,").append(combogridOptions(element));
        	}
            String columnId = element.getColumnId();
            String componentVersionId = getComponentVersionId();
            Construct construct = XarchListener.getBean(ConstructDao.class).getByAssembleComponentVersionId(componentVersionId);
            if (construct != null) {
            	String reserveZoneName = "";
            	if (ConstantVar.Component.Type.LOGIC_TABLE.equals(XarchListener.getBean(ComponentVersionService.class).getByID(
            			construct.getBaseComponentVersionId()).getComponent().getType())) {
            		String tableId = XarchListener.getBean(ColumnDefineService.class).getByID(columnId).getTableId();
            		reserveZoneName = AppDefineUtil.RZ_NAME_PRE.concat(XarchListener.getBean(ColumnDefineService.class).findByColumnNameAndTableId(
            				element.getColumnName(), TableUtil.getLogicTableCode(tableId)).getId());
				} else {
					reserveZoneName = AppDefineUtil.RZ_NAME_PRE.concat(columnId);
				}
                ComponentReserveZone componentReserveZone = XarchListener.getBean(ComponentReserveZoneDao.class).getByComponentVersionIdAndNameAndPage(
                        construct.getBaseComponentVersionId(), reserveZoneName, AppDefineUtil.getPage(1));
                if (componentReserveZone != null && StringUtil.isNotEmpty(componentVersionId)) {
                    List<ConstructDetail> constructDetailList = XarchListener.getBean(ConstructDetailDao.class)
                            .getByConstructIdAndReserveZoneIdOrderByShowOrderAsc(construct.getId(), componentReserveZone.getId());
                    if (CollectionUtils.isNotEmpty(constructDetailList)) {
                    	if (StringUtil.isEmpty(element.getTooltip())) {
                    		dataOptions
                            .append(",")
                            .append("buttons:[{'outerRight':[{id:'"
                                    + element.getColumnName()
                                    + "_button',label:'构件按钮',icons:'icon-checkmark-circle',text:false,click:function(e, data) {MT_FORM_click('{cformDivId}', '"
                                    + reserveZoneName + "', '" + element.getShowName() + "选择', 2)}}]}]");
						} else {
							addTooltip(dataOptions, element);
							dataOptions
							.append(",{id:'"
                                    + element.getColumnName()
                                    + "_button',label:'构件按钮',icons:'icon-checkmark-circle',text:false,click:function(e, data) {MT_FORM_click('{cformDivId}', '"
                                    + reserveZoneName + "', '" + element.getShowName() + "选择', 2)}}]}]");
						}
                    }
                }
            }
        } else if (isTextboxLabel(element) || isIntegerLabel(element)) {
        	if (StringUtil.isEmpty(element.getTooltip())) {
        		dataOptions
        		.append(",")
        		.append("buttons:[{'innerRight':[{label:'" + element.getDataTypeExtend() + "'}]}]");
			} else {
				addTooltip(dataOptions, element);
				dataOptions
				.append(",{'innerRight':[{label:'" + element.getDataTypeExtend() + "'}]}]");
			}
		} else if (isFloatingLabel(element)) {
			if (StringUtil.isEmpty(element.getTooltip())) {
				dataOptions
	    		.append(",")
	    		.append("buttons:[{'innerRight':[{label:'" + element.getLabel() + "'}]}]");
			} else {
				addTooltip(dataOptions, element);
				dataOptions
				.append(",{'innerRight':[{label:'" + element.getLabel() + "'}]}]");
			}
		}
        
        return String.valueOf(dataOptions).replace("\"", "&quot;");
    }
    
    /**
     * qiucs 2014-8-25 
     * <p>描述: 表单ID</p>
     * @return String    返回类型   
     */
    private String getFormId() {
        return "_".concat(getTableId()).concat("_").concat(getTimestamp());
    }
    
    /**
     * qiucs 2014-8-25 
     * <p>描述: 表单元素ID</p>
     * @param  ele
     * @return String    返回类型   
     */
    private String getElementId(String columnName) {
        return columnName.concat("_").concat(getTimestamp());
    }
    
    private boolean isHidden(AppFormElement element) {
        return "1".equals(element.getHidden());
    }
    
    private boolean isTextbox(AppFormElement element) {
        return FormUtil.CoralInputType.TEXTBOX.equals(element.getInputType());
    }
    
    private boolean isTextboxButton(AppFormElement element) {
        return FormUtil.CoralInputOption.TEXTBOXBUTTON.equals(element.getInputOption());
    }
    
    private boolean isTextboxLabel(AppFormElement element) {
    	return FormUtil.CoralInputOption.TEXTBOXLABEL.equals(element.getInputOption());
    }
    
    private boolean isNumberbox(AppFormElement element) {
        return ConstantVar.DataType.NUMBER.equals(element.getDataType());
    }
    
    private boolean isIntegerLabel(AppFormElement element) {
    	return FormUtil.CoralInputOption.INTEGERLABEL.equals(element.getInputOption());
    }
    
    private boolean isFloating(AppFormElement element) {
    	return FormUtil.CoralInputOption.FLOATING.equals(element.getInputOption());
    }
    
    private boolean isFloatingLabel(AppFormElement element) {
    	return FormUtil.CoralInputOption.FLOATINGLABEL.equals(element.getInputOption());
    }
    
    private boolean isCalendar(AppFormElement element) {
        return FormUtil.CoralInputType.DATEPICKER.equals(element.getInputType());
    }
    
    private boolean isCombobox(AppFormElement element) {
        return FormUtil.CoralInputType.COMBOBOX.equals(element.getInputType());
    }
    
    private boolean isCombogrid(AppFormElement element) {
        return FormUtil.CoralInputType.COMBOGRID.equals(element.getInputType());
    }
    
    private boolean isTextarea(AppFormElement element) {
        return FormUtil.CoralInputType.TEXTAREA.equals(element.getInputType());
    }
    
    private boolean isCombotree(AppFormElement element) {
        return FormUtil.CoralInputType.COMBOTREE.equals(element.getInputType());
    }
    
    private boolean isRadio(AppFormElement element) {
        return FormUtil.CoralInputType.RADIO.equals(element.getInputType());
    }
    
    private boolean isCheckbox(AppFormElement element) {
        return FormUtil.CoralInputType.CHECKBOX.equals(element.getInputType());
    }
    
    private boolean isSubfield(AppFormElement element) {
        return AppFormElement.SUBFIELD_ID.equals(element.getColumnId());
    }
    
    private boolean isPlaceholder(AppFormElement element) {
        return AppFormElement.PLACEHOLDER_ID.equals(element.getColumnId());
    }
    
    private String getDateFormat (AppFormElement element) {
    	if (StringUtil.isEmpty(element.getInputOption())) {
			return "yyyy-MM-dd HH:mm:ss";
		}
    	return element.getInputOption();
    }
    
    private String getSrcDateFormat (AppFormElement element) {
    	if (StringUtil.isEmpty(element.getDataTypeExtend())) {
			return "yyyy-MM-dd HH:mm:ss";
		}
    	return element.getDataTypeExtend();
    }
    
    private void addTooltip (StringBuffer dataOptions, AppFormElement element) {
    	dataOptions.append(",").append("buttons:[{'floatRight':[{" +
    			"label:'" + element.getTooltip() + "'," +
    			"text: false," +
    			"componentCls: 'question'," +
    			"icons:'icon-question2'," +
    			"click: function(e, data) {}}]}");
    }
    
    /**
     * qiucs 2015-1-22 上午11:06:17
     * <p>描述: 将编码转换成单选框或复选框数据 </p>
     * @return String
     */
    private String code2data(String codeTypeCode) {
    	if (StringUtil.isEmpty(codeTypeCode)) return "";
    	StringBuilder data = new StringBuilder();
    	List<Code> list = CodeUtil.getInstance().getCodeList(codeTypeCode);
    	int i = 0, len = list.size();
    	Code code = null;
    	for (; i < len; i++) {
    		code = list.get(i);
    		data.append(";").append(code.getValue()).append(":").append(code.getName());
    	}
    	if (data.length() > 0) data.deleteCharAt(0);
    	
    	return String.valueOf(data);
    }
    
    /**
     * qiucs 2015-8-24 上午11:08:03
     * <p>描述: 将编码转换成单下拉框数据 </p>
     * @return String
     */
    private String code2data(String codeTypeCode, String value) {
    	CodePageModel cpm = new CodePageModel();    	
    	cpm.setComboType(CodePageModel.COMBOBOX);
    	cpm.setId(codeTypeCode);
    	cpm.setMenuId(getMenuId());
    	cpm.setComponentVersionId(getComponentVersionId());
    	cpm.setTableId(getTableId());
    	cpm.setSelected(value);    	
    	cpm.init();    	
    	return JsonUtil.bean2json(cpm.getData());
    }
    
    /**
     * qiucs 2015-1-22 下午2:42:27
     * <p>描述: 将编码转换成下拉树 </p>
     * @return Object
     */
    @SuppressWarnings("unused")
    private String code2tree(String codeTypeCode) {
    	if (StringUtil.isEmpty(codeTypeCode)) return "[]";
    	Object data = CodeUtil.getInstance().getCodeTree(codeTypeCode);
    	return JsonUtil.bean2json(data);
    }
    
    /**
     * qiucs 2015-3-23 下午2:30:29
     * <p>描述: 下拉列表UI </p>
     * @return String
     */
    private String combogridOptions(AppFormElement element) {
    	String codeTypeCode = element.getCodeTypeCode();
    	BusinessCode bc = XarchListener.getBean(BusinessCodeService.class).getByCodeTypeCode(codeTypeCode);
    	String className = (null != bc ? bc.getClassName() : null);
    	if (StringUtil.isEmpty(className)) {
    		log.warn("下拉列表（" + element.getColumnName() + "）未配置，请检查！");
    		return StringUtil.EMPTY;
    	}
    	StringBuilder sb = new StringBuilder();
    	try {
			CodeApplication application = (CodeApplication) Class.forName(className).newInstance();
			Object gcfg = application.getCodeGrid(codeTypeCode);
			if (null != gcfg) {
				Map<String, Object> gMap = (Map<String, Object>)gcfg;
				if (gMap.containsKey("url")) {
					StringBuilder url = new StringBuilder(gMap.get("url").toString());
					if (url.indexOf("?") > 0) url.append("&");
					else url.append("?");
					url.append("P_tableId=").append(getTableId()).append("&P_menuId=").append(getMenuId()).append("&P_pageType=form");
					gMap.put("url", url.toString());
				}
				sb.append(JsonUtil.bean2json(gcfg));
			}
		} catch (Exception e) {
			log.error("获取下拉列表配置信息出错（" + element.getColumnName() + "）！");
		}
    	if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1).deleteCharAt(0);
    	//System.out.println(sb);
    	return sb.toString().replaceAll("\"", "\\&quot;");
    }
    
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public int getColspan() {
        return colspan;
    }
    public void setColspan(int colspan) {
        this.colspan = colspan;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTimestamp() {
    	if (StringUtil.isEmpty(timestamp)) {
    		timestamp = String.valueOf(System.currentTimeMillis());
    	}
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getMasterTableId() {
        return masterTableId;
    }
    public void setMasterTableId(String masterTableId) {
        this.masterTableId = masterTableId;
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
    public Map<String, String> getDefaultData() {
        return defaultData;
    }
    public void setDefaultData(Map<String, String> defaultData) {
        this.defaultData = defaultData;
    }
    public Map<String, String> getFlowDefaultData() {
		return flowDefaultData;
	}
	public void setFlowDefaultData(Map<String, String> flowDefaultData) {
		this.flowDefaultData = flowDefaultData;
	}
	public List<String> getKeptItems() {
        return keptItems;
    }
    public void setKeptItems(List<String> keptItems) {
        this.keptItems = keptItems;
    }
    public List<String> getIncreaseItems() {
        return increaseItems;
    }
    public void setIncreaseItems(List<String> increaseItems) {
        this.increaseItems = increaseItems;
    }
    public List<String> getInheritItems() {
        return inheritItems;
    }
    public void setInheritItems(List<String> inheritItems) {
        this.inheritItems = inheritItems;
    }
	public boolean isFirstActivity() {
		return firstActivity;
	}
	public void setFirstActivity(boolean firstActivity) {
		this.firstActivity = firstActivity;
	}
	public Map<String, String> getTbarPos() {
		Map<String, String> tbarPos = new HashMap<String, String>();
		tbarPos.put("nested", SystemParameterUtil.getInstance().getSystemParamValue("嵌入式表单工具条默认显示位置"));
		tbarPos.put("popup", SystemParameterUtil.getInstance().getSystemParamValue("弹出式表单工具条默认显示位置"));
		return tbarPos;
	}
	public Map<String, List<String>> getRelationMap() {
		return relationMap;
	}
	public void setRelationMap(Map<String, List<String>> relationMap) {
		this.relationMap = relationMap;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
}

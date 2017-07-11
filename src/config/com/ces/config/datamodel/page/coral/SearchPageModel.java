package com.ces.config.datamodel.page.coral;

import com.ces.config.application.CodeApplication;
import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.config.dhtmlx.dao.component.ComponentReserveZoneDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDao;
import com.ces.config.dhtmlx.dao.construct.ConstructDetailDao;
import com.ces.config.dhtmlx.entity.appmanage.AppSearch;
import com.ces.config.dhtmlx.entity.appmanage.AppSearchPanel;
import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.service.appmanage.AppSearchPanelService;
import com.ces.config.dhtmlx.service.appmanage.AppSearchService;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.utils.*;
import com.ces.xarch.core.web.listener.XarchListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>描述: 根据配置信息生成检索区（coral4.0版）</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-16 上午10:39:53
 *
 */
public class SearchPageModel extends DefaultPageModel {
	

	private static Log log = LogFactory.getLog(SearchPageModel.class);
    
    public static final String P_TIMESTAMP = "P_timestamp";
    // 查询区显示方式： 0-弹出式，1-嵌入式
    public static final String P_TYPE = "P_type";
    
    private Integer colspan;
    private String timestamp;
    private String type; 
    
    public void init() {
        setData(cast2html());
    }
    /**
     * qiucs 2014-7-16 
     * <p>描述: 将配置信息转换为coral4.0表单</p>
     * @return String    返回类型   
     * @throws
     */
    public String cast2html() {
        StringBuffer html = new StringBuffer();
        // current user id
        String userId = CommonUtil.getUser().getId();
        String tableId= getTableId();
        String componentVersionId = getComponentVersionId();
        String menuId = getMenuId();
        if (StringUtil.isNotEmpty(getWorkflowId())) {
        	// 工作流应用定义规则
        	tableId = WorkflowUtil.getViewId(getWorkflowId());
        	userId = CommonUtil.SUPER_ADMIN_ID;
        	componentVersionId = getBox();
        	menuId = XarchListener.getBean(WorkflowVersionService.class).getRunningVersionId(getWorkflowId());
        }
        // search panel config
        AppSearchPanel panel = XarchListener.getBean(AppSearchPanelService.class).findDefineEntity(tableId, componentVersionId, menuId, userId);
        if (null == panel) return "";
        
        boolean isNested = "1".equals(getType());
        // 打开方式
        // search filter items
        List<AppSearch> items = XarchListener.getBean(AppSearchService.class).findDefineList(tableId, componentVersionId, menuId, userId);
        
        setColspan(panel.getColspan());
        
        html.append("<form class=\"coralui-form\" data-options=\"\">");
        
        html.append("<div class=\"" + (isNested ? "" : "app-search-form") + "\"><div class=\"fillwidth colspan" + panel.getColspan() + " clearfix\"");
        if (isNested) {
        	html.append(">");
        } else {
        	html.append(" style=\"width: " + (280 * getColspan() + 32) + "px;\">");
        }
        StringBuffer inputHTML = null;
        for (int i = 0; i < items.size(); i++) {
            AppSearch item = items.get(i);
            inputHTML = new StringBuffer();
            inputHTML.append("<div class=\"app-inputdiv" + (12/getColspan()) + "\">");
            
            if (AppDefineUtil.Operator.Key.BT.equals(item.getFilterType())) {
                item.setFilterType(AppDefineUtil.Operator.Key.GTE);
                item.setColumnId("-1");
                inputHTML.append(cast2input(item, false));
                if (!isNested) {
                	inputHTML.append("</div><div class=\"app-inputdiv" + (12/getColspan()) + "\">");
                }
                item = (AppSearch)item.clone();
                item.setShowName("~");
                item.setFilterType(AppDefineUtil.Operator.Key.LTE);
                inputHTML.append(cast2input(item, true));
            } else {
            	inputHTML.append(cast2input(item, false));
            }
            inputHTML.append("</div>");
            html.append(inputHTML);
        }
        // 如果是嵌入式，则加入查询按钮
        /*if ("1".equals(panel.getType())) {
            html.append(buttonHTML(mod, panel.getColspan()));
        }*/
        
        html.append("</div></div>");
        
        html.append("</form>");
        
        return String.valueOf(html);
    }
    
    public List<AppSearch> assembleElements(List<Object[]> items) {
        List<AppSearch> list = new ArrayList<AppSearch>();
        for (int i = 0; i < items.size(); i++) {
            //t_cd.id, t_cd.show_name, t_cd.column_name,t_as.filter_type, t_cd.data_type, t_cd.code_type_code
            Object[] obj = items.get(i);
            AppSearch item = new AppSearch();
            
            item.setId(String.valueOf(obj[0]));
            item.setShowName(String.valueOf(obj[1]));
            item.setColumnName(String.valueOf(obj[2]));
            item.setFilterType(StringUtil.null2empty(obj[3]));
            item.setDataType(StringUtil.null2empty(obj[4]));
            item.setCodeTypeCode(StringUtil.null2empty(obj[5]));
            
            list.add(item);
            
            //
            if (AppDefineUtil.Operator.Key.BT.equals(item.getFilterType())) {
                item.setFilterType(AppDefineUtil.Operator.Key.GTE);
                item = (AppSearch)item.clone();
                item.setShowName("~");
                item.setFilterType(AppDefineUtil.Operator.Key.LTE);
                list.add(item);
            }
        }
        return list;
    }
    /**
     * qiucs 2014-7-16 
     * <p>描述: 配置信息转换为coral4.0表单元素</p>
     * @param  element
     * @param  isBorder
     * @return String    返回类型   
     * @throws
     */
    protected String cast2input(AppSearch element, boolean isBorder) {
        StringBuffer inputHTML = new StringBuffer();
        boolean isBetween = "~".equals(element.getShowName());
        
        // start form element div
        // label
        inputHTML.append("<label class=\"app-input-label" + (isBetween ? (("1".equals(getType()) ? " pct-fixed\"" : "\"") + " style=\"text-align:center;\"") : "\"") + ">")
		       	.append(element.getShowName()).append((isBetween ? "" : "："))
		       	.append("</label>");
        // element
        
        inputHTML.append(getStartTag(element));

        inputHTML.append(" id=\"").append(getElementId(element))
                 //.append("\" name=\"").append(element.getColumnName())
                 .append("\" data-options=\"").append(getDataOptions(element)).append("\"");
            
        inputHTML.append(getEndTag(element));
        // end form element div
            
        System.out.println(inputHTML);
        return String.valueOf(inputHTML);
    }
    /**
     * qiucs 2014-7-17 
     * <p>描述: 检索区按钮（查询与重置）</p>
     * @param  mod
     * @param  colspan
     * @return String    返回类型   
     * @throws
    private String buttonHTML(int mod, Short colspan) {
        StringBuffer inputHTML = new StringBuffer();
        inputHTML.append("<div class=\"app-inputdiv appform-colspan-")
                 .append((0 == mod) ? colspan : 1)
                 .append("\" style=\"text-align:right;\">");
        inputHTML.append("<button type=\"button\" name=\"rs_" + getTableId() + "\" class=\"coralui-button\" style=\"margin-right:10px;\" data-options=\"label:&quot;重置&quot;\"></button>")
                 .append("<button type=\"button\" name=\"sh_" + getTableId() + "\" class=\"coralui-button\" style=\"margin-left:10px;\" data-options=\"label:&quot;查询&quot;\"></button>");
        inputHTML.append("</div>");
        return String.valueOf(inputHTML);
    }*/
    /**
     * qiucs 2014-7-16 
     * <p>描述: 开始标签</p>
     * @param  element
     * @return String    返回类型   
     * @throws
     */
    private String getStartTag(AppSearch element) {
        StringBuffer startTag = new StringBuffer();
        
        if (isTextbox(element)) {
            startTag.append("<input class=\"coralui-textbox\" type=\"text\"");
        } else if (isCombobox(element)) {
            startTag.append("<input class=\"coralui-combobox\"");
        } else if (isCombotree(element)) {
            startTag.append("<input class=\"coralui-combotree\"");
        } else if (isCombogrid(element)) {
            startTag.append("<input class=\"coralui-combogrid\"");
        } else if (isCalendar(element)) {
            startTag.append("<input class=\"coralui-datepicker\" type=\"text\"");
        } else if (isRadio(element)) {
        	startTag.append("<input class=\"coralui-radiolist\"");
        } else if (isCheckbox(element)) {
        	startTag.append("<input class=\"coralui-checkboxlist\"");
        }
        
        return String.valueOf(startTag);
    }
    /**
     * qiucs 2014-7-16 
     * <p>描述: 结束标签</p>
     * @param  element
     * @return String    返回类型   
     */
    private String getEndTag(AppSearch element) {
        StringBuffer endTag = new StringBuffer();
        
        endTag.append("/>");
        
        return String.valueOf(endTag);
    }
    /**
     * qiucs 2014-7-16 
     * <p>描述: 标签中的data-options属性值</p>
     * @param  element
     * @return String    返回类型   
     * @throws
     */
    private String getDataOptions(AppSearch element) {
        StringBuffer dataOptions = new StringBuffer();
        // name
        dataOptions.append("name:&quot;").append(getColumnName(element)).append("&quot;");
        if ("-1".equals(element.getColumnId())) {
        	if ("1".equals(getType())) {
        		dataOptions.append(",componentCls:&quot;pct50-label&quot;");
        	}
        	if ("~".equals(element.getShowName())) {
            	dataOptions.append(",startDateId:&quot;").append(getElementId(element).replaceFirst(element.getFilterType(), AppDefineUtil.Operator.Key.GTE)).append("&quot;");
        	}
        }
        // validate
        if (isNumberbox(element)) {
        	if (isFloatingLabel(element)) { 
            	dataOptions.append(",").append("pattern:&quot;//^(-?[0-9]+)(\\.[0-9]{0," + StringUtil.null2zero(element.getPrecision()) + "})?$//&quot;");
            	dataOptions.append(",").append("errMsg:&quot;不符合浮点型格式或精度过大&quot;");
			} else if (isFloating(element)) {
				dataOptions.append(",").append("pattern:&quot;//^(-?[0-9]+)(\\.[0-9]{0," + StringUtil.null2zero(element.getDataTypeExtend()) + "})?$//&quot;");
				dataOptions.append(",").append("errMsg:&quot;不符合浮点型格式或精度过大&quot;");
			} else {
				dataOptions.append(",").append("validType:&quot;number&quot;");
			}
        }
        // combobox options
        if (isCombobox(element) && StringUtil.isNotEmpty(element.getCodeTypeCode())) {
            dataOptions.append(",").append("url:&quot;")
                       .append("{action}!page.json?E_frame_name=coral&E_model_name=code&id=").append(element.getCodeTypeCode())
                       .append("&P_menuId=").append(getMenuId())
                       .append("&P_componentVersionId=").append(getComponentVersionId())
                       .append("&quot;");
        } else if (isCombotree(element)) {
        	dataOptions.append(",popupDialog:true")
                       .append(",beforeClick:&quot;CFG_combotreeControl&quot;")
		               .append(",url:&quot;")
		               .append("{action}!page.json?E_frame_name=coral&E_model_name=code&P_COMBO_TYPE=combotree&id=")
		               .append(element.getCodeTypeCode())
		               .append("&P_menuId=").append(getMenuId())
		               .append("&P_componentVersionId=").append(getComponentVersionId())
		               //.append(",data:&quot;")
		               //.append(code2tree(element.getCodeTypeCode()))
		               .append("&quot;");
        } else if (isCheckbox(element) || isRadio(element)) {
        	dataOptions.append(",repeatLayout:&quot;flow&quot;")
			           .append(",data:&quot;")
			           .append(code2data(element.getCodeTypeCode()))
			           .append("&quot;");
		} else if (isCalendar(element)) {
			dataOptions.append(",showOn:&quot;button&quot;");
			dataOptions.append(",dateFormat:&quot;" + getDateFormat(element) + "&quot;");
			dataOptions.append(",srcDateFormat:&quot;" + getSrcDateFormat(element) + "&quot;");
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
                    		dataOptions
                            .append(",")
                            .append("buttons:[{'outerRight':[{id:'"
                                    + element.getColumnName()
                                    + "_button',label:'构件按钮',icons:'icon-checkmark-circle',text:false,click:function(e, data) {MT_SEARCH_click('{csearchDivId}', '"
                                    + reserveZoneName + "', '" + element.getShowName() + "选择', 2)}}]}]");
						}
                    }
                }
		} else if (isTextboxLabel(element) || isIntegerLabel(element)) {
        		dataOptions
        		.append(",")
        		.append("buttons:[{'innerRight':[{label:'" + element.getDataTypeExtend() + "'}]}]");
		} else if (isFloatingLabel(element)) {
				dataOptions
	    		.append(",")
	    		.append("buttons:[{'innerRight':[{label:'" + element.getLabel() + "'}]}]");
		}
        
        return String.valueOf(dataOptions).replace("\"", "&quot;");
    }
    
    /**
     * qiucs 2015-1-23 上午11:06:17
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
     * qiucs 2015-3-23 下午2:31:26
     * <p>描述: 检索项NAME </p>
     * @return String
     */
    private String getColumnName(AppSearch element) {
        return AppDefineUtil.getItemName(element.getColumnName(), element.getFilterType(), true);
    }
    
    /**
     * qiucs 2015-3-23 下午2:30:29
     * <p>描述: 下拉列表UI </p>
     * @return String
     */
    private String combogridOptions(AppSearch element) {
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
					url.append("P_tableId=").append(getTableId()).append("&P_menuId=").append(getMenuId()).append("&P_pageType=grid");
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
    
    private boolean isTextbox(AppSearch element) {
        return StringUtil.isEmpty(element.getInputType()) ||
        		FormUtil.CoralInputType.TEXTBOX.equals(element.getInputType()) ||
        		FormUtil.CoralInputType.TEXTAREA.equals(element.getInputType());
    }
    
    private boolean isTextboxLabel(AppSearch element) {
    	return FormUtil.CoralInputOption.TEXTBOXLABEL.equals(element.getInputOption());
    }
    
    private boolean isNumberbox(AppSearch element) {
        return ConstantVar.DataType.NUMBER.equals(element.getDataType());
    }
    
    private boolean isIntegerLabel(AppSearch element) {
    	return FormUtil.CoralInputOption.INTEGERLABEL.equals(element.getInputOption());
    }
    
    private boolean isFloating(AppSearch element) {
    	return FormUtil.CoralInputOption.FLOATING.equals(element.getInputOption());
    }
    
    private boolean isFloatingLabel(AppSearch element) {
    	return FormUtil.CoralInputOption.FLOATINGLABEL.equals(element.getInputOption());
    }
    
    private boolean isCalendar(AppSearch element) {
        return FormUtil.CoralInputType.DATEPICKER.equals(element.getInputType());
    }
    
    private boolean isCombobox(AppSearch element) {
        return FormUtil.CoralInputType.COMBOBOX.equals(element.getInputType());
    }
    
    private boolean isCombogrid(AppSearch element) {
        return FormUtil.CoralInputType.COMBOGRID.equals(element.getInputType());
    }
    
    private boolean isCombotree(AppSearch element) {
        return FormUtil.CoralInputType.COMBOTREE.equals(element.getInputType());
    }
    
    private boolean isRadio(AppSearch element) {
        return FormUtil.CoralInputType.RADIO.equals(element.getInputType());
    }
    
    private boolean isCheckbox(AppSearch element) {
        return FormUtil.CoralInputType.CHECKBOX.equals(element.getInputType());
    }
    
    private boolean isTextboxButton(AppSearch element) {
        return FormUtil.CoralInputOption.TEXTBOXBUTTON.equals(element.getInputOption());
    }
    
    private String getElementId(AppSearch elem) {
        return getColumnName(elem).concat("_").concat(getTimestamp());
    }
    
    private String getDateFormat (AppSearch element) {
    	if (StringUtil.isEmpty(element.getInputOption())) {
			return "yyyy-MM-dd HH:mm:ss";
		}
    	return element.getInputOption();
    }
    
    private String getSrcDateFormat (AppSearch element) {
    	if (StringUtil.isEmpty(element.getDataTypeExtend())) {
			return "yyyy-MM-dd HH:mm:ss";
		}
    	return element.getDataTypeExtend();
    }

    public Integer getColspan() {
        return colspan;
    }
    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

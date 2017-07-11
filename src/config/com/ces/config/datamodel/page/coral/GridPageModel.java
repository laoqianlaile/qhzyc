package com.ces.config.datamodel.page.coral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.config.dhtmlx.entity.appmanage.AppColumn;
import com.ces.config.dhtmlx.entity.appmanage.AppGrid;
import com.ces.config.dhtmlx.entity.appmanage.AppSort;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.service.appmanage.AppColumnService;
import com.ces.config.dhtmlx.service.appmanage.AppGridService;
import com.ces.config.dhtmlx.service.appmanage.AppSortService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FormUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.xarch.core.web.listener.XarchListener;

public class GridPageModel extends DefaultPageModel {
    
    private static Log log = LogFactory.getLog(GridPageModel.class);
    
    private List<String> colNames = new ArrayList<String>();

    private List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();

    //private List<String> widths = new ArrayList<String>();

    private List<String> types = new ArrayList<String>();

    private List<String> columnTypes = new ArrayList<String>();

    //private List<String> aligns = new ArrayList<String>();

    private List<String> columns = new ArrayList<String>();

    private List<String> orders = new ArrayList<String>();

    private List<String> datatypes = new ArrayList<String>();

    private List<String> codetypes = new ArrayList<String>();

    private List<String> columnIds = new ArrayList<String>();

    private List<String> cardColumns = new ArrayList<String>(); // 缩略图信息字段

    private List<Object> urls = new ArrayList<Object>();
    
    private int dblclick;
    
    private boolean hasRowNumber;
    
    private String keyColumn;
    
    private String linkContent;
    
    private int linkIndex = -1;
    
    private boolean pageable;
    
    private boolean adaptive;
    
    private int searchType = 0;
    
    private String pageSize = "20";
    
    private boolean cellEdit = false;
    
    private boolean editableRows = false;
    
    private int clicksToEdit;
    
    private int headerSetting = 0;
    
    private boolean multiselect = false;
    
    private boolean singleselect = false;

    public void init() {
        try {
            String userId = CommonUtil.getUser().getId();
            String tableId= getTableId();
            String componentVersionId = getComponentVersionId();
            String menuId = getMenuId();
            if (StringUtil.isNotEmpty(getWorkflowId())) {
                // 工作流应用定义规则
                tableId = WorkflowUtil.getViewId(getWorkflowId());
                setTableId(tableId);
                userId = CommonUtil.SUPER_ADMIN_ID;
                componentVersionId = getBox();
                menuId = XarchListener.getBean(WorkflowVersionService.class).getRunningVersionId(getWorkflowId());
            }
            
            List<AppColumn> list = XarchListener.getBean(AppColumnService.class)
                    .findAllDefineList(tableId, componentVersionId, menuId, userId);  
            //
            AppGrid appGrid = XarchListener.getBean(AppGridService.class).findDefineEntity(tableId, componentVersionId, menuId, userId);
            if (null != appGrid) {
                setDblclick(appGrid.getDblclick());
                setHasRowNumber((1 == appGrid.getHasRowNumber()));
                setPageable(1 == appGrid.getPageable());
                setAdaptive(1 == appGrid.getAdaptive());
                setSearchType(appGrid.getSearchType());
                setHeaderSetting(appGrid.getHeaderSetting());
                String pageSize = SystemParameterUtil.getInstance().getSystemParamValue("列表每页行数");
                setMultiselect(2 == appGrid.getSelectModel()); // 多选
                setSingleselect(1 == appGrid.getSelectModel());// 单选
                if (StringUtil.isNotEmpty(pageSize)) {
					setPageSize(pageSize);
				}
            }
            // 是否配置了超链接
            boolean isAssembleOp = isAssembleOp();
            
            Map<String, Object> url = null;
            
            for (int i = 0; i < list.size(); i++) {
                AppColumn entity = list.get(i);
                if ("3".equals(entity.getColumnType())) {
                    if (isAssembleOp) {
                        entity.setColumnName("GRID_LINK_RESERVE_ZONE");
                        setLinkIndex(i);
                    } else {
                        continue;
                    }
                }
                String columnName = columnHandle(entity);
                colNames.add(entity.getShowName());
                colModel.add(cast2colModel(entity, appGrid)); 
                
                //widths.add(StringUtil.isEmpty(obj[2]) ? "100" : StringUtil.null2zero(obj[2]));
                //aligns.add(StringUtil.null2empty(obj[3]));
                types.add(entity.getType());
                columnTypes.add(entity.getColumnType());
                columnIds.add(StringUtil.null2empty(entity.getColumnId()));
                columns.add(columnHandle(entity));
                datatypes.add(entity.getDataType());
                codetypes.add(entity.getCodeTypeCode());
                if (StringUtil.isNotEmpty(entity.getUrl())) {
                	url = new HashMap<String, Object>();
                	url.put("idx", i);
                	url.put("content", entity.getUrl());
                	urls.add(url);
                }
                //urls.add(URLEncoder.encode(StringUtil.null2empty(entity.getUrl()).replace(",", "|"), "utf-8"));
                if (isCardColumn(entity.getType())) {
                    cardColumns.add(columnName.toLowerCase());
                }
            }
            if (isAssembleOp) {
            	for (int i = 0; i < list.size(); i++) {
                    AppColumn entity = list.get(i);
                    if ("3".equals(entity.getColumnType())) {
    		        	if (appGrid !=null && appGrid.getOpeColPosition() == 0) {
    		        		colModel.get(i+1).put("resizable", false);
    					} else {
    						colModel.get(i-1).put("resizable", false);
    					}
    		        	break;
                    }
    			}
			}
            // 加入工作流隐藏值
            proccessCoflow(colModel, colNames);
            // 加入ID
            proccessID(colModel, colNames);
            
            List<AppSort> apps = XarchListener.getBean(AppSortService.class).findDefineList(tableId, componentVersionId, menuId, userId);
            for (AppSort app : apps) {
                orders.add(app.getColumnName());
                orders.add(app.getSortType());
            }
        } catch (Exception e) {
            log.error("获取列表配置出错", e);
        }
    }
    // 是否配置了超链接（在“列表列定义”中操作列是否在“构件组装”列表超链接是否配置过）
    private boolean isAssembleOp() {
        List<ConstructDetail> constructDetailList = null;
        ComponentVersion componentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(getComponentVersionId());
        if (null == componentVersion || !ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            return false;
        }
        Construct construct = XarchListener.getBean(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
        String baseComponentVersionId = construct.getBaseComponentVersionId();
        ComponentVersion baseComponentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(baseComponentVersionId);
        if (null == baseComponentVersion) return false;
        
        String reserveZoneName = "";
        
        PhysicalTableDefine physicalTableDefine = XarchListener.getBean(PhysicalTableDefineService.class).getByID(getTableId());
        if (ConstantVar.Component.Type.LOGIC_TABLE.equals(baseComponentVersion.getComponent().getType())) {
            
            if (StringUtil.isEmpty(physicalTableDefine.getLogicTableCode())) return false;
            
            String commonReserveZoneName = AppDefineUtil.getCommonZoneName(physicalTableDefine.getLogicTableCode(), AppDefineUtil.L_GRID, "LINK", "0");
            ComponentReserveZone commonReserveZone = XarchListener.getBean(ComponentReserveZoneService.class).getCommonReserveZoneByName(commonReserveZoneName);
            constructDetailList = XarchListener.getBean(ConstructDetailService.class).getByConstructIdAndReserveZoneId(construct.getId(), commonReserveZone.getId());
            reserveZoneName = commonReserveZone.getName();
        } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(baseComponentVersion.getComponent().getType())) {
            List<ComponentReserveZone> reserveZoneList = XarchListener.getBean(ComponentReserveZoneService.class).getByComponentVersionId(baseComponentVersionId);
            
            if (CollectionUtils.isEmpty(reserveZoneList)) return false;
            
            for (ComponentReserveZone reserveZone : reserveZoneList) {
                if (ConstantVar.Component.ReserveZoneType.GRID_LINK.equals(reserveZone.getType()) && reserveZone.getName().indexOf(getTableId()) != -1) {
                    reserveZoneName = reserveZone.getName();
                    constructDetailList = XarchListener.getBean(ConstructDetailService.class).getByConstructIdAndReserveZoneId(construct.getId(), reserveZone.getId());
                    break;
                }
            }
        } else if (ConstantVar.Component.Type.NO_TABLE.equals(baseComponentVersion.getComponent().getType())) {
            List<ComponentReserveZone> reserveZoneList = XarchListener.getBean(ComponentReserveZoneService.class).getByComponentVersionId(baseComponentVersionId);
            
            if (CollectionUtils.isEmpty(reserveZoneList)) return false;
            
            for (ComponentReserveZone reserveZone : reserveZoneList) {
                if (ConstantVar.Component.ReserveZoneType.GRID_LINK.equals(reserveZone.getType())) {
                    reserveZoneName = reserveZone.getName();
                    constructDetailList = XarchListener.getBean(ConstructDetailService.class).getByConstructIdAndReserveZoneId(construct.getId(), reserveZone.getId());
                    break;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            setLinkContent(toLinkContent(reserveZoneName, constructDetailList));
            return true;
        }
        return false;
    }
    
    /**
     * qiucs 2015-3-16 下午2:52:20
     * <p>描述: 获取列表操作列 组装的构件</p>
     * @return String
     */
    private String toLinkContent(String reserveZoneName, List<ConstructDetail> list) {
        String columnName = "";
        String linkType = SystemParameterUtil.getInstance().getSystemParamValue("列表操作列显示形式");
        int linkCount = Integer.valueOf(StringUtil.null2zero(SystemParameterUtil.getInstance().getSystemParamValue("列表操作列显示个数")));
        String moreButton = "";
        ConstructDetail constructDetail = null;
        for (int i = 0; i < list.size(); i++) {
            constructDetail = list.get(i);
            if ("1".equals(linkType)) {
                if (linkCount == 0 || (i + 1 < linkCount) || ((i + 1 == linkCount) && list.size() == linkCount)) {
                    columnName += "<button class=\"coral-button coral-component coral-state-default coral-corner-all coral-button-text-only\" title=\""
                            + constructDetail.getButtonDisplayName() + "\" onClick=\"MT_GRID_LINK_ZONE_click('{1}','" + reserveZoneName + "','"
                            + constructDetail.getButtonCode() + "','{2}')\"><span class=\"coral-button-text\">" + constructDetail.getButtonDisplayName()
                            + "</span></button>";
                } else {
                    moreButton += "<button class=\"coral-button coral-component coral-state-default coral-corner-all coral-button-text-only\" title=\""
                            + constructDetail.getButtonDisplayName() + "\" onClick=\"MT_GRID_LINK_ZONE_click('{1}','" + reserveZoneName + "','"
                            + constructDetail.getButtonCode() + "','{2}')\"><span class=\"coral-button-text\">" + constructDetail.getButtonDisplayName()
                            + "</span></button>";
                }
            } else if ("2".equals(linkType)) {
                String buttonStyle = constructDetail.getButtonCls();
                if (StringUtil.isEmpty(buttonStyle)) {
                    buttonStyle = "edit_tb";
                }
                if (linkCount == 0 || (i + 1 < linkCount) || ((i + 1 == linkCount) && list.size() == linkCount)) {
                    columnName += "<button class=\"" + buttonStyle + "\" title=\"" + constructDetail.getButtonDisplayName()
                            + "\" onClick=\"MT_GRID_LINK_ZONE_click('{1}','" + reserveZoneName + "','" + constructDetail.getButtonCode()
                            + "','{2}')\"></button>";
                } else {
                    moreButton += "<div class=\"" + buttonStyle + "\" title=\"" + constructDetail.getButtonDisplayName()
                            + "\" onClick=\"MT_GRID_LINK_ZONE_click('{1}','" + reserveZoneName + "','" + constructDetail.getButtonCode() + "','{2}')\"></div>";
                }
            } else {
                columnName += "<a class=\"grid_link_reserve_zone\" title=\"" + constructDetail.getButtonDisplayName()
                        + "\" onclick=\"javascript:MT_GRID_LINK_ZONE_click('{1}','" + reserveZoneName + "','" + constructDetail.getButtonCode() + "','{2}')\" href=\"javascript:void(0);\">"
                        + constructDetail.getButtonDisplayName() + "</a> ";
            }
        }
        if ("1".equals(linkType) && !"".equals(moreButton)) {
            columnName += moreButton;
        }
        if ("2".equals(linkType) && !"".equals(moreButton)) {
            String more = "<div class='grid_link_down_div' onmouseover='$(this).show()' onmouseout='$(this).hide()'>";
            more += moreButton;
            more += "</div>";
            columnName += "<button class='drop_tb' onClick='CFG_clickSplitDownImgButton(event, this)' onmouseout='CFG_hideSplitDownImgButton(this)'></button>";
            columnName += more;
        }

        return columnName;
    }
    
    private boolean isCardColumn(String type) {
        if (AppColumn.Type.CARD.equals(type) || AppColumn.Type.TEXT_CARD.equals(type)) {
            return true;
        }
        return false;
    }

    private String columnHandle(AppColumn entity) {
        if (AppColumn.SPECIAL_VALUE.equals(entity.getColumnId())
                || !"0".equals(entity.getColumnType())) {
            if ("2".equals(entity.getColumnType()) || "3".equals(entity.getColumnType())) { // 固定值
                return "'" + entity.getColumnName() + "' AS " + entity.getColumnAlias();
            }
            // 自定义SQL语句
            return "(" + entity.getColumnName() + ") AS " + entity.getColumnAlias();
        }
        return entity.getColumnName();
    }
    
    /**
     * qiucs 2014-7-15 
     * <p>描述: </p>
     * @param  obj
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> cast2colModel(AppColumn entity, AppGrid appGrid) {
        Map<String, Object> col = new HashMap<String, Object>();
        
        col.put("name", toGridColumn(entity)); // 
        col.put("width", entity.getWidth());
        col.put("align", entity.getAlign());
        if ("3".equals(entity.getColumnType())) {
            col.put("frozen", true);
            col.put("resizable", false);
            col.put("sortable", false);
            col.put("columnSortable", true);
            col.put("fixed", true);
        } else if (AppColumn.Type.HIDDEN.equals(entity.getType())) {
        	col.put("hidden", Boolean.TRUE);
        } else if (AppColumn.Type.EDITABLE.equals(entity.getType())) {
        	String editType = getEditType(entity);
        	setCellEdit(appGrid.getEditModel() == 2);
            setEditableRows(appGrid.getEditModel() == 1);
            setClicksToEdit(appGrid.getEditModel());
        	if (isFormatEdit(appGrid)) {
        		setCellEdit(Boolean.TRUE);
	        	col.put("formatter", editType);
	        	col.put("formatoptions", getEditOptions(entity));
	        	if ("combobox".equals(editType)) {
	        		col.put("revertCode", Boolean.TRUE);
	        	}
			} else {
				col.put("editable", Boolean.TRUE);
	        	col.put("edittype", editType);
	        	col.put("editoptions", getEditOptions(entity));
	        	if ("combobox".equals(editType)) {
	        		col.put("revertCode", Boolean.TRUE);
	        	}
			}
        } else if (!AppColumn.Type.VALUE.equals(entity.getType()) 
        		&& StringUtil.isNotEmpty(entity.getInputType()) 
        		&& !FormUtil.CoralInputType.TEXTBOX.equals(entity.getInputType())
                && !FormUtil.CoralInputType.DATEPICKER.equals(entity.getInputType())
                && !FormUtil.CoralInputType.TEXTAREA.equals(entity.getInputType())
                && StringUtil.isNotEmpty(entity.getCodeTypeCode())) {
            col.put("formatter", "convertCode");
            col.put("formatoptions", cast2data(entity.getCodeTypeCode()));
            col.put("revertCode", Boolean.TRUE);
        }
        return col;
    }
    
    private String getEditType(AppColumn entity) {
    	
    	if (FormUtil.CoralInputType.DATEPICKER.equals(entity.getInputType())) {
    		return "datepicker";
    	} else if (FormUtil.CoralInputType.COMBOBOX.equals(entity.getInputType())
    			|| FormUtil.CoralInputType.RADIO.equals(entity.getInputType())
    			|| FormUtil.CoralInputType.CHECKBOX.equals(entity.getInputType())) {
    		return "combobox";
    	}/* else if (FormUtil.CoralInputType.COMBOBOX.equals(entity.getInputType())) {
    		
    	}*/
    	
    	return "text";
    }
    
    
    private Object getEditOptions(AppColumn entity) {
    	Map<String, Object> options = new HashMap<String, Object>();
    	
    	if (FormUtil.CoralInputType.DATEPICKER.equals(entity.getInputType())) {
        	options.put("dateFormat", getSrcDateFormat(entity));
        	options.put("srcDateFormat", getSrcDateFormat(entity));
    	} else if (FormUtil.CoralInputType.COMBOBOX.equals(entity.getInputType())
    			|| FormUtil.CoralInputType.RADIO.equals(entity.getInputType())
    			|| FormUtil.CoralInputType.CHECKBOX.equals(entity.getInputType())) {
    		return (cast2data(entity.getCodeTypeCode()));
    	}
    	
    	return options;
    }

    private String toGridColumn(AppColumn entity) {
        if (AppColumn.SPECIAL_VALUE.equals(entity.getColumnId())
                || !"0".equals(entity.getColumnType())) {// 固定值, 自定义SQL语句
            return StringUtil.null2empty(entity.getColumnAlias())/*.toLowerCase()*/;
        }
        return StringUtil.null2empty(entity.getColumnName())/*.toLowerCase()*/;
    }
    
    private boolean isFormatEdit(AppGrid appGrid) {
    	if (appGrid != null && appGrid.getEditModel() == 0) return true;
    	return false;
    }
    
    /**
     * qiucs 2014-8-12 
     * <p>描述: 工作流特有字段处理 </p>
     * @param  list    设定参数   
     */
    private void proccessID(List<Map<String, Object>> list, List<String> colNames) {
        Map<String, Object> col = new HashMap<String, Object>();
        col.put("name", ConstantVar.ColumnName.ID/*.toLowerCase()*/); // 
        col.put("key", true);
        col.put("hidden", true);
        list.add(col);
        
        colNames.add(ConstantVar.ColumnName.ID);        
        setKeyColumn(ConstantVar.ColumnName.ID);
    }
    
    /**
     * qiucs 2014-8-12 
     * <p>描述: 工作流特有字段处理 </p>
     * @param  list    设定参数   
     */
    private void proccessCoflow(List<Map<String, Object>> list, List<String> colNames) {
        if (StringUtil.isEmpty(getBox())) {
            return;
        }
        Map<String, Object> col = new HashMap<String, Object>();
        col.put("name", WorkflowUtil.Alias.processInstanceId); // 
        col.put("hidden", true);
        list.add(col);
        
        colNames.add(WorkflowUtil.Alias.processInstanceId);
        
        col = new HashMap<String, Object>();
        col.put("name", WorkflowUtil.Alias.workitemId); // 
        col.put("hidden", true);
        list.add(col);
        
        colNames.add(WorkflowUtil.Alias.workitemId);
        
        col = new HashMap<String, Object>();
        col.put("name", WorkflowUtil.Alias.workitemStatusKey); // 
        col.put("hidden", true);
        list.add(col);
        
        colNames.add(WorkflowUtil.Alias.workitemStatusKey);
        
        col = new HashMap<String, Object>();
        col.put("name", WorkflowUtil.Alias.workitemActivityId); // 
        col.put("hidden", true);
        list.add(col);
        
        colNames.add(WorkflowUtil.Alias.workitemActivityId);
    }
    
    private Object cast2data(String codeTypeCode) {
        List<Code> list = CodeUtil.getInstance().getCodeList(codeTypeCode);
        List<Map<String, Object>> 
                   data = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = null;
        if (CollectionUtils.isNotEmpty(list)) {
            Code c = null;
            for (int i = 0, len = list.size(); i < len; i++) {
                c = list.get(i);
                item = new HashMap<String, Object>();
                item.put("value", c.getValue());
                item.put("text", c.getName());
                data.add(item);
            }
        }
        item = new HashMap<String, Object>();
        item.put("data", data);
        return item;
    }
    
    private String getSrcDateFormat (AppColumn element) {
    	if (StringUtil.isEmpty(element.getDataTypeExtend())) {
			return "yyyy-MM-dd HH:mm:ss";
		}
    	return element.getDataTypeExtend();
    }

    public List<String> getColNames() {
        return colNames;
    }

    public void setColNames(List<String> colNames) {
        this.colNames = colNames;
    }

    public List<Map<String, Object>> getColModel() {
        return colModel;
    }

    public void setColModel(List<Map<String, Object>> colModel) {
        this.colModel = colModel;
    }

    /*public List<String> getWidths() {
        return widths;
    }

    public void setWidths(List<String> widths) {
        this.widths = widths;
    }*/

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    /*public List<String> getAligns() {
        return aligns;
    }

    public void setAligns(List<String> aligns) {
        this.aligns = aligns;
    }*/

    public List<String> getColumnTypes() {
        return columnTypes;
    }
    
    public void setColumnTypes(List<String> columnTypes) {
        this.columnTypes = columnTypes;
    }
    
    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public List<String> getDatatypes() {
        return datatypes;
    }

    public void setDatatypes(List<String> datatypes) {
        this.datatypes = datatypes;
    }

    public List<String> getCodetypes() {
        return codetypes;
    }

    public void setCodetypes(List<String> codetypes) {
        this.codetypes = codetypes;
    }

    public List<String> getColumnIds() {
        return columnIds;
    }

    public void setColumnIds(List<String> columnIds) {
        this.columnIds = columnIds;
    }

    public List<String> getCardColumns() {
        return cardColumns;
    }

    public void setCardColumns(List<String> cardColumns) {
        this.cardColumns = cardColumns;
    }

    public List<Object> getUrls() {
        return urls;
    }

    public void setUrls(List<Object> urls) {
        this.urls = urls;
    }

    public int getDblclick() {
        return dblclick;
    }

    public void setDblclick(int dblclick) {
        this.dblclick = dblclick;
    }

    public boolean isHasRowNumber() {
        return hasRowNumber;
    }

    public void setHasRowNumber(boolean hasRowNumber) {
        this.hasRowNumber = hasRowNumber;
    }
    
    public String getKeyColumn() {
        return keyColumn;
    }
    
    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }
    
    public String getLinkContent() {
        return linkContent;
    }
    
    public void setLinkContent(String linkContent) {
        this.linkContent = linkContent;
    }
    
    public int getLinkIndex() {
        return linkIndex;
    }
    
    public void setLinkIndex(int linkIndex) {
        this.linkIndex = linkIndex;
    }
    public boolean isPageable() {
        return pageable;
    }
    public void setPageable(boolean pageable) {
        this.pageable = pageable;
    }
    
    public boolean isAdaptive() {
        return adaptive;
    }
    
    public void setAdaptive(boolean adaptive) {
        this.adaptive = adaptive;
    }
    
	public int getSearchType() {
		return searchType;
	}
	
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public boolean isCellEdit() {
		return cellEdit;
	}
	public void setCellEdit(boolean cellEdit) {
		this.cellEdit = cellEdit;
	}
	public boolean isEditableRows() {
		return editableRows;
	}
	public void setEditableRows(boolean editableRows) {
		this.editableRows = editableRows;
	}
	public int getClicksToEdit() {
		return clicksToEdit;
	}
	public void setClicksToEdit(int clicksToEdit) {
		this.clicksToEdit = clicksToEdit;
	}
	public int getHeaderSetting() {
		return headerSetting;
	}
	public void setHeaderSetting(int headerSetting) {
		this.headerSetting = headerSetting;
	}
	public boolean isMultiselect() {
		return multiselect;
	}
	public void setMultiselect(boolean multiselect) {
		this.multiselect = multiselect;
	}
	public boolean isSingleselect() {
		return singleselect;
	}
	public void setSingleselect(boolean singleselect) {
		this.singleselect = singleselect;
	}
    

}

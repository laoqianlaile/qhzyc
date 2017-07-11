/**
 * 列表初始化
 */
function workflowSetting(that) {
	var layout = that.bLayout;
	layout.detachToolbar();
	var tabs = layout.attachTabbar();
	tabs.setImagePath(IMAGE_PATH);
	// 
	var proTab = tabs.addTab("_protab_", "流程定义");
	var formTab = tabs.addTab("_formtab_", "表单定义");
	var appTab = tabs.addTab("_apptab_", "应用定义");
	var specTab = tabs.addTab("_spectab_", "节点个性化定义");
	// 
	tabs.setTabActive("_protab_");
	//
	workflowProSetting(tabs.cells("_protab_"), that);
	//
	tabs.attachEvent("onSelect", function(id,last_id){
		var layout = tabs.cells(id);
   		if (id == "_protab_") {
   			workflowProSetting(layout, that);//数据源字段设置
	    } else if (id == "_formtab_") {
	    	workflowFormSetting(layout, that);//表关系设置
	    } else if (id == "_apptab_") {
	    	workflowAppSetting(layout, that);
		} else if (id == "_spectab_") {
			workflowSpecSetting(layout, that);
		}
		return true;
   	});
}

/**
 * 流程定义
 * @param formTab
 */
function workflowProSetting(proTab, that) {
	proTab.attachURL(contextPath + "/coflow/view/designer.jsp?versionId=" + that.nodeId);
}

/**
 * 工作流表单定义
 * @param formTab
 */
function workflowFormSetting(formTab, that) {
	initAppFormWin(formTab, "-1"/*componentVersionId*/);
}
/**
 * 工作流应用定义
 * @param formTab
 */
function workflowAppSetting(appTab, that) {
	loadCoflowAppDefine(appTab);
}
/**
 * 节点个性化定义
 * @param formTab
 */
function workflowSpecSetting(specTab, that) {
	
	var layout = specTab.attachLayout("2U");
	var layoutA = layout.cells("a");
	var layoutB = layout.cells("b");
	
	layoutA.hideHeader();
	layoutB.hideHeader();
	
	workflowActivityGrid(layoutA, that);
	
	workflowSpecAccordion(layoutB, that);
}

function workflowActivityGrid(layoutA, that) {
	layoutA.setWidth(180);
	// 
	that.activityId = "-1";
	
	var url = AppActionURI.workflowVersion + "!getActivities.json?id=" + that.nodeId;
	var aGrid = layoutA.attachGrid();
	aGrid.enableDragAndDrop(true);
	var aCfg = {
			format: {
				headers: ["<center>流程节点</center>"],
				cols: ["name"],
				colWidths: ["*"],
				colTypes: ["ro"],
				colAligns: ["left"]
			},
			multselect: false
		};
	initGridWithoutPageable(aGrid, aCfg, url);
	
	aGrid.attachEvent("onRowSelect", function(id, ind) {
		that.activityId = id;
		that.reloadSpec();
	});
}

function workflowSpecAccordion(layout, that) {
	var dhxAccord = layout.attachAccordion();
	
	var accForm = dhxAccord.addItem("_specForm_", "表单设置");
	dhxAccord.addItem("_specBox_", "待办箱按钮设置");
	if ("1" == that.enableAssist) {
		dhxAccord.addItem("_specOpinion_", "辅助意见设置");
	}
	dhxAccord.openItem("_specForm_");
	initSpecForm(accForm, that); 
	dhxAccord.attachEvent("onBeforeActive", function (itemId) {
		var item = dhxAccord.cells(itemId);
		openAcdItem(item, itemId);
		return true;
	});
	
	that.reloadSpec = function () {
		dhxAccord.forEachItem(function(item) {
			console.log("is open: " + item.isOpened());
			if (item.isOpened()) {
				openAcdItem(item, item.getId());
			}
		});
	};
	
	function openAcdItem(item, itemId) {
		if ("_specForm_" == itemId) {
			initSpecForm(item, that); 
		} else if ("_specBox_" == itemId) {
			initSpecBox(item, that); 
		} else if ("_specOpinion_" == itemId) {
			initSpecOpinion(item, that);
		}
	}
}

function initSpecForm(layout, that) {
	var curLayout = layout.attachLayout("2U");
	var aLayout = curLayout.cells("a");
	// 界面字段
	aLayout.hideHeader();
	aLayout.setWidth(320);
	var sbar = aLayout.attachStatusBar();
	var btbar = new dhtmlXToolbarObject(sbar.id);
	btbar.setIconPath(TOOLBAR_IMAGE_PATH);
	btbar.addButton("subLayout$save", 0, "保存", "save.gif");
	btbar.setAlign("right");
	btbar.attachEvent("onClick", function(itemId) {
		if ("subLayout$save" == itemId) {
			saveSetting();
		}
	});
	

	
	var aGrid = aLayout.attachGrid();
	var aCfg = {
			format: {
				headers: ["<center>可用</center>","<center>字段名称</center>","<center>默认值</center>"],
				cols: ["id","showName",""],
				userdata: ["colspan", "columnName", "percent", "dataType", "codeTypeCode", "length", "required", "readonly", "hidden", "inputType", "defaultValue", "kept", "validation", "tooltip", "columnId", "increase", "inherit"],
				colWidths: ["40","*","160"],
				colTypes: ["ch","ro","ed"],
				colAligns: ["center","left","left"]
			},
			multselect: false
		};
	var gurl = AppActionURI.appFormElement + "!defineColumn.json?P_tableId=" + app.tableId + "&P_componentVersionId=-1&P_menuId=" + app.menuId; 
	initGridWithoutPageable(aGrid, aCfg, gurl);
	//
	processDisabledColumns();
	//
	aGrid.attachEvent("onCheckbox", function(rowId, cInd, state) { 
		var name = aGrid.getUserData(rowId, "columnName");
		if ("0" == state) {
			curForm.disableItem(name);
		} else {
			curForm.enableItem(name);
		}
	}); 

	// 界面预览
	var formLayout = curLayout.cells("b");
	formLayout.setText("界面预览");
	var curForm = previewForm ();
	// 保存表单个性化配置
	function saveSetting () {
		if (isEmpty(that.activityId) || "-1" == that.activityId) {
			dhtmlx.message("请选择一个流程节点，再保存！");
			return;
		}
		var rowsValue = "";
		aGrid.forEachRow(function(rowId) {
			var disabled = aGrid.cells(rowId, 0).getValue();
			var defaultValue = aGrid.cells(rowId, 2).getValue();
			if (!("1" == disabled && "" == defaultValue)) {
				var columnId = aGrid.getUserData(rowId, "columnId");
				rowsValue += ";" + columnId + "," + (disabled == "0" ? "1" : "0") + "," + defaultValue;
			} 
		});
		if (rowsValue.length > 0) {
			rowsValue = rowsValue.substring(1);
		}
		var url = AppActionURI.workflowFormSetting + "!save.json";
		var params = "P_workflowVersionId=" + app.menuId + "&P_activityId=" + that.activityId + 
			"&P_rowsValue=" + rowsValue;
		dhtmlxAjax.post(url, params, function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				dhtmlx.message(getMessage("operate_success"));
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
		});
	};
	// 获取表单页面整体设置
	function formMaster () {
		var url = AppActionURI.appForm + "!edit.json?tableId=" + app.tableId + "&componentVersionId=-1&menuId=" + app.menuId;
		var obj = loadJson(url);
		return obj;
	};
	// 预览表单
	function previewForm () {
		var labelWidth = 80;
		var inputWidth = 160;
		var formJson = [{
            type : "settings",
            position : "label-left",
            labelWidth : labelWidth,
            inputWidth : inputWidth,
            labelAlign : "right",
            offsetTop:"2"
        }];
		var formObj = formMaster();
		if (!formObj) return;
		var formcolspan = formObj.colspan;
		var border = formObj.border;
		var blockWidth = (labelWidth + inputWidth) * formcolspan + 40;
		var block = {type: "block", width: blockWidth, list:[]};
		var newcolumn = {type:"newcolumn"};
		var preIdx = 0;
		var curIdx = 0;
		var ttlNum = aGrid.getRowsNum();
		var rowIdx = 0;
		var percentCount = 0;
		var disableItems = [];
		for (var rowIdx = 0;rowIdx < ttlNum; rowIdx++) {
			//["colspan","required","readonly","hidden","textarea","defaultValue","columnName","dataType","length","codeTypeCode"]
			var rowId     = aGrid.getRowId(rowIdx);
			var enabled   = aGrid.cells(rowId, 0).getValue();
    		var label     = aGrid.cells(rowId, 1).getValue();
    		var tooltip   = aGrid.getUserData(rowId, "tooltip");
    		var colspan   = aGrid.getUserData(rowId, "colspan");
    		var required  = aGrid.getUserData(rowId, "required");
    		var readonly  = aGrid.getUserData(rowId, "readonly");
    		var hidden    = aGrid.getUserData(rowId, "hidden");
    		var textarea  = aGrid.getUserData(rowId, "textarea");
    		var value     = aGrid.getUserData(rowId, "defaultValue");//*/
    		var columnId  = aGrid.getUserData(rowId, "columnId");
    		var name      = aGrid.getUserData(rowId, "columnName");
    		var inputType = aGrid.getUserData(rowId, "inputType");
    		var datatype  = aGrid.getUserData(rowId, "dataType");
    		var maxLength = aGrid.getUserData(rowId, "length");
    		var percent   = parseInt(aGrid.getUserData(rowId, "percent"));
    		
    		if ("0" == enabled) {
    			disableItems.push(name);
    		}

    		var type     = getFormItemType(hidden, inputType);
    		var onerow = {};
    		
    		if ("-1" == columnId) {
    			if (block.list.length > 0) {
        			formJson.push(block);
    			}
    			block = {type: "block", width: blockWidth, list:[]};
    			block.list.push({type: "subfield", label: tooltip, width: blockWidth});
    			formJson.push(block);
    			block = {type: "block", width: blockWidth, list:[]};
    			curIdx = preIdx = 0;// reset to 0
    			continue;
    		} else if ("-2" == columnId) {
    			onerow.width = (inputWidth * colspan + labelWidth * (colspan - 1));
        		curIdx += colspan;
        		onerow.type  = "placeholder";
    		} else {
	    		if ("1" != required && (percent == 100 || (percent != 100 && percentCount == 0) )) {
	    			label += "：";
	    		} else {
	    			label = "";
	    		}
	    		if (percent != 100 && percentCount != 0) {
	    			onerow.label = label;
	    		} else {
	    			onerow.label = (("1" == border) ? "<strong>" + label + "</strong>" : label);
	    		}
	    		onerow.name = name;
	    		onerow.value = value;
	    		if ("hidden" != type) {
	    			onerow.required = ("1" == required ? true : false);
	        		onerow.readonly = ("1" == readonly ? true : false);
	        		onerow.maxLength= maxLength;
	        		if ("textarea" == type) {
	        			type = "input";
	        			onerow.rows = 3;
//	        			colspan = formcolspan;
	        		} else if ("combo" == type) {
	        			onerow.options = [{value: "", text: "请选择"}];
	        		} else if ("calendar" == type) {
	        			onerow.dateFormat = "%Y-%m-%d";
	        			onerow.readonly = true;
	        		}
	        		if (percent == 100 || (percent != 100 && percentCount == 0)) {
	        			curIdx += colspan;
	        		}
	        		if (percent != 100) {
	        			onerow.width = (inputWidth * colspan * percent / 100 + labelWidth * (colspan - 1));
	        			percentCount += percent;
	        		} else {
	        			onerow.width = (inputWidth * colspan + labelWidth * (colspan - 1));
	        		}
	        		if (percentCount == 99 || percentCount == 100) {
            			percentCount = 0;
            		}
	    		}    		
	    		onerow.type = type;
    		}
    		if (curIdx > formcolspan) {
    			formJson.push(block);
    			block = {type: "block", width: blockWidth, list:[]};
    			curIdx = colspan; preIdx = 0;// reset to 0
    		}
    		block.list.push(onerow);
//    		if (curIdx > preIdx) {
    			block.list.push(newcolumn);
//    		}
    		preIdx = curIdx;
    		// 
    		if (ttlNum == (1 + rowIdx)) {
    			formJson.push(block);
    		} 
		}
		
		var tempForm = formLayout.attachForm(formJson);
		// 把已配置的置灰
		for (var i = 0; i < disableItems.length; i++) {
			tempForm.disableItem(disableItems[i]);
		}
		return tempForm;
	};
	// 类型转换
	function getFormItemType (hidden, inputType) {
		if (hidden && "1" == hidden) return "hidden";
		if ("textarea" == inputType) return "textarea";
		if ("datepicker" == inputType) return "calendar";
		if ("textbox" == inputType) return "input";
		if ("checkbox" == inputType) return "checkboxlist";
		if ("radio" == inputType) return "radiolist";
		return "combo";
	};
	
	function processDisabledColumns() {
		var url = AppActionURI.workflowFormSetting + "!formSetting.json?P_workflowVersionId=" + app.menuId + "&P_activityId=" + that.activityId;
		var columnArr = loadJson(url);
		aGrid.forEachRow(function(rowId) {
			var dataType = aGrid.getUserData(rowId, "dataType");
			var columnId  = aGrid.getUserData(rowId, "columnId");
			if ("-1" == columnId || "-2" == columnId) {
				aGrid.setCellExcellType(rowId, 2, "ro");
			} else {
				var cellObj = aGrid.cellById(rowId, 2);
				if ("d" == dataType) {
//				aGrid.setDateFormat("%Y-%m-%d")
//				aGrid.setCellExcellType(rowId,2,"dhxCalendarA");
					aGrid.setCellExcellType(rowId, 2, "co");
					var combo = aGrid.getCustomCombo(rowId, 2);
					combo.put("", "");
					combo.put("_CURRENT_DATE_", "当前日期");
				}
				var codeTypeCode = aGrid.getUserData(rowId, "codeTypeCode");
				if (isNotEmpty(codeTypeCode)) {
					aGrid.setCellExcellType(rowId, 2, "co");
					var opts = loadJson(contextPath + "/code/code!combobox.json?codeTypeCode=" + codeTypeCode);
					if ("AUTH_USER" === codeTypeCode) {
						opts.unshift({value: "_CURRENT_USER_", text: "当前人员"});
					} else if ("AUTH_DEPT" === codeTypeCode) {
						opts.unshift({value: "_CURRENT_DEPT_", text: "当前部门"});
					}
					opts.unshift({value: "", text: ""});
					var combo = aGrid.getCustomCombo(rowId, 2);
					for(var i=0; i<opts.length; i++) {
						combo.put(opts[i].value, opts[i].text);
					}
				}
				var columnId = aGrid.getUserData(rowId, "columnId");
				var setting = columnArr[columnId];
				if(null != setting) {
					aGrid.cells(rowId, 0).setValue(setting.disabled == "0"	? "1" : "0");
					aGrid.cells(rowId, 2).setValue(setting.defaultValue);
				}
			}
		});
	}
}

function initSpecBox(layout, that) {
	var tableId = app.viewId; // 工作流应用定义的表ID为对应的视图ID
	var curLayout = layout.attachLayout("2U");
	var aLayout = curLayout.cells("a");
	var bLayout = curLayout.cells("b");
	aLayout.hideHeader();
	bLayout.hideHeader();

	// grid
	initBottomTbar(aLayout, "1");
	var gGrid = initButtonGrid(aLayout, "1");
	// form
	initBottomTbar(bLayout, "0");
	var fGrid = initButtonGrid(bLayout, "0");
	
	// 保存按钮个性设置
	function saveSetting (type) {
		var grid = ("1" == type ? gGrid : fGrid);
		if (isEmpty(that.activityId) || "-1" == that.activityId) {
			dhtmlx.message("请选择一个流程节点，再保存！");
			return;
		}
		var rowsValue = "";
		grid.forEachRow(function(rowId) {
			var enabled = grid.cells(rowId, 0).getValue();
			if ("0" == enabled) {
				var buttonCode = grid.getUserData(rowId, "buttonCode");
				rowsValue += ";" + buttonCode;
			} 
		});
		if (rowsValue.length > 0) {
			rowsValue = rowsValue.substring(1);
		}
		var url = AppActionURI.workflowButtonSetting + "!save.json?P_workflowVersionId=" + app.menuId +
				"&P_activityId=" + that.activityId + "&P_buttonType=" + type + 
				"&P_rowsValue=" + rowsValue;
		dhtmlxAjax.get(url, function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				dhtmlx.message(getMessage("operate_success"));
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
		});
	};
	// 底部工具条初始化
	function initBottomTbar(layout, type) {
		var sbar = layout.attachStatusBar();
		var tbar = new dhtmlXToolbarObject(sbar.id);
		tbar.setIconPath(TOOLBAR_IMAGE_PATH);
		tbar.addButton("save", 0, "保存", "save.gif");
		tbar.setAlign("right");
		tbar.attachEvent("onClick", function(itemId) {
			if ("save" == itemId) {
				saveSetting(type);
			}
		});
		return tbar;
	}
	// 初始化按钮列表
	function initButtonGrid(layout, type) {
		var grid = layout.attachGrid();
		var gcfg = {
				format: {
					headers: ["<center>显示</center>","<center>" + (1 == type ? "列表" : "表单") + "按钮名称</center>","<center>显示名称</center>"],
					cols: ["display","buttonName","showName"],
					userdata : ["buttonCode"],
					colWidths: ["40","160","*"],
					colTypes: ["ch","ro","ro"],
					colAligns: ["center","left","left"]
				},
				multselect: false
			};
		var gurl = AppActionURI.appButton + "!defineButton.json?P_type=" + type + "&P_tableId=" + tableId + "&P_componentVersionId=todo&P_menuId=" + app.menuId;
		initGridWithoutPageable(grid, gcfg, gurl);
		processHiddenButtons(grid, type);
		return grid;
	} 
	
	function processHiddenButtons(grid, type) {
		var url = AppActionURI.workflowButtonSetting + "!hiddenButtons.json?P_workflowVersionId=" + app.menuId +
		"&P_activityId=" + that.activityId + "&P_buttonType=" + type;
		var buttonArr = loadJson(url);
		if (isEmpty(buttonArr)) return;
		var buttonStr = "," + buttonArr.join(",") + ",";
		grid.forEachRow(function(rowId) {
			var buttonCode = grid.getUserData(rowId, "buttonCode");
			if (buttonStr.indexOf("," + buttonCode + ",") > -1) {
				grid.cells(rowId, 0).setValue("0");
			}
		});
	}
}

function initSpecOpinion(layout, that) {
	layout.detachToolbar();
	var btbar = layout.attachToolbar();
	btbar.setIconPath(TOOLBAR_IMAGE_PATH);
	btbar.addButton("sepc$add", 0, "新增", "new.gif");
	btbar.addSeparator("sepc$septr", 1);
	btbar.addButton("sepc$del", 2, "删除", "delete.gif");
	btbar.attachEvent("onClick", function(itemId) {
		if ("sepc$add" == itemId) {
			if (grid.getRowId(0) == '') {
            	return;
            }
            grid.addRow('','',0);
            grid.cells('',0).open();
		} else if ("sepc$del" == itemId) {
			var rowIds = grid.getSelectedRowId();
			if (isEmpty(rowIds)) {
				dhtmlx.message(getMessage("select_record"));
				return;
			} 
			dhtmlx.confirm({
				type:"confirm",
				text: getMessage("delete_warning"),
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
					if (flag) {
						var durl = AppActionURI.workflowAssistOpinion + "!destroy.json?id=" + rowIds + "&P_workflowVersionId=" + app.menuId;
						dhtmlxAjax.get(durl, function(loader) {
							var msg = eval("(" + loader.xmlDoc.responseText + ")");
							if (msg.success) {
								dhtmlx.message(getMessage("delete_success"));
					            loadGridData(grid, gcfg, gurl);
							} else {
								dhtmlx.message(getMessage("delete_failure"));
							}
				        });
					}
				}
			});
		}
	});
	
	var grid = layout.attachGrid();
	var gcfg = {
			format: {
				headers: ["<center>&nbsp;</center>","<center>辅助意见</center>","<center>备注</center>"],
				cols: ["showOrder","opinionText", "remark"],
				colWidths: ["40","320","*"],
				colTypes: ["sub_row_form","ro","ro"],
				colAligns: ["center","left","left"]
			},
			multselect: false
		};
	var gurl = AppActionURI.workflowAssistOpinion + "!search.json?P_workflowVersionId=" + app.menuId + "&P_activityId=" + that.activityId;
	initGridWithoutPageable(grid, gcfg, gurl);
	
	grid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
		subform.c = [
						{type: "hidden", name: "_method"},
						{type: "hidden", name: "id"},
						{type: "hidden", name: "workflowVersionId"},
						{type: "hidden", name: "activityId"},
						{type: "hidden", name: "showOrder"},
						{type: "block", width:600, list:[
			 				{type: "input", label: "辅助意见", labelWidth:80, labelAlign:"right", name: "opinionText", required:true, width:400, maxLength:255, tooltip:"请输入辅助意见!"}
			 			]},
						{type: "block", width:600, list:[
			  				{type: "input", label: "备　　注：", labelWidth:80, labelAlign:"right", name: "remark", inputHeight:60, width:400, rows:4},
				 			{type: "newcolumn"},
							{type: "button", name: "save", value: "保存", width:80, offsetTop: 40}
			 			]}
					];
	});
	
	grid.attachEvent("onSubFormLoaded", function(subform,id,index) {
		//
		if (isNotEmpty(id)) {
			var url = AppActionURI.workflowAssistOpinion + "!edit.json?P_workflowVersionId=" + app.menuId + "&id=" + id;
			var obj = loadJson(url);
			subform.setFormData(obj);
		} else {
			subform.setItemValue("workflowVersionId", app.menuId);
			subform.setItemValue("activityId", that.activityId);
		}
		//
		subform.attachEvent("onButtonClick", function(itemId) {
			if ("save" == itemId) {
				var surl = AppActionURI.workflowAssistOpinion + "!save.json";
				subform.send(surl, "post", function(loader, response) {
					var formData = eval("(" + loader.xmlDoc.responseText + ")");
					if (formData.id == null || formData.id == "") {
						dhtmlx.message(getMessage("save_failure"));
					} else {
						dhtmlx.message(getMessage("save_success"));
						loadGridData(grid, gcfg, gurl);
					}
				});
			}
		});
	});
	
	grid.attachEvent("onSubRowOpen", function(id,expanded){
		if (expanded) {
			grid.forEachRow(function(rId){
				if (id != rId) {
					grid.cells(rId,0).close();
				}
			});
		}
	});
}
/**
 * 工作流与表单界面配置
 * @param that
 */
function initFormSetting(that) {
	// 初始化当前TAB页面板
	var _this = this;
	var curLayout = that.tabbar.cells("tab$03").attachLayout("2U");
	initLayout(curLayout);
	
	curLayout.cells("a").setWidth(445);
	var subLayout = curLayout.cells("a").attachLayout("2U");
	initLayout(subLayout);
	
	var subLayoutsbar = subLayout.attachStatusBar();
	var subLayoutbtbar = new dhtmlXToolbarObject(subLayoutsbar.id);
	subLayoutbtbar.setIconPath(TOOLBAR_IMAGE_PATH);
	subLayoutbtbar.addButton("subLayout$save", 0, "保存", "save.gif");
	subLayoutbtbar.setAlign("right");
	subLayoutbtbar.attachEvent("onClick", function(itemId) {
		if ("subLayout$save" == itemId) {
			_this.subLayoutbSave();
		}
	});
	this.subLayoutbSave = function() {
		var rowsValue = "";
		bGrid.forEachRow(function(rowId) {
			var disabled = bGrid.cells(rowId, 1).getValue();
			if ("1" == disabled) {
				var columnId = bGrid.getUserData(rowId, "columnId");
				rowsValue += ";" + columnId;
			} 
		});
		if (rowsValue.length > 0) {
			rowsValue = rowsValue.substring(1);
		}
		var url = AppActionURI.workflowFormSetting + "!save?P_workflowId=" + that.nodeId +
				"&P_tableId=" + that.tableId + 
				"&P_activityId=" + that.activityId + 
				"&P_moduleId=" + that.moduleId + 
				"&P_rowsValue=" + rowsValue;
		dhtmlxAjax.get(url, function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.status) {
				dhtmlx.message(getMessage("operate_success"));
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
		});
	};
	
	var aLayout = subLayout.cells("a");
	var bLayout = subLayout.cells("b");

	// 流程节点模块区域
	aLayout.hideHeader();
	aLayout.setWidth(260);
	var aGrid = aLayout.attachGrid();
	var aCfg = {
			format: {
				headers: ["<center>流程节点</center>","<center>模块名称</center>"],
				cols: ["activityName","moduleName"],
				userdata: ["activityId","moduleId"],
				colWidths: ["120","*"],
				colTypes: ["ro","ro"],
				colAligns: ["left","left"]
			},
			multselect: false
		};
	this.aGridUrl = function() {
		return AppActionURI.workflowBinding + "!bindedModule.json?E_model_name=datagrid&Q_workflowId=" + that.nodeId;
	};
	initGridWithoutPageable(aGrid, aCfg, _this.aGridUrl());
	aGrid.attachEvent("onRowSelect", function(rowId, ind) {
		that.activityId = aGrid.getUserData(rowId, "activityId");
		that.moduleId   = aGrid.getUserData(rowId, "moduleId");
		var url = AppActionURI.workflowFormSetting + "!formColumns.json?E_model_name=datagrid" +
				"&Q_workflowId=" + that.nodeId +
				"&Q_activityId=" + activityId +
				"&Q_tableId=" + that.tableId +
				"&Q_moduleId=" + moduleId;
		bGrid.clearAll();
		loadGridData(bGrid, bCfg, url);
		// 展现表单预览
		form = _this.previewForm(that.tableId, moduleId);
	});

	// 界面字段
	bLayout.hideHeader();
	bLayout.setWidth(180);
	var bGrid = bLayout.attachGrid();
	var bCfg = {
			format: {
				headers: ["<center>字段名称</center>","<center>不可用</center>"],
				cols: ["showName","disabled"],
				userdata: ["colspan","required","readonly","hidden","inputType","defaultValue","columnName","dataType","length","codeTypeCode","columnId"],
				colWidths: ["120","*"],
				colTypes: ["ro","ch"],
				colAligns: ["left","center"]
			},
			multselect: false
		};
	initGridWithoutPageable(bGrid, bCfg, null);
	bGrid.attachEvent("onCheckbox", function(rowId, cInd, state) { 
		/*var required = bGrid.getUserData(rowId, "required");
		if ("1" == required) {
			dhtmlx.alert("必输项，不可置灰！");
			bGrid.cells(rowId).setValue("0");
			return;
		}//*/
		var name = bGrid.getUserData(rowId, "columnName");
		if ("1" == state) {
			form.disableItem(name);
		} else {
			form.enableItem(name);
		}
	}); 

	// 界面预览
	var formLayout = curLayout.cells("b");
	formLayout.setText("界面预览");
	var curForm = null;
	// 获取表单页面整体设置
	this.formMaster = function(tableId, moduleId) {
		var url = AppActionURI.appForm + "!edit.json?P_tableId=" + tableId + "&P_moduleId=" + moduleId;
		var obj = loadJson(url);
		return obj;
	};
	// 预览表单
	this.previewForm = function(tableId, moduleId) {
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
		var formObj = _this.formMaster(tableId, moduleId);
		var formcolspan = formObj.colspan;
		var border = formObj.border;
		var blockWidth = (labelWidth + inputWidth) * formcolspan + 40;
		var block = {type: "block", width: blockWidth, list:[]};
		var newcolumn = {type:"newcolumn"};
		var preIdx = 0;
		var curIdx = 0;
		var ttlNum = bGrid.getRowsNum();
		var rowIdx = 0;
		var disableItems = [];
		for (var rowIdx = 0;rowIdx < ttlNum; rowIdx++) {
			//["colspan","required","readonly","hidden","textarea","defaultValue","columnName","dataType","length","codeTypeCode"]
			var rowId     = bGrid.getRowId(rowIdx);
    		var label     = bGrid.cells(rowId, 0).getValue();
    		var disabled  = bGrid.cells(rowId, 1).getValue();
    		var colspan   = bGrid.getUserData(rowId, "colspan");
    		var required  = bGrid.getUserData(rowId, "required");
    		var readonly  = bGrid.getUserData(rowId, "readonly");
    		var hidden    = bGrid.getUserData(rowId, "hidden");
    		var inputType = bGrid.getUserData(rowId, "inputType");
    		var value     = bGrid.getUserData(rowId, "defaultValue");//*/
    		var name      = bGrid.getUserData(rowId, "columnName");
    		var datatype  = bGrid.getUserData(rowId, "dataType");
    		var maxLength = bGrid.getUserData(rowId, "length");
    		
    		if ("1" == disabled) {
    			disableItems.push(name);
    		}
    		
    		var type = _this.getFormItemType(hidden, datatype, textarea);
    		var onerow = {};
    		if ("1" != required) {
    			label += "：";
    		}
    		onerow.label = (("1" == border) ? "<strong>" + label + "</strong>" : label);
    		onerow.name = name;
    		onerow.value = value;
    		if ("hidden" != type) {
    			onerow.required = ("1" == required ? true : false);
        		onerow.readonly = ("1" == readonly ? true : false);
        		onerow.maxLength= maxLength;
        		if ("textarea" == type) {
        			type = "input";
        			onerow.rows = 3;
        			colspan = formcolspan;
        		} else if ("combo" == type) {
        			onerow.options = [{value: "", text: "请选择"}];
        		} else if ("calendar" == type) {
        			onerow.dateFormat = "%Y-%m-%d";
        			onerow.readonly = true;
        		}
        		if (colspan > 1) {
        			onerow.width = (inputWidth * colspan + labelWidth * (colspan - 1));
        		}
        		curIdx += colspan;
    		}    		
    		onerow.type = type;
    		if (curIdx > formcolspan) {
    			formJson.push(block);
    			block = {type: "block", width: blockWidth, list:[]};
    			curIdx = colspan; preIdx = 0;// reset to 0
    		}
    		block.list.push(onerow);
    		if (curIdx > preIdx) {
    			block.list.push(newcolumn);
    		}
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
	this.getFormItemType = function(hidden, inputType) {
		if (hidden && "1" == hidden) return "hidden";
		if ("textarea" == inputType) return "textarea";
		if ("datepicker" == inputType) return "calendar";
		if ("text" == inputType) return "input";
		return "combo";
	};
	// reload form binded
	that.reloadFormSetting = function() {
		loadGridData(aGrid, aCfg, _this.aGridUrl());
		bGrid.clearAll();
		curForm = formLayout.attachForm();
		/*if (curForm && curForm.unload) {
			curForm.unload();
			curForm = null;
		}//*/
	};
	// 标记已加载
	that.formSettingInited = true;
}


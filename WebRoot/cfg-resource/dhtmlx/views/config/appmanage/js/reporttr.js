/**
 * 数据源关系配置(table relation)
 */
function loadCellReportTR(tabbar, that) {
	var _this = this;
	var moduleUrl = AppActionURI.reportTableRelation;
	
	var dhxLayout = tabbar.cells("tab$report$02").attachLayout("4W");
	dhxLayout.cont.obj._offsetTop = 1;
	dhxLayout.cont.obj._offsetLeft = 1;
	dhxLayout.cont.obj._offsetHeight = -2;
	dhxLayout.cont.obj._offsetWidth = -2;
	dhxLayout.setSizes();
	
	var ttbar = dhxLayout.attachToolbar();
	ttbar.setIconPath(TOOLBAR_IMAGE_PATH);
	ttbar.addDiv("origintablediv", 3);
	ttbar.addDiv("relatetablediv", 4);
	ttbar.addSeparator("top$septr$02", 7);
	ttbar.addButton("checkrelation", 8, "检测表关系");
	ttbar.addSeparator("top$septr$01", 9);
	ttbar.addButton("obtainrelation", 10, "自动获取表关系");
	var originCombo = new dhtmlXCombo("origintablediv","origintable",190);
	var relateCombo = new dhtmlXCombo("relatetablediv","relatetable",190);
	//originCombo.enableFilteringMode(true);
	//relateCombo.enableFilteringMode(true);
	originCombo.readonly(true);
	relateCombo.readonly(true);
	originCombo.attachEvent("onSelectionChange", function() {
		_this.comboChange(originCombo, relateCombo, aGrid, aCfg);
	});
	relateCombo.attachEvent("onSelectionChange", function() {
		_this.comboChange(relateCombo, originCombo, bGrid, bCfg);
	});

	ttbar.attachEvent("onClick", function(itemId) {
		if ("checkrelation" == itemId) {
			_this.checkRelation();
		} else if ("obtainrelation" == itemId) {
			_this.obtainRelation();
		}
	});
	ttbar.attachEvent("onChange", function(itemId) {
		if ("origintableinp" == itemId) {
			_this.tableChange(itemId, "relatetableinp", aGrid, aCfg);
		} else if ("relatetableinp" == itemId) {
			_this.tableChange(itemId, "origintableinp", bGrid, bCfg);
		} 
	});
	// save report data source configuration
	this.saveTR = function() {
		var cnt = dGrid.getRowsNum();
		/*if (0 == cnt) {
			dhtmlx.alert("请先进行数据源字段配置，再保存！");
			return;
		}*/
		var rowsValue = "";
		for (var i=0; i < cnt; i++) {
			var rowId = dGrid.getRowId(i);
			rowsValue += ";" + rowId.replace(/(;)/g, ",");
		}
		rowsValue = rowsValue.substring(1);
		var url = moduleUrl + "!save.json?P_reportId=" + nodeId + "&P_rowsValue=" + rowsValue;
		dhtmlxAjax.get(url,function(loader){
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				dhtmlx.message(getMessage("operate_success"));
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
		});//*/
	};
	// 
	this.checkRelation = function() {
		var url = moduleUrl + "!check.json?P_reportId=" + nodeId;
		dhtmlxAjax.get(url,function(loader){
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				dhtmlx.message(jsonObj.message);
		    } else if ("ERROR" == jsonObj.message) {
		    	dhtmlx.message("检查出错，请联系管理员！");
		    } else {
		    	dhtmlx.message("[" + jsonObj.message + "]未配置表关系！");
		    }
		});//*/
	};
	// 
	this.obtainRelation = function() {
		var url = moduleUrl + "!findTableRelation.json?P_reportId=" + nodeId;
		dhtmlxAjax.get(url,function(loader){
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				dhtmlx.message(jsonObj.message);
				if (jsonObj.status == 1) {
			    	loadGridData(dGrid, dCfg, dUrl);
			    }
			} else {
				dhtmlx.message("自动获取表关系出错，请联系管理员！");
			}
		});//*/
	};
	// 
	this.comboChange = function(combo, relationCombo, grid, cfg) {
		var rowId = combo.getSelectedValue();
		if (null == rowId || "" == rowId) return;
		var relateRowId = relationCombo.getSelectedValue();
		if (rowId == relateRowId) {
			combo.selectOption(0);
			grid.clearAll();
			dhtmlx.alert("源表与关联表不能是同一张表，请重新选择！");
			return;
		}
		var url = AppActionURI.reportDataSource + "!columnsOfTable.json?E_model_name=datagrid&Q_tableId=" + rowId;
		loadGridData(grid, cfg, url);
	};
	// load report tables
	this.loadReportTables = function() {
		originCombo.clearAll();
		relateCombo.clearAll();
		var url = AppActionURI.reportDataSource + "!reportTables.json?Q_reportId=" + nodeId;
		var datas = loadJson(url);
		var od = new Array({value: "", text: "请选择源表"}), rd = new Array({value:"", text:"请选择关联表"});
		for (var i = 0; i < datas.length; i++) {
			od.push(datas[i]);
			rd.push(datas[i]);
		}
		originCombo.addOption(od);
		relateCombo.addOption(rd);
		originCombo.selectOption(0);
		relateCombo.selectOption(0);
	};
	this.loadReportTables();
	
	var aLayout = dhxLayout.cells("a"); // 用户表列表
	var bLayout = dhxLayout.cells("b"); // 表字段列表
	var cLayout = dhxLayout.cells("c"); // 按钮操作区
	var dLayout = dhxLayout.cells("d"); // 绑定列表区
	// 用户表列表
	aLayout.setWidth(205);
	aLayout.hideHeader();
	var atbar = aLayout.attachToolbar();
	atbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	atbar.addInputText("table$search", 0,"源表字段检索(支持拼音)",180);
	atbar.attachEvent("onEnter", function(itemId, value) {
		if ("table$search" == itemId) {
			searchInGrid(aGrid, value, 0);
		}
	});//*/
	atbar.attachEvent("onFocus", function(itemId) {
		if ("table$search" == itemId) {
			atbar.setValue(itemId, "");
		}
	});
	var aGrid = aLayout.attachGrid();
	var aCfg = {
			format: {
				headers: ["<center>源表字段名称</center>"],
				//cols: ["show_name"],
				colWidths: ["200"],
				colTypes: ["ro"],
				colAligns: ["left"]
			}
		};
	initGridWithoutPageable(aGrid, aCfg, null);
	aGrid.enableMultiselect(false);
	// 表字段列表
	bLayout.setWidth(205);
	bLayout.hideHeader();
	var btbar = bLayout.attachToolbar();
	btbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	btbar.addInputText("relatetable$search", 0,"关联表字段检索(支持拼音)",180);
	btbar.attachEvent("onEnter", function(itemId, value) {
		if ("relatetable$search" == itemId) {
			searchInGrid(bGrid, value, 0);
		}
	});
	btbar.attachEvent("onFocus", function(itemId) {
		if ("relatetable$search" == itemId) {
			btbar.setValue(itemId, "");
		}
	});
	var bGrid = bLayout.attachGrid();

	var bCfg = {
			format: {
				headers: ["<center>关联表字段名称</center>"],
				//cols: ["show_name"],
				colWidths: ["200"],
				colTypes: ["ro"],
				colAligns: ["left"]
			}
		};
	initGridWithoutPageable(bGrid, bCfg, null);
	bGrid.enableMultiselect(false);

	// create div in document body
	var divIdTR = "DIV-oparatorArea-TR";
	createEmptyDiv(divIdTR, 40);
	var btnForm = new dhtmlXForm(divIdTR, getMoveButton({allToRight:false,saveButton:true}));
	btnForm.attachEvent("onButtonClick", function(id){
	    if ("toRight" == id) {
	    	_this.toMoveSelected();
	    } else if ("toLeft" == id) {
	    	_this.toCancel(false);
	    } else if ("allToLeft" == id) {
	    	_this.toCancel(true);
	    } else if ("save" == id) {
	    	_this.saveTR();
	    }
	});
	cLayout.hideHeader();
	cLayout.setWidth(105);
	cLayout.attachObject(divIdTR);
	// 移动选中行
	this.toMoveSelected = function() {
		var oRowId = aGrid.getSelectedRowId();
    	if (null == oRowId) {dhtmlx.alert("请选择源表字段！"); return;}
    	var rRowId = bGrid.getSelectedRowId();
    	if (null == rRowId) {dhtmlx.alert("请选择关系表字段！"); return;}
    	var oTableId   = originCombo.getSelectedValue();
    	var oTableName = originCombo.getSelectedText();
    	var rTableId   = relateCombo.getSelectedValue();
    	var rTableName = relateCombo.getSelectedText();
		var rowId = oTableId + ";" + oRowId + ";" + rTableId + ";" + rRowId;
		if (dGrid.getRowIndex(rowId) > -1) {
			dGrid.selectRowById(rowId);
			dhtmlx.alert("此关系已经配好，请重新设置！");
			return;
		}
		var oColumnName  = aGrid.cells(oRowId, 0).getValue();
		var rColumnName  = bGrid.cells(rRowId, 0).getValue();
		var values = [oTableName,oColumnName,rTableName,rColumnName];
		dGrid.addRow(rowId, values);
    	//formGrid.deleteSelectedRows();
    	aGrid.clearSelection();
    	bGrid.clearSelection();
	};
	// 取消配置
	this.toCancel = function(isAll) {
		if (isAll) {
			dGrid.clearAll();
		} else {
			dGrid.deleteSelectedRows();
		}
	};
	// 表字段列表
	//dLayout.setWidth(500);
	dLayout.hideHeader();
	
	var dGrid = dLayout.attachGrid();

	var dCfg = {
			format: {
				headers: ["<center>源表名</center>", "<center>源表已选字段</center>","<center>关联表名</center>", "<center>关联表已选字段</center>"],
				//cols: ["show_name"],
				colWidths: ["160", "160","160", "160"],
				colTypes: ["ro", "ro","ro", "ro"],
				colAligns: ["left", "left","left", "left"]
			}
		};
	var dUrl = moduleUrl + "!defineTableRelation.json?E_model_name=datagrid&Q_reportId=" + nodeId;
	initGridWithoutPageable(dGrid, dCfg, dUrl);//*/
	dGrid.enableDragAndDrop(true);
	
	// success loaded (loaded completed)
	that.relationLoaded = true;
}

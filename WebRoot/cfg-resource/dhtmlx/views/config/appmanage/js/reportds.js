/**
 * 数据源字段配置
 */

function loadCellReportDS(tabbar, that) {
	var _this = this;
	var moduleUrl = AppActionURI.reportDataSource;
	
	var dhxLayout = tabbar.cells("tab$report$01").attachLayout("4W");
	dhxLayout.cont.obj._offsetTop = 1;
	dhxLayout.cont.obj._offsetLeft = 1;
	dhxLayout.cont.obj._offsetHeight = -2;
	dhxLayout.cont.obj._offsetWidth = -2;
	dhxLayout.setSizes();
	
	/*var ttbar = dhxLayout.attachToolbar();
	ttbar.setIconPath(IMAGE_PATH);
	ttbar.addButton("save", 0, "保存", "save.gif");
	//ttbar.addSeparator("bottom$septr$01", 1);
	//ttbar.setAlign("right");
	ttbar.attachEvent("onClick", function(itemId) {
		if ("save" == itemId) {
			_this.save();
		}
	});//*/
	// save report data source configuration
	this.save = function() {
		var cnt = dGrid.getRowsNum();
		if (0 == cnt) {
			dhtmlx.alert("请先进行数据源字段配置，再保存！");
			return;
		}
		var preTableId = null;
		var tabRowsValue = "";
		var colRowsValue = "";
		for (var i = 0; i < cnt; i++) {
			var rowId = dGrid.getRowId(i);
			var idArr = rowId.split(";");
			var tableId = idArr[0], columnId = idArr[1];
			// 拼接所有表信息
			if (null == preTableId || preTableId != tableId) {
				tabRowsValue += ";" + tableId + ","+ 
						dGrid.getUserData(rowId, "userdata_0");         // 表信息
			}
			preTableId = tableId;
			// 拼接所有字段信息
			colRowsValue += ";" + tableId + "," + columnId + "," + dGrid.cells(rowId, 1).getValue();
		}
		tabRowsValue = tabRowsValue.substring(1);
		colRowsValue = colRowsValue.substring(1);
		var url = moduleUrl + "!save.json";
		var params = "P_reportId=" + nodeId + "&P_tabRowsValue=" + tabRowsValue + "&P_colRowsValue=" + colRowsValue;
		/*dhtmlxAjax.get(encodeURI(url),function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				dhtmlx.message(getMessage("operate_success"));
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
		});*/
		dhtmlxAjax.post(url, encodeURI(params),function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				dhtmlx.message(getMessage("operate_success"));
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
		});
	};
	
	var aLayout = dhxLayout.cells("a"); // 用户表列表
	var bLayout = dhxLayout.cells("b"); // 表字段列表
	var cLayout = dhxLayout.cells("c"); // 按钮操作区
	var dLayout = dhxLayout.cells("d"); // 绑定列表区
	// 用户表列表
	aLayout.setWidth(205);
	aLayout.hideHeader();
	//modify by xu.wy @20150826
//				var atbar = aLayout.attachToolbar();
//				atbar.setIconsPath(TOOLBAR_IMAGE_PATH);
//				atbar.addInput("table$search", 0,"表名检索",180);
//				atbar.attachEvent("onEnter", function(itemId, value) {
//					if ("table$search" == itemId) {
//						searchInGrid(aGrid, value, 0);
//					}
//				});
//				var aGrid = aLayout.attachGrid();
//				aGrid.attachEvent("onRowSelect", function(rowId, cind) {
//					//var type = aGrid.getUserData(rowId, "userdata_0");
//					var bUrl = moduleUrl + "!columnsOfTable.json?E_model_name=datagrid&Q_tableId=" + rowId;// + "&Q_type=" + type;
//					loadGridData(bGrid, bCfg, bUrl);
//				});
//				var aCfg = {
//						format: {
//							headers: ["<center>表名称（或视图名）</center>"],
//							//cols: ["show_name"],,
//							//userdata:[2],
//							colWidths: ["200"],
//							colTypes: ["ro"],
//							colAligns: ["left"]
//						}
//					};
//				var aUrl = moduleUrl + "!tables.json?E_model_name=datagrid";
//				initGridWithoutPageable(aGrid, aCfg, aUrl);
//				aGrid.enableMultiselect(false);
	//modify by xu.wy @20150826start
	aLayout.setText("表名称（或视图名）");
	var tree = aLayout.attachTree();
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	// 物理表分类节点
	var ptItem = loadJson(AppActionURI.tableClassification + "!treeNode.json");
	// 视图分类节点
	var vItem = loadJson(AppActionURI.tableClassification + "!treeNodeOfView.json");
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
		{id:"-PT", text: "物理表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item: ptItem},
		{id:"-V", text: "视图", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, userdata:[{name:"type",content:"0"},{name:"classification",content:"V"}], item: vItem}
	]};
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(AppActionURI.tableTree + "!tree.json?E_model_name=tree&P_filterId=parentId&F_in=name,child&P_UD=type,classification");
	tree.loadJSONObject(treeJson);
	//tree.refreshItem("-1");
	//点击节点刷新右边列表
	tree.attachEvent("onClick", function(nId) {
		var type = tree.getUserData(nId, "type");
		if (type=="1") {
			// 树节点ID
			var bUrl = moduleUrl + "!columnsOfTable.json?E_model_name=datagrid&Q_tableId=" + nId;// + "&Q_type=" + type;
			loadGridData(bGrid, bCfg, bUrl);
		} else {
			// 树节点ID
			bGrid.clearAll();
		}
	});
	//modify by xu.wy @20150826stop

	// 表字段列表
	bLayout.setWidth(205);
	bLayout.hideHeader();
	var btbar = bLayout.attachToolbar();
	btbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	btbar.addInput("column$search", 0,"字段检索",180);
	btbar.attachEvent("onEnter", function(itemId, value) {
		if ("column$search" == itemId) {
			searchInGrid(bGrid, value, 0);
		}
	});
	var bGrid = bLayout.attachGrid();

	var bCfg = {
			format: {
				headers: ["<center>字段名称（表或视图）</center>"],
				//cols: ["show_name"],
				//userdata: [2],
				colWidths: ["200"],
				colTypes: ["ro"],
				colAligns: ["left"]
			}
		};
	initGridWithoutPageable(bGrid, bCfg, null);

	// create div in document body
	var divId = "DIV-oparatorArea-DS";
	createEmptyDiv(divId, 35);
	var buttonForm = new dhtmlXForm(divId, getMoveButton({/*allToRight:false, */saveButton:true}));
	buttonForm.attachEvent("onButtonClick", function(id){
	    if ("toRight" == id) {
	    	_this.moveSelected(tree, bGrid, dGrid);
	    } else if ("toLeft" == id) {
	    	_this.cancel(dGrid, false);
	    } else if ("allToLeft" == id) {
	    	_this.cancel(dGrid, true);
	    } else if ("save" == id) {
	    	_this.save();
	    } else if ("allToRight" == id) {
	    	_this.moveSelected(tree, bGrid, dGrid, true);
	    }
	});
	cLayout.hideHeader();
	cLayout.setWidth(105);
	cLayout.attachObject(divId);
	// 移动选中行
	this.moveSelected = function(tabTree, colGrid, toGrid, isAll) {
		var tabRowId = tabTree.getSelectedItemId();
    	if (null == tabRowId) {dhtmlx.alert("请选择表名！"); return;}
    	var colRowIds = null;
    	if (isAll) {
    		colRowIds = colGrid.getAllRowIds();
    	} else {
    		colRowIds = colGrid.getSelectedRowId();
    	}
    	if (null == colRowIds) {dhtmlx.alert("请选择字段！"); return;}
    	var colRowIdArr = colRowIds.split(",");
    	var tableInfo   = tabTree.getSelectedItemText()+ " (" + tabTree.getUserData(tabRowId, "tableName") + ")";
    	//var type        = tabGrid.getUserData(tabRowId, "userdata_0");
    	var tabIndex = null;
    	var searchRlt = toGrid.findCell(tableInfo, 0, true); // 查找第一条存在记录
    	// 查找相同表中字段的最后一条索引位置后一条
    	if (searchRlt.length > 0) {
    		var rowId = searchRlt[0][0];
    		tabIndex = toGrid.getRowIndex(rowId) + 1;
    		var ttlNum = toGrid.getRowsNum();
    		while (tabIndex < ttlNum) {
    			var cell      = toGrid.cells2(tabIndex, 0);
    			var cellValue = cell.getValue();
    			if (cellValue == null || cellValue.length == 0) {
    				tabIndex += 1;
    			} else {
    				break;
    			}
    		}
    	}
    	for (var i = 0; i < colRowIdArr.length; i++) {
    		var colRowId = colRowIdArr[i];
    		var rowId = tabRowId + ";" + colRowId;
    		var idx = toGrid.getRowIndex(rowId);
    		if (idx > -1) continue;
    		var columnInfo  = colGrid.cells(colRowId, 0).getValue();
    		var values = null;
    		if (0 == i && tabIndex == null) {
    			values = [tableInfo, columnInfo];
    		} else {
    			values = ["", columnInfo];
    		}
    		if (null != tabIndex) {
    			toGrid.addRow(rowId, values, tabIndex);
    			tabIndex += 1;
    		} else {
    			toGrid.addRow(rowId, values);
    		}
    		toGrid.setUserData(rowId, "userdata_0", tableInfo); // 表信息
    	}
    	//formGrid.deleteSelectedRows();
    	//tabGrid.clearSelection();
    	colGrid.clearSelection();
	};
	// 取消配置
	this.cancel = function(fromGrid, isAll) {
		if (isAll) {
			fromGrid.clearAll();
		} else {
			fromGrid.deleteSelectedRows();
		}
	};
	// 表字段列表
	//dLayout.setWidth(500);
	dLayout.hideHeader();
	
	var dGrid = dLayout.attachGrid();

	var dCfg = {
			format: {
				headers: ["<center>表名称（或视图名）</center>", "<center>已选字段</center>"],
				//cols: ["show_name"],
				userdata: [3],
				colWidths: ["255", "260"],
				colTypes: ["ro", "ro"],
				colAligns: ["left", "left"]
			}
		};
	var dUrl = moduleUrl + "!defineTableAndColumn.json?E_model_name=datagrid&Q_reportId=" + nodeId;
	initGridWithoutPageable(dGrid, dCfg, dUrl);//*/
	dGrid.enableDragAndDrop(false);
	
	// success loaded (loaded completed)
	that.dsLoaded = true;
}

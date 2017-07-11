/** 应用定义中的排序定义.*/
function initAppSortWin(win, componentVersionId, f_close) {
	APP_sortcfg(win, app, componentVersionId, f_close);
}
/**
 * 排序自定义统一入口
 * @param win
 * @param app
 * @param componentVersionId
 * @param f_close
 */
function APP_sortcfg(win, app/*{modified:boolean, tableId:string}*/, componentVersionId, f_close /*function*/) {
	var dhxLayout = win.attachLayout("3W");
	var tableId = app.tableId;
	// 工作流配置（tableId为工作流对应的视图ID）
	if (true === app.isWorkflow) {
		tableId = app.viewId;
	}
	
	dhxLayout.cont.obj._offsetTop = 1;
	dhxLayout.cont.obj._offsetLeft = 1;
	dhxLayout.cont.obj._offsetHeight = -2;
	dhxLayout.cont.obj._offsetWidth = -2;
	dhxLayout.setSizes();
	
	var sbar = dhxLayout.attachStatusBar();
	var btbar = new dhtmlXToolbarObject(sbar.id);
	btbar.setIconPath(TOOLBAR_IMAGE_PATH);
	btbar.addButton("bottom$save", 0, "保存", "save.gif");
	btbar.addSeparator("bottom$septr$01", 1);
	btbar.addButton("bottom$clear", 2, "清空配置", "delete.gif");
	btbar.addSeparator("bottom$septr$02", 3);
	btbar.addButton("bottom$close", 4, "关闭", "close.gif");
	btbar.setAlign("right");
	btbar.attachEvent("onClick", function(itemId) {
		if ("bottom$close" == itemId) {
			if (typeof f_close == "function") f_close();
		} else if ("bottom$save" == itemId) {
			var cnt = rGrid.getRowsNum();
			if (0 == cnt) {
				dhtmlx.alert("请先进行列表字段配置，再保存！");
				return;
			}
			var rowsValue = "";
			for (var i = 0; i < cnt; i++) {
				var rowId = rGrid.getRowId(i);
				var sortType = rGrid.cells(rowId,1).getValue();
				rowsValue += ";" + rowId + "," + sortType;
			}
			rowsValue = rowsValue.substring(1);
			var url = AppActionURI.appSort + "!save.json?P_tableId=" + tableId + "&P_menuId=" + app.menuId 
			                                         + "&P_componentVersionId=" + componentVersionId + "&P_rowsValue=" + rowsValue;
			if (true === app.isWorkflow) { url += "&P_userId=1";} // 工作流时,没有个性化设置
			dhtmlxAjax.get(url,function(loader){
				var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				if (jsonObj.success) {
					dhtmlx.message(getMessage("operate_success"));
			    } else {
			    	dhtmlx.message(getMessage("operate_failure"));
			    }
				app.modified = false;
			});
		} else if ("bottom$clear" == itemId) {
			 dhtmlx.confirm({
					type:"confirm",
					text: "确定要清空列表排序配置？",
					ok: "确定",
					cancel: "取消",
					callback: function(flag) {
						if (flag) {
							var url = AppActionURI.appSort + "!clear.json?P_tableId=" 
								+ tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId;
							dhtmlxAjax.get(url,function(loader){
								var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
								if (jsonObj.success) {
							    	rGrid.forEachRow(function(rowId) {
							    		var name     = rGrid.cells(rowId, 0).getValue();
							    		var sortType = rGrid.cells(rowId, 1).getValue();				    		
							    		lGrid.addRow(rowId, name);
							    		lGrid.setUserData(rowId, "userdata_0", sortType);
									});
							    	rGrid.clearAll();
							    	dhtmlx.message(getMessage("operate_success"));
							    } else {
							    	dhtmlx.message(getMessage("operate_failure"));
							    }
								app.modified = false;
							});
						}
					}
			});
		}
	});
	
	var lLayout = dhxLayout.cells("a");
	var cLayout = dhxLayout.cells("b");
	var rLayout = dhxLayout.cells("c");
	//lLayout.setText("源字段");
	lLayout.setWidth(205);
	lLayout.hideHeader();
	var ttbar = dhxLayout.attachToolbar();
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addDiv("top$searchcolumndiv", 0);
	
	var sform = initSearchArea();
	//cLayout.setText("操作区");
	cLayout.hideHeader();
	cLayout.setWidth(115);

	//rLayout.setText("排序字段");
	rLayout.hideHeader();

	var lcfg = {
			format: {
				headers: ["<center>字段名称</center>"],
				cols: ["showName"],
				id  : ["columnId"],
				colWidths: ["200"],
				colTypes: ["ro"],
				colAligns: ["left"]
			}
		};
	var rcfg = {
			format: {
				headers: ["<center>字段名称</center>","<center>排序</center>"],
				cols   : ["showName", "sortType"],
				id     : ["columnId"],
				colWidths: ["200", "*"],
				colTypes : ["ro", "co"],
				colAligns: ["left", "left"]
			}
		};
		
	var lGrid = dhxLayout.cells("a").attachGrid();
	var rGrid = dhxLayout.cells("c").attachGrid();
	rGrid.enableDragAndDrop(true);
	
	var rcombo = rGrid.getCombo(1);
	rcombo.put("asc", "升序");
	rcombo.put("desc", "降序");
	var params = "E_model_name=datagrid&E_frame_name=coral&P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId;
	var lurl = AppActionURI.appSort + "!defaultColumn.json?" + params;
	var rurl = AppActionURI.appSort + "!defineColumn.json?" + params;
	initGridWithoutColumnsAndPageable(lGrid, lcfg, lurl);
	initGridWithoutColumnsAndPageable(rGrid, rcfg, rurl);
	
	lGrid.attachEvent("onRowDblClicked", function(rowId, cInd){
		if (null == rowId) return;
		// add row in right grid
		rGrid.addRow(rowId, [lGrid.cells(rowId,0).getValue(), "asc"]);
		// delete current row
		lGrid.deleteRow(rowId);
		//
		app.modified = true;
	});
	// 
	rGrid.attachEvent("onDrop", function (sId, tId, dId, sObj, tObj, sCol, tCol) {
		app.modified = true;
	});
	rGrid.attachEvent("onEditCell", function (stage, rId, cInd, nValue, oValue) {
		if (nValue != oValue) app.modified = true;
		return true;
	});
	// create div in document body
	createEmptyDiv("DIV-oparatorArea-sort", 33);
	
	var buttonForm = new dhtmlXForm("DIV-oparatorArea-sort", getMoveButton());
	
	buttonForm.attachEvent("onButtonClick", function(id){
	    if ("toRight" == id) {
	    	var rowIds = lGrid.getSelectedRowId();
	    	if (null == rowIds) return;
	    	var rowArray = rowIds.split(",");
	    	for (var i = 0; i < rowArray.length; i++) {
	    		var name     = lGrid.cells(rowArray[i],0).getValue();
	    		var sortType = lGrid.getUserData(rowArray[i],"userdata_0");
	    		if (null == sortType || "" == sortType) {
	    			sortType = "asc";
	    		}
	    		rGrid.addRow(rowArray[i], [name, sortType]);
	    	}
	    	lGrid.deleteSelectedRows();
	    } else if ("allToRight" == id) {
	    	lGrid.forEachRow(function(rowId) {
	    		var name     = lGrid.cells(rowId, 0).getValue();
	    		var sortType = lGrid.getUserData(rowId, "userdata_0");
	    		if (null == sortType || "" == sortType) {
	    			sortType = "asc";
	    		}
	    		rGrid.addRow(rowId, [name, sortType]);
	    	});
	    	lGrid.clearAll();
	    } else if ("toLeft" == id) {
	    	var rowIds = rGrid.getSelectedRowId();
	    	if (null == rowIds) return;
	    	var rowArray = rowIds.split(",");
	    	for (var i = 0; i < rowArray.length; i++) {
	    		var name     = rGrid.cells(rowArray[i], 0).getValue();
	    		var sortType = rGrid.cells(rowArray[i], 1).getValue();
	    		lGrid.addRow(rowArray[i], name);
	    		lGrid.setUserData(rowArray[i], "userdata_0", sortType);
	    	}
	    	rGrid.deleteSelectedRows();
	    } else if ("allToLeft" == id) {
	    	rGrid.forEachRow(function(rowId) {
	    		var name     = rGrid.cells(rowId, 0).getValue();
	    		var sortType = rGrid.cells(rowId, 1).getValue();
	    		lGrid.addRow(rowId, name);
	    		lGrid.setUserData(rowId, "userdata_0", sortType);
	    	});
	    	rGrid.clearAll();
	    } 
	    //
	    app.modified = true;
	});
	
	dhxLayout.cells("b").attachObject("DIV-oparatorArea-sort");
	
	
	/**
	 * 字段检索
	 * @returns {dhtmlXForm}
	 */
	function initSearchArea() {
		var sformJson = [{type: "input", name: "searchcolumn", className: "dhx_toolbar_form", value: "字段检索(支持拼音)", width:195, inputHeight:17}];
		var form = new dhtmlXForm("top$searchcolumndiv",sformJson);
		var scInp = form.getInput("searchcolumn");
		scInp.onfocus = function() {
			form.setItemValue("searchcolumn","");
		};
		scInp.onblur = function() {
			form.setItemValue("searchcolumn","字段检索(支持拼音)");
		};
		scInp.onkeydown = function(e) {
			e = e || window.event;
			var keyCode = e.keyCode || e.which;
			if (13 == keyCode) {
				var value = form.getItemValue("searchcolumn");
				searchInGrid(lGrid, value, 0);
			}
		};
		return form;
	}
	
}

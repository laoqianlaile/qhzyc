/** 应用定义中检索配置.*/
function initAppSearchWin(win, componentVersionId, f_close/*function*/) {
	APP_searchcfg(win, app, componentVersionId, f_close);
}

/** 检索配置统一入口.*/
function APP_searchcfg(win, app/*{modified:boolean, tableId:string}*/, componentVersionId, f_close/*function*/) {
	var tableId = app.tableId;
	// 工作流配置（tableId为工作流对应的视图ID）
	if (true === app.isWorkflow) {
		tableId = app.viewId;
	}
	var params = "E_model_name=datagrid&E_frame_name=coral&P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId;
	initBaseSearch(win);
	
	/** 基本检索初始化 */
	function initBaseSearch(layout){

		var sbar = layout.attachStatusBar(), 
		    baseSearch = layout.attachLayout("3W"),
		    btbar = new dhtmlXToolbarObject(sbar.id);
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
				var colspan = pform.getItemValue("colspan");
				var type = pform.getItemValue("type");
				var BS_cnt = rGrid.getRowsNum();
				if ( 0 == BS_cnt) {
					dhtmlx.alert("请先配置基本检索，再保存！");
					return;
				}
				var rowsValue = "";
				for (var i = 0; i < BS_cnt; i++) {
					var rowId = rGrid.getRowId(i);
					var fType =  rGrid.cells(rowId ,1).getValue();
					var sName =  rGrid.cells(rowId ,0).getValue();
					rowsValue += ";" + rowId+","+ fType + "," + sName;
				}
				rowsValue = rowsValue.substring(1);
				var url = AppActionURI.appSearchPanel + "!save.json?";
				var params = "tableId=" + tableId + "&componentVersionId=" + componentVersionId + "&menuId=" + app.menuId 
						+"&colspan=" + colspan +"&type=" + type + "&P_rowsValue=" + rowsValue;
				if (true === app.isWorkflow) { params += "&userId=1";} // 工作流时,没有个性化设置
				dhtmlxAjax.post(url, params, function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					if (jsonObj.success) {
				    	dhtmlx.message(getMessage("operate_success"));
				    } else {
				    	dhtmlx.message(getMessage("operate_failure"));
				    }
					app.modified = false;
					//if (typeof f_close == "function") f_close();
				});
			} else if ("bottom$clear" == itemId) {
				dhtmlx.confirm({
					type:"confirm",
					text: "确定要清空检索配置？",
					ok: "确定",
					cancel: "取消",
					callback: function(flag) {
						if (!flag) { return; }
						var url = AppActionURI.appSearchPanel + "!clear.json?tableId=" + tableId 
						           + "&componentVersionId=" + componentVersionId + "&menuId=" + app.menuId ;
						dhtmlxAjax.get(url,function(loader){
							var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
							if (jsonObj.success) {
								rGrid.forEachRow(function(rowId) {
									lGrid.addRow(rowId,rGrid.cells(rowId,0).getValue());
						    		lGrid.setUserData(rowId, "filterType", rGrid.cells(rowId ,1).getValue());
                                });
                                rGrid.clearAll();
						    	pform.setItemValue("colspan",1);
						    	dhtmlx.message(getMessage("operate_success"));
						    } else {
						    	dhtmlx.message(getMessage("operate_failure"));
						    }
							app.modified = false;
							//if (typeof f_close == "function") f_close();
						});
					}
				});
			}
		});
		
		
		baseSearch.cont.obj._offsetTop = 1;
		baseSearch.cont.obj._offsetLeft = 1;
		baseSearch.cont.obj._offsetHeight = -2;
		baseSearch.cont.obj._offsetWidth = -2;
		baseSearch.setSizes();
		
		var lurl = AppActionURI.appSearch + "!defaultColumn.json?" + params;
		var rurl = AppActionURI.appSearch + "!defineColumn.json?" + params;	
		
		var lLayout = baseSearch.cells("a");
		var cLayout = baseSearch.cells("b");
		var rLayout = baseSearch.cells("c");
		var divName = "top$searchcolumndiv01";
		var divOfCellB = "DIV-oparatorArea01";
		lLayout.setWidth(205);
		lLayout.hideHeader();

		var ttbar = baseSearch.attachToolbar();
		ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
		ttbar.addDiv(divName, 0);
		ttbar.addSeparator("top$septr$01", 1);
		ttbar.addDiv("top$searchpaneldiv", 2);
		cLayout.hideHeader();
		cLayout.setWidth(115);

		//rLayout.setWidth(355);
		rLayout.hideHeader();

		var lcfg = {
			format: {
				headers: ["<center>字段名称</center>"],
				cols : ["showName"],
				id   : ["columnId"],
				userdata: ["columnName", "filterType"],
				colWidths: ["200"],
				colTypes: ["ro"],
				colAligns: ["left"]
			}
		};
		var rcfg = {
				format: {
					headers: ["<center>字段名称</center>","<center>过滤条件</center>"],
					cols: ["showName", "filterType"],
					id   : ["columnId"],
					colWidths: ["200","150"],
					colTypes: ["ed","coro"],
					colAligns: ["left","left"]
				}
			};
		var lGrid = baseSearch.cells("a").attachGrid();
		
		var rGrid = baseSearch.cells("c").attachGrid();

		var sform = initSearchArea(divName, lGrid);
		var pform = initSearchPanel();
		
		var filterTypeCombo = rGrid.getCombo(1);
		filterTypeCombo.put( "LIKE",  "包含");
		filterTypeCombo.put( "EQ",  "等于");
		filterTypeCombo.put( "GT",  "大于");
		filterTypeCombo.put( "GTE",  "大于等于");
		filterTypeCombo.put( "LT",  "小于");
		filterTypeCombo.put( "LTE",  "小于等于");
		filterTypeCombo.put( "NOT",  "不等于");
		filterTypeCombo.put( "BT",  "介于");
		filterTypeCombo.put( "NLL",  "为空");
			
		rGrid.enableDragAndDrop(true,true);
		
		initGridWithoutColumnsAndPageable(lGrid, lcfg, lurl);
		initGridWithoutColumnsAndPageable(rGrid, rcfg, rurl);
		
		lGrid.attachEvent("onRowDblClicked", function(rowId, cInd){
			if (null == rowId) return;
			var filterType = lGrid.getUserData(rowId,"filterType");
			if (filterType=="") {
		    	filterType = "LIKE";
		    }
			rGrid.addRow(rowId, [lGrid.cells(rowId,0).getValue(), filterType]);
			// delete current row
			lGrid.deleteRow(rowId);
			app.modified = true;
		});
		rGrid.attachEvent("onDrop", function (sId, tId, dId, sObj, tObj, sCol, tCol) {
			app.modified = true;
		});
		rGrid.attachEvent("onEditCell", function (stage, rId, cInd, nValue, oValue) {
			if (nValue != oValue) app.modified = true;
			return true;
		});
		createEmptyDiv(divOfCellB, 34);
		
		var buttonForm = new dhtmlXForm(divOfCellB, getMoveButton());
		
		buttonForm.attachEvent("onButtonClick", function(id){
		    if ("toRight" == id) {
		    	var rowIds = lGrid.getSelectedRowId();
		    	if (null == rowIds) return;
		    	var rowArray = rowIds.split(",");
		    	for (var i = 0; i < rowArray.length; i++) {
		    		var filterType = lGrid.getUserData(rowArray[i],"filterType");
		    		if(filterType==""){
		    			filterType = "EQ";
		    		}
					rGrid.addRow(rowArray[i], [lGrid.cells(rowArray[i],0).getValue(), filterType]);
		    	}
		    	lGrid.deleteSelectedRows();
		    } else if ("allToRight" == id) {
		    	lGrid.forEachRow(function(rowId) {
		    		var name  = lGrid.cells(rowId,0).getValue();
		    		var filterType = lGrid.getUserData(rowId,"filterType");
		    		if(filterType==""){
		    			filterType = "EQ";
		    		}
		    		rGrid.addRow(rowId, [name, filterType]);
		    	});
		    	lGrid.clearAll();
		    } else if ("toLeft" == id) {
		    	var rowIds = rGrid.getSelectedRowId();
		    	if (null == rowIds) return;
		    	var rowArray = rowIds.split(",");
		    	for (var i = 0; i < rowArray.length; i++) {
		    		lGrid.addRow(rowArray[i],rGrid.cells(rowArray[i],0).getValue());
		    		lGrid.setUserData(rowArray[i], "filterType", rGrid.cells(rowArray[i] ,1).getValue());
		    	}
		    	rGrid.deleteSelectedRows();
		    } else if ("allToLeft" == id) {
		    	rGrid.forEachRow(function(rowId) {
		    		var name  = rGrid.cells(rowId, 0).getValue();
		    		var fType = rGrid.cells(rowId,1).getValue();
		    		lGrid.addRow(rowId, name);
		    		lGrid.setUserData(rowId, "filterType", fType);
		    	});
		    	rGrid.clearAll();
		    } 
		    app.modified = true;
		});
		
		baseSearch.cells("b").attachObject(divOfCellB);
		return rGrid;
	}
	
	/**
	 * 字段检索
	 * @returns {dhtmlXForm}
	 */
	function initSearchArea(divName, lGrid) {
		var sformJson = [{type: "input", name: "searchcolumn", className: "dhx_toolbar_form", value: "字段检索(支持拼音)", width:195, inputHeight:17}];
		var form = new dhtmlXForm(divName,sformJson);
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
	/**
	 * 查询区配置
	 * @returns {dhtmlXForm}
	 */
	function initSearchPanel() {
		var formJson = [
	        {type: "combo", name: "colspan", className: "dhx_toolbar_form", label: "　 列数：", style:"font-size:11px;", width: 60, 
	        	options:[{value:1, text:"1列"},
	                     {value:2, text:"2列"},
	                     {value:3, text:"3列"},
	                     {value:4, text:"4列"}]}/*,
 	   	 	{type: "newcolumn"},
 	   	 	{type: "radio", name: "type", value: 0, checked: true, offsetLeft: 20, className: "dhx_toolbar_form", label: "弹出式", position: "label-right", labelAlign:"left", labelWidth: 60},
 	   	 	{type: "newcolumn"},
 	   	 	{type: "radio", name: "type", value: 1, offsetLeft: 20, className: "dhx_toolbar_form", label: "嵌入式", position: "label-right", labelAlign:"left", labelWidth: 60}*/
        ];
     	var form = new dhtmlXForm("top$searchpaneldiv",formJson);
     	var sUrl = AppActionURI.appSearchPanel + "!show.json?tableId=" + tableId + "&componentVersionId=" + componentVersionId + "&menuId=" + app.menuId;
     	var entity = loadJson(sUrl);
     	form.setFormData(entity);
     	return form;
	}
}

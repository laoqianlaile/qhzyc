/**
 * 按钮配置
 * @param win
 * @param componentVersionId
 * @param type
 *        0-表单按钮配置 1-列表按钮配置
 * @returns {initAppButtonWin}
 */
function initAppButtonWin(win, componentVersionId, type, f_close/*function*/) {
	var moduleUrl = AppActionURI.appButton;
	var tableId = app.tableId;
	// 工作流配置（tableId为工作流对应的视图ID）
	if (true === app.isWorkflow) {
		tableId = app.viewId;
	}
	
	var _this = this;
	// 打印按钮配置具体报表用了
	var reportCfg = {};
	var dhxLayout = win.attachLayout("3W");
	initLayout(dhxLayout);
	
	var layoutA = dhxLayout.cells("a");
	var layoutB = dhxLayout.cells("b");
	var layoutC = dhxLayout.cells("c");
	
	var sbar = win.attachStatusBar();
	var btbar = new dhtmlXToolbarObject(sbar.id);
	btbar.setIconPath(TOOLBAR_IMAGE_PATH);
	btbar.addButton("bottom$save", 0, "保存", "save.gif");
	btbar.addSeparator("bottom$septr$01", 1);
	btbar.addButton("bottom$clear", 2, "清空配置", "delete.gif");
	btbar.addSeparator("bottom$septr$02", 3);
	btbar.addButton("bottom$close", 4, "关闭", "close.gif");
	btbar.setAlign("right");
	btbar.attachEvent("onClick", function(id) {
		if ("bottom$save" == id) {
			save();
		} else if ("bottom$clear" == id) {
			clear();
		} else if ("bottom$close" == id) {
			if (typeof f_close == "function") f_close();
		}
	});
	/** 保存配置.*/
	function save() {
		var ttl = cGrid.getRowsNum();
		if (ttl == 0) {
			dhtmlx.alert("请先配置，再保存！");
			return;
		}
		var rowsValue = "";
		for (var i = 0; i < ttl; i++) {
			var rowId = cGrid.getRowId(i);
			var buttonCode = cGrid.getUserData(rowId, "buttonCode");
			var buttonName = cGrid.cells(rowId, 0).getValue();
			var showName   = cGrid.cells(rowId, 1).getValue();
			var buttonClass= cGrid.cells(rowId, 2).getValue();
			var remark     = cGrid.cells(rowId, 3).getValue();
			rowsValue += (";" + buttonCode + "," + buttonName + "," + showName + "," + buttonClass + "," + remark);
		}
		if (rowsValue.length > 0) rowsValue = rowsValue.substring(1);
		var sUrl = moduleUrl + "!save.json";
		var params = "P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId
				+ "&P_type=" + type + "&P_rowsValue=" + rowsValue;
		dhtmlxAjax.post(sUrl, params, function(loader) {
			var rlt = eval("(" + loader.xmlDoc.responseText + ")");
			if (true == rlt.success) {
				dhtmlx.message(getMessage("operate_success"));
			} else {
				dhtmlx.message(getMessage("operate_failure"));
			}
			app.modified = false;
		});
	}
	/** A布局区*/
	layoutA.hideHeader();
	layoutA.setWidth(125);
	
	var aGridCfig = {
			format: {
				headers: ["<center>按钮名称</center>"],
				cols:["buttonName"],
				userdata:["buttonCode", "showName"],
				colWidths: ["*"],
				colTypes: ["ro"],
				colAligns: ["left"]
			}
		};
	var aGrid = layoutA.attachGrid();
	var data = getBoxButtons(componentVersionId, type);
	initGridWithoutPageable(aGrid, aGridCfig, data);
	// aGrid.parse(datas, "json");
	/** B布局区*/
	layoutB.hideHeader();
	layoutB.setWidth(115);
	createEmptyDiv("DIV-oparatorArea", 25);
	var buttonForm = new dhtmlXForm("DIV-oparatorArea", getMoveButton());
	layoutB.attachObject("DIV-oparatorArea");
	buttonForm.attachEvent("onButtonClick", function(id){
	    if ("toRight" == id) {
	    	var rowIds = aGrid.getSelectedRowId();
	    	if (null == rowIds) return;
	    	var idArr = rowIds.split(",");
	    	for (var i = 0; i < idArr.length; i++) {
	    		var rowId = idArr[i];
	    		var buttonName = aGrid.cells(rowId, 0).getValue();
	    		var showName   = aGrid.getUserData(rowId, "showName");
	    		var buttonCode = aGrid.getUserData(rowId, "buttonCode");
	    		var cRowId = cGrid.uid();
	    		cGrid.addRow(cRowId, [buttonName, showName,""]);
	    		cGrid.setUserData(cRowId, "buttonCode", buttonCode);
	    	}
	    	aGrid.deleteSelectedRows();
	    } else if ("allToRight" == id) {
	    	aGrid.forEachRow(function(rowId) {
	    		var buttonName = aGrid.cells(rowId, 0).getValue();
	    		var showName   = aGrid.getUserData(rowId, "showName");
	    		var buttonCode = aGrid.getUserData(rowId, "buttonCode");
	    		var cRowId = cGrid.uid();
	    		cGrid.addRow(cRowId, [buttonName, showName,""]);
	    		cGrid.setUserData(cRowId, "buttonCode", buttonCode);
	    	});
	    	aGrid.clearAll();
	    } else if ("toLeft" == id) {
	    	var rowIds = cGrid.getSelectedRowId();
	    	if (null == rowIds) return;
	    	var idArr = rowIds.split(",");
	    	for (var i = 0; i < idArr.length; i++) {
	    		var rowId = idArr[i];
	    		var buttonName = cGrid.cells(rowId, 0).getValue();
	    		var showName   = cGrid.cells(rowId, 1).getValue();
	    		var buttonCode = cGrid.getUserData(rowId, "buttonCode");
	    		aGrid.addRow(buttonCode, buttonName);
	    		aGrid.setUserData(buttonCode, "buttonCode", buttonCode);
	    		aGrid.setUserData(buttonCode, "showName", showName);
	    	}
	    	cGrid.deleteSelectedRows();
	    } else if ("allToLeft" == id) {
	    	cGrid.forEachRow(function(rowId) {
	    		var buttonName = cGrid.cells(rowId, 0).getValue();
	    		var showName   = cGrid.cells(rowId, 1).getValue();
	    		var buttonCode = cGrid.getUserData(rowId, "buttonCode");
	    		aGrid.addRow(buttonCode, buttonName);
	    		aGrid.setUserData(buttonCode, "buttonCode", buttonCode);
	    		aGrid.setUserData(buttonCode, "showName", showName);
	    	});
	    	cGrid.clearAll();
	    }
	    app.modified = true;
	});
	/** 删除配置.*/
	function clear() {
	   dhtmlx.confirm({
			type:"confirm",
			text: "确定要清空配置？",
			ok: "确定",
			cancel: "取消",
			callback: function(flag) {
				if (flag) {
		            var dUrl = moduleUrl + "!clear.json?P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId
		            		+ "&P_menuId=" + app.menuId + "&P_type=" + type;
					dhtmlxAjax.get(addTimestamp(dUrl), function(loader) {
						var rlt = eval("(" + loader.xmlDoc.responseText + ")");
						if (true == rlt.success) {
							cGrid.forEachRow(function(rowId) {
					    		var name  = cGrid.cells(rowId, 1).getValue();
					    		var buttonCode = cGrid.getUserData(rowId, "buttonCode");
					    		aGrid.addRow(buttonCode, name);
					    	});
					    	cGrid.clearAll();
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
	

	/** C布局区*/
	layoutC.hideHeader();
	
	var cGridCfig = {
			format: {
				headers: ["<center>按钮名称</center>","<center>显示名称</center>","<center>按钮样式</center>","<center>备注</center>"],
				cols:["buttonName","showName", "buttonClass","remark"],
				userdata:["buttonCode"],
				//userdata: [5],
				colWidths: ["120","160","*"],
				colTypes: ["ro","ed", "co","ed"],
				colAligns: ["left","left","left"]
			}
		};
	var cGrid = layoutC.attachGrid();
	var rcombo = cGrid.getCombo(2);
	var cData  = loadJson(contextPath + "/code/code!getCodeList.json?codeTypeCode=OPERATE_COLUMN_BUTTON_STYLE");
	if (cData && cData.data) {
		for (var i = 0, len = cData.data.length; i < len; i++) {
			rcombo.put(cData.data[i].value, cData.data[i].name);
		}
	}
	cGrid.enableDragAndDrop(true);
	var url = moduleUrl + "!defineButton.json?P_tableId=" + tableId + "&P_componentVersionId=" + componentVersionId 
			+ "&P_menuId=" + app.menuId + "&P_type=" + type;
	initGridWithoutPageable(cGrid, cGridCfig, url);

	cGrid.forEachRow(function(rowId) {
		var buttonCode = cGrid.getUserData(rowId, "buttonCode");
		aGrid.deleteRow(buttonCode);
	});
	cGrid.attachEvent("onDrop", function (sId, tId, dId, sObj, tObj, sCol, tCol) {
		app.modified = true;
	});
	cGrid.attachEvent("onEditCell", function (stage, rId, cInd, nValue, oValue) {
		if (nValue != oValue) app.modified = true;
		return true;
	});
	
	function getBoxButtons(box, type) {
		
		if ("0" === type)  return getFormButtons(box);
		
		return getGridButtons(box);
	}
	/**
	 * 工作箱对应的列表按钮
	 * @param box
	 * @returns {Array}
	 */
	function getGridButtons(box) {
		var btns = [];
		if ("applyfor" == box) {
			btns.push({id: "create", buttonCode: "create", buttonName:"新增", showName:"新增", remark:""});
			btns.push({id: "update", buttonCode: "update", buttonName:"修改", showName:"修改", remark:""});
			btns.push({id: "delete", buttonCode: "delete", buttonName:"删除", showName:"删除", remark:""});
			/*btns.push({id: "upload", buttonCode: "upload", buttonName:"电子全文上传", showName:"电子全文上传", remark:""});
			btns.push({id: "viewDocument", buttonCode: "viewDocument", buttonName:"查看附件", showName:"查看附件", remark:""});
			btns.push({id: "report", buttonCode: "report", buttonName:"报表", showName:"报表", remark:""});*/
			btns.push({id: "integrationSearch", buttonCode: "integrationSearch", buttonName:"一体化检索", showName:"一体化检索", remark:""});
			btns.push({id: "baseSearch", buttonCode: "baseSearch", buttonName:"基本检索", showName:"检索", remark:""});
		} else if ("todo" == box) {
			btns.push({id: "checkout", buttonCode: "checkout", buttonName:"签收", showName:"签收", remark:""});
			btns.push({id: "transact", buttonCode: "transact", buttonName:"办理", showName:"办理", remark:""});
			btns.push({id: "transactAndCheckout", buttonCode: "transactAndCheckout", buttonName:"签收并办理", showName:"签收并办理", remark:""});
			btns.push({id: "track", buttonCode: "track", buttonName:"跟踪", showName:"跟踪", remark:""});
			btns.push({id: "reassign", buttonCode: "reassign", buttonName:"转办", showName:"转办", remark:""});
			btns.push({id: "untread", buttonCode: "untread", buttonName:"退回", showName:"退回", remark:""});
			btns.push({id: "suspend", buttonCode: "suspend", buttonName:"中止（删除）", showName:"删除", remark:""});
			btns.push({id: "termination", buttonCode: "termination", buttonName:"终止", showName:"终止", remark:""});
			btns.push({id: "integrationSearch", buttonCode: "integrationSearch", buttonName:"一体化检索", showName:"一体化检索", remark:""});
			btns.push({id: "baseSearch", buttonCode: "baseSearch", buttonName:"基本检索", showName:"检索", remark:""});
		} else {
			btns.push({id: "view", buttonCode: "view", buttonName:"查看", showName:"查看", remark:""});
			if ("hasdone" == box) {
				btns.push({id: "recall", buttonCode: "recall", buttonName:"撤回", showName:"撤回", remark:""});
			}
			btns.push({id: "track", buttonCode: "track", buttonName:"跟踪", showName:"跟踪", remark:""});
			btns.push({id: "integrationSearch", buttonCode: "integrationSearch", buttonName:"一体化检索", showName:"一体化检索", remark:""});
			btns.push({id: "baseSearch", buttonCode: "baseSearch", buttonName:"基本检索", showName:"检索", remark:""});
		}
		
		return btns;
	}
	/**
	 * 工作箱对应的表单按钮
	 * @param box
	 * @returns {Array}
	 */
	function getFormButtons(box) {
		var btns = [];
		if ("applyfor" == box) {
			btns.push({id: "start", buttonCode: "start", buttonName:"启动", showName:"启动", remark:""});
			btns.push({id: "startAndComplete", buttonCode: "startAndComplete", buttonName:"启动并提交", showName:"启动并提交", remark:""});
			btns.push({id: "save", buttonCode: "save", buttonName:"暂存", showName:"暂存", remark:""});
		} else if ("todo" == box) {
			btns.push({id: "save", buttonCode: "save", buttonName:"暂存", showName:"暂存", remark:""});
			btns.push({id: "checkout", buttonCode: "checkout", buttonName:"签收", showName:"签收", remark:""});
			btns.push({id: "complete", buttonCode: "complete", buttonName:"提交", showName:"提交", remark:""});
			btns.push({id: "deliver", buttonCode: "deliver", buttonName:"传阅", showName:"传阅", remark:""});
			btns.push({id: "untread", buttonCode: "untread", buttonName:"退回", showName:"退回", remark:""});
			btns.push({id: "suspend", buttonCode: "suspend", buttonName:"中止（删除）", showName:"删除", remark:""});
			btns.push({id: "reassign", buttonCode: "reassign", buttonName:"转办", showName:"转办", remark:""});
			btns.push({id: "termination", buttonCode: "termination", buttonName:"终止", showName:"终止", remark:""});
		} else if ("hasdone" == box) {
			btns.push({id: "recall", buttonCode: "recall", buttonName:"撤回", showName:"撤回", remark:""});
		} else if ("toread" == box) {
			btns.push({id: "hasread", buttonCode: "hasread", buttonName:"阅毕", showName:"阅毕", remark:""});
		}
		
		return btns;
	}
}

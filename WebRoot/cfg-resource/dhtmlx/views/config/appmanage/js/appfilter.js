function initAppFilterWin(win, componentVersionId, f_close/*function*/) {	
	var initLayout = win.attachLayout("2E");
	var initLayoutA = initLayout.cells("a");
	initLayoutA.setText("SQL语句预览区");
	//initLayoutA.hideHeader();
	initLayoutA.setHeight(80);
	
	var initLayoutB = initLayout.cells("b");
	var dhxLayout = initLayoutB.attachLayout("3W");
	var moduleUrl = AppActionURI.appFilter;
	var sbar = dhxLayout.attachStatusBar();
	var btbar = new dhtmlXToolbarObject(sbar.id);
	btbar.setIconPath(TOOLBAR_IMAGE_PATH);
	btbar.addButton("bottom$clearAllGroup", 0, "清除所有组合", "delete.gif");
	btbar.addSeparator("bottom$septr$01", 1);
	btbar.addButton("bottom$preview", 2, "预览", "preview.gif");
	btbar.addButton("bottom$septr$02", 3);
	btbar.addButton("previewcheck", 4, "测试", "checkout.gif");
	btbar.addSeparator("botton$septr$03", 5);
	btbar.addButton("bottom$save", 6, "保存", "save.gif");
	btbar.addSeparator("botton$septr$04", 7);
	btbar.addButton("bottom$clear", 8, "清空配置", "delete.gif");
	btbar.addSeparator("botton$septr$05", 9);
	btbar.addButton("bottom$close", 10, "关闭", "close.gif");
	btbar.setAlign("right");
	btbar.attachEvent("onClick", function(itemId) {
		if("bottom$clearAllGroup" == itemId){
	    	var rowIds = rGrid.getAllRowIds();
	    	var idArr = rowIds.split(",");
	    	for(var i=0; i < idArr.length; i++){
	    		rGrid.cellById(idArr[i], 1).setValue("");
	    		rGrid.cellById(idArr[i], 5).setValue("");
	    	}
	    	app.modified = true;
		} else if ("bottom$preview" == itemId) {
			previewSQL();
		} else if ("bottom$close" == itemId) {
			if (typeof f_close == "function") f_close();
		} else if ("bottom$save" == itemId) {
			var cnt = rGrid.getRowsNum();
			if (0 == cnt) {
				dhtmlx.alert("请先配置，再保存！");
				return;
			}
			
			var rowsValue = "";
			for (var i = 0; i < cnt; i++) {				
				var rrowId = rGrid.getRowId(i);
				var showName = rGrid.cells(rrowId,2).getValue();
				var fType = rGrid.cells(rrowId,3).getValue();
				var value = rGrid.cells(rrowId,4).getValue();
				if(""==value){
					dhtmlx.alert("【" + showName + "】未设置值，请先设置值再保存");
					return;
				}
				var columnId = rGrid.getUserData(rrowId, "columnId");	
				var relation = rGrid.cells(rrowId, 0).getValue();
				var leftP = rGrid.cells(rrowId, 1).getValue();
				var rightP = rGrid.cells(rrowId, 5).getValue();
				var columnName = rGrid.getUserData(rrowId, "columnName");
				rowsValue += ";" + columnId + "," + relation + "," + leftP + "," + columnName + "," + fType + "," + value + "," +rightP;
			}
			rowsValue = rowsValue.substring(1);			
			var relSql = getRelSql(app.tableId, rGrid);
			var Check_Url = moduleUrl + "!checkSql.json?P_sql=" + relSql;
			dhtmlxAjax.get(encodeURI(Check_Url), function(loader) {
				var result = eval("(" + loader.xmlDoc.responseText + ")");
				if (result === true) {
					var url = moduleUrl + "!save.json?P_tableId=" + app.tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId + "&P_rowsValue=" + rowsValue;
					dhtmlxAjax.get(encodeURI(url),function(loader) {
						var rlt = eval("(" + loader.xmlDoc.responseText + ")");
						if (rlt && true == rlt.success) {
							dhtmlx.message(getMessage("operate_success"));
					    } else {
					    	dhtmlx.message(getMessage("operate_failure"));
					    }
						app.modified = false;
					});
			    } else {
			    	dhtmlx.message("配置存在语法错误，请检查！");
			    	return;
			    }
			});			
		} else if ("bottom$clear" == itemId) {
			dhtmlx.confirm({
				type:"confirm",
				text: "确定要清空配置？",
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
					if(flag){
						var url = moduleUrl + "!clear.json?P_tableId=" + app.tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId;
						dhtmlxAjax.get(url,function(loader){
							var rlt = eval("(" + loader.xmlDoc.responseText + ")");
							if (rlt && true == rlt.succuss) {
						    	rGrid.clearAll();
						    	initLayoutA.attachHTMLString("");
						    	dhtmlx.message(getMessage("operate_success"));
						    } else {
						    	dhtmlx.message(getMessage("operate_failure"));
						    }
							app.modified = false;
						});
					}
				}
			});
		} else if ("previewcheck" == itemId) {
			previewSQL();
			var relSql = getRelSql(app.tableId, rGrid);
			if(undefined != relSql) {
				var Check_Url = moduleUrl + "!checkSql.json?P_sql=" + relSql;
				dhtmlxAjax.get(encodeURI(Check_Url),function(loader) {
					var result = eval("(" + loader.xmlDoc.responseText + ")");
					if (result == true) {
						dhtmlx.message("语法测试通过！");
				    } else {
				    	dhtmlx.message("配置存在语法错误，请检查！");
				    }
				});
			}
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
	//ttbar.addInputText("top$search", 0,"字段检索(支持拼音)",190);
	ttbar.addDiv("top$searchdiv", 0);
	
	//ttbar.addInputCheck("top$check", 1,"a",30, true);
	//ttbar.attachEvent("onEnter", function(itemId, value) {
	//	if ("top$search" == itemId) {
	//		searchInGrid(lGrid, value, 0);
	//	}
	//});
	//ttbar.attachEvent("onFocus", function(itemId) {
	//	if ("top$search" == itemId) {
	//		ttbar.setValue(itemId, "");
	//	}
	//});
	var sform = initSearchArea();
	//cLayout.setText("操作区");
	
	cLayout.setWidth(115);
	cLayout.hideHeader();
	//rLayout.setText("检索字段");
	//rLayout.setWidth(365);
	rLayout.hideHeader();

	var lcfg = {
			format: {
				headers: ["<center>字段名称</center>"],
				cols: ["showName"],
				userdata: ["columnId", "columnName", "dataType", "codeTypeCode"],
				colWidths: ["200"],
				colTypes: ["ro"],
				colAligns: ["left"]
			}
		};
	var rcfg = {
			format: {
				headers: ["<center>关系</center>","<center>左括号</center>","<center>字段名称</center>","<center>过滤条件</center>","<center>值</center>","<center>右括号</center>",""],
				cols: ["relation", "leftParenthesis", "showName", "filterType", "value", "rightParenthesis"],
				id  : ["id"],
				userdata: ["columnId", "columName", "dataType", "codeTypeCode"],
				colWidths: ["60", "60", "120", "60", "80", "60","*"],
				colTypes: ["co", "ed", "ro", "co","ed", "ed", "ro"],
				colAligns: ["center", "right", "right", "center", "left", "left", "center"]
			}
		};
	var lGrid = dhxLayout.cells("a").attachGrid();	
	var rGrid = dhxLayout.cells("c").attachGrid();
	rGrid.enableDragAndDrop(true);
	var rcombo = rGrid.getCombo(0);
	rcombo.put("AND","并且");
	rcombo.put("OR","或者");
	var mcombo = rGrid.getCombo(3);
	mcombo.put("LIKE","包含");
	mcombo.put("EQ","等于");
	mcombo.put("GT","大于");
	mcombo.put("LT","小于");
	mcombo.put("NOT","不等于");
	mcombo.put("GTE","大等于");
	mcombo.put("LTE","小等于");
	var params = "E_model_name=datagrid&E_frame_name=coral&P_tableId=" + app.tableId + "&P_componentVersionId=" + componentVersionId + "&P_menuId=" + app.menuId;
	var lurl = moduleUrl + "!defaultColumn.json?" + params;
	var rurl = moduleUrl + "!defineColumn.json?" + params;
	initGridWithoutPageable(lGrid, lcfg, lurl);
	initGridWithoutPageable(rGrid, rcfg, rurl);
	for(var i=0; i<rGrid.getRowsNum();i++){
		rId = rGrid.getRowId(i);
		var type = rGrid.getUserData(rId, "dataType");
		var codeTypeCode = rGrid.getUserData(rId, "codeTypeCode");
		if ("d" == type) {
			var cellObj = rGrid.cellById(rId, 4);
			cellObj.grid.setDateFormat("%Y-%m-%d");
			rGrid.setCellExcellType(rId,4,"dhxCalendar");
		}
		if ("u" == type || "e" == type || "p" == type) {
			var url = moduleUrl + "!selectOption.json?P_type=" + type +"&P_codeTypeCode=" + codeTypeCode;
			var opts = loadJson(encodeURI(url));	
			rGrid.setCellExcellType(rId, 4, "co");	
			var combo = rGrid.getCustomCombo(rId, 4);
			var cell = rGrid.cellById(rId, 4);
			var value = cell.getValue();
			for(var j = 0; j < opts.length; j++){
				combo.put(opts[j].value, opts[j].text);
			}
			cell.setValue(value);
		}
	}
	
	var preview = getPreviewSql(rGrid, true);
	if(undefined != preview){
		initLayoutA.attachObject(createPreviewDiv(preview, initLayoutA.getHeight() - 30));
	}
	var erowId = "";
	var ecInd = "";
	rGrid.attachEvent("onEditCell", function(stage,rId,cInd,nValue,oValue){
		erowId = rId;
		ecInd = cInd;		
		var type = rGrid.getUserData(erowId, "dataType");
		var codeTypeCode = rGrid.getUserData(erowId, "codeTypeCode");
		if("4" == cInd){			
			if("d" == type && "1"==stage){			
				var dcellObj = rGrid.cellById(rId, cInd);
				dcellObj.grid.setDateFormat("%Y-%m-%d");
				rGrid.setCellExcellType(rId,cInd,"dhxCalendar");
			} else if(("u" == type || "e" == type || "p" == type) && "1"==stage){
				rGrid.setCellExcellType(rId,cInd,"co");	
				var combo = rGrid.getCustomCombo(rId, cInd);
				if (combo.size() == 0) {
					var url = moduleUrl + "!selectOption.json?P_type=" + type +"&P_codeTypeCode=" + codeTypeCode;
					var opts = loadJson(encodeURI(url));
					combo.clear();
					for(var i = 0; i < opts.length; i++){
						combo.put(opts[i].value, opts[i].text);
					}
				}
			}
			if("2" == stage && "" == rGrid.cells(rId, cInd).getValue()){
				rGrid.cellById(rId, cInd).setValue(oValue);
			}
		}
		if (nValue != oValue) app.modified = true;
		return true;
	});
	rGrid.attachEvent("onDrop", function (sId, tId, dId, sObj, tObj, sCol, tCol) {
		//var sIdx = rGrid.getRowIndex(sId);
		if ("" == rGrid.cells(sId, 0).getValue()) {
			rGrid.cells(sId, 0).setValue("AND");
		}
		if ("" != rGrid.cellByIndex(0, 0).getValue()) {
			rGrid.cellByIndex(0, 0).setValue("");
		}
		app.modified = true;
	});
	rGrid.attachEvent("onKeyPress", function(code,cFlag,sFlag){	
		if("1" == ecInd){
			if((code == 57 && sFlag) || (code == 8 || code == 46)){
				return true;
			}
		} else if("5" == ecInd){
			if((code == 48 && sFlag) || (code == 8 || code==46)){
				return true;
			}
		} else if("4" == ecInd){
			var type = rGrid.getUserData(erowId,"dataType");
			if("n" == type){
				if(!sFlag){
					if((code > 95 && code < 106) || (code > 45 && code < 58) || (code > 36 && code < 41) || code == 8){
						return true;
					}else if(("" == rGrid.cells(erowId, 4).getValue()) && (code == 109 || code == 189)){
						return true;					
					}
				}	
			} else {
				return true;
			}  				
		} 
		return false;
	});	

	lGrid.attachEvent("onRowDblClicked", function(rowId, cInd){
		if (null == rowId) return;
		// add row in right grid
		var columId = lGrid.getUserData(rowId, "columId");
		var columnName = lGrid.getUserData(rowId, "columnName");
		var dataType = lGrid.getUserData(rowId, "dataType");
		var codeTypeCode = lGrid.getUserData(rowId, "codeTypeCode");
		var rrowId = lGrid.uid(); 
		var cnt = rGrid.getRowsNum();
		rGrid.addRow(rrowId, [(0 == cnt ? "" : "AND"), "", lGrid.cells(rowId,0).getValue(), "EQ", "", ""]);
		rGrid.setUserData(rrowId, "columnId", columId);
		rGrid.setUserData(rrowId, "columnName", columnName);
		rGrid.setUserData(rrowId, "dataType", dataType);
		rGrid.setUserData(rrowId, "codeTypeCode", codeTypeCode);
		app.modified = true;
	});	
	createEmptyDiv("DIV-oparatorArea-filter", 33);	
	var formJson = getMoveButton();
	formJson[0].list.push({
		type: "button",
		name: "group",
		value: "组合",
		width: 60
	});
	formJson[0].list.push({
		type: "button",
		name: "split",
		value: "拆分",
		width: 60
	});
	var buttonForm = new dhtmlXForm("DIV-oparatorArea-filter", formJson);	
	dhxLayout.cells("b").attachObject("DIV-oparatorArea-filter");	
	buttonForm.attachEvent("onButtonClick", function(id){
	    if ("toRight" == id) {
	    	var rowIds = lGrid.getSelectedRowId();
	    	if (null == rowIds) return;
	    	var idArr = rowIds.split(",");
	    	for (var i = 0; i < idArr.length; i++) {
	    		var rowId = idArr[i];
	    		var name  = lGrid.cells(rowId, 0).getValue();
	    		var columnId = lGrid.getUserData(rowId, "columnId");
	    		var columnName = lGrid.getUserData(rowId, "columnName");
	    		var dataType = lGrid.getUserData(rowId, "dataType");
	    		var codeTypeCode = lGrid.getUserData(rowId, "codeTypeCode");
	    		var cnt = rGrid.getRowsNum();
	    		var rRowId = rGrid.uid();
	    		rGrid.addRow(rRowId, [(0 == cnt ? "" : "AND"), "", name, "EQ", "", ""]);
	    		rGrid.setUserData(rRowId, "columnId", columnId);
	    		rGrid.setUserData(rRowId, "columnName", columnName);
	    		rGrid.setUserData(rRowId, "dataType", dataType);
	    		rGrid.setUserData(rRowId, "codeTypeCode", codeTypeCode);
	    	}
	    	//lGrid.deleteSelectedRows();
	    } else if ("allToRight" == id) {
	    	lGrid.forEachRow(function(rowId) {
	    		var name  = lGrid.cells(rowId,0).getValue();
	    		var columnId = lGrid.getUserData(rowId, "columnId");
	    		var columnName = lGrid.getUserData(rowId, "columnName");
	    		var dataType = lGrid.getUserData(rowId, "dataType");
	    		var codeTypeCode = lGrid.getUserData(rowId, "codeTypeCode");
	    		var cnt = rGrid.getRowsNum();
	    		var rRowId = rGrid.uid();
	    		rGrid.addRow(rRowId, [(0 == cnt ? "" : "AND"), "", name, "EQ", "", ""]);
	    		rGrid.setUserData(rRowId, "columnId", columnId);
	    		rGrid.setUserData(rRowId, "columnName", columnName);
	    		rGrid.setUserData(rRowId, "dataType", dataType);
	    		rGrid.setUserData(rRowId, "codeTypeCode", codeTypeCode);
	    	});
	    	//lGrid.clearAll();
	    } else if ("toLeft" == id) {
	    	rGrid.deleteSelectedRows();
	    } else if ("allToLeft" == id) {
	    	rGrid.clearAll();
	    } else if ("group" == id){
	    	var rowIds = rGrid.getSelectedRowId();
	    	if (null == rowIds){
	    		dhtmlx.alert("请在右边列表中选择要组合的行！");
	    		return;
	    	} 
	    	var newIdArr = sortIdArr(rGrid, rowIds);
	    	var leftP = rGrid.cells(newIdArr[0],1).getValue();
	    	var rightP = rGrid.cells(newIdArr[newIdArr.length - 1], 5).getValue();
	    	for(var i=0; i<newIdArr.length; i++){
	    		if(i == 0){
	    			rGrid.cellById(newIdArr[i], 1).setValue(leftP + "(");	
	    		} else {
	    			rGrid.moveRowTo(newIdArr[i], newIdArr[i-1], "move");
	    		}
	    		if(i == newIdArr.length-1)rGrid.cellById(newIdArr[i], 5).setValue(rightP + ")");
	    	}
	    } else if ("split" == id) {
	    	var rowIds = rGrid.getSelectedRowId();
	    	if (null == rowIds){
	    		dhtmlx.alert("请在右边的列表中选择要拆分的组合！");
	    		return;
	    	} 
	    	var idArr = sortIdArr(rGrid, rowIds);
	    	var leftPValue = rGrid.cells(idArr[0], 1).getValue();
	    	var rightPValue = rGrid.cells(idArr[idArr.length - 1], 5).getValue();
	    	if("-1" == leftPValue.indexOf("(") || "-1" == rightPValue.indexOf(")")){
	    		dhtmlx.alert("请选择正确的组合后，再拆分！");
	    		return;
	    	} else {
	    		rGrid.cellById(idArr[0], 1).setValue(leftPValue.substring(1));
		    	rGrid.cellById(idArr[idArr.length - 1], 5).setValue(rightPValue.substring(0,rightPValue.lastIndexOf(")")));
	    	}	    	
	    } 
	    app.modified = true;
	});
	
	
	/**
	 * 字段检索
	 * @returns {dhtmlXForm}
	 */
	function initSearchArea() {
		var sformJson = [{type: "input", name: "searchcolumn", className: "dhx_toolbar_form", value: "字段检索(支持拼音)", width:195, inputHeight:17}];
		var form = new dhtmlXForm("top$searchdiv",sformJson);
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
	/*预览*/
	function previewSQL() {
		initLayoutA.attachHTMLString("");
		var preview = getPreviewSql(rGrid);
		if(undefined != preview){
			initLayoutA.attachObject(createPreviewDiv(preview, initLayoutA.getHeight() - 30));
		}
	}
	
}

function getFilterItem(column, key, value, isPreview) {
	var space = " ";
	if (isPreview === true) {
		space = "&nbsp;&nbsp;&nbsp;&nbsp;";
		if ("包含" == key) return column + space + key + space + "'%" + value + "%'";
		return column + space + key + space + "'" + value + "'";
	}
	if ("LIKE" == key) return column + space + "LIKE" + space + "'%" + value + "%'";
	if ("EQ" == key) return column + space + "=" + space + "'" + value + "'";
	if ("GT" == key) return column + space + ">" + space + "'" + value + "'";
	if ("LT" == key) return column + space + "<" + space + "'" + value + "'";
	if ("NOT" == key) return column + space + "<>" + space + "'" + value + "'";
	if ("GTE" == key) return column + space + ">=" + space + "'" + value + "'";
	if ("LTE" == key) return column + space + "<=" + space + "'" + value + "'";
	return column + space + key + space + "'" + value + "'";
}

function getRelSql(tableId, rGrid){
	var cnt = rGrid.getRowsNum();
	if (0 == cnt) {
		dhtmlx.alert("请先进行列表过滤条件配置，再测试！");
		return;
	}
	var url     = AppActionURI.physicalTableDefine + "/" + tableId + ".json";
	var loader  = dhtmlxAjax.getSync(url);
	var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
	var relSql  = "  SELECT  count(*)  FROM  " + jsonObj.tableName + "  WHERE  1 <> 1 AND";			
	for(var i = 0; i < cnt; i++) {
		var rowId = rGrid.getRowId(i);
		if("" == rGrid.cells(rowId, 4).getValue()) {
			dhtmlx.alert("【" + rGrid.cells(rowId, 2).getValue() + "】未设置值，请先设置值再测试！");
			return;
		}
		var relation = (0 == i) ? "" : (" " + rGrid.cells(rowId, 0).getValue());
		var leftP    = rGrid.cells(rowId, 1).getValue();
		var column   = rGrid.getUserData(rowId, "columnName");
		var filter   = rGrid.cells(rowId, 3).getValue();
		var value    = rGrid.cells(rowId, 4).getValue();
		var rightP   = rGrid.cells(rowId, 5).getValue();	
		relSql += relation + " " + leftP + getFilterItem(column, filter, value) +  rightP; 
	}
	return relSql;
}
function  getPreviewSql(rGrid, isInit) {
	var cnt = rGrid.getRowsNum();
	if(0 == cnt){
		if(isInit){
			return "";
		} 
		dhtmlx.alert("请先进行列表过滤条件配置，再预览！");
		return;			
	}
	
	var _4space = "&nbsp;&nbsp;&nbsp;&nbsp;";
	var rowId = rGrid.getRowId(0);
	var showName = rGrid.cells(rowId, 2).getValue();
	if("" == rGrid.cells(rowId, 4).getValue()){
		dhtmlx.alert("【" + showName + "】未设置值，请先设置值再预览！");
		return;
	}
	var previewSql = "SELECT" + _4space + "*" + _4space + "FROM" + _4space + app.tableComment + _4space + "WHERE";		
	for (var i = 0; i < cnt; i++){
		var rowId = rGrid.getRowId(i);		
		if ("" == rGrid.cells(rowId, 4).getValue()) {
			dhtmlx.alert("【" + rGrid.cells(rowId, 2).getValue() + "】未设置值，请先设置值再预览！");
			return;
		}
		var type = rGrid.getUserData(rowId, "dataType");
		if("u" == type || "e" == type || "p" == type){
			value = rGrid.cells(rowId, 4).getText();
		} else {
			value = rGrid.cells(rowId, 4).getValue(); 
		}
		var relation = (0 == i) ? "" : (_4space + rGrid.cells(rowId, 0).getText());
		var leftP    = rGrid.cells(rowId, 1).getValue();
		var name     = rGrid.cells(rowId, 2).getValue();
		var filter   = rGrid.cells(rowId, 3).getText();
		var rightP   = rGrid.cells(rowId, 5).getValue();	
		previewSql += relation + _4space + leftP + getFilterItem(name, filter, value, true) +  rightP; 
	}
	return previewSql + ";";
}

function sortIdArr(rGrid, rowIds){
	var idArr = rowIds.split(",");
	var indexArr = [];
	var newIdArr = [];
	for (var i=0; i < idArr.length; i++){
		indexArr.push(rGrid.getRowIndex(idArr[i]));
	}
	indexArr.sort(compareInt);
	for(var j = 0; j < indexArr.length; j++){
		newIdArr.push(rGrid.getRowId(indexArr[j]));
	}
	return newIdArr;
}

function compareInt(int1, int2){
    var iNum1 = parseInt(int1);   
    var iNum2 = parseInt(int2);
    if(iNum1 < iNum2){
        return -1;
    }else if(iNum1 > iNum2){
        return 1;
    }else{
        return 0;
    }
}
function createPreviewDiv(preview, height) {
	var obj = document.getElementById("DIV-preview");
	if (null == obj) {
		obj = document.createElement("DIV");
		obj.setAttribute("id", "DIV-preview");
		obj.setAttribute("style", "top:10px;left:5px;line-height:20px;font-family: Tahoma;font-size: 11px;color: blue;display: none;overflow-y:auto; overflow-x:auto;height:" + height + "px;");
		obj.innerHTML = preview;
	}
	return obj;
}



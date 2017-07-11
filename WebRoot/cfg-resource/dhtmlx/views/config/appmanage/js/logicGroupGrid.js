/**
 * 
 * @param that
 * @param layout
 */
function initLogicGroupGrid(that, layout) {
	layout.hideHeader();

	var _this = this;
	var moduleUrl = AppActionURI.logicGroupDefine;
	var ttbar = layout.attachToolbar();
	var grid = layout.attachGrid();
	var ttbarIndex = 1;
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("add", ttbarIndex++, "新增", "new.gif");
	ttbar.addSeparator("septr$01", ttbarIndex++);
	ttbar.addButton("delete", ttbarIndex++, "删除", "delete.gif");
	ttbar.addSeparator("septr$02", ttbarIndex++);
	ttbar.addButton("physical", ttbarIndex++, "生成物理表", "copy.gif");
    ttbar.addSeparator("septr$03", ttbarIndex++);
    ttbar.showItem("physical");
	ttbar.showItem("septr$03");
	layout.showToolbar();
	ttbar.showItem("add");
	ttbar.showItem("septr$01");
	ttbar.showItem("delete");
	ttbar.showItem("septr$02");
	ttbar.attachEvent("onClick", function(id) {
		if ("add" == id) {
			if (grid.getRowId(0) == '') {
				return;
			}
			grid.addRow('', '', 0);
			grid.cells('', 0).open();
		} else if ("delete" == id) {
			deleteById();
		} else if ("physical" == id) {
			var rowId = grid.getSelectedRowId();
			if (null == rowId || "" == rowId) {
				dhtmlx.message(getMessage("select_record"));
				return;
			} 
			if (rowId.split(",").length > 1) {
				dhtmlx.message(getMessage("select_only_one_record"));
				return;
			}
			var groupCode = grid.cellById(rowId, 2).getValue();
			var groupData = loadJson(AppActionURI.logicGroupRelation + "!getLogicTablesByGroupCode.json?P_groupCode=" + groupCode);
			if (!dhxWins) {
		        dhxWins = new dhtmlXWindows();
		    }
		    var winWidth = 700;
		    var winHeight = 300;
		    dataWin = dhxWins.createWindow(WIN_ID, 0, 0, winWidth, winHeight);
		    dataWin.setModal(true);
		    dataWin.setText("生成物理表");
		    dataWin.center();
		    var formFormat = initFormFormat(getLogicGroupJson(groupData));
		    var logicGroupFormGrid = dataWin.attachForm(formFormat);
		    if (initLogicGroupFormToolbar && typeof initLogicGroupFormToolbar == "function") {
		        var statusBar = dataWin.attachStatusBar();
		        var toolBar = new dhtmlXToolbarObject(statusBar);
		        initLogicGroupFormToolbar.call(this, toolBar, groupData, logicGroupFormGrid);
		    }
			logicGroupFormGrid.attachEvent("onButtonClick", function(buttonName){
				for ( var i = 0; i < groupData.length; i++) {
					if (groupData[i].code == buttonName) {
						if (!dhxWins) {
					        dhxWins = new dhtmlXWindows();
					    }
					    var winWidth = 300;
					    var winHeight = 500;
					    dataWin = dhxWins.createWindow(groupData[i].code, 0, 0, winWidth, winHeight);
					    dataWin.setModal(true);
					    dataWin.setText("选择");
					    dataWin.center();
					    var tree = dhxWins.window(groupData[i].code).attachTree();
					    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
						tree.attachEvent("onMouseIn", function(id) {
							tree.setItemStyle(id, "background-color:#D5E8FF;");
						});
						tree.attachEvent("onMouseOut", function(id) {
							tree.setItemStyle(id, "background-color:#FFFFFF;");
						});
						// 物理表分类节点
						var ptItem = loadJson(AppActionURI.tableClassification + "!treeNode.json");
						that.prefixObj = {};
						for (var i = 0; i < ptItem.length; i++){
							var item = ptItem[i];
							that.prefixObj[item.userdata[1].content] = item.prefix;
						}
						var treeJson = {id:0, item:[
							{id:"-PT", text: "物理表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, child:"1", item: ptItem}
						]};
						tree.enableRadioButtons(true);
						tree.enableSingleRadioMode(true,"-PT");
						tree.setDataMode("json");
						tree.enableSmartXMLParsing(true);
						tree.setXMLAutoLoading(AppActionURI.tableTree+"!getPhysicalTree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,child&P_UD=type,classification,tableLabel");
						tree.loadJSONObject(treeJson);
					    var statusBar = dataWin.attachStatusBar();
				        var toolBar = new dhtmlXToolbarObject(statusBar);
				        initPhysicalTreeAreaToolbar.call(this, toolBar, tree, buttonName, logicGroupFormGrid);
					}
				}
			});
		}
	});
	var gcfg = {
		format : {
			headers : [ "&nbsp;", "<center>逻辑表组名称</center>", "<center>逻辑表组代码</center>", "<center>状态</center>", "<center>备注</center>", "" ],
			cols : [ "id", "groupName", "code", "status", "remark", "*" ],
			colWidths : [ "30", "200", "150", "150", "250", "*" ],
			colTypes : [ "sub_row_form", "ro", "ro", "co", "ro", "ro" ],
			colAligns : [ "right", "left", "left", "left" ,"left" ]
		}
	};
	
	var statusCombo = grid.getCombo(3);
	statusCombo.put("0","未应用");
	statusCombo.put("1","已应用");

	var gurl = getGridUrl();
	grid.enableDragAndDrop(true);
	grid.enableTooltips("false,false,false,false");
	initGridWithoutPageable(grid, gcfg, gurl);
	/** 列表拖拽调整顺序 */
	grid.attachEvent("onDrag", function(sIds, tId) {
		// 判断选中的记录是否连续，如果不连续，则不能调整顺序
		if (sIds.indexOf(",") != -1) {
			var idArr = sIds.split(",");
			var preIdx = null, curIdx = null;
			for ( var i = 0; i < idArr.length; i++) {
				curIdx = grid.getRowIndex(idArr[i]);
				if (null == preIdx) {
					preIdx = curIdx;
					continue;
				}
				if (Math.abs(curIdx - preIdx) != 1) {
					dhtmlx.message("请选择连续的记录进行调整顺序！");
					return false;
				}
				preIdx = curIdx;
			}
		}
		return true;
	});

	function getFormJson() {
		return [
    				{type: "block", width: "500", list:[
                        {type: "hidden", name: "_method"},
						{type: "hidden", name: "id"},
						{type: "hidden", name: "showOrder"},
						{type: "block", name: "block_table", width:700, list:[
							{type: "input", label: "逻辑表组名称：", labelWidth:120, name: "groupName", required:true, width:170, maxLength:24},
							{type: "newcolumn"},
							{type: "input", label: "逻辑表组编码：", labelWidth:120, name: "code", required:true, width:170, maxLength:24, offsetLeft:20}                      			 				
						    ]},
			 			{type: "block", width:790, list:[
							{type: "input", label: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;注：", name: "remark",labelWidth:120,width:480, maxLength:24, rows:3},
							{type: "newcolumn"},
							{type: "button", name: "save", value: "保存", width:80, offsetTop:30, offsetLeft:10}
						]}
					]}
				];
	}
	
	grid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue) {
		return;
	});

	grid.attachEvent("onRowSelect", function(rId, cInd) {
		grid.cells(rId, 0).open();
	});

	grid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
		subform.c = getFormJson();
	});

	grid.attachEvent("onSubFormLoaded", function(subform, id, index) {

		var MODEL_URL = AppActionURI.logicGroupDefine;
		if (id != "") {
			var url = MODEL_URL + "/" + id + ".json?_method=get";
			loadForm(subform, url);
			var data = loadJson(url);
			if (null == data) {
				return;
			}
			subform.setFormData(data);
		}

		subform.attachEvent("onButtonClick", function(buttonName) {
			if (buttonName == "save") {
				var id = subform.getItemValue("id");
				var groupName = subform.getItemValue("groupName");
				var code = subform.getItemValue("code");
				if (null == groupName || "" == groupName) {
					dhtmlx.alert("逻辑表组名称不可为空！");
					return;
				}
				if (null == code || "" == code) {
					dhtmlx.alert("逻辑表编码不可为空！");
					return;
				}
				code = code.toUpperCase();
				if (!code.match(/([\w\_])+/)) {
					dhtmlx.alert("逻辑表编码只能由字母、数字、下划线组成！");
					return;
				}
				var checkUrl = moduleUrl + "!checkUnique.json?id=" + id + "&Q_EQ_code=" + code;
				dhtmlxAjax.get(checkUrl, function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					if (false == jsonObj.success) {
						if ("OK" == jsonObj.message) {
							dhtmlx
									.message(getMessage("form_field_exist","表名称"));
						} else {
							dhtmlx.message("唯一检查出错，请联系管理员！");
						}
					} else {// */
						submit(id);
					}
				});// */
			}
		});

		/** 表单提交 */
		function submit(id) {
			var url = moduleUrl;
			var groupName = subform.getItemValue("groupName");
			var remark = subform.getItemValue("remark");
			if (id) { // modify
				subform.setItemValue("_method", "put");
				url += "/" + id + ".json";
			} else { // create
				subform.setItemValue("_method", "post");
				url += ".json";
			}
			subform.send(url, "post", function(loader, response) {
				var formData = eval("(" + loader.xmlDoc.responseText + ")");
				if (formData.id == null || formData.id == "") {
					dhtmlx.message(getMessage("save_failure"));
					return;
				}
				subform.setFormData(formData);
				that.reloadGrid();
				that.reloadTreeItem();
				dhtmlx.message(getMessage("save_success"));
			});
		}
	});

	grid.attachEvent("onSubRowOpen", function(id, expanded) {
		if (expanded) {
			grid.forEachRow(function(rId) {
				if (id != rId) {
					grid.cells(rId, 0).close();
				}
			});
		}
	});

	grid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj, sCol, tCol) {
		if (undefined != tId) {
			var adjustUrl = moduleUrl + "!adjustShowOrder?P_sourceIds=" + sId + "&P_targetId=" + tId;
			dhtmlxAjax.get(adjustUrl, function(loader) {
				that.reloadGrid();
				grid.selectRowById(sId);
				that.reloadTreeItem();
			});
		}
	});

	/** 列表刷新 */
	that.reloadGrid = function() {
		loadGridData(grid, gcfg, getGridUrl());
	};
	/** 列表工具条按钮 */
	that.buttonGrid = function() {
		layout.showToolbar();
		ttbar.showItem("add");
		ttbar.showItem("septr$01");
		ttbar.showItem("delete");
		ttbar.showItem("septr$02");
	};
	/** 列表查询地址 */
	function getGridUrl() {
		return moduleUrl + "!search.json?P_orders=showOrder";
	};
	/** 动态生成生成物理表表单 */
	function getLogicGroupJson(groupData){
		var logicGroupJson = {
			format: [
			    {type: "hidden", name: "logicGroupCode"},     
			    {type: "block", width: "700", list:[
				    {type: "input", label: "物理表组名称：", labelWidth: "120", name: "groupName", maxLength:100, required: true, width: 200},
				    {type: "newcolumn"},
				    {type: "input", label: "物理表组编码：", labelWidth: "120", name: "code", maxLength:100, required: true, width: 200}
				]},
				{type: "block", name: "block_table", width:700, list:[
					{type: "itemlabel", label: "逻辑表", labelWidth:100},
					{type: "newcolumn"},
					{type: "itemlabel", label: "显示名称　　", labelWidth:100, offsetLeft:20},  
					{type: "newcolumn"},
	  				{type: "itemlabel", label: "表名称　　　　", labelWidth:140, offsetLeft:20},
					{type: "newcolumn"},
	 				{type: "itemlabel", label: "所属分类　　", labelWidth:100, offsetLeft:20}
	 			]},
			]
		};
		for ( var i = 0; i < groupData.length; i++) {
			logicGroupJson.format.push(
				{type: "block", width: "700", list:[
					{type: "hidden", name: "tableType" + groupData[i].code, value:"0"},
					{type: "hidden", name: "tableTreeId" + groupData[i].code},
					{type: "hidden", name: "created" + groupData[i].code,value:"0"},
					{type: "hidden", name: "classification" + groupData[i].code},
					{type: "hidden", name: "logicTableCode" + groupData[i].code, value:groupData[i].code},
					{type: "hidden", name: "showOrder" + groupData[i].code, value:groupData[i].showOrder},
  					{type: "itemlabel", label: groupData[i].showName, labelWidth:100, width:80},
  					{type: "newcolumn"},
  					{type: "input", name: "showName" + groupData[i].code, width:100, required: true, offsetLeft:20},  
  					{type: "newcolumn"},
	  				{type: "input", name: "tablePrefix" + groupData[i].code, width:40, offsetLeft:20},
					{type: "newcolumn"},
	 				{type: "input", name: "tableCode" + groupData[i].code, width:100},
					{type: "newcolumn"},
					{type: "input", name: "tableTreeText" + groupData[i].code, maxLength:80, required: true, width: 100, offsetLeft:20},
                    {type: "newcolumn"},
                    {type: "button", name: groupData[i].code, value: "选择", width: 50, offsetLeft:20}
				]}
			);
		}
		return logicGroupJson;
	}
	
	/** 初始化生成物理表工具条*/
	function initLogicGroupFormToolbar(toolBar, groupData, logicGroupFormGrid) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window(WIN_ID).close();
			} else if (id == "save") {
				var rowId = grid.getSelectedRowId();
				var code = grid.cellById(rowId, 2).getValue();
				logicGroupFormGrid.setItemValue("logicGroupCode", code);
				var groupName = logicGroupFormGrid.getItemValue("groupName");
				if (null == groupName || "" == groupName) {
					dhtmlx.alert("物理表组名不可为空！");
					return;
				}
				for ( var i = 0; i < groupData.length - 1; i++) {
					var tableCodeI = groupData[i].code;
					var tableNameI = logicGroupFormGrid.getItemValue("tablePrefix" + tableCodeI) + logicGroupFormGrid.getItemValue("tableCode" + tableCodeI);
					for ( var j = i + 1; j < groupData.length; j++) {
						var tableCodeJ = groupData[j].code;
						var tableNameJ = logicGroupFormGrid.getItemValue("tablePrefix" + tableCodeJ) + logicGroupFormGrid.getItemValue("tableCode" + tableCodeJ);
						if (tableNameI == tableNameJ) {
							dhtmlx.alert("新建的物理表表名称不能相同!");
							return;
						}
					}
				}
				var checkUrl = AppActionURI.physicalGroupDefine + "!checkUnique.json?Q_EQ_groupName=" + groupName;
				dhtmlxAjax.get(checkUrl,function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					if (false == jsonObj.success) {
						if ("OK" == jsonObj.message) {
							dhtmlx.message(getMessage("form_field_exist", "表名称"));
						} else {
							dhtmlx.message("唯一检查出错，请联系管理员！");
						}
					} else {//*/
						submit();
					}
				});//*/
			}
			/** 表单提交*/
			function submit() {
				var url = AppActionURI.physicalTableDefine;
				var tableNames;
				var logicTableCodes;
				for ( var i = 0; i < groupData.length; i++) {
					var tableCode = groupData[i].code;
					tableNames += "," + logicGroupFormGrid.getItemValue("tablePrefix" + tableCode) + logicGroupFormGrid.getItemValue("tableCode" + tableCode);
				}
				for ( var i = 0; i < groupData.length; i++) {
					logicTableCodes += "," + groupData[i].code;
				}
				var checkUrl = AppActionURI.physicalTableDefine + "!checkAllUnique.json?P_tableNames" + tableNames;
				var result = loadJson(checkUrl);
				if (result.success == false) {
					dhtmlx.alert(result.message);
				} else {
					logicGroupFormGrid.send(AppActionURI.physicalGroupDefine + "!saveAll.json", "post", function(loader, response) {
						var formData = eval("(" + loader.xmlDoc.responseText + ")");
//						if (formData.id == null || formData.id == "") {
//							dhtmlx.message(getMessage("save_failure"));
//							return;
//						}
//						logicGroupFormGrid.setFormData(formData);
//						that.reloadGrid();
//						that.reloadTreeItem();
//						var code = grid.cellById(rowId, 2).getValue();
//						//复制表的操作
//						var url = AppActionURI.columnDefine + "!copyFileds2newTableByLogicTable.json?logicTable_code="+code+"&newTable_id="+formData.id+"";
//						dhtmlxAjax.get(url,function(loader){
//							var rlt = eval("(" + loader.xmlDoc.responseText + ")");
//							if ("" != rlt.message) {
//								dhtmlx.message(rlt.message);
//								return;
//							}
//							dhtmlx.message(getMessage("save_success"));
//							dhxWins.window(WIN_ID).close();
//						});
						alert(formData);
					});
					logicGroupFormGrid.setItemValue("_method", "post");
					url += ".json";
					checkUrl += "!checkUnique.json?Q_EQ_tableTreeId=" + tableTreeId + "&Q_EQ_tableCode=" + tableCode;
				}
			}
		});
	}
	
	/** 初始化物理表树选择工具条*/
	function initPhysicalTreeAreaToolbar(toolBar, tree, code, logicGroupFormGrid) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;确认&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;取消&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window(code).close();
			} else if (id == "save") {
				var tableTreeId = tree.getAllChecked();
				if (tableTreeId == "-PT") {
					dhtmlx.alert("请选择一个具体的分类!");
					return;
				}
				var tableTreeText = tree.getItemText(tableTreeId);
				logicGroupFormGrid.setItemValue("tableTreeId" + code,tableTreeId);
				logicGroupFormGrid.setItemValue("tableTreeText" + code,tableTreeText);
				logicGroupFormGrid.setItemValue("classification" + code,tree.getUserData(tableTreeId,"classification"));
				logicGroupFormGrid.setItemValue("tablePrefix" + code,that.prefixObj[tree.getUserData(tableTreeId,"classification")]);
				dhxWins.window(code).close();
			}
		});
	}
	
	/** 单条删除记录 */
	function deleteById() {
		var rowIds = grid.getSelectedRowId();
		if (null == rowIds || "" == rowIds) {
			dhtmlx.message(getMessage("select_record"));
			return;
		}
		if (rowIds.split(",").length > 1) {
			dhtmlx.message(getMessage("select_only_one_record"));
			return;
		}
		//验证逻辑表组是否存在逻辑表关联关系
		var code = grid.cells(rowIds,2).getValue();
		if (grid.cells(rowIds,3).getValue() == "1") {
			dhtmlx.message("该逻辑表组已被构件应用，不能删除!");
			return;
		}
		var checkUrl = AppActionURI.logicGroupRelation + "!checkUnique.json?Q_EQ_groupCode=" + code;
		dhtmlxAjax.get(checkUrl,function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (false == jsonObj.success) {
				dhtmlx.message("该逻辑表组下还有逻辑表，不能删除!");
			} else {
				dhtmlx
				.confirm({
					type : "confirm",
					text : getMessage("delete_warning"),
					ok : "确定",
					cancel : "取消",
					callback : function(flag) {
						if (flag) {
							deleteTableById(rowIds);
						}
					}
				});
			}
		});
	}
	;

	function deleteTableById(id) {
		dhtmlxAjax.get(moduleUrl + "/" + id + ".json?_method=delete", function(
				loader) {
			var obj = eval("(" + loader.xmlDoc.responseText + ")");
				that.reloadGrid();
				that.reloadTreeItem();
				dhtmlx.message(getMessage("delete_success"));
		});
	}
}

//初始化逻辑组表详细页面
function initLogicGroupDetailGrid(that, layout, nodeId){
	layout.hideHeader();

	var _this = this;
	var moduleUrl = AppActionURI.logicGroupDefine;
	var ttbar = layout.attachToolbar();
	var grid = layout.attachGrid();
	var ttbarIndex = 1;
	var groupCode = nodeId.substring(3);
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("add", ttbarIndex++, "添加", "new.gif");
	ttbar.addSeparator("septr$01", ttbarIndex++);
	ttbar.addButton("delete", ttbarIndex++, "移除", "delete.gif");
	ttbar.addSeparator("septr$02", ttbarIndex++);
	ttbar.addButton("check", ttbarIndex++, "校验", "sync.gif");
	ttbar.addSeparator("septr$03", ttbarIndex++);
	layout.showToolbar();
	ttbar.showItem("add");
	ttbar.showItem("septr$01");
	ttbar.showItem("delete");
	ttbar.showItem("septr$02");
	ttbar.showItem("check");
	ttbar.showItem("septr$03");
	ttbar.attachEvent("onClick", function(id) {
		if ("add" == id) {
			if (!dhxWins) {
		        dhxWins = new dhtmlXWindows();
		    }
		    var winWidth = 500;
		    var winHeight = 300;
		    dataWin = dhxWins.createWindow("win_id", 0, 0, winWidth, winHeight);
		    dataWin.setModal(true);
		    dataWin.setText("新增");
		    dataWin.center();
		    var logicGroupAreaFormGrid = dhxWins.window("win_id").attachGrid();
		    if (initLogicGroupAreaFormToolbar && typeof initLogicGroupAreaFormToolbar == "function") {
		        var statusBar = dataWin.attachStatusBar();
		        var toolBar = new dhtmlXToolbarObject(statusBar);
		        initLogicGroupAreaFormToolbar.call(this, toolBar, logicGroupAreaFormGrid, groupCode);
		    }
			logicGroupAreaFormData  = {
					format: {
						headers: ["","<center>逻辑表名称</center>","<center>逻辑表编码</center>"],
						   cols: ["selected","showName","code"],
						   id:["id"],
						colWidths: ["50","150","150"],
						colTypes: ["ch","ro","co"],
						colAligns: ["left","left","left"]
					}
				};
			
			var logicGroupURL = AppActionURI.logicTableDefine + "!getLTExcludingLG.json?P_groupCode=" + groupCode + "&P_orders=showOrder";
			initGridWithoutPageable(logicGroupAreaFormGrid,logicGroupAreaFormData,logicGroupURL);
			logicGroupAreaFormGrid.enableTooltips("false,false,false");
		} else if ("delete" == id) {
			deleteById();
		} else if ("check" == id) {
			var url = AppActionURI.logicGroupRelation + "!check.json?P_groupCode=" + groupCode;
			var result = loadJson(url);
			if (result.success == false) {
				dhtmlx.alert(result.message);
			}
		}
	});
	var gcfg = {
		format : {
			headers : [ "<center>逻辑表名称</center>","<center>逻辑表编码</center>", "<center>父逻辑表</center>", "" ],
			cols : [ "tableCode", "tableCode", "parentTableCode", "" ],
			colWidths : [ "200", "200", "200", "*" ],
			colTypes : [ "co", "ro", "co", "ro" ],
			colAligns : [ "left", "left", "left", "left" ]
		}
	};
	var dataJson = loadJson(AppActionURI.logicTableDefine + "!search.json?F_in=showName,code");
	var colNameCombo = grid.getCombo(0);
	var colParentCombo = grid.getCombo(2);
	for (var i = 0; i < dataJson.data.length; i++) {
		colNameCombo.put(dataJson.data[i].code,dataJson.data[i].showName);
		colParentCombo.put(dataJson.data[i].code,dataJson.data[i].showName);
	}
	
	var gurl = AppActionURI.logicGroupRelation + "!search.json?Q_EQ_groupCode="+groupCode+"&P_orders=showOrder";
	grid.enableDragAndDrop(true);
	grid.enableTooltips("false,false,false");
	initGridWithoutPageable(grid, gcfg, gurl);
	
	grid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj, sCol, tCol) {
		if (undefined != tId) {
			var adjustUrl = AppActionURI.logicGroupRelation + "!adjustShowOrder?P_sourceIds=" + sId + "&P_targetId=" + tId + "&P_groupCode=" + groupCode;
			dhtmlxAjax.get(adjustUrl, function(loader) {
				that.reloadGrid();
				grid.selectRowById(sId);
			});
		}
	});
	
	grid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue) {
		if (0 == cInd) return false;
		if (rId != "" && rId != null) {
			var parentComboJson = loadJson(AppActionURI.logicTableDefine + "!getLTIncludingLG.json?P_groupCode=" + groupCode + "&P_rId=" + rId);
			colParentCombo.clear();
			for (var i = 0; i < parentComboJson.length; i++) {
				colParentCombo.put(parentComboJson[i].code,parentComboJson[i].showName);
			}
		}
		if (nValue == oValue) return;
		var url = AppActionURI.logicGroupRelation + "!update.json?id=" + rId + "&P_parentTableCode=" + nValue;
		var rlt = loadJson(encodeURI(url));
		if (false === rlt.success) {
			grid.cells(rId, cInd).setValue(oValue);
		} else {
			dhtmlx.message("操作成功！");
		}
		return true;
	});
	
	/** 初始化工具条*/
	function initLogicGroupAreaFormToolbar(toolBar, logicGroupAreaFormGrid, groupCode) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window(WIN_ID).close();
			} else if (id == "save") {
	        	var rowsValue = "";
				logicGroupAreaFormGrid.forEachRow(function(rowId) {
					var disabled = logicGroupAreaFormGrid.cells(rowId, 0).getValue();
					if ("1" == disabled) {
						var code = logicGroupAreaFormGrid.cells(rowId, 2).getValue();
						rowsValue += ";" + code;
					} 
				});
				if (rowsValue.length <= 0) {
					dhtmlx.alert("请选择一个逻辑表！");
					return;
				}
				var url = AppActionURI.logicGroupRelation + "!save?P_tableCodes=" + rowsValue + "&P_groupCode=" + groupCode;
				dhtmlxAjax.get(url , function(loader) {
					
					dhxWins.window(WIN_ID).close();
					that.reloadGrid();
					dhtmlx.message(getMessage("save_success"));
				});
				
			}
		});
	}
	
	/** 单条删除记录*/
	function deleteById() {
		var rowIds = grid.getSelectedRowId();
		if (null == rowIds || "" == rowIds) {
			dhtmlx.message(getMessage("select_record"));
			return;
		} 
		if (rowIds.split(",").length > 1) {
			dhtmlx.message(getMessage("select_only_one_record"));
			return;
		}
		dhtmlx.confirm({
			type:"confirm",
			text: getMessage("delete_warning"),
			ok: "确定",
			cancel: "取消",
			callback: function(flag) {
			if (flag) {
				dhtmlxAjax.get(AppActionURI.logicGroupRelation + "/" + rowIds + ".json?_method=delete", function(loader) {
					var obj = eval("(" + loader.xmlDoc.responseText + ")");
					that.reloadGrid();
					dhtmlx.message(getMessage("delete_success"));
		        });
				}
			}
		});
	};
	
	that.reloadGrid = function(){
		loadGridData(grid, gcfg, gurl);
	};
}



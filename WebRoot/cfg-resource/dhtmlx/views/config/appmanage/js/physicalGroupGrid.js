/**
 * 
 * @param that
 * @param layout
 */
function initPhysicalGroupGrid(that, layout) {
	layout.hideHeader();

	var _this = this;
	var moduleUrl = AppActionURI.physicalGroupDefine;
	var ttbar = layout.attachToolbar();
	var grid = layout.attachGrid();
	var ttbarIndex = 1;
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("add", ttbarIndex++, "新增", "new.gif");
	ttbar.addSeparator("septr$01", ttbarIndex++);
	ttbar.addButton("delete", ttbarIndex++, "删除", "delete.gif");
	ttbar.addSeparator("septr$02", ttbarIndex++);
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
		}
	});
	var gcfg = {
		format : {
			headers : [ "&nbsp;", "<center>物理表组名称</center>","<center>逻辑表组名称</center>","<center>备注</center>", "" ],
			cols : [ "id", "groupName", "logicGroupCode", "remark", "" ],
			colWidths : [ "30", "200", "200", "150", "*" ],
			colTypes : [ "sub_row_form", "ro", "co", "ro", "ro" ],
			colAligns : [ "right", "left", "left", "left", "left" ]
		}
	};
	var tCombo = grid.getCombo(2);
	var dataJson = loadJson(AppActionURI.logicGroupDefine + "!search.json?F_in=id,groupName");
	for ( var i = 0; i < dataJson.data.length; i++) {
		tCombo.put(dataJson.data[i].id,dataJson.data[i].groupName);
	}

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
    				{type: "block", width: "800", list:[
                        {type: "hidden", name: "_method"},
						{type: "hidden", name: "id"},
						{type: "hidden", name: "showOrder"},
						{type: "block", name: "block_table", width:700, list:[
							{type: "input", label: "物理表组名称：", labelWidth:120, name: "groupName", required:true, width:170, maxLength:24},
							{type: "newcolumn"},
							{type: "input", label: "物理表组编码：", labelWidth:120, name: "code", width:170, maxLength:24, offsetLeft:20}                 			 				
						    ]},
					    {type: "block", width:700, list:[
					        {type: "combo", label: "逻辑表组名称：", labelWidth:120, name: "logicGroupCode", required:true, width:170, maxLength:24},
					        {type: "newcolumn"}
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
		return false;
	});

	grid.attachEvent("onRowSelect", function(rId, cInd) {
		grid.cells(rId, 0).open();
	});

	grid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
		subform.c = getFormJson();
	});

	grid.attachEvent("onSubFormLoaded", function(subform, id, index) {
		
		//初始化combo
		var t_combo = subform.getCombo("logicGroupCode");
		var dataJson = loadJson(AppActionURI.logicGroupDefine + "!search.json?F_in=code,groupName");
		for ( var i = 0; i < dataJson.data.length; i++) {
			t_combo.addOption(dataJson.data[i].code,dataJson.data[i].groupName);
		}

		var MODEL_URL = AppActionURI.physicalGroupDefine;
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
				var groupName = subform.getItemValue("groupName");
				var code = subform.getItemValue("code");
				if (null == groupName || "" == groupName) {
					dhtmlx.alert("物理表组名称不可为空！");
					return;
				}
				var checkUrl = moduleUrl + "!checkUnique.json?id=" + id + "&Q_EQ_groupName=" + groupName;
				dhtmlxAjax.get(checkUrl, function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					if (false == jsonObj.success) {
						if ("OK" == jsonObj.message) {
							dhtmlx.message(getMessage("form_field_exist","物理表组名称"));
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
	}
	;
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
		//验证树定义中是否存在该物理表组节点
		var checkUrl = AppActionURI.treeDefine + "!checkUnique.json?Q_EQ_dbId=" + rowIds;
		dhtmlxAjax.get(checkUrl,function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (false == jsonObj.success) {
				dhtmlx.message("树定义中存在该物理表组，不能删除!");
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

//初始化物理组表详细页面
function initPhysicalGroupDetailGrid(that, layout, nodeId){
	layout.hideHeader();

	var _this = this;
	var moduleUrl = AppActionURI.physicalGroupDefine;
	var ttbar = layout.attachToolbar();
	var grid = layout.attachGrid();
	var ttbarIndex = 1;
	var groupId = nodeId.substring(3);
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("add", ttbarIndex++, "添加", "new.gif");
	ttbar.addSeparator("septr$01", ttbarIndex++);
	ttbar.addButton("delete", ttbarIndex++, "移除", "delete.gif");
	ttbar.addSeparator("septr$02", ttbarIndex++);
	layout.showToolbar();
	ttbar.showItem("add");
	ttbar.showItem("septr$01");
	ttbar.showItem("delete");
	ttbar.showItem("septr$02");
	ttbar.attachEvent("onClick", function(id) {
		if ("add" == id) {
			if (!dhxWins) {
		        dhxWins = new dhtmlXWindows();
		    }
		    var winWidth = 250;
		    var winHeight = 350;
		    dataWin = dhxWins.createWindow("win_id", 0, 0, winWidth, winHeight);
		    dataWin.setModal(true);
		    dataWin.setText("新增");
		    dataWin.center();
		    var groupTree = dhxWins.window("win_id").attachTree();
		    groupTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
		    groupTree.attachEvent("onMouseIn", function(id) {
		    	groupTree.setItemStyle(id, "background-color:#D5E8FF;");
			});
		    groupTree.attachEvent("onMouseOut", function(id) {
		    	groupTree.setItemStyle(id, "background-color:#FFFFFF;");
			});
		    var ptItem = loadJson(moduleUrl + "!getTreeOfGroup.json?P_groupId=" + groupId);
		    var treeJson = {id:0, item:[
		                                {id:"-PT", text: "物理表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item: ptItem},
		                        	]};
		    groupTree.setDataMode("json");
		    groupTree.enableSmartXMLParsing(true);
		    groupTree.setXMLAutoLoading(moduleUrl+"!getTreeNodes.json?P_groupId=" + groupId);
		    groupTree.loadJSONObject(treeJson);
		    groupTree.showItemCheckbox(1);
		    groupTree.enableCheckBoxes(true);
		    var statusBar = dataWin.attachStatusBar();
	        var toolBar = new dhtmlXToolbarObject(statusBar);
	        initPhysicalGroupAreaToolbar.call(this, toolBar, groupTree, groupId);
		} else if ("delete" == id) {
			deleteById(grid, groupId);
		}
	});
	var gcfg = {
		format : {
			headers : [ "<center>物理表名称</center>","<center>物理表英文名称</center>", "<center>逻辑表名称</center>", "" ],
//			cols : [ 1, 2, 3],
//			id : [0],
			colWidths : [ "200", "200", "200", "*" ],
			colTypes : [ "ro", "ro", "ro", "ro" ],
			colAligns : [ "left", "left", "left", "left" ]
		}
	};
	
	var gurl = AppActionURI.physicalGroupRelation + "!getPhysicalGroupRelationByGroupId.json?P_groupId="+groupId;
	grid.enableDragAndDrop(false);
	grid.enableTooltips("false,false,false");
	initGridWithoutPageable(grid, gcfg, gurl);
	
	/** 初始化工具条*/
	function initPhysicalGroupAreaToolbar(toolBar, groupTree, groupId) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window(WIN_ID).close();
			} else if (id == "save") {
				var ids = groupTree.getAllChecked();
				if (null == ids || ids == "") {
					dhtmlx.alert("请至少选择一张物理表!");
					return;
				}
				var codes = null;
				var idArr = ids.split(",");
				for ( var i = 0; i < idArr.length; i++) {
					codes += "," + groupTree.getParentId(idArr[i]);
				}
				//验证物理表间是否存在表关系，提示
				var checkUrl = AppActionURI.tableRelation + "!getRelationsOfIds.json?P_codes=" + codes + "&P_groupId=" + groupId + "&P_ids=" + ids;
				var result = loadJson(checkUrl);
				if (result.success == false) {
					dhtmlx.alert(result.message);
					return;
				}
				var url = AppActionURI.physicalGroupRelation + "!save?P_tableIds=" + ids + "&P_groupId=" + groupId;
				dhtmlxAjax.get(url , function(loader) {
					dhxWins.window(WIN_ID).close();
					that.reloadGrid();
					dhtmlx.message(getMessage("save_success"));
				});
			}
		});
	}
	
	/** 单条删除记录*/
	function deleteById(grid, groupId) {
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
				dhtmlxAjax.get(AppActionURI.physicalGroupRelation + "/" + rowIds + ".json?_method=delete", function(loader) {
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


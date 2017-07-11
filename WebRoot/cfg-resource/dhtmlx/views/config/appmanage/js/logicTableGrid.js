/**
 * 
 * @param that
 * @param layout
 */
function initLogicTableGrid(that, layout, nodeId) {
    layout.hideHeader();
    
    var _this = this;
    var moduleUrl = AppActionURI.logicTableDefine;
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
	layout.showToolbar();
	ttbar.showItem("add");
	ttbar.showItem("septr$01");
	ttbar.showItem("delete");
	ttbar.showItem("septr$02");
	ttbar.showItem("physical");
	ttbar.showItem("septr$03");
	ttbar.attachEvent("onClick", function(id) {
		if ("add" == id) {
			if (grid.getRowId(0) == '') {
            	return;
            }
            grid.addRow('','',0);
            grid.cells('',0).open();
		} else if ("delete" == id) {
			deleteById();
		} else if ("physical" == id) {
			var rowIds = grid.getSelectedRowId();
			if (null == rowIds || "" == rowIds) {
				dhtmlx.message(getMessage("select_record"));
				return;
			} 
			if (rowIds.split(",").length > 1) {
				dhtmlx.message(getMessage("select_only_one_record"));
				return;
			}
			logicTableFormGrid = openWindow(logicTableFormData, "生成物理表", initLogicTableFormToolbar, 450, 200);
			logicTableFormGrid.attachEvent("onButtonClick", function(buttonName){
				if ("select" == buttonName) {
					if (!dhxWins) {
				        dhxWins = new dhtmlXWindows();
				    }
				    var winWidth = 300;
				    var winHeight = 500;
				    dataWin = dhxWins.createWindow("physicalTree", 0, 0, winWidth, winHeight);
				    dataWin.setModal(true);
				    dataWin.setText("选择");
				    dataWin.center();
				    var tree = dhxWins.window("physicalTree").attachTree();
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
			        initPhysicalTreeAreaToolbar.call(this, toolBar, tree);
				}
			});
		}
	});
	var	gcfg  = {
				format: {
					headers: ["&nbsp;","<center>逻辑表名称</center>","<center>逻辑表编码</center>","<center>状态</center>","<center>备注</center>",""],
					   cols: ["id","showName","code","status","remark",""],
					colWidths: ["30","200","200","150","150","*"],
					colTypes: ["sub_row_form","ro","ro","co","ro","ro"],
					colAligns: ["right","left","left","left","left"]
				}
		};
	
	var statusCombo = grid.getCombo(3);
	statusCombo.put("0","未应用");
	statusCombo.put("1","已应用");
	
	var gurl = getGridUrl();
	grid.enableDragAndDrop(true);
	grid.enableTooltips("false,false,false,false");
	initGridWithoutPageable(grid, gcfg, gurl);
	/** 列表拖拽调整顺序*/
	grid.attachEvent("onDrag",function(sIds,tId) {
		// 判断选中的记录是否连续，如果不连续，则不能调整顺序
		if (sIds.indexOf(",") != -1) {
			var idArr = sIds.split(",");
			var preIdx = null, curIdx = null;
			for (var i = 0; i < idArr.length; i++) {
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
						{type: "hidden", name: "classification", value: classification},
						{type: "hidden", name: "tableTreeId", value: nodeId},
						{type: "hidden", name: "showOrder"},
						{type: "block", name: "block_table", width:700, list:[
							{type: "input", label: "逻辑表名称：", labelAlign:"right", labelWidth:120, name: "showName", required:true, width:200, maxLength:24},
							{type: "newcolumn"},
							{type: "input", label: "逻辑表编码：", labelAlign:"right", labelWidth:120, name: "code", required:true, width:200, maxLength:24, offsetLeft:20}                      			 				
						    ]},
			 			{type: "block", width:790, list:[
							{type: "input", label: "备　　　注：　", labelAlign:"right", name: "remark", labelWidth:120, width:540, maxLength:1000, rows:3},
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
    	grid.cells(rId,0).open();
	});
	
	grid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
		subform.c = getFormJson();
	});
	
	grid.attachEvent("onSubFormLoaded", function(subform,id,index) {

		var MODEL_URL = AppActionURI.logicTableDefine;
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
				var id  = subform.getItemValue("id");
				var showName    = subform.getItemValue("showName");
				var code = subform.getItemValue("code");
				if (null == showName || "" == showName) {
					dhtmlx.alert("逻辑表名称不可为空！");
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
				dhtmlxAjax.get(checkUrl,function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					if (false == jsonObj.success) {
						if ("OK" == jsonObj.message) {
							dhtmlx.message(getMessage("form_field_exist", "表名称"));
						} else {
							dhtmlx.message("唯一检查出错，请联系管理员！");
						}
					} else {//*/
						submit(id);
					}
				});//*/
	        }
		});
		    
		    /** 表单提交*/
		function submit(id) {
			var url = moduleUrl;
			var showName = subform.getItemValue("showName");
			var	code = subform.getItemValue("code");
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
	
	grid.attachEvent("onSubRowOpen", function(id,expanded){
		if (expanded) {
			grid.forEachRow(function(rId){
				if (id != rId) {
					grid.cells(rId,0).close();
				}
			});
		}
	});
				
	grid.attachEvent("onDrop", function(sId,tId,dId,sObj,tObj,sCol,tCol){
		if(undefined != tId){
			var adjustUrl = moduleUrl + "!adjustShowOrder?P_sourceIds=" + sId + "&P_targetId=" + tId;
			dhtmlxAjax.get(adjustUrl, function(loader) {
				that.reloadGrid();
				grid.selectRowById(sId);
				that.reloadTreeItem();
			});
		}		
	});
	
	/** 列表刷新*/
	that.reloadGrid = function() {
		loadGridData(grid, gcfg, getGridUrl());
	};
	/** 列表工具条按钮*/
	that.buttonGrid = function () {
		layout.showToolbar();
		ttbar.showItem("add");
		ttbar.showItem("septr$01");
		ttbar.showItem("delete");
		ttbar.showItem("septr$02");
		ttbar.showItem("physical");
		ttbar.showItem("septr$03");
		};
	/** 列表查询地址*/
	function getGridUrl() {
		return moduleUrl + "!search.json?Q_EQ_tableTreeId=" + nodeId + "&P_orders=showOrder";
	};
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
		//验证逻辑表是否存在逻辑表组关联关系
		var code = grid.cells(rowIds,2).getValue();
		var checkUrl = AppActionURI.logicGroupRelation + "!checkUnique.json?Q_EQ_tableCode=" + code;
		dhtmlxAjax.get(checkUrl,function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (false == jsonObj.success) {
				dhtmlx.message("该逻辑表与逻辑表组存在关联关系，不能删除!");
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
	};
	
	function deleteTableById(id) {
		dhtmlxAjax.get(moduleUrl + "/" + id + ".json?_method=delete", function(loader) {
			var obj = eval("(" + loader.xmlDoc.responseText + ")");
			if (typeof obj == "string") {
			    obj = eval("(" + obj + ")");
			}
			if (obj.success == false) {
				dhtmlx.alert(obj.message);
				return;
			} else {
				that.reloadGrid();
				that.reloadTreeItem();
				dhtmlx.message(getMessage("delete_success"));
			}			
        });
	}
	
	logicTableFormData  = {
			format: [
				{type: "block", width: "350", list:[
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "tableType", value:"0"},
					{type: "hidden", name: "tableTreeId"},
					{type: "hidden", name: "created",value:"0"},
					{type: "hidden", name: "classification"},
					{type: "hidden", name: "logicTableCode"},
					{type: "input", label: "显示名称：", labelWidth: "120", name: "showName", maxLength:100, required: true, width: 200},
					{type: "block", name: "block_table", width:420, list:[
	  					{type: "itemlabel", label: "表名称：　", labelWidth:120},
						{type: "newcolumn"},
		  				{type: "input", labelWidth:80, name: "tablePrefix", width:40},
						{type: "newcolumn"},
		 				{type: "input", name: "tableCode", width:158, maxLength:20}
		 			]},
		 			{type: "block", width:420, list:[
                        {type: "input", label: "所属分类：", labelWidth: "120", name: "tableTreeText", maxLength:100, required: true, width: 200},
                        {type: "newcolumn"},
                        {type: "button", name: "select", value: "选择", width: 50, offsetLeft:20}
					]},
				]}
			]
		};
	
	/** 初始化生成物理表工具条*/
	function initLogicTableFormToolbar(toolBar) {
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
				logicTableFormGrid.setItemValue("logicTableCode", code);
				var prefix    = logicTableFormGrid.getItemValue("tablePrefix");
				var tableCode = logicTableFormGrid.getItemValue("tableCode");
				var tableTreeId = logicTableFormGrid.getItemValue("tableTreeId");
				if (null == prefix || "" == prefix) {
					dhtmlx.alert("表前缀不可为空！");
					return;
				}
				if (null == tableCode || "" == tableCode) {
					dhtmlx.alert("表名称不可为空！");
					return;
				}
				if (null == tableTreeId || "" == tableTreeId) {
					dhtmlx.alert("请选择物理表的所属分类！");
					return;
				}
				prefix = prefix.toUpperCase();
				tableCode = tableCode.toUpperCase();
				if ("T_XTPZ_" == prefix || "T_WF_" == prefix) {
					dhtmlx.alert("此表前缀已被系统使用，请修改！");
					return;
				}
				if (!prefix.match(/^[t_|T_]([\w\_])+\_$/)) {
					dhtmlx.alert("表前缀格式只能以T_开头并要以_结束，中间可由字母、数字、下划线组成！");
					return;
				}
				if (!tableCode.match(/([\w\_])+/)) {
					dhtmlx.alert("表名称只能是字母、数字组成！");
					return;
				}
				var checkUrl = AppActionURI.physicalTableDefine + "!checkUnique.json?Q_EQ_tableCode=" + tableCode.toUpperCase() + "&Q_EQ_tablePrefix=" + prefix;
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
				var checkUrl = url;
				var tableTreeId = logicTableFormGrid.getItemValue("tableTreeId");
				var tableCode = logicTableFormGrid.getItemValue("tableCode");
				logicTableFormGrid.setItemValue("_method", "post");
				url += ".json";
				checkUrl += "!checkUnique.json?Q_EQ_tableTreeId=" + tableTreeId + "&Q_EQ_tableCode=" + tableCode;
				dhtmlxAjax.get(encodeURI(addTimestamp(checkUrl)), function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					if (false == jsonObj.success) {
						if ("OK" == jsonObj.message) {
							dhtmlx.message(getMessage("form_field_exist", "同级目录下该显示名称"));
						} else {
							dhtmlx.message("唯一检查出错，请联系管理员！");
						}
					} else {
						logicTableFormGrid.send(url, "post", function(loader, response) {
							var formData = eval("(" + loader.xmlDoc.responseText + ")");
							if (formData.id == null || formData.id == "") {
								dhtmlx.message(getMessage("save_failure"));
								return;
							}
							logicTableFormGrid.setFormData(formData);
							that.reloadGrid();
							that.reloadTreeItem();
							var code = grid.cellById(rowId, 2).getValue();
							//复制表的操作
							var url = AppActionURI.columnDefine + "!copyFileds2newTableByLogicTable.json?logicTable_code="+code+"&newTable_id="+formData.id+"";
							dhtmlxAjax.get(url,function(loader){
								var rlt = eval("(" + loader.xmlDoc.responseText + ")");
								if ("" != rlt.message) {
									dhtmlx.message(rlt.message);
									return;
								}
								dhtmlx.message(getMessage("save_success"));
								dhxWins.window(WIN_ID).close();
							});
						});
					}
				});			
			}
		});
	}
	
	/** 初始化物理表树选择工具条*/
	function initPhysicalTreeAreaToolbar(toolBar, tree) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;确认&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;取消&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window("physicalTree").close();
			} else if (id == "save") {
				var tableTreeId = tree.getAllChecked();
				if (tableTreeId == "-PT") {
					dhtmlx.alert("请选择一个具体的分类!");
					return;
				}
				var tableTreeText = tree.getItemText(tableTreeId);
				logicTableFormGrid.setItemValue("tableTreeId",tableTreeId);
				logicTableFormGrid.setItemValue("tableTreeText",tableTreeText);
				logicTableFormGrid.setItemValue("classification",tree.getUserData(tableTreeId,"classification"));
				var tablePrefix = loadJson(AppActionURI.tableTree + "!getTablePrefix.json?P_tableTreeId=" + tableTreeId);
				if (tablePrefix == 0) {
					tablePrefix = that.prefixObj[tree.getUserData(tableTreeId,"classification")];
				}
				logicTableFormGrid.setItemValue("tablePrefix",tablePrefix);
				dhxWins.window("physicalTree").close();
			}
		});
	}
}


/**
* 表定义树初始化
* @param that
*/
function initTableTree(that){
    var mbbtbar = that.aLayout.attachToolbar();
    mbbtbar.setIconPath(IMAGE_PATH);
    mbbtbar.addDiv("form$pos", 0);
    // 
    initConfigType(that);
    // 
    initPhysicalTableTree(that);
}
/**
* 表定义树物理表
* @param that
*/
function initPhysicalTableTree(that) {
	var moduleUrl = AppActionURI.tableTree;
	var layout = that.aLayout;
	layout.setWidth(240);
	layout.hideHeader();
	tree = layout.attachTree();
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
	// 逻辑表分类节点
	var ltItem = loadJson(AppActionURI.logicClassification + "!treeNode.json");
	// 视图分类节点
	var vItem = loadJson(AppActionURI.tableClassification + "!treeNodeOfView.json");
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
	    {id:"-C", text: "基础表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:0, userdata:[{name:"type",content:"1"},{name:"classification",content:"C"}]},
	    {id:"-LT", text: "逻辑表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", item: ltItem},
	    {id:"-LG", text: "逻辑表组", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, userdata:[{name:"type",content:"0"},{name:"classification",content:"LG"}]},
		{id:"-PT", text: "物理表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item: ptItem},
		//{id:"-CF", text: "工作流表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, userdata:[{name:"type",content:"0"},{name:"classification",content:"CF"}]},
		{id:"-PG", text: "物理表组", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, userdata:[{name:"type",content:"0"},{name:"classification",content:"PG"}]},
		{id:"-V", text: "视图", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, userdata:[{name:"type",content:"0"},{name:"classification",content:"V"}], item: vItem}
	]};
	// 初始右键菜单
	var treeMenu = new dhtmlXMenuObject();
	treeMenu.renderAsContextMenu();
	treeMenu.loadXMLString("<menu><item id='1' text='新增表分类'/><item id='2' text='修改表分类'/><item id='3' text='删除表分类'/></menu>");
	treeMenu.attachEvent("onClick", function(id) {
		GWIN_WIDTH = 400;
		GWIN_HEIGHT = 240;
		if (id == "1") {
			tableTreeAreaForm = openNewWindow(tableTreeAreaFormData, initTableTreeAreaFormToolbar, 400, 200);
			tableTreeAreaForm.setItemValue("parentId", contextMenuNodeId);
			tableTreeAreaForm.setItemValue("classification", classification);
			var flag = false;
			for (var i = 0; i < ptItem.length; i++){
				var item = ptItem[i];
				if (item.userdata[1].content == classification) {
					flag = true;
				}
			}
			if (flag) {
				tableTreeAreaForm.removeItem("code");
				tableTreeAreaForm.setItemValue("nodeType","1");
			} else {
				tableTreeAreaForm.removeItem("tablePrefix");
				tableTreeAreaForm.setItemValue("nodeType","0");
			}
		} else if (id == "2") {
			MODEL_URL = moduleUrl;
			tableTreeAreaForm = openEditWindow(tableTreeAreaFormData, contextMenuNodeId, initTableTreeAreaFormToolbar, 400, 200);
			var flag = false;
			for (var i = 0; i < ptItem.length; i++){
				var item = ptItem[i];
				if (item.userdata[1].content == classification) {
					flag = true;
				}
			}
			if (flag) {
				tableTreeAreaForm.removeItem("code");
				tableTreeAreaForm.setItemValue("nodeType","1");
			} else {
				tableTreeAreaForm.removeItem("tablePrefix");
				tableTreeAreaForm.setItemValue("nodeType","0");
			}
		} else if (id == "3") {
			var checkUrl = AppActionURI.tableTree + "!checkDelete.json?P_tableTreeId=" + contextMenuNodeId;
			dhtmlxAjax.get(checkUrl, function(loader) {
				var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				if (false == jsonObj.success) {
					dhtmlx.message(jsonObj.message);
				} else {
					dhtmlx.confirm({
						type:"confirm",
						text: getMessage("delete_warning"),
						ok: "确定",
						cancel: "取消",
						callback: function(flag) {
						if (flag) {
								dhtmlxAjax.get(AppActionURI.tableTree + "/" + contextMenuNodeId + ".json?_method=delete", function(loader) {
					                jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					                if (typeof jsonObj == 'string') {
					                	jsonObj = eval("(" + jsonObj + ")");
					                }
					                if (jsonObj.success) {
					                    dhtmlx.message(getMessage("delete_success"));
					                    var parId = tree.getParentId(contextMenuNodeId);
					                    tree.refreshItem(parId);
					                    nodeId = parId;
					                } else {
					                	dhtmlx.message(isNotEmpty(jsonObj.message) ? jsonObj.message : getMessage("save_failure"));
					                }
					            });
							}
						}
					});
				}
			});
		}
	});
	tree.enableContextMenu(treeMenu);
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(moduleUrl+"!tree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,child&P_UD=type,classification,tableLabel");
	tree.loadJSONObject(treeJson);
	//点击节点刷新右边列表
	tree.attachEvent("onClick", function(nId) {
		// 树节点ID
		var treeType = "physical";
		nodeId = nId;
		type = tree.getUserData(nId,"type");
		treeName = tree.getItemText(nId);
		classification = tree.getUserData(nId,"classification");
		if ("-PT" == nId || "-LT" == nId) {
			initLayoutBHelp(that);
		} else if (type=="1" || nId == "-C") {
			initLayoutBContent(that, nodeId, null, treeName);
		} else if(type == "0") {
			if ("-LG" == nId) {
				initLogicGroupCreate(that);
			} else if ("-PG" == nId) {
				initPhysicalGroupCreate(that);
			} else {
				initPhysicalTableCreate(that, treeType);
			}
		} else if (type == "2") {
			initLogicGroupDetailCreate(that, nodeId);
		} else if (type == "3") {
			initPhysicalGroupDetailCreate(that, nodeId);
		} else if (type == "4") {
			initLogicTableRelationDetailCreate(that, nodeId, treeName);
		} else if (type == "5") {
			groupId = tree.getParentId(nodeId).substring(3);
			nodeId = nodeId.substring(nodeId.length - 32, nodeId.length);
			initLayoutBContent(that, nodeId, groupId, treeName);
		} else if (type == "6") {
			initLogicTableCreate(that, nodeId);
		}
		
	});
	tree.attachEvent("onBeforeContextMenu", function(nId) {
		contextMenuNodeId = nId;
		classification = tree.getUserData(nId,"classification");
		type = tree.getUserData(nId,"type");
		var flag = false;
		for (var i = 0; i < ptItem.length; i++){
			var item = ptItem[i];
			if (item.id == nId) {
				flag = true;
			}
		}
		for (var i = 0; i < ltItem.length; i++){
			var item = ltItem[i];
			if (item.id == nId) {
				flag = true;
			}
		}
		if(flag) {//物理表下第一层节点，只能新增子节点，不能修改删除
			treeMenu.showItem("1");
			treeMenu.hideItem("2");
			treeMenu.hideItem("3");
		} else if(nId.length > 30 && type == 0){
			treeMenu.showItem("1");
			treeMenu.showItem("2");
			treeMenu.showItem("3");
		} else{
			treeMenu.hideItem("1");
			treeMenu.hideItem("2");
			treeMenu.hideItem("3");
		}
		return true;
	});
    tree.enableDragAndDrop(true, false);
    tree.setDragBehavior("complex", true);
	tree.attachEvent("onDrag", function(sId, tId, id){
		var draggable = tree.getUserData(sId,"draggable");
		var classification = tree.getUserData(tId,"classification");
		var type = tree.getUserData(tId,"type");
		var flag = false;
		if (type == "0") {
			for (var i = 0; i < ptItem.length; i++){
				var item = ptItem[i];
				if (classification == item.userdata[1].content) {
					flag = true;
				}
			}
		}
		if (draggable == "1" && flag) {
			return true;
		}
		dhtmlx.message("只支持改变物理表分类！");
        return false;
	});
	tree.attachEvent("onDrop", function(sId, tId, id){
		var url = AppActionURI.tableTree + "!updateTableTreeId.json?start=" + sId + "&targetId=" + tId;
		var result = loadJson(url);
		dhtmlx.message(result);
	});
	/** 刷新树节点*/
	that.reloadTreeItem = function(nId) {
		if (nId) {
			tree.refreshItem(nId);
		} else {
			tree.refreshItem(nodeId);
		}
	};
} 

/**
 * 初始化表定义分类表单工具条的方法
 * @param {dhtmlxToolbar} toolBar
 */
function initTableTreeAreaFormToolbar(toolBar) {
	var module = AppActionURI.tableTree;
	toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
	toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
	toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
	toolBar.setAlign("right");
	toolBar.attachEvent("onClick", function(id) {
		if (id == "close") {
			dhxWins.window(WIN_ID).close();
		} else if (id == "save") {
			var id = tableTreeAreaForm.getItemValue("id");
        	var name = tableTreeAreaForm.getItemValue("name");
        	if (null == name || "" == name) {
				dhtmlx.alert("表定义分类名称不可为空！");
				return;
			}
        	var checkUrl = module + "!checkUnique.json?Q_EQ_name=" + name + "&Q_EQ_parentId=" + contextMenuNodeId;
        	dhtmlxAjax.get(checkUrl,function(loader) {
				var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				if (false == jsonObj.success) {
					if ("OK" == jsonObj.message) {
						dhtmlx.message(getMessage("form_field_exist", "表定义分类名称"));
					} else {
						dhtmlx.message("唯一检查出错，请联系管理员！");
					}
				} else {//*/
					if (id == "") {
		        		SAVE_URL = module;
		        		tableTreeAreaForm.setItemValue("_method", "post");
		        	} else {
		        		SAVE_URL = module + "/" + id;
		        		tableTreeAreaForm.setItemValue("_method", "put");
		        	}
		        	tableTreeAreaForm.send(SAVE_URL, "post", function(loader, response){
						dhxWins.window(WIN_ID).close();
						dhtmlx.message(getMessage("save_success"));
						if(id != ""){
							var parentId = tree.getParentId(contextMenuNodeId);
							tree.refreshItem(parentId);
						}else{
							tree.refreshItem(contextMenuNodeId);
						}
		    		});
				}
			});
		}
	});
}

var tableTreeAreaFormData = {
		format: [
			{type: "block", width: "350", list:[
				{type: "hidden", name: "_method"},
				{type: "hidden", name: "id"},
				{type: "hidden", name: "nodeType", value: "1"},
				{type: "hidden", name: "parentId"},
				{type: "hidden", name: "showOrder"},
				{type: "hidden", name: "classification"},
				{type: "input", label: "分类名称：", labelWidth: "120", name: "name", maxLength:100, required: true, width: 200},
				{type: "input", label: "物理表前缀：&nbsp;&nbsp;&nbsp;", labelWidth: "120", name: "tablePrefix", maxLength:5, width: 200},
				{type: "input", label: "分类编码：&nbsp;&nbsp;&nbsp;", labelWidth: "120", name: "code", maxLength:20, width: 200}
			]}
		],
		settings: {labelWidth: 80, inputWidth: 160}
	};
/**
* 表定义树逻辑表
* @param that
*/
function initLogicTableTree(that){
	var layout = that.aLayout;
	var tree = layout.attachTree();
	var ptItem = loadJson(AppActionURI.tableClassification + "!treeNode.json");
	that.prefixObj = {};
	for (var i = 0; i < ptItem.length; i++){
		var item = ptItem[i];
		that.prefixObj[item.userdata[1].content] = item.prefix;
	}
	layout.setWidth(240);
	layout.hideHeader();
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	// 这几个分类是需求中固定的
	var treeJson = {id:0, item:[
		{id:"-1", text: "逻辑表组", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child: true}
	]};
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(AppActionURI.tableTree + "!logicTableTree.json");
	tree.loadJSONObject(treeJson);
	//点击节点刷新右边列表
	tree.attachEvent("onClick", function(nId) {
		// 树节点ID
		var treeType = "logic";
		nodeId = nId;
		type = tree.getUserData(nId,"type");
		treeName = tree.getItemText(nId);
		classification = tree.getUserData(nId,"classification");
		if (nId == "-1") {
			initLogicGroupCreate(that);
		} else if (type == "2" && classification == "LG") {
			initLogicGroupDetailCreate(that, nodeId.substring(3));
		} else if (type == "1" && classification == "LT") {
			initPhysicalTableCreate(that, treeType);
		} else {
			initLayoutBContent(that, nodeId, null, treeName);
		}
	});
	/** 刷新树节点*/
	that.reloadTreeItem = function(nId) {
		if (nId) {
			tree.refreshItem(nId);
		} else {
			tree.refreshItem(nodeId);
		}
	};
}

/**
* 表定义树工作流表
* @param that
*/
function initWorkflowTableTree(that){
	var moduleUrl = AppActionURI.tableTree;
	var layout = that.aLayout;
	layout.setWidth(240);
	layout.hideHeader();
	tree = layout.attachTree();
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
		{id:"-W", text: "工作流", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1, userdata:[{name:"type",content:"0"},{name:"classification",content:"W"}]}
	]};
	// 初始右键菜单
	var treeMenu = new dhtmlXMenuObject();
	treeMenu.renderAsContextMenu();
	treeMenu.loadXMLString("<menu><item id='1' text='新增表分类'/><item id='2' text='修改表分类'/><item id='3' text='删除表分类'/></menu>");
	treeMenu.attachEvent("onClick", function(id) {
		GWIN_WIDTH = 400;
		GWIN_HEIGHT = 240;
		if (id == "1") {
			tableTreeAreaForm = openNewWindow(tableTreeAreaFormData, initTableTreeAreaFormToolbar, 400, 200);
			tableTreeAreaForm.setItemValue("parentId", contextMenuNodeId);
			tableTreeAreaForm.setItemValue("classification", classification);
		} else if (id == "2") {
			MODEL_URL = moduleUrl;
			tableTreeAreaForm = openEditWindow(tableTreeAreaFormData, contextMenuNodeId, initTableTreeAreaFormToolbar, 400, 200);
		} else if (id == "3") {
			dhtmlx.confirm({
				type:"confirm",
				text: getMessage("delete_warning"),
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
				if (flag) {
						dhtmlxAjax.get(AppActionURI.tableTree + "/" + contextMenuNodeId + ".json?_method=delete", function(loader) {
			                jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			                if (typeof jsonObj == 'string') {
			                	jsonObj = eval("(" + jsonObj + ")");
			                }
			                if (jsonObj.success) {
			                    dhtmlx.message(getMessage("delete_success"));
			                    var parId = tree.getParentId(contextMenuNodeId);
			                    tree.refreshItem(parId);
			                } else {
			                	dhtmlx.message(isNotEmpty(jsonObj.message) ? jsonObj.message : getMessage("save_failure"));
			                }
			            });
					}
				}
			});
		}
	});
	tree.enableContextMenu(treeMenu);
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(moduleUrl+"!tree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,child&P_UD=type,classification,tableLabel");
	tree.loadJSONObject(treeJson);
	//点击节点刷新右边列表
	tree.attachEvent("onClick", function(nId) {
		// 树节点ID
		var treeType = "physical";
		nodeId = nId;
		type = tree.getUserData(nId,"type");
		treeName = tree.getItemText(nId);
		classification = tree.getUserData(nId,"classification");
		if ("-PT" == nId) {
			initLayoutBHelp(that);
		} else if (type == "1") {
			initLayoutBContent(that, nodeId, null, treeName);
		} else if(type == "0") {
			initPhysicalTableCreate(that, treeType);
		}
		
	});
	tree.attachEvent("onBeforeContextMenu", function(nId) {
		contextMenuNodeId = nId;
		classification = tree.getUserData(nId,"classification");
		type = tree.getUserData(nId,"type");
		if (nId == null || nId == "") {
			treeMenu.hideItem("1");
			treeMenu.hideItem("2");
			treeMenu.hideItem("3");
		} else if(nId == "-W") {//工作流节点，只能新增子节点，不能修改删除
			treeMenu.showItem("1");
			treeMenu.hideItem("2");
			treeMenu.hideItem("3");
		} else{
			treeMenu.showItem("1");
			treeMenu.showItem("2");
			treeMenu.showItem("3");
		}
		return true;
	});
	/** 刷新树节点*/
	that.reloadTreeItem = function(nId) {
		if (nId) {
			tree.refreshItem(nId);
		} else {
			tree.refreshItem(nodeId);
		}
	};
}

/**
 * 配置方式
 * @param that
 */
function initConfigType(that) {
	var formJson = [
        {type: "combo", name: "treeStruct", className: "dhx_toolbar_form", style:"font-size:11px;", width: 160, 
        	options:[{value: "physical", text:"以物理表方式"},
                     {value: "logic", text:"以逻辑表组方式"},
                     {value: "workflow", text:"以工作流方式"}
        	]
        }];
 	var form = new dhtmlXForm("form$pos",formJson);
 	form.attachEvent("onChange", function(itemId, value) {
		if ("logic" == value) {
			initLogicTableTree(that);
		} else if ("physical" == value){
			initPhysicalTableTree(that);
		} else if ("workflow" == value){
			initWorkflowTableTree(that);
		}
 	} );
}
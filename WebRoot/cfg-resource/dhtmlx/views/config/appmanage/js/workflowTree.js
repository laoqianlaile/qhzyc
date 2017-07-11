/**
 * 工作流分类树初始化
 */
function initWorkflowTree(that) {
	// 页面设置
	var layout = that.aLayout;
	that.tree = layout.attachTree();
	
	var _this = this;
	var tree = that.tree;
	var moduleUrl = AppActionURI.workflowTree;
	var treeForm;
	var contextMenuNodeId = "-1";
	var treeFormJson = {
			format: [
				{type: "block", width: "350", list:[
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "id"},
					{type: "hidden", name: "parentId"},
					{type: "hidden", name: "showOrder"},
					{type: "input", label: "分类名称：", labelWidth: "120", name: "name", maxLength:100, required: true, width: 200}
				]}
			],
			settings: {labelWidth: 80, inputWidth: 160}
		};
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	var rootItems = [{id:'-1',text:"公用流程", child:1, userdata:[{name:"type",content:"-1"}]}];
	var systemMenus = loadJson(contextPath + "/menu/menu!search.json?Q_EQ_parentId=-1&P_orders=showOrder&F_in=id,name");
	if (systemMenus && systemMenus.data.length > 0) {
		var mData = systemMenus.data;
		for (var i = 0, len = mData.length; i < len; i++) {
			if (mData[i].id == "sys_0") continue;
			rootItems.push({id: ("-CT" + mData[i].id),text:mData[i].name, child:1, userdata:[{name:"type",content:"-1"}]});
		}
	}

	var treeJson = {id:0, item:[
        {id:'-9',text:"工作流分类树", 
        	im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", 
        	child:1, userdata:[{name:"type",content:"-9"}],
        	open: true, item: rootItems}
    ]};
	// 初始右键菜单
	var treeMenu = new dhtmlXMenuObject();
	treeMenu.renderAsContextMenu();
	treeMenu.loadXMLString("<menu><item id='create' text='新增工作流分类'/><item id='update' text='修改工作流分类'/><item id='delete' text='删除工作流分类'/></menu>");
	treeMenu.attachEvent("onClick", function(id) {
		var treeId = ("-1" == contextMenuNodeId ? "-1" : contextMenuNodeId.substring(3));
		if (id == "create") {
			treeForm = openNewWindow(treeFormJson, initTreeFormToolbar, 400, 200);
			treeForm.setItemValue("parentId", treeId);
		} else if (id == "update") {
			treeForm = openEditWindow(treeFormJson, treeId, initTreeFormToolbar, 400, 200, moduleUrl);
		} else if (id == "delete") {
			dhtmlx.confirm({
				type:"confirm",
				text: getMessage("delete_warning"),
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
					if (flag) {
						var checkUrl = AppActionURI.workflowTree + "!checkDelete.json?id=" + treeId;
						var msg = loadJson(checkUrl);
						if (!msg.success) {
							dhtmlx.message("该节点有子节点，请先删除所有子节点！");
							return;
						}
						dhtmlxAjax.get(moduleUrl + "/" + treeId + ".json?_method=delete", function(loader) {
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
  	tree.setXMLAutoLoading(moduleUrl + "!tree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,child,im0,im1,im2&P_UD=type,tableId");
  	tree.loadJSONObject(treeJson);
  	tree.refreshItem("-1");
  	tree.selectItem("-1", false, "0");
	//点击节点刷新右边列表
	tree.attachEvent("onClick", function(nId) {
		if ("-9" == nId) {
			tree.selectItem("-1", true, "0");
			return ;
		}
		var type = tree.getUserData(nId, "type");
		// 树节点ID
		that.nodeId  = ("-1" == nId ? "-1" : nId.substring(3));
		that.treeId  = nId;
		that.nodeType= type;
		if ("-1" == type || "0" == type) {
			// 分类节点
			workflowDefineGrid(that);
		} else if ("1" == type) {
			// 流程节点
			workflowVersionGrid(that);
		} else if ("2" == type) {
			// 版本节点
			var pId = tree.getParentId(nId);
			app.isWorkflow = true;
			app.menuId  = that.nodeId;//pId.substring(3);
			app.viewId  = tree.getUserData(pId, "viewId");
			that.enableAssist = tree.getUserData(pId, "enableAssistTable");//是否有辅助意见
			app.tableId = tree.getUserData(pId, "tableId");
			if (isEmpty(app.tableId)) {
				dhtmlx.alert("该工作流定义末绑定业务表，请重新配置！");
				return;
			}
			workflowSetting(that);
		}
	});
	tree.attachEvent("onBeforeContextMenu", function(nId) {
		contextMenuNodeId = nId;
		var type = tree.getUserData(nId, "type");
		if (type == "-1") {
			treeMenu.showItem("create");
			treeMenu.hideItem("update");
			treeMenu.hideItem("delete");
		} else if("0" == type) {
			treeMenu.showItem("create");
			treeMenu.showItem("update");
			treeMenu.showItem("delete");
		} else{
			return false;
		}
		return true;
	});
	
	that.refreshCurrentNode = function() {
		tree.refreshItem(that.treeId);
		tree.selectItem(that.treeId);
	};
	
	that.updateCurrentNode = function(text, userdata) {
		tree.setItemText(that.treeId, text);
		if (!userdata) return;
		for (var i = 0; i < userdata.length; i++) {
			tree.setUserData(that.treeId, userdata[i].name, userdata[i].content);
		}
	};
	
	that.updateTreeNode = function(id, text, userdata) {
		id = ("-CV" + id);
		tree.setItemText(id, text);
		if (!userdata) return;
		for (var i = 0; i < userdata.length; i++) {
			tree.setUserData(id, userdata[i].name, userdata[i].content);
		}
	};

	that.insertTreeNode = function(id, text, userdata) {
		id = ("-CV" + id);
		var pId = tree.getParentId(that.treeId);
		tree.insertNewChild(pId, id, text);
		if (!userdata) return;
		for (var i = 0; i < userdata.length; i++) {
			tree.setUserData(id, userdata[i].name, userdata[i].content);
		}
		// tree.selectItem(id);
	};
	
	that.getCurrentStatus= function() {
		return tree.getUserData(that.treeId, "status");
	};
	
	function initTreeFormToolbar(toolBar) {
		toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
		toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
		toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
		toolBar.setAlign("right");
		toolBar.attachEvent("onClick", function(id) {
			if (id == "close") {
				dhxWins.window(WIN_ID).close();
			} else if (id == "save") {
				var id = treeForm.getItemValue("id");
	        	var name = treeForm.getItemValue("name");
	        	if (null == name || "" == name) {
					dhtmlx.alert("表定义分类名称不可为空！");
					return;
				}
	        	var checkUrl = moduleUrl + "!checkUnique.json?Q_EQ_name=" + name;
	        	dhtmlxAjax.get(checkUrl,function(loader) {
					var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
					if (false == jsonObj.success) {
						if ("OK" == jsonObj.message) {
							dhtmlx.message(getMessage("form_field_exist", "表定义分类名称"));
						} else {
							dhtmlx.message("唯一检查出错，请联系管理员！");
						}
					} else {//*/
						var saveUrl = moduleUrl;
						if (id == "") {
			        		treeForm.setItemValue("_method", "post");
			        	} else {
			        		saveUrl += "/" + id;
			        		treeForm.setItemValue("_method", "put");
			        	}
						treeForm.send(saveUrl, "post", function(loader, response){
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
}
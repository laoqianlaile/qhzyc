/**
 * 列表初始化
 */
function workflowDefineGrid(that) {
	var layout = that.bLayout;
	// 
	layout.detachToolbar();
	// 隐藏标题
	layout.hideHeader();
	// 初始变量
	var ttbar = layout.attachToolbar();
	var grid  = layout.attachGrid();
	var cfg   = {
			format: {
				headers: ["<center>工作流名称</center>","<center>工作流编码<center/>","<center>业务表名<center/>","<center>备注</center>"],
				cols:["workflowName", "workflowCode", "businessTableText","remark"],
				colWidths: ["180","120","180","*"],
				colTypes: ["ro","ro","ro","ro"],
				colAligns: ["left","center","left","left"]
			}
		};
	var url = getGridUrl(that);
	
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("add", 1, "新增", "new.gif");
	ttbar.addSeparator("top$septr$01", 2);
	ttbar.addButton("update", 3, "修改", "update.gif");
	ttbar.addSeparator("top$septr$01", 4);
	ttbar.addButton("del", 5, "删除", "delete.gif");
	ttbar.addSeparator("top$septr$02", 6);
	ttbar.addButton("refreshView", 7, "同步视图", "refresh.gif");
	
	ttbar.attachEvent("onClick", function(itemId) {
		if ("add" == itemId) {
			workflowDefineForm(that, "新增工作流");
		} else if ("update" == itemId) {
			var rowIds = grid.getSelectedRowId();
			if (null == rowIds || "" == rowIds) {
				dhtmlx.message(getMessage("select_record"));
				return;
			}
			if (rowIds.indexOf(",") > 0) {
				dhtmlx.message(getMessage("select_only_one_record"));
				return;
			}
			workflowDefineForm(that, "修改工作流", rowIds);
		} else if ("del" == itemId) {
			del();
		} else if ("refreshView" == itemId) {
			refreshView();
		}
	});
	//
	function add () {
		that.resetForm();
	};
	// 
	function del () {
		var rowIds = grid.getSelectedRowId();
		if (null == rowIds || "" == rowIds) {
			dhtmlx.message(getMessage("select_record"));
			return;
		}
		dhtmlx.confirm({
			type:"confirm",
			text: getMessage("delete_warning"),
			ok: "确定",
			cancel: "取消",
			callback: function(flag) {
				if (flag) {
					var cUrl = AppActionURI.workflowDefine + "!checkDelete.json?id=" + rowIds;
					var msg = loadJson(cUrl);
					if (!msg.success) {
						dhtmlx.message(msg.message);
						return;
					}
					var deleteUrl = AppActionURI.workflowDefine + "/" + rowIds + ".json?_method=delete";
					dhtmlxAjax.get(deleteUrl, function(loader) {
						var obj = eval("(" + loader.xmlDoc.responseText + ")");
						if (obj.success == false) {
							var id = obj.message;
							if (id == "ERROR") {
								dhtmlx.alert(getMessage("delete_failure"));
								return;
							}
							var name = grid.cells(id, 0).getValue();
							dhtmlx.alert("【" + name + "】有子节点，请先删除子节点！");
							return;
						} else {
							dhtmlx.message(getMessage("delete_success"));
							that.reloadDefineGrid();
							that.refreshCurrentNode();
						}
					});
				} else {
					return;
				}
			}
		});
	};
	
	/**
	 * 同步视图
	 */
	function refreshView() {
		var rowIds = grid.getSelectedRowId();
		if (null == rowIds || "" == rowIds || rowIds.indexOf(",") > 0) {
			dhtmlx.message("请选择一条记录，再同步视图！");
			return;
		}
		if (rowIds.indexOf(",") > 0) {
			dhtmlx.message("一次只能选择一条记录！");
			return;
		}
		var url = AppActionURI.workflowDefine + "!syncBusinessView.json?id=" + rowIds;
		dhtmlxAjax.get(url, function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				dhtmlx.message("同步成功！");
			} else {
				dhtmlx.message("同步失败！");
			}
		});
	};
	//
	initGridWithoutPageable(grid, cfg, url);
	grid.enableDragAndDrop(true);
	
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
	grid.attachEvent("onDrop", function(sId,tId,dId,sObj,tObj,sCol,tCol){
		if(undefined != tId) {
			var adjustUrl = AppActionURI.workflowDefine + "!adjustShowOrder?P_workflowTreeId=" + that.nodeId + "&P_sourceIds=" + sId + "&P_targetId=" + tId;
			dhtmlxAjax.get(adjustUrl, function(loader) {
				that.reloadDefineGrid();
				that.grid.selectRowById(sId);
				that.refreshCurrentNode();
			});
		}		
	});
	/**
	 * 列表数据加载URL
	 */
	function getGridUrl(that) {
		return AppActionURI.workflowDefine + "!search.json?Q_EQ_workflowTreeId=" + that.nodeId + "&P_orders=showOrder";
	}
	
	that.reloadDefineGrid = function () {
		loadGridData(grid, cfg, getGridUrl(that));
	};
}

function workflowDefineForm(that, title, id) {
	if ("-1" == that.nodeId || "0" != that.nodeType) {
		// 非工作流分类节点，不可新增工作流
		dhtmlx.alert("请选择工作流分类节点，再新增");
		return;
	}
	var subWin = createDhxWindow({
	        id : "win$add",
	        title : title,
	        width : 680,
	        height : 400
	    });
	var subWinTtbar = subWin.attachToolbar();
	subWinTtbar.setIconPath(TOOLBAR_IMAGE_PATH);
	subWinTtbar.addButton("top$save", 0, "保存", "save.gif");
	subWinTtbar.attachEvent("onClick", function(itemId, value) {
		if ("top$save" == itemId) {
			submit();
		}
	});
	//
	var subWinSbar = subWin.attachStatusBar();
	var subWinBtbar = new dhtmlXToolbarObject(subWinSbar.id);
	subWinBtbar.setIconPath(TOOLBAR_IMAGE_PATH);
	subWinBtbar.addButton("bottom$save", 0, "保存", "save.gif");
	subWinBtbar.addSeparator("bottom$septr$01", 2);
	subWinBtbar.addButton("bottom$close", 5, "关闭", "close.gif");
	subWinBtbar.setAlign("right");
	//
	subWinBtbar.attachEvent("onClick", function(itemId) {
		if ("bottom$save" == itemId) {
			submit();
		} else if ("bottom$close" == itemId) {
			subWin.close();
		}
	});
	//
	var form = subWin.attachForm(getWFFormJson(that));
	form.attachEvent("onChange", function(itemId, value, checked) {
		if ("boxApplyfor" == itemId) {
			changeBoxName(checked, "nameApplyfor", "申请箱");
		} else if ("boxTodo" == itemId) {
			changeBoxName(checked, "nameTodo", "待办箱");
		} else if ("boxHasdone" == itemId) {
			changeBoxName(checked, "nameHasdone", "已办箱");
		} else if ("boxComplete" == itemId) {
			changeBoxName(checked, "nameComplete", "办结箱");
		} else if ("boxToread" == itemId) {
			changeBoxName(checked, "nameToread", "待阅箱");
		} else if ("boxHasread" == itemId) {
			changeBoxName(checked, "nameHasread", "阅毕箱");
		}
	});
	
	// 
	form.attachEvent("onButtonClick", function(itemId) {
		if ("chooseTable" == itemId) {
			openTableWin();
		}
	});
	//
	loadFormData(id);
	
	/*****************************()****************************/
	function changeBoxName(checked, itemName, boxName) {
		if (checked) {
			form.enableItem(itemName);
			form.setItemValue(itemName, boxName);
		} else {
			form.setItemValue(itemName, "");
			form.disableItem(itemName);
		}
	}
	
	function loadFormData(id) {
		if (isEmpty(id)) return;
		var url = AppActionURI.workflowDefine + "!show.json?id=" + id;
		var formData = loadJson(url);
		form.setFormData(formData);
		form.setReadonly ("workflowCode", true)
		if ("1" === formData.boxHasread) form.enableItem("nameHasread");
		if ("1" === formData.boxToread) form.enableItem("nameToread");
	}
	
	// 
	function submit() {
		if (!form.validate()) {
			return;
		}
		var id = form.getItemValue("id");
		var code = form.getItemValue("workflowCode");
		var checkUrl = AppActionURI.workflowDefine + "!checkUnique.json?id=" + id + "&Q_EQ_workflowCode=" + code;
		var msg = loadJson(checkUrl);
		if (!msg.success) {
			dhtmlx.alert("编码重复，请修改！");
			return;
		}
		subWinTtbar.disableItem("top$save");
		subWinBtbar.disableItem("bottom$save");
		var sUrl = AppActionURI.workflowDefine + "!update.json";
		form.send(sUrl, "post", function(loader, response) {
			var formData = eval("(" + loader.xmlDoc.responseText + ")");
			if (formData.id == null || formData.id == "") {
				dhtmlx.message(getMessage("save_failure"));
				return;
			}
			form.setFormData(formData);
			that.reloadDefineGrid();
			that.refreshCurrentNode();
			dhtmlx.message(getMessage("save_success"));
			subWinBtbar.enableItem("bottom$save");
			subWinTtbar.enableItem("top$save");
		});
	}
	
	function openTableWin() {
		var tableId = "";
		var twin = createDhxWindow({
		        id : "win$add",
		        title : "选择业务表",
		        width : 400,
		        height : 400
		    });
		//
		var sbar = twin.attachStatusBar();
		var btbar = new dhtmlXToolbarObject(sbar.id);
		btbar.setIconPath(TOOLBAR_IMAGE_PATH);
		btbar.addButton("bottom$sure", 0, "确定", "save.gif");
		btbar.addSeparator("bottom$septr$01", 2);
		btbar.addButton("bottom$close", 5, "关闭", "close.gif");
		btbar.setAlign("right");
		
		btbar.attachEvent("onClick", function(itemId) {
			if ("bottom$sure" == itemId) {
				if (isEmpty(tableId)) {
					dhtmlx.message("请选择物理表！");
					return;
				}
				// 同一张表可以绑定多个流程
				//var id = form.getItemValue("id");
				//var checkUrl = AppActionURI.workflowDefine + "!checkUnique.json?id=" + id + "&Q_EQ_businessTableId=" + tableId;
				//dhtmlxAjax.get(encodeURI(checkUrl),function(loader) {
					//var msg = eval("(" + loader.xmlDoc.responseText + ")");
					var showName = tree.getItemText(tableId);
					//if (msg.success) {
						form.setItemValue("businessTableId", tableId);
						form.setItemValue("businessTableText", showName);
						twin.close();
					//} else {
					//	dhtmlx.alert("表（" + showName + "）已经绑定过工作流，请选择其他表！");
					//}
				//});
			} else {
				twin.close();
			}
		});
		
		var tree = twin.attachTree();
		tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
		tree.attachEvent("onMouseIn", function(id) {
			tree.setItemStyle(id, "background-color:#D5E8FF;");
		});
		tree.attachEvent("onMouseOut", function(id) {
			tree.setItemStyle(id, "background-color:#FFFFFF;");
		});
		// 物理表分类节点
		var ptItem = loadJson(AppActionURI.tableClassification + "!treeNode.json");
		// 这几个分类是需求中固定的
		var treeJson = {id:0, item:[
			{id:"-PT", text: "物理表", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item: ptItem}
		]};
		tree.setDataMode("json");
		tree.enableSmartXMLParsing(true);
		tree.setXMLAutoLoading(AppActionURI.tableTree + "!tree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId");
		tree.loadJSONObject(treeJson);
		//tree.refreshItem("-1");
		//点击节点刷新右边列表
		tree.attachEvent("onClick", function(nId) {
			var type = tree.getUserData(nId, "type");
			if (type=="1") {
				tableId = nId;
			} else {
				tableId = "";
			}
		});
	}
}

function getWFFormJson(that) {
	var formJson = [
       		{type: "setting", labelWidth: 80, inputWidth: 200},
       		{type: "hidden", name: "_method"},
       		{type: "hidden", name: "id"},
       		{type: "hidden", name: "workflowTreeId", value: that.nodeId},
       		{type: "hidden", name: "businessTableId"},
       		{type: "hidden", name: "showOrder"},
       		{type: "block", name:"block_name", width: 660, list:[
    				{type: "input", label: "名称", labelAlign:"right", labelWidth: 80, name: "workflowName", offsetTop:20, required:true, width:200, maxLength:50, tooltip:"请输入工作流名称！"},
    				{type: "newcolumn"},
    				{type: "input", label: "编码", labelAlign:"right", labelWidth: 80, name: "workflowCode", offsetTop:20, required:true, width:200, maxLength:16, validate: "ValidAplhaNumeric", tooltip:"请输入工作流编码(最长为16个字符)！"},
    				]},
           	{type: "block", name: "block_table", width: 660, list:[
    				{type: "input", offsetTop:10, label: "表", labelAlign:"right", labelWidth: 80, name: "businessTableText", readonly:true, required:true, width:160},
    				{type: "newcolumn"},
    				{type: "button", offsetTop:10, width: 40, name: "chooseTable", value : "选择"},
    				{type: "newcolumn"},
    				{type: "itemlabel", offsetTop:10, label: "启用相关表：", labelAlign:"right", labelWidth: 80, width:80},
    				{type: "newcolumn"},
            		{type: "checkbox", offsetTop:10, label: "附件表",  width:80, name: "enableDocumentTable", position:"label-right", labelAlign:"left"},
    				{type: "newcolumn"},
            		{type: "checkbox", offsetTop:10, label: "审批意见表", width:80, name: "enableConfirmTable", position:"label-right", labelAlign:"left"},
    				{type: "newcolumn"},
            		{type: "checkbox", offsetTop:10, label: "辅助意见表", width:80, name: "enableAssistTable", position:"label-right", labelAlign:"left"}
       			]},
       		{type: "block", name: "block_box", width: 660, offsetTop:10, list:[
    				{type: "itemlabel", offsetTop:10, label: "工作箱：", labelAlign:"right", labelWidth: 80, width:80},
    				{type: "newcolumn"},
            		{type: "checkbox", offsetTop:10, label: "申请箱", checked: true, labelWidth: 50, name: "boxApplyfor", position:"label-right", labelAlign:"left"},
            		{type: "checkbox", offsetTop:10, label: "已办箱", checked: true, labelWidth: 50, name: "boxHasdone", position:"label-right", labelAlign:"left"},
            		{type: "checkbox", offsetTop:10, label: "待阅箱", labelWidth: 50, name: "boxToread", position:"label-right", labelAlign:"left"},
    				{type: "newcolumn"},
    				{type: "input", offsetTop:10, label: "", value: "申请箱", labelAlign:"right", labelWidth: 10, name: "nameApplyfor", width:128},
    				{type: "input", offsetTop:10, label: "", value: "已办箱", labelAlign:"right", labelWidth: 10, name: "nameHasdone", width:128},
    				{type: "input", offsetTop:10, label: "", disabled: true, labelAlign:"right", labelWidth: 10, name: "nameToread", width:128},
    				{type: "newcolumn"},
    				{type: "itemlabel", offsetTop:10, label: "　", labelAlign:"right", labelWidth: 80, width:80},
    				{type: "newcolumn"},
            		{type: "checkbox", offsetTop:10, label: "待办箱", checked: true, labelWidth: 50, name: "boxTodo", position:"label-right", labelAlign:"left"},
            		{type: "checkbox", offsetTop:10, label: "办结箱", checked: true, labelWidth: 50, name: "boxComplete", position:"label-right", labelAlign:"left"},
            		{type: "checkbox", offsetTop:10, label: "已阅箱", labelWidth: 50, name: "boxHasread", position:"label-right", labelAlign:"left"},
    				{type: "newcolumn"},
    				{type: "input", offsetTop:10, label: "", value: "待办箱", labelAlign:"right", labelWidth: 10, name: "nameTodo", width:128},
    				{type: "input", offsetTop:10, label: "", value: "办结箱", labelAlign:"right", labelWidth: 10, name: "nameComplete", width:128},
    				{type: "input", offsetTop:10, label: "", disabled: true, labelAlign:"right", labelWidth: 10, name: "nameHasread", width:128},
       			]},
       		{type: "block", name: "block_remark", width: 660, offsetTop:10, list:[
    				{type: "input", offsetTop:10, label: "备注：", labelAlign:"right", labelWidth: 80, name: "remark", rows:3, width:480, maxLength:100}
          		]}
       	];
	return formJson;
}


/**
 * 列表初始化
 */
function workflowVersionGrid(that) {
	var layout = that.bLayout;
	// 
	layout.detachToolbar();
	// 隐藏标题
	layout.hideHeader();
	// 初始变量
	var ttbar = layout.attachToolbar();
	var grid  = layout.attachGrid();
	var cfg  = {
			format: {
				headers: ["<center>版本号</center>","<center>状态<center/>","<center>备注</center>"],
				cols:["version", "statusStr","remark"],
				colWidths: ["180","120","*"],
				colTypes: ["ro","ro","ro"],
				colAligns: ["left","center","left"]
			}
		};
	var url = getGridUrl(that);
	
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("add", 1, "新增", "new.gif");
	ttbar.addSeparator("top$septr$01", 2);
	ttbar.addButton("updaete", 3, "修改", "update.gif");
	ttbar.addSeparator("top$septr$01", 4);
	ttbar.addButton("del", 5, "删除", "delete.gif");
	
	ttbar.attachEvent("onClick", function(itemId) {
		if ("add" == itemId) {
			workflowVersionForm(that, "新增工作流");
		} else if ("updaete" == itemId) {
			var rowIds = grid.getSelectedRowId();
			if (null == rowIds || "" == rowIds) {
				dhtmlx.message(getMessage("select_record"));
				return;
			}
			if (rowIds.indexOf(",") > 0) {
				dhtmlx.message(getMessage("select_only_one_record"));
				return;
			}
			workflowVersionForm(that, "修改工作流", rowIds);
		} else if ("del" == itemId) {
			del();
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
		if (rowIds.indexOf(",") > 0) {
			dhtmlx.message("一次只能删除一个版本号！");
			return;
		}
		dhtmlx.confirm({
			type:"confirm",
			text: getMessage("delete_warning"),
			ok: "确定",
			cancel: "取消",
			callback: function(flag) {
				if (flag) {
					var cUrl = AppActionURI.workflowVersion + "!checkDelete.json?id=" + rowIds;
					var msg = loadJson(cUrl);
					if (!msg.success) {
						dhtmlx.message(msg.message);
						return;
					}
					var deleteUrl = AppActionURI.workflowVersion + "/" + rowIds + ".json?_method=delete";
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
							that.reloadVersionGrid();
							that.refreshCurrentNode();
						}
					});
				} else {
					return;
				}
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
			var adjustUrl = AppActionURI.workflowVersion + "!adjustShowOrder?P_workflowTreeId=" + that.nodeId + "&P_sourceIds=" + sId + "&P_targetId=" + tId;
			dhtmlxAjax.get(adjustUrl, function(loader) {
				that.reloadVersionGrid();
				that.grid.selectRowById(sId);
				that.refreshCurrentNode();
			});
		}		
	});
	/**
	 * 列表数据加载URL
	 */
	function getGridUrl(that) {
		return AppActionURI.workflowVersion + "!search.json?Q_EQ_workflowId=" + that.nodeId + "&P_orders=showOrder";
	}
	
	that.reloadVersionGrid = function () {
		loadGridData(grid, cfg, getGridUrl(that));
	};
}

function workflowVersionForm(that, title, id) {
	if ("1" != that.nodeType) {
		// 非工作流分类节点，不可新增工作流
		dhtmlx.alert("请选择工作流节点，再新增");
		return;
	}
	var subWin = createDhxWindow({
	        id : "win$add",
	        title : title,
	        width : 420,
	        height : 300
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
	var form = subWin.attachForm(getWFVersionFormJson(that));
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
		var url = AppActionURI.workflowVersion + "!show.json?id=" + id;
		var formData = loadJson(url);
		form.setFormData(formData);
	}
	
	// 
	function submit() {
		if (!form.validate()) {
			return;
		}
		var wid = form.getItemValue("workflowId");
		var id = form.getItemValue("id");
		var vs = form.getItemValue("version");
		var checkUrl = AppActionURI.workflowVersion + "!checkUnique.json?id=" + id + "&Q_EQ_version=" + vs + "&Q_EQ_workflowId=" + wid;
		var msg = loadJson(checkUrl);
		if (!msg.success) {
			dhtmlx.alert("版本号重复，请修改！");
			return;
		}
		var sUrl = AppActionURI.workflowVersion;
        if (id == "") {
            form.setItemValue("_method", "post");
        } else {
        	sUrl = sUrl + "/" + id;
            form.setItemValue("_method", "put");
        }
        sUrl = sUrl + ".json";
		form.send(sUrl, "post", function(loader, response) {
			var formData = eval("(" + loader.xmlDoc.responseText + ")");
			if (formData.id == null || formData.id == "") {
				dhtmlx.message(getMessage("save_failure"));
				return;
			}
			form.setFormData(formData);
			that.reloadVersionGrid();
			that.refreshCurrentNode();
			dhtmlx.message(getMessage("save_success"));
			subWin.close();
		});
	}
	
}

function getWFVersionFormJson(that) {
	var formJson = [
       		{type: "hidden", name: "_method"},
       		{type: "hidden", name: "id"},
       		{type: "hidden", name: "workflowId", value: that.nodeId},
       		{type: "hidden", name: "status", value: "undefined"},
       		{type: "hidden", name: "showOrder"},
       		{type: "block", name:"block_name", width: 400, list:[
    				{type: "input", label: "版本号", labelAlign:"right", labelWidth: 80, name: "version", offsetTop:20, required:true, width:300, maxLength:50, tooltip:"请输入工作流名称！"},
    				]},
       		{type: "block", name: "block_remark", width: 400, offsetTop:10, list:[
    				{type: "input", offsetTop:10, label: "备注：", labelAlign:"right", labelWidth: 80, name: "remark", rows:3, width:300, maxLength:249}
          		]}
       	];
	return formJson;
}


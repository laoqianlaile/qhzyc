MODEL_URL = contextPath + "/resource/resource";
var systemId, canCreateButtonResource, resourceForm, buttonTree, buttonTreeKey;
var buttonIds;
var resourceFormData = {
        format: [
            {type: "block", width: "650", list:[
                {type: "hidden", name: "_method"},
                {type: "hidden", name: "id"},
                {type: "hidden", name: "type"},
                {type: "hidden", name: "parentId"},
                {type: "hidden", name: "systemId"},
                {type: "hidden", name: "buttonIds"},
                {type: "hidden", name: "canUse", value: "1"},
                {type: "hidden", name: "showOrder"},
                {type: "input", label: "资源名称:", name: "name", maxLength:100, required: true, tooltip: '资源名称不能为空'},
                {type: "input", label: "备注:&nbsp;&nbsp;&nbsp;", name: "remark", rows:"3", maxLength:"500"}
            ]}
        ],
        settings: {labelWidth: 120, inputWidth: 400, labelAlign : "right", offsetTop: "3"}
    };
var resourceGridData = {
    	format: {
    		headers: ["<center>名称</center>", "<center>备注</center>", ""],
    		cols: ["name", "remark"],
    		userDatas: ["targetId"],
    		colWidths: ["150", "300", "*"],
    		colTypes: ["ro", "ro", "ro"],
    		colAligns: ["left", "left"],
    		colTooltips: ["true", "true", "false"]
    	}
    };
/**
 * 初始化资源列表
 */
function initSelfGrid() {
    dataGrid.setImagePath(IMAGE_PATH);
    dataGrid.setHeader(resourceGridData.format.headers.toString());
    dataGrid.setInitWidths(resourceGridData.format.colWidths.toString());
    dataGrid.setColTypes(resourceGridData.format.colTypes.toString());
    dataGrid.setColAlign(resourceGridData.format.colAligns.toString());
    if (resourceGridData.format.colTooltips) {
        dataGrid.enableTooltips(resourceGridData.format.colTooltips.toString());
    }
    dataGrid.setSkin(Skin);
    dataGrid.init();
    dataGrid.enableMultiselect(true);
    dataGrid.enableDragAndDrop(true);
    dataGrid.attachEvent("onBeforeDrag", function(id) {
        return this.cells(id, 0).getValue();
    });
    dataGrid.attachEvent("onDrag", function(sId, tId) {
        if (sId.indexOf(",") != -1) {
            dhtmlx.message(getMessage("drag_one_record"));
            return false;
        }
        return true;
    });
    dataGrid.attachEvent("onDrop", function(sId, tId) {
        loadJson(MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId);
        refreshResourceGrid();
        tree.refreshItem(currentTreeNodeId);
    });
    dataGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    if (pageable) {
        pagesize = getCookie("pagesize") || PAGE_SIZE;
        dataGrid.enablePaging(true, pagesize, 1, statusBar);
        dataGrid.setPagingSkin('toolbar', Skin);
    }
}
/**
 * 刷新资源列表
 */
function refreshResourceGrid() {
    QUERY_URL = MODEL_URL + "!search.json?Q_EQ_parentId=" + tree.getSelectedItemId() + "&&Q_EQ_canUse=1&P_orders=showOrder";
    search(dataGrid, resourceGridData);
}
/**
 * 初始化资源列表工具条
 */
function initSelfToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("add", 0, "新增", "new.gif");
    toolBar.addSeparator("septr$01", 2);
    toolBar.addButton("update", 3, "修改", "update.gif");
    toolBar.addSeparator("septr$02", 4);
    toolBar.addButton("delete", 5, "删除", "delete.gif");
    toolBar.addSeparator("septr$03", 6);
    toolBar.addButton("sync", 7, "同步到系统管理平台", "sync.gif");
    toolBar.addSeparator("septr$04", 8);
    toolBar.addButton("refresh", 9, "刷新", "refresh.gif");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "add") {
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var winWidth = 700;
            var winHeight = 500;
            if (winHeight > document.body.clientHeight) {
                winHeight = document.body.clientHeight;
            }
            var win = dhxWins.createWindow(WIN_ID, 0, 0, winWidth, winHeight);
            win.setModal(true);
            win.setText("新增");
            win.center();
            initBindingButton(win);
        } else if (id == "update") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshResourceGrid();
                return;
            }
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var winWidth = 700;
            var winHeight = 500;
            if (winHeight > document.body.clientHeight) {
                winHeight = document.body.clientHeight;
            }
            var win = dhxWins.createWindow(WIN_ID, 0, 0, winWidth, winHeight);
            win.setModal(true);
            win.setText("修改");
            win.center();
            initBindingButton(win, selectId);
        } else if (id == "delete") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshResourceGrid();
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        dhtmlxAjax.get(MODEL_URL + "/" + selectId + "?_method=delete", function() {
                        	dhtmlx.message(getMessage("delete_success"));
                            refreshResourceGrid();
                            tree.refreshItem(currentTreeNodeId);
                        });
                    }
                }
            });
        } else if (id == "sync") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshResourceGrid();
                return;
            }
            var systemIds = "";
            var resourceIdArr = selectId.split(",");
            for (var i in resourceIdArr) {
                systemIds += "," + dataGrid.getUserData(resourceIdArr[i], "targetId");
            }
            systemIds = systemIds.substring(1, systemIds.length);
            dhtmlxAjax.get(MODEL_URL + "!syncToAuth.json?systemIds=" + systemIds, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message("同步成功！");
                } else {
                    dhtmlx.message("同步失败！");
                }
            });
        } else if (id == "refresh") {
            refreshResourceGrid();
            tree.refreshItem(currentTreeNodeId);
        }
    });
}
function initBindingButton(win, id) {
    var bindingButtonLayout = win.attachLayout("2E");
    var formCell = bindingButtonLayout.cells("a");
    var treeCell = bindingButtonLayout.cells("b");
    formCell.hideHeader();
    treeCell.hideHeader();
    formCell.setHeight(110);
    resourceForm = formCell.attachForm(initDetailFormFormat(resourceFormData));
    if (id && id != "") {
        var url = MODEL_URL + "/" + id + ".json?_method=get";
        var formData = loadJson(url);
        loadFormData(resourceForm, formData);
    }
    initBindingButtonTree(treeCell, id);
    var statusBar = win.attachStatusBar();
    var bindingButtonToolbar = new dhtmlXToolbarObject(statusBar);
    initBindingButtonToolbar(bindingButtonToolbar);
}
function initBindingButtonTree(treeCell, id) {
    buttonTree = treeCell.attachTree();
    buttonTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    buttonTree.attachEvent("onMouseIn", function(id) {
        buttonTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    buttonTree.attachEvent("onMouseOut", function(id) {
        buttonTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var rootText = tree.getItemText(currentTreeNodeId);
    var treeJson = {
        id : 0,
        item : [{
            id : "M_" + currentTreeNodeId,
            text : rootText,
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            child : 1
        }]
    };
    buttonTree.setDataMode("json");
    buttonTree.enableSmartXMLParsing(true);
    buttonTree.enableCheckBoxes(true, true);
    buttonTree.enableThreeStateCheckboxes(true);
    var current = new Date();
    buttonTreeKey = current.getTime() + "_" + Math.floor(Math.random()*100);
    var buttonTreeUrl = MODEL_URL + "!getButtonTree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_OPEN=true&P_buttonTreeKey=" + buttonTreeKey + "&P_menuResourceId=" + currentTreeNodeId;
    if (id && id != "") {
        buttonTreeUrl += "&P_resourceId=" + id;
    }
    buttonTree.setXMLAutoLoading(buttonTreeUrl);
    buttonTree.loadJSONObject(treeJson);
    buttonTree.refreshItem("M_" + currentTreeNodeId);
}
function initBindingButtonToolbar(toolBar) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(buttonName) {
        if (buttonName == "close") {
            dhxWins.window(WIN_ID).close();
        } else if (buttonName == "save") {
            if (!resourceForm.validate()) {
                return;
            }
            var id = resourceForm.getItemValue("id");
            var name = resourceForm.getItemValue("name");
            var result = eval("(" + loadJson(MODEL_URL + "!validateResource.json?id=" + id + "&name=" + encodeURIComponent(name) + "&parentId=" + tree.getSelectedItemId() + "&systemId=" + systemId) + ")");
            if (result.nameExist) {
                dhtmlx.message("同一级下资源名称已经存在，请修改！");
                return;
            }
            var checkedIds = buttonTree.getAllCheckedBranches();
            if (!checkedIds) {
                dhtmlx.message("请选择按钮！");
                return;
            }
            var checkedIdArr = checkedIds.split(",");
            buttonIds = "";
            for (var i=0; i<checkedIdArr.length; i++) {
                if (checkedIdArr[i].indexOf("CVB_") == 0 || checkedIdArr[i].indexOf("CDB_") == 0) {
                    buttonIds += checkedIdArr[i] + ",";
                }
            }
            buttonIds = buttonIds.substring(0, buttonIds.length-1);
            resourceForm.setItemValue("buttonIds", buttonIds);
            if (id == "") {
                SAVE_URL = MODEL_URL + ".json";
                resourceForm.setItemValue("_method", "post");
                resourceForm.setItemValue("parentId", tree.getSelectedItemId());
            } else {
                SAVE_URL = MODEL_URL + "/" + id + ".json";
                resourceForm.setItemValue("_method", "put");
            }
            resourceForm.setItemValue("systemId", systemId);
            resourceForm.send(SAVE_URL, "post", function(loader, response) {
                dhtmlx.message(getMessage("save_success"));
                refreshResourceGrid();
                dhxWins.window(WIN_ID).close();
            });
        }
    });
}
function enableToolBar() {
    toolBar.enableItem("add");
    toolBar.enableItem("update");
    toolBar.enableItem("delete");
    toolBar.enableItem("refresh");
}
function disableToolBar() {
    toolBar.disableItem("add");
    toolBar.disableItem("update");
    toolBar.disableItem("delete");
    toolBar.disableItem("refresh");
}
/**
 * 页面初始化方法
 */
function init() {
	dhxLayout = new dhtmlXLayoutObject("content", "2U");
	dhxLayout.cells("a").hideHeader();
	dhxLayout.cells("b").hideHeader();
	dhxLayout.cells("a").setWidth(240);
	dhxLayout.setAutoSize();
	
	tree = dhxLayout.cells("a").attachTree();
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	tree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
	tree.setXMLAutoLoading(MODEL_URL + "!getResourceTree.json?E_model_name=tree&F_in=name,hasChild&P_UD=systemId,canCreateButtonResource&P_filterId=parentId&P_orders=showOrder");
	var treeJson = {id:0, item:[{id:-1,text:"资源", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif",item:[]}]};
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
 	tree.attachEvent("onClick", function(id) {
		currentTreeNodeId = "" + id;
		dhxLayout.cells("b").hideHeader();
		if (!toolBar) {
			toolBar = dhxLayout.cells("b").attachToolbar();
		    statusBar = dhxLayout.cells("b").attachStatusBar();
		    initSelfToolBar();
	    }
	    if (!dataGrid) {
		    dataGrid = dhxLayout.cells("b").attachGrid();
		    initSelfGrid();
	    }
	    if (id == "-1") {
            systemId = "";
            toolBar.enableItem("sync");
        } else {
            systemId = this.getUserData(id, "systemId");
            toolBar.disableItem("sync");
        }
	    canCreateButtonResource = this.getUserData(id, "canCreateButtonResource");
	    if (canCreateButtonResource == "1") {
	        enableToolBar();
	    } else {
	        disableToolBar();
	    }
		refreshResourceGrid();
	});
	tree.selectItem("-1", true);
}
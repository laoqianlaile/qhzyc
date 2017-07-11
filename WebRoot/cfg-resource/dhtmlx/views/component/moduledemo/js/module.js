var TREE_URL = contextPath + "/module-category";
var MODULE_MODEL_URL = contextPath + "/module-demo";
var MODULE_CATEGORY_MODEL_URL = contextPath + "/module-category";
var categoryForm, formToolbar;
var categoryFormData = {
	format: [
		{type: "block", width: "350", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "parentId"},
			{type: "input", label: "分类名称：", labelWidth: "100", name: "name", maxLength:50, required: true, width: 200}
		]}
	],
	settings: {labelWidth: 80, inputWidth: 160}
};
var gridData = {
	format: {
		headers: ["<center>名称</center>", "<center>值</center>", "<center>说明</center>", ""],
		cols: ["name", "value", "remark"],
		colWidths: ["120", "120", "120", "*"],
		colTypes: ["ro", "ro", "ro", "ro"],
		colAligns: ["left", "left", "left"]
	}
};
var formData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "categoryId"},
			{type: "hidden", name: "showOrder"},
			{type: "input", label: "名称:", name: "name", maxLength:50, required: true},
			{type: "input", label: "说明:", name: "remark", maxLength:50, required: true},
			{type: "newcolumn"},
			{type: "input", label: "值:", name: "value", maxLength:20, required: true}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
// 初始化grid
function initGrid1(dragable) {
    dataGrid.setImagePath(IMAGE_PATH);
    dataGrid.setHeader(gridData.format.headers.toString());
    dataGrid.setInitWidths(gridData.format.colWidths.toString());
    dataGrid.setColTypes(gridData.format.colTypes.toString());
    dataGrid.setColAlign(gridData.format.colAligns.toString());
    dataGrid.setSkin(Skin);
    dataGrid.init();
    dataGrid.enableMultiselect(true);
    if (dragable) {
        dataGrid.enableDragAndDrop(true);
    }
    dataGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    dataGrid.attachEvent("onRowSelect", function(rId, cInd) {
    	MODEL_URL = MODULE_MODEL_URL;
        selectRow();
    });
    dataGrid.attachEvent("onDrag", function(sId, tId) {
        if (sId.indexOf(",") != -1) {
            alert("只能拖动一条记录!");
            return false;
        }
        return true;
    });
    dataGrid.attachEvent("onDrop", function(sId, tId) {
        loadJson(MODULE_MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId);
        searchDataGrid();
    });
    if (pageable) {
        pagesize = getCookie("pagesize") || PAGE_SIZE;
        dataGrid.enablePaging(true, pagesize, 1, statusBar);
        dataGrid.setPagingSkin('toolbar', Skin);
    }
    searchDataGrid();
}
// 初始化toolBar
function initToolBar1(toolBar) {
    toolBar.setIconsPath(IMAGE_PATH);
    toolBar.addButton("add", 0, "新增", "new.gif");
    toolBar.addSeparator("septr$01", 2);
    toolBar.addButton("refresh", 3, "刷新", "true.gif");
    toolBar.addSeparator("septr$01", 4);
    toolBar.addButton("buttonReserve", 5, "按钮预留区");
    CFG_addToolbarButtons(toolBar, "toolBar", 6);
    toolBar.attachEvent('onClick', function(id) {
        if (id == "add") {
            initFormData(detailForm, true);
            return;
        } else if (id == "refresh") {
            searchDataGrid();
            return;
        } else if (id == "buttonReserve") {
	        CFG_clickButtonOrTreeNode("buttonReserve", "按钮预留区", "2");
        	return;
        }
        CFG_clickToolbar("toolBar", id);
    });
}
function initFormToolBar() {
	if (!formToolbar) {
    	formToolbar = dhxLayout.cells("c").attachToolbar();
    	formToolbar.setIconsPath(IMAGE_PATH);
		formToolbar.addButton("save", 1, "保存", "save.gif");
		formToolbar.addSeparator("septr$01", 2);
		formToolbar.addButton("reset", 3, "重置", "ruku.gif");
		formToolbar.attachEvent('onClick', function(id) {
	       	if (id == "save") {
	       		var id = detailForm.getItemValue("id");
	        	var name = detailForm.getItemValue("name");
	        	var result = eval("(" + loadJson(MODULE_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + encodeURIComponent(name)) + ")");
	        	if (result.nameExist) {
	        		alert("名称已经存在，请修改！");
	        		return;
	        	}
	        	if (id == "") {
	        		SAVE_URL = MODULE_MODEL_URL;
	        		detailForm.setItemValue("_method", "post");
	        	} else {
	        		SAVE_URL = MODULE_MODEL_URL + "/" + id;
	        		detailForm.setItemValue("_method", "put");
	        	}
        		detailForm.send(SAVE_URL, "post", function(loader, response){
			    	searchDataGrid();
        		});
        		if (id == "") {
	        		initFormData(detailForm, false);
        		}
	        } else if (id == "reset") {
	        	initFormData(detailForm, false);
	        }
	    });
	}
}
function searchDataGrid() {
	QUERY_URL = MODULE_MODEL_URL + "!search.json?Q_EQ_categoryId=" + tree.getSelectedItemId() + "&P_orders=showOrder";
	search();
}
function initCategoryFormToolbar(toolBar) {
	toolBar.setIconsPath(IMAGE_PATH);
	toolBar.addButton("submit", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
	toolBar.addButton("close", 3, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
	toolBar.setAlign("right");
	toolBar.attachEvent("onClick", function(id) {
		if (id == "close") {
			dhxWins.window(WIN_ID).close();
		} else if (id == "submit") {
			var id = categoryForm.getItemValue("id");
        	var name = categoryForm.getItemValue("name");
        	var result = eval("(" + loadJson(MODULE_CATEGORY_MODEL_URL + "!validateFields.json?id=" + id + "&parentId=" + contextMenuNodeId + "&name=" + encodeURIComponent(name)) + ")");
        	if (result.nameExist) {
        		alert("分类已经存在，请修改！");
        		return;
        	}
        	if (id == "") {
        		SAVE_URL = MODULE_CATEGORY_MODEL_URL;
        		categoryForm.setItemValue("_method", "post");
        	} else {
        		SAVE_URL = MODULE_CATEGORY_MODEL_URL + "/" + id;
        		categoryForm.setItemValue("_method", "put");
        	}
    		categoryForm.send(SAVE_URL, "post", function(loader, response){
				dhxWins.window(WIN_ID).close();
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
function load() {
	dhxLayout = new dhtmlXLayoutObject("content", "3L");
	dhxLayout.cells("a").hideHeader();
	dhxLayout.cells("b").hideHeader();
	dhxLayout.cells("c").hideHeader();
	dhxLayout.cells("a").setWidth(240);
	dhxLayout.cells("b").setHeight(500);
	dhxLayout.setAutoSize("b;c", "a");
	/** 构件组装方式为内嵌的代码，构件上内嵌构件显示的区域设置  start */
	CFG_reserveZone = dhxLayout.cells("b");
	dhxLayout.attachEvent("onResizeFinish", function(){
		if (window.CFG_componentZoneResize) {
			CFG_componentZoneResize();
		}
	});
	dhxLayout.attachEvent("onPanelResizeFinish", function(){
		if (window.CFG_componentZoneResize) {
			CFG_componentZoneResize();
		}
	});
	/** 构件组装方式为内嵌的代码 ，构件上内嵌构件显示的区域设置  end */

	
	tree = dhxLayout.cells("a").attachTree();
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	
	var treeJson = {id:0, item:[{id:-1,text:"ModuleDemo",im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[]}]};
	tree.setDataMode("json");
	// 初始右键菜单
	var treeMenu = new dhtmlXMenuObject();
	treeMenu.renderAsContextMenu();
	treeMenu.loadXMLString("<menu><item id='1' text='新增分类'/><item id='2' text='修改分类'/><item id='3' text='删除分类'/></menu>");
	treeMenu.attachEvent("onClick", function(id) {
		MODEL_URL = MODULE_CATEGORY_MODEL_URL;
		GWIN_WIDTH = 400;
		GWIN_HEIGHT = 240;
		if (id == "1") {
			categoryForm = openNewWindow(categoryFormData, initCategoryFormToolbar);
			categoryForm.setItemValue("parentId", contextMenuNodeId);
		} else if (id == "2") {
			categoryForm = openEditWindow(categoryFormData, contextMenuNodeId, initCategoryFormToolbar);
		} else if (id == "3") {
			deleteById(contextMenuNodeId, function(loader) {
				if (loader.xmlDoc.responseText == "")
        			return null;
				jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
				if (jsonObj.message != "OK") {
					alert(jsonObj.message);
				} else {
			        var parId = tree.getParentId(contextMenuNodeId);
			        tree.refreshItem(parId);
			        tree.selectItem(parId, true);
		        }
		    });
		}
	});
	tree.enableContextMenu(treeMenu);
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(TREE_URL+"!tree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId");
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
	tree.attachEvent("onClick", function(id) {
		if (this.getItemText(id) == "TreeNodeReserve") {
			CFG_clickButtonOrTreeNode("TreeNodeReserve", "树节点预留区", "3");
			return;
		}
		if (id != "-1") {
			if (!dataGrid) {
				toolBar = dhxLayout.cells("b").attachToolbar();
				initToolBar1(toolBar);
			    statusBar = dhxLayout.cells("b").attachStatusBar();
			    dataGrid = dhxLayout.cells("b").attachGrid();
			    initGrid1(true);
			    detailForm = dhxLayout.cells("c").attachForm(initFormFormat(formData));
			    initFormToolBar();
		    } else {
		    	searchDataGrid();
		    }
		    detailForm.setItemValue("categoryId", tree.getSelectedItemId());
			dataGrid.selectRow(0, true, true, true);
		}
	});
	tree.attachEvent("onBeforeContextMenu", function(nId) {
		contextMenuNodeId = nId;
		if (nId == "-1") {
			treeMenu.showItem("1");
			treeMenu.hideItem("2");
			treeMenu.hideItem("3");
		} else {
			treeMenu.showItem("1");
			treeMenu.showItem("2");
			treeMenu.showItem("3");
		}
		return true;
	});
	tree.selectItem("-1", true);
}
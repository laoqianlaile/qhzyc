MODEL_URL = contextPath + "/systemcomponent/system-component-version";
gridData = {
	format: {
		headers: ["<center>构件名称</center>", "<center>构件类名</center>", "<center>构件版本</center>", "<center>类型</center>", "<center>是否绑定菜单</center>", ""],
		cols: ["componentVersion.alias", "componentVersion.name", "componentVersion.version", "componentVersion.type", "bindingMenu"],
		colWidths: ["150", "150", "150", "150", "150", "*"],
		colTypes: ["ro", "ro", "ro", "co", "co", "ro"],
		colAligns: ["left", "left", "left", "left", "left"],
		colTooltips: ["true", "true", "true", "true", "true", "false"]
	}
};
var isRootMenu;
/**
 * 加载GridData
 * @param {dhtmlxGrid} grid
 * @param {string} url
 */
function loadGridData(grid, url) {
    try {
    	grid.clearAll();
    } catch (e){
    	
    }
    var dataJson = loadJson(url);
    var jsonArray = dataJson.data ? dataJson.data : dataJson;
    var datas = {};
    datas.rows = [];
    for (var i = 0; i < jsonArray.length; i++) {
        var row = {};
        row.id = jsonArray[i][0];
        row.data = [];
        for (var j = 1; j < jsonArray[i].length; j++) {
            row.data[j - 1] = jsonArray[i][j];
        }
        datas.rows[i] = row;
    }
    grid.parse(datas, "json");
}
/**
 * 刷新列表
 */
function refreshSystemComponentGrid() {
	var url = MODEL_URL + "!getSystemComponentVersions.json?menuId=" + currentTreeNodeId;
    loadGridData(dataGrid, url);
}
/**
 * 初始化菜单列表工具条
 */
function initSelfToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("add", 0, "添加构件", "new.gif");
    toolBar.addSeparator("septr$01", 1);
    toolBar.addButton("delete", 2, "删除", "delete.gif");
    toolBar.addSeparator("septr$02", 3);
    toolBar.addButton("refresh", 4, "刷新", "refresh.gif");
    toolBar.addSeparator("septr$03" , 5);
    toolBar.addButton("viewBindingComponent", 6, "查看关联构件", "view.gif");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "add") {
        	selectComponentVersion();
        } else if (id == "delete") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId.indexOf('SCV_') != -1) {
                dhtmlx.message('通过菜单绑定的构件不能删除！');
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
                        	refreshSystemComponentGrid();
                        });
                    }
                }
            });
        } else if (id == "viewBindingComponent") {
            var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            if (!dhxWins) {
                dhxWins = new dhtmlXWindows();
            }
            var moduleWin = dhxWins.createWindow("moduleWin", 0, 0, 500, 400);
            moduleWin.setModal(true);
            moduleWin.setText("查看关联构件");
            moduleWin.center();
            moduleWin.button("park").hide();
            moduleWin.button("minmax1").hide();
            moduleWin.button("minmax2").hide();
            var tabbar = moduleWin.attachTabbar();
		    tabbar.setImagePath(IMAGE_PATH);
		    
            tabbar.addTab("tab$table$01", "构件组装", "130px");
            var bindingConstructTree = tabbar.cells("tab$table$01").attachTree();
            bindingConstructTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
            bindingConstructTree.setDataMode("json");
            bindingConstructTree.enableCheckBoxes(1);
            bindingConstructTree.enableSmartXMLParsing(true);
            bindingConstructTree.setXMLAutoLoading(contextPath + "/construct/construct!getCheckedConstructTree.json?E_model_name=tree&componentVersionId="+selectIds);
            bindingConstructTree.showItemCheckbox(1, 0);
            var constructJson = {
                    id : 0,
                    item : [{
                        id : -1,
                        text : "构件组装",
                        im0 : "safe_close.gif",
                        im1 : "safe_open.gif",
                        im2 : "safe_close.gif",
                        open : true,
                        item : []
                    }]
                };
            bindingConstructTree.loadJSONObject(constructJson);
            bindingConstructTree.refreshItem("-1");
		    
			tabbar.addTab("tab$table$02", "构件生产库", "130px");
            var bindingComponentTree = tabbar.cells("tab$table$02").attachTree();
            bindingComponentTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
            bindingComponentTree.setDataMode("json");
            bindingComponentTree.enableCheckBoxes(1);
            bindingComponentTree.enableSmartXMLParsing(true);
            bindingComponentTree.setXMLAutoLoading(COMPONENT_AREA_MODEL_URL+"!getCheckedTree.json?E_model_name=tree&componentVersionId="+selectIds);
            bindingComponentTree.showItemCheckbox(1, 0);
            var componentJson = {
                    id : 0,
                    item : [{
                        id : -1,
                        text : "构件生产库",
                        im0 : "safe_close.gif",
                        im1 : "safe_open.gif",
                        im2 : "safe_close.gif",
                        open : true,
                        item : []
                    }]
                };
            bindingComponentTree.loadJSONObject(componentJson);
            bindingComponentTree.refreshItem("-1");
            
		    tabbar.setTabActive("tab$table$01");
		} else if (id == "refresh") {
        	refreshSystemComponentGrid();
        }
    });
}
/**
 * 创建系统构件管理模块说明
 * @return {div}
 */
function createHelpDiv() {
	var obj = document.getElementById("DIV-help");
	if (null == obj) {
		obj = document.createElement("DIV");
		obj.setAttribute("id", "DIV-help");
		obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;");
		obj.innerHTML = "<ul> \n"
			+ "<li type=\"square\">"
			+ "<p><b>系统构件管理模块说明：</b><br></p> \n"
			+ "<p>1. 系统节点对应的列表为系统下绑定的所有构件<br></p> \n"
			+ "<p>2. 因为有些构件不是通过菜单触发的，例如触发器触发的构件，此类构件无法通过菜单绑定到系统中，可以通过此处添加构件实现<br></p> \n"
			+ "</li> \n"
			+ "</ul> \n";
	}
	return obj;
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
	var treeJson = {id:0, item:[{id:-1,text:"系统", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif",item:[]}]};
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	tree.setXMLAutoLoading(contextPath + "/menu/menu!getMenuTree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_orders=showOrder");
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
	tree.attachEvent("onClick", function(id) {
		currentTreeNodeId = "" + id;
		if (id == "-1") {
			if (toolBar) {
				dhxLayout.cells("b").detachToolbar();
				toolBar = null;
				dataGrid = null;
			}
			dhxLayout.cells("b").showHeader();
			dhxLayout.cells("b").setText("操作说明");
			dhxLayout.cells("b").attachObject(createHelpDiv());
		} else {
			dhxLayout.cells("b").hideHeader();
			if (!toolBar) {
				toolBar = dhxLayout.cells("b").attachToolbar();
			    initSelfToolBar();
		    }
		    if (!dataGrid) {
			    dataGrid = dhxLayout.cells("b").attachGrid();
			    pageable = false;
			    initGrid();
			    var typeCombo = dataGrid.getCombo(3);
			    typeCombo.put("0", "公用构件");
                typeCombo.put("1", "页面构件");
                typeCombo.put("2", "逻辑构件");
                typeCombo.put("3", "树构件");
                typeCombo.put("4", "物理表构件");
                typeCombo.put("5", "逻辑表构件");
                typeCombo.put("6", "通用表构件");
                typeCombo.put("7", "标签页构件");
                typeCombo.put("8", "中转器构件");
                typeCombo.put("9", "组合构件");
			    var bindingMenuCombo = dataGrid.getCombo(4);
			    bindingMenuCombo.put("0", "否");
			    bindingMenuCombo.put("1", "是");
		    }
		    if (tree.getParentId(id) == -1) {
		    	isRootMenu = true;
		    	toolBar.showItem("add");
		    	toolBar.showItem("delete");
		    	toolBar.showItem("septr$01");
		    	toolBar.showItem("septr$02");
		    } else {
		    	isRootMenu = false;
		    	toolBar.hideItem("add");
		    	toolBar.hideItem("delete");
		    	toolBar.hideItem("septr$01");
		    	toolBar.hideItem("septr$02");
		    }
		    refreshSystemComponentGrid();
    	}
	});
	tree.selectItem("-1", true);
}
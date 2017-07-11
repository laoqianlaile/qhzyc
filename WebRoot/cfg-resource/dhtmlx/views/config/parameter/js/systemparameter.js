TREE_URL = contextPath + "/parameter/system-parameter-category";
var SYSTEM_PARAMETER_MODEL_URL = contextPath + "/parameter/system-parameter";
var SYSTEM_PARAMETER_CATEGORY_MODEL_URL = contextPath + "/parameter/system-parameter-category";
// 分类表单
var categoryForm;
var categoryFormData = {
	format: [
		{type: "block", width: "350", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "parentId"},
			{type: "hidden", name: "hasChild"},
			{type: "input", label: "分类名称：", labelWidth: "100", name: "name", maxLength:100, width: 200, required: true, tooltip: '分类名称不能为空'}
		]}
	],
	settings: {labelWidth: 80, inputWidth: 160}
};
detailFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "categoryId"},
			{type: "hidden", name: "showOrder"},
			{type: "input", label: "名称:", name: "name", maxLength:100, required: true, tooltip: '名称不能为空'},
			{type: "input", label: "值:", name: "value", maxLength:200, required: true, tooltip: '值不能为空'},
			{type: "combo", label: "类型:", name: "type", maxLength:200, required: true, tooltip: '类型不能为空', options:[
				{value: "3", text: "PDT", selected:true},
				{value: "2", text: "实施人员"},
				{value: "1", text: "用户"}
			]},
			{type: "input", label: "参数说明:", name: "remark", maxLength:500, required: true, tooltip: '参数说明不能为空', rows:"3"},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"100", list:[
				{type: "button", name: "save", value: "保存", width:80}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
gridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>值</center>", "<center>类型</center>", "<center>参数说明</center>", ""],
		cols: ["id", "name", "value", "type", "remark"],
		colWidths: ["30", "120", "120", "120", "300", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "co", "ro", "ro"],
		colAligns: ["right", "left", "left", "left", "left"],
		colTooltips: ["false", "true", "true", "true", "true", "false"]
	}
};
/**
 * 初始化分类表单工具条
 * @param {dhtmlxToolbar} toolBar
 */
function initCategoryFormToolbar(toolBar) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(buttonName) {
        if (buttonName == "close") {
            dhxWins.window(WIN_ID).close();
        } else if (buttonName == "save") {
            var id = categoryForm.getItemValue("id");
            var name = categoryForm.getItemValue("name");
            var parentId;
            if (id) {
                parentId = tree.getParentId(contextMenuNodeId);
            } else {
                parentId = contextMenuNodeId;
            }
            var result = eval("(" + loadJson(SYSTEM_PARAMETER_CATEGORY_MODEL_URL + "!validateFields.json?id=" + id + "&parentId=" + parentId + "&name=" + encodeURIComponent(name)) + ")");
            if (result.nameExist) {
                dhtmlx.message(getMessage("form_field_exist", "参数分类名称"));
                return;
            }
            if (id == "") {
                SAVE_URL = SYSTEM_PARAMETER_CATEGORY_MODEL_URL;
                categoryForm.setItemValue("_method", "post");
            } else {
                SAVE_URL = SYSTEM_PARAMETER_CATEGORY_MODEL_URL + "/" + id;
                categoryForm.setItemValue("_method", "put");
            }
            categoryForm.send(SAVE_URL, "post", function(loader, response) {
                dhxWins.window(WIN_ID).close();
                dhtmlx.message(getMessage("save_success"));
                if (id != "") {
                    var parentId = tree.getParentId(contextMenuNodeId);
                    tree.refreshItem(parentId);
                } else {
                    tree.refreshItem(contextMenuNodeId);
                }
            });
        }
    });
}
/**
 * 初始化系统参数列表
 */
function initSelfGrid() {
    dataGrid.setImagePath(IMAGE_PATH);
    dataGrid.setHeader(gridData.format.headers.toString());
    dataGrid.setInitWidths(gridData.format.colWidths.toString());
    dataGrid.setColTypes(gridData.format.colTypes.toString());
    dataGrid.setColAlign(gridData.format.colAligns.toString());
    dataGrid.setSkin(Skin);
    dataGrid.init();
    if (gridData.format.colTooltips) {
        dataGrid.enableTooltips(gridData.format.colTooltips.toString());
    }
    dataGrid.enableMultiselect(true);
    dataGrid.enableDragAndDrop(true);
    dataGrid.attachEvent("onRowDblClicked", function(rId, cInd) {
    });
    dataGrid.attachEvent("onBeforeDrag", function(id) {
        return this.cells(id, 1).getValue();
    });
    dataGrid.attachEvent("onDrag", function(sId, tId) {
        if (sId.indexOf(",") != -1) {
            dhtmlx.message(getMessage("drag_one_record"));
            return false;
        }
        return true;
    });
    dataGrid.attachEvent("onDrop", function(sId, tId) {
        loadJson(SYSTEM_PARAMETER_MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId);
        searchDataGrid();
    });
    dataGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    dataGrid.attachEvent("onRowSelect", function(rId, cInd) {
        dataGrid.cells(rId, 0).open();
    });
    dataGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(detailFormData);
    });
    dataGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (id != "") {
            var url = MODEL_URL + "/" + id + ".json?_method=get";
            loadForm(subform, url);
            subform.setReadonly('name', true);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(SYSTEM_PARAMETER_MODEL_URL + "!validateFields.json?id=" + id + "&name=" + encodeURIComponent(name)) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "系统参数名称"));
                    return;
                }
                if (id == "") {
                    SAVE_URL = SYSTEM_PARAMETER_MODEL_URL;
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = SYSTEM_PARAMETER_MODEL_URL + "/" + id;
                    subform.setItemValue("_method", "put");
                }
                subform.setItemValue("categoryId", tree.getSelectedItemId());
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    searchDataGrid();
                });
            }
        });
    });
    dataGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            dataGrid.forEachRow(function(rId) {
                if (id != rId) {
                    dataGrid.cells(rId, 0).close();
                }
            });
        }
    });
    if (pageable) {
        pagesize = getCookie("pagesize") || PAGE_SIZE;
        dataGrid.enablePaging(true, pagesize, 1, statusBar);
        dataGrid.setPagingSkin('toolbar', Skin);
    }
    searchDataGrid();
}
/**
 * 刷新列表
 */
function searchDataGrid() {
    QUERY_URL = SYSTEM_PARAMETER_MODEL_URL + "!search.json?Q_EQ_categoryId=" + tree.getSelectedItemId() + "&P_orders=showOrder";
    search();
}
/**
 * 初始化参数列表工具条
 */
function initSelfToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("add", 0, "新增", "new.gif");
    toolBar.addSeparator("septr$01", 1);
    toolBar.addButton("refresh", 2, "刷新", "refresh.gif");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "add") {
            dataGrid.addSubRow();
        } else if (id == "refresh") {
            searchDataGrid();
        }
    });
}
/**
 * 创建系统参数模块说明
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
        		+ "<p><b>系统参数分类树操作说明：</b><br></p> \n" 
        		+ "<p>1. 根节点下<br></p> \n" 
        		+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;1.1 可以新增参数分类节点<br></p> \n" 
        		+ "<p>2. 在参数分类节点下<br></p> \n" 
        		+ "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以新增参数分类节点<br></p> \n"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以修改参数分类节点<br></p> \n" 
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;2.1 可以删除参数分类节点<br></p> \n" 
                + "</li> \n" 
                + "</ul> \n" 
                + "<ul> \n" 
                + "<li type=\"square\">" 
                + "<p><b>系统参数分类树操作步骤：</b><br></p> \n"
                + "<p>1. 【新增】选择一个节点，右键->弹出右键菜单->选择“新增参数分类”，则在该节点下新增一个参数分类<br></p> \n" 
                + "<p>2. 【修改】选择一个节点，右键->弹出右键菜单->选择“修改参数分类”<br></p> \n" 
                + "<p>3. 【删除】选择一个节点，右键->弹出右键菜单->选择“删除参数分类”，若该参数分类下有子分类或有参数，则不能删除<br></p> \n" 
                + "<p>4. 点击根节点时，右侧页面是操作说明<br></p> \n" 
                + "</li> \n" 
                + "</ul> \n" 
                + "<ul> \n"
                + "<li type=\"square\">" 
                + "<p><b>系统参数操作注意：</b><br></p> \n" 
                + "<p>1. 系统参数不能被删除，所以新增的时候要慎重，不要增加错了参数名称<br></p> \n" 
                + "<p>2. 系统参数修改时不能修改名称<br></p> \n" 
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
    tree.attachEvent("onMouseIn", function(id) {
        tree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    tree.attachEvent("onMouseOut", function(id) {
        tree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    tree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
    var treeJson = {
        id : 0,
        item : [{id : -1, text : "系统参数定义", im0 : "safe_close.gif", im1 : "safe_open.gif", im2 : "safe_close.gif", open : true, item : []}]
    };
    tree.setDataMode("json");
    // 初始右键菜单
    var treeMenu = new dhtmlXMenuObject();
    treeMenu.renderAsContextMenu();
    treeMenu.loadXMLString("<menu><item id='1' text='新增参数分类'/><item id='2' text='修改参数分类'/><item id='3' text='删除参数分类'/></menu>");
    treeMenu.attachEvent("onClick", function(id) {
        MODEL_URL = SYSTEM_PARAMETER_CATEGORY_MODEL_URL;
        GWIN_WIDTH = 400;
        GWIN_HEIGHT = 240;
        if (id == "1") {
            categoryForm = openNewWindow(categoryFormData, initCategoryFormToolbar);
            categoryForm.setItemValue("parentId", contextMenuNodeId);
        } else if (id == "2") {
            categoryForm = openEditWindow(categoryFormData, contextMenuNodeId, initCategoryFormToolbar);
        } else if (id == "3") {
            dhtmlxAjax.get(MODEL_URL + "/" + contextMenuNodeId + ".json?_method=delete", function(loader) {
                jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                	jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("delete_success"));
                    var parId = tree.getParentId(contextMenuNodeId);
                    tree.refreshItem(parId);
                    tree.selectItem(parId, true);
                } else {
                	dhtmlx.message(isNotEmpty(jsonObj.message) ? jsonObj.message : getMessage("delete_failure"));
                }
            });
        }
    });
    tree.enableContextMenu(treeMenu);
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
    tree.enableSmartXMLParsing(true);
    tree.setXMLAutoLoading(TREE_URL + "!tree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId");
    tree.loadJSONObject(treeJson);
    tree.refreshItem("-1");
    tree.attachEvent("onClick", function(id) {
        if (id == "-1") {
            if (toolBar) {
                dhxLayout.cells("b").detachToolbar();
                dhxLayout.cells("b").detachStatusBar();
                toolBar = null;
                statusBar = null;
                dataGrid = null;
            }
            dhxLayout.cells("b").showHeader();
            dhxLayout.cells("b").setText("操作说明");
            dhxLayout.cells("b").attachObject(createHelpDiv());
        } else {
            MODEL_URL = SYSTEM_PARAMETER_MODEL_URL;
            dhxLayout.cells("b").hideHeader();
            if (!dataGrid) {
                toolBar = dhxLayout.cells("b").attachToolbar();
                initSelfToolBar();
                statusBar = dhxLayout.cells("b").attachStatusBar();
                dataGrid = dhxLayout.cells("b").attachGrid();
                var typeCombo = dataGrid.getCombo(3);
                typeCombo.put("3", "PDT");
                typeCombo.put("2", "实施人员");
                typeCombo.put("1", "用户");
                initSelfGrid();
            } else {
                searchDataGrid();
            }
        }
    });
    tree.selectItem("-1", true);
}
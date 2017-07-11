var COMPONENT_AREA_MODEL_URL = contextPath + "/component/component-area";
var componentAreaForm;
var contextMenuNodeId = "-1";
var componentAreaFormData = {
    format: [
        {type: "block", width: "350", list:[
            {type: "hidden", name: "_method"},
            {type: "hidden", name: "id"},
            {type: "hidden", name: "parentId"},
            {type: "hidden", name: "showOrder"},
            {type: "hidden", name: "hasChild"},
            {type: "input", label: "构件分类名称：", labelWidth: "120", name: "name", maxLength:100, required: true, width: 200}
        ]}
    ],
    settings: {labelWidth: 80, inputWidth: 160}
};
/**
 * 模块分类树初始化
 */
function initModuleTree(that) {
    var layout = that.aLayout;
    layout.setText("构件分类树");
    layout.setWidth(240);
    var _this = this;
    tree = layout.attachTree();
    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    tree.attachEvent("onMouseIn", function(id) {
        tree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    tree.attachEvent("onMouseOut", function(id) {
        tree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    tree.setStdImages("folderClosed.gif", "folderOpen.gif", "folderClosed.gif");
    var treeJson = {id:0, item:[
        {id:'-1',text:"构件分类树", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", child:1}
    ]};
    // 初始右键菜单
    var treeMenu = new dhtmlXMenuObject();
    treeMenu.renderAsContextMenu();
    treeMenu.loadXMLString("<menu><item id='1' text='新增构件分类'/><item id='2' text='修改构件分类'/><item id='3' text='删除构件分类'/></menu>");
    treeMenu.attachEvent("onClick", function(id) {
        GWIN_WIDTH = 400;
        GWIN_HEIGHT = 240;
        if (id == "1") {
            componentAreaForm = openNewWindow(componentAreaFormData, initComponentAreaFormToolbar, 400, 200);
            componentAreaForm.setItemValue("parentId", contextMenuNodeId);
        } else if (id == "2") {
            componentAreaForm = openEditWindow(componentAreaFormData, contextMenuNodeId, initComponentAreaFormToolbar, 400, 200);
        } else if (id == "3") {
            dhtmlxAjax.get(COMPONENT_AREA_MODEL_URL + "/" + contextMenuNodeId + ".json?_method=delete", function(loader) {
                jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("delete_success"));
                    var parId = tree.getParentId(contextMenuNodeId);
                    tree.refreshItem(parId);
                } else {
                    dhtmlx.message(isNotEmpty(jsonObj.message) ? jsonObj.message : "删除失败！");
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
    tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    var treeUrl = COMPONENT_AREA_MODEL_URL+"!tree.json?E_model_name=tree&F_in=name,hasChild&P_filterId=parentId&P_orders=showOrder";
    tree.setXMLAutoLoading(treeUrl);
    tree.loadJSONObject(treeJson);
    tree.refreshItem("-1");
    tree.enableDragAndDrop(true, false);
	tree.setDragBehavior("complex", true);
	tree.attachEvent("onDrop", function(sId, tId, id){
		var url = COMPONENT_AREA_MODEL_URL + "!sort.json?start=" + sId + "&targetId=" + tId;
		if (id) {
			url += "&end=" + id;
		}
		var result = loadJson(url);
		dhtmlx.message(result);
	});
    // 点击节点刷新右边列表
    tree.attachEvent("onClick", function(nId) {
        // 树节点ID
        that.nodeId = nId;
        if (nId == "-1") {
        	if (that.createModuleGrid) {
        	    that.bLayout.detachToolbar();
        	    that.bLayout.detachStatusBar();
        	    that.createModuleGrid = false;
        	}
        	that.bLayout.setText("操作说明");
            that.bLayout.showHeader();
            that.bLayout.attachObject(createHelpDiv());
        } else {
            initModuleGrid(that.bLayout, that);
        }
    });
    that.reloadTreeItem = function() {
        tree.refreshItem(that.nodeId);
        tree.selectItem(that.nodeId, false, "0");
    };
    tree.selectItem("-1", true, "0");
}
/**
 * 创建说明DIV
 */
function createHelpDiv() {
    var obj = document.getElementById("DIV-help");
    if (null == obj) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-help");
        obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;");
        obj.innerHTML = "<ul> \n" + "<li type=\"square\">" + "<p><b>构件定义模块说明：</b><br></p> \n"
                + "<p>1. 左侧为构件分类树，右击树节点可以新增、修改、删除构件分类<br></p> \n"
                + "<p>2. 点击根节点时，右侧页面是操作说明<br></p> \n"
                + "<p>3. 点击构件分类节点时，右侧页面是构件列表，用于新增、修改、删除构件，构件类型包括树构件、逻辑表构件、物理表构件<br></p> \n" + "</li> \n" + "</ul> \n";
    }
    return obj;
}
/**
 * 初始化构件分类表单工具条的方法
 * @param {dhtmlxToolbar} toolBar
 */
function initComponentAreaFormToolbar(toolBar) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("save", 1, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar.addButton("close", 2, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(id) {
        if (id == "close") {
            dhxWins.window(WIN_ID).close();
        } else if (id == "save") {
            var id = componentAreaForm.getItemValue("id");
            var name = componentAreaForm.getItemValue("name");
            var parentId = componentAreaForm.getItemValue("parentId");
            var result = eval("("
                    + loadJson(COMPONENT_AREA_MODEL_URL + "!validateFields.json?id=" + id + "&name="
                            + encodeURIComponent(name) + "&parentId=" + parentId) + ")");
            if (result.nameExist) {
                dhtmlx.message(getMessage("form_field_exist", "构件分类名称"));
                return;
            }
            if (id == "") {
                SAVE_URL = COMPONENT_AREA_MODEL_URL;
                componentAreaForm.setItemValue("_method", "post");
            } else {
                SAVE_URL = COMPONENT_AREA_MODEL_URL + "/" + id;
                componentAreaForm.setItemValue("_method", "put");
            }
            componentAreaForm.send(SAVE_URL, "post", function(loader, response) {
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

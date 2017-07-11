// 绑定组合构件的菜单树，组合构件树，组合构件中的按钮列表
var assemblyMenuTree, assemblyComponentTree, constructButtonGrid;
var assemblyMenuTreeCell, assemblyComponentTreeCell, constructButtonGridCell;
var assemblyMenuTreeNodeId, assemblyComponentTreeNodeId, assemblyComponentTreeNodeType;
/**
 * 初始化构件组装按钮权限布局
 * @param authTabBar 权限tab页
 * @param objId 角色或用户ID
 * @param objType 类型，角色或用户
 */
function initAuthConstructButton(authTabBar, objId, objType) {
    var dhxLayout = authTabBar.cells("auth$05").attachLayout("3W");
    dhxLayout.setSizes();
    assemblyMenuTreeCell = dhxLayout.cells("a");
    assemblyComponentTreeCell = dhxLayout.cells("b");
    constructButtonGridCell = dhxLayout.cells("c");
    assemblyMenuTreeCell.setWidth(220);
    assemblyComponentTreeCell.setWidth(220);
    assemblyMenuTreeCell.setText("菜单树");
    assemblyComponentTreeCell.setText("构件树");
    constructButtonGridCell.hideHeader();
    initAssemblyMenuTree(objId, objType);
    initConstructButtonGrid();
    initConstructButtonToolbar(objId, objType);
}
/**
 * 初始化绑定组合构件的菜单树
 */
function initAssemblyMenuTree(objId, objType) {
    ASSEMBLY_MENU_TREE_URL = contextPath
            + "/menu/menu!getAssemblyComponentMenuTree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,hasChild&P_UD=bindingType,componentVersionId&P_OPEN=true&objectId="
            + objId + "&objectType=" + objType;
    assemblyMenuTree = assemblyMenuTreeCell.attachTree();
    assemblyMenuTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    assemblyMenuTree.attachEvent("onMouseIn", function(id) {
        assemblyMenuTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    assemblyMenuTree.attachEvent("onMouseOut", function(id) {
        assemblyMenuTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var treeJson = {
        id : 0,
        item : [{
            id : -1,
            text : "菜单",
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            item : []
        }]
    };
    assemblyMenuTree.setDataMode("json");
    assemblyMenuTree.enableSmartXMLParsing(true);
    assemblyMenuTree.setXMLAutoLoading(ASSEMBLY_MENU_TREE_URL);
    assemblyMenuTree.loadJSONObject(treeJson);
    assemblyMenuTree.refreshItem("-1");
    assemblyMenuTree.attachEvent("onClick", function(nId) {
        // 树节点ID
        assemblyMenuTreeNodeId = nId;
        var bindingType = assemblyMenuTree.getUserData(nId, "bindingType");
        if (bindingType == "1") {
            initAssemblyComponentTree(objId, objType, assemblyMenuTree.getUserData(nId, "componentVersionId"));
        } else {
            assemblyMenuTreeNodeId = null;
            if (assemblyComponentTree) {
                assemblyComponentTree.destructor();
                assemblyComponentTree = null;
                assemblyComponentTreeNodeId = null;
            }
        }
        constructButtonGrid.clearAll();
    });
}
/**
 * 初始化绑定组合构件树
 */
function initAssemblyComponentTree(objId, objType, assembleComponentVersionId) {
    ASSEMBLY_COMPONENT_TREE_URL = contextPath
            + "/construct/construct!getAssemblyComponentTree.json?E_model_name=tree&P_filterId=parentId&F_in=name,hasChild&P_OPEN=true&assembleComponentVersionId="
            + assembleComponentVersionId;
    assemblyComponentTree = assemblyComponentTreeCell.attachTree();
    assemblyComponentTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    assemblyComponentTree.attachEvent("onMouseIn", function(id) {
        assemblyComponentTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    assemblyComponentTree.attachEvent("onMouseOut", function(id) {
        assemblyComponentTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var treeJson = {
        id : 0,
        item : [{
            id : -1,
            text : "组合构件",
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            item : []
        }]
    };
    assemblyComponentTree.setDataMode("json");
    assemblyComponentTree.enableSmartXMLParsing(true);
    assemblyComponentTree.setXMLAutoLoading(ASSEMBLY_COMPONENT_TREE_URL);
    assemblyComponentTree.loadJSONObject(treeJson);
    assemblyComponentTree.refreshItem("-1");
    assemblyComponentTree.attachEvent("onClick", function(nId) {
        // 树节点ID
    	if (nId.indexOf('_') != -1) {
    	    assemblyComponentTreeNodeId = nId.substring(0, nId.indexOf('_'));
    	} else {
            assemblyComponentTreeNodeId = nId;
    	}
    	assemblyComponentTreeNodeType = assemblyComponentTree.getAttribute(nId, "prop0");
    	if (nId == "-1" || "TREE" == assemblyComponentTreeNodeType) {
    	    assemblyComponentTreeNodeId = null;
            constructButtonGrid.clearAll();
        } else {
            loadConstructButtonGrid(objId, objType);
        }
    });
}
/**
 * 初始化构件组装按钮列表
 */
function initConstructButtonGrid() {
    constructButtonGrid = constructButtonGridCell.attachGrid();
    constructButtonGridData = {
        format : {
            headers : ["<center>可用</center>", "<center>预留区名称</center>", "<center>按钮名称</center>", "<center>构件名称</center>", ""],
            colWidths : ["60", "180", "180", "180", "*"],
            colTypes : ["ch", "ro", "ro", "ro", "ro"],
            colAligns : ["center", "left", "left", "left", "left"]
        }
    };
    initGridWithoutColumnsAndPageable(constructButtonGrid, constructButtonGridData, null);
}
/**
 * 加载按钮列表数据
 */
function loadConstructButtonGrid(objId, objType) {
    constructButtonGrid.clearAll();
    var url = contextPath + "/construct/construct!getButtonsOfConstruct.json?componentVersionId="
            + assemblyComponentTreeNodeId;
    loadGridDataArray(constructButtonGrid, url);
    constructButtonGrid.checkAll();
    var newObjType = objType;
    if (objType == '-U') {
        newObjType = 1;
    } else if (objType == '-R') {
        newObjType = 0;
    }
    var checkedurl = contextPath
            + "/authority/authority-construct-button!search.json?F_in=constructDetailId&Q_EQ_menuId="
            + assemblyMenuTreeNodeId + "&Q_EQ_componentVersionId=" + assemblyComponentTreeNodeId + "&Q_EQ_objectId="
            + objId + "&Q_EQ_objectType=" + newObjType;
    dhtmlxAjax.get(checkedurl, function(loader) {
        var result = eval("(" + loader.xmlDoc.responseText + ")");
        if (result.data != "") {
            for (var i = 0; i < result.data.length; i++) {
                constructButtonGrid.cellById(result.data[i].constructDetailId, 0).setValue(0);
            }
        }
    });
}
/**
 * 初始化保存工具条
 */
function initConstructButtonToolbar(objId, objType) {
    var statusBar = constructButtonGridCell.attachStatusBar();
    var btbar = new dhtmlXToolbarObject(statusBar);
    btbar.setIconPath(TOOLBAR_IMAGE_PATH);
    btbar.addButton("bottom$save", 0, "保存", "save.gif");
    btbar.addSeparator("bottom$septr$01", 1);
    btbar.setAlign("right");
    btbar.attachEvent("onClick", function(id) {
        if ("bottom$save" == id) {
            var uncheckedIds = "";
            var allIds = constructButtonGrid.getAllRowIds();
            var checkedIds = constructButtonGrid.getCheckedRows(0);
            var allIdsArr = allIds.split(",");
            for (var i = 0; i < allIdsArr.length && "" != allIds; i++) {
                if (checkedIds.indexOf(allIdsArr[i]) == -1) {
                    uncheckedIds += allIdsArr[i] + ",";
                }
            }
            if (!assemblyMenuTreeNodeId) {
                dhtmlx.alert("请先选择菜单树节点再进行其操作!");
                return;
            }
            if (!assemblyComponentTreeNodeId) {
                dhtmlx.alert("请先选择构件树节点再进行其操作!");
                return;
            }
            var url = contextPath + "/authority/authority-construct-button!saveAuthButton.json" + "?P_menuId="
                    + assemblyMenuTreeNodeId + "&P_componentVersionId=" + assemblyComponentTreeNodeId + "&P_objectId="
                    + objId + "&P_objectType=" + objType + "&P_constructDetailIds=" + uncheckedIds;
            dhtmlxAjax.get(encodeURI(url), function(loader) {
                dhtmlx.message(getMessage("operate_success"));
            });
        }
    });
}
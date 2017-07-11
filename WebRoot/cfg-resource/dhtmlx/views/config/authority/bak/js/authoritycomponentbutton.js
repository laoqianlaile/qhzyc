// 绑定开发的构件的菜单树，开发的构件树，开发的构件中的按钮列表
var developMenuTree, developComponentTree, componentButtonGrid;
var developMenuTreeCell, developComponentTreeCell, componentButtonGridCell;
var developMenuTreeNodeId, developComponentTreeNodeId, componentVersionId;
/**
 * 初始化开发的构件按钮权限布局
 * @param authTabBar 权限tab页
 * @param objId 角色或用户ID
 * @param objType 类型，角色或用户
 */
function initAuthComponentButton(authTabBar, objId, objType) {
    var dhxLayout = authTabBar.cells("auth$04").attachLayout("3W");
    dhxLayout.setSizes();
    developMenuTreeCell = dhxLayout.cells("a");
    developComponentTreeCell = dhxLayout.cells("b");
    componentButtonGridCell = dhxLayout.cells("c");
    developMenuTreeCell.setWidth(220);
    developComponentTreeCell.setWidth(220);
    developMenuTreeCell.setText("菜单树");
    developComponentTreeCell.setText("构件树");
    componentButtonGridCell.hideHeader();
    initDevelopMenuTree(objId, objType);
    initComponentButtonGrid();
    initComponentButtonToolbar(objId, objType);
}
/**
 * 初始化绑定开发的构件的菜单树
 */
function initDevelopMenuTree(objId, objType) {
    ASSEMBLY_MENU_TREE_URL = contextPath
            + "/menu/menu!getComponentMenuTree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,hasChild&P_UD=bindingType,componentVersionId&P_OPEN=true&objectId="
            + objId + "&objectType=" + objType;
    developMenuTree = developMenuTreeCell.attachTree();
    developMenuTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    developMenuTree.attachEvent("onMouseIn", function(id) {
        developMenuTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    developMenuTree.attachEvent("onMouseOut", function(id) {
        developMenuTree.setItemStyle(id, "background-color:#FFFFFF;");
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
    developMenuTree.setDataMode("json");
    developMenuTree.enableSmartXMLParsing(true);
    developMenuTree.setXMLAutoLoading(ASSEMBLY_MENU_TREE_URL);
    developMenuTree.loadJSONObject(treeJson);
    developMenuTree.refreshItem("-1");
    developMenuTree.attachEvent("onClick", function(nId) {
        // 树节点ID
        developMenuTreeNodeId = nId;
        var bindingType = developMenuTree.getUserData(nId, "bindingType");
        if (bindingType == "1") {
            initDevelopComponentTree(objId, objType, developMenuTree.getUserData(nId, "componentVersionId"));
        } else {
            developMenuTreeNodeId = null;
            if (developComponentTree) {
                developComponentTree.destructor();
                developComponentTree = null;
                developComponentTreeNodeId = null;
            }
        }
        componentButtonGrid.clearAll();
    });
}
/**
 * 初始化绑定开发的构件树
 */
function initDevelopComponentTree(objId, objType, componentVersionId) {
    ASSEMBLY_COMPONENT_TREE_URL = contextPath
            + "/construct/construct!getDevelopComponentTree.json?E_model_name=tree&P_filterId=parentId&F_in=name,hasChild&P_OPEN=true&componentVersionId="
            + componentVersionId;
    developComponentTree = developComponentTreeCell.attachTree();
    developComponentTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    developComponentTree.attachEvent("onMouseIn", function(id) {
        developComponentTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    developComponentTree.attachEvent("onMouseOut", function(id) {
        developComponentTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var treeJson = {
        id : 0,
        item : [{
            id : -1,
            text : "开发的构件",
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            item : []
        }]
    };
    developComponentTree.setDataMode("json");
    developComponentTree.enableSmartXMLParsing(true);
    developComponentTree.setXMLAutoLoading(ASSEMBLY_COMPONENT_TREE_URL);
    developComponentTree.loadJSONObject(treeJson);
    developComponentTree.refreshItem("-1");
    developComponentTree.attachEvent("onClick", function(nId) {
        developComponentTreeNodeId = nId;
        if (nId != "-1") {
            componentVersionId = developComponentTree.getAttribute(nId, "prop0");
            loadComponentButtonGrid(objId, objType);
        } else {
            componentVersionId = null;
            developComponentTreeNodeId = null;
            componentButtonGrid.clearAll();
        }
    });
}
/**
 * 初始化开发的构件按钮列表
 */
function initComponentButtonGrid() {
    componentButtonGrid = componentButtonGridCell.attachGrid();
    componentButtonGridData = {
        format : {
            headers : ["<center>可用</center>", "<center>按钮名称</center>", ""],
            colWidths : ["60", "180", "*"],
            colTypes : ["ch", "ro", "ro"],
            colAligns : ["center", "left", "left"]
        }
    };
    initGridWithoutColumnsAndPageable(componentButtonGrid, componentButtonGridData, null);
}
/**
 * 加载按钮列表数据
 */
function loadComponentButtonGrid(objId, objType) {
    componentButtonGrid.clearAll();
    var url = contextPath + "/component/component-button!getButtonsOfComponent.json?componentVersionId="
            + developComponentTreeNodeId;
    loadGridDataArray(componentButtonGrid, url);
    componentButtonGrid.checkAll();
    var newObjType = objType;
    if (objType == '-U') {
        newObjType = 1;
    } else if (objType == '-R') {
        newObjType = 0;
    }
    var checkedurl = contextPath
            + "/authority/authority-component-button!search.json?F_in=componentButtonId&Q_EQ_menuId="
            + developMenuTreeNodeId + "&Q_EQ_componentVersionId=" + developComponentTreeNodeId + "&Q_EQ_objectId=" + objId
            + "&Q_EQ_objectType=" + newObjType;
    dhtmlxAjax.get(checkedurl, function(loader) {
        var result = eval("(" + loader.xmlDoc.responseText + ")");
        if (result.data != "") {
            for (var i = 0; i < result.data.length; i++) {
                componentButtonGrid.cellById(result.data[i].componentButtonId, 0).setValue(0);
            }
        }
    });
}
/**
 * 初始化保存工具条
 */
function initComponentButtonToolbar(objId, objType) {
    var statusBar = componentButtonGridCell.attachStatusBar();
    var btbar = new dhtmlXToolbarObject(statusBar);
    btbar.setIconPath(TOOLBAR_IMAGE_PATH);
    btbar.addButton("bottom$save", 0, "保存", "save.gif");
    btbar.addSeparator("bottom$septr$01", 1);
    btbar.setAlign("right");
    btbar.attachEvent("onClick", function(id) {
        if ("bottom$save" == id) {
            var uncheckedIds = "";
            var allIds = componentButtonGrid.getAllRowIds();
            var checkedIds = componentButtonGrid.getCheckedRows(0);
            var allIdsArr = allIds.split(",");
            for (var i = 0; i < allIdsArr.length && "" != allIds; i++) {
                if (checkedIds.indexOf(allIdsArr[i]) == -1) {
                    uncheckedIds += allIdsArr[i] + ",";
                }
            }
            if (!developMenuTreeNodeId) {
                dhtmlx.alert("请先选择菜单树节点再进行其操作!");
                return;
            }
            if (!developComponentTreeNodeId) {
                dhtmlx.alert("请先选择构件树节点再进行其操作!");
                return;
            }
            var url = contextPath + "/authority/authority-component-button!saveAuthButton.json" + "?P_menuId="
                    + developMenuTreeNodeId + "&P_componentVersionId=" + developComponentTreeNodeId + "&P_objectId=" + objId
                    + "&P_objectType=" + objType + "&P_componentButtonIds=" + uncheckedIds;
            dhtmlxAjax.get(encodeURI(url), function(loader) {
                dhtmlx.message(getMessage("operate_success"));
            });
        }
    });
}
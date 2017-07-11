// 直接或间接绑定自定义构件的菜单树
var dataSelfDefineMenuTree, dataConfigGrid;
var dataSelfDefineMenuTreeCell, dataConfigGridCell;
var dataSelfDefineMenuTreeNodeId;
/**
 * 初始化自定义按钮权限布局
 * @param authTabBar 权限tab页
 * @param objId 角色或用户ID
 * @param objType 类型，角色或用户
 */
function initAuthDataConfig(authtabbar, objId, objType) {
    var dhxLayout = authtabbar.cells("auth$02").attachLayout("2U");
    dhxLayout.setSizes();
    dataSelfDefineMenuTreeCell = dhxLayout.cells("a");
    dataConfigGridCell = dhxLayout.cells("b");
    dataSelfDefineMenuTreeCell.setWidth(220);
    dataSelfDefineMenuTreeCell.setText("菜单树");
    dataConfigGridCell.hideHeader();
    initDataSelfDefineMenuTree(objId, objType);
    initDataConfigGrid();
}
/**
 * 初始化直接或间接绑定自定义构件的菜单树
 * @param objId 角色或用户ID
 * @param objType 类型，角色或用户
 */
function initDataSelfDefineMenuTree(objId, objType) {
    SELF_DEFINE_MENU_TREE_URL = contextPath
            + "/menu/menu!getSelfDefineComponentMenuTree.json?E_model_name=tree&P_orders=showOrder&P_filterId=parentId&F_in=name,hasChild&P_UD=bindingType,componentVersionId&P_OPEN=true&objectId="
            + objId + "&objectType=" + objType;
    dataSelfDefineMenuTree = dataSelfDefineMenuTreeCell.attachTree();
    dataSelfDefineMenuTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    dataSelfDefineMenuTree.attachEvent("onMouseIn", function(id) {
        dataSelfDefineMenuTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    dataSelfDefineMenuTree.attachEvent("onMouseOut", function(id) {
        dataSelfDefineMenuTree.setItemStyle(id, "background-color:#FFFFFF;");
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
    dataSelfDefineMenuTree.setDataMode("json");
    dataSelfDefineMenuTree.enableSmartXMLParsing(true);
    dataSelfDefineMenuTree.setXMLAutoLoading(SELF_DEFINE_MENU_TREE_URL);
    dataSelfDefineMenuTree.loadJSONObject(treeJson);
    dataSelfDefineMenuTree.refreshItem("-1");
    dataSelfDefineMenuTree.attachEvent("onClick", function(nId) {
        // 菜单树节点ID
        dataSelfDefineMenuTreeNodeId = nId;
        var bindingType = dataSelfDefineMenuTree.getUserData(nId, "bindingType");
        if (bindingType == "1") {
            var url = contextPath + "/authority/authority-tree!getDataConfigGridData.json?P_objectId="
                    + objId + "&P_objectType=" + objType + "&P_menuId=" + nId;
            loadGridDataArray(dataConfigGrid, url);
        } else {
            dataSelfDefineMenuTreeNodeId = null;
            dataConfigGrid.clearAll();
        }
    });
}
/**
 * 初始化数据权限配置列表
 */
function initDataConfigGrid() {
    dataConfigGrid = dataConfigGridCell.attachGrid();
    dataConfigGridData = {
        format: {
            headers: ["<center>构件名称</center>", "<center>配置树权限</center>", "<center>配置数据权限</center>", "<center>配置编码权限</center>", ""],
            colWidths: ["200", "100", "100", "100", "*"],
            colTypes: ["ro", "img", "img", "img", "ro"],
            colAligns: ["left", "center", "center", "center", "center"]
        }
    };
    initGridWithoutColumnsAndPageable(dataConfigGrid, dataConfigGridData, null);
}
/**
 * 树权限配置
 */
function treeAuthority(objectId, objectType, menuId, componentVersionId) {
    var treeId = loadJson(contextPath + "/authority/authority-tree!getTreeRootId.json?P_componentVersionId=" + componentVersionId);
    if (isNotEmpty(treeId)) {
        var treeNameJson = loadJson(contextPath + "/appmanage/tree-define!show.json?id=" + treeId);
        var treeName = treeNameJson.name;
        if (treeName != undefined) {
            var h = 450;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var win = createDhxWindow({
                id : "tree$authority$win",
                title : "树权限配置",
                width : 500,
                height : h
            });
            loadAuthorityTree(win, treeId, treeName, objectId, objectType, menuId, componentVersionId);
        } else {
            dhtmlx.alert("该构件配置过的树已删除或不存在，请重新配置!");
        }
    } else {
        dhtmlx.alert("该构件没有树，无需配置!");
    }
}
/**
 * 数据权限配置
 */
function dataAuthority(objectId, objectType, menuId, componentVersionId) {
    var h = 450;
    if (h > document.body.clientHeight) {
        h = document.body.clientHeight;
    }
    var win = createDhxWindow({
        id : "data$authority$win",
        title : "数据权限维护",
        width : 500,
        height : h
    });
    initAuthorityDataWin(win, objectId, objectType, menuId, componentVersionId);
}
/**
 * 编码权限配置
 */
function codeAuthority(objectId, objectType, menuId, componentVersionId) {
	var h = 450;
    if (h > document.body.clientHeight) {
        h = document.body.clientHeight;
    }
    var win = createDhxWindow({
        id : "code$authority$win",
        title : "编码权限维护",
        width : 700,
        height : h
    });
    initAuthorityCodeWin(win, objectId, objectType, menuId, componentVersionId);
}
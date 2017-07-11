/**
 * 加载资源权限
 * @param tabbar 整个权限配置Tabbar
 * @param objectId 角色ID或用户ID
 * @param objectType 对象类型 0：角色、1：用户
 */
function loadAuthorityResource(tabbar, objectId, objectType) {
    tabbar.cells("auth$01").detachToolbar();
    var toolbar = tabbar.cells("auth$01").attachToolbar();
    toolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolbar.addButton("save", 1, "保存", "save.gif");
    toolbar.attachEvent("onClick", function(id) {
        if (id == "save") {
            var menuIds = tree.getAllCheckedBranches();
            var url = contextPath + "/authority/authority!saveAuthoritys.json?P_objectId=" + objectId
                    + "&P_objectType=" + objectType + "&P_authorityIds=" + menuIds;
            dhtmlxAjax.get(url, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == "string") {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("save_success"));
                } else {
                    dhtmlx.message(getMessage("save_failure"));
                }
            });
        }
    });

    var tree = tabbar.cells("auth$01").attachTree();
    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    tree.attachEvent("onMouseIn", function(id) {
        tree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    tree.attachEvent("onMouseOut", function(id) {
        tree.setItemStyle(id, "background-color:#FFFFFF;");
    });

    var authorityIds = findAuthorityIds(objectId, objectType, "0", "");
    var treeAuthJson = {
        id : 0,
        item : [{
            id : -1,
            text : "菜单树",
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            child : 1
        }]
    };
    tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    tree.enableCheckBoxes(true, true);
    tree.enableThreeStateCheckboxes(true);
    tree.setXMLAutoLoading(contextPath + "/menu/menu!tree.json?E_model_name=tree&F_in=name,hasChild&"
            + "P_filterId=parentId&P_orders=showOrder&P_OPEN=true&P_CHECKED=" + authorityIds);
    tree.loadJSONObject(treeAuthJson);
    tree.refreshItem("-1");
}
/**
 * 获取有权限的菜单IDS
 */
function findAuthorityIds(objectId, objectType) {
    var url = contextPath + "/authority/authority!getMenuIds.json?P_objectId=" + objectId + "&P_objectType="
            + objectType;
    var idArray = loadJson(url);
    return idArray.toString();
}
/**
 * 数据权限配档案树
 * 
 * @param {Object} win
 * @param {String} treeId
 * @param {String} treeName
 * @param {String} objectId
 * @param {String} objectType
 * @param {String} menuId
 * @param {String} componentVersionId
 */
function loadAuthorityTree(win, treeId, treeName, objectId, objectType, menuId, componentVersionId) {
    win.setText("树权限配置");
    win.center();
    var statusBar = win.attachStatusBar();
    var toolBar1 = new dhtmlXToolbarObject(statusBar);
    toolBar1.setAlign("right");
    toolBar1.addButton("save", 0, "保存");
    toolBar1.addButton("copyAuth", 1, "复制(其他角色或用户)");
    toolBar1.addButton("close", 2, "关闭");
    toolBar1.attachEvent("onClick", function(id) {
        if (id == "save") {
            var treeDefineIds = archTree.getAllCheckedBranches();
            if (!treeDefineIds) {
                dhtmlx.message("请选择树节点！");
                return;
            }
            var url = contextPath + "/authority/authority-tree!saveAuthorityTree.json?P_objectId=" + objectId
                    + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId="
                    + componentVersionId + "&P_treeDefineIds=" + treeDefineIds;
            dhtmlxAjax.get(url, function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof jsonObj == 'string') {
                    jsonObj = eval("(" + jsonObj + ")");
                }
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("save_success"));
                } else {
                    dhtmlx.message(getMessage("save_failure"));
                }
            });
        } else if (id == "copyAuth") {
            var treeDefineIds = archTree.getAllCheckedBranches();
            if (!treeDefineIds) {
                dhtmlx.message("请选择树节点！");
                return;
            }
            var h = 450;
            if (h > document.body.clientHeight) {
                h = document.body.clientHeight;
            }
            var authwin = createDhxWindow({
                id : "copyAuthorityTree",
                title : "",
                width : 500,
                height : h
            });
            initCopyAuthorityTree(authwin, treeId, objectId, objectType, menuId, componentVersionId, treeDefineIds);
        } else if (id == "close") {
            win.close();
        }
    });
    win.button("close").attachEvent("onClick", function() {
        win.close();
    });
    var archTree = win.attachTree();
    archTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    archTree.attachEvent("onMouseIn", function(id) {
        archTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    archTree.attachEvent("onMouseOut", function(id) {
        archTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var url = contextPath + "/authority/authority-tree!getAuthorityTreeNodeIds.json?P_objectId=" + objectId + "&P_objectType="
            + objectType + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId;
    var authorityTreeNodeIds = loadJson(url);
    var treeAuthJson = {
        id : 0,
        item : [{
            id : treeId,
            text : treeName,
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            child : 1
        }]
    };
    archTree.setDataMode("json");
    archTree.enableSmartXMLParsing(true);
    archTree.enableCheckBoxes(true, true);
    archTree.enableThreeStateCheckboxes(true);
    var arch_URL = contextPath + "/appmanage/tree-define";
    archTree.setXMLAutoLoading(arch_URL
        + "!tree.json?E_model_name=tree&P_filterId=parentId&F_in=name,child&P_orders=showOrder&Q_EQ_dynamic=0&P_OPEN=true&P_CHECKED="
        + authorityTreeNodeIds);
    archTree.loadJSONObject(treeAuthJson);
    archTree.refreshItem(treeId);

}
/**
 * 复制到其他角色或用户
 */
function initCopyAuthorityTree(authwin, treeId, objectId, objectType, menuId, componentVersionId, treeDefineIds) {
    authwin.setText("权限树");
    authwin.center();
    var statusBar = authwin.attachStatusBar();
    var toolBar1 = new dhtmlXToolbarObject(statusBar);
    toolBar1.setAlign("right");
    toolBar1.addButton("save", 0, "保存");
    toolBar1.addButton("close", 1, "关闭");
    toolBar1.attachEvent("onClick", function(id) {
        if (id == "save") {
            var roleUserIds = roleUserTree.getAllChecked();
            // 判断选择用户类型
            if (roleUserIds.indexOf(",") > 0) {
                var roleUserIdArray = roleUserIds.split(",");
                var userIds = "";
                var roleIds = "";
                for (var i = 0; i < roleUserIdArray.length; i++) {
                    var pId = roleUserTree.getParentId(roleUserIdArray[i]);
                    if (pId == "-R") {
                        roleIds += roleUserIdArray[i] + ",";
                    } else if (pId == "-U") {
                        userIds += roleUserIdArray[i] + ",";
                    }
                }
            } else {
                var pId = roleUserTree.getParentId(roleUserIds);
                if (pId == "-R") {
                    roleIds = roleUserIds;
                } else if (pId == "-U") {
                    userIds = roleUserIds;
                }
            }
            if (roleUserIds == "") {
                dhtmlx.message("请先选角色或用户再保存!");
            } else {
                var url = contextPath + "/authority/authority-tree!copyAuthorityTree.json?P_userIds=" + userIds + "&P_roleIds=" + roleIds
                        + "&P_objectId=" + objectId + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId+ "&P_treeDefineIds=" + treeDefineIds;
                dhtmlxAjax.get(url, function(loader) {
                    var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                    if (typeof jsonObj == 'string') {
                        jsonObj = eval("(" + jsonObj + ")");
                    }
                    if (jsonObj.success) {
                        dhtmlx.message(getMessage("save_success"));
                    } else {
                        dhtmlx.message(getMessage("save_failure"));
                    }
                });
            }
        } else if (id == "close") {
            authwin.close();
        }
    });	
    roleUserTree = authwin.attachTree();
    roleUserTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    roleUserTree.attachEvent("onMouseIn", function(id) {
        roleUserTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    roleUserTree.attachEvent("onMouseOut", function(id) {
        roleUserTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
	var treeJson = {id:0, item:[
        {id:"-A", text: "数据权限", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[
            {id:'-R',text:"角色", child:1, type:"role"}, 
        	{id:'-U',text:"用户", child:1, type:"user"}
        ]}
    ]};
	roleUserTree.setDataMode("json");
    roleUserTree.enableSmartXMLParsing(true);
    roleUserTree.enableCheckBoxes(true, true);
    roleUserTree.enableThreeStateCheckboxes(true);
	TREE_URL = contextPath + "/authority/authority";
    roleUserTree.setXMLAutoLoading(TREE_URL + "!tree.json?E_model_name=tree&F_in=text,child&P_UD=type");
    roleUserTree.loadJSONObject(treeJson);
}
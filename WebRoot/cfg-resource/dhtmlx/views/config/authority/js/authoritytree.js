var authTreeLayout, authTree, authTreeToolbar;
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
    var tbar = win.attachToolbar();
    tbar.setIconsPath(TOOLBAR_IMAGE_PATH);
    tbar.addDiv("top$searchTextdiv1", 0, "left");
    if (useTreePower) {
        var url = AUTHORITY_APPROVE_URL + "!getTreeAuthorityApprove.json?P_objectId=" + objectId
                + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId="
                + componentVersionId;
        var result = loadJson(url);
        if (typeof result == 'string') {
            tbar.addText("status", 1, "状态：<font color='red'>"+result+"</font>", "right");
        }
    }
    var treeControlForm = initTreeForm(treeId, objectId, objectType, menuId, componentVersionId);
    var statusBar = win.attachStatusBar();
    authTreeToolbar = new dhtmlXToolbarObject(statusBar);
    authTreeToolbar.setAlign("right");
    authTreeToolbar.addButton("save", 0, "保存");
    authTreeToolbar.addButton("copyAuth", 1, "复制(其他角色)");
    authTreeToolbar.addButton("deleteAuth", 2, "清空配置");
    authTreeToolbar.addButton("close", 3, "关闭");
    authTreeToolbar.attachEvent("onClick", function(id) {
        if (id == "save") {
            var ctrl = treeControlForm.getItemValue("control");
            if (ctrl == "0") {
                var url = AUTHORITY_TREE_URL + "!deleteAuthorityTree.json?P_objectId=" + objectId
                        + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId="
                        + componentVersionId;
                dhtmlxAjax.get(url, function(loader) {
                });
                dhtmlx.message(getMessage("save_success"));
                authTree.setXMLAutoLoading(TREE_DEFINE_URL
                        + "!tree.json?E_model_name=tree&P_filterId=parentId&F_in=name,child&P_orders=showOrder&Q_EQ_dynamic=0&P_OPEN=true");
            } else {
                var treeDefineIds = authTree.getAllCheckedBranches();
                if (!treeDefineIds) {
                    dhtmlx.message("请选择树节点！");
                    return;
                }
                var controlDataAuth = treeControlForm.getItemValue("controlDataAuth");
                var url = AUTHORITY_TREE_URL + "!saveAuthorityTree.json?P_objectId=" + objectId
                        + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId="
                        + componentVersionId + "&P_treeDefineIds=" + treeDefineIds + "&P_controlDataAuth=" + controlDataAuth;
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
        } else if (id == "copyAuth") {
            var treeDefineIds = authTree.getAllCheckedBranches();
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
        } else if (id == "deleteAuth") {
            var url = AUTHORITY_TREE_URL + "!deleteAuthorityTree.json?P_objectId=" + objectId
                    + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId="
                    + componentVersionId;
            dhtmlxAjax.get(url, function(loader) {
                dhtmlx.message("删除树权限配置成功！");
                authTree.setCheck(treeId, 0);
            });
        } else if (id == "close") {
            win.close();
        }
    });
    win.button("close").attachEvent("onClick", function() {
        win.close();
    });
    authTreeLayout = win.attachLayout("1C");
    authTreeLayout.cells("a").hideHeader();
    authTree = authTreeLayout.cells("a").attachTree();
    authTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    authTree.attachEvent("onMouseIn", function(id) {
        authTree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    authTree.attachEvent("onMouseOut", function(id) {
        authTree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    var url = AUTHORITY_TREE_URL + "!getAuthorityTreeNodeIds.json?P_objectId=" + objectId + "&P_objectType="
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
    authTree.setDataMode("json");
    authTree.enableSmartXMLParsing(true);
    authTree.enableCheckBoxes(true, true);
    authTree.enableThreeStateCheckboxes(true);
    authTree.setXMLAutoLoading(TREE_DEFINE_URL
        + "!tree.json?E_model_name=tree&P_filterId=parentId&F_in=name,child&P_orders=showOrder&Q_EQ_dynamic=0&P_OPEN=true&P_CHECKED="
        + authorityTreeNodeIds);
    authTree.loadJSONObject(treeAuthJson);
    authTree.refreshItem(treeId);
    if (authorityTreeNodeIds && authorityTreeNodeIds.length > 0) {
        treeControlForm.setItemValue("control", "1");
        var url = AUTHORITY_TREE_DATA_URL + "!getControlDataAuth.json?P_objectId=" + objectId
                + "&P_objectType=" + objectType + "&P_menuId=" + menuId + "&P_componentVersionId="
                + componentVersionId;
        var result = loadJson(url);
        if (typeof result == 'string') {
            result = eval('(' + result + ')');
            if ("1" == result.controlDataAuth) {
                treeControlForm.checkItem("controlDataAuth");
            }
        }
    } else {
        authTree.lockTree(true);
        treeControlForm.setItemValue("control", "0");
        authTree.setCheck(treeId, 1);
        authTreeToolbar.disableItem("copyAuth");
        authTreeToolbar.disableItem("deleteAuth");
        treeControlForm.disableItem("controlDataAuth");
    }
}
/**
 * 是否配置树权限
 * @returns {dhtmlXForm}
 */
function initTreeForm(treeId, objectId, objectType, menuId, componentVersionId) {
    var sformJson = [
        {type: "radio", name: "control", value: "0", className: "dhx_toolbar_form", label: "全部节点可访问", position: "label-right", labelAlign: "left", checked: false, labelWidth: 100},
        {type: "newcolumn"},
        {type: "radio", name: "control", value: "1", className: "dhx_toolbar_form", label: "控制", position: "label-right", labelAlign: "left", checked: false, labelWidth: 50},
        {type: "newcolumn"},
        {type: "checkbox", name: "controlDataAuth", className: "dhx_toolbar_form", label: "是否作为数据权限", position: "label-right", labelAlign: "left", checked: false, labelWidth: 100}
    ];
    var form = new dhtmlXForm("top$searchTextdiv1", sformJson);
    form.attachEvent("onChange", function(id, value, state) {
        if (id == "control") {
            if (value == "0") {
                authTree.setCheck(treeId, 1);
                authTree.lockTree(true);
                authTreeToolbar.disableItem("copyAuth");
                authTreeToolbar.disableItem("deleteAuth");
                form.disableItem("controlDataAuth");
            } else {
                authTree.lockTree(false);
                var url = AUTHORITY_TREE_URL + "!getAuthorityTreeNodeIds.json?P_objectId=" + objectId + "&P_objectType="
                        + objectType + "&P_menuId=" + menuId + "&P_componentVersionId=" + componentVersionId;
                var authorityTreeNodeIds = loadJson(url);
                authTree.setXMLAutoLoading(TREE_DEFINE_URL
                        + "!tree.json?E_model_name=tree&P_filterId=parentId&F_in=name,child&P_orders=showOrder&Q_EQ_dynamic=0&P_OPEN=true&P_CHECKED="
                        + authorityTreeNodeIds);
                authTree.refreshItem(treeId);
                if (!authorityTreeNodeIds || authorityTreeNodeIds.length==0) {
                    authTree.setCheck(treeId, 0);
                }
                authTreeToolbar.enableItem("copyAuth");
                authTreeToolbar.enableItem("deleteAuth");
                form.enableItem("controlDataAuth");
            }
        }
    });
    return form;
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
            if (!roleUserIds) {
                dhtmlx.message("请先选角色或用户再保存!");
                return;
            }
            // 判断选择用户类型
            var roleUserIdArray = roleUserIds.split(",");
            var userIds = "";
            var roleIds = "";
            for (var i = 0; i < roleUserIdArray.length; i++) {
                if (roleUserIdArray[i].startWith("R")) {
                    roleIds += roleUserIdArray[i] + ",";
                } else if (roleUserIdArray[i].startWith("U")) {
                    userIds += roleUserIdArray[i] + ",";
                }
            }
            var url = AUTHORITY_TREE_URL + "!copyAuthorityTree.json?P_userIds=" + userIds + "&P_roleIds=" + roleIds
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
    roleUserTree.setXMLAutoLoading(AUTHORITY_URL + "!tree.json?E_model_name=tree&F_in=text,child&P_UD=type&P_systemId="+currentSystemId+"&P_objectId="+objectId+"&P_objectType="+objectType);
    roleUserTree.loadJSONObject(treeJson);
}
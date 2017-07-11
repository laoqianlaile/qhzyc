MODEL_URL = contextPath + "/menu/menu";
MENU_INPUT_PARAM_MODEL_URL = contextPath + "/menu/menu-input-param";
MENU_SELF_PARAM_MODEL_URL = contextPath + "/menu/menu-self-param";
// 菜单根节点ID（系统ID）
var rootMenuId;
// 菜单绑定构件的自身配置layout、菜单绑定构件的输入参数配置layout
var selfParamConfigLayout, inputParamConfigLayout;
// 菜单绑定构件的的自身配置列表、菜单绑定构件的输入配置列表
var selfParamConfigGrid, inputParamConfigGrid;
// 当前选中的一条 菜单ID
var currentMenuId;
detailFormData = [{
    type : "settings",
    labelWidth : 120,
    inputWidth : 270,
    labelAlign : "right"
}, {
    type : "block",
    list : [
    	{type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "parentId"},
			{type: "hidden", name: "rootMenuId"},
			{type: "hidden", name: "hasChild"},
			{type: "hidden", name: "showOrder"},
			{type: "input", label: "菜单名称:", name: "name", maxLength:100, required: true, tooltip: '菜单名称不能为空'},
			{type: "input", label: "菜单编码:&nbsp;&nbsp;&nbsp;", name: "code", maxLength:100},
			{type: "block", width: "800", list: [
             	{type: "itemlabel", name: "bindingTypeLabel", label: "绑定类型:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
             	{type: "newcolumn"},
         		{type: "radio", name: "bindingType", value: "", label: "不绑定", labelWidth: 60, position:"label-left", labelAlign:"right", checked: true},
         		{type: "newcolumn"},
         		{type: "radio", name: "bindingType", value: "0", label: "绑定URL", labelWidth: 80, position:"label-left", labelAlign:"right"},
         		{type: "newcolumn"},
			    {type: "radio", name: "bindingType", value: "1", label: "绑定构件", labelWidth: 80, position:"label-left", labelAlign:"right"}
			]},
			{type: "input", label: "菜单URL:&nbsp;&nbsp;&nbsp;", name: "url", maxLength:500, rows:3},
			{type: "block", width: "800", list:[
			    {type: "combo", label: "构件:&nbsp;&nbsp;&nbsp;", name: "componentVersionId", width:274, showAll: true},
			    {type: "newcolumn"},
				{type: "button", name: "selectComp", value: "选择", width:80}
			]},
			{type: "block", width: "800", list:[
				{type: "itemlabel", name: "isQuickMenuLabel", label: "是否快捷菜单:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
				{type: "newcolumn"},
					{type: "radio", name: "isQuickMenu", label: "是", value:"1", labelWidth:60, position:"label-left", labelAlign:"right"},
				{type: "newcolumn"},
					{type: "radio", name: "isQuickMenu", label: "否", value:"0", labelWidth:80, position:"label-left", labelAlign:"right", checked: true},
				{type: "newcolumn"},
            ]},
			{type: "block", width: "800", list:[
				{type: "itemlabel", name: "useNavigationLabel", label: "是否使用导航条:&nbsp;&nbsp;&nbsp;", labelAlign:"right"},
				{type: "newcolumn"},
 				{type: "radio", name: "useNavigation", label: "是", value:"1", labelWidth:60, position:"label-left", labelAlign:"right"},
				{type: "newcolumn"},
 				{type: "radio", name: "useNavigation", label: "否", value:"0", labelWidth:80, position:"label-left", labelAlign:"right", checked: true},
    			{type: "newcolumn"},
				{type: "button", name: "save", value: "保存", offsetLeft:102, width:80}
			]}
		]}
	]
}];
gridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>编码</center>", ""],
		cols: ["id", "name", "code"],
		userDatas: ["bindingType", "hasChild"],
		colWidths: ["30", "150", "150", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left"],
		colTooltips: ["false", "true", "true", "false"]
	}
};
/**
 * 初始化菜单列表
 */
function initSelfGrid() {
    dataGrid.setImagePath(IMAGE_PATH);
    dataGrid.setHeader(gridData.format.headers.toString());
    dataGrid.setInitWidths(gridData.format.colWidths.toString());
    dataGrid.setColTypes(gridData.format.colTypes.toString());
    dataGrid.setColAlign(gridData.format.colAligns.toString());
    if (gridData.format.colTooltips) {
        dataGrid.enableTooltips(gridData.format.colTooltips.toString());
    }
    dataGrid.setSkin(Skin);
    dataGrid.init();
    dataGrid.enableMultiselect(true);
    dataGrid.enableDragAndDrop(true);
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
        loadJson(MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId);
        refreshMenuGrid();
        tree.refreshItem(currentTreeNodeId);
    });
    dataGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    dataGrid.attachEvent("onRowSelect", function(rId, cInd) {
        dataGrid.cells(rId, 0).open();
        var subform = this.cellById(rId, 0).getSubForm();
    	if (subform) {
    		currentMenuId = subform.getItemValue("id");
    	}
    });
    dataGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = detailFormData;
    });
    dataGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (currentTreeNodeId.indexOf('sys') != -1) {
            subform.lock();
        } else if (id != "" && id.indexOf('sys') != -1) {
            subform.lock();
        }
        if (currentTreeNodeId == "-1" || (id != "" && eval(this.getUserData(id, "hasChild")))) {
            subform.disableItem("bindingTypeLabel");
            subform.disableItem("useNavigationLabel");
            subform.disableItem("isQuickMenuLabel");
            subform.disableItem("bindingType", "");
            subform.disableItem("bindingType", "0");
            subform.disableItem("bindingType", "1");
            subform.disableItem("url");
            subform.disableItem("componentVersionId");
            subform.disableItem("selectComp");
            subform.disableItem("useNavigation", 0);
            subform.disableItem("useNavigation", 1);
            subform.disableItem("isQuickMenu", 0);
            subform.disableItem("isQuickMenu", 1);
        } else {
            subform.disableItem("url");
            subform.disableItem("componentVersionId");
            subform.disableItem("selectComp");
            subform.disableItem("useNavigation", 0);
            subform.disableItem("useNavigation", 1);
            subform.disableItem("useNavigationLabel");
            subform.disableItem("isQuickMenu", 0);
            subform.disableItem("isQuickMenu", 1);
            subform.disableItem("isQuickLabel");
            subform.attachEvent("onChange", function() {
                var bindingType = this.getItemValue("bindingType");
                if (bindingType == "0") {
                    subform.enableItem("url");
                    subform.disableItem("componentVersionId");
                    subform.setItemValue("componentVersionId", "");
                    subform.disableItem("selectComp");
                    subform.disableItem("useNavigation", 0);
                    subform.disableItem("useNavigation", 1);
                    subform.disableItem("useNavigationLabel");
                    subform.enableItem("isQuickMenu", 0);
                    subform.enableItem("isQuickMenu", 1);
                    subform.enableItem("isQuickMenuLabel");
                } else if (bindingType == "1") {
                    subform.disableItem("url");
                    subform.setItemValue("url", "");
                    subform.enableItem("componentVersionId");
                    subform.enableItem("selectComp");
                    subform.enableItem("useNavigation", 0);
                    subform.enableItem("useNavigation", 1);
                    subform.enableItem("useNavigationLabel");
                    subform.enableItem("isQuickMenu", 0);
                    subform.enableItem("isQuickMenu", 1);
                    subform.enableItem("isQuickMenuLabel");
                } else {
                    subform.disableItem("url");
                    subform.setItemValue("url", "");
                    subform.disableItem("componentVersionId");
                    subform.setItemValue("componentVersionId", "");
                    subform.disableItem("selectComp");
                    subform.disableItem("useNavigation", 0);
                    subform.disableItem("useNavigation", 1);
                    subform.disableItem("useNavigationLabel");
                    subform.disableItem("isQuickMenu", 0);
                    subform.disableItem("isQuickMenu", 1);
                    subform.disableItem("isQuickMenuLabel");
                }
            });
            initPageComponentVersionCombo(subform.getCombo("componentVersionId"));
        }
        if (id != "") {
            var url = MODEL_URL + "/" + id + ".json?_method=get";
            var formData = loadJson(url);
            loadFormData(subform, formData);
            var bindingType = formData["bindingType"];
            subform.checkItem("bindingType", formData["bindingType"]);
            subform.checkItem("useNavigation", formData["useNavigation"]);
            subform.checkItem("isQuickMenu", formData["isQuickMenu"]);
            if (bindingType == "0") {
                subform.enableItem("url");
                subform.enableItem("useNavigation", 0);
                subform.enableItem("useNavigation", 1);
                subform.enableItem("isQuickMenu", 0);
                subform.enableItem("isQuickMenu", 1);
                subform.enableItem("isQuickMenuLabel");
            } else if (bindingType == "1") {
                subform.enableItem("componentVersionId");
                subform.enableItem("selectComp");
                subform.enableItem("useNavigation", 0);
                subform.enableItem("useNavigation", 1);
                subform.enableItem("useNavigationLabel");
                subform.enableItem("isQuickMenu", 0);
                subform.enableItem("isQuickMenu", 1);
                subform.enableItem("isQuickMenuLabel");
            }
            currentMenuId = subform.getItemValue("id");
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                if (!subform.validate()) {
                    return;
                }
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var code = subform.getItemValue("code");
                var bindingType = subform.getItemValue("bindingType");
                var bindingUrl = subform.getItemValue("url");
                var componentVersionId = subform.getCombo("componentVersionId").getActualValue();
                if (bindingType == '0' && bindingUrl == '') {
                    dhtmlx.message("请输入菜单URL！");
                    return;
                }
                if (bindingType == '1') {
                    if (componentVersionId == '') {
                        dhtmlx.message("请选择构件！");
                        return;
                    }
                    if (subform.getCombo("componentVersionId").getComboText() == subform.getCombo("componentVersionId").getActualValue()) {
                        dhtmlx.message(getMessage("selected_not_exist", "构件"));
                        return;
                    }
                }
                var result = eval("(" + loadJson(MODEL_URL + "!validateMenu.json?id=" + id + "&name=" + encodeURIComponent(name) + "&code=" + encodeURIComponent(code) + "&parentId=" + tree.getSelectedItemId() + "&rootMenuId=" + rootMenuId + "&bindingType=" + bindingType + "&componentVersionId=" + componentVersionId) + ")");
                if (result.nameExist) {
                    dhtmlx.message("同一级下菜单名称已经存在，请修改！");
                }
                if (result.codeExist) {
                    dhtmlx.message("同一根菜单下菜单编码已经存在，请修改！");
                }
                if (!result.componentValid) {
                    dhtmlx.message("该构件和其它菜单下绑定的构件冲突，请重新选择构件！");
                }
                if (result.nameExist || result.codeExist || !result.componentValid) {
                    return;
                }
                if (id == "") {
                    SAVE_URL = MODEL_URL + ".json";
                    subform.setItemValue("_method", "post");
                    subform.setItemValue("parentId", tree.getSelectedItemId());
                } else {
                    SAVE_URL = MODEL_URL + "/" + id + ".json";
                    subform.setItemValue("_method", "put");
                }
                if (bindingType == "0") {
                    subform.setItemValue("componentVersionId", "");
                } else if (bindingType == "1") {
                    subform.setItemValue("url", "");
                } else {
                    subform.setItemValue("url", "");
                    subform.setItemValue("componentVersionId", "");
                }
                subform.setItemValue("rootMenuId", rootMenuId);
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    refreshMenuGrid();
                    tree.refreshItem(currentTreeNodeId);
                    var content = loader.xmlDoc.responseText.replace(/:null/g, ":''");
                    var formData = eval("(" + content + ")");
                    currentMenuId = formData.id;
                    if (formData.bindingType == "1") {
                    	var result = eval("(" + loadJson(MODEL_URL + "!validateConfig.json?menuId=" + currentMenuId) + ")");
					    if (result.needConfig) {
	                        configMenuParam();
					    }
                    }
                });
            } else if (buttonName == "selectComp") {
                var h = 450;
                if (h > document.body.clientHeight) {
                    h = document.body.clientHeight;
                }
                var win = createDhxWindow({
                    id : "selectCompWin",
                    title : "选择构件",
                    width : 500,
                    height : h
                });
                win.center();
                var winStatusBar = win.attachStatusBar();
                var toolBar1 = new dhtmlXToolbarObject(winStatusBar);
                toolBar1.setAlign("right");
                toolBar1.addButton("confirm", 0, "确定");
                toolBar1.addButton("cancel", 1, "取消");
                toolBar1.attachEvent("onClick", function(id) {
                    if (id == "confirm") {
                        var checkedTreeIds = selectCompTree.getAllCheckedBranches();
                        if (!checkedTreeIds) {
                            dhtmlx.message("请选择树节点！");
                            return;
                        } else {
                            var checkedTreeIdArray = checkedTreeIds.split(",");
                            var selectedCompIds = [];
                            for (var i in checkedTreeIdArray) {
                                if (checkedTreeIdArray[i].indexOf("CV_") == 0) {
                                    selectedCompIds[selectedCompIds.length] = checkedTreeIdArray[i].replace("CV_", "");
                                }
                            }
                            if (selectedCompIds.length == "0") {
                                dhtmlx.message("请选择构件！");
                            } else if (selectedCompIds.length == "1") {
                                subform.setItemValue("componentVersionId", selectedCompIds[0]);
                                win.close();
                            } else {
                                dhtmlx.message("只能选择一个构件！");
                            }
                        }
                    } else if (id == "cancel") {
                        win.close();
                    }
                });
                win.button("close").attachEvent("onClick", function() {
                    win.close();
                });
                var selectCompTree = win.attachTree();
                selectCompTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
                selectCompTree.attachEvent("onMouseIn", function(id) {
                    selectCompTree.setItemStyle(id, "background-color:#D5E8FF;");
                });
                selectCompTree.attachEvent("onMouseOut", function(id) {
                    selectCompTree.setItemStyle(id, "background-color:#FFFFFF;");
                });
                var treeAuthJson = {
                    id : 0,
                    item : [{
                        id : "-1",
                        text : "构件",
                        im0 : "safe_close.gif",
                        im1 : "safe_open.gif",
                        im2 : "safe_close.gif",
                        child : 1
                    }]
                };
                selectCompTree.setDataMode("json");
                selectCompTree.enableSmartXMLParsing(true);
                selectCompTree.enableCheckBoxes(true, true);
                var selectCompURL = contextPath
                        + "/component/component-version!getMenuComponentTree.json?E_model_name=tree&P_filterId=parentId&F_in=name,child&P_OPEN=false";
                selectCompTree.setXMLAutoLoading(selectCompURL);
                selectCompTree.loadJSONObject(treeAuthJson);
                selectCompTree.refreshItem("-1");
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
}
/**
 * 初始化构件下拉框
 * @param {dhtmlxCombo} componentVersionCombo
 */
function initPageComponentVersionCombo(componentVersionCombo) {
    componentVersionCombo.clearAll(true);
    var componentVersionComboUrl = contextPath + "/component/component-version!getPageComponentVersionList.json";
    if (releasedSystem) {
        componentVersionComboUrl += "?isSystemUsed=1";
    }
    var jsonObj = loadJson(componentVersionComboUrl);
    if (jsonObj && jsonObj.data && jsonObj.data.length) {
        var opt_data = [];
        for (var m = 0; m < jsonObj.data.length; m++) {
            opt_data[m] = {
                text : jsonObj.data[m][1],
                value : jsonObj.data[m][0]
            };
        }
        componentVersionCombo.addOption(opt_data);
    }
}
/**
 * 刷新菜单列表
 */
function refreshMenuGrid() {
    QUERY_URL = MODEL_URL + "!search.json?Q_EQ_parentId=" + tree.getSelectedItemId() + "&P_orders=showOrder";
    search();
}
/**
 * 初始化菜单列表工具条
 */
function initSelfToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("add", 0, "新增", "new.gif");
    toolBar.addSeparator("septr$01", 1);
    toolBar.addButton("config", 2, "配置", "setup.gif");
    toolBar.addSeparator("septr$02", 3);
    toolBar.addButton("setupIcon", 4, "设置图标", "setup.gif");
    toolBar.addSeparator("septr$03", 5);
    toolBar.addButton("setupQuickIcon", 6, "设置快捷图标", "setup.gif");
    toolBar.addSeparator("septr$04", 7);
    toolBar.addButton("delete", 8, "删除", "delete.gif");
    toolBar.addSeparator("septr$05", 9);
    toolBar.addButton("preview", 10, "预览", "preview.gif");
    toolBar.addSeparator("septr$06", 11);
    toolBar.addButton("refresh", 12, "刷新", "refresh.gif");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "add") {
            var bindingType = tree.getUserData(currentTreeNodeId, "bindingType");
            var warningText;
            if (bindingType == "0") {
                warningText = "父菜单已经绑定了URL，若创建子菜单，则父菜单将撤销绑定URL，是否继续创建子菜单?";
            } else if (bindingType == "1") {
                warningText = "父菜单已经绑定了构件，若创建子菜单，则父菜单将撤销绑定构件，是否继续创建子菜单?";
            }
            if (warningText) {
                dhtmlx.confirm({
                    type : "confirm",
                    text : warningText,
                    ok : "是",
                    cancel : "否",
                    callback : function(flag) {
                        if (flag) {
                            dataGrid.addSubRow();
                        }
                    }
                });
            } else {
                dataGrid.addSubRow();
            }
        } else if (id == "config") {
        	var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
            	refreshMenuGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            if (dataGrid.getUserData(selectIds, "bindingType") != "1") {
            	dhtmlx.message("该菜单没有绑定构件！");
                return;
            }
            var result = eval("(" + loadJson(MODEL_URL + "!validateConfig.json?menuId=" + currentMenuId) + ")");
		    if (!result.needConfig) {
            	dhtmlx.message("该菜单绑定的构件没有参数可以配置！");
		        return;
		    }
        	configMenuParam();
        } else if (id == "setupIcon") {
        	var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
            	refreshMenuGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            if (dataGrid.getUserData(selectIds, "bindingType") == "0" || dataGrid.getUserData(selectIds, "bindingType") == "1") {
            	dhtmlx.message("该菜单无需设置图标！");
                return;
            }
        	setupIcon();
        } else if (id == "setupQuickIcon") {
        	var selectIds = dataGrid.getSelectedRowId();
        	if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
            	refreshMenuGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            if (dataGrid.getUserData(selectIds, "bindingType") == "0" || dataGrid.getUserData(selectIds, "bindingType") == "1") {
            	setupQuickIcon();
            }else {
            	dhtmlx.message("该菜单无需设置快捷图标！");
            	return;
            }
        } else if (id == "delete") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                refreshMenuGrid();
                return;
            } else if (selectId.indexOf('sys') != -1) {
                dhtmlx.message('"系统配置平台"不能删除！');
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
                            refreshMenuGrid();
                            tree.refreshItem(currentTreeNodeId);
                        });
                    }
                }
            });
        } else if (id == "preview") {
        	var selectIds = dataGrid.getSelectedRowId();
            if (selectIds == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectIds == "") {
            	refreshMenuGrid();
                return;
            } else if (selectIds.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            window.open(basePath + "app.jsp?systemId=" + selectIds,
								'previewSys', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
        } else if (id == "refresh") {
            refreshMenuGrid();
            tree.refreshItem(currentTreeNodeId);
        }
    });
}
var inputParamConfigGridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>参数说明</center>", "<center>值</center>", ""],
		colWidths: ["30", "120", "120", "200", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left", "left"],
		colTooltips: ["false", "true", "true", "true", "false"]
	}
};
var inputParamFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "menuId"},
			{type: "hidden", name: "inputParamId"},
			{type: "input", label: "名称:", name: "name", readonly:true},
			{type: "input", label: "值:", name: "value", maxLength:200},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"22", list:[
				{type: "button", name: "save", value: "保存", width:80}
			]}
		]}
    ],
	settings: {labelWidth: 80, inputWidth: 200}
};
/**
 * 初始化菜单输出参数配置layout
 */
function initInputParamConfigLayout() {
    inputParamConfigLayout.cells('a').hideHeader();
    inputParamConfigGrid = inputParamConfigLayout.cells('a').attachGrid();
    inputParamConfigGrid.setImagePath(IMAGE_PATH);
    inputParamConfigGrid.setHeader(inputParamConfigGridData.format.headers.toString());
    inputParamConfigGrid.setInitWidths(inputParamConfigGridData.format.colWidths.toString());
    inputParamConfigGrid.setColTypes(inputParamConfigGridData.format.colTypes.toString());
    inputParamConfigGrid.setColAlign(inputParamConfigGridData.format.colAligns.toString());
    if (inputParamConfigGridData.format.colTooltips) {
        inputParamConfigGrid.enableTooltips(inputParamConfigGridData.format.colTooltips.toString());
    }
    inputParamConfigGrid.setSkin(Skin);
    inputParamConfigGrid.init();
    inputParamConfigGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    inputParamConfigGrid.attachEvent("onRowSelect", function(rId, cInd) {
        inputParamConfigGrid.cells(rId, 0).open();
    });
    inputParamConfigGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(inputParamFormData);
    });
    inputParamConfigGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        var url = MENU_INPUT_PARAM_MODEL_URL + "/" + id + ".json?_method=get";
        loadForm(subform, url);
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                subform.setItemValue("_method", "put");
                subform.send(MENU_INPUT_PARAM_MODEL_URL + "/" + id, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    inputParamConfigGridLoadData();
                });
            }
        });
    });
    inputParamConfigGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            inputParamConfigGrid.forEachRow(function(rId) {
                if (id != rId) {
                    inputParamConfigGrid.cells(rId, 0).close();
                }
            });
        }
    });
    inputParamConfigGridLoadData();
}
/**
 * inputParamConfigGrid加载数据
 */
function inputParamConfigGridLoadData() {
    var url = MENU_INPUT_PARAM_MODEL_URL + "!getInputParamList.json?E_model_name=datagrid&menuId=" + currentMenuId;
    loadParamGridData(inputParamConfigGrid, url);
}
/**
 * 加载构件的参数配置GridData
 * @param {dhtmlxGrid} grid
 * @param {string} url
 */
function loadParamGridData(grid, url) {
    grid.clearAll();
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
var selfParamConfigGridData = {
	format: {
		headers: ["&nbsp;", "<center>名称</center>", "<center>参数说明</center>", "<center>值</center>", ""],
		cols: ["id", "name", "remark", "text"],
		colWidths: ["30", "120", "120", "120", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left", "left"],
		colTooltips: ["false", "true", "true", "true", "false"]
	}
};
var selfParamFormData;
/**
 * 初始化菜单绑定构件的的自身配置layout
 */
function initSelfParamConfigLayout() {
    selfParamConfigLayout.cells('a').hideHeader();
    selfParamConfigGrid = selfParamConfigLayout.cells('a').attachGrid();
    selfParamConfigGrid.setImagePath(IMAGE_PATH);
    selfParamConfigGrid.setHeader(selfParamConfigGridData.format.headers.toString());
    selfParamConfigGrid.setInitWidths(selfParamConfigGridData.format.colWidths.toString());
    selfParamConfigGrid.setColTypes(selfParamConfigGridData.format.colTypes.toString());
    selfParamConfigGrid.setColAlign(selfParamConfigGridData.format.colAligns.toString());
    if (selfParamConfigGridData.format.colTooltips) {
        selfParamConfigGrid.enableTooltips(selfParamConfigGridData.format.colTooltips.toString());
    }
    selfParamConfigGrid.setSkin(Skin);
    selfParamConfigGrid.init();
    selfParamConfigGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    selfParamConfigGrid.attachEvent("onRowSelect", function(rId, cInd) {
        selfParamConfigGrid.cells(rId, 0).open();
    });
    selfParamConfigGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform, selectId) {
        GET_BY_ID_URL = MENU_SELF_PARAM_MODEL_URL + "/" + selectId + ".json?_method=get";
        var formObj = loadJson(GET_BY_ID_URL);
	    if (formObj.type == "1") {
    		selfParamFormData = {
				format: [
			        {type: "block", width: "800", list:[
						{type: "hidden", name: "id"},
						{type: "input", label: "名称:", name: "name", readonly: true},
						{type: "combo", label: "值:", name: "value", options: eval(formObj.options)},
						{type: "newcolumn"},
						{type: "block", width: "120", offsetTop:"22", list:[
							{type: "button", name: "save", value: "保存", width:80}
						]}
					]}
		        ],
				settings: {labelWidth: 80, inputWidth: 200}
			};
	    } else if (formObj.type == "2") {
	    	selfParamFormData = {
				format: [
			        {type: "block", width: "800", list:[
						{type: "hidden", name: "id"},
						{type: "input", label: "名称:", name: "name", readonly: true},
						{type: "multiselect", label: "值:", name: "value", inputHeight:90, inputWidth:130, options: eval(formObj.options)},
						{type: "newcolumn"},
						{type: "block", width: "120", offsetTop:"22", list:[
							{type: "button", name: "save", value: "保存", width:80}
						]}
					]}
		        ],
				settings: {labelWidth: 80, inputWidth: 200}
			};
	    } else {
	    	selfParamFormData = {
				format: [
			        {type: "block", width: "800", list:[
						{type: "hidden", name: "id"},
						{type: "input", label: "名称:", name: "name", readonly: true},
						{type: "input", label: "值:", name: "value", maxLength:200},
						{type: "newcolumn"},
						{type: "block", width: "120", offsetTop:"22", list:[
							{type: "button", name: "save", value: "保存", width:80}
						]}
					]}
		        ],
				settings: {labelWidth: 80, inputWidth: 200}
			};
	    }
        subform.c = initDetailFormFormat(selfParamFormData);
    });
    selfParamConfigGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        var url = MENU_SELF_PARAM_MODEL_URL + "/" + id + ".json?_method=get";
        loadForm(subform, url);
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var value = subform.getItemValue("value");
                var valueType = subform.getItemType("value");
                var text;
                if (valueType == "combo") {
                    var valueCombo = subform.getCombo("value");
                    if (value == '') {
                        text = "";
                    } else {
                        text = valueCombo.getComboText();
                    }
                } else if (valueType == "multiselect") {
                    var k = [];
                    var opts = subform.getOptions("value");
                    for (var i = 0; i < opts.length; i++) {
                        if (opts[i].value == '') {
                            continue;
                        }
                        if (opts[i].selected) {
                            k.push(opts[i].text);
                        }
                    }
                    text = k.toString();
                } else {
                    text = subform.getItemValue("value");
                }
                dhtmlxAjax.get(MENU_SELF_PARAM_MODEL_URL + "!saveMenuSelfParam.json?id=" + id + "&value=" + encodeURIComponent(value) + "&text=" + encodeURIComponent(text), function() {
                    dhtmlx.message(getMessage("save_success"));
                    selfParamConfigGridLoadData();
                });
            }
        });
    });
    selfParamConfigGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            selfParamConfigGrid.forEachRow(function(rId) {
                if (id != rId) {
                    selfParamConfigGrid.cells(rId, 0).close();
                }
            });
        }
    });
    selfParamConfigGridLoadData();
}
/**
 * grid加载数据
 */
function selfParamConfigGridLoadData() {
    selfParamConfigGrid.clearAll();
    var url = MENU_SELF_PARAM_MODEL_URL + "!search.json?E_model_name=datagrid&F_in=" + selfParamConfigGridData.format.cols.toString() + "&Q_EQ_menuId=" + currentMenuId;
    var loader = dhtmlxAjax.getSync(url);
    if (loader.xmlDoc.responseText == "")
        return;
    // 替换列表显示为“null” 的列 --wm
    var reg = new RegExp("\"null\"", "g");
    var loaderDoc = loader.xmlDoc.responseText;
    if (loaderDoc.indexOf("null") != -1) {
        var dataJson = loaderDoc.replace(reg, "\"\"");
    }
    var rows = eval('(' + dataJson + ')');
    // 如果查询结果为空
    if (rows.rows == undefined) {
        //dhtmlx.message("结果集为空！");
        return;
    }
    selfParamConfigGrid.parse(rows, "json");
}
/**
 * 配置组合构件配置信息
 */
function configMenuParam() {
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var baseConfigWin = dhxWins.createWindow("baseConfigWin", 0, 0, 1000, 400);
    baseConfigWin.setModal(true);
    baseConfigWin.setText("菜单配置");
    baseConfigWin.center();
    var configTabbar = baseConfigWin.attachTabbar();
    configTabbar.setImagePath(IMAGE_PATH);
    configTabbar.addTab("baseSelfConfig", "构件自身参数设置", "120px");
    configTabbar.addTab("inputParamConfig", "构件输入参数配置", "120px");
    configTabbar.setTabActive("baseSelfConfig");
    selfParamConfigLayout = configTabbar.cells("baseSelfConfig").attachLayout("1C");
    initSelfParamConfigLayout();
    inputParamConfigLayout = configTabbar.cells("inputParamConfig").attachLayout("1C");
    initInputParamConfigLayout();
}
/**
 * 设置菜单图标
 */
function setupIcon() {
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var iconWin = dhxWins.createWindow("baseConfigWin", 0, 0, 800, 400);
    iconWin.setModal(true);
    iconWin.setText("设置图标");
    iconWin.center();
    var iconForm = iconWin.attachForm(getIconFormData());
    var iconFormStatusBar = iconWin.attachStatusBar();
    var iconFormToolbar = new dhtmlXToolbarObject(iconFormStatusBar);
    initIconToolBar(iconFormToolbar, iconWin, iconForm);
    var formData = loadJson(MODEL_URL + "/" + currentMenuId + ".json?_method=get");
	if (formData.icon1) {
		iconForm.checkItem("icon1", formData.icon1);
	}
	if (formData.icon2) {
		iconForm.checkItem("icon2", formData.icon2);
	}
}
/**
 * 设置快捷菜单图标
 */
function setupQuickIcon() {
	if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var iconWin = dhxWins.createWindow("baseConfigWin", 0, 0, 800, 400);
    iconWin.setModal(true);
    iconWin.setText("设置快捷图标");
    iconWin.center();
    var iconForm = iconWin.attachForm(getQuickIconFormData());
    var iconFormStatusBar = iconWin.attachStatusBar();
    var iconFormToolbar = new dhtmlXToolbarObject(iconFormStatusBar);
    initQuickIconToolBar(iconFormToolbar, iconWin, iconForm);
    var formData = loadJson(MODEL_URL + "/" + currentMenuId + ".json?_method=get");
	if (formData.quickIcon) {
		iconForm.checkItem("quickIcon", formData.quickIcon);
	}
}
/**
 * 获取菜单图标表单内容
 */
function getIconFormData() {
	var formData = [{
	    type : "settings",
	    labelWidth : 80,
	    inputWidth : 270,
	    labelAlign : "right"
	}, {
	    type : "block",
	    list : []
	}];
	if (currentTreeNodeId == rootMenuId) {
		var oneLevelIconFieldSet = {type: "fieldset",  name: "oneLevel", label: "一级菜单图标", width:720, offsetLeft:20, list:[]};
	    var menuIconJsonObj = loadJson(contextPath + "/menu/menu!getMenuIcons.json?level=1");
	    if (menuIconJsonObj) {
	        for (var m = 0; m < menuIconJsonObj.length; m = m + 6) {
	        	var blk = {type: "block", width: 650, offsetLeft:20, list:[
    		    	{type: "img", src: contextPath + "/cfg-resource/coral40/common/themes/base/images/menuicon/onelevel/" + menuIconJsonObj[m], offsetLeft:20},
    				{type: "radio", label: "图标" + (m + 1), name: "icon1", value:menuIconJsonObj[m], width: 100, labelAlign:"left", position:"label-right"}
    		    ]}
    		    for (var i = m + 1; i < menuIconJsonObj.length && i < m + 6; i++) {
    		    	blk.list.push({type : "newcolumn"});
    		    	blk.list.push({type: "img", src: contextPath + "/cfg-resource/coral40/common/themes/base/images/menuicon/onelevel/" + menuIconJsonObj[i], offsetLeft:20});
    		    	blk.list.push({type: "radio", label: "图标" + (i + 1), name: "icon1", value:menuIconJsonObj[i], width: 100, labelAlign:"left", position:"label-right"});
    		    }
    		    oneLevelIconFieldSet.list.push(blk);
	        }
	    }
	    formData[1].list.push(oneLevelIconFieldSet);
	}
	var twoLevelIconFieldSet = {type: "fieldset",  name: "twoLevel", label: "二级菜单图标", width:720, offsetLeft:20, list:[]};
    var menuIconJsonObj = loadJson(contextPath + "/menu/menu!getMenuIcons.json?level=2");
    if (menuIconJsonObj) {
        for (var m = 0; m < menuIconJsonObj.length; m = m + 6) {
            var blk = {type: "block", width: 650, offsetLeft:20, list:[
		    	{type: "img", src: contextPath + "/cfg-resource/coral40/common/themes/base/images/menuicon/twolevel/" + menuIconJsonObj[m], offsetLeft:20},
				{type: "radio", label: "图标" + (m + 1), name: "icon2", value:menuIconJsonObj[m], width: 100, labelAlign:"left", position:"label-right"}
		    ]}
		    for (var i = m + 1; i < menuIconJsonObj.length && i < m + 6; i++) {
		    	blk.list.push({type : "newcolumn"});
		    	blk.list.push({type: "img", src: contextPath + "/cfg-resource/coral40/common/themes/base/images/menuicon/twolevel/" + menuIconJsonObj[i], offsetLeft:20});
		    	blk.list.push({type: "radio", label: "图标" + (i + 1), name: "icon2", value:menuIconJsonObj[i], width: 100, labelAlign:"left", position:"label-right"});
		    }
		    twoLevelIconFieldSet.list.push(blk);
        }
    }
    formData[1].list.push(twoLevelIconFieldSet);
	return formData;
}
/**
 * 获取快捷菜单图标表单内容
 * 
 */
function getQuickIconFormData() {
	var formData = [{
	    type : "settings",
	    labelWidth : 80,
	    inputWidth : 270,
	    labelAlign : "right"
	}, {
	    type : "block",
	    list : []
	}];
	var quickIconFieldSet = {type: "fieldset",  name: "quickicon", label: "快捷菜单图标", width:720, offsetLeft:20, list:[]};
    var quickMenuIconJsonObj = loadJson(contextPath + "/menu/menu!getQuickMenuIcons.json");
    if (quickMenuIconJsonObj) {
        for (var m = 0; m < quickMenuIconJsonObj.length; m = m + 6) {
            var blk = {type: "block", width: 650, offsetLeft:20, list:[
		    	{type: "img", src: contextPath + "/cfg-resource/coral40/common/themes/base/images/menuicon/quickicon/" + quickMenuIconJsonObj[m], offsetLeft:20},
				{type: "radio", label: "图标" + (m + 1), name: "quickIcon", value:quickMenuIconJsonObj[m], width: 100, labelAlign:"left", position:"label-right"}
		    ]}
		    for (var i = m + 1; i < quickMenuIconJsonObj.length && i < m + 6; i++) {
		    	blk.list.push({type : "newcolumn"});
		    	blk.list.push({type: "img", src: contextPath + "/cfg-resource/coral40/common/themes/base/images/menuicon/quickicon/" + quickMenuIconJsonObj[i], offsetLeft:20});
		    	blk.list.push({type: "radio", label: "图标" + (i + 1), name: "quickIcon", value:quickMenuIconJsonObj[i], width: 100, labelAlign:"left", position:"label-right"});
		    }
            quickIconFieldSet.list.push(blk);
        }
    }
    formData[1].list.push(quickIconFieldSet);
	return formData;
}
/**
 * 初始化菜单图标表单工具条
 */
function initIconToolBar(toolBar, iconWin, iconForm) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.setAlign("right");
    toolBar.addButton("clear", 0, "&nbsp;&nbsp;取消图标设置&nbsp;&nbsp;");
    toolBar.addSeparator("septr$01", 1);
    toolBar.addButton("save", 2, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar.addSeparator("septr$01", 3);
    toolBar.addButton("close", 4, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "clear") {
        	var url = MODEL_URL + "!cancelMenuIcon.json?menuId=" + currentMenuId;
            dhtmlxAjax.get(url, function(loader) {
                dhtmlx.message({
                    text : getMessage("operate_success")
                });
                var icon1 = iconForm.getItemValue("icon1");
	    		if (icon1) {
	    			iconForm.uncheckItem("icon1", icon1);
	    		}
	    		var icon2 = iconForm.getItemValue("icon2");
	    		if (icon2) {
	    			iconForm.uncheckItem("icon2", icon2);
	    		}
            });
        } else if (id == "save") {
        	var url = MODEL_URL + "!saveMenuIcon.json?menuId=" + currentMenuId;
    		var icon1 = iconForm.getItemValue("icon1");
    		if (icon1) {
    			url += "&icon1=" + icon1;
    		}
    		var icon2 = iconForm.getItemValue("icon2");
    		if (icon2) {
    			url += "&icon2=" + icon2;
    		}
            dhtmlxAjax.get(url, function(loader) {
                dhtmlx.message({
                    text : getMessage("save_success")
                });
            });
        } else if (id == "close") {
        	iconWin.close();
        }
    });
}
/**
 * 初始化快捷菜单图标表单工具条
 */
function initQuickIconToolBar(toolBar, iconWin, iconForm) {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.setAlign("right");
    toolBar.addButton("clear", 0, "&nbsp;&nbsp;取消快捷图标设置&nbsp;&nbsp;");
    toolBar.addSeparator("septr$01", 1);
    toolBar.addButton("save", 2, "&nbsp;&nbsp;保存&nbsp;&nbsp;");
    toolBar.addSeparator("septr$01", 3);
    toolBar.addButton("close", 4, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "clear") {
        	var url = MODEL_URL + "!cancelQuickMenuIcon.json?id=" + currentMenuId;
            dhtmlxAjax.get(url, function(loader) {
                dhtmlx.message({
                    text : getMessage("operate_success")
                });
                var quickIcon = iconForm.getItemValue("quickIcon");
	    		if (quickIcon) {
	    			iconForm.uncheckItem("quickIcon", quickIcon);
	    		}
            });
        } else if (id == "save") {
        	var url = MODEL_URL + "!saveQuickMenuIcon.json?id=" + currentMenuId;
    		var quickIcon = iconForm.getItemValue("quickIcon");
    		if (quickIcon) {
    			url += "&quickIcon=" + encodeURIComponent(quickIcon);
    		}
            dhtmlxAjax.get(url, function(loader) {
                dhtmlx.message({
                    text : getMessage("save_success")
                });
            });
        } else if (id == "close") {
        	iconWin.close();
        }
    });
}
function enableToolBar() {
    toolBar.enableItem("add");
    toolBar.enableItem("config");
    toolBar.enableItem("setupIcon");
    toolBar.enableItem("setupQuickIcon");
    toolBar.enableItem("delete");
    toolBar.enableItem("preview");
    toolBar.enableItem("refresh");
}
function disableToolBar() {
    toolBar.disableItem("add");
    toolBar.disableItem("config");
    toolBar.disableItem("setupIcon");
    toolBar.disableItem("setupQuickIcon");
    toolBar.disableItem("delete");
    toolBar.disableItem("preview");
    toolBar.disableItem("refresh");
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
	tree.setXMLAutoLoading(MODEL_URL + "!tree.json?E_model_name=tree&F_in=name,hasChild&P_UD=rootMenuId,bindingType&P_filterId=parentId&P_orders=showOrder");
	var treeJson = {id:0, item:[{id:-1,text:"菜单", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif",item:[]}]};
	tree.loadJSONObject(treeJson);
	tree.refreshItem("-1");
	tree.enableDragAndDrop(true, false);
    tree.setDragBehavior("complex", true);
    tree.attachEvent("onBeforeDrag", function(sId){
        if (sId.indexOf("sys") != -1) {
            dhtmlx.message("系统配置平台本身菜单不能拖动！");
            return false;
        }
        return true;
    });
    tree.attachEvent("onDrag", function(sId, tId, id, sObject, tObject) {
        if (tree.getParentId(sId) == "-1" && tId != "-1") {
            dhtmlx.message("系统级菜单只能在根节点下拖动！");
            return false;
        } else if (tree.getParentId(sId) != "-1" && tId == "-1") {
            dhtmlx.message("不能将菜单变成系统级菜单！");
            return false;
        }
        var sBindingType = tree.getUserData(sId, "bindingType");
        if ((sBindingType === "0" || sBindingType === "1") && tree.getParentId(tId) == "-1") {
            dhtmlx.message("绑定构件或URL的菜单不能拖动到系统菜单下！");
            return false;
        }
        return true;
    });
    tree.attachEvent("onDrop", function(sId, tId, id) {
        var url = MODEL_URL + "!treeSort.json?start=" + sId + "&targetId=" + tId;
        if (id) {
            url += "&end=" + id;
        }
        var result = loadJson(url);
        dhtmlx.message(result);
        tree.selectItem(tId, true);
    });
	tree.attachEvent("onClick", function(id) {
		currentTreeNodeId = "" + id;
		if (id == "-1") {
			rootMenuId = "";
		} else if (this.getParentId(id) == "-1") {
			rootMenuId = id;
		} else {
			rootMenuId = this.getUserData(id, "rootMenuId");
		}
		dhxLayout.cells("b").hideHeader();
		if (!toolBar) {
			toolBar = dhxLayout.cells("b").attachToolbar();
		    statusBar = dhxLayout.cells("b").attachStatusBar();
		    initSelfToolBar();
	    }
	    if (!dataGrid) {
		    dataGrid = dhxLayout.cells("b").attachGrid();
		    initSelfGrid();
	    }
	    if (currentTreeNodeId.indexOf('sys') != -1) {
	    	disableToolBar();
	    } else {
	    	enableToolBar();
	    }
	    if (id == -1) {
		    if (releasedSystem) {
                disableToolBar();
            } else {
                enableToolBar();
            }
            toolBar.showItem("preview");
		} else {
		    toolBar.hideItem("preview");
		}
		refreshMenuGrid();
	});
	tree.selectItem("-1", true);
}